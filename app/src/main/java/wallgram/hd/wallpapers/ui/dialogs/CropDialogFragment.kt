package wallgram.hd.wallpapers.ui.dialogs

import android.os.Bundle
import android.view.View
import wallgram.hd.wallpapers.databinding.FragmentCropDialogBinding
import wallgram.hd.wallpapers.databinding.FragmentInstallDialogBinding
import wallgram.hd.wallpapers.model.Config
import wallgram.hd.wallpapers.ui.main.MainViewModel
import wallgram.hd.wallpapers.ui.base.BaseBottomSheetDialogFragment
import wallgram.hd.wallpapers.ui.crop.CropFragment
import wallgram.hd.wallpapers.ui.main.withParentFragment
import wallgram.hd.wallpapers.ui.wallpaper.WallpaperFragment
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.withArgs

const val SET_BOTH_SCREEN = 0
const val SET_HOME_SCREEN = 1
const val SET_LOCK_SCREEN = 2
const val DOWNLOAD_TO_GALLERY = 4

class CropDialogFragment : BaseBottomSheetDialogFragment<
        MainViewModel, FragmentCropDialogBinding>(
        FragmentCropDialogBinding::inflate
) {


    companion object {
        private const val ARG_LINK = "arg_link"
        fun create(url: String) = CropDialogFragment().withArgs(
            ARG_LINK to url
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            homeScreenBtn.setOnClickListener {
                dismissAllowingStateLoss()
                withParentFragment {
                    (this as CropFragment).setWallpaper(SET_HOME_SCREEN)
                }
            }

            lockScreenBtn.setOnClickListener {
                dismissAllowingStateLoss()
                withParentFragment {
                    (this as CropFragment).setWallpaper(SET_LOCK_SCREEN)
                }
            }

            bothScreenBtn.setOnClickListener {
                dismissAllowingStateLoss()
                withParentFragment {
                    (this as CropFragment).setWallpaper(SET_BOTH_SCREEN)
                }
            }


            saveGalleryBtn.setOnClickListener {
                dismissAllowingStateLoss()
                withParentFragment {
                    (this as CropFragment).setWallpaper(DOWNLOAD_TO_GALLERY)
                }
            }
        }
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

}