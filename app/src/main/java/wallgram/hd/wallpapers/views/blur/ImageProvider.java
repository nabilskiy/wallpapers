package wallgram.hd.wallpapers.views.blur;

import android.graphics.Bitmap;

import java.util.List;

public interface ImageProvider {

    Bitmap onProvider(int position);
    List<String> keys();

}