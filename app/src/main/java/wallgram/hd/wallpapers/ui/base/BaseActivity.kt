package wallgram.hd.wallpapers.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

private typealias ActivityViewBindingInflater<B> = (
    inflater: LayoutInflater
) -> B

abstract class BaseActivity<V : ViewModel, B : ViewBinding>(private val bindingInflater: ActivityViewBindingInflater<B>)
    :AppCompatActivity(){

    lateinit var binding: B

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: V

    protected abstract fun getViewModel(): Class<V>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)
        viewModel = viewModelFactory.create(getViewModel())
    }

}