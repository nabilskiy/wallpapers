package wallgram.hd.wallpapers.presentation.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.FragmentDownloadDialogBinding
import wallgram.hd.wallpapers.presentation.base.BaseBottomSheetDialogFragment
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.presentation.main.withParentFragment
import wallgram.hd.wallpapers.presentation.wallpaper.WallpaperFragment

class DownloadDialogFragment : BaseFragment<
        DownloadViewModel, FragmentDownloadDialogBinding>(
    FragmentDownloadDialogBinding::inflate
), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.forEach { currentView ->
            if (currentView is MaterialButton)
                currentView.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.home_screen_btn -> handle(DownloadAction.Home())
            R.id.lock_screen_btn -> handle(DownloadAction.Lock())
            R.id.both_screen_btn -> handle(DownloadAction.Both())
            R.id.download_btn -> handle(DownloadAction.Download())
        }
    }

    fun handle(action: DownloadAction) {
        (parentFragment as WallpaperFragment).handle(action)
    }

    override val viewModel: DownloadViewModel by viewModels()
}