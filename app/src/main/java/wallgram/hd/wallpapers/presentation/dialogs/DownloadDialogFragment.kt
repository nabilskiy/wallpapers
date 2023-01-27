package wallgram.hd.wallpapers.presentation.dialogs

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.FragmentDownloadDialogBinding
import wallgram.hd.wallpapers.model.Links
import wallgram.hd.wallpapers.presentation.base.BaseBottomSheetDialogFragment
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.presentation.main.withParentFragment
import wallgram.hd.wallpapers.presentation.wallpaper.WallpaperFragment
import javax.inject.Inject

@AndroidEntryPoint
class DownloadDialogFragment : BaseFragment<
        DownloadViewModel, FragmentDownloadDialogBinding>(
    FragmentDownloadDialogBinding::inflate
), View.OnClickListener {

    companion object {
        private const val ORIGINAL_SCREEN = "original_screen"
        fun newInstance(resolution: String) = DownloadDialogFragment().apply {
            arguments = bundleOf(ORIGINAL_SCREEN to resolution)
        }
    }

    @Inject
    lateinit var displayProvider: DisplayProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resolution = arguments?.getString(
            ORIGINAL_SCREEN, displayProvider.getScreen()
        ) ?: displayProvider.getScreen()

        binding.root.forEach { currentView ->
            currentView.setOnClickListener(this)
        }

        binding.downloadBtn.setResolution(displayProvider.getScreen())
        binding.originalDownloadBtn.setResolution(resolution)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.home_screen_btn -> handle(DownloadAction.Home())
            R.id.lock_screen_btn -> handle(DownloadAction.Lock())
            R.id.both_screen_btn -> handle(DownloadAction.Both())
            R.id.download_btn -> handle(DownloadAction.Download())
            R.id.original_download_btn -> handle(DownloadAction.DownloadSource())
        }
    }

    fun handle(action: DownloadAction) {
        (parentFragment as WallpaperFragment).handle(action)
    }

    override val viewModel: DownloadViewModel by viewModels()
}