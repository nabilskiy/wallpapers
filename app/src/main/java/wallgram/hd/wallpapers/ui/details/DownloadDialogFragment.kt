package wallgram.hd.wallpapers.ui.details

import android.os.Bundle
import android.view.View
import wallgram.hd.wallpapers.databinding.FragmentDownloadDialogBinding
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.ui.main.MainViewModel
import wallgram.hd.wallpapers.ui.base.BaseBottomSheetDialogFragment
import wallgram.hd.wallpapers.ui.wallpaper.WallpaperFragment
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.withArgs

class DownloadDialogFragment : BaseBottomSheetDialogFragment<
        DownloadViewModel, FragmentDownloadDialogBinding>(
        FragmentDownloadDialogBinding::inflate
) {

    companion object {
        private const val ARG_LINKS = "arg_links"
        fun create(links: Links) = DownloadDialogFragment().withArgs(
                ARG_LINKS to links
        )
    }

    private val links: Links? by args(ARG_LINKS)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            portraitButton.setOnClickListener {
                links?.let {
                    viewModel.download(it.portrait ?: "")
                }
            }
        }
    }

    override fun getViewModel(): Class<DownloadViewModel> = DownloadViewModel::class.java

}