package wallgram.hd.wallpapers.views.blur;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import org.checkerframework.checker.units.qual.A;

import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import coil.Coil;
import coil.ImageLoader;
import coil.memory.MemoryCache;
import coil.util.CoilUtils;
import wallgram.hd.wallpapers.R;
import wallgram.hd.wallpapers.util.FastBlur;
import wallgram.hd.wallpapers.views.blur.mode.AnimMode;
import wallgram.hd.wallpapers.views.blur.mode.BlurAnimMode;

public class ECardFlowLayout extends FrameLayout {

    private static final int SWITCH_ANIM_TIME = 300;    //300 ms
    private static final int MSG_JUDGE_RESET = 0x1;

    private Context mContext;
    private ExecutorService mThreadPool;
    private MyHandler mHandler;
    private NotifyRunnable mNotifyRunnable;

    private ImageView mBgImage;
    private ImageView mBlurImage;
    private ViewPager2 mViewPager;

    private AnimMode mAnimMode;

    private RecyclingBitmapDrawable curBp, lastBp, nextBp;
    private Bitmap curBlurBp, lastBlurBp, nextBlurBp;

    private int mCurDirection = AnimMode.D_RIGHT;
    private int mSwitchAnimTime = SWITCH_ANIM_TIME;
    private float mLastOffset;
    private int mRadius;
    private int mCurPosition;
    private int mLastPosition;
    private boolean isSwitching;

    private ArrayList<String> images;

    private ImageLoader loader;

    public ECardFlowLayout(Context context) {
        super(context);
    }

    public ECardFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        loader = Coil.imageLoader(mContext);

        images = new ArrayList<>();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.attr_layout);
        mSwitchAnimTime = ta.getInt(R.styleable.attr_layout_switchAnimTime, SWITCH_ANIM_TIME);
        ta.recycle();

        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initViewPager();
    }

    public void setImages(List<String> images, int targetPosition) {
        this.images.clear();
        this.images.addAll(images);

        curBp = loadBitmap(targetPosition);
        nextBp = loadBitmap(targetPosition + 1);
        if (mAnimMode == null) {
            throw new IllegalStateException("You should setAnimMode before setImageProvider");
        }
        if (mBlurImage != null) {
            curBlurBp = blurBitmap(targetPosition);
            nextBlurBp = blurBitmap(targetPosition + 1);
            mBlurImage.setImageBitmap(blurBitmap(targetPosition));
        }
        if (curBp != null)
            mBgImage.setImageBitmap(curBp.getBitmap());
    }

    private void init() {
        mThreadPool = Executors.newCachedThreadPool();
        mNotifyRunnable = new NotifyRunnable();
        mHandler = new MyHandler(this);
        mBlurImage = new ImageView(mContext);
        initImageView(mBlurImage);
        mBgImage = new ImageView(mContext);
        initImageView(mBgImage);
    }

    private void initImageView(ImageView image) {
        image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(image);
    }

    private void initViewPager() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ViewPager2) {
                mViewPager = (ViewPager2) getChildAt(i);
            }
        }
        if (mViewPager == null) {
            throw new IllegalStateException("Can't find ViewPager in ECardFlowLayout");
        }

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset != 0 && positionOffset != mLastOffset) {
                    if (positionOffset < mLastOffset) {
                        mCurDirection = AnimMode.D_LEFT;
                    } else {
                        mCurDirection = AnimMode.D_RIGHT;
                    }
                    mLastOffset = positionOffset;
                }
                int lastPosition = Math.round(position + positionOffset);
                if (mLastPosition != lastPosition) {
                    if (mCurDirection == AnimMode.D_LEFT) {
                        switchBgToLast(lastPosition);
                    } else {
                        switchBgToNext(lastPosition);
                    }
                }
                mLastPosition = lastPosition;
                if (mAnimMode != null) {
                    mAnimMode.transformPage(mBgImage, position + positionOffset, mCurDirection);
                }
            }
        });

        mBgImage.setVisibility(GONE);
    }

    private void updateNextRes(final int position) {
        mCurPosition = position;
        detachBitmap(lastBp);
        lastBp = curBp;
        curBp = nextBp;
        if (mBlurImage != null) {
            recycleBitmap(lastBlurBp);
            lastBlurBp = curBlurBp;
            curBlurBp = nextBlurBp;
        }

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                nextBp = loadBitmap(position + 1);
                if (mBlurImage != null) {
                    nextBlurBp = blurBitmap(position + 1);
                }
                sendMsg();
            }
        });

    }

    private void updateLastRes(final int position) {
        mCurPosition = position;
        detachBitmap(nextBp);
        nextBp = curBp;
        curBp = lastBp;
        if (mBlurImage != null) {
            recycleBitmap(nextBlurBp);
            nextBlurBp = curBlurBp;
            curBlurBp = lastBlurBp;
        }

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                lastBp = loadBitmap(position - 1);
                if (mBlurImage != null) {
                    lastBlurBp = blurBitmap(position - 1);
                }
                sendMsg();
            }
        });
    }

    private void startTrans(int targetPosition, ImageView targetImage, RecyclingBitmapDrawable startBp, RecyclingBitmapDrawable endBp) {
        if (endBp == null)
            endBp = loadBitmap(targetPosition);

        if (startBp == null || endBp == null)
            return;

        TransitionDrawable td = new TransitionDrawable(new Drawable[]{startBp, endBp});
        targetImage.setImageDrawable(td);
        td.setCrossFadeEnabled(true);
        td.startTransition(mSwitchAnimTime);
    }

    public void switchCurrentBg(final int targetPosition) {
        if (isSwitching) {
            return;
        }
        isSwitching = true;
        startTrans(targetPosition, mBgImage, curBp, nextBp);
        if (mBlurImage != null) {
            startTrans(targetPosition, mBlurImage, new RecyclingBitmapDrawable(getResources(), curBlurBp), new RecyclingBitmapDrawable(getResources(), nextBlurBp));
        }
        mNotifyRunnable.setTarget(targetPosition, true);
        mBgImage.postDelayed(mNotifyRunnable, mSwitchAnimTime);
    }

    public void switchBgToNext(final int targetPosition) {
        if (isSwitching) {
            return;
        }
        isSwitching = true;
        startTrans(targetPosition + 1, mBgImage, curBp, nextBp);
        if (mBlurImage != null) {
            startTrans(targetPosition + 1, mBlurImage, new RecyclingBitmapDrawable(getResources(), curBlurBp), new RecyclingBitmapDrawable(getResources(), nextBlurBp));
        }
        mNotifyRunnable.setTarget(targetPosition, true);
        mBgImage.postDelayed(mNotifyRunnable, mSwitchAnimTime);
    }

    private void switchBgToLast(final int targetPosition) {
        if (isSwitching) {
            return;
        }
        isSwitching = true;
        startTrans(targetPosition - 1, mBgImage, curBp, lastBp);
        if (mBlurImage != null) {
            startTrans(targetPosition - 1, mBlurImage, new RecyclingBitmapDrawable(getResources(), curBlurBp), new RecyclingBitmapDrawable(getResources(), lastBlurBp));
        }
        mNotifyRunnable.setTarget(targetPosition, false);
        mBgImage.postDelayed(mNotifyRunnable, mSwitchAnimTime);
    }

    public void jumpBgToTarget(final int targetPosition) {
        mCurPosition = targetPosition;
        if (isSwitching) {
            return;
        }
        isSwitching = true;
        final RecyclingBitmapDrawable newBitmap = loadBitmap(targetPosition);
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{curBp, newBitmap});
        mBgImage.setImageDrawable(td);
        td.setCrossFadeEnabled(true);
        td.startTransition(mSwitchAnimTime);
        if (mBlurImage != null) {
            TransitionDrawable tdb = new TransitionDrawable(new Drawable[]{new BitmapDrawable(mContext.getResources(), curBlurBp),
                    new BitmapDrawable(mContext.getResources(), blurBitmap(targetPosition))});
            mBlurImage.setImageDrawable(tdb);
            tdb.setCrossFadeEnabled(true);
            tdb.startTransition(mSwitchAnimTime);
        }
        mBgImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        detachBitmap(nextBp);
                        detachBitmap(lastBp);
                        detachBitmap(curBp);
                        curBp = newBitmap;
                        nextBp = loadBitmap(targetPosition + 1);
                        lastBp = loadBitmap(targetPosition - 1);
                        if (mBlurImage != null) {
                            recycleBitmap(nextBlurBp);
                            recycleBitmap(lastBlurBp);
                            recycleBitmap(curBlurBp);
                            curBlurBp = blurBitmap(targetPosition);
                            nextBlurBp = blurBitmap(targetPosition + 1);
                            lastBlurBp = blurBitmap(targetPosition - 1);
                        }
                        sendMsg();
                    }
                });
            }
        }, mSwitchAnimTime);
    }

    private void sendMsg() {
        Message msg = new Message();
        msg.what = MSG_JUDGE_RESET;
        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    private void judgeReset() {
        isSwitching = false;
        if (Math.abs(mCurPosition - mLastPosition) <= 1) {
            if (mCurPosition > mLastPosition) {
                switchBgToLast(mLastPosition);
            } else if (mCurPosition < mLastPosition) {
                switchBgToNext(mLastPosition);
            }
        } else {
            jumpBgToTarget(mLastPosition);
        }
    }

    @Nullable
    private Bitmap blurBitmap(int targetPosition) {
        // RecyclingBitmapDrawable bitmapDrawable = mLruCache.get(String.valueOf(targetPosition));
        RecyclingBitmapDrawable bitmapDrawable = null;

        MemoryCache memoryCache = loader.getMemoryCache();
        if (memoryCache != null) {
            if (targetPosition >= 0 && targetPosition < images.size()) {
                MemoryCache.Key key = new MemoryCache.Key(images.get(targetPosition), Collections.emptyMap());
                MemoryCache.Value value = memoryCache.get(key);
                if (value != null) {
                    bitmapDrawable = new RecyclingBitmapDrawable(getResources(), value.getBitmap());
                }
            }
        }

        if (bitmapDrawable == null || bitmapDrawable.getBitmap() == null) {
            return null;
        }

        if (bitmapDrawable.getBitmap().isRecycled())
            return null;

        Bitmap blurBitmap = bitmapDrawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);

        blurBitmap = FastBlur.blur(blurBitmap, mRadius, true);

        darkenBitMap(blurBitmap);

        return blurBitmap;
    }

    private void darkenBitMap(Bitmap bm) {

        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(0xC1C1C1);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xC1C1C1, 0x00000000);    // darken
        p.setColorFilter(filter);
       // canvas.drawBitmap(bm, new Matrix(), p);

    }




    private RecyclingBitmapDrawable loadBitmap(int targetPosition) {

        RecyclingBitmapDrawable bitmap = null;

        MemoryCache memoryCache = loader.getMemoryCache();
        if (memoryCache != null) {
            if (targetPosition >= 0 && targetPosition < images.size()) {
                MemoryCache.Key key = new MemoryCache.Key(images.get(targetPosition), Collections.emptyMap());
                MemoryCache.Value value = memoryCache.get(key);
                if (value != null) {
                    bitmap = new RecyclingBitmapDrawable(getResources(), value.getBitmap());
                }
            }
        }

        // RecyclingBitmapDrawable bitmap = mLruCache.get(String.valueOf(targetPosition));
        if (bitmap != null) {
            bitmap.setIsDisplayed(true);


        }

        if(bitmap != null && bitmap.getBitmap() != null && bitmap.getBitmap().isRecycled()){
            return null;
        }

        return bitmap;
    }

    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
    }

    private void detachBitmap(RecyclingBitmapDrawable bitmap) {
        if (bitmap != null) {
            bitmap.setIsDisplayed(false);
        }
    }

    public void setImageProvider(ImageProvider provider, int targetPosition) {
        curBp = loadBitmap(targetPosition);
        nextBp = loadBitmap(targetPosition + 1);
        if (mAnimMode == null) {
            throw new IllegalStateException("You should setAnimMode before setImageProvider");
        }
        if (mBlurImage != null) {
            curBlurBp = blurBitmap(targetPosition);
            nextBlurBp = blurBitmap(targetPosition + 1);
            mBlurImage.setImageBitmap(blurBitmap(targetPosition));
        }
        if (curBp != null)
            mBgImage.setImageBitmap(curBp.getBitmap());
    }

    public void setAnimMode(AnimMode animMode) {
        mAnimMode = animMode;
        mRadius = mAnimMode.blurRadius();
    }

    public void setSwitchAnimTime(int switchAnimTime) {
        mSwitchAnimTime = switchAnimTime;
    }

    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        if (!mThreadPool.isShutdown()) {
            mThreadPool.shutdown();
        }
        mHandler = null;
        detachBitmap(curBp);
        detachBitmap(lastBp);
        detachBitmap(nextBp);
        if (mBlurImage != null) {
            recycleBitmap(curBlurBp);
            recycleBitmap(lastBlurBp);
            recycleBitmap(nextBlurBp);
        }
    }

    private class NotifyRunnable implements Runnable {

        private int targetPosition;
        private boolean isNext;

        @Override
        public void run() {
            if (isNext) {
                updateNextRes(targetPosition);
            } else {
                updateLastRes(targetPosition);
            }
        }

        void setTarget(int targetPosition, boolean isNext) {
            this.targetPosition = targetPosition;
            this.isNext = isNext;
        }
    }

    private static class MyHandler extends Handler {

        WeakReference<ECardFlowLayout> mLayout;

        MyHandler(ECardFlowLayout layout) {
            mLayout = new WeakReference<>(layout);
        }

        @Override
        public void handleMessage(Message msg) {
            ECardFlowLayout layout = mLayout.get();
            switch (msg.what) {
                case MSG_JUDGE_RESET:
                    layout.judgeReset();
                    break;
            }
        }
    }
}