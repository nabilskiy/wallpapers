package wallgram.hd.wallpapers.views.biv.view;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

import wallgram.hd.wallpapers.views.biv.metadata.ImageInfoExtractor;

public class ImageViewFactory {

    public final View createMainView(final Context context, final int imageType,
            final int initScaleType) {
        switch (imageType) {
            case ImageInfoExtractor.TYPE_GIF:
            case ImageInfoExtractor.TYPE_ANIMATED_WEBP:
                return createAnimatedImageView(context, imageType, initScaleType);
            case ImageInfoExtractor.TYPE_STILL_WEBP:
            case ImageInfoExtractor.TYPE_STILL_IMAGE:
            default:
                return createStillImageView(context);
        }
    }

    public boolean isAnimatedContent(final int imageType) {
        switch (imageType) {
            case ImageInfoExtractor.TYPE_GIF:
            case ImageInfoExtractor.TYPE_ANIMATED_WEBP:
                return true;
            default:
                return false;
        }
    }

    protected SubsamplingScaleImageView createStillImageView(final Context context) {
        return new SubsamplingScaleImageView(context);
    }

    public void loadSillContent(final View view, final Uri uri) {
        if (view instanceof SubsamplingScaleImageView) {
            ((SubsamplingScaleImageView) view).setImage(ImageSource.uri(uri));
        }
    }

    protected View createAnimatedImageView(final Context context, final int imageType,
            final int initScaleType) {
        return null;
    }

    public void loadAnimatedContent(final View view, final int imageType, final File imageFile) {
    }

    public View createThumbnailView(final Context context, final ImageView.ScaleType scaleType,
            final boolean willLoadFromNetwork) {
        final ImageView thumbnailView = new ImageView(context);
        if (scaleType != null) {
            thumbnailView.setScaleType(scaleType);
        }
        return thumbnailView;
    }

    public void loadThumbnailContent(final View view, final File thumbnail) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageURI(Uri.fromFile(thumbnail));
        }
    }

    public void loadThumbnailContent(final View view, final Uri thumbnail) {
    }
}
