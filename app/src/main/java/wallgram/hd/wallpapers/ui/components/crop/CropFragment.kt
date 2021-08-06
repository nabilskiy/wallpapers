package wallgram.hd.wallpapers.ui.components.crop

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import wallgram.hd.wallpapers.databinding.FragmentCropBinding
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.util.Common
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.getResolution
import wallgram.hd.wallpapers.util.withArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class CropFragment : BaseFragment<CropViewModel, FragmentCropBinding>(
        FragmentCropBinding::inflate
) {

    companion object {
        private const val ARG_GALLERY = "arg_gallery"
        fun create(gallery: Gallery) = CropFragment().withArgs(
                ARG_GALLERY to gallery
        )
    }

    private val item: Gallery by args(ARG_GALLERY)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPic(item.id, requireContext().getResolution(), "ru")

        viewModel.picLiveData.observe(viewLifecycleOwner, {
            it.data?.let { pic ->
                Glide.with(requireContext()).asBitmap().load(pic.links.landscape
                        ?: pic.links.source)
                        .into(object : CustomViewTarget<SubsamplingScaleImageView, Bitmap>(binding.cropView) {
                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                binding.progressBar.isVisible = false
                            }

                            override fun onResourceCleared(placeholder: Drawable?) {
                                // clear all resources
                            }

                            override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                                binding.progressBar.isVisible = false
                                binding.cropView.setImage(ImageSource.bitmap(resource))

                                val f2 = (wallgram.hd.wallpapers.util.Common.getHeight(activity) / binding.cropView.sHeight).toFloat()
                                val f3 = (wallgram.hd.wallpapers.util.Common.getWidth(activity) / binding.cropView.sWidth).toFloat()

                                var f1 = f2
                                if (f3 > f2) {
                                    f1 = f3
                                }

                                binding.cropView.setScaleAndCenter(f1, PointF(wallgram.hd.wallpapers.util.Common.getHeight(activity) * pic.focus[0], 0.0f))
                                binding.cropView.setMinimumDpi(50)
                                binding.cropView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                            }
                        })
            }
        })


    }

    override fun getViewModel(): Class<CropViewModel> = CropViewModel::class.java
}