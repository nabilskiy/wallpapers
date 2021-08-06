package wallgram.hd.wallpapers.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import wallgram.hd.wallpapers.R;
import wallgram.hd.wallpapers.util.AndroidUtilities;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class OriginalWallpaperDialogFragment extends DialogFragment {

    public interface OriginalWallpaperListener {
        void originalClicked(DialogInterface dialog, int position, String tag);
    }

    private OriginalWallpaperListener listener;

    public OriginalWallpaperDialogFragment() {
        super();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OriginalWallpaperListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OriginalWallpaperListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Item[] items = {
                new Item(getResources().getString(R.string.portrait), R.drawable.ic_portrait),
                new Item(getResources().getString(R.string.landscape), R.drawable.ic_landscape),
                new Item(getResources().getString(R.string.original), R.drawable.ic_photo_fullsize)
        };

        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(
                getContext(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                items){
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = v.findViewById(android.R.id.text1);
                tv.setTextSize(16);
                tv.setHeight(AndroidUtilities.dp(14));

                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);
                tv.setCompoundDrawablePadding(AndroidUtilities.dp(12));

                return v;
            }
        };

        return new MaterialAlertDialogBuilder(getContext())
                .setAdapter(adapter, (dialog, which) -> listener.originalClicked(dialog, which, getTag()))
                .create();
    }

    static class Item{
        final String text;
        final int icon;
        Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @NonNull
        @Override
        public String toString() {
            return text;
        }
    }

}
