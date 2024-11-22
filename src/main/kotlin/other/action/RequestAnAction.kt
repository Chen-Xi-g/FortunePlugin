package other.action

import com.android.tools.idea.insights.androidAppId
import com.android.utils.findGradleBuildFile
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.util.PsiTreeUtil
import other.dialog.RequestDialog
import other.template.mvvm.requestImportKt
import other.template.mvvm.requestKt
import wu.seal.jsontokotlin.filetype.KotlinFileType
import wu.seal.jsontokotlin.library.JsonToKotlinBuilder
import wu.seal.jsontokotlin.model.DefaultValueStrategy
import wu.seal.jsontokotlin.model.PropertyTypeStrategy
import wu.seal.jsontokotlin.model.TargetJsonConverter
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Locale

/**
 * 通过JSON生成请求函数和实体类
 */
class RequestAnAction : AnAction("生成请求函数和实体类") {

    private var number: Int = 0

    @OptIn(ExperimentalStdlibApi::class)
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.apply {
            val path = getPath(event)
            val editor: Editor = event.getRequiredData(CommonDataKeys.EDITOR)
            val psiFile: PsiFile = event.getRequiredData(CommonDataKeys.PSI_FILE)
            val applicationId: String = com.intellij.openapi.module.ModuleUtil.findModuleForFile(psiFile)?.androidAppId ?: return
            // 打开自定义对话框
            val dialog = RequestDialog(path)
            if (dialog.showAndGet()) {
                // 确定按钮
                val httpType = dialog.getHttpType()
                val className = dialog.getMethodName()
                val url = dialog.getApiPath()
                val json = dialog.getJsonBody()
                val params = dialog.getParams()
                val isList = dialog.isList()

                insertGlobalVariable(
                    psiFile,
                    applicationId,
                    json,
                    className,
                    params,
                    className.replaceFirstChar { if (it.isLowerCase()) it.lowercase(Locale.ROOT) else it.toString() },
                    httpType,
                    url,
                    isList
                )
            }
        }
    }

    private fun getPath(e: AnActionEvent): String {
        val psiFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        return psiFile?.name ?: ""
    }

    // 获取当前文件中的类
    private fun getPsiClass(psiFile: PsiFile): PsiClass? {
        return PsiTreeUtil.findChildOfAnyType(psiFile, PsiClass::class.java)
    }

    // 插入全局变量到类的最前面
    private fun insertGlobalVariable(
        psiFile: PsiFile,
        applicationId: String,
        json: String,
        className: String,
        params: List<Pair<String, String>>,
        dataName: String,
        httpType: String,
        url: String,
        isList: Boolean
    ) {
        val project = psiFile.project
        val factory = JavaPsiFacade.getElementFactory(project)

        val classPackageName = ".model.$className"

        // 生成实体类
        val actualOutPut = JsonToKotlinBuilder()
            .setPackageName("$applicationId.model")
            .setPropertyTypeStrategy(PropertyTypeStrategy.NotNullable)
            .setDefaultValueStrategy(DefaultValueStrategy.AvoidNull)
            .setAnnotationLib(TargetJsonConverter.Serializable)
            .enableInnerClassModel(true)
            .build(json, className)

        createClassFile(project, applicationId, className, actualOutPut)

        // 导包
        val importText = requestImportKt(classPackageName, httpType)
        val import = factory.createImportStatementOnDemand(importText)
//        javaFile.importList?.add(import)

        // 插入导包

        // 创建一个新的字段（全局变量）
        val fieldText = requestKt(className, dataName, isList)
        val field = factory.createFieldFromText(fieldText, psiFile)

        // 插入成员变量
        PsiFileFactory.getInstance(project).createFileFromText(field, KotlinFileType.INSTANCE, fieldText)
        if (fields.isNotEmpty()) {
            val firstField = fields.first()
            val parent = firstField.parent

            // 在类的最前面插入新字段
            parent.addBefore(field, firstField)
        } else {
            // 如果类没有字段，则将字段直接添加到类中
            val classBody = psiClass
            classBody.add(field)
        }

        // 插入请求函数
        val methodText = requestKt(className, params, dataName, httpType, url, isList)
        val method = factory.createMethodFromText(methodText, psiClass)

        // 插入方法, 在当前光标位置插入
        psiClass.add(method)
    }

    /**
     * 获取当前项目的包名
     */
    private fun getPackageName(psiClass: PsiClass): String? {
// 获取 Gradle 文件路径
        val gradleFile = findGradleBuildFile(File(psiClass.project.basePath)) // 根据项目根路径获取 gradle 文件
        return getPackageNameFromGradle(gradleFile)
    }

    // 从 build.gradle 文件中提取 applicationId
    private fun getPackageNameFromGradle(gradleFile: File): String? {
        val reader = BufferedReader(InputStreamReader(FileInputStream(gradleFile)))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            if (line?.contains("applicationId") == true) {
                // 提取 applicationId 中的值
                val regex = """applicationId\s+"([^"]+)"""".toRegex()
                val matchResult = regex.find(line)
                return matchResult?.groups?.get(1)?.value
            }
        }
        return null
    }

    /**
     * 创建实体类文件
     */
    private fun createClassFile(project: Project,rootPackageName: String, className: String, data: String) {
        val path = ("$rootPackageName.model").replace(".", "/")
        val targetDirectory: VirtualFile? = LocalFileSystem.getInstance().findFileByPath(path)
        if (targetDirectory == null){
            // 找到或创建父目录
            val directory = LocalFileSystem.getInstance().findFileByPath(rootPackageName.replace(".", "/"))
            directory?.createChildDirectory(null, "model")
            if (number == 5){
                number = 0
                return
            }
            number++
            createClassFile(project, rootPackageName, className, data)
        }else{
            val virtualFile = targetDirectory.createChildData(this, "$className.kt").apply {
                setBinaryContent(data.toByteArray())
            }
            LocalFileSystem.getInstance()
                .refreshAndFindFileByIoFile(virtualFile.toNioPath().toFile())

            FileEditorManager.getInstance(project).openFile(virtualFile, true)
        }


    }
}