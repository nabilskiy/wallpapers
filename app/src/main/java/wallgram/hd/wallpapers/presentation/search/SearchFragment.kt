package wallgram.hd.wallpapers.presentation.search

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.BaseFragment

import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.data.gallery.WallpapersCache
import wallgram.hd.wallpapers.databinding.FragmentSearchBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.wallpaper.WallpaperItemDecoration
import wallgram.hd.wallpapers.util.dp

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {

    private lateinit var suggestionAdapter: ArrayAdapter<String>

    override fun invalidate() {
        super.invalidate()
        viewModel.back()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        suggestionAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
        )

        with(binding) {

            val galleryAdapter = SearchAdapter()

            toolbar.setNavigationOnClickListener { viewModel.back() }


            viewModel.wallpapersLiveData.observe(viewLifecycleOwner) {
                it.map(galleryAdapter)
            }

            viewModel.progressLiveData.observe(viewLifecycleOwner) { refreshing ->
                refreshing.apply(binding.swipeRefreshLayout)
            }

            searchText.isIconified = false
            searchText.setOnQueryTextListener(SimpleQueryListener(viewModel))

            swipeRefreshLayout.apply {
                setColorSchemeResources(R.color.colorYellow)
                setOnRefreshListener {
                    viewModel.reset()
                    viewModel.search(query())
                }
            }

            val gridLayoutManager =
                GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false).apply {
                    spanSizeLookup = galleryAdapter.getSpanSizeLookup(3)
                }

            list.apply {
                layoutManager = gridLayoutManager
                setHasFixedSize(true)

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val visibleItemCount = gridLayoutManager.childCount / 4
                        val totalItemCount = gridLayoutManager.itemCount
                        val firstVisibleItem = gridLayoutManager.findLastVisibleItemPosition()

                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount)
                            viewModel.loadMoreData(
                                query()
                            )
                    }
                })

                itemAnimator = null
                addItemDecoration(WallpaperItemDecoration(2.dp, 6))
                adapter = galleryAdapter
            }

        }

        viewModel.init()
    }

    private fun query() = binding.searchText.query.toString()

    override fun onDestroyView() {
        binding.searchText.setOnQueryTextListener(null)
        super.onDestroyView()
    }

    override val viewModel: SearchViewModel by viewModels()

}