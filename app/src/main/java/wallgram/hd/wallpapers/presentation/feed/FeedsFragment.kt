package wallgram.hd.wallpapers.presentation.feed

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.withArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.App.Companion.modo
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.databinding.FragmentFeedsBinding
import wallgram.hd.wallpapers.util.modo.back

@AndroidEntryPoint
class FeedsFragment : BaseFragment<FeedViewModel, FragmentFeedsBinding>(
    FragmentFeedsBinding::inflate
) {

    companion object {
        private const val WALLPAPER = "wallpaper"

        fun create(wallpaperRequest: WallpaperRequest) = FeedsFragment().withArgs(
            WALLPAPER to wallpaperRequest
        )
    }

    private val wallpaperRequest: WallpaperRequest by args(WALLPAPER)

    private var position = 0

    override fun invalidate() {
        super.invalidate()
        modo.back()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            titleText.show(wallpaperRequest.getTitle())

            backBtn.setOnClickListener {
                viewModel.back()
            }

            if(wallpaperRequest.getTitle().isBlank()){
                header.isVisible = false
                (header.layoutParams as AppBarLayout.LayoutParams)
                    .scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
            }

            val list = listOf(
                wallpaperRequest.copy("date"),
                wallpaperRequest.copy("popular"),
                wallpaperRequest.copy("random")
            )

            viewPager.apply {
                offscreenPageLimit = 3
                adapter = FeedsAdapter(this@FeedsFragment, list)
            }

            TabLayoutMediator(
                tabLayout,
                viewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = resources.getStringArray(R.array.feed_list)[position]
            }.attach()
        }
    }

    override val viewModel: FeedViewModel by viewModels()
}