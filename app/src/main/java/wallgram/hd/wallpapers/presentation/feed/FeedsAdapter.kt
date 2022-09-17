package wallgram.hd.wallpapers.presentation.feed

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.presentation.wallpapers.WallpapersFragment

class FeedsAdapter(fa: Fragment, val list: List<WallpaperRequest>) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return WallpapersFragment.newInstance(list[position])

    }
}
