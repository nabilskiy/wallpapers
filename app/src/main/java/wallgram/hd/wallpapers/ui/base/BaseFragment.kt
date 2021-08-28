package wallgram.hd.wallpapers.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

private typealias FragmentViewBindingInflater<B> = (
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
) -> B

abstract class BaseFragment<V : ViewModel, B : ViewBinding>(
    private val bindingInflater: FragmentViewBindingInflater<B>
) : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: B? = null

    protected val binding: B get() = _binding!!

    protected lateinit var viewModel: V

    protected abstract fun getViewModel(): Class<V>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(getViewModel())
    }

    open fun invalidate() {

    }
}