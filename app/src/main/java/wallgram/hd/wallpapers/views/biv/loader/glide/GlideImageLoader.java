package wallgram.hd.wallpapers.views.biv.loader.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import wallgram.hd.wallpapers.views.biv.loader.ImageLoader;
import wallgram.hd.wallpapers.views.biv.metadata.ImageInfoExtractor;

public class GlideImageLoader implements ImageLoader {
    protected final RequestManager mRequestManager;

    private final Map<Integer, ImageDownloadTarget> mFlyingRequestTargets = new HashMap<>(3);

    protected GlideImageLoader(Context context, OkHttpClient okHttpClient) {
        GlideProgressSupport.init(Glide.get(context), okHttpClient);
        mRequestManager = Glide.with(context);
    }

    public static GlideImageLoader with(Context context) {
        return with(context, null);
    }

    public static GlideImageLoader with(Context context, OkHttpClient okHttpClient) {
        return new GlideImageLoader(context, okHttpClient);
    }

    @Override
    public void loadImage(final int requestId, final Uri uri, final Callback callback) {
        final boolean[] cacheMissed = new boolean[1];
        ImageDownloadTarget target = new ImageDownloadTarget(uri.toString()) {
            @Override
            public void onResourceReady(@NonNull File resource,
                    Transition<? super File> transition) {
                super.onResourceReady(resource, transition);
                if (cacheMissed[0]) {
                    callback.onCacheMiss(ImageInfoExtractor.getImageType(resource), resource);
                } else {
                    callback.onCacheHit(ImageInfoExtractor.getImageType(resource), resource);
                }
                callback.onSuccess(resource);
            }

            @Override
            public void onLoadFailed(final Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                callback.onFail(new GlideLoaderException(errorDrawable));
            }

            @Override
            public void onDownloadStart() {
                cacheMissed[0] = true;
                callback.onStart();
            }

            @Override
            public void onProgress(int progress) {
                callback.onProgress(progress);
            }

            @Override
            public void onDownloadFinish() {
                callback.onFinish();
            }
        };
        cancel(requestId);
        rememberTarget(requestId, target);

        downloadImageInto(uri, target);
    }

    @Override
    public void prefetch(Uri uri) {
        downloadImageInto(uri, new PrefetchTarget());
    }

    @Override
    public synchronized void cancel(int requestId) {
        clearTarget(mFlyingRequestTargets.remove(requestId));
    }

    @Override
    public synchronized void cancelAll() {
        List<ImageDownloadTarget> targets = new ArrayList<>(mFlyingRequestTargets.values());
        for (ImageDownloadTarget target : targets) {
            clearTarget(target);
        }
    }

    protected void downloadImageInto(Uri uri, Target<File> target) {
        mRequestManager
                .downloadOnly()
                .load(uri)
                .into(target);
    }

    private synchronized void rememberTarget(int requestId, ImageDownloadTarget target) {
        mFlyingRequestTargets.put(requestId, target);
    }

    private void clearTarget(ImageDownloadTarget target) {
        if (target != null) {
            mRequestManager.clear(target);
        }
    }
}
