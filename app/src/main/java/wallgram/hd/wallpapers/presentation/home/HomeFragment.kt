package wallgram.hd.wallpapers.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.WallpaperRequest
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.databinding.FragmentHomeBinding
import wallgram.hd.wallpapers.presentation.base.NoLimitRecycledViewPool
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.gallery.GalleryUi
import wallgram.hd.wallpapers.presentation.home.scroll.HomeCarouselUi
import wallgram.hd.wallpapers.util.dp

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    override val viewModel: HomeViewModel by viewModels()

    private val mPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            if (it.itemId == R.id.action_search) {
                viewModel.search()
            }
            false
        }

        val recycledViewPool: RecyclerView.RecycledViewPool = NoLimitRecycledViewPool()

        val homeAdapter = HomeAdapter(object : GenericAdapter.ClickListener<Pair<Int, Int>> {
            override fun click(item: Pair<Int, Int>) {
                viewModel.itemClicked(WallpaperRequest.DATE(), item.first, item.second)
            }

        }, recycledViewPool)

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
        }
    }

    override fun invalidate() {
        super.invalidate()
        binding.homeList.smoothScrollToPosition(0)
    }

}