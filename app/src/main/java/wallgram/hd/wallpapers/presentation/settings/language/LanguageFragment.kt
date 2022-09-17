package wallgram.hd.wallpapers.presentation.settings.language

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.FragmentLanguageBinding
import wallgram.hd.wallpapers.presentation.base.BaseActivity
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.util.setCustomChecked

@AndroidEntryPoint
class LanguageFragment : BaseFragment<LanguagesViewModel, FragmentLanguageBinding>(
    FragmentLanguageBinding::inflate
) {

    override fun invalidate() {
        super.invalidate()
        viewModel.back()
    }

    private val listener = RadioGroup.OnCheckedChangeListener { _, i -> selectLanguage(i) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null)
            checkCurrentLanguage()

        with(binding) {
            toolbar.setNavigationOnClickListener { viewModel.back() }
            langGroup.setOnCheckedChangeListener(listener)
        }

    }

    private fun checkCurrentLanguage() {
        val id = when ((requireActivity() as BaseActivity<*>).getCurrentLanguage().language) {
            "en" -> R.id.english_item
            "es" -> R.id.espanol_item
            "zh" -> R.id.china_item
            "de" -> R.id.deutsch_item
            "fr" -> R.id.fr_item
            "uk" -> R.id.ukraine_item
            "pt" -> R.id.pt_item
            "ru" -> R.id.russia_item
            else -> R.id.english_item
        }
        binding.langGroup.setCustomChecked(id, listener)
    }

    private fun selectLanguage(i: Int) {
        val lang = when (i) {
            R.id.russia_item -> "ru"
            R.id.english_item -> "en"
            R.id.deutsch_item -> "de"
            R.id.espanol_item -> "es"
            R.id.fr_item -> "fr"
            R.id.pt_item -> "pt"
            R.id.ukraine_item -> "uk"
            R.id.china_item -> "zh"
            else -> "en"
        }
        (requireActivity() as BaseActivity<*>).setLanguage(lang)
    }

    override val viewModel: LanguagesViewModel by viewModels()
}