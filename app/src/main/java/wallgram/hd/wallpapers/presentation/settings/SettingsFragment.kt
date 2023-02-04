package wallgram.hd.wallpapers.presentation.settings

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import wallgram.hd.wallpapers.databinding.FragmentSettingsBinding
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.App.Companion.modo
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.util.Common
import wallgram.hd.wallpapers.util.modo.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    private val manager: ReviewManager by lazy {
        ReviewManagerFactory.create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            clearBtn.setOnClickListener { viewModel.clearCache() }

            historyItem.setOnClickListener {
                viewModel.showScreen(Screens.History())
            }
            subscribeItem.setOnClickListener { viewModel.showScreen(Screens.Subscription()) }
            reviewItem.setOnClickListener { onReviewClicked() }
            siteItem.setOnClickListener { viewModel.showScreen(Screens.Browser(Common.getSiteUrl())) }
            langItem.setOnClickListener { viewModel.showScreen(Screens.Language()) }
            resolutionItem.setOnClickListener { viewModel.showScreen(Screens.Resolution()) }

            viewModel.observe(viewLifecycleOwner) {
                it.show(binding.cacheValue)
            }

            viewModel.init()

        }
    }

    private fun onReviewClicked() {
        redirectToPlayStore()
//        val request = manager.requestReviewFlow()
//        request.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val reviewInfo = request.result
//                val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
//                flow.addOnCompleteListener {
//                    // Обрабатываем завершение сценария оценки
//                }
//            } else {
//            }
//        }
    }


    private fun redirectToPlayStore() {
        val appPackageName: String = requireContext().packageName
        try {
            modo.launch(Screens.Browser("market://details?id=$appPackageName"))
        } catch (exception: ActivityNotFoundException) {
            modo.launch(Screens.Browser("https://play.google.com/store/apps/details?id=$appPackageName"))
        }
    }


    override val viewModel: SettingsViewModel by viewModels()
}