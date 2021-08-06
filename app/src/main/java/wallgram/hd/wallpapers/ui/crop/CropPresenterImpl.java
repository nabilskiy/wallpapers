package wallgram.hd.wallpapers.ui.crop;

import android.graphics.BitmapRegionDecoder;

import com.bumptech.glide.request.FutureTarget;

import io.reactivex.disposables.Disposable;

public class CropPresenterImpl{


    private Disposable fetchSubscription;
    private Disposable bitmapSubscription;
    private Disposable decoderSubscription;

    private FutureTarget futureTarget;

    private BitmapRegionDecoder mDecoder;

//    @Override
//    public void loadPicData(int id) {
//        if (isAttached())
//            getView().showLoading();
//
//        fetchSubscription = model.getWallpaperDetails(id, Common.getResolutionReverse(BaseApplication.getContext()), Locale.getDefault().getLanguage())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(this::showLoading)
//                .subscribe(this::onWallpapersFetchSuccess, this::onWallpapersFetchFailed);
//    }
//
//    private void hideLoading() {
//        if(isAttached())
//            getView().hideLoading();
//    }
//
//    private void showLoading(Disposable disposable) {
//        if(isAttached())
//            getView().showLoading();
//    }
//
//    @Override
//    public void getBitmap(FutureTarget futureTarget) {
//        this.futureTarget = futureTarget;
//        bitmapSubscription = Observable.fromCallable(new CallableLongAction(futureTarget))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(bitmap -> {
//                    if (isAttached())
//                        getView().setImageToView(ImageSource.bitmap(bitmap));
//
//                    decoderSubscription = Completable.fromAction(() -> createDecoder(bitmap))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(() -> {
//                                if (isAttached()) {
//                                    getView().setButtonSaveEnabled(true);
//                                }
//                            }, throwable -> {
//                            });
//                }, throwable -> {});
//    }
//
//    @Override
//    public void onSaveClick() {
//        if(isAttached())
//            getView().saveBitmap(mDecoder);
//    }
//
//    private void createDecoder(Bitmap bitmap) {
//        InputStream is;
//        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
//            byte[] bitmapdata = bos.toByteArray();
//            is = new ByteArrayInputStream(bitmapdata);
//            mDecoder = BitmapRegionDecoder.newInstance(new BufferedInputStream(is), true);
//        } catch (IOException e) {
//            if (isAttached())
//                getView().showToast();
//        }
//    }
//
//
//    private void onWallpapersFetchFailed(Throwable throwable) {
//        if (isAttached())
//            getView().showError();
//    }
//
//    private void onWallpapersFetchSuccess(Pic.PicData picData) {
//        if (isAttached())
//            getView().showImage(picData.getOriginal(), picData.getFocus()[0]);
//    }
//
//    @Override
//    public void detachView() {
//        if(isAttached())
//            getView().clearGlide(futureTarget);
//        super.detachView();
//        RxUtils.unsubscribe(fetchSubscription, decoderSubscription, bitmapSubscription);
//    }
//
//    static class CallableLongAction implements Callable<Bitmap> {
//
//        private final FutureTarget data;
//
//        CallableLongAction(FutureTarget data) {
//            this.data = data;
//        }
//
//        @Override
//        public Bitmap call() throws Exception {
//            return (Bitmap) data.get();
//        }
//    }
}
