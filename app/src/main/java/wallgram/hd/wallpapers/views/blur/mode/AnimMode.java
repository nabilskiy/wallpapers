package wallgram.hd.wallpapers.views.blur.mode;

import android.widget.ImageView;

public interface AnimMode {

    int D_LEFT = -1;
    int D_RIGHT = 1;

    void transformPage(ImageView ivBg, float position, int direction);
}