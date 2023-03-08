package wallgram.hd.wallpapers.presentation.main

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.presentation.base.Screens
import wallgram.hd.wallpapers.databinding.FragmentMainBinding
import wallgram.hd.wallpapers.util.modo.*
import wallgram.hd.wallpapers.util.modo.multi.StackContainerFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.core.data.PreferenceDataStore
import wallgram.hd.wallpapers.presentation.base.BaseActivity
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.presentation.wallpaper.DownloadsCountStore
import wallgram.hd.wallpapers.presentation.wallpaper.SubsTopicStore
import javax.inject.Inject

@AndroidEntryPoint
open class MainFragment : MultiStackFragment() {

    private var _binding: FragmentMainBinding? = null
    protected val binding: FragmentMainBinding get() = _binding!!

    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore

    private lateinit var subsTopicStore: SubsTopicStore.Mutable

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


    private val modo = wallgram.hd.wallpapers.App.modo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        subsTopicStore = SubsTopicStore.Base(preferenceDataStore)
        return binding.root
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


        if (!subsTopicStore.read()) {
            Firebase.messaging.subscribeToTopic("news_" + getCurrentLanguage())
                .addOnCompleteListener { task ->
                    var msg = "Subscribed"
                    if (!task.isSuccessful) {
                        msg = "Subscribe failed"
                    } else {
                        subsTopicStore.save(true)
                    }
                }
        }
    }



    private fun getCurrentLanguage(): String {
        return (requireActivity() as BaseActivity<*>).getCurrentLanguage().language
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

fun Fragment.withParentFragment(action: BaseFragment<*, *>.() -> Unit) {
    parentFragment
        ?.let { it as? BaseFragment<*, *> }
        ?.also(action)
}