package wallgram.hd.wallpapers.util.modo.multi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.feed.FeedsFragment
import wallgram.hd.wallpapers.presentation.history.HistoryFragment
import wallgram.hd.wallpapers.presentation.main.MainFragment
import wallgram.hd.wallpapers.presentation.settings.language.LanguageFragment
import wallgram.hd.wallpapers.presentation.subscribe.SubscriptionFragment
import wallgram.hd.wallpapers.util.modo.AppScreen
import wallgram.hd.wallpapers.util.modo.MultiScreen
import wallgram.hd.wallpapers.util.modo.ModoRender

class StackContainerFragment : Fragment() {
    internal val index by lazy { requireArguments().getInt(ARG_INDEX) }

    private val render by lazy {
        object : ModoRender(childFragmentManager, CONTAINER_ID, {}) {
            override fun createMultiStackFragment(multiScreen: MultiScreen) = MainFragment()

            override fun setupTransaction(
                fragmentManager: FragmentManager,
                transaction: FragmentTransaction,
                screen: AppScreen,
                newFragment: Fragment
            ) {
                if (newFragment is LanguageFragment || newFragment is SubscriptionFragment
                    || newFragment is FeedsFragment || newFragment is HistoryFragment) {
                    transaction.setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                }
            }
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