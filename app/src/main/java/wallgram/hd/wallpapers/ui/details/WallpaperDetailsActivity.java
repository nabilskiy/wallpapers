package wallgram.hd.wallpapers.ui.details;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wallgram.hd.wallpapers.App;
import wallgram.hd.wallpapers.R;
import wallgram.hd.wallpapers.databinding.ActivityWallpaperDetailsBinding;
import wallgram.hd.wallpapers.model.Gallery;
import wallgram.hd.wallpapers.ui.crop.CropFragment;
import wallgram.hd.wallpapers.util.Constants;
import wallgram.hd.wallpapers.util.LockScreenUtil;
import wallgram.hd.wallpapers.views.OriginalWallpaperDialogFragment;
import wallgram.hd.wallpapers.views.ratedialog.PreferenceHelper;

public class WallpaperDetailsActivity extends AppCompatActivity implements WallpaperDetailsFragment.Callback,
        OriginalWallpaperDialogFragment.OriginalWallpaperListener {

    private static final int REQUEST_PERMISSION_STORAGE = 1;
    private ActivityWallpaperDetailsBinding binding;

    private Disposable fetchSubscription;
    //private InterstitialAd mInterstitialAd;


    private int installType = 0;

    private boolean isClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWallpaperDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);



        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(Constants.PAGE) && extras.containsKey(Constants.POSITION) && extras.containsKey(Constants.TYPE)) {
                int currentPage = extras.getInt(Constants.PAGE, -1);
                int currentPosition = extras.getInt(Constants.POSITION, -1);
               // SortType currentType = (SortType) extras.getSerializable(Constants.TYPE);

//                if (currentPage != -1 && currentPosition != -1 && currentType != null) {
//                    WallpaperDetailsFragment wallpaperDetailsFragment = WallpaperDetailsFragment.newInstance(currentPosition, currentPage, currentType);
//                    getSupportFragmentManager().beginTransaction().add(R.id.wallpaper_details_container, wallpaperDetailsFragment, WallpaperDetailsFragment.TAG_FRAGMENT).commit();
//                }
            }
        }


        if (PreferenceHelper.isAds(this)) {
//            mInterstitialAd = new InterstitialAd(this);
//            mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_banner_id));
//            mInterstitialAd.setAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    App.isInterstitialShowed = true;
//                    finish();
//                }
//            });
//            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClick = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (!fragment.isVisible()) continue;

            WallpaperDetailsFragment detailsFragment = (WallpaperDetailsFragment) getSupportFragmentManager().findFragmentByTag(WallpaperDetailsFragment.TAG_FRAGMENT);
            if (detailsFragment != null)
                if (detailsFragment.hideBottomSheet())
                    return;

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                if (goToBackActivity()) return;
            }
        }
    }

    private boolean goToBackActivity() {
//        if (PreferenceHelper.isAds(this))
//            if (!App.isInterstitialShowed)
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                    return true;
//                }
//        finish();
        return false;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(App.localeHelper.setLocale(base));
    }

    @Override
    public void onCropClick(Gallery galleryItem) {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.wallpaper_details_container, CropFragment.newInstance(galleryItem), CropFragment.TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setWindowState(boolean isFullscreen) {
        if (isFullscreen)
            hideSystemUI();
        else showSystemUI();
    }

//    @Override
//    public void onImageClicked(ArrayList<Gallery> wallpapersList, int position, int page, SortType sortType) {
//        if (isClick)
//            return;
//        isClick = true;
//
//        Gson gson = new Gson();
//        String json = gson.toJson(wallpapersList);
//
//        PreferenceHelper.setWallpapersArray(this, json);
//
//        Intent intent = new Intent(this, WallpaperDetailsActivity.class);
//        Bundle extras = new Bundle();
//        extras.putInt(Constants.POSITION, position);
//        extras.putInt(Constants.PAGE, page);
//        extras.putSerializable(Constants.TYPE, sortType);
//        intent.putExtras(extras);
//        startActivity(intent);
//    }

    @Override
    public void itemClicked(int position, String tag) {
        installType = position;
        switch (tag) {
            case CropFragment.TAG_FRAGMENT:
                CropFragment cropFragment = (CropFragment) getSupportFragmentManager().findFragmentByTag(CropFragment.TAG_FRAGMENT);
//                if (cropFragment != null)
//                    cropFragment.saveBitmap();
                break;
            case WallpaperDetailsFragment.TAG_FRAGMENT:
                WallpaperDetailsFragment detailsFragment = (WallpaperDetailsFragment) getSupportFragmentManager().findFragmentByTag(WallpaperDetailsFragment.TAG_FRAGMENT);
//                if (detailsFragment != null)
//                    detailsFragment.saveBitmap(null);
                break;
        }
    }

    public void showLoading(boolean show){
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setWallpaper(String tag, Gallery galleryItem) {
        if (!tag.equals(WallpaperDetailsFragment.TAG_FRAGMENT))
            onBackPressed();

        File file = new File(getExternalCacheDir(), "savedBitmap.png");

        if (!file.exists())
            return;


        WallpaperDetailsFragment detailsFragment = (WallpaperDetailsFragment) getSupportFragmentManager().findFragmentByTag(WallpaperDetailsFragment.TAG_FRAGMENT);
//        if (detailsFragment != null)
//            detailsFragment.addToHistory(galleryItem);

        if (PreferenceHelper.isLimitedDownload(this)) {
            PreferenceHelper.setInstallTimes(this, PreferenceHelper.getInstallTimes(this) + 1);
//            if (PreferenceHelper.getInstallTimes(this) > 10) {
//                startActivity(new Intent(this, SubscribeActivity.class));
//                PreferenceHelper.setInstallInterval(this);
//                return;
//            }
        }

        fetchSubscription = Completable.fromAction(() -> setBitmapToWallpaper(file))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> binding.progressBar.setVisibility(View.VISIBLE))
                .doFinally(() -> binding.progressBar.setVisibility(View.GONE))
                .subscribe(this::showToast, throwable -> {

                });

        //удалять файл после установки
    }

    @Override
    public void setToolbarVisibility(boolean b) {
        binding.toolbar.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void showToast() {
        Toast toast = Toast.makeText(this, getResources().getString(installType == 3 ? R.string.wallpaper_downloaded : R.string.wallpaper_installed), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void setBitmapToWallpaper(File file) throws IOException {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);

        switch (installType) {
            case 0:
                wallpaperManager.setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi"))
                        downloadLockWallpaper(file);
                    else {
                        wallpaperManager.setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), null, false, WallpaperManager.FLAG_LOCK);
                    }
                } else {
                    downloadLockWallpaper(file);
                }
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), null, false, WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
                    if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi"))
                        downloadLockWallpaper(file);
                } else {
                    wallpaperManager.setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    downloadLockWallpaper(file);
                }
                break;
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    insertImage(getContentResolver(), BitmapFactory.decodeFile(file.getAbsolutePath()), String.valueOf(System.currentTimeMillis()), null);
                else
                    insertImage(BitmapFactory.decodeFile(file.getAbsolutePath()), String.valueOf(System.currentTimeMillis()));
                break;
        }

    }

    public final void insertImage(Bitmap source,
                                  String title) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Akspic");

            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, title + ".jpg");
            if (file.exists()) {
                file.delete();
            }

            if (source != null) {
                try {
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(file);
                        source.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public final String insertImage(ContentResolver cr,
                                    Bitmap source,
                                    String title,
                                    String description) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, title);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
            values.put(MediaStore.Images.Media.DESCRIPTION, description);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());

            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Akspic");


            Uri url = null;
            String stringUrl = null;
            try {
                url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                if (source != null) {
                    try (OutputStream imageOut = cr.openOutputStream(url)) {
                        source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
                    }
                } else {
                    cr.delete(url, null, null);
                    url = null;
                }
            } catch (Exception e) {
                if (url != null) {
                    cr.delete(url, null, null);
                    url = null;
                }
            }

            if (url != null) {
                stringUrl = url.toString();
            }

            return stringUrl;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            return "";
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void downloadLockWallpaper(File file) {
        if (file.exists()) {
            LockScreenUtil mLockScreenUtils = new LockScreenUtil(this);
            Intent localIntent = mLockScreenUtils.getLockScreenIntent(Build.MANUFACTURER, file.getAbsolutePath());
            try {
                startActivity(localIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, getResources().getString(R.string.wrong_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        binding.toolbar.setVisibility(View.GONE);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        binding.toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void originalClicked(DialogInterface dialog, int position, String tag) {
        dialog.dismiss();
        if (tag.equals(WallpaperDetailsFragment.TAG_FRAGMENT)) {
            WallpaperDetailsFragment detailsFragment = (WallpaperDetailsFragment) getSupportFragmentManager().findFragmentByTag(WallpaperDetailsFragment.TAG_FRAGMENT);
//            if (detailsFragment != null) {
//                if (position == 0) {
//                    detailsFragment.saveBitmap(null);
//                } else if (position == 1) {
//
//                } else {
//
//                }
//            }
//            detailsFragment.saveBitmap(null);
        }
    }
}
