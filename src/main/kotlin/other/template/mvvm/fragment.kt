package other.template.mvvm

fun fragmentKt(
    rootPackageName: String,
    packageName: String,
    fragmentName: String,
    fragmentDesc: String,
    viewBindingName: String,
    viewModelName: String,
    viewModelPackageName: String
) = """
package $packageName

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import $viewModelPackageName.$viewModelName
import $rootPackageName.databinding.FragmentTestBinding
import com.fortune.library.mvvm.fragment.BaseFragment

/**
 * $fragmentDesc 页面
 */
class $fragmentName : BaseFragment<$viewBindingName>() {

    /**
     * 初始化ViewModel
     */
    override val viewModel: $viewModelName by viewModels()

    /**
     * 在initView中执行View初始化的任务
     */
    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    /**
     * 在obtainData中做数据的初始化
     */
    override fun obtainData() {
    }

    /**
     * 在registerObserver中注册观察者
     */
    override fun registerObserver() {
        super.registerObserver()
    }
}
""".trimIndent()

fun fragmentListKt(
    rootPackageName: String,
    packageName: String,
    fragmentName: String,
    fragmentDesc: String,
    viewBindingName: String,
    itemViewBindingName: String,
    viewModelName: String,
    viewModelPackageName: String
) = """
package $packageName

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import $rootPackageName.databinding.$viewBindingName
import $rootPackageName.databinding.$itemViewBindingName
import $viewModelPackageName.$viewModelName
import com.fortune.library.mvvm.fragment.BaseListFragment
import com.fortune.library.mvvm.rv.linear
import com.fortune.library.mvvm.rv.setup

/**
 * $fragmentDesc 页面
 */
class $fragmentName : BaseListFragment<$viewBindingName>() {

    /**
     * 初始化ViewModel
     */
    override val viewModel: $viewModelName by viewModels()

    /**
     * 在initView中执行View初始化的任务
     */
    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        initAdapter()
    }

    /**
     * 分页加载数据, page为当前页数
     */
    override fun loadData(page: Int) {
    }

    /**
     * 在obtainData中做数据的初始化
     */
    override fun obtainData() {
    }

    /**
     * 在registerObserver中注册观察者
     */
    override fun registerObserver() {
        super.registerObserver()
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        recyclerView.linear()
            .setup<String, $itemViewBindingName> {
                onBind {
                }
            }
    }

}
""".trimIndent()