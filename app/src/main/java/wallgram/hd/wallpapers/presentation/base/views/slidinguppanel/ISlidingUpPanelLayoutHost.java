package wallgram.hd.wallpapers.presentation.base.views.slidinguppanel;

import android.widget.ScrollView;

import androidx.recyclerview.widget.RecyclerView;

public interface ISlidingUpPanelLayoutHost {

    void onRecyclerViewAttached(RecyclerView view);

    void onViewDetached();

    void hide();

}