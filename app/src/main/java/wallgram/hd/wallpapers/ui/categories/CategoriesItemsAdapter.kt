package wallgram.hd.wallpapers.ui.categories

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.ui.wallpapers.WallpapersFragment

class CategoriesItemsAdapter(fa: Fragment, val category: Int, val type: WallType) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WallpapersFragment.newInstance(type = type, category = category, sort = "date")
            1 -> WallpapersFragment.newInstance(type = type, category = category, sort = "popular")
            else -> WallpapersFragment.newInstance(type = type, category = category, sort = "random")
        }
    }
}