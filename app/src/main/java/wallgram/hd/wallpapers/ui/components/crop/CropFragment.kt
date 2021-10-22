package wallgram.hd.wallpapers.ui.components.crop

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapRegionDecoder
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import java.lang.Exception
import android.widget.Toast

import android.app.WallpaperManager
import android.graphics.Rect
import java.io.*
import java.lang.IllegalArgumentException


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
                wallgram.hd.wallpapers.R.id.action_save -> {
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


                setCropped()
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

    private fun setCropped() {

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

    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else {
            val directory = File(Environment.getExternalStorageDirectory().toString() + "/" + folderName)
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            if (file.absolutePath != null) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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