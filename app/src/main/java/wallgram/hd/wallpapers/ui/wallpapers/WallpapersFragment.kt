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
import wallgram.hd.wallpapers.databinding.FragmentWallpaperBinding
import wallgram.hd.wallpapers.databinding.FragmentWallpapersBinding
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.main.MainActivity
import wallgram.hd.wallpapers.util.*
import wallgram.hd.wallpapers.util.modo.externalForward

private const val CATEGORY_ID = "category_id"
private const val TYPE_ID = "type_id"
private const val SORT_TYPE = "sort_type"
private const val FEED_REQUEST = "feed_request"

class WallpapersFragment : BaseFragment<WallpapersViewModel, FragmentWallpapersBinding>(
    FragmentWallpapersBinding::inflate
) {

    private val wallpapersAdapter: WallpapersAdapter by lazy {
        WallpapersAdapter { position, id ->
            viewModel.itemClicked(position, id)
        }
    }

    companion object {
        fun newInstance(feedRequest: FeedRequest): WallpapersFragment =
            WallpapersFragment().withArgs(
                FEED_REQUEST to feedRequest
            )
    }

    private val feedRequest: FeedRequest by args(FEED_REQUEST)

    override fun invalidate() {
        super.invalidate()
        binding.list.scrollToPosition(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feedRequest.apply {
            resolution = requireContext().getResolution()
        }
        viewModel.getLiveData(feedRequest)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.wallpapersLiveData.observe(viewLifecycleOwner, {
            wallpapersAdapter.submitData(lifecycle = lifecycle, it)
        })

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
                itemAnimator = null
                //  addItemDecoration(ItemOffsetDecoration())
                adapter = concatAdapter
            }
            wallpapersAdapter.addLoadStateListener { loadState ->
                // Only show the list if refresh succeeds.

                list.isVisible = loadState.source.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                if (swipeRefreshLayout.isRefreshing)
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