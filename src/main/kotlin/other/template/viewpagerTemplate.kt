package other.template

import com.android.tools.idea.wizard.template.Category
import com.android.tools.idea.wizard.template.CheckBoxWidget
import com.android.tools.idea.wizard.template.Constraint
import com.android.tools.idea.wizard.template.FormFactor
import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.PackageNameWidget
import com.android.tools.idea.wizard.template.TemplateData
import com.android.tools.idea.wizard.template.TextFieldWidget
import com.android.tools.idea.wizard.template.WizardUiContext
import com.android.tools.idea.wizard.template.activityToLayout
import com.android.tools.idea.wizard.template.booleanParameter
import com.android.tools.idea.wizard.template.fragmentToLayout
import com.android.tools.idea.wizard.template.impl.activities.common.MIN_API
import com.android.tools.idea.wizard.template.impl.defaultPackageNameParameter
import com.android.tools.idea.wizard.template.stringParameter
import com.android.tools.idea.wizard.template.template
import java.util.Locale

@OptIn(ExperimentalStdlibApi::class)
val viewpagerTemplate
    get() = template {
        name = "Android MVVM ViewPager2"
        description = "一键创建 MVVM ViewPager2 规范文件"
        minApi = MIN_API
        category = Category.Other
        formFactor = FormFactor.Mobile
        screens = listOf(
            WizardUiContext.ActivityGallery,
            WizardUiContext.FragmentGallery,
            WizardUiContext.MenuEntry
        )

        val viewPager2Name = stringParameter {
            name = "ViewPager2 Name"
            default = "Main"
            constraints = listOf(Constraint.NONEMPTY) // 唯一, 非空
            help = "ViewPager2 名称, 生成Activity/ViewPager2 -> Fragment"
        }

        val viewPager2Desc = stringParameter {
            name = "ViewPager2 Description"
            default = "首页ViewPager2"
            constraints = listOf(Constraint.NONEMPTY)
            help = "ViewPager2 描述"
        }

        val generateAdapter = booleanParameter {
            name = "Generate a ViewPager2 Adapter File"
            default = true
            help = "是否生成 ViewPager2 Adapter 文件"
        }

        val activityLayoutName = stringParameter {
            name = "Activity Layout Name"
            default = "activity_main"
            constraints = listOf(Constraint.LAYOUT, Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { activityToLayout("Viewpager" + viewPager2Name.value) }
        }

        val fragmentLayoutName = stringParameter {
            name = "Fragment Layout Name"
            default = "fragment_main"
            constraints = listOf(Constraint.LAYOUT, Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { fragmentToLayout("Viewpager" + viewPager2Name.value) }
        }

        val generateListFragment = booleanParameter {
            name = "Generate a List Fragment"
            default = false
            help = "是否生成列表Fragment"
        }

        val generateViewModel = booleanParameter {
            name = "Generate a ViewModel File"
            default = true
            help = "是否生成ViewModel文件"
        }

        val viewModelName = stringParameter {
            name = "ViewModel Name"
            default = "MainViewModel"
            constraints = listOf(Constraint.NONEMPTY)
            suggest = { "${viewPager2Name.value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}ViewPagerViewModel" }
            visible = { generateViewModel.value }
        }

        // Package name
        val packageName = defaultPackageNameParameter

        widgets(
            TextFieldWidget(viewPager2Name),
            TextFieldWidget(viewPager2Desc),
            TextFieldWidget(activityLayoutName),
            TextFieldWidget(fragmentLayoutName),
            CheckBoxWidget(generateListFragment),
            CheckBoxWidget(generateViewModel),
            TextFieldWidget(viewModelName),
            CheckBoxWidget(generateAdapter),
            PackageNameWidget(packageName)
        )

        recipe = { data: TemplateData ->
            viewpagerRecipe(
                data as ModuleTemplateData,
                viewPager2Name.value,
                viewPager2Desc.value,
                activityLayoutName.value,
                fragmentLayoutName.value,
                generateListFragment.value,
                generateViewModel.value,
                viewModelName.value,
                generateAdapter.value,
                packageName.value
            )
        }
    }