package wallgram.hd.wallpapers.ui.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import wallgram.hd.wallpapers.R;
import wallgram.hd.wallpapers.model.Gallery;

public class DetailsViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int IMAGE_ITEM_VIEW_TYPE = 0;
    private static final int BANNER_AD_VIEW_TYPE = 1;

    private View.OnTouchListener mOnTouchListener;
    private final RequestManager mGlide;

    private List<Gallery> wallpapersResult;

    public DetailsViewPagerAdapter(RequestManager mGlide) {
        this.mGlide = mGlide;
        wallpapersResult = new ArrayList<>();
    }

    public View.OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }

    public void setOnTouchListener(View.OnTouchListener mOnTouchListener) {
        this.mOnTouchListener = mOnTouchListener;
    }

    public List<Gallery> getWallpapers() {
        return wallpapersResult;
    }

    public void setWallpapers(List<Gallery> wallpapersResult) {
        this.wallpapersResult = wallpapersResult;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case IMAGE_ITEM_VIEW_TYPE:
                View itemView = inflater.inflate(R.layout.item_pager_image, parent, false);
                viewHolder = new ImagePagerVH(itemView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case IMAGE_ITEM_VIEW_TYPE:
                Gallery item = wallpapersResult.get(position);
                if (item == null)
                    return;

                final ImagePagerVH pagerViewHolder = (ImagePagerVH) holder;
                pagerViewHolder.progressBar.setVisibility(View.VISIBLE);
                mGlide.load(item.getOriginal())
                        .thumbnail(mGlide.load(item.getPreview()))
                        .apply(new RequestOptions().skipMemoryCache(true))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                pagerViewHolder.progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                pagerViewHolder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(pagerViewHolder.mPhotoImageView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return wallpapersResult == null ? 0 : wallpapersResult.size();
    }

    @Override
    public long getItemId(int position) {
        return wallpapersResult.get(position).getId();
    }

    public Gallery getItem(int position) {
        if (wallpapersResult != null && position < wallpapersResult.size())
            return wallpapersResult.get(position);
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) == null)
            return BANNER_AD_VIEW_TYPE;
        return IMAGE_ITEM_VIEW_TYPE;
    }

    public void removeLoadingFooter() {
        int position = wallpapersResult.size() - 1;
        Gallery result = getItem(position);

        if (result == null) {
            wallpapersResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(Gallery g) {
        wallpapersResult.add(g);
        notifyItemInserted(wallpapersResult.size() - 1);
    }

    public void add(int position, Gallery obj) {
        wallpapersResult.add(position, obj);
        notifyItemInserted(position);
    }

    public void addAll(List<Gallery> galleryResults) {
        wallpapersResult.addAll(galleryResults);
        notifyItemRangeInserted(wallpapersResult.size() - 1, galleryResults.size());
    }

    public void firstAddAll(List<Gallery> galleryResults) {
        wallpapersResult.addAll(galleryResults);
        notifyItemRangeInserted(0, galleryResults.size());
    }

    public void remove(Gallery g) {
        int position = wallpapersResult.indexOf(g);
        if (position > -1) {
            wallpapersResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    class ImagePagerVH extends RecyclerView.ViewHolder {

        private final ImageView mPhotoImageView;
        private final ProgressBar progressBar;

        ImagePagerVH(@NonNull View itemView) {
            super(itemView);

            mPhotoImageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.progress_bar);

            itemView.setOnTouchListener(mOnTouchListener);
        }
    }
}
