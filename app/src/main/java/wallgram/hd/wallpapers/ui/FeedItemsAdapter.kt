package wallgram.hd.wallpapers.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.ui.wallpapers.WallpapersFragment

class FeedItemsAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WallpapersFragment.newInstance(FeedRequest(sort = "date"))
            1 -> WallpapersFragment.newInstance(FeedRequest(sort = "popular"))
            else -> WallpapersFragment.newInstance(FeedRequest(sort = "random"))
        }
    }




}