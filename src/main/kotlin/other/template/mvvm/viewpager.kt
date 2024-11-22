package other.template.mvvm
fun viewPager2Activity(
    rootPackageName: String,
    packageName: String,
    activityName: String,
    activityDesc: String,
    adapterName: String,
    viewBindingName: String,
    viewModelName: String
) = """
package $packageName

import android.os.Bundle
import androidx.activity.viewModels
import $rootPackageName.databinding.$viewBindingName
import com.fortune.library.mvvm.activity.BaseActivity
import com.gyf.immersionbar.ImmersionBar

/**
 * $activityDesc 页面
 */
class $activityName : BaseActivity<$viewBindingName>(){

    /**
     * 初始化ViewModel
     */
    override val viewModel: $viewModelName by viewModels()

    /**
     * 设置标题栏是否显示
     */
    override val isShowTitle: Boolean = false
    
    /**
     * ViewPager2 适配器
     */
    private val vpAdapter by lazy {
        $adapterName(supportFragmentManager, lifecycle)
    }

    /**
     * 在initView中执行View初始化的任务
     */
    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.setStatusBarView(this, binding.vPadding)
        initAdapter()
    }
    
    /**
     * 在obtainData中做数据的初始化
     */
    override fun obtainData() {
    }
    
    /**
     * 初始化适配器
     */
    private fun initAdapter(){
        // 禁用滑动
//        binding.vp2.isUserInputEnabled = false
        // 缓存页面数量
//        binding.vp2.offscreenPageLimit = 2
        binding.vp2.adapter = vpAdapter
    }

}
""".trimIndent()


fun viewPager2Fragment(
    rootPackageName: String,
    packageName: String,
    fragmentName: String,
    fragmentDesc: String,
    viewBindingName: String,
    viewModelName: String
) = """
package $packageName

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import $rootPackageName.databinding.$viewBindingName
import com.fortune.library.mvvm.fragment.BaseFragment
/**
 * $fragmentDesc 页面
 */
class $fragmentName : BaseFragment<$viewBindingName>() {

    companion object{
    
        /**
         * 获取Fragment实例
         */
        fun getInstance() = DemoViewPagerFragment()
    }

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

fun viewPager2ListFragment(
    rootPackageName: String,
    packageName: String,
    fragmentName: String,
    fragmentDesc: String,
    viewBindingName: String,
    itemViewBindingName: String,
    viewModelName: String
) = """
package $packageName

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import $rootPackageName.databinding.$viewBindingName
import $rootPackageName.databinding.$itemViewBindingName
import com.fortune.library.mvvm.fragment.BaseListFragment
import com.fortune.library.mvvm.rv.linear
import com.fortune.library.mvvm.rv.setup

/**
 * $fragmentDesc 页面
 */
class $fragmentName : BaseListFragment<$viewBindingName>() {

    companion object{
    
        /**
         * 获取Fragment实例
         *
         * @param type 页面类型
         */
        fun getInstance(type: Int): $fragmentName{
            val fragment = $fragmentName()
            fragment.arguments?.putInt("type", type)
            return fragment
        }
    }

    /**
     * 初始化ViewModel
     */
    override val viewModel: $viewModelName by viewModels()
    
    /**
     * 页面类型
     */
    private val type: Int by lazy {
        arguments?.getInt("type") ?: 0
    }

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

fun viewPager2Adapter(
    packageName: String,
    adapterName: String,
    fragmentName: String,
    desc: String,
    generateList: Boolean
) = """
package $packageName

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * $desc 适配器
 */
class $adapterName(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return $fragmentName.getInstance(${if (generateList) "position" else ""})
    }

    override fun getItemCount(): Int {
        return 2
    }
}
""".trimIndent()