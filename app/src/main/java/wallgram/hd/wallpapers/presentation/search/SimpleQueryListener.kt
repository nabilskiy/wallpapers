package wallgram.hd.wallpapers.presentation.search

import android.widget.SearchView

class SimpleQueryListener(private val search: Search) : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?) = find(query)
    override fun onQueryTextChange(newText: String?) = find(newText)

    private fun find(query: String?): Boolean {
        search.search(query.orEmpty().trim())
        return !query.isNullOrEmpty()
    }
}