package wallgram.hd.wallpapers.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.local.preference.LANGUAGE
import wallgram.hd.wallpapers.data.local.preference.PreferenceContract
import wallgram.hd.wallpapers.databinding.FragmentSettingsBinding
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.main.MainActivity
import wallgram.hd.wallpapers.ui.main.MainViewModel
import wallgram.hd.wallpapers.util.FileUtils
import wallgram.hd.wallpapers.util.dp
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wallgram.hd.wallpapers.util.modo.back
import java.util.*
import javax.inject.Inject

class SettingsFragment : BaseFragment<MainViewModel, FragmentSettingsBinding>(
        FragmentSettingsBinding::inflate
) {

    private val modo = wallgram.hd.wallpapers.App.modo

    @Inject
    lateinit var preferences: PreferenceContract

    private val fileUtils: FileUtils by lazy {
        FileUtils()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            backBtn.setOnClickListener { modo.back() }
            clearBtn.setOnClickListener { clearCacheImages() }
            setLanguage()
            langGroup.setOnCheckedChangeListener { _, i ->
                selectLanguage(i)
            }

            lifecycleScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    cacheValue.text = resources.getString(R.string.cache_size, fileUtils.getFileSize(fileUtils.getFolderSize(requireContext().cacheDir)))
                }
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
        startActivity(Intent(requireContext(), MainActivity::class.java))
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

    private fun clearCacheImages() {
        lifecycleScope.launch(Dispatchers.IO) {
            Glide.get(requireContext()).clearDiskCache()
            withContext(Dispatchers.Main) {
                binding.cacheValue.text = resources.getString(R.string.cache_size, fileUtils.getFileSize(fileUtils.getFolderSize(requireContext().cacheDir)))
            }
        }
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java
}