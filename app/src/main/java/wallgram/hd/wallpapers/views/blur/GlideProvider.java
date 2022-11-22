package wallgram.hd.wallpapers.views.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

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

    public void setImages(String[] paths){
        mPath = paths;
    }


    @Override
    public Bitmap onProvider(int position) {
        try {
            if (position >= 0 && position < mPath.length) {
                return Glide.with(mContext)
                        .asBitmap().onlyRetrieveFromCache(true)
                        .load(mPath[position])
                        .apply(new RequestOptions().format(DecodeFormat.PREFER_RGB_565))
                        .submit(100,100)
                        .get();
            }
            return null;
        } catch (Exception e) {
            return null;
        }

    }

}
