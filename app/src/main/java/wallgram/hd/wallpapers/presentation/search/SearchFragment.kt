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

            val galleryAdapter = SearchAdapter(object : GenericAdapter.ClickListener<Pair<Int, Int>> {
                override fun click(item: Pair<Int, Int>) {
                    viewModel.itemClicked(WallpaperRequest.SEARCH(""), item.first)
                }

            })

            toolbar.setNavigationOnClickListener { viewModel.back() }


            viewModel.wallpapersLiveData.observe(viewLifecycleOwner) {
                it.map(galleryAdapter)
            }

            searchText.isIconified = false
            searchText.setOnQueryTextListener(SimpleQueryListener(viewModel))

            swipeRefreshLayout.setColorSchemeResources(R.color.colorYellow)
            swipeRefreshLayout.setOnRefreshListener {
                // wallpapersAdapter.refresh()
            }

//            searchField.setStartIconOnClickListener {
//                requireActivity().onBackPressed()
//            }


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
                        viewModel.loadMoreData(
                            binding.searchText.query.toString(),
                            gridLayoutManager.findLastVisibleItemPosition()
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

    override fun onDestroyView() {
        binding.searchText.setOnQueryTextListener(null)
        super.onDestroyView()
    }

    override val viewModel: SearchViewModel by viewModels()

}