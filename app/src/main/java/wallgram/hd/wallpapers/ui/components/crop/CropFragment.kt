package wallgram.hd.wallpapers.ui.components.crop

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import wallgram.hd.wallpapers.databinding.FragmentCropBinding
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.util.Common
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.withArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.util.getResolution
import wallgram.hd.wallpapers.views.biv.BigImageViewer
import wallgram.hd.wallpapers.views.biv.loader.glide.GlideImageLoader
import wallgram.hd.wallpapers.views.biv.view.GlideImageViewFactory

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
            setImageViewFactory(GlideImageViewFactory())
            showImage(Uri.parse(landscapeLink))

        }
    }


    override fun getViewModel(): Class<CropViewModel> = CropViewModel::class.java
}