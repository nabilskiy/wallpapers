package wallgram.hd.wallpapers.presentation.settings.resolution

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.FragmentLanguageBinding
import wallgram.hd.wallpapers.databinding.FragmentResolutionBinding
import wallgram.hd.wallpapers.presentation.base.BaseActivity
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.util.setCustomChecked

@AndroidEntryPoint
class ResolutionFragment : BaseFragment<ResolutionViewModel, FragmentResolutionBinding>(
    FragmentResolutionBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resolutionsAdapter = ResolutionsAdapter()


        with(binding) {
            toolbar.setNavigationOnClickListener { viewModel.back() }

            resolutionsView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = null
                adapter = resolutionsAdapter
            }
        }

        viewModel.observe(viewLifecycleOwner){ resolutionsUi ->
            resolutionsUi.map(resolutionsAdapter)
        }

        viewModel.observeUpdate(viewLifecycleOwner){
            viewModel.update()
        }

    }

    override fun invalidate() {
        super.invalidate()
        viewModel.back()
    }

    override val viewModel: ResolutionViewModel by viewModels()
}