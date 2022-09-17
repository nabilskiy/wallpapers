package wallgram.hd.wallpapers.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.views.slidinguppanel.Feature
import wallgram.hd.wallpapers.presentation.base.views.slidinguppanel.ISlidingUpPanelLayoutHost
import wallgram.hd.wallpapers.presentation.base.views.slidinguppanel.Show
import wallgram.hd.wallpapers.presentation.base.views.slidinguppanel.SlidingUpPanelLayout

private typealias FragmentSlidingUpViewBindingInflater<B> = (
    inflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
) -> B

abstract class BaseSlidingUpFragment<V : ViewModel, B : ViewBinding>(
    private val bindingInflater: FragmentSlidingUpViewBindingInflater<B>
) : Fragment(), ISlidingUpPanelLayoutHost, Show {

    protected lateinit var mSlidingUpPanelLayout: SlidingUpPanelLayout

    protected var _binding: B? = null

    protected val binding: B get() = _binding!!

    protected abstract val viewModel: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = bindingInflater.invoke(inflater, container, false)
        mSlidingUpPanelLayout = binding.root.findViewById(R.id.sliding_layout)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSlidingUpPanelLayout.setFadeOnClickListener {
            mSlidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }

        if (savedInstanceState == null) return
        val slideOffset = savedInstanceState.getFloat("cached_slide_offset", 0f)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun invalidate() {}

    open fun back() {
        if (mSlidingUpPanelLayout.panelState != SlidingUpPanelLayout.PanelState.COLLAPSED)
            mSlidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    override fun show(feature: Feature) {
        val fragment = childFragmentManager
            .findFragmentById(R.id.sliding_up_panel_fragment_container) as BaseFragment<*, *>?
        if (fragment != null) removeIfAddedFeature()
        childFragmentManager
            .beginTransaction()
            .replace(R.id.sliding_up_panel_fragment_container, feature.fragment)
            .commit()

        mSlidingUpPanelLayout.anchorPoint = feature.anchorPoint
        mSlidingUpPanelLayout.post {
            mSlidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
        }
    }

    fun panelShowed() = mSlidingUpPanelLayout.panelState !=
            SlidingUpPanelLayout.PanelState.COLLAPSED

    fun hideIfOpenedFeature() {
        mSlidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    protected fun removeIfAddedFeature() {
        val fragmentManager = childFragmentManager
        val fragment =
            fragmentManager.findFragmentById(R.id.sliding_up_panel_fragment_container)
                ?: return
        fragmentManager
            .beginTransaction()
            .remove(fragment)
            .commitAllowingStateLoss()
    }

    override fun onRecyclerViewAttached(view: RecyclerView) {
        mSlidingUpPanelLayout.setScrollableView(view)
    }

    override fun onViewDetached() {
        val layout = mSlidingUpPanelLayout
        layout.setScrollableView(null)
    }

    override fun hide() {
        if (mSlidingUpPanelLayout.panelState != SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mSlidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }


}