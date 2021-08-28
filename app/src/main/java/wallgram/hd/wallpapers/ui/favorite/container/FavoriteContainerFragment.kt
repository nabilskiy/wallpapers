package wallgram.hd.wallpapers.ui.favorite.container

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.FragmentCategoriesItemsBinding
import wallgram.hd.wallpapers.databinding.FragmentFavoriteContainerBinding
import wallgram.hd.wallpapers.model.SubCategory
import wallgram.hd.wallpapers.ui.FeedViewModel
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.categories.CategoriesItemsAdapter
import wallgram.hd.wallpapers.ui.categories.CategoriesListFragment
import wallgram.hd.wallpapers.ui.favorite.FavoriteViewModel
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.findCurrentFragment
import wallgram.hd.wallpapers.util.withArgs

class FavoriteContainerFragment : BaseFragment<FavoriteViewModel, FragmentFavoriteContainerBinding>(
    FragmentFavoriteContainerBinding::inflate
) {
    companion object {
        private const val ARG_TYPE = "arg_type"
        fun create(type: WallType = WallType.FAVORITE) = FavoriteContainerFragment().withArgs(
            ARG_TYPE to type
        )
    }

    private val type: WallType by args(ARG_TYPE, WallType.FAVORITE)

    override fun invalidate() {
        super.invalidate()
        binding.appBarLayout.setExpanded(true, true)
        binding.viewPager.findCurrentFragment(childFragmentManager)?.let{
            (it as BaseFragment<*,*>).invalidate()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            viewPager.apply {
                offscreenPageLimit = 2
                isUserInputEnabled = false
                adapter = FavoriteItemsAdapter(this@FavoriteContainerFragment, type)
            }

            when (type) {
                WallType.FAVORITE -> viewPager.currentItem = 0
                else -> viewPager.currentItem = 1
            }

            TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
                tab.text = resources.getStringArray(
                    R.array.favorite_list
                )[position]
            }.attach()
        }
    }

    fun selectScreen(i: Int) {
        binding.viewPager.setCurrentItem(i, true)
    }

    override fun getViewModel(): Class<FavoriteViewModel> = FavoriteViewModel::class.java
}