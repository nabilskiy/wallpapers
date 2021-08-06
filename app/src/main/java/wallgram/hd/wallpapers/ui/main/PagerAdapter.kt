package wallgram.hd.wallpapers.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ItemPagerBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PagerAdapter(var fa: FragmentManager, var lifecycle: Lifecycle) :
    RecyclerView.Adapter<PagerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
//            binding.viewPager.apply {
//                offscreenPageLimit = 3
//                adapter = FeedItemsAdapter(fa, lifecycle)
//            }

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab: TabLayout.Tab, position: Int -> tab.text = binding.root.context.resources.getStringArray(R.array.feed_list)[position] }.attach()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ItemPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount() = 1
}