package other.template

import com.android.tools.idea.wizard.template.Template
import com.android.tools.idea.wizard.template.WizardTemplateProvider

class CreateMVVMTemplateProviderImpl : WizardTemplateProvider(){
    override fun getTemplates(): List<Template> = listOf(
        activityTemplate,
        fragmentTemplate,
        viewpagerTemplate
    )
}