package other.template.mvvm

fun activityKt(
    rootPackageName: String,
    packageName: String,
    activityName: String,
    activityDesc: String,
    viewBindingName: String,
    viewModelName: String,
    viewModelPackageName: String
) = """
package $packageName

import android.os.Bundle
import androidx.activity.viewModels
import $rootPackageName.databinding.$viewBindingName
import $viewModelPackageName.$viewModelName
import com.fortune.library.mvvm.activity.BaseActivity

/**
 * $activityDesc 页面
 */
class $activityName : BaseActivity<$viewBindingName>() {

    /**
     * 初始化ViewModel
     */
    override val viewModel: $viewModelName by viewModels()

    /**
     * 设置标题栏是否显示
     */
    override val isShowTitle: Boolean = true

    /**
     * 在initView中执行View初始化的任务
     */
    override fun initView(savedInstanceState: Bundle?) {
        setTitle("$activityDesc")
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

fun activityListKt(
    rootPackageName: String,
    packageName: String,
    activityName: String,
    activityDesc: String,
    viewBindingName: String,
    itemViewBindingName: String,
    viewModelName: String,
    viewModelPackageName: String
) = """
package $packageName

import android.os.Bundle
import androidx.activity.viewModels
import $viewModelPackageName.$viewModelName
import $rootPackageName.databinding.$viewBindingName
import $rootPackageName.databinding.$itemViewBindingName
import com.fortune.library.mvvm.activity.BaseListActivity
import com.fortune.library.mvvm.rv.linear
import com.fortune.library.mvvm.rv.setup

/**
 * $activityDesc 页面
 */
class $activityName : BaseListActivity<$viewBindingName>() {

    /**
     * 初始化ViewModel
     */
    override val viewModel: $viewModelName by viewModels()

    /**
     * 设置标题栏是否显示
     */
    override val isShowTitle: Boolean = true

    /**
     * 在initView中执行View初始化的任务
     */
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setTitle("$activityDesc")
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