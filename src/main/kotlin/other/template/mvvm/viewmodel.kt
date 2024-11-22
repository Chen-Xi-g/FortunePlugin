package other.template.mvvm

fun viewModelKt(
    packageName: String,
    viewModelName: String,
    desc: String
) = """
package $packageName

import com.fortune.library.mvvm.viewmodel.BaseVM
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * $desc ViewModel
 */
class $viewModelName : BaseVM() {

}
""".trimIndent()