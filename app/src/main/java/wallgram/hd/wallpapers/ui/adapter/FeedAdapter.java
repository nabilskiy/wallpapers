package wallgram.hd.wallpapers.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import wallgram.hd.wallpapers.R;
import wallgram.hd.wallpapers.model.Gallery;
import wallgram.hd.wallpapers.model.PicMeta;
import wallgram.hd.wallpapers.model.SubCategory;
import wallgram.hd.wallpapers.util.ColorFilterTransformation;
import wallgram.hd.wallpapers.util.DynamicParams;
import wallgram.hd.wallpapers.views.FlowLayout;

import static android.view.View.GONE;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int GALLERY_ITEM_VIEW_TYPE = 0;
    private static final int LOADING_ITEM_VIEW_TYPE = 1;
    private static final int CATEGORY_ITEM_VIEW_TYPE = 2;
    private static final int HEADER_ITEM_VIEW_TYPE = 4;
    private static final int BANNER_AD_VIEW_TYPE = 3;
    private int ITEMS_PER_AD;

    private ArrayList<Gallery> newsList;
    private final RequestManager mGlide;
    private boolean isLoadingAdded = false;
    private String errorMsg;
    private boolean retryPageLoad = false;

    public FeedAdapter(RequestManager mGlide, int i) {
        newsList = new ArrayList<>();
        this.mGlide = mGlide;

        this.ITEMS_PER_AD = i;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case GALLERY_ITEM_VIEW_TYPE:
                View itemView = inflater.inflate(R.layout.item_photo, parent, false);
                viewHolder = new ImagesViewHolder(itemView, parent.getContext());
                break;
            case CATEGORY_ITEM_VIEW_TYPE:
                View categoryView = inflater.inflate(R.layout.item_category, parent, false);
                viewHolder = new CategoryViewHolder(categoryView, parent.getContext());
                break;
            case LOADING_ITEM_VIEW_TYPE:
                View loadingView = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new LoadingViewHolder(loadingView);
                break;
            case HEADER_ITEM_VIEW_TYPE:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, parent, false);
                viewHolder = new HeaderViewHolder(headerView, parent.getContext());
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = getItem(position);
        switch (getItemViewType(position)) {
            case GALLERY_ITEM_VIEW_TYPE:
                ((ImagesViewHolder) holder).bind((Gallery) item);
                break;
            case CATEGORY_ITEM_VIEW_TYPE:
                ((CategoryViewHolder) holder).bind((SubCategory) item);
                break;
            case LOADING_ITEM_VIEW_TYPE:
                ((LoadingViewHolder) holder).bind();
                break;
            case HEADER_ITEM_VIEW_TYPE:
                ((HeaderViewHolder) holder).bind((Gallery) item);
        }
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == newsList.size() - 1 && isLoadingAdded)
            return LOADING_ITEM_VIEW_TYPE;

        Object item = getItem(position);
        if (item instanceof SubCategory)
            return CATEGORY_ITEM_VIEW_TYPE;

        if (item instanceof Gallery) {
//            if (((Gallery) item).getPicData() != null) {
//                return HEADER_ITEM_VIEW_TYPE;
//            }
//            if (PreferenceHelper.isAds(view.getContext())) {
//                if (position > 0 && (position - ITEMS_PER_AD) % 22 == 0) {
//                    return BANNER_AD_VIEW_TYPE;
//                }
//            }
        }

        return GALLERY_ITEM_VIEW_TYPE;
    }

    private Object getItem(int position) {
        return newsList.get(position);
    }

    private void add(Gallery article) {
        newsList.add(article);
        notifyItemInserted(newsList.size() - 1);
    }

    public void addAll(Object newsResults) {
        if (newsResults instanceof ArrayList) {
            newsList.addAll((ArrayList) newsResults);
            int size = newsList.size();
            notifyItemRangeInserted(size == 0 ? 0 : size - 1, ((ArrayList) newsResults).size());
        }
    }

    public void remove(Gallery article) {
        int position = newsList.indexOf(article);
        if (position > -1) {
            newsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ArrayList<Gallery> getNewsList() {
        return newsList;
    }

    public void setNewsList(ArrayList<Gallery> newsList) {
        this.newsList = newsList;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add((Gallery) null);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        if (getItemCount() > 0) {
            int position = newsList.size() - 1;

            if (getItem(position) == null) {
                newsList.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public void clear() {
        int size = newsList.size();
        newsList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(newsList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

//    public void add(Pic.PicData pic) {
//       // Gallery item = new Gallery();
//       // item.setPicData(pic);
//       // newsList.add(item);
//        notifyItemInserted(newsList.size() - 1);
//    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if(holder instanceof ImagesViewHolder){
            ImageView imageView = ((ImagesViewHolder) holder).mPhotoImageView;
            if(imageView != null){
                Glide.with(imageView.getContext()).clear(imageView);
            }
        }
        super.onViewRecycled(holder);
    }

    class ImagesViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mPhotoImageView;

        ImagesViewHolder(View itemView, Context context) {
            super(itemView);

            itemView.getLayoutParams().height = DynamicParams.instance.a(context).y;
            mPhotoImageView = itemView.findViewById(R.id.iv_photo);

            itemView.setTag(this);
        }

        void bind(Gallery gallery) {
            if (gallery == null)
                return;

            mGlide.load(gallery.getPreview())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .apply(new RequestOptions().format(DecodeFormat.PREFER_RGB_565))
                    .apply(new RequestOptions().placeholder(new ColorDrawable(Color.parseColor("#252831"))))
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .into(mPhotoImageView);

//            itemView.setOnClickListener(v -> {
//                if (view instanceof FeedContract.View){
//                    ((FeedContract.View) view).onItemClicked(getNewsList(), getAdapterPosition());
//                }
//                else if (view instanceof DetailsContract.View){
//                    ArrayList<Gallery> galleries = new ArrayList<>(getNewsList());
//                    if(galleries.get(0).getId() == null)
//                        galleries.remove(0);
//                    ((DetailsContract.View) view).onItemClicked(galleries, getAdapterPosition());
//                }
//            });
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ProgressBar mProgressBar;
        private final TextView mErrorTxt;
        private final LinearLayout mErrorLayout;

        LoadingViewHolder(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            ImageButton mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

         //   mRetryBtn.setBackground(Theme.createSelectorDrawable(Theme.ACTION_BAR_WHITE_SELECTOR_COLOR));
            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        void bind() {
            if (retryPageLoad) {

                mErrorLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);

                mErrorTxt.setText(
                        errorMsg != null ?
                                errorMsg :
                                itemView.getResources().getString(R.string.error_msg));

            } else {
                mErrorLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

//                    showRetry(false, null);
//                    if (view instanceof FeedContract.View)
//                        ((FeedContract.View) FeedAdapter.this.view).retryPageLoad();
//                    else if (view instanceof DetailsContract.View)
//                        ((DetailsContract.View) FeedAdapter.this.view).retryPageLoad();
//                    break;
            }
        }
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView categoryName;
        private final ShapeableImageView imageView;

        CategoryViewHolder(View itemView, Context context) {
            super(itemView);

            itemView.getLayoutParams().height = DynamicParams.instance.c(context).y;

            imageView = itemView.findViewById(R.id.image);
            categoryName = itemView.findViewById(R.id.name);
        }

        void bind(SubCategory item) {
            if (item == null)
                return;

            mGlide.load(item.getBackground())
                    .apply(RequestOptions.bitmapTransform(new ColorFilterTransformation(Color.argb(80, 0, 0, 0))))
                    .apply(new RequestOptions().format(DecodeFormat.PREFER_RGB_565))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .apply(new RequestOptions().placeholder(new ColorDrawable(Color.parseColor("#252831"))))
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .into(imageView);

            categoryName.setText(item.getName());
//            itemView.setOnClickListener(v -> {
//                if (view instanceof FeedContract.View)
//                    ((FeedContract.View) view).onItemClicked(item.getId(), item.getName());
//                else if (view instanceof DetailsContract.View)
//                    ((DetailsContract.View) view).onItemClicked(item.getId(), item.getName());
//            });
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView categoryText;
        private final TextView publishedText;
        private final TextView licenseText;
        private final FlowLayout linear;

        private final Context context;

        HeaderViewHolder(View itemView, Context context) {
            super(itemView);

            this.context = context;

            categoryText = itemView.findViewById(R.id.category_text);
            categoryText.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(context, R.drawable.ic_category), null, null, null);
            publishedText = itemView.findViewById(R.id.published_text);
            publishedText.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(context, R.drawable.ic_person_icon), null, null, null);
            licenseText = itemView.findViewById(R.id.license_text);
            licenseText.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources.getDrawable(context, R.drawable.ic_license), null, null, null);
            linear = itemView.findViewById(R.id.linear);

            linear.setMaxRows(2);
        }

        void bind(Gallery item) {
//            Pic.PicData picData = item.getPicData();
//            PicMeta picMeta = picData.getPicMeta();
//
//            categoryText.setText(picData.getCategory() != null ? picData.getCategory().getName() : context.getResources().getString(R.string.other_key));
//            if(picData.getCategory() != null)
//                categoryText.setOnClickListener(v -> context.startActivity(SubgroupActivity.newIntent(context, SortType.CATEGORY_ITEMS, picData.getCategory().getId(), picData.getCategory().getName())));
//
//            if(picMeta == null)
//                return;
//
//            setPublishedInfo(picData, picMeta);
//            setLicenseInfo(picMeta, picMeta.getAuthorSource() != null ? picMeta.getAuthorSource() : "");
//            setTagsLayout(picData);
        }

//        private void setTagsLayout(Pic.PicData picData) {
//            linear.removeAllViews();
//            for (final Tag s : picData.getTags()) {
//                TextView t = new TextView(context);
//                t.setBackgroundResource(R.drawable.rounded_corner);
//                t.setTextColor(context.getResources().getColor(R.color.text_color_selector));
//                t.setText(s.getName());
//              //  t.setOnClickListener(v -> context.startActivity(SubgroupActivity.newIntent(context, SortType.TAG_ITEMS, s.getId(), s.getName())));
//                linear.addView(t);
//            }
//        }

//        private void setPublishedInfo(Pic.PicData picData, PicMeta picMeta) {
//            String publishedTxt = picData.getPublished() != null ? picData.getPublished() : "";
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM, d, yyyy", Locale.getDefault());
//            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//            if(!publishedTxt.equals("")){
//                String pub = publishedTxt.substring(0, publishedTxt.indexOf(' '));
//                try {
//                    Date d = date.parse(pub);
//
//                    if (d == null)
//                        return;
//
//                    publishedTxt = picMeta.getAuthor() == null ?
//                            picData.getUser() == null ? dateFormat.format(d) : picData.getUser().getNick() + ", " + dateFormat.format(d) :
//                            picMeta.getAuthor() + ", " + dateFormat.format(d);
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            publishedText.setText(publishedTxt);
//        }

        private void setLicenseInfo(PicMeta picMeta, String authorSource) {
            String licence = picMeta.getLicense();

            licenseText.setVisibility(licence == null ? GONE : View.VISIBLE);
            publishedText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_person_icon, 0, licence == null ? 0 : R.drawable.ic_open_in_new, 0);
            publishedText.setOnClickListener(licence == null ? null : (View.OnClickListener) v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorSource));
                context.startActivity(browserIntent);
            });

            licenseText.setText(licence != null ? context.getResources().getString(R.string.license_key, licence) : "");
        }
    }
}
