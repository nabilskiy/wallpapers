package wallgram.hd.wallpapers.ui.wallpapers

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.databinding.WallpapersFragmentBinding
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.main.MainActivity
import wallgram.hd.wallpapers.util.*
import wallgram.hd.wallpapers.util.modo.externalForward

private const val CATEGORY_ID = "category_id"
private const val TYPE_ID = "type_id"
private const val SORT_TYPE = "sort_type"

class WallpapersFragment : BaseFragment<WallpapersViewModel, WallpapersFragmentBinding>(
        WallpapersFragmentBinding::inflate
) {

    private val wallpapersAdapter: WallpapersAdapter by lazy {
        WallpapersAdapter { position, id ->
            viewModel.itemClicked(position, id)
        }
    }

    companion object {

        fun newInstance(type: WallType, category: Int, sort: String): WallpapersFragment =
                WallpapersFragment().withArgs(
                        TYPE_ID to type,
                        CATEGORY_ID to category,
                        SORT_TYPE to sort
                )

        fun newInstance(sort: String): WallpapersFragment =
                WallpapersFragment().withArgs(
                        SORT_TYPE to sort
                )
    }

    private val category: Int by args(CATEGORY_ID, -1)
    private val sort: String by args(SORT_TYPE, "")
    private val type: WallType by args(TYPE_ID)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if(type == WallType.ALL)
//        when(sort){
//            "date" -> if(vM.dateLiveData.value == null) vM.getLiveData(type,category,sort)
//            "popular" -> if(vM.popularLiveData.value == null) vM.getLiveData(type,category,sort)
//            "random" -> if(vM.randomLiveData.value == null) vM.getLiveData(type,category,sort)
//            else -> if(vM.wallpapersLiveData.value == null) vM.getLiveData(type,category,sort)
//        }



        viewModel.getLiveData(type, category, sort, requireContext().getResolution())

        viewModel.wallpapersLiveData.observe(viewLifecycleOwner, {
            wallpapersAdapter.submitData(lifecycle = lifecycle, it)
        })

//        when (sort) {
//            "date" -> vM.dateLiveData.observe(viewLifecycleOwner, {
//                wallpapersAdapter.submitData(lifecycle = lifecycle, it)
//            })
//            "popular" -> vM.popularLiveData.observe(viewLifecycleOwner, {
//                wallpapersAdapter.submitData(lifecycle = lifecycle, it)
//            })
//            "random" -> vM.randomLiveData.observe(viewLifecycleOwner, {
//                wallpapersAdapter.submitData(lifecycle = lifecycle, it)
//            })
//            else -> vM.wallpapersLiveData.observe(viewLifecycleOwner, {
//                wallpapersAdapter.submitData(lifecycle = lifecycle, it)
//            })
//        }

        val concatAdapter = wallpapersAdapter.withLoadStateHeaderAndFooter(
                header = ReposLoadStateAdapter { wallpapersAdapter.retry() },
                footer = ReposLoadStateAdapter { wallpapersAdapter.retry() })

        with(binding) {

            swipeRefreshLayout.setColorSchemeResources(R.color.colorYellow)
            swipeRefreshLayout.setOnRefreshListener {
                wallpapersAdapter.refresh()
            }

            list.apply {
                layoutManager = GridLayoutManager(requireContext(), 3).apply {
                    spanSizeLookup = ConcatSpanSizeLookup(3) { concatAdapter.adapters }
                }
               addItemDecoration(ConcatItemDecoration { concatAdapter.adapters })
              //  addItemDecoration(ItemOffsetDecoration())
                adapter = concatAdapter
            }
            wallpapersAdapter.addLoadStateListener { loadState ->
                // Only show the list if refresh succeeds.

                list.isVisible = loadState.source.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                if(swipeRefreshLayout.isRefreshing)
                    swipeRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
           //     errorLayout.root.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                errorState?.let {
//                    Toast.makeText(
//                            this,
//                            "\uD83D\uDE28 Wooops ${it.error}",
//                            Toast.LENGTH_LONG
//                    ).show()
                }
            }
//            errorLayout.btnRetry.setOnClickListener {
//                wallpapersAdapter.retry()
//            }
        }

    }

    override fun getViewModel(): Class<WallpapersViewModel> = WallpapersViewModel::class.java

}