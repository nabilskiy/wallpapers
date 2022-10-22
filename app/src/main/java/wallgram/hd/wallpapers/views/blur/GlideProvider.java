package wallgram.hd.wallpapers.views.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import wallgram.hd.wallpapers.util.ColorFilterTransformation;

public class GlideProvider implements ImageProvider {

    private int width, height;
    private Context mContext;
    private String[] mPath;

    public GlideProvider(Context mContext, String[] path, int width, int height) {
        this.mPath = path;
        this.mContext = mContext;
        this.width = width;
        this.height = height;
    }


    @Override
    public Bitmap onProvider(int position) {
        try {
            if (position >= 0 && position < mPath.length) {
                return Glide.with(mContext)
                        .asBitmap().onlyRetrieveFromCache(true)
                        .load(mPath[position])
                        .apply(
                                RequestOptions.bitmapTransform(
                                        new ColorFilterTransformation(
                                                Color.argb(50, 48, 48, 48)
                                        )
                                )
                        )
                        .submit()
                        .get();
            }
            return null;
        } catch (Exception e) {
            return null;
        }

    }
}
