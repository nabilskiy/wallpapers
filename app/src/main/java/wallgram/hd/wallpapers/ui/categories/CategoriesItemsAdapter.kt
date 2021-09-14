package wallgram.hd.wallpapers.ui.categories

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.ui.wallpapers.WallpapersFragment

class CategoriesItemsAdapter(fa: Fragment, val feedRequest: FeedRequest) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WallpapersFragment.newInstance(feedRequest.copy(sort="date"))
            1 -> WallpapersFragment.newInstance(feedRequest.copy(sort="popular"))
            else -> WallpapersFragment.newInstance(feedRequest.copy(sort = "random"))
        }
    }
}