package wallgram.hd.wallpapers.views.biv.view;

public interface ImageSaveCallback {
    void onSuccess(String uri);

    void onFail(Throwable t);
}
