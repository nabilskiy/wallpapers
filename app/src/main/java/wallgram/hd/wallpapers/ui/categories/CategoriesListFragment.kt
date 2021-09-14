package wallgram.hd.wallpapers.ui.categories

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import wallgram.hd.wallpapers.App.Companion.modo
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.util.modo.back
import wallgram.hd.wallpapers.util.modo.exit


class CategoriesListFragment : BaseFragment<FeedViewModel, FragmentCategoriesItemsBinding>(
    FragmentCategoriesItemsBinding::inflate
) {

    companion object {
        private const val ARG_FEED_REQUEST = "arg_feed_request"

        fun create(feedRequest: FeedRequest) = CategoriesListFragment().withArgs(
            ARG_FEED_REQUEST to feedRequest
        )
    }

    private val feedRequest: FeedRequest by args(ARG_FEED_REQUEST)

    override fun invalidate() {
        super.invalidate()
        modo.back()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            if (feedRequest.categoryName.isBlank()) {
                titleText.isVisible = false
                (titleText.layoutParams as AppBarLayout.LayoutParams)
                    .scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
            }


            titleText.text = feedRequest.categoryName.capitalize()
            viewPager.apply {
                offscreenPageLimit = 3
                adapter = CategoriesItemsAdapter(this@CategoriesListFragment, feedRequest)
            }
            viewPager.post {
                when (feedRequest.type) {
                    WallType.POPULAR -> viewPager.currentItem = 1
                    else -> viewPager.currentItem = 0
                }
            }

            TabLayoutMediator(
                tabLayout,
                viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = resources.getStringArray(R.array.feed_list)[position]
            }.attach()
        }
    }

    override fun getViewModel(): Class<FeedViewModel> = FeedViewModel::class.java

}