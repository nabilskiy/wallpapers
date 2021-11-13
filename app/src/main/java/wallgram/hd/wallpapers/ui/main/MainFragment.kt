package wallgram.hd.wallpapers.ui.main

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.ui.base.Screens
import wallgram.hd.wallpapers.databinding.FragmentMainBinding
import wallgram.hd.wallpapers.ui.base.ViewModelFactory
import wallgram.hd.wallpapers.util.modo.*
import wallgram.hd.wallpapers.util.modo.multi.StackContainerFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.AndroidSupportInjection
import wallgram.hd.wallpapers.ui.base.BaseFragment
import javax.inject.Inject


open class MainFragment : MultiStackFragment() {

    private var _binding: FragmentMainBinding? = null
    protected val binding: FragmentMainBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: MainViewModel



    private var multiScreen: MultiScreen? = null
        set(value) {
            if (value != null) {
                field = value
                localRenders.forEach { (index, render) ->
                    value.stacks.getOrNull(index)?.let { state ->
                        render(state)
                    }
                }
                selectTab(value.selectedStack)
            }
        }

    private val localRenders = mutableMapOf<Int, NavigationRender>()
    internal fun setRender(index: Int, render: NavigationRender?) {
        if (render != null) {
            localRenders[index] = render
            multiScreen?.stacks?.getOrNull(index)?.let { state ->
                render(state)
            }
        } else {
            localRenders.remove(index)
        }
    }

    override fun applyMultiState(multiScreen: MultiScreen) {
        this.multiScreen = multiScreen
    }

    override fun getCurrentFragment(): Fragment? =
        childFragmentManager.fragments
            .filterIsInstance<StackContainerFragment>()
            .firstOrNull { it.isVisible }
            ?.getCurrentFragment()

    private fun getFragment(index: Int): Fragment? =
        childFragmentManager.fragments
            .filterIsInstance<StackContainerFragment>()
            .firstOrNull { it.index == index }
            ?.getCurrentFragment()


    private val modo = wallgram.hd.wallpapers.App.modo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            bottomNavigationView.setOnItemSelectedListener { it ->
                if (multiScreen?.selectedStack == it.order)
                    getCurrentFragment()?.let { fragment ->
                        (fragment as BaseFragment<*, *>).invalidate()
                    }
                else {
                    if (multiScreen?.selectedStack == 4) {
                        getCurrentFragment()?.let { fragment ->
                            (fragment as BaseFragment<*, *>).invalidate()
                        }
                    }
                    modo.selectStack(it.order)
                }
                true
            }
        }

    }

    private fun showRateAppFallbackDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.rate_title)
            .setMessage("If you are enjoying our app, please take a moment to rate it on PlayStore. Thanks for your support!")
            .setPositiveButton("Rate Now") { dialog, which -> redirectToPlayStore() }
            .setNegativeButton("Remind me later") { dialog, which -> }
            .setNeutralButton("No, Thanks") { dialog, which -> }
            .setOnDismissListener(DialogInterface.OnDismissListener { dialog: DialogInterface? -> })
            .show()
    }

    private fun redirectToPlayStore() {
        val appPackageName: String = requireContext().packageName
        try {
            modo.launch(Screens.Browser("market://details?id=$appPackageName"))
        } catch (exception: ActivityNotFoundException) {
            modo.launch(Screens.Browser("https://play.google.com/store/apps/details?id=$appPackageName"))
        }
    }

    private fun selectTab(index: Int) {

        binding.bottomNavigationView.menu.getItem(index).isChecked = true

        val addedFragments =
            childFragmentManager.fragments.filterIsInstance<StackContainerFragment>()

        val currentContainerFragment = addedFragments.firstOrNull { it.isVisible }
        if (currentContainerFragment?.index == index) {
            return
        }

        childFragmentManager.beginTransaction().also { transaction ->
            val tabExists = addedFragments.any { it.index == index }
            if (!tabExists) {
                transaction.add(
                    R.id.mainScreenContainer,
                    StackContainerFragment.create(index),
                    index.toString()
                )
            }
            addedFragments.forEach { f ->
                if (f.index == index && !f.isVisible) {
                    transaction.show(f)
                } else if (f.index != index && f.isVisible) {
                    transaction.hide(f)
                }
            }
        }.commitNowAllowingStateLoss()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}

fun Fragment.withMainFragment(action: MainFragment.() -> Unit) {
    parentFragment
        ?.let { it as? MainFragment }
        ?.also(action)
}

fun Fragment.withParentFragment(action: BaseFragment<*,*>.() -> Unit) {
    parentFragment
        ?.let { it as? BaseFragment<*,*> }
        ?.also(action)
}