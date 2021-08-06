package wallgram.hd.wallpapers.ui.wallpapers

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.util.*

class ReposLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<ReposLoadStateViewHolder>(), SpanSizeLookupOwner,
        ItemDecorationOwner {
    override fun onBindViewHolder(holder: ReposLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ReposLoadStateViewHolder {
        return ReposLoadStateViewHolder.create(parent, retry)
    }

    override fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup = SimpleSpanSizeLookup(3)
    override fun getItemDecorations(): List<RecyclerView.ItemDecoration> = listOf(SimpleItemDecoration(16.dp))
}