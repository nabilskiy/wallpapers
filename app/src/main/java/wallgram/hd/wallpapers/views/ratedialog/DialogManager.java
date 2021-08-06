package wallgram.hd.wallpapers.views.ratedialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import static wallgram.hd.wallpapers.views.ratedialog.PreferenceHelper.setAgreeShowDialog;
import static wallgram.hd.wallpapers.views.ratedialog.PreferenceHelper.setRemindInterval;
import static wallgram.hd.wallpapers.views.ratedialog.Utils.getDialogBuilder;

final class DialogManager {

    private DialogManager() {
    }

    static Dialog create(final Context context, final DialogOptions options) {
        MaterialAlertDialogBuilder builder = getDialogBuilder(context);

        if (options.shouldShowTitle()) builder.setTitle(options.getTitleText(context));

        builder.setCancelable(options.getCancelable());

        View view = options.getView();
        if (view != null) builder.setView(view);

        final OnClickButtonListener listener = options.getListener();

        builder.setPositiveButton(options.getPositiveText(context), (dialog, which) -> {
            setAgreeShowDialog(context, false);
            if (listener != null) listener.onClickButton(which);
        });

        if (options.shouldShowNeutralButton()) {
            builder.setNeutralButton(options.getNeutralText(context), (dialog, which) -> {
                setAgreeShowDialog(context, false);
                if (listener != null) listener.onClickButton(which);
            });
        }

        if (options.shouldShowNegativeButton()) {
            builder.setNegativeButton(options.getNegativeText(context), (dialog, which) -> {
                setRemindInterval(context);
                if (listener != null) listener.onClickButton(which);
            });
        }

        return builder.create();
    }

}