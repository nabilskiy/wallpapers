package wallgram.hd.wallpapers.ui.details

import wallgram.hd.wallpapers.databinding.FragmentInstallDialogBinding
import wallgram.hd.wallpapers.ui.main.MainViewModel
import wallgram.hd.wallpapers.ui.base.BaseBottomSheetDialogFragment

class InstallDialogFragment : BaseBottomSheetDialogFragment<
        MainViewModel, FragmentInstallDialogBinding>(
        FragmentInstallDialogBinding::inflate
) {
    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

}