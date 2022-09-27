package wallgram.hd.wallpapers.presentation.wallpapers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.databinding.FragmentSimilarBinding
import wallgram.hd.wallpapers.databinding.FragmentWallpapersBinding
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.views.slidinguppanel.ISlidingUpPanelLayoutHost
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.wallpaper.WallpaperItemDecoration
import wallgram.hd.wallpapers.util.*

@AndroidEntryPoint
class SimilarFragment : BaseFragment<SimilarViewModel, FragmentSimilarBinding>(
    FragmentSimilarBinding::inflate
) {

    companion object {
        private const val WALLPAPER = "wallpaper"

        fun newInstance(wallpaperRequest: WallpaperRequest): SimilarFragment =
            SimilarFragment().withArgs(
                WALLPAPER to wallpaperRequest
            )
    }

    private val wallpaperRequest: WallpaperRequest by args(WALLPAPER)

    override fun invalidate() {
        super.invalidate()
        binding.list.scrollToPosition(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetch(wallpaperRequest)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val galleryAdapter = SimilarAdapter(object : GenericAdapter.ClickListener<Pair<Int, Int>> {
            override fun click(item: Pair<Int, Int>) {
                viewModel.itemClicked(wallpaperRequest, item.first - 1)
            }
        })

        viewModel.wallpapersLiveData.observe(viewLifecycleOwner) {
            it.map(galleryAdapter)
        }

        with(binding) {

            toolbar.handleClick {
                if (parentFragment is ISlidingUpPanelLayoutHost) {
                    (parentFragment as ISlidingUpPanelLayoutHost).hide()
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
                        viewModel.loadMoreData(
                            wallpaperRequest,
                            gridLayoutManager.findLastVisibleItemPosition()
                        )
                    }
                })

                itemAnimator = null
                addItemDecoration(WallpaperItemDecoration(2.dp, 6))
                adapter = galleryAdapter
            }
        }

        if (parentFragment is ISlidingUpPanelLayoutHost) {
            (parentFragment as ISlidingUpPanelLayoutHost).onRecyclerViewAttached(binding.list)
        }

    }

    override val viewModel: SimilarViewModel by viewModels()
}