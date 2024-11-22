package other.template

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import other.notify.NotifyAction
import other.template.androidManifest.manifestTemplateXml
import other.template.mvvm.activityKt
import other.template.mvvm.activityListKt
import other.template.mvvm.viewModelKt
import other.template.res.layout.defaultLayoutKt
import other.template.res.layout.defaultListItemLayout
import other.template.res.layout.defaultListLayout
import java.io.File
import java.util.Locale

/**
 * 一键创建 MVVM Activity 规范文件
 *
 * @param moduleData 模块数据
 * @param activityName Activity 业务名称, 如 Main会生成MainActivity.kt和activity_main.xml
 * @param activityDesc Activity 描述
 * @param generateLayout 是否生成布局文件
 * @param layoutName 布局名称
 * @param generateViewModel 是否生成ViewModel文件
 * @param viewModelName ViewModel名称
 * @param generateList 是否生成列表
 * @param packageName 包名
 */
@OptIn(ExperimentalStdlibApi::class)
fun RecipeExecutor.activityRecipe(
    moduleData: ModuleTemplateData,
    activityName: String,
    activityDesc: String,
    generateLayout: Boolean,
    layoutName: String,
    generateViewModel: Boolean,
    viewModelName: String,
    generateList: Boolean,
    packageName: String
){

    // 获取模块数据: 项目数据, 代码输出路径, 资源输出路径, Manifest输出路径
    val (projectData, srcOut, resOut, manifestOut) = moduleData

    // 应用包名
    val rootPackageName = moduleData.projectTemplateData.applicationPackage

    // 获取ViewBinding名称
    val viewBindingName = layoutName.split("_").joinToString(""){ word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    } + "Binding"

    val activityName = if (activityName.lowercase().contains("activity")) activityName else "${activityName}Activity"

    val activityPackageName = "${packageName}.ui.activity"
    val viewModelPackageName = "${packageName}.ui.viewmodel"
    val activityFileDir = "ui/activity/"
    val viewModelFileDir = "ui/viewmodel/"

    val itemLayoutName = layoutName.lowercase().replace("activity", "item")
    val itemViewBindingName = itemLayoutName.split("_").joinToString(""){ word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    } + "Binding"

    val notifyList = mutableListOf<File>()

    // 生成AndroidManifest.xml
    mergeXml(
        manifestTemplateXml(packageName.replace(rootPackageName?:"","") + ".ui.activity.",activityName),
        manifestOut.resolve("AndroidManifest.xml")
    )

    // 生成Activity文件
    if (generateList){
        val activityFile = srcOut.resolve(activityFileDir + "${activityName}.kt")
        notifyList.add(activityFile)
        save(
            activityListKt(
                rootPackageName = rootPackageName ?: "",
                packageName = activityPackageName,
                activityName = activityName,
                activityDesc = activityDesc,
                viewBindingName = viewBindingName,
                itemViewBindingName = itemViewBindingName,
                viewModelName = viewModelName,
                viewModelPackageName = viewModelPackageName
            ), activityFile
        )
    }else{
        val activityFile = srcOut.resolve(activityFileDir + "${activityName}.kt")
        notifyList.add(activityFile)
        save(
            activityKt(
                rootPackageName = rootPackageName ?: "",
                packageName = activityPackageName,
                activityName = activityName,
                activityDesc = activityDesc,
                viewBindingName = viewBindingName,
                viewModelName = viewModelName,
                viewModelPackageName = viewModelPackageName
            ), activityFile
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
                desc = activityDesc

            ),
            viewModelFile
        )
    }

    // 生成布局文件
    if (generateLayout){
        if (generateList){
            val activityLayoutFile = resOut.resolve("layout/${layoutName}.xml")
            val activityItemLayoutFile = resOut.resolve("layout/${itemLayoutName}.xml")
            notifyList.add(activityLayoutFile)
            notifyList.add(activityItemLayoutFile)
            save(
                defaultListLayout(itemLayoutName),
                activityLayoutFile
            )
            save(
                defaultListItemLayout(),
                activityItemLayoutFile
            )
        }else{
            val activityLayoutFile = resOut.resolve("layout/${layoutName}.xml")
            notifyList.add(activityLayoutFile)
            save(
                defaultLayoutKt(),
                activityLayoutFile
            )
        }
    }

    NotifyAction.notifyFileSuccess(
        1,
        notifyList
    )
}