package wallgram.hd.wallpapers.ui.wallpaper

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import kotlinx.coroutines.*
import android.text.TextUtils
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.github.razir.progressbutton.*
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.FragmentWallpaperBinding
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.wallpapers.*
import wallgram.hd.wallpapers.util.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.model.Config
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.details.DownloadDialogFragment
import wallgram.hd.wallpapers.ui.details.InstallDialogFragment
import wallgram.hd.wallpapers.util.downloadx.State
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.math.atan2


class WallpaperFragment : BaseFragment<WallpaperViewModel, FragmentWallpaperBinding>(
    FragmentWallpaperBinding::inflate
) {

    companion object {
        private const val ARG_POSITION = "arg_position"
        private const val ARG_PIC = "arg_pic"
        private const val ARG_TYPE = "arg_type"
        fun create(type: WallType, position: Int, pic: Int) = WallpaperFragment().withArgs(
            ARG_POSITION to position,
            ARG_PIC to pic,
            ARG_TYPE to type
        )
    }

    private val downloadLiveDataPrivate = MutableLiveData<Resource<Uri>>()
    private val installLiveDataPrivate = MutableLiveData<Resource<Uri>>()

    private val wallpapersAdapter: WallpaperItemAdapter by lazy {
        WallpaperItemAdapter {
            gestureDetector.onTouchEvent(it)
        }
    }

    private lateinit var gestureDetector: GestureDetector

    private val cornerRadius = 12.dp.toFloat()

    private val position: Int by args(ARG_POSITION, 0)
    private val pic: Int by args(ARG_PIC, 0)
    private val type: WallType by args(ARG_TYPE, WallType.ALL)

    private val similarAdapter: WallpapersAdapter by lazy {
        WallpapersAdapter { position, id ->
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                viewModel.itemClicked(position, id)
            else bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gestureDetector =
            GestureDetector(requireContext(), SwipeGestureDetector())
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private var isTagsVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.color_status_bar)

        if (type == WallType.ALL)
            viewModel.wallpapersLiveData.observe(viewLifecycleOwner, {
                wallpapersAdapter.submitData(lifecycle, it)
                if (binding.list.currentItem == 0)
                    binding.list.setCurrentItem(position, false)
            }) else if (type == WallType.SIMILAR) {
            viewModel.similarData.observe(viewLifecycleOwner, {
                wallpapersAdapter.submitData(lifecycle, it)
                if (binding.list.currentItem == 0)
                    binding.list.setCurrentItem(position, false)
            })
        }

        with(binding) {

            bottomSheetBehavior =
                BottomSheetBehavior.from<View>(bottomSheet)
            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }

                override fun onSlide(view: View, slideOffset: Float) {
                    titleText.animate().alpha(slideOffset).setDuration(0).start()
                    toolbar.getChildAt(1).animate().alpha(1 - slideOffset).setDuration(0).start()
                    (bottomSheet.background as GradientDrawable).cornerRadius =
                        (cornerRadius - (cornerRadius * slideOffset))
                    //   bottomSheet.setCornerRadius(cornerRadius - (cornerRadius * slideOffset))
                    lineView.animate().alpha(1 - slideOffset).setDuration(0).start()
                }

            })
            val downloadDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_download_icon)!!
            val installDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_install)!!
            val drawableSize = resources.getDimensionPixelSize(R.dimen.doneSize)
            downloadDrawable.setBounds(0,0,drawableSize, drawableSize)
            installDrawable.setBounds(0,0,drawableSize, drawableSize)

            downloadBtn.showDrawable(downloadDrawable){
                buttonTextRes = R.string.download
                gravity = DrawableButton.GRAVITY_TEXT_START
            }

            installBtn.showDrawable(installDrawable){
                buttonTextRes = R.string.install_wallpaper
                gravity = DrawableButton.GRAVITY_TEXT_START
            }

            downloadLiveDataPrivate.observe(viewLifecycleOwner, {
                when (it) {
                    is Resource.Loading ->{

                        downloadBtn.showProgress {
                            progressColor = Color.WHITE
                            gravity = DrawableButton.GRAVITY_CENTER

                        }

                        downloadBtn.isEnabled = false
                    }


                    is Resource.Success ->{
                        downloadBtn.isEnabled = true
                        downloadBtn.hideProgress(R.string.download)
                        downloadBtn.showDrawable(downloadDrawable){
                            buttonTextRes = R.string.download
                            gravity = DrawableButton.GRAVITY_TEXT_START
                        }
                    }

                }
            })

            installLiveDataPrivate.observe(viewLifecycleOwner, {
                when (it) {
                    is Resource.Loading ->{

                        installBtn.showProgress {
                            progressColor = Color.WHITE
                            gravity = DrawableButton.GRAVITY_CENTER

                        }

                        installBtn.isEnabled = false
                    }


                    is Resource.Success ->{
                        installBtn.isEnabled = true
                        installBtn.hideProgress(R.string.install_wallpaper)
                        installBtn.showDrawable(installDrawable){
                            buttonTextRes = R.string.install_wallpaper
                            gravity = DrawableButton.GRAVITY_TEXT_START
                        }
                    }
                }
            })

            bindProgressButton(downloadBtn)
            bindProgressButton(installBtn)


            viewModel.getLiveData(FeedRequest(type = WallType.SIMILAR, category = pic))

            viewModel.similarLiveData.observe(viewLifecycleOwner) {

                similarAdapter.submitData(lifecycle = lifecycle, it)
            }

            bottomSheetBehavior.peekHeight =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) 62.dp else 42.dp + getNavigationBarHeight()


            toolbar.menu.getItem(2).actionView.findViewById<ImageView>(R.id.tag_icon)
                ?.setOnClickListener {
                    if (isTagsVisible) hideTags() else showTags()
                }

            viewModel.picLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> {
                        updateToolbar()
                    }
                    is Resource.Success -> {
                        toolbar.menu.getItem(2).actionView?.findViewById<ProgressBar>(R.id.tag_progress_bar)?.isVisible =
                            false
                        toolbar.menu.getItem(2).actionView?.findViewById<ImageView>(R.id.tag_icon)?.isVisible =
                            true

                        it.data?.let {
                            if (isTagsVisible) {
                                isTagsVisible = false
                                return@observe
                            }

                            chipGroup.removeAllViews()

                            it.tags.forEach { tag ->

                                val textView = TextView(requireContext()).apply {
                                    text = tag.name
                                    ellipsize = TextUtils.TruncateAt.END
                                    maxLines = 1
                                    maxLength(15)
                                    setBackgroundResource(R.drawable.rounded_corner)
                                    setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.colorWhite
                                        )
                                    )
                                    includeFontPadding = false
                                    textSize = 14F
                                    typeface = ResourcesCompat.getFont(
                                        requireContext(),
                                        R.font.roboto_medium
                                    )
                                    setOnClickListener {
                                        viewModel.onTagClicked(tag)
                                    }
                                }
                                chipGroup.addView(textView)
                            }

                            showTagsView()
                        }


                    }
                    else -> {
                        hideTags()
                    }

                }
            }

            bottomSheet.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            buttonsContainer.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(bottom = rect.bottom + windowInsetsCompat.systemWindowInsetBottom)
                windowInsetsCompat
            }

            toolbar.doOnApplyWindowInsets { view, windowInsetsCompat, rect ->
                view.updatePadding(top = rect.top + windowInsetsCompat.systemWindowInsetTop)
                windowInsetsCompat
            }

            toolbar.setNavigationIcon(R.drawable.ic_back)
            toolbar.setNavigationOnClickListener {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                else viewModel.onBack()
            }
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_favorite -> setFavorite()
                    R.id.action_crop -> toCrop()
                    R.id.action_info -> if (chipGroup.isVisible) hideTags() else showTags()
                }
                true
            }

            list.apply {
                adapter = wallpapersAdapter

            }

            val concatAdapter = similarAdapter.withLoadStateHeaderAndFooter(
                header = ReposLoadStateAdapter { wallpapersAdapter.retry() },
                footer = ReposLoadStateAdapter { wallpapersAdapter.retry() })

            bottomSheetFragment.apply {
                layoutManager = GridLayoutManager(requireContext(), 3).apply {
                    spanSizeLookup = ConcatSpanSizeLookup(3) { concatAdapter.adapters }
                }
                itemAnimator = null
                addItemDecoration(ConcatItemDecoration { concatAdapter.adapters })
                adapter = concatAdapter
            }

            list.reduceDragSensitivity()

            list.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                    // checkFavorite(position)
                    val item = wallpapersAdapter.peek(position)
                    item?.let {
                        viewModel.clearInformation()
                        viewModel.isFavorite(it)
                        viewModel.getLiveData(
                            FeedRequest(
                                type = WallType.SIMILAR,
                                category = it.id
                            )
                        )
                        updateCrop(it.links.landscape)
                    }
                }
            })


            viewModel.favoriteLiveData.observe(viewLifecycleOwner, {
                toolbar.menu.getItem(1)
                    .setIcon(if (it) R.drawable.ic_fav_fill else R.drawable.ic_fav_outlined)
            })

            installBtn.setOnClickListener {
                val item = wallpapersAdapter.peek(binding.list.currentItem)
                item?.let {
                    InstallDialogFragment.create(it.links.portrait ?: "").show(
                        childFragmentManager,
                        InstallDialogFragment::class.java.simpleName
                    )
                }
            }

            downloadBtn.setOnClickListener {
                val item = wallpapersAdapter.peek(binding.list.currentItem)
                item?.let {
                    DownloadDialogFragment.create(it.links).show(
                        childFragmentManager,
                        DownloadDialogFragment::class.java.simpleName
                    )
                }

            }

            wallpapersAdapter.addLoadStateListener { loadState ->
                // Only show the list if refresh succeeds.

                //  list.isVisible = loadState.source.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                //progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                //  errorLayout.root.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
//                    Toast.makeText(
//                            this,
//                            "\uD83D\uDE28 Wooops ${it.error}",
//                            Toast.LENGTH_LONG
//                    ).show()
                }
            }
            /* errorLayout.btnRetry.setOnClickListener {
                 wallpapersAdapter.retry()
             }*/

        }
    }

    private fun updateToolbar() {
        binding.toolbar.menu.getItem(2).actionView?.findViewById<ImageView>(R.id.tag_icon)?.isVisible =
            false

        binding.toolbar.menu.getItem(2).actionView?.findViewById<ProgressBar>(R.id.tag_progress_bar)?.isVisible =
            true

    }

    private fun updateCrop(landscape: String?) {
        binding.toolbar.menu.getItem(0).isVisible = landscape != null
    }

    private fun getNavigationBarHeight(): Int {
        val resources: Resources = resources
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }


    private fun showTags() {
        if (viewModel.picLiveData.value is Resource.Success) {
            showTagsView()
            return
        }
        val item = wallpapersAdapter.peek(binding.list.currentItem)
        item?.let {
            viewModel.getPic(it.id, requireContext().getResolution())
        }
    }

    private fun showTagsView() {
        if (binding.chipGroup.alpha == 1f)
            return

        lifecycleScope.launch {
            val anim1 = async { binding.chipGroup.alpha(0f, 1f) }
            val anim2 =
                async { binding.chipGroup.translationY(0f, binding.toolbar.height.toFloat()) }
            awaitAll(anim1, anim2)
        }
        isTagsVisible = true
    }

    private fun hideTags() {
        if (binding.chipGroup.alpha == 0f)
            return

        lifecycleScope.launch {
            val anim1 = async { binding.chipGroup.alpha(1f, 0f) }
            val anim2 =
                async { binding.chipGroup.translationY(binding.toolbar.height.toFloat(), 0f) }
            awaitAll(anim1, anim2)
        }
        isTagsVisible = false
    }

    private fun hideAllViews() {
        hideTags()
        lifecycleScope.launch {
            val anim1 = async { binding.downloadBtn.alpha(1f, 0f) }
            val anim2 = async { binding.installBtn.alpha(1f, 0f) }
            val anim3 =
                async { binding.toolbar.translationY(0f, -binding.toolbar.height.toFloat()) }

            awaitAll(anim1, anim2, anim3)
        }

        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showAllViews() {
        lifecycleScope.launch {
            val anim1 = async { binding.downloadBtn.alpha(0f, 1f) }
            val anim2 = async { binding.installBtn.alpha(0f, 1f) }
            val anim3 =
                async { binding.toolbar.translationY(-binding.toolbar.height.toFloat(), 0f) }

            awaitAll(anim1, anim2, anim3)
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.isHideable = false

        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            binding.root
        ).show(WindowInsetsCompat.Type.systemBars())
    }


    private fun checkFavorite(position: Int) {
        val item = wallpapersAdapter.peek(position)
        item?.let {
            viewModel.isFavorite(it)
        }
    }

    private fun toCrop() {
        val item = wallpapersAdapter.peek(binding.list.currentItem)
        item?.let {
            val landscape = it.links.landscape
            if (landscape != null) {
                isTagsVisible = true
                viewModel.onCropClicked(landscape)
            }
        }
    }

    private fun setFavorite() {
        val item = wallpapersAdapter.peek(binding.list.currentItem)
        item?.let {
            viewModel.setFavorite(it)
        }
    }

    override fun getViewModel(): Class<WallpaperViewModel> = WallpaperViewModel::class.java

    fun downloadSupend(url: String) {

        val temp: File = Glide.with(requireContext())
            .asFile()
            .load(url)
            .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .get(2, TimeUnit.MINUTES)

        val result = WallpaperUtils.saveToFile(context, url, temp)
        downloadLiveDataPrivate.postValue(Resource.Success(result))
    }

    fun downloadAndSetWallpaper(url: String, config: Config) {
        installLiveDataPrivate.postValue(Resource.Loading())
        lifecycleScope.launch(Dispatchers.IO) {
            val wallpaper = WallpaperUtils.getImageFile(requireContext(), url)
            if (wallpaper == null || !wallpaper.exists()) {
                throw IOException("Download wallpaper failure")
            }
            val uiHelper = UIHelper()
            val result = uiHelper.setWallpaper(requireContext(), config, wallpaper)
            installLiveDataPrivate.postValue(Resource.Success(result))
        }
    }


    fun download(url: String) {
        downloadLiveDataPrivate.postValue(Resource.Loading())

        lifecycleScope.launch(Dispatchers.IO) {

            downloadSupend(url)
        }

    }


    private fun getMessageFromState(state: State) = when (state) {
        is State.Succeed, is State.Failed -> R.string.ok_btn
        is State.Stopped -> R.string.error
        is State.None -> R.string.error
        else -> -1
    }

    private fun getDrawableFromState(state: State) = when (state) {
        is State.Succeed, is State.Failed -> R.drawable.animated_check
        is State.Stopped -> R.drawable.ic_round_error
        is State.None -> R.drawable.ic_round_error
        else -> -1
    }

    inner class SwipeGestureDetector : SimpleOnGestureListener() {


        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (binding.downloadBtn.alpha == 1f)
                hideAllViews()
            else showAllViews()
            return false
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val x1 = e1.x
            val y1 = e1.y
            val x2 = e2.x
            val y2 = e2.y
            val angle = Math.toDegrees(atan2((y1 - y2).toDouble(), (x2 - x1).toDouble()))
            if (angle > 20 && angle <= 160) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            return false
        }
    }
}
