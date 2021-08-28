package wallgram.hd.wallpapers.ui.base

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import dagger.android.AndroidInjection
import wallgram.hd.wallpapers.util.localization.OnLocaleChangedListener
import java.util.*
import javax.inject.Inject

private typealias ActivityViewBindingInflater<B> = (
    inflater: LayoutInflater
) -> B

abstract class BaseActivity<V : ViewModel, B : ViewBinding>(private val bindingInflater: ActivityViewBindingInflater<B>)
    :AppCompatActivity(), OnLocaleChangedListener{

    lateinit var binding: B

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: V

    protected abstract fun getViewModel(): Class<V>

    private val localizationDelegate: LocalizationActivityDelegate by lazy{
        LocalizationActivityDelegate(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
        viewModel = viewModelFactory.create(getViewModel())
    }

    override fun onResume() {
        super.onResume()
        localizationDelegate.onResume(this)
    }

    override fun attachBaseContext(newBase: Context) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase))
        super.attachBaseContext(newBase)
    }

    override fun getBaseContext() = localizationDelegate.getApplicationContext(super.getBaseContext())

    override fun getApplicationContext() = localizationDelegate.getApplicationContext(super.getApplicationContext())

    override fun getResources() = localizationDelegate.getResources(super.getResources())

    override fun onBeforeLocaleChanged() {
    }

    override fun onAfterLocaleChanged() {
    }

    fun setLanguage(language: String){
        localizationDelegate.setLanguage(this, language)
    }

    fun setLanguage(language: String, country: String){
        localizationDelegate.setLanguage(this, language, country)
    }

    fun setLanguage(locale: Locale) {
        localizationDelegate.setLanguage(this, locale)
    }

    fun getCurrentLanguage() = localizationDelegate.getLanguage(this)

}