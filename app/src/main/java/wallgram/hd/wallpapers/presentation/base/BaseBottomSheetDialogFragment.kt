package wallgram.hd.wallpapers.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import wallgram.hd.wallpapers.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

typealias DialogFragmentViewBindingInflater<B> = (
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
) -> B

abstract class BaseBottomSheetDialogFragment<V : ViewModel, B : ViewBinding>(
    private val bindingInflater: DialogFragmentViewBindingInflater<B>
) : BottomSheetDialogFragment() {

    private var _binding: B? = null

    protected val binding: B get() = _binding!!

    protected abstract val viewModel: V

    override fun getTheme(): Int = R.style.AppTheme_BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet =
            requireDialog().findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        //  bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.skipCollapsed = true

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

