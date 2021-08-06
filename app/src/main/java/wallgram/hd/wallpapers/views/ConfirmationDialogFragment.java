package wallgram.hd.wallpapers.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import wallgram.hd.wallpapers.R;

public class ConfirmationDialogFragment extends DialogFragment {

    public interface ConfirmationListener {
        void confirmButtonClicked();
    }

    private ConfirmationListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ConfirmationListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(getContext())
                .setMessage(getResources().getString(R.string.clear_confirm))
                .setPositiveButton(getResources().getString(R.string.clear), (ialog, which) -> listener.confirmButtonClicked())
                .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .create();
    }
}
