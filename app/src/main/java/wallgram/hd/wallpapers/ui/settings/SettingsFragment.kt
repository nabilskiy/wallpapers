package wallgram.hd.wallpapers.ui.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.local.preference.PreferenceContract
import wallgram.hd.wallpapers.databinding.FragmentSettingsBinding
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.main.MainViewModel
import wallgram.hd.wallpapers.util.CacheUtils
import com.bumptech.glide.Glide
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wallgram.hd.wallpapers.ui.base.Screens
import wallgram.hd.wallpapers.ui.favorite.container.FavoriteContainerFragment
import wallgram.hd.wallpapers.ui.main.MainFragment
import wallgram.hd.wallpapers.util.Common
import javax.inject.Inject

class SettingsFragment : BaseFragment<MainViewModel, FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    @Inject
    lateinit var preferences: PreferenceContract

    private val cacheUtils: CacheUtils by lazy {
        CacheUtils()
    }

    private val manager: ReviewManager by lazy {
        ReviewManagerFactory.create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            clearBtn.setOnClickListener { clearCacheImages() }

            historyItem.setOnClickListener {
                viewModel.onItemClicked(Screens.History())
                val fragment = requireParentFragment().requireParentFragment()
                (fragment as MainFragment).getCurrentFragment()?.let {
                    if(it is FavoriteContainerFragment)
                            it.selectScreen(1)
                }

            }
            subscribeItem.setOnClickListener { viewModel.onItemClicked(Screens.Subscription()) }
            reviewItem.setOnClickListener { onReviewClicked() }
            siteItem.setOnClickListener { viewModel.onItemClicked(Screens.Browser(Common.getSiteUrl())) }
            langItem.setOnClickListener { viewModel.onItemClicked(Screens.Language()) }

            lifecycleScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    cacheValue.text = resources.getString(
                        R.string.cache_size,
                        cacheUtils.getFileSize(cacheUtils.getFolderSize(requireContext().cacheDir))
                    )
                }
            }
        }
    }

    private fun onReviewClicked() {
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = request.result
                val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)

                flow.addOnCompleteListener {

                    // Обрабатываем завершение сценария оценки

                }
            } else {
            }
        }
    }

    private fun clearCacheImages() {
        lifecycleScope.launch(Dispatchers.IO) {
            Glide.get(requireContext()).clearDiskCache()
            withContext(Dispatchers.Main) {
                binding.cacheValue.text = resources.getString(
                    R.string.cache_size,
                    cacheUtils.getFileSize(cacheUtils.getFolderSize(requireContext().cacheDir))
                )
            }
        }
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java
}