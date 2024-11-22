package other.template

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import other.notify.NotifyAction
import other.template.mvvm.fragmentKt
import other.template.mvvm.fragmentListKt
import other.template.mvvm.viewModelKt
import other.template.res.layout.defaultLayoutKt
import other.template.res.layout.defaultListItemLayout
import other.template.res.layout.defaultListLayout
import java.io.File
import java.util.Locale

/**
 * 一键创建 MVVM Fragment 规范文件
 *
 * @param moduleData 模块数据
 * @param fragmentName Fragment 业务名称, 如 Main会生成MainFragment.kt和fragment_main.xml
 * @param fragmentDesc Fragment 描述
 * @param generateLayout 是否生成布局文件
 * @param layoutName 布局名称
 * @param generateViewModel 是否生成ViewModel文件
 * @param viewModelName ViewModel名称
 * @param generateList 是否生成列表
 * @param packageName 包名
 */
@OptIn(ExperimentalStdlibApi::class)
fun RecipeExecutor.fragmentRecipe(
    moduleData: ModuleTemplateData,
    fragmentName: String,
    fragmentDesc: String,
    generateLayout: Boolean,
    layoutName: String,
    generateViewModel: Boolean,
    viewModelName: String,
    generateList: Boolean,
    packageName: String
){

    // 获取模块数据: 项目数据, 代码输出路径, 资源输出路径, Manifest输出路径
    val (projectData, srcOut, resOut) = moduleData

    // 应用包名
    val rootPackageName = moduleData.projectTemplateData.applicationPackage

    // 获取ViewBinding名称
    val viewBindingName = layoutName.split("_").joinToString(""){ word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    } + "Binding"

    val activityName = if (fragmentName.lowercase().contains("fragment")) fragmentName else "${fragmentName}Fragment"

    val activityPackageName = "${packageName}.ui.fragment"
    val viewModelPackageName = "${packageName}.ui.viewmodel"
    val activityFileDir = "ui/fragment/"
    val viewModelFileDir = "ui/viewmodel/"

    val itemLayoutName = layoutName.lowercase().replace("fragment", "item")
    val itemViewBindingName = itemLayoutName.split("_").joinToString(""){ word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    } + "Binding"

    val notifyList = mutableListOf<File>()

    // 生成Fragment文件
    if (generateList){
        val fragmentFile = srcOut.resolve(activityFileDir + "${activityName}.kt")
        notifyList.add(fragmentFile)
        save(
            fragmentListKt(
                rootPackageName = rootPackageName ?: "",
                packageName = activityPackageName,
                fragmentName = activityName,
                fragmentDesc = fragmentDesc,
                viewBindingName = viewBindingName,
                itemViewBindingName = itemViewBindingName,
                viewModelName = viewModelName,
                viewModelPackageName = viewModelPackageName
            ), fragmentFile
        )
    }else{
        val fragmentFile = srcOut.resolve(activityFileDir + "${activityName}.kt")
        notifyList.add(fragmentFile)
        save(
            fragmentKt(
                rootPackageName = rootPackageName ?: "",
                packageName = activityPackageName,
                fragmentName = activityName,
                fragmentDesc = fragmentDesc,
                viewBindingName = viewBindingName,
                viewModelName = viewModelName,
                viewModelPackageName = viewModelPackageName
            ), fragmentFile
        )
    }

    // 生成ViewModel文件
    if (generateViewModel){
        val viewModelFile = srcOut.resolve(viewModelFileDir + "${viewModelName}.kt")
        notifyList.add(viewModelFile)
        save(
            viewModelKt(
                packageName = viewModelPackageName,
                viewModelName = viewModelName,
                desc = fragmentDesc

            ),
            viewModelFile
        )
    }

    // 生成布局文件
    if (generateLayout){
        if (generateList){
            val fragmentLayoutFile = resOut.resolve("layout/${layoutName}.xml")
            val fragmentItemLayoutFile = resOut.resolve("layout/${itemLayoutName}.xml")
            notifyList.add(fragmentLayoutFile)
            notifyList.add(fragmentItemLayoutFile)
            save(
                defaultListLayout(itemLayoutName),
                fragmentLayoutFile
            )
            save(
                defaultListItemLayout(),
                fragmentItemLayoutFile
            )
        }else{
            val fragmentLayoutFile = resOut.resolve("layout/${layoutName}.xml")
            notifyList.add(fragmentLayoutFile)
            save(
                defaultLayoutKt(),
                fragmentLayoutFile
            )
        }
    }

    NotifyAction.notifyFileSuccess(
        2,
        notifyList
    )
}