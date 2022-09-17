package wallgram.hd.wallpapers.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

private typealias FragmentViewBindingInflater<B> = (
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
) -> B

abstract class BaseFragment<V : ViewModel, B : ViewBinding>(
    private val bindingInflater: FragmentViewBindingInflater<B>
) : Fragment() {

    protected var _binding: B? = null

    protected val binding: B get() = _binding!!

    protected abstract val viewModel: V

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

    open fun invalidate() {

    }

    open fun back(){

    }
}