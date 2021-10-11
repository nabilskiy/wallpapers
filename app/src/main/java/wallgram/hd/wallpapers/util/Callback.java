package wallgram.hd.wallpapers.util;

import android.util.Log;

public interface Callback<T> {
    /**
     * start
     */
    void onPreExecute();

    /**
     * stop
     */
    void onPostExecute();

    void onSuccess(T t);

    void onError(Throwable e);

    void onFinish();

    class EmptyCallback<T> implements Callback<T> {
        @Override
        public void onPreExecute() {

        }

        @Override
        public void onPostExecute() {

        }

        @Override
        public void onSuccess(T t) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onFinish() {

        }
    }

    /**
     * @author liaoheng
     * @version 2015年9月16日
     */
    class LogEmptyCallback<T> implements Callback<T> {
        private String TAG = LogEmptyCallback.class.getSimpleName();

        public LogEmptyCallback(String TAG) {
            this.TAG = TAG;
        }

        public LogEmptyCallback() {
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute() {
        }

        @Override
        public void onSuccess(T t) {
            Log.d(TAG, " onSuccess : " + t);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        }

        @Override
        public void onFinish() {
        }
    }
}