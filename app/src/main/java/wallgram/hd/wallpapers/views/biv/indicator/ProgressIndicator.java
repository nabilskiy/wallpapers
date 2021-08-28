package wallgram.hd.wallpapers.views.biv.indicator;

import android.view.View;

import wallgram.hd.wallpapers.views.biv.view.BigImageView;

public interface ProgressIndicator {
    /**
     * DO NOT add indicator view into parent! Only called once per load.
     * */
    View getView(BigImageView parent);

    void onStart();

    /**
     * @param progress in range of {@code [0, 100]}
     */
    void onProgress(int progress);

    void onFinish();
}
