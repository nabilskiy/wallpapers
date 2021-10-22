package wallgram.hd.wallpapers.ui.favorite.container

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import wallgram.hd.wallpapers.ui.favorite.FavoriteFragment
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.ui.wallpapers.WallpapersFragment

class FavoriteItemsAdapter(fa: Fragment, val type: WallType) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int) = FavoriteFragment.create(position)
}