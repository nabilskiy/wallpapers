package wallgram.hd.wallpapers.presentation.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.DisplayProvider
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.core.data.PreferenceDataStore
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.databinding.FragmentHomeBinding
import wallgram.hd.wallpapers.presentation.base.NoLimitRecycledViewPool
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.gallery.GalleryViewType
import wallgram.hd.wallpapers.presentation.wallpaper.DownloadsCountStore
import wallgram.hd.wallpapers.util.dp
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    override val viewModel: HomeViewModel by viewModels()

    private lateinit var downloadsStore: DownloadsCountStore.Mutable

    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore

    @Inject
    lateinit var displayProvider: DisplayProvider

    private val manager: ReviewManager by lazy {
        ReviewManagerFactory.create(requireActivity().applicationContext)
    }

    private val mPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        downloadsStore = DownloadsCountStore.Base(preferenceDataStore)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mPermissionResult.launch(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> viewModel.search()
                R.id.action_changer -> viewModel.changer()
                R.id.action_premium -> viewModel.navigateSubscriptions()
            }

            false
        }

        val recycledViewPool: RecyclerView.RecycledViewPool = NoLimitRecycledViewPool()

        val homeAdapter = HomeAdapter(recycledViewPool)

        with(binding) {
            swipeRefreshLayout.apply {
                setColorSchemeResources(wallgram.hd.wallpapers.R.color.colorYellow)
                setOnRefreshListener {
                    viewModel.loadData()
                }
            }
            homeList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(HomeItemDecoration(30.dp, 7))
                adapter = homeAdapter
            }
        }

        viewModel.categoriesLiveData.observe(
            viewLifecycleOwner
        ) { filtersUi ->
            filtersUi.map(homeAdapter)
        }

        viewModel.progressLiveData.observe(viewLifecycleOwner) { refreshing ->
            refreshing.apply(binding.swipeRefreshLayout)

            checkIfFragmentAttached {
                Handler(Looper.getMainLooper()).postDelayed({
                    showReviewDialog()
                }, 1500)
            }
        }

        viewModel.observeSubscriptions(viewLifecycleOwner) { subscribed ->
            binding.toolbar.menu.findItem(R.id.action_premium)?.let {
                it.isVisible = !subscribed
            }
        }
    }

    fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
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


    override fun invalidate() {
        super.invalidate()
        binding.homeList.smoothScrollToPosition(0)
    }

}