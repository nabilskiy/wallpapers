package wallgram.hd.wallpapers.presentation.base.adapter

import androidx.recyclerview.widget.GridLayoutManager

interface SpanSizeLookupOwner {
    fun getSpanSizeLookup(spanCount: Int): GridLayoutManager.SpanSizeLookup
}