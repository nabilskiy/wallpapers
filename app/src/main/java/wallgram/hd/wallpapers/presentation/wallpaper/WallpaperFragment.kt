package wallgram.hd.wallpapers.presentation.wallpaper

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.work.WorkInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.DEFAULT_SKU
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.core.data.ads.interstitial.AdInterstitial
import wallgram.hd.wallpapers.core.data.ads.interstitial.AdInterstitialCallback
import wallgram.hd.wallpapers.core.data.permissions.Permission
import wallgram.hd.wallpapers.core.data.permissions.PermissionProvider
import wallgram.hd.wallpapers.data.workers.WallpaperApplier.Companion.APPLY_EXTERNAL_KEY
import wallgram.hd.wallpapers.data.workers.WallpaperApplier.Companion.APPLY_OPTION_KEY
import wallgram.hd.wallpapers.data.workers.WallpaperDownloader.Companion.DOWNLOAD_FILE_EXISTED
import wallgram.hd.wallpapers.data.workers.WallpaperDownloader.Companion.DOWNLOAD_PATH_KEY
import wallgram.hd.wallpapers.databinding.FragmentWallpaperBinding
import wallgram.hd.wallpapers.presentation.base.BaseSlidingUpFragment
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.views.slidinguppanel.Features
import wallgram.hd.wallpapers.presentation.dialogs.DownloadAction
import wallgram.hd.wallpapers.presentation.dialogs.DownloadPopup
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.util.*
import wallgram.hd.wallpapers.util.blurhash.BlurHash
import wallgram.hd.wallpapers.util.blurhash.blurPlaceHolder
import wallgram.hd.wallpapers.views.blur.DrawableImageProvider
import wallgram.hd.wallpapers.views.blur.GlideProvider
import wallgram.hd.wallpapers.views.blur.mode.BlurAnimMode
import java.io.File

@AndroidEntryPoint
class WallpaperFragment : BaseSlidingUpFragment<WallpaperViewModel, FragmentWallpaperBinding>(
    FragmentWallpaperBinding::inflate
) {

    override val viewModel: WallpaperViewModel by viewModels()

    private var isLoadingAds = false
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var mAdIsLoading: Boolean = false
    private var timeRemaining: Long = 0L

    private val dialog: DownloadPopup by lazy {
        DownloadPopup.Base(binding.root, layoutInflater.inflate(R.layout.view_loading, null, false))
    }

    private lateinit var permissionProvider: PermissionProvider

    private val adInterstitial = AdInterstitial.Base()

    //private val itemId: Int by args(ITEM_ID, 0)

    companion object {
        private const val ITEM_ID = "id"

        fun newInstance(id: Int) = WallpaperFragment().withArgs(ITEM_ID to id)
    }

    override fun onDestroyView() {
        dialog.hide()
        adInterstitial.show(requireActivity())

        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.cancelWorkManagerTasks()
    }

    private fun checkSubscriptionAndLoadAds() {
        viewModel.getCurrentSub().observe(viewLifecycleOwner, {
            if (it == DEFAULT_SKU)
                loadAds()
        })
    }

    private fun loadAds() {

//        if (!cacheManager.isRewardedAdShowed)
//            if (rewardedInterstitialAd == null && !isLoadingAds) {
//                loadRewardedInterstitialAd()
//            }
//
//        if (!cacheManager.isInterstitialAdShowed) {
//            if (!mAdIsLoading && mInterstitialAd == null) {
//                mAdIsLoading = true
//                loadAd()
//            }
//        }

    }

    private fun loadRewardedInterstitialAd() {
        if (rewardedInterstitialAd == null) {
            isLoadingAds = true
            val adRequest = AdRequest.Builder().build()

            // Load an ad.
//            RewardedInterstitialAd.load(
//                    requireContext(),
//                    "ca-app-pub-3722478150829941/2334662603",
//                    adRequest,
//                    object : RewardedInterstitialAdLoadCallback() {
//                        override fun onAdFailedToLoad(adError: LoadAdError) {
//                            super.onAdFailedToLoad(adError)
//                            Log.d(
//                                    WallpaperFragment::class.java.name,
//                                    "onAdFailedToLoad: ${adError.message}"
//                            )
//                            isLoadingAds = false
//                            rewardedInterstitialAd = null
//                        }
//
//                        override fun onAdLoaded(rewardedAd: RewardedInterstitialAd) {
//                            super.onAdLoaded(rewardedAd)
//                            Log.d(WallpaperFragment::class.java.name, "Ad was loaded.")
//
//                            rewardedInterstitialAd = rewardedAd
//                            isLoadingAds = false
//                        }
//                    })
        }
    }

    private fun showRewardedVideo(url: String) {
        if (rewardedInterstitialAd == null) {
            //download(url)
            return
        }

        rewardedInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(WallpaperFragment::class.java.name, "Ad was dismissed.")

                // Don't forget to set the ad reference to null so you
                // don't show the ad a second time.
                rewardedInterstitialAd = null

                // if (rewardItem != null)
                // download(url)

                // Preload the next rewarded interstitial ad.
                loadRewardedInterstitialAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d(WallpaperFragment::class.java.name, "Ad failed to show.")

                // Don't forget to set the ad reference to null so you
                // don't show the ad a second time.
                rewardedInterstitialAd = null
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(WallpaperFragment::class.java.name, "Ad showed fullscreen content.")
            }
        }

        rewardedInterstitialAd?.show(
            requireActivity()
        ) { rewardItem ->
            //  cacheManager.isRewardedAdShowed = true
            this@WallpaperFragment.rewardItem = rewardItem
        }
    }

    private var rewardItem: RewardItem? = null

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.color_status_bar)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionProvider =
            PermissionProvider.Base(this)

        adInterstitial.load(requireActivity(), null)
    }

    private fun currentItem(): ItemUi {
        return galleryAdapter.getItems()[binding.list.currentItem]
    }

    private val galleryAdapter =
        GalleryFullAdapter(object : GenericAdapter.ClickListener<GalleryUi> {
            override fun click(item: GalleryUi) {

            }
        })

    override fun back() {
        if (panelShowed()) {
            super.back()
            return
        } else viewModel.back()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkSubscriptionAndLoadAds()


        with(binding) {

           // intArrayOf(R.drawable.background_subscription, R.drawable.category_frame, R.drawable.subscribe_bg)


            viewModel.wallpapersLiveData.observe(viewLifecycleOwner) {
                it.map(galleryAdapter)
                it.map(object: Mapper.Unit<List<ItemUi>>{
                    override fun map(data: List<ItemUi>) {
                        val list = data.map { item -> item.uri().first }
                        ecardflowLayout.apply {
                            setAnimMode(BlurAnimMode())
                            setImageProvider(GlideProvider(requireContext(), list.toTypedArray(), 1080, 1920), binding.list.currentItem)
                        }
                    }

                })

            }

            viewModel.observeUpdate(viewLifecycleOwner) { isFavorite ->
                favoriteBtn.check(isFavorite)
                viewModel.update()
            }

            viewModel.positionLiveData.observe(viewLifecycleOwner) {
                binding.list.setCurrentItem(it, false)
                binding.ecardflowLayout.switchBgToNext(it - 1)
            }

            toolbar.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(top = rect.top + windowInsetsCompat.systemWindowInsetTop)
                windowInsetsCompat
            }

            slidingUpPanelFragmentContainer.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(top = rect.top + windowInsetsCompat.systemWindowInsetTop)
                windowInsetsCompat
            }

            list.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(top = rect.top + windowInsetsCompat.systemWindowInsetTop, bottom = rect.bottom + windowInsetsCompat.systemWindowInsetBottom)
                windowInsetsCompat
            }

            buttonsContainer.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(bottom = rect.bottom + windowInsetsCompat.systemWindowInsetBottom)
                windowInsetsCompat
            }

            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                viewModel.back()
            }

            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_info -> show(Features.Info(currentItem().id().toInt()))
                }
                true
            }


//            blurLayout.viewBehind = viewToBlur
//            blurLayout.updateForMilliSeconds(1000)

            list.clipToPadding = false
            list.clipChildren = false
            list.offscreenPageLimit = 3
            list.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val compositePageTransformer = CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(30.dp))
                addTransformer { page, position ->
                    val r: Float = 1 - Math.abs(position)
                    page.scaleY = 0.9f + r * 0.1f
                }
            }

            list.setPageTransformer(compositePageTransformer)

            list.apply {
                adapter = galleryAdapter

                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                    override fun onPageSelected(position: Int) {
                        val item = currentItem()

                        favoriteBtn.check(item.isFavorite())
                        viewModel.loadMoreData(
                            position
                        )

                    }
                })
            }

            similarBtn.setOnClickListener {
                show(Features.Similar(WallpaperRequest.SIMILAR(currentItem().id().toInt())))
            }

            favoriteBtn.setOnClickListener {
                val item = currentItem()
                viewModel.changeFavorite(item)
            }

            downloadBtn.setOnClickListener {
                show(Features.Download())
            }

            viewModel.init()

        }
    }

    private fun getNavigationBarHeight(): Int {
        val resources: Resources = resources
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    private fun hideAllViews() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showAllViews() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            binding.root
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    fun handle(t: DownloadAction) {
        hideIfOpenedFeature()

        permissionProvider
            .request(Permission.Storage)
            .rationale("Необходим доступ к хранилищу")
            .checkPermission { granted ->
                if (granted)
                    downloadOrApply(t)
            }
    }

    private fun downloadOrApply(t: DownloadAction) {
        val id = currentItem().id().toInt()
        viewModel.cancelWorkManagerTasks()
        currentItem().changeHistory()

        when (t) {
            is DownloadAction.Home -> {
                viewModel.install(0, id).observe(viewLifecycleOwner) { info ->
                    onApplyResult(info)
                }
            }
            is DownloadAction.Lock -> {
                viewModel.install(1, id).observe(viewLifecycleOwner) { info ->
                    onApplyResult(info)
                }
            }
            is DownloadAction.Both -> {
                viewModel.install(2, id).observe(viewLifecycleOwner) { info ->
                    onApplyResult(info)
                }
            }
            is DownloadAction.Download -> viewModel.download(id)
                .observe(viewLifecycleOwner) { info ->
                    onDownloadResult(info)
                }
        }
    }

    private fun onDownloadResult(info: WorkInfo?) {
        if (info != null) {
            val path = info.outputData.getString(DOWNLOAD_PATH_KEY) ?: ""
            when (info.state) {
                WorkInfo.State.FAILED -> onDownloadError()
                WorkInfo.State.ENQUEUED -> onDownloadQueued()
                WorkInfo.State.SUCCEEDED -> {
                    val existed = info.outputData.getBoolean(DOWNLOAD_FILE_EXISTED, false)
                    if (existed) onDownloadExistent(path, "Изображение уже загружено")
                    else onDownloadExistent(path, "Изображение загружено")
                }
                else -> {

                }
            }
        }
    }

    private fun onApplyResult(info: WorkInfo?) {
        if (info != null) {
            if (info.state.isFinished) {
                if (info.state == WorkInfo.State.SUCCEEDED) {
                    val option = info.outputData.getInt(APPLY_OPTION_KEY, -1)
                    if (option == APPLY_EXTERNAL_KEY) {
                        onWallpaperReadyToBeApplied(
                            info.outputData.getString(DOWNLOAD_PATH_KEY) ?: ""
                        )
                    } else onWallpaperApplied()
                } else if (info.state == WorkInfo.State.FAILED) {
                    onDownloadError()
                }
            } else if (info.state == WorkInfo.State.ENQUEUED) {
                onWallpaperApplicationEnqueued(0)
            }
        }
    }

    private fun cancelWorkManagerTasks() {
        dialog.hide()
        viewModel.cancelWorkManagerTasks()
    }

    private fun onWallpaperApplicationEnqueued(applyOption: Int) {
        dialog.setState(WallpaperApplication.Enqueued {
            cancelWorkManagerTasks()
        })
    }

    private fun onWallpaperApplied() {
        dialog.setState(WallpaperApplication.Applied())
        viewModel.cancelWorkManagerTasks()
    }

    open fun onWallpaperReadyToBeApplied(path: String) {

    }

    private fun onDownloadError() {
        dialog.setState(WallpaperApplication.Error())
        viewModel.cancelWorkManagerTasks()
    }

    private fun onDownloadQueued() {
        dialog.setState(Downloading.Enqueued {
            cancelWorkManagerTasks()
        })
    }

    private fun onDownloadExistent(path: String, message: String) {
        try {
            val file = File(path)
            val fileUri: Uri? = file.getUri(requireContext()) ?: Uri.fromFile(file)
            dialog.setState(Downloading.Existent(message) {
                openFile(fileUri, file)
            })
        } catch (e: Exception) {
            Log.d("ERROR", e.message ?: "")
        }
        viewModel.cancelWorkManagerTasks()
    }

    private fun openFile(fileUri: Uri?, file: File) {
        fileUri?.let {
            try {
                startActivity(Intent().apply {
                    action = Intent.ACTION_VIEW
                    setDataAndType(fileUri, file.getMimeType("image/*"))
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                })
            } catch (e: Exception) {

            }
        }
        cancelWorkManagerTasks()
    }

}
