package wallgram.hd.wallpapers.util.modo.multi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import wallgram.hd.wallpapers.ui.main.MainFragment
import wallgram.hd.wallpapers.util.modo.MultiScreen
import wallgram.hd.wallpapers.util.modo.ModoRender

class StackContainerFragment : Fragment() {
    internal val index by lazy { requireArguments().getInt(ARG_INDEX) }

    private val render by lazy {
        object : ModoRender(childFragmentManager, CONTAINER_ID, {}) {
            override fun createMultiStackFragment(multiScreen: MultiScreen) = MainFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FrameLayout(requireContext()).apply {
        id = CONTAINER_ID
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as? MainFragment)?.setRender(index, render)
    }

    override fun onPause() {
        super.onPause()
        (parentFragment as? MainFragment)?.setRender(index, null)
    }

    fun getCurrentFragment(): Fragment? =
        childFragmentManager.findFragmentById(CONTAINER_ID)

    companion object {
        private const val CONTAINER_ID = 2387
        private const val ARG_INDEX = "arg_index"

        fun create(index: Int) = StackContainerFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_INDEX, index)
            }
        }
    }
}