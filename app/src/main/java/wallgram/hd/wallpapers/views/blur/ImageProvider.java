package wallgram.hd.wallpapers.views.blur;

import android.graphics.Bitmap;

public interface ImageProvider {

    Bitmap onProvider(int position);
}