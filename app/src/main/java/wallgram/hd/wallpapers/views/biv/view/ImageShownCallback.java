package wallgram.hd.wallpapers.views.biv.view;

import androidx.annotation.UiThread;

@UiThread
public interface ImageShownCallback {

    void onThumbnailShown();
    void onMainImageShown();
}
