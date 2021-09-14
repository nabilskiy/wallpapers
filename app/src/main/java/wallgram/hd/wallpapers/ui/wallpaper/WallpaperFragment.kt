package wallgram.hd.wallpapers.ui.wallpaper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Environment
import kotlinx.coroutines.*
import android.os.Handler
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.FragmentWallpaperBinding
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.wallpapers.*
import wallgram.hd.wallpapers.util.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.details.DownloadDialogFragment
import wallgram.hd.wallpapers.ui.details.InstallDialogFragment
import wallgram.hd.wallpapers.util.downloadx.State
import wallgram.hd.wallpapers.util.downloadx.download
import wallgram.hd.wallpapers.views.progressbutton.*
import java.io.File


class WallpaperFragment : BaseFragment<WallpaperViewModel, FragmentWallpaperBinding>(
    FragmentWallpaperBinding::inflate
) {

    companion object {
        private const val ARG_POSITION = "arg_position"
        private const val ARG_PIC = "arg_pic"
        fun create(position: Int, pic: Int) = WallpaperFragment().withArgs(
            ARG_POSITION to position,
            ARG_PIC to pic
        )
    }

    private val wallpapersAdapter: WallpaperItemAdapter by lazy {
        WallpaperItemAdapter {
         //   Toast.makeText(requireContext(), "CLICK", Toast.LENGTH_SHORT).show()
//            modo.externalForward(Screens.Wallpaper())
        }
    }

    private val cornerRadius = 12.dp.toFloat()

    private val position: Int by args(ARG_POSITION, 0)
    private val pic: Int by args(ARG_PIC, 0)

    private val similarAdapter: WallpapersAdapter by lazy {
        WallpapersAdapter { position, id ->
            viewModel.itemClicked(position, id)
        }
    }

    private var isTagsVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.color_status_bar)

        viewModel.wallpapersLiveData.observe(viewLifecycleOwner, {
            wallpapersAdapter.submitData(lifecycle, it)
            if (binding.list.currentItem == 0)
                binding.list.setCurrentItem(position, false)
        })

        with(binding) {

            val bottomSheetBehavior: BottomSheetBehavior<*> =
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

            viewModel.getLiveData(FeedRequest(type = WallType.SIMILAR, category = pic))

            viewModel.similarLiveData.observe(viewLifecycleOwner) {
                similarAdapter.submitData(lifecycle = lifecycle, it)
            }

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
                            if(isTagsVisible){
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
                addItemDecoration(ConcatItemDecoration { concatAdapter.adapters })
                adapter = concatAdapter
            }


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

            list.setOnClickListener {
                Log.d("CLICK", "OK")
            }

            viewModel.favoriteLiveData.observe(viewLifecycleOwner, {
                toolbar.menu.getItem(1)
                    .setIcon(if (it) R.drawable.ic_fav_fill else R.drawable.ic_fav_outlined)
            })

            installBtn.setOnClickListener {
                InstallDialogFragment().show(
                    childFragmentManager,
                    InstallDialogFragment::class.java.simpleName
                )
            }

            downloadBtn.attachTextChangeAnimator()
            bindProgressButton(downloadBtn)

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
        if(binding.chipGroup.alpha == 1f)
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
        if(binding.chipGroup.alpha == 0f)
            return

        lifecycleScope.launch {
            val anim1 = async { binding.chipGroup.alpha(1f, 0f) }
            val anim2 =
                async { binding.chipGroup.translationY(binding.toolbar.height.toFloat(), 0f) }
            awaitAll(anim1, anim2)
        }
        isTagsVisible = false
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
            if (landscape != null)
                viewModel.onCropClicked(landscape)
        }
    }

    private fun setFavorite() {
        val item = wallpapersAdapter.peek(binding.list.currentItem)
        item?.let {
            viewModel.setFavorite(it)
        }
    }

    override fun getViewModel(): Class<WallpaperViewModel> = WallpaperViewModel::class.java

    fun download(url: String) {
        val downloadTask = lifecycleScope.download(
            url
        )
        downloadTask.state()
            .onEach {
                when (it) {
                    is State.Downloading -> {
                        if (!binding.downloadBtn.isProgressActive())
                            showLoading()
                    }
                    is State.Succeed -> showDone(it)
                    is State.Failed -> showDone(it)
                    is State.Stopped -> showDone(it)
                    is State.None -> showDone(it)
                }
            }
            .launchIn(lifecycleScope)

        downloadTask.start()
    }

    private fun showLoading() {
        val button = binding.downloadBtn
        button.showProgress {
            buttonTextRes = R.string.loading_key
            progressColor = Color.WHITE
            gravity = DrawableButton.GRAVITY_TEXT_END
        }
        button.isEnabled = false
    }

    private fun showDone(state: State) {
        val animatedDrawable =
            ContextCompat.getDrawable(requireContext(), getDrawableFromState(state))!!
        val drawableSize = resources.getDimensionPixelSize(R.dimen.doneSize)
        animatedDrawable.setBounds(0, 0, drawableSize, drawableSize)
        val button = binding.downloadBtn
        Handler().postDelayed({
            button.isEnabled = true

            button.showDrawable(animatedDrawable) {
                buttonTextRes = getMessageFromState(state)
            }
            Handler().postDelayed({
                button.hideDrawable(R.string.download)
            }, 2000)
        }, 500)
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
}
