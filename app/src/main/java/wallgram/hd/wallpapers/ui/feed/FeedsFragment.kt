package wallgram.hd.wallpapers.ui.feed

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.withArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import wallgram.hd.wallpapers.App.Companion.modo
import wallgram.hd.wallpapers.databinding.FragmentFeedsBinding
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.util.modo.back
import java.util.*


class FeedsFragment : BaseFragment<FeedViewModel, FragmentFeedsBinding>(
    FragmentFeedsBinding::inflate
) {

    companion object {
        private const val ARG_FEED_REQUEST = "arg_feed_request"

        fun create(feedRequest: FeedRequest) = FeedsFragment().withArgs(
            ARG_FEED_REQUEST to feedRequest
        )
    }

    private val feedRequest: FeedRequest by args(ARG_FEED_REQUEST)
    private var position = 0

    override fun invalidate() {
        super.invalidate()
        modo.back()
    }

    override fun onDestroyView() {
        position = binding.viewPager.currentItem
        super.onDestroyView()

        viewModel.bundleFragment.value = bundleOf(
            "POSITION" to position
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.bundleFragment.observe(viewLifecycleOwner, {
            binding.viewPager.post {
                position = it.getInt("POSITION", 0)
                binding.viewPager.currentItem = position
            }
        })
        if (viewModel.bundleFragment.value == null) {
            binding.viewPager.post {
                binding.viewPager.currentItem = when (feedRequest.type) {
                    WallType.POPULAR -> 1
                    else -> 0
                }
            }
        }

        with(binding) {

            if (feedRequest.categoryName.isBlank()) {
                titleText.isVisible = false
                (titleText.layoutParams as AppBarLayout.LayoutParams)
                    .scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
            }


            titleText.text = feedRequest.categoryName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            viewPager.apply {
                offscreenPageLimit = 3
                adapter = FeedsAdapter(this@FeedsFragment, feedRequest)
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