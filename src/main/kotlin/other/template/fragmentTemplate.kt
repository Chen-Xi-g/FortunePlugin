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
import com.android.tools.idea.wizard.template.booleanParameter
import com.android.tools.idea.wizard.template.fragmentToLayout
import com.android.tools.idea.wizard.template.impl.activities.common.MIN_API
import com.android.tools.idea.wizard.template.impl.defaultPackageNameParameter
import com.android.tools.idea.wizard.template.stringParameter
import com.android.tools.idea.wizard.template.template
import java.util.Locale

@OptIn(ExperimentalStdlibApi::class)
val fragmentTemplate
    get() = template {
        name = "Android MVVM Fragment"
        description = "一键创建 MVVM Fragment 规范文件"
        minApi = MIN_API
        category = Category.Fragment
        formFactor = FormFactor.Mobile
        screens = listOf(
            WizardUiContext.FragmentGallery,
            WizardUiContext.MenuEntry
        )

        val fragmentName = stringParameter {
            name = "Fragment Name"
            default = "MainFragment"
            constraints = listOf(Constraint.UNIQUE, Constraint.NONEMPTY) // 唯一, 非空
            help = "Fragment 名称, 生成MainFragment.kt和fragment_main.xml"
        }

        val fragmentDesc = stringParameter {
            name = "Fragment Description"
            default = "主页"
            constraints = listOf(Constraint.NONEMPTY)
            help = "Fragment 描述"
        }

        val generateLayout = booleanParameter {
            name = "Generate a Layout File"
            default = true
            help = "是否生成布局文件"
        }

        val layoutName = stringParameter {
            name = "Layout Name"
            default = "fragment_main"
            constraints = listOf(Constraint.LAYOUT, Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { fragmentToLayout(fragmentName.value) }
            visible = { generateLayout.value }
        }

        val generateViewModel = booleanParameter {
            name = "Generate a ViewModel File"
            default = true
            help = "是否生成ViewModel文件"
        }

        val viewModelName = stringParameter {
            name = "ViewModel Name"
            default = "MainViewModel"
            constraints = listOf(Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { "${fragmentName.value.replace("Fragment", "").replaceFirstChar { it.uppercase(Locale.ROOT) }}ViewModel" }
            visible = { generateViewModel.value }
        }

        val generateList = booleanParameter {
            name = "Generate a List Fragment"
            default = false
            help = "生成的Fragment是否是列表"
        }

        // Package name
        val packageName = defaultPackageNameParameter

        widgets(
            TextFieldWidget(fragmentName),
            TextFieldWidget(fragmentDesc),
            CheckBoxWidget(generateLayout),
            TextFieldWidget(layoutName),
            CheckBoxWidget(generateViewModel),
            TextFieldWidget(viewModelName),
            CheckBoxWidget(generateList),
            PackageNameWidget(packageName)
        )

        recipe = { data: TemplateData ->
            fragmentRecipe(
                data as ModuleTemplateData,
                fragmentName.value,
                fragmentDesc.value,
                generateLayout.value,
                layoutName.value,
                generateViewModel.value,
                viewModelName.value,
                generateList.value,
                packageName.value
            )
        }
    }