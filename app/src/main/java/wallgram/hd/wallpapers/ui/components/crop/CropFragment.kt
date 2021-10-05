package wallgram.hd.wallpapers.ui.components.crop

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import wallgram.hd.wallpapers.databinding.FragmentCropBinding
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.ui.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.util.*
import wallgram.hd.wallpapers.views.biv.BigImageViewer
import wallgram.hd.wallpapers.views.biv.loader.ImageLoader
import wallgram.hd.wallpapers.views.biv.loader.glide.GlideImageLoader
import wallgram.hd.wallpapers.views.biv.view.GlideImageViewFactory
import java.io.File
import java.lang.Exception

class CropFragment : BaseFragment<CropViewModel, FragmentCropBinding>(
    FragmentCropBinding::inflate
) {

    companion object {
        private const val ARG_LANDSCAPE = "arg_landscape"
        fun create(landscape: String) = CropFragment().withArgs(
            ARG_LANDSCAPE to landscape
        )
    }

    private val landscapeLink: String by args(ARG_LANDSCAPE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BigImageViewer.initialize(GlideImageLoader.with(requireContext()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.toolbar.doOnApplyWindowInsets { _view, windowInsetsCompat, rect ->
            _view.updatePadding(top = rect.top + windowInsetsCompat.systemWindowInsetTop)
            windowInsetsCompat
        }

        binding.toolbar.setNavigationOnClickListener {
            viewModel.onBack()
        }
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_save -> {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        false
                    }
                    binding.mBigImage.saveImageIntoGallery()
                }
            }
            true
        }



        binding.mBigImage.apply {
            setImageLoaderCallback(object : ImageLoader.Callback {
                override fun onCacheHit(imageType: Int, image: File?) {

                }

                override fun onCacheMiss(imageType: Int, image: File?) {
                }

                override fun onStart() {
                }

                override fun onProgress(progress: Int) {
                }

                override fun onFinish() {
                    if (binding != null)
                        binding.progressBar.isVisible = false
                }

                override fun onSuccess(image: File?) {
                    if (binding != null)
                        binding.progressBar.isVisible = false
                }

                override fun onFail(error: Exception?) {
                    if (binding != null)
                        binding.progressBar.isVisible = false
                }
            })
            setImageViewFactory(GlideImageViewFactory())
            showImage(Uri.parse(landscapeLink))
            setOnClickListener {
                if (binding.toolbar.translationY == 0f)
                    hideAllViews()
                else showAllViews()
            }
        }
    }

    private fun hideAllViews() {
        lifecycleScope.launch {
            val anim1 =
                async { binding.toolbar.translationY(0f, -binding.toolbar.height.toFloat()) }

            awaitAll(anim1)
        }

        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showAllViews() {
        lifecycleScope.launch {
            val anim1 =
                async { binding.toolbar.translationY(-binding.toolbar.height.toFloat(), 0f) }

            awaitAll(anim1)
        }

        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            binding.root
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    override fun onDestroy() {
        super.onDestroy()

        BigImageViewer.imageLoader().cancelAll()
    }

    override fun getViewModel(): Class<CropViewModel> = CropViewModel::class.java
}