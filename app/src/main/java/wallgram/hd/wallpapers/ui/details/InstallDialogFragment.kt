package wallgram.hd.wallpapers.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import wallgram.hd.wallpapers.databinding.FragmentInstallDialogBinding
import wallgram.hd.wallpapers.model.Config
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.ui.main.MainViewModel
import wallgram.hd.wallpapers.ui.base.BaseBottomSheetDialogFragment
import wallgram.hd.wallpapers.ui.main.withParentFragment
import wallgram.hd.wallpapers.ui.wallpaper.WallpaperFragment
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.withArgs

class InstallDialogFragment : BaseBottomSheetDialogFragment<
        MainViewModel, FragmentInstallDialogBinding>(
        FragmentInstallDialogBinding::inflate
) {


    companion object {
        private const val ARG_LINK = "arg_link"
        fun create(url: String) = InstallDialogFragment().withArgs(
            ARG_LINK to url
        )
    }

    private val url: String by args(ARG_LINK)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            homeScreenBtn.setOnClickListener {
                dismissAllowingStateLoss()
                withParentFragment {
                    (this as WallpaperFragment).downloadAndSetWallpaper(url, Config.Builder().setWallpaperMode(1).build())
                }
            }

            lockScreenBtn.setOnClickListener {
                dismissAllowingStateLoss()
                withParentFragment {
                    (this as WallpaperFragment).downloadAndSetWallpaper(url, Config.Builder().setWallpaperMode(2).build())
                }
            }

            bothScreenBtn.setOnClickListener {
                dismissAllowingStateLoss()
                withParentFragment {
                    (this as WallpaperFragment).downloadAndSetWallpaper(url, Config.Builder().setWallpaperMode(0).build())
                }
            }
        }
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

}