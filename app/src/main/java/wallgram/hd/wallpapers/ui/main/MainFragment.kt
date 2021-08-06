package wallgram.hd.wallpapers.ui.main

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.databinding.FragmentMainBinding
import wallgram.hd.wallpapers.model.Category
import wallgram.hd.wallpapers.ui.*
import wallgram.hd.wallpapers.ui.base.ViewModelFactory
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.Common
import wallgram.hd.wallpapers.util.modo.*
import wallgram.hd.wallpapers.util.modo.multi.StackContainerFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_main.view.*
import javax.inject.Inject


open class MainFragment : MultiStackFragment() {

    private var _binding: FragmentMainBinding? = null
    protected val binding: FragmentMainBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: MainViewModel

    private val manager: ReviewManager by lazy {
        ReviewManagerFactory.create(requireContext())
    }

    private var multiScreen: MultiScreen? = null
        set(value) {
            if (value != null) {
                if (field == null) {

                }

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(MainViewModel::class.java)
    }

    private val categoriesAdapter: MenuCategoriesListAdapter by lazy {
        MenuCategoriesListAdapter {
            modo.selectStack(1)
            modo.forward(Screens.CategoriesList(it, type = WallType.CATEGORY))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.categoriesLiveData.observe(viewLifecycleOwner, ::handleCategoriesList)

        binding.categoriesList.apply {
            layoutManager = LinearLayoutManager(requireContext())
       //     addItemDecoration(ItemOffsetDecoration())
            adapter = categoriesAdapter
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> modo.selectStack(0)
                R.id.category -> modo.selectStack(1)
                R.id.favorites -> modo.selectStack(2)
                R.id.history -> modo.selectStack(3)
                R.id.search -> modo.selectStack(4)
            }
            true
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.menuItems.children.forEach { v ->
            v.setOnClickListener {
                onClickMenuItem(it)
            }
        }

    }

    private fun handleCategoriesList(status: Resource<List<Category>>) {
        when (status) {
            is Resource.Success -> {
                //binding.progressBar.toGone()
                status.data?.let {
                    categoriesAdapter.submitList(it)
                }
            }
        }
    }

    private fun onClickMenuItem(view: View) {
        when (view.id) {
            R.id.home_item -> modo.selectStack(0)
            R.id.category_item -> modo.selectStack(1)
            R.id.favorites_item -> modo.selectStack(2)
            R.id.history_item -> modo.selectStack(3)
            R.id.settings_item -> modo.externalForward(Screens.Settings())
            R.id.subs_item -> modo.externalForward(Screens.Subscription())
            R.id.feedback_item -> showRateApp()
            R.id.site_item -> modo.launch(Screens.Browser(wallgram.hd.wallpapers.util.Common.getSiteUrl()))
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun showRateApp() {
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
            } else {
                showRateAppFallbackDialog()
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
            Toast.makeText(requireContext(), "Уже выбрано", Toast.LENGTH_SHORT).show()
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