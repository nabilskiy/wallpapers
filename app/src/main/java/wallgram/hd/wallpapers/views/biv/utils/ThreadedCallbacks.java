package wallgram.hd.wallpapers.views.biv.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Keep;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Keep
public final class ThreadedCallbacks implements InvocationHandler {

    private static final Object NON_SENSE = new Object();
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private final Object mTarget;
    private final Handler mHandler;

    private ThreadedCallbacks(final Handler handler, final Object target) {
        mHandler = handler;
        mTarget = target;
    }

    public static <T> T create(final Class<T> type, final T target) {
        return create(MAIN_HANDLER, type, target);
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(final Handler handler, final Class<T> type, final T target) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(),
                new Class[] { type }, new ThreadedCallbacks(handler, target));
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args)
            throws Throwable {
        if (!method.getReturnType().equals(Void.TYPE)) {
            throw new RuntimeException("Method should return void: " + method);
        }
        if (Looper.myLooper() == mHandler.getLooper()) {
            method.invoke(mTarget, args);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        method.invoke(mTarget, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return NON_SENSE;
    }
}
