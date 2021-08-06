package wallgram.hd.wallpapers.views.ratedialog;

import android.content.Context;
import android.os.Build;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

final class Utils {

    private Utils() {
    }

    static boolean isLollipop() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    static MaterialAlertDialogBuilder getDialogBuilder(Context context) {
      return new MaterialAlertDialogBuilder(context);
    }

}
