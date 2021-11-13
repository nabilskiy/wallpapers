package wallgram.hd.wallpapers.ui.settings.language

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.core.view.forEach
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.local.preference.PreferenceContract
import wallgram.hd.wallpapers.databinding.FragmentLanguageBinding
import wallgram.hd.wallpapers.ui.base.BaseActivity
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.settings.SettingsViewModel
import wallgram.hd.wallpapers.util.dp
import wallgram.hd.wallpapers.util.setCustomChecked
import javax.inject.Inject

class LanguageFragment : BaseFragment<SettingsViewModel, FragmentLanguageBinding>(
    FragmentLanguageBinding::inflate
) {
    companion object {
        fun create() = LanguageFragment()
    }

    @Inject
    lateinit var preferences: PreferenceContract

    override fun invalidate() {
        super.invalidate()
        viewModel.onBackClicked()
    }

    private val listener = RadioGroup.OnCheckedChangeListener { _, i -> selectLanguage(i) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null)
            checkCurrentLanguage()

        with(binding) {
            toolbar.setNavigationOnClickListener { viewModel.onBackClicked() }
            langGroup.setOnCheckedChangeListener(listener)
        }

    }

    private fun checkCurrentLanguage() {
        val id = when ((requireActivity() as BaseActivity<*, *>).getCurrentLanguage().language) {
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
        binding.langGroup.forEach {
            if (it.id == binding.langGroup.checkedRadioButtonId)
                it.setPadding(27.dp, 0, 0, 0)
            else it.setPadding(44.dp, 0, 0, 0)
        }
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
        (requireActivity() as BaseActivity<*, *>).setLanguage(lang)

        binding.langGroup.forEach {
            if (it.id == binding.langGroup.checkedRadioButtonId)
                it.setPadding(27.dp, 0, 0, 0)
            else it.setPadding(44.dp, 0, 0, 0)
        }
    }

    override fun getViewModel(): Class<SettingsViewModel> = SettingsViewModel::class.java
}