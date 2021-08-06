package wallgram.hd.wallpapers.util;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import wallgram.hd.wallpapers.R;

public class DynamicParams {

    public static final DynamicParams instance = new DynamicParams();
    private final Point a = new Point();

    public Point a(@NonNull Context paramContext)
    {
        Point localPoint = b(paramContext);
        int i = paramContext.getResources().getDimensionPixelSize(R.dimen.line_width_normal);
        i = (localPoint.x - i * 2) / 3;
        return new Point(i, localPoint.y * i / localPoint.x);
    }

    public Point c(@NonNull Context paramContext)
    {
        Point localPoint = b(paramContext);
        int i = paramContext.getResources().getDimensionPixelSize(R.dimen.line_width_normal);
        i = (localPoint.x - i * 2) / 9;
        return new Point(i, localPoint.y * i / localPoint.x);
    }

    private Point b(@NonNull Context paramContext)
    {
        Point localPoint = new Point();
        WindowManager window = (WindowManager)paramContext.getSystemService(Context.WINDOW_SERVICE);
        if (window != null) {
            window.getDefaultDisplay().getRealSize(localPoint);
        }
        return localPoint;
    }

    public final Point previewSize()
    {
        return this.a;
    }
}
