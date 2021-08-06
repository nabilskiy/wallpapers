package wallgram.hd.wallpapers.ui.categories

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.FragmentCategoriesItemsBinding
import wallgram.hd.wallpapers.model.SubCategory
import wallgram.hd.wallpapers.ui.FeedViewModel
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.withArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class CategoriesListFragment : BaseFragment<FeedViewModel, FragmentCategoriesItemsBinding>(
        FragmentCategoriesItemsBinding::inflate
) {

    companion object {
        private const val ARG_CATEGORY = "arg_category"
        private const val ARG_TYPE = "arg_type"
        fun create(type: WallType, category: SubCategory) = CategoriesListFragment().withArgs(
                ARG_TYPE to type,
                ARG_CATEGORY to category
        )
    }

    private val category: SubCategory by args(ARG_CATEGORY)
    private val type: WallType by args(ARG_TYPE, WallType.ALL)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            if(category.name.isBlank()){
                titleText.isVisible = false
                (titleText.layoutParams as AppBarLayout.LayoutParams)
                        .scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
            }

            titleText.text = category.name.capitalize()
            viewPager.apply {
                offscreenPageLimit = 3
                adapter = CategoriesItemsAdapter(this@CategoriesListFragment, category = category.id, type)
            }
            viewPager.post {
                when (type) {
                    WallType.POPULAR -> viewPager.currentItem = 1
                    else -> viewPager.currentItem = 0
                }
            }

            TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int -> tab.text = resources.getStringArray(R.array.feed_list)[position] }.attach()
        }
    }

    override fun getViewModel(): Class<FeedViewModel> = FeedViewModel::class.java

}