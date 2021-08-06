
package wallgram.hd.wallpapers.ui.details;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import wallgram.hd.wallpapers.R;
import wallgram.hd.wallpapers.databinding.FragmentWallpaperDetailsBinding;
import wallgram.hd.wallpapers.model.Gallery;
import wallgram.hd.wallpapers.ui.adapter.DetailsViewPagerAdapter;
import wallgram.hd.wallpapers.ui.adapter.FeedAdapter;
import wallgram.hd.wallpapers.util.AndroidUtilities;
import wallgram.hd.wallpapers.util.Common;
import wallgram.hd.wallpapers.util.Constants;
import wallgram.hd.wallpapers.views.ratedialog.PreferenceHelper;
import wallgram.hd.wallpapers.views.topsheet.TopSheetBehavior;

import static android.content.Context.DOWNLOAD_SERVICE;

public class WallpaperDetailsFragment extends Fragment {


    private static final int REQUEST_PERMISSION_STORAGE = 1;
    public static final String TAG_FRAGMENT = "DetailsFragmentTag";

//    @Inject
//    DetailsContract.Presenter presenter;

    private RewardedAd rewardedAd;

    private FragmentWallpaperDetailsBinding binding;

    private DetailsViewPagerAdapter viewPagerAdapter;
    private int currentItem = 0;

    private Menu menu;

    private Callback callback;
    private GestureDetector gestureDetector;
    private BottomSheetBehavior bottomSheetBehavior;

    private BottomSheetDialog mBottomSheetDialog;
    private TopSheetBehavior tt;

    private FeedAdapter detailsAdapter;
    private GridLayoutManager gridLayoutManager;

    private DownloadManager mgr = null;
   // private Pic.PicData picData = null;

//    @Override
//    public void retryPageLoad() {
//        presenter.nextSimilarPage();
//    }
//
//    @Override
//    public void onItemClicked(ArrayList<Gallery> wallpapersList, int position) {
//        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
//            callback.onImageClicked(wallpapersList, position, presenter.getCurrentPage(), sortType);
//        else bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//    }
//
//    @Override
//    public void onItemClicked(int id, String title) {
//    }
//
//    public void saveBitmap(Bitmap bitmap) {
//        if (bitmap == null)
//            presenter.getBitmap(Glide.with(this)
//                    .asBitmap()
//                    .load(viewPagerAdapter.getItem(binding.viewpager.getCurrentItem()).getOriginal())
//                    .submit());
//        else {
//            saveBitmapToStorage(bitmap);
//            callback.setWallpaper(TAG_FRAGMENT, viewPagerAdapter.getItem(binding.viewpager.getCurrentItem()));
//        }
//    }
//
//    @Override
//    public void clearGlide(FutureTarget futureTarget) {
//        Glide.with(this).clear(futureTarget);
//    }
//
//    public void addToHistory(Gallery galleryItem) {
//        presenter.onHistoryAdd(galleryItem);
//        Log.d(WallpaperDetailsFragment.class.getName(), "ADD");
//    }

    public boolean hideBottomSheet() {
        if (bottomSheetBehavior == null)
            return false;

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        }
        return false;
    }

    public interface Callback {
        void onCropClick(Gallery galleryItem);

        void setWindowState(boolean isFullscreen);

      //  void onImageClicked(ArrayList<Gallery> wallpapersList, int position, int page, SortType sortType);

        void itemClicked(int position, String tag);

        void setWallpaper(String tag, Gallery galleryItem);

        void setToolbarVisibility(boolean b);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (Callback) context;
    }

    public WallpaperDetailsFragment() {
    }

//    public static WallpaperDetailsFragment newInstance(int position, int page, SortType sortType) {
//        WallpaperDetailsFragment fragment = new WallpaperDetailsFragment();
//        Bundle args = new Bundle();
//        args.putInt(Constants.POSITION, position);
//        args.putInt(Constants.PAGE, page);
//        args.putSerializable(Constants.TYPE, sortType);
//        fragment.setArguments(args);
//        return fragment;
//    }

    private BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getActivity() instanceof WallpaperDetailsActivity)
                ((WallpaperDetailsActivity) getActivity()).showLoading(false);
            Toast toast = Toast.makeText(context, getResources().getString(R.string.wallpaper_downloaded), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
     //   ((BaseApplication) getActivity().getApplication()).createDetailsComponent().inject(this);

        boolean isAds = PreferenceHelper.isAds(getActivity());
        if (isAds) {
            createAndLoadRewardedAd();
        }

        mgr = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        viewPagerAdapter = new DetailsViewPagerAdapter(Glide.with(this));

        if (getArguments() != null) {
            currentItem = getArguments().getInt(Constants.POSITION, 0);
       //     sortType = (SortType) getArguments().getSerializable(Constants.TYPE);

            Gson gson = new Gson();
            Type typeGson = new TypeToken<List<Gallery>>() {
            }.getType();
            ArrayList<Gallery> wallpapersList = gson.fromJson(PreferenceHelper.getWallpapersArray(getActivity()), typeGson);

            viewPagerAdapter.addAll(wallpapersList);
        }

//        presenter.setSortType(sortType);
//        Gallery item = viewPagerAdapter.getItem(currentItem);
//        if (item != null)
//            presenter.setId(viewPagerAdapter.getItem(currentItem).getId());
//        presenter.attachView(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWallpaperDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (detailsAdapter.getItemViewType(i) == 0) {
                    return 1;
                }
                return 3;
            }
        });

     //   detailsAdapter = new FeedAdapter(Glide.with(this), this, 22);

        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(detailsAdapter);

        binding.recyclerView.addOnScrollListener(onScrollListener);

        binding.slidingButton.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        // bottomSheetBehavior.setPeekHeight(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? AndroidUtilities.dp(80) : AndroidUtilities.dp(80) + getNavigationBarHeight());

        gestureDetector = new GestureDetector(getActivity(), new SwipeGestureDetector());
        viewPagerAdapter.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));


        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == TopSheetBehavior.STATE_EXPANDED) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.similar_wallpapers);

                    if (menu != null) {
                        for (int i = 0; i < menu.size(); i++) {
                            if (menu.getItem(i) != null)
                                menu.getItem(i).setVisible(false);
                        }

                    }
                } else if (newState == TopSheetBehavior.STATE_COLLAPSED) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                    if (menu != null) {
                        for (int i = 0; i < menu.size(); i++) {
                            if (menu.getItem(i) != null)
                                menu.getItem(i).setVisible(true);
                        }

                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (binding != null) {
                    if (binding.slidingLayout != null)
                        binding.slidingLayout.setAlpha(1 - slideOffset);
                }
            }
        });

        binding.linear.setMaxRows(2);

        ViewCompat.setOnApplyWindowInsetsListener(binding.topSheet, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {

                int paddingTop = insets.getSystemWindowInsetTop();

                TypedValue tv = new TypedValue();
                if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    paddingTop += TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
                }

                v.setPadding(0, paddingTop + convertPixelsToDp(getContext(), 44), 0, convertPixelsToDp(getContext(), 16));

                return insets;
            }
        });


        tt = TopSheetBehavior.from(binding.topSheet);

        binding.downloadButton.setOnClickListener(v -> {
            View v1 = getLayoutInflater().inflate(R.layout.dialog_download_wallpaper, null);

            mBottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.MyBottomSheetDialogTheme);
            mBottomSheetDialog.setContentView(v1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            Button cancelButton = v1.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(v2 -> mBottomSheetDialog.hide());

            ((TextView) v1.findViewById(R.id.port_size)).setText(Common.getResolution(getContext()) + " px");
            ((TextView) v1.findViewById(R.id.album_size)).setText(Common.getLandscapeResolution(getContext()) + " px");

            Gallery item = viewPagerAdapter.getItem(binding.viewpager.getCurrentItem());

        //    ((TextView) v1.findViewById(R.id.original_size)).setText(item.getOriginalWidth() + " x " + item.getOriginalHeight() + " px");

//            if(v1.findViewById(R.id.original_item) != null) v1.findViewById(R.id.original_item).setOnClickListener(v22 -> {
//                if (picData != null) {
//                    mBottomSheetDialog.dismiss();
//                    if (!mBottomSheetDialog.isShowing()) {
//                        boolean isAds = PreferenceHelper.isAds(getActivity());
//                        if (picData.getLinks() != null) {
//                            if (isAds && !App.isRewardShowed)
//                                showRewardedAd(picData.getLinks().getSource());
//                            else {
//                                downloadImage(picData.getLinks().getSource());
//                            }
//                        }
//                    }
//
//                }
//            });

//            if(v1.findViewById(R.id.port_item) != null)
//                v1.findViewById(R.id.port_item).setOnClickListener(v22 -> {
//                if (picData != null) {
//                    if (picData.getLinks() != null) {
//                        downloadImage(picData.getLinks().getPortrait());
//                        mBottomSheetDialog.hide();
//                    }
//                }
//            });

//            if(v1.findViewById(R.id.album_item) != null) v1.findViewById(R.id.album_item).setOnClickListener(v22 -> {
//                if (picData != null) {
//                    if(picData.getLinks() != null){
//                        downloadImage(picData.getLinks().getLandscape());
//                        mBottomSheetDialog.hide();
//                    }
//
//                }
//            });
            // set background transparent
            ((View) v1.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

            mBottomSheetDialog.show();
            mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);
        });

        binding.installButton.setOnClickListener(v -> {
            View v1 = getLayoutInflater().inflate(R.layout.dialog_install_wallpaper, null);

            mBottomSheetDialog = new BottomSheetDialog(getContext(), R.style.MyBottomSheetDialogTheme);
            mBottomSheetDialog.setContentView(v1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            Button cancelButton = v1.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(v2 -> mBottomSheetDialog.hide());

            v1.findViewById(R.id.home_screen).setOnClickListener(v22 -> {
                if (callback != null) {
                    callback.itemClicked(0, TAG_FRAGMENT);
                    mBottomSheetDialog.hide();
                }
            });

            v1.findViewById(R.id.lock_screen).setOnClickListener(v22 -> {
                if (callback != null) {
                    callback.itemClicked(1, TAG_FRAGMENT);
                    mBottomSheetDialog.hide();
                }
            });

            v1.findViewById(R.id.both_screen).setOnClickListener(v22 -> {
                if (callback != null) {
                    callback.itemClicked(2, TAG_FRAGMENT);
                    mBottomSheetDialog.hide();
                }
            });


            // set background transparent
            ((View) v1.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

            mBottomSheetDialog.show();
            mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mBottomSheetDialog = null;
                }
            });
        });

        binding.viewpager.setAdapter(viewPagerAdapter);
        binding.viewpager.setCurrentItem(currentItem, false);
        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (PreferenceHelper.isAds(getActivity())) {
                    if (position > 0 && (position - 10) % 11 == 0) {
                        if (position != currentItem)
                            addBannerAds(position);
                    }
                }

                detailsAdapter.clear();

//                presenter.setId(viewPagerAdapter.getItem(position) != null ? viewPagerAdapter.getItem(position).getId() : 0);
//
//                presenter.getPicInfo();
//                if (position == viewPagerAdapter.getItemCount() - 2) {
//                    presenter.nextPage();
//                }
//
//                changeViewState(viewPagerAdapter.getItem(position) != null);
//
//                presenter.showFavoriteButton(viewPagerAdapter.getItem(position));
            }
        });

        setAds();

      //  presenter.getPicInfo();

        return view;
    }

    public void createAndLoadRewardedAd() {
       /* rewardedAd = new RewardedAd(getContext(),
                "ca-app-pub-6657449436952029/6719990861");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);*/
    }

    private void showRewardedAd(String source) {
       /* if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    createAndLoadRewardedAd();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    downloadImage(source);
                    App.isRewardShowed = true;
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                }
            };
            rewardedAd.show(getActivity(), adCallback);
        }*/
    }

    private void downloadImage(String url) {
        if(url == null)
            return;

        if (getActivity() instanceof WallpaperDetailsActivity)
            ((WallpaperDetailsActivity) getActivity()).showLoading(true);
        String title = System.currentTimeMillis() + ".jpg";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "/Akspic/" + title);

        mgr.enqueue(request);
    }

    int convertPixelsToDp(Context context, int pixels) {
        return (int) (pixels * context.getResources().getDisplayMetrics().density);
    }

    private void changeViewState(boolean b) {
        if (callback != null)
            callback.setToolbarVisibility(b);

        binding.setWallpaperButton.setVisibility(b ? View.VISIBLE : View.GONE);
        binding.bottomSheet.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void setAds() {
        boolean isAds = PreferenceHelper.isAds(getActivity());
        binding.adView.setVisibility(isAds ? View.VISIBLE : View.GONE);
        binding.slidingButton.getLayoutParams().height = isAds ? AndroidUtilities.dp(37) : AndroidUtilities.dp(87);
        if (isAds) {
            binding.adView.loadAd(new AdRequest.Builder().build());
        }
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = gridLayoutManager.getChildCount();
            int totalItemCount = gridLayoutManager.getItemCount();
            int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
              //  presenter.nextSimilarPage();
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.wallpaper_menu, menu);
        this.menu = menu;
      //  presenter.showFavoriteButton(viewPagerAdapter.getItem(binding.viewpager.getCurrentItem()));

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.action_favorite:
                onFavoriteClick();
                return true;
            case R.id.action_crop:
                if (callback != null)
                    callback.onCropClick(viewPagerAdapter.getItem(binding.viewpager.getCurrentItem()));
                return true;
            case R.id.action_info:
                if (tt.getState() == TopSheetBehavior.STATE_EXPANDED)
                    tt.setState(TopSheetBehavior.STATE_COLLAPSED);
                else tt.setState(TopSheetBehavior.STATE_EXPANDED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onFavoriteClick() {
     //   presenter.onFavoriteClick(viewPagerAdapter.getItem(binding.viewpager.getCurrentItem()));
    }

    private void addBannerAds(int position) {
        int nextPosition = position + 1;
        if (viewPagerAdapter.getItemCount() > nextPosition)
            if (viewPagerAdapter.getWallpapers().get(nextPosition) != null)
                viewPagerAdapter.add(nextPosition, null);
    }

    @Override
    public void onDestroyView() {
        binding.adView.destroy();
    //    presenter.detachView();
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        binding.adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.adView.resume();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(onComplete);
      //  ((BaseApplication) getActivity().getApplication()).releaseDetailsComponent();
        super.onDestroy();
    }

//    @Override
//    public void showFavorited() {
//        if (menu != null) {
//            if (menu.getItem(2) != null)
//                menu.getItem(2).setIcon(R.drawable.ic_star_white);
//        }
//    }
//
//    @Override
//    public void showUnFavorited() {
//        if (menu != null)
//            if (menu.getItem(2) != null)
//                menu.getItem(2).setIcon(R.drawable.ic_star_border_white);
//    }
//
//    @Override
//    public void setData(List<Gallery> galleries) {
//        if (viewPagerAdapter == null)
//            return;
//
//        viewPagerAdapter.addAll(galleries);
//    }
//
//    @Override
//    public void showError(String message) {
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void showFooterError(boolean show, String errorMsg) {
//        if (detailsAdapter != null)
//            detailsAdapter.showRetry(show, errorMsg);
//    }
//
//    @Override
//    public void showDetails(Pic.PicData pic) {
//        this.picData = pic;
//        PicMeta picMeta = pic.getPicMeta();
//
//        binding.categoryText.setText(pic.getCategory() != null ? pic.getCategory().getName() : getResources().getString(R.string.other_key));
//        if (pic.getCategory() != null)
//            binding.categoryText.setOnClickListener(v -> startActivity(SubgroupActivity.newIntent(getActivity(), SortType.CATEGORY_ITEMS, pic.getCategory().getId(), pic.getCategory().getName())));
//
//        if (picMeta == null)
//            return;
//
//        setPublishedInfo(pic, picMeta);
//        setLicenseInfo(picMeta, picMeta.getAuthorSource() != null ? picMeta.getAuthorSource() : "");
//        setTagsLayout(pic);
//    }

//    private void setTagsLayout(Pic.PicData picData) {
//        binding.linear.removeAllViews();
//        for (final Tag s : picData.getTags()) {
//            TextView t = new TextView(getContext());
//            t.setBackgroundResource(R.drawable.rounded_corner);
//            t.setTextColor(getResources().getColor(R.color.text_color_selector));
//            t.setText(s.getName());
//         //   t.setOnClickListener(v -> startActivity(SubgroupActivity.newIntent(getActivity(), SortType.TAG_ITEMS, s.getId(), s.getName())));
//            binding.linear.addView(t);
//        }
//    }

//    private void setPublishedInfo(Pic.PicData picData, PicMeta picMeta) {
//        String publishedTxt = picData.getPublished() != null ? picData.getPublished() : "";
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM, d, yyyy", Locale.getDefault());
//        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//        if (!publishedTxt.equals("")) {
//            String pub = publishedTxt.substring(0, publishedTxt.indexOf(' '));
//            try {
//                Date d = date.parse(pub);
//
//                if (d == null)
//                    return;
//
//                publishedTxt = picMeta.getAuthor() == null ?
//                        picData.getUser() == null ? dateFormat.format(d) : picData.getUser().getNick() + ", " + dateFormat.format(d) :
//                        picMeta.getAuthor() + ", " + dateFormat.format(d);
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//        binding.publishedText.setText(publishedTxt);
//    }

//    private void setLicenseInfo(PicMeta picMeta, String authorSource) {
//        String licence = picMeta.getLicense();
//
//        binding.licenseText.setVisibility(licence == null ? GONE : View.VISIBLE);
//        binding.publishedText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_person_icon, 0, licence == null ? 0 : R.drawable.ic_open_in_new, 0);
//        binding.publishedText.setOnClickListener(licence == null ? null : (View.OnClickListener) v -> {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorSource));
//            startActivity(browserIntent);
//        });
//
//        binding.licenseText.setText(licence != null ? getResources().getString(R.string.license_key, licence) : "");
//    }

    private void saveBitmapToStorage(Bitmap bitmap) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            File file = new File(getActivity().getExternalCacheDir(), "savedBitmap.png");

            try {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
    }

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

//    @Override
//    public void clearInfo() {
//        if (detailsAdapter == null)
//            return;
//
//        if (detailsAdapter.getItemCount() > 0)
//            detailsAdapter.clear();
//    }
//
//    @Override
//    public void setSimilarData(List<Gallery> galleries) {
//        if (detailsAdapter != null)
//            detailsAdapter.addAll(galleries);
//    }
//
//    @Override
//    public void showLoading() {
//        binding.progressBar.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void hideLoading() {
//        binding.progressBar.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void removeLoadingFooter() {
//        if (detailsAdapter != null)
//            detailsAdapter.removeLoadingFooter();
//    }
//
//    @Override
//    public void addLoadingFooter() {
//        if (detailsAdapter != null)
//            detailsAdapter.addLoadingFooter();
//    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (tt.getState() == TopSheetBehavior.STATE_EXPANDED) {
                tt.setState(TopSheetBehavior.STATE_COLLAPSED);
                return false;
            }

            if (callback != null)
                callback.setWindowState(binding.setWallpaperButton.getVisibility() == View.VISIBLE);
            binding.setWallpaperButton.setVisibility(binding.setWallpaperButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            binding.bottomSheet.setVisibility(binding.bottomSheet.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            tt.setState(TopSheetBehavior.STATE_COLLAPSED);
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x1 = e1.getX(), y1 = e1.getY();
            float x2 = e2.getX(), y2 = e2.getY();

            double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));

            if (angle > 45 && angle <= 135) {
                if (binding.setWallpaperButton.getVisibility() == View.VISIBLE) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
            return false;
        }
    }
}