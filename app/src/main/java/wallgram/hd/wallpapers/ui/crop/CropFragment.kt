package wallgram.hd.wallpapers.ui.crop

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import wallgram.hd.wallpapers.databinding.FragmentCropBinding
import wallgram.hd.wallpapers.ui.base.BaseFragment
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

import java.io.*
import android.util.Log
import androidx.core.graphics.toRect
import wallgram.hd.wallpapers.ui.dialogs.CropDialogFragment
import wallgram.hd.wallpapers.ui.dialogs.DOWNLOAD_TO_GALLERY
import wallgram.hd.wallpapers.ui.dialogs.InstallDialogFragment
import android.database.Cursor
import android.graphics.*
import android.view.Gravity
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showDrawable
import com.github.razir.progressbutton.showProgress
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.model.Config


class CropFragment : BaseFragment<CropViewModel, FragmentCropBinding>(
    FragmentCropBinding::inflate
) {

    companion object {
        private const val ARG_LANDSCAPE = "arg_landscape"
        fun create(landscape: String) = CropFragment().withArgs(
            ARG_LANDSCAPE to landscape
        )
    }

    private val installLiveDataPrivate = MutableLiveData<Resource<Uri>>()
    private var selectedType: Int = 0

    private val landscapeLink: String by args(ARG_LANDSCAPE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BigImageViewer.initialize(GlideImageLoader.with(requireContext()))
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.color_status_bar)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        installLiveDataPrivate.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading ->{
                    binding.progressBar.isVisible = true
                }
                is Resource.Success ->{
                    binding.progressBar.isVisible = false
                    showToast(if(selectedType == DOWNLOAD_TO_GALLERY) R.string.image_dowloaded else R.string.wallpaper_setted)
                }
                is Resource.DataError ->{
                    binding.progressBar.isVisible = false
                }
            }
        })

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

                        false
                    }


                    CropDialogFragment().show(
                        childFragmentManager,
                        CropDialogFragment::class.java.simpleName
                    )
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

    private fun showToast(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).apply{
            setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorBackground))

            val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textView.gravity = Gravity.CENTER_HORIZONTAL
        }.show()
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

    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String): File {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
            return File(getPath(uri!!))
        } else {
            val directory =
                File(Environment.getExternalStorageDirectory().toString() + "/" + folderName)
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
            return file
        }
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            requireContext().contentResolver.query(uri, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }

    private fun contentValues(): ContentValues {
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

    fun setWallpaperForScreen(wallpaper: File?, type: Int){
        val config = Config.Builder().setWallpaperMode(type).build()
        lifecycleScope.launch(Dispatchers.IO) {
            if (wallpaper == null || !wallpaper.exists()) {
                throw IOException("Download wallpaper failure")
            }
            val uiHelper = UIHelper()
            val result = uiHelper.setWallpaper(requireContext(), config, wallpaper)

            installLiveDataPrivate.postValue(Resource.Success(result))
        }
    }

    override fun getViewModel(): Class<CropViewModel> = CropViewModel::class.java
    fun setWallpaper(type: Int) {

        selectedType = type
        installLiveDataPrivate.postValue(Resource.Loading())
        val decoder: BitmapRegionDecoder? = BitmapRegionDecoder.newInstance(
            FileInputStream(binding.mBigImage.currentImageFile),
            false
        )
        if (decoder != null) {
            val rect = binding.mBigImage.croppedRect

            val bitmap = decoder.decodeRegion(rect, null)
            val file = saveImage(bitmap, requireContext(), "Wallgram")

            if(type != DOWNLOAD_TO_GALLERY){
                setWallpaperForScreen(file, type)
            } else{
                installLiveDataPrivate.postValue(Resource.Success(Uri.fromFile(file)))
            }

            bitmap.recycle()

        }
    }
}