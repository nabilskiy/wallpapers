package wallgram.hd.wallpapers.presentation.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wallgram.hd.wallpapers.databinding.FragmentFavoriteBinding
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.databinding.FragmentHistoryBinding
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.HeaderItemDecoration
import wallgram.hd.wallpapers.presentation.favorite.FavoriteAdapter
import wallgram.hd.wallpapers.presentation.favorite.FavoriteViewModel
import wallgram.hd.wallpapers.presentation.favorite.HistoryAdapter
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewType
import wallgram.hd.wallpapers.presentation.wallpaper.WallpaperItemDecoration
import wallgram.hd.wallpapers.util.*

@AndroidEntryPoint
class HistoryFragment : BaseFragment<HistoryViewModel, FragmentHistoryBinding>(
    FragmentHistoryBinding::inflate
) {

    override fun invalidate() {
        super.invalidate()
        viewModel.back()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val galleryAdapter = HistoryAdapter()

        viewModel.wallpapersLiveData.observe(viewLifecycleOwner) {
            it.map(galleryAdapter)
        }

        viewModel.observeUpdate(viewLifecycleOwner) {
            viewModel.fetch()
        }

        with(binding) {

            backBtn.setOnClickListener {
                viewModel.back()
            }

            val gridLayoutManager =
                GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false).apply {
                    spanSizeLookup = galleryAdapter.getSpanSizeLookup(3)
                }

            historyList.apply {
                layoutManager = gridLayoutManager
                itemAnimator = null
                addItemDecoration(HeaderItemDecoration(12.dp, 0))
                addItemDecoration(WallpaperItemDecoration(2.dp, 6))
                adapter = galleryAdapter
            }
        }

    }

    private fun clearAll() {
//        if (favoritesAdapter.itemCount > 0) {
//            MaterialAlertDialogBuilder(requireContext())
//                .setTitle(resources.getString(R.string.clear))
//                .setMessage(resources.getString(R.string.clear_confirm))
//                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
//                    dialog.dismiss()
//                }
//                .setPositiveButton(resources.getString(R.string.ok_btn)) { dialog, _ ->
//                    viewModel.clearAll(type)
//                    dialog.dismiss()
//                }
//                .show()
//        }
    }

    override val viewModel: HistoryViewModel by viewModels()
}