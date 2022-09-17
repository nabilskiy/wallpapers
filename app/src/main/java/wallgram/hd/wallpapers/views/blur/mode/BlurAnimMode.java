package wallgram.hd.wallpapers.views.blur.mode;

import android.widget.ImageView;

public class BlurAnimMode implements AnimMode {

    private static final int DEFAULT_BLUR_RADIUS = 100;
    private int mRadius;

    public BlurAnimMode() {
        this.mRadius = DEFAULT_BLUR_RADIUS;
    }

    public BlurAnimMode(int radius) {
        this.mRadius = radius;
    }

    @Override
    public void transformPage(ImageView ivBg, float position, int direction) {
        float mFraction = (float) Math.cos(2 * Math.PI * position);
        if (mFraction < 0)
            mFraction = 0;
            ivBg.setAlpha(mFraction);
    }

    public int getBlurRadius() {
        return mRadius;
    }
}