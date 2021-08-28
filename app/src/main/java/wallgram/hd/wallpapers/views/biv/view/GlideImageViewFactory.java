package wallgram.hd.wallpapers.views.biv.view;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import wallgram.hd.wallpapers.views.biv.metadata.ImageInfoExtractor;

public class GlideImageViewFactory extends ImageViewFactory {
    @Override
    protected final View createAnimatedImageView(final Context context, final int imageType,
            final int initScaleType) {
        switch (imageType) {
            case ImageInfoExtractor.TYPE_GIF:
            case ImageInfoExtractor.TYPE_ANIMATED_WEBP: {
                final ImageView imageView = new ImageView(context);
                imageView.setScaleType(BigImageView.scaleType(initScaleType));
                return imageView;
            }
            default:
                return super.createAnimatedImageView(context, imageType, initScaleType);
        }
    }

    @Override
    public final void loadAnimatedContent(final View view, final int imageType,
            final File imageFile) {
        switch (imageType) {
            case ImageInfoExtractor.TYPE_GIF:
            case ImageInfoExtractor.TYPE_ANIMATED_WEBP: {
                if (view instanceof ImageView) {
                    Glide.with(view.getContext())
                            .load(imageFile)
                            .into((ImageView) view);
                }
                break;
            }

            default:
                super.loadAnimatedContent(view, imageType, imageFile);
        }
    }

    @Override
    public void loadThumbnailContent(final View view, final Uri thumbnail) {
        if (view instanceof ImageView) {
            Glide.with(view.getContext())
                    .load(thumbnail)
                    .into((ImageView) view);
        }
    }
}
