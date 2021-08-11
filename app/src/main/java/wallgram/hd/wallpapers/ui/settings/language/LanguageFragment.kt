package wallgram.hd.wallpapers.ui.settings.language

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.forEach
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.local.preference.LANGUAGE
import wallgram.hd.wallpapers.data.local.preference.PreferenceContract
import wallgram.hd.wallpapers.databinding.FragmentLanguageBinding
import wallgram.hd.wallpapers.model.Gallery
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.components.crop.CropFragment
import wallgram.hd.wallpapers.ui.main.MainActivity
import wallgram.hd.wallpapers.ui.settings.SettingsViewModel
import wallgram.hd.wallpapers.util.dp
import wallgram.hd.wallpapers.util.withArgs
import java.util.*
import javax.inject.Inject

class LanguageFragment: BaseFragment<SettingsViewModel, FragmentLanguageBinding>(
    FragmentLanguageBinding::inflate
) {
    companion object {
        fun create() = LanguageFragment()
    }

    @Inject
    lateinit var preferences: PreferenceContract

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLanguage()

        with(binding){
            toolbar.setNavigationOnClickListener { viewModel.onBackClicked() }
            langGroup.setOnCheckedChangeListener { _, i ->
                selectLanguage(i)
            }
        }

    }

        private fun selectLanguage(i: Int) {
        var lang = preferences.getString(LANGUAGE, Locale.getDefault().language)
        when(i){
            R.id.russia_item -> lang = "ru"
            R.id.english_item -> lang = "en"
            R.id.deutsch_item -> lang = "de"
            R.id.espanol_item -> lang = "es"
            R.id.china_item -> lang = "zh"
        }
        binding.langGroup.forEach {
            if(it.id == binding.langGroup.checkedRadioButtonId)
                it.setPadding(24.dp, 0,0,0)
            else it.setPadding(42.dp, 0,0,0)
        }
        preferences.save(LANGUAGE, lang)

        wallgram.hd.wallpapers.App.localeHelper.setNewLocale(requireContext(), lang)
        requireActivity().finish()
        startActivity(Intent(requireActivity(), MainActivity::class.java))
    }

    private fun setLanguage(){
        val lang = preferences.getString(LANGUAGE, Locale.getDefault().language)
        with(binding){
            when(lang){
                "en" -> langGroup.check(R.id.english_item)
                "ru" -> langGroup.check(R.id.russia_item)
                "de" -> langGroup.check(R.id.deutsch_item)
                "es" -> langGroup.check(R.id.espanol_item)
                "zh" -> langGroup.check(R.id.china_item)
            }
            langGroup.forEach {
                if(it.id == langGroup.checkedRadioButtonId)
                    it.setPadding(24.dp, 0,0,0)
                else it.setPadding(42.dp, 0,0,0)
            }
        }

    }

    override fun getViewModel(): Class<SettingsViewModel> = SettingsViewModel::class.java
}