package other.template

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.intellij.openapi.project.ProjectManager
import other.notify.NotifyAction
import other.template.androidManifest.manifestTemplateXml
import other.template.mvvm.viewModelKt
import other.template.mvvm.viewPager2Activity
import other.template.mvvm.viewPager2Adapter
import other.template.mvvm.viewPager2Fragment
import other.template.mvvm.viewPager2ListFragment
import other.template.res.layout.defaultLayoutKt
import other.template.res.layout.defaultListItemLayout
import other.template.res.layout.defaultListLayout
import other.template.res.layout.viewPager2ActivityLayout
import java.io.File
import java.util.Locale

/**
 * 一键创建 MVVM ViewPager2 规范文件
 *
 * @param moduleData 模块数据
 * @param viewPager2Name ViewPager2 业务名称
 * @param viewPager2Desc ViewPager2 描述
 * @param generateViewModel 是否生成ViewModel文件
 * @param viewModelName ViewModel名称
 * @param generateAdapter 是否生成Adapter文件
 * @param packageName 包名
 */
@OptIn(ExperimentalStdlibApi::class)
fun RecipeExecutor.viewpagerRecipe(
    moduleData: ModuleTemplateData,
    viewPager2Name: String,
    viewPager2Desc: String,
    activityLayoutName: String,
    fragmentLayoutName: String,
    generateListFragment: Boolean,
    generateViewModel: Boolean,
    viewModelName: String,
    generateAdapter: Boolean,
    packageName: String
) {
    // 获取模块数据: 项目数据, 代码输出路径, 资源输出路径, Manifest输出路径
    val (projectData, srcOut, resOut, manifestOut) = moduleData

    // 应用包名
    val rootPackageName = moduleData.projectTemplateData.applicationPackage ?: ""

    // ViewPager2 Activity ViewBinding 名称
    val viewPager2ActivityViewBindingName = activityLayoutName.split("_").joinToString("") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    } + "Binding"

    // ViewPager2 Fragment ViewBinding 名称
    val viewPager2FragmentViewBindingName = fragmentLayoutName.split("_").joinToString("") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    } + "Binding"

    // ViewPager2 Fragment List Item ViewBinding 名称
    val viewPager2FragmentItemViewBindingName =
        viewPager2FragmentViewBindingName.replace("Fragment", "Item")

    val activityName =
        viewPager2Name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } + "ViewPageActivity"

    val fragmentName =
        viewPager2Name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } + "ViewPagerFragment"

    val adapterName =
        viewPager2Name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } + "ViewPagerAdapter"

    val fragmentItemLayoutName = fragmentLayoutName.replace("fragment", "item")

    val viewPager2PackageName = "${packageName}.ui.viewpager.${viewPager2Name.lowercase()}"
    val viewPager2FileDir = "ui/viewpager/${viewPager2Name.lowercase()}/"

    val notifyList = mutableListOf<File>()

    // 生成AndroidManifest.xml
    mergeXml(
        manifestTemplateXml(
            viewPager2PackageName.replace(rootPackageName, "") + ".",
            activityName
        ),
        manifestOut.resolve("AndroidManifest.xml")
    )

    // 生成Activity
    val activityFile = srcOut.resolve("$viewPager2FileDir$activityName.kt")
    notifyList.add(activityFile)
    save(
        viewPager2Activity(
            rootPackageName,
            viewPager2PackageName,
            activityName,
            viewPager2Desc,
            adapterName,
            viewPager2ActivityViewBindingName,
            viewModelName
        ),
        activityFile
    )

    // 生成Fragment
    if (generateListFragment) {
        val listFragmentFile = srcOut.resolve("$viewPager2FileDir$fragmentName.kt")
        notifyList.add(listFragmentFile)
        save(
            viewPager2ListFragment(
                rootPackageName,
                viewPager2PackageName,
                fragmentName,
                viewPager2Desc,
                viewPager2FragmentViewBindingName,
                viewPager2FragmentItemViewBindingName,
                viewModelName
            ),
            listFragmentFile
        )
    } else {
        val fragmentFile = srcOut.resolve("$viewPager2FileDir$fragmentName.kt")
        notifyList.add(fragmentFile)
        save(
            viewPager2Fragment(
                rootPackageName,
                viewPager2PackageName,
                fragmentName,
                viewPager2Desc,
                viewPager2FragmentViewBindingName,
                viewModelName
            ),
            fragmentFile
        )
    }

    // 生成ViewModel
    if (generateViewModel) {
        val viewModelFile = srcOut.resolve("$viewPager2FileDir$viewModelName.kt")
        notifyList.add(viewModelFile)
        save(
            viewModelKt(
                packageName + ".ui.viewpager.${viewPager2Name.lowercase()}",
                viewModelName,
                viewPager2Desc
            ),
            viewModelFile
        )
    }

    // 生成Adapter
    if (generateAdapter) {
        val adapterFile = srcOut.resolve(
            "$viewPager2FileDir$adapterName.kt"
        )
        notifyList.add(adapterFile)
        save(
            viewPager2Adapter(
                packageName + ".ui.viewpager.${viewPager2Name.lowercase()}",
                adapterName,
                fragmentName,
                viewPager2Desc,
                generateListFragment
            ),
            adapterFile
        )
    }

    // 生成Activity Layout
    val activityLayoutFile = resOut.resolve("layout/${activityLayoutName}.xml")
    notifyList.add(activityLayoutFile)
    save(
        viewPager2ActivityLayout(),
        activityLayoutFile
    )

    // 生成Fragment Layout
    if (generateListFragment) {
        val listLayoutFile = resOut.resolve("layout/$fragmentLayoutName.xml")
        val listItemLayoutFile = resOut.resolve("layout/$fragmentItemLayoutName.xml")
        notifyList.add(listLayoutFile)
        notifyList.add(listItemLayoutFile)
        save(
            defaultListLayout(fragmentItemLayoutName),
            listLayoutFile
        )
        save(
            defaultListItemLayout(),
            listItemLayoutFile
        )
    } else {
        val fragmentLayoutFile = resOut.resolve("layout/$fragmentLayoutName.xml")
        notifyList.add(fragmentLayoutFile)
        save(
            defaultLayoutKt(),
            fragmentLayoutFile
        )
    }

    NotifyAction.notifyFileSuccess(
        3,
        notifyList
    )

}