package wallgram.hd.wallpapers.views.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
                        .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .load(mPath[position])
                        .submit()
                        .get();
            }
            return null;
        } catch (Exception e) {
            return null;
        }

    }
}
