package wallgram.hd.wallpapers.ui.crop;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;

import wallgram.hd.wallpapers.R;
import wallgram.hd.wallpapers.databinding.FragmentCropBinding;
import wallgram.hd.wallpapers.model.Gallery;
import wallgram.hd.wallpapers.ui.details.WallpaperDetailsFragment;
import wallgram.hd.wallpapers.util.Common;
import wallgram.hd.wallpapers.util.Constants;

public class CropFragment extends Fragment {

    private static final int REQUEST_PERMISSION_STORAGE = 1;
    public static final String TAG_FRAGMENT = "CropFragmentTag";

//    @Inject
//    CropContract.Presenter presenter;

    private FragmentCropBinding binding;

    private Gallery galleryItem;
    private float x = 0;

    private boolean isFullScreen = true;
    private WallpaperDetailsFragment.Callback callback;
    private Menu menu;

    private BottomSheetDialog mBottomSheetDialog;

    public static CropFragment newInstance(Gallery gallery) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.ITEM, gallery);
        CropFragment fragment = new CropFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (WallpaperDetailsFragment.Callback) context;
    }

    public CropFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        ((BaseApplication) getActivity().getApplication()).createCropComponent().inject(this);
//        if (getArguments() != null) {
//            galleryItem = getArguments().getParcelable(Constants.ITEM);
//        }
//
//        presenter.attachView(this);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCropBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

       // binding.errorLayout.btnRetry.setOnClickListener(v -> presenter.loadPicData(galleryItem.getId()));

        binding.cropView.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {

            }

            @Override
            public void onImageLoaded() {
                binding.progressBar.setVisibility(View.GONE);
                float f2 = Common.getHeight(getActivity()) / binding.cropView.getSHeight();
                float f3 = Common.getWidth(getActivity()) / binding.cropView.getSWidth();

                float f1 = f2;
                if (f3 > f2) {
                    f1 = f3;
                }

                binding.cropView.setScaleAndCenter(f1, new PointF(Common.getHeight(getActivity()) * x, 0.0f));
                binding.cropView.setMinimumDpi(50);
                binding.cropView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
            }

            @Override
            public void onPreviewLoadError(Exception e) {

            }

            @Override
            public void onImageLoadError(Exception e) {
                //showError();
            }

            @Override
            public void onTileLoadError(Exception e) {

            }

            @Override
            public void onPreviewReleased() {

            }
        });

        binding.cropView.setOnClickListener(v -> {
            if (callback != null)
                callback.setWindowState(isFullScreen);
            isFullScreen = !isFullScreen;
        });

      //  presenter.loadPicData(galleryItem.getId());

        return view;
    }

    private int getNavigationBarHeight(){
        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.filters_menu, menu);
        this.menu = menu;

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.crop_image_key));
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

      //  setButtonSaveEnabled(false);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_PERMISSION_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            presenter.onSaveClick();
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            View v1 = getLayoutInflater().inflate(R.layout.dialog_install_wallpaper, null);

            mBottomSheetDialog = new BottomSheetDialog(getContext(), R.style.MyBottomSheetDialogTheme);
            mBottomSheetDialog.setContentView(v1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            v1.findViewById(R.id.download_button).setVisibility(View.VISIBLE);
            v1.findViewById(R.id.download_border).setVisibility(View.VISIBLE);


            Button cancelButton = v1.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(v2 -> mBottomSheetDialog.hide());

            v1.findViewById(R.id.home_screen).setOnClickListener(v22 -> {
                if(callback != null) {
                    callback.itemClicked(0, TAG_FRAGMENT);
                    mBottomSheetDialog.hide();
                }
            });

            v1.findViewById(R.id.lock_screen).setOnClickListener(v22 -> {
                if(callback != null) {
                    callback.itemClicked(1, TAG_FRAGMENT);
                    mBottomSheetDialog.hide();
                }
            });

            v1.findViewById(R.id.both_screen).setOnClickListener(v22 -> {
                if(callback != null){
                    callback.itemClicked(2, TAG_FRAGMENT);
                    mBottomSheetDialog.hide();
                }
            });

            v1.findViewById(R.id.download_button).setOnClickListener(v22 -> {
                if(callback != null){
                    callback.itemClicked(3, TAG_FRAGMENT);
                    mBottomSheetDialog.hide();
                }
            });

            // set background transparent
            ((View) v1.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

            mBottomSheetDialog.show();
            mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);
        }
        return super.onOptionsItemSelected(item);
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    //    presenter.detachView();
        binding = null;
    }

//    @Override
//    public void showImage(String uri, float x) {
//        this.x = x;
//
//        presenter.getBitmap(Glide.with(this)
//                .asBitmap()
//                .load(uri)
//                .submit());
//    }

    @Override
    public void onDestroy() {
      //  ((BaseApplication) getActivity().getApplication()).releaseCropComponent();
        super.onDestroy();
    }

//    @Override
//    public void setImageToView(ImageSource imageSource) {
//        binding.cropView.setImage(imageSource);
//    }
//
//    @Override
//    public void showError() {
//        binding.progressBar.setVisibility(View.GONE);
//        binding.errorLayout.errorLayout.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void showLoading() {
//        binding.progressBar.setVisibility(View.VISIBLE);
//        binding.errorLayout.errorLayout.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void saveBitmap(BitmapRegionDecoder bitmapRegionDecoder) {
//        if (bitmapRegionDecoder != null) {
//            Rect rect = new Rect();
//            binding.cropView.visibleFileRect(rect);
//            Bitmap bmp = bitmapRegionDecoder.decodeRegion(rect, null);
//            saveBitmapToStorage(bmp);
//
//            callback.setWallpaper(TAG_FRAGMENT, galleryItem);
//            binding.progressBar.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void setButtonSaveEnabled(boolean isEnabled) {
//        if (menu != null)
//            if (menu.getItem(0) != null) {
//                menu.getItem(0).setEnabled(isEnabled);
//                menu.getItem(0).getIcon().setColorFilter(getResources().getColor(isEnabled ? R.color.colorWhite : R.color.colorGray), PorterDuff.Mode.SRC_ATOP);
//            }
//    }
//
//    @Override
//    public void showToast() {
//        Toast.makeText(getActivity(), getResources().getString(R.string.wrong_error), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void clearGlide(FutureTarget futureTarget) {
//        Glide.with(this).clear(futureTarget);
//    }
//
//    @Override
//    public void hideLoading() {
//        binding.progressBar.setVisibility(View.GONE);
//    }

//    public void saveBitmap() {
//        showLoading();
//        if (presenter != null)
//            presenter.onSaveClick();
//    }
}
