package wallgram.hd.wallpapers.presentation.wallpaper

import android.app.DownloadManager
import android.content.Intent
import android.content.res.Resources
import android.graphics.BlurMaskFilter.Blur
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.work.WorkInfo
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.App.Companion.modo
import wallgram.hd.wallpapers.DEFAULT_SKU
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.Mapper
import wallgram.hd.wallpapers.core.data.PreferenceDataStore
import wallgram.hd.wallpapers.core.data.permissions.Permission
import wallgram.hd.wallpapers.core.data.permissions.PermissionProvider
import wallgram.hd.wallpapers.data.ads.banner.BannerAd
import wallgram.hd.wallpapers.data.ads.interstitial.InterstitialAd
import wallgram.hd.wallpapers.data.billing.BillingRepository
import wallgram.hd.wallpapers.data.workers.DownloadProgressLiveData
import wallgram.hd.wallpapers.data.workers.WallpaperApplier.Companion.APPLY_EXTERNAL_KEY
import wallgram.hd.wallpapers.data.workers.WallpaperApplier.Companion.APPLY_OPTION_KEY
import wallgram.hd.wallpapers.data.workers.WallpaperDownloader
import wallgram.hd.wallpapers.data.workers.WallpaperDownloader.Companion.DOWNLOAD_PATH_KEY
import wallgram.hd.wallpapers.databinding.FragmentWallpaperBinding
import wallgram.hd.wallpapers.presentation.base.BaseSlidingUpFragment
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.presentation.base.adapter.ItemUi
import wallgram.hd.wallpapers.presentation.base.views.slidinguppanel.Features
import wallgram.hd.wallpapers.presentation.dialogs.DownloadAction
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.main.AppLaunchStore
import wallgram.hd.wallpapers.util.*
import wallgram.hd.wallpapers.views.ChefSnackbar
import wallgram.hd.wallpapers.views.blur.GlideProvider
import wallgram.hd.wallpapers.views.blur.mode.BlurAnimMode
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class WallpaperFragment : BaseSlidingUpFragment<WallpaperViewModel, FragmentWallpaperBinding>(
    FragmentWallpaperBinding::inflate
) {

    private val manager: ReviewManager by lazy {
        ReviewManagerFactory.create(requireActivity().applicationContext)
    }

    override val viewModel: WallpaperViewModel by viewModels()

    private lateinit var snackbar: ChefSnackbar
    private lateinit var permissionProvider: PermissionProvider

    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore
    private lateinit var downloadsStore: DownloadsCountStore.Mutable

    @Inject
    lateinit var adInterstitial: InterstitialAd

    @Inject
    lateinit var adBanner: BannerAd

    companion object {
        fun newInstance() = WallpaperFragment()
    }

    private fun showInterstitialAd() {
        if (needShowInterstitialAd()) adInterstitial.show()
    }

    private fun needShowInterstitialAd() = !modo.state.chain.any { it.id.contains("Wallpaper") }

    override fun onDestroyView() {
        snackbar.dismiss()

        showInterstitialAd()

        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.cancelWorkManagerTasks()
    }

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

        permissionProvider = PermissionProvider.Base(this)
        downloadsStore = DownloadsCountStore.Base(preferenceDataStore)

        lifecycle.addObserver(adBanner)
        adInterstitial.load()
    }

    private fun addDownload(){
        var downloadCount = downloadsStore.read()
        downloadCount += 1
        downloadsStore.save(downloadCount)
        showReviewDialog()
    }


    private fun showReviewDialog() {
        var downloadCount = downloadsStore.read()
        if (downloadCount > 2) {
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
                    flow.addOnCompleteListener {
                        // Обрабатываем завершение сценария оценки
                    }
                } else {

                }
            }
        }
    }
    private fun currentItem(): ItemUi {
        return galleryAdapter.getItems()[binding.list.currentItem]
    }

    private val galleryAdapter = GalleryFullAdapter()

    override fun back() {
        if (panelShowed()) {
            super.back()
            return
        } else viewModel.back()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            snackbar = ChefSnackbar.make(root)

            val banner = adBanner.adSize()
            adViewContainer.layoutParams.apply {
                width = banner.getWidthInPixels(requireContext())
                height = banner.getHeightInPixels(requireContext()) + 52.dp
            }
            adBanner.show(adViewContainer)

//            val glideProvider = GlideProvider(context, arrayOf(), 1080, 1920)
//            ecardflowLayout.setViewPager(list)
//            ecardflowLayout.setAnimMode(BlurAnimMode())

            viewModel.wallpapersLiveData.observe(viewLifecycleOwner) {
                it.map(galleryAdapter)

//                it.map(object : Mapper.Unit<List<ItemUi>> {
//                    override fun map(data: List<ItemUi>) {
//                        val list = data.map { item -> item.uri().first }
//                        ecardflowLayout.setImages(list.toTypedArray())
//                        ecardflowLayout.setImageProvider(glideProvider, binding.list.currentItem)
//
//                    }
//                })
            }

            viewModel.downloadLiveData.observe(viewLifecycleOwner) {
                onDownloadResult(it)
            }


            viewModel.observeUpdate(viewLifecycleOwner) { isFavorite ->
                favoriteBtn.check(isFavorite)
                viewModel.fetchWallpapers()
            }

            viewModel.positionLiveData.observe(viewLifecycleOwner) {
                binding.list.setCurrentItem(it, false)
             //   binding.ecardflowLayout.switchBgToNext(it - 1)
            }

            toolbar.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(top = rect.top + windowInsetsCompat.systemWindowInsetTop)
                windowInsetsCompat
            }

            slidingUpPanelFragmentContainer.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(top = rect.top + windowInsetsCompat.systemWindowInsetTop)
                windowInsetsCompat
            }

//            list.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
//                view.updatePadding(
//                    top = rect.top + windowInsetsCompat.systemWindowInsetTop,
//                    bottom = rect.bottom + windowInsetsCompat.systemWindowInsetBottom
//                )
//                windowInsetsCompat
//            }

            adViewContainer.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(bottom = rect.bottom + windowInsetsCompat.systemWindowInsetBottom)
                windowInsetsCompat
            }

            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                viewModel.back()
            }

            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_info -> {
                        val id = currentItem().id().toIntOrNull()
                        if (id != null)
                            show(Features.Info(id))
                    }
                }
                true
            }

         //   list.clipToPadding = false
         //   list.clipChildren = false
            list.offscreenPageLimit = 3
          //  list.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

//            val compositePageTransformer = CompositePageTransformer().apply {
//                addTransformer(MarginPageTransformer(30.dp))
//                addTransformer { page, position ->
//                    val r: Float = 1 - Math.abs(position)
//                    page.scaleY = 0.9f + r * 0.1f
//                }
//            }

          //  list.setPageTransformer(compositePageTransformer)


            list.apply {
                adapter = galleryAdapter

                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                    override fun onPageSelected(position: Int) {
                        val item = currentItem()

                        updateView(item.id())

                        favoriteBtn.check(item.isFavorite())
                        viewModel.loadMoreData(
                            position
                        )

                    }

                })
            }

            similarBtn.setOnClickListener {
                val id = currentItem().id().toIntOrNull()
                if (id != null)
                    show(Features.Similar(WallpaperRequest.SIMILAR(id)))
            }

            favoriteBtn.setOnClickListener {
                val item = currentItem()
                viewModel.changeFavorite(item)
            }

            downloadBtn.setOnClickListener {
                val item = currentItem()
                if(item is GalleryUi){
                    show(Features.Download(item.originalResolution()))
                }

            }

            viewModel.init()

        }
    }

    private fun currentViewIsAd(id: String) = id == "-1"

    private fun updateView(id: String) = with(binding) {
        val showed = currentViewIsAd(id)

        adViewContainer.isInvisible = showed
        buttonsContainer.isInvisible = showed
        toolbar[1].isInvisible = showed
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
            window, binding.root
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    fun handle(t: DownloadAction) {
        hideIfOpenedFeature()

        permissionProvider.request(Permission.Storage).rationale("Необходим доступ к хранилищу")
            .checkPermission { granted ->
                if (granted) downloadOrApply(t)
            }
    }

    private fun downloadOrApply(t: DownloadAction) {
        val id = currentItem().id().toIntOrNull() ?: return
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
            is DownloadAction.DownloadSource -> viewModel.downloadSource(id)
//            is DownloadAction.Download -> downloadImageNew(
//                id.toString(),
//                currentItem().uri().second
//            )
//            is DownloadAction.Download -> viewModel.download(id)
//                .observe(viewLifecycleOwner) { info ->
//                    onDownloadResult(info)
//                }
        }
    }

    private fun onDownloadResult(info: WallpaperDownloader.Result) {
        val path = info.path()
        val id = info.id()

        when (info) {
            is WallpaperDownloader.Result.Failure -> onDownloadError(id)
            is WallpaperDownloader.Result.Enqueue -> {
                onDownloadQueued(id)
                val live = DownloadProgressLiveData(requireContext(), id)
                live.observe(viewLifecycleOwner) {
                    when (it.status) {
                        DownloadManager.STATUS_SUCCESSFUL -> onDownloadSuccess(info, path)
                        DownloadManager.STATUS_FAILED -> onDownloadError(id)
                        DownloadManager.STATUS_PAUSED -> onDownloadPaused()
                        DownloadManager.STATUS_PENDING -> onDownloadPending(id)
                        32 -> onDownloadCancelled()
                        else -> {
                            onDownloadQueued(id)
                        }
                    }
                }

            }
            is WallpaperDownloader.Result.Success -> {
                onDownloadSuccess(info, path)
            }
        }
    }

    private fun onDownloadSuccess(
        info: WallpaperDownloader.Result, path: String
    ) {
        val id = info.id()
        val existed = info.existed()
        if (existed) onDownloadExistent(id, path, R.string.image_already_downloaded)
        else onDownloadExistent(id, path, R.string.image_downloaded)
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
                    onApplyError()
                }
            } else if (info.state == WorkInfo.State.ENQUEUED) {
                onWallpaperApplicationEnqueued(0)
            }
        }
    }

    private fun cancelWorkManagerTasks() {
        snackbar.dismiss()
        viewModel.cancelWorkManagerTasks()
    }

    private fun onWallpaperApplicationEnqueued(applyOption: Int) =
        snackbar.state(WallpaperApplication.Enqueued {
            cancelWorkManagerTasks()
        })


    private fun onWallpaperApplied() {
        snackbar.state(WallpaperApplication.Applied())
        addDownload()
        viewModel.cancelWorkManagerTasks()
    }

    private fun onApplyError() {
        snackbar.state(WallpaperApplication.Error())
        viewModel.cancelWorkManagerTasks()
    }

    open fun onWallpaperReadyToBeApplied(path: String) {

    }

    private fun onDownloadError(id: Long) {
        snackbar.state(Downloading.Error())

    }

    private fun onDownloadQueued(id: Long) {
        snackbar.state(Downloading.Enqueued {
            viewModel.cancelDownload(id)
        })
    }

    private fun onDownloadPending(id: Long) {
        snackbar.state(Downloading.Pending {
            viewModel.cancelDownload(id)
        })
    }

    private fun onDownloadPaused() {
        snackbar.state(Downloading.Paused {
            cancelWorkManagerTasks()
        })
    }

    private fun onDownloadCancelled() {
        snackbar.state(Downloading.Cancelled())
    }

    private fun onDownloadExistent(id: Long, path: String, message: Int) {
        try {
            val file = File(path)
            val fileUri: Uri? = file.getUri(requireContext()) ?: Uri.fromFile(file)
            snackbar.state(Downloading.Existent(message) {
                openFile(fileUri, file)
            })
            addDownload()
        } catch (e: Exception) {
            Log.d("ERROR", e.message ?: "")
        }
        //viewModel.cancelDownload(id)
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
