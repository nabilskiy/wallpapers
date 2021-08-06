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

import wallgram.hd.wallpapers.util.AndroidUtilities;

public class SetWallpaperDialogFragment extends DialogFragment {

    public interface SelectionListener {
        void itemClicked(DialogInterface dialog, int position, String tag);
    }

    private SelectionListener listener;

    public SetWallpaperDialogFragment() {
        super();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SelectionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Item[] items = {
                new Item(getResources().getString(R.string.home_screen), R.drawable.ic_wallpaper_icon),
                new Item(getResources().getString(R.string.lock_screen), R.drawable.ic_screen_lock),
                new Item(getResources().getString(R.string.both_screens), R.drawable.ic_two_screen),
                new Item(getResources().getString(R.string.upload_gallery), R.drawable.ic_download_icon),
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

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);
                tv.setCompoundDrawablePadding(AndroidUtilities.dp(12));

                return v;
            }
        };

        return new MaterialAlertDialogBuilder(getContext())
                .setAdapter(adapter, (dialog, which) -> listener.itemClicked(dialog, which, getTag()))
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
