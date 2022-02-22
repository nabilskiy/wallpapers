package wallgram.hd.wallpapers.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import wallgram.hd.wallpapers.databinding.FragmentDownloadDialogBinding
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.ui.base.BaseBottomSheetDialogFragment
import wallgram.hd.wallpapers.ui.main.withParentFragment
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

    private val links: Links by args(ARG_LINKS)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            portraitButton.isVisible = links.portrait != null
            landscapeButton.isVisible = links.landscape != null

            portraitButton.setOnClickListener {
                links.portrait?.let { url ->
                    dismissAllowingStateLoss()
                    withParentFragment {
                        (this as WallpaperFragment).download(url)
                    }
                }
            }
            landscapeButton.setOnClickListener {
                links.landscape?.let { url ->
                    dismissAllowingStateLoss()
                    withParentFragment {
                        (this as WallpaperFragment).download(url)
                    }
                }
            }
            sourceButton.setOnClickListener {
                dismissAllowingStateLoss()
                withParentFragment {
                    (this as WallpaperFragment).downloadSource(links.source)
                }
            }
        }
    }

    override fun getViewModel(): Class<DownloadViewModel> = DownloadViewModel::class.java

}