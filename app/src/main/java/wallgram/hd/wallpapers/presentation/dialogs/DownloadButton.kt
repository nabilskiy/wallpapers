package wallgram.hd.wallpapers.presentation.dialogs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.ViewDownloadButtonBinding

class DownloadButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewDownloadButtonBinding = ViewDownloadButtonBinding.inflate(
        LayoutInflater.from(context), this
    )

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(attrs, R.styleable.DownloadButton)
            if (a.hasValue(R.styleable.DownloadButton_wallpaper))
                setWallpaper(a.getString(R.styleable.DownloadButton_wallpaper)!!)

            if (a.hasValue(R.styleable.DownloadButton_resolution))
                setResolution(a.getString(R.styleable.DownloadButton_resolution)!!)

            a.recycle()
        }
    }

    fun setWallpaper(wallpaper: String) {
        binding.wallpaperText.text = wallpaper
    }

    fun setResolution(resolution: String) {
        binding.resolutionText.text = resolution
    }

}