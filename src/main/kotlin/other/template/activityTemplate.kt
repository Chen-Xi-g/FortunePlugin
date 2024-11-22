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
import com.android.tools.idea.wizard.template.impl.activities.common.MIN_API
import com.android.tools.idea.wizard.template.impl.defaultPackageNameParameter
import com.android.tools.idea.wizard.template.stringParameter
import com.android.tools.idea.wizard.template.template
import java.util.Locale

@OptIn(ExperimentalStdlibApi::class)
val activityTemplate
    get() = template {
        name = "Android MVVM Activity"
        description = "一键创建 MVVM Activity 规范文件"
        minApi = MIN_API
        category = Category.Activity
        formFactor = FormFactor.Mobile
        screens = listOf(
            WizardUiContext.ActivityGallery,
            WizardUiContext.MenuEntry
        )

        val activityName = stringParameter {
            name = "Activity Name"
            default = "MainActivity"
            constraints = listOf(Constraint.UNIQUE, Constraint.NONEMPTY) // 唯一, 非空
            help = "Activity 名称, 生成MainActivity.kt和activity_main.xml"
        }

        val activityDesc = stringParameter {
            name = "Activity Description"
            default = "主页"
            constraints = listOf(Constraint.NONEMPTY)
            help = "Activity 描述"
        }

        val generateLayout = booleanParameter {
            name = "Generate a Layout File"
            default = true
            help = "是否生成布局文件"
        }

        val layoutName = stringParameter {
            name = "Layout Name"
            default = "activity_main"
            constraints = listOf(Constraint.LAYOUT, Constraint.UNIQUE, Constraint.NONEMPTY)
            suggest = { activityToLayout(activityName.value) }
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
            suggest = { "${activityName.value.replace("Activity", "").replaceFirstChar { it.uppercase(Locale.ROOT) }}ViewModel" }
            visible = { generateViewModel.value }
        }

        val generateList = booleanParameter {
            name = "Generate a List Activity"
            default = false
            help = "生成的Activity是否是列表"
        }

        // Package name
        val packageName = defaultPackageNameParameter

        widgets(
            TextFieldWidget(activityName),
            TextFieldWidget(activityDesc),
            CheckBoxWidget(generateLayout),
            TextFieldWidget(layoutName),
            CheckBoxWidget(generateViewModel),
            TextFieldWidget(viewModelName),
            CheckBoxWidget(generateList),
            PackageNameWidget(packageName)
        )

        recipe = { data: TemplateData ->
            activityRecipe(
                data as ModuleTemplateData,
                activityName.value,
                activityDesc.value,
                generateLayout.value,
                layoutName.value,
                generateViewModel.value,
                viewModelName.value,
                generateList.value,
                packageName.value
            )
        }
    }