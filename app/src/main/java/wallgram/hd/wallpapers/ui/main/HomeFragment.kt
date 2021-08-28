package wallgram.hd.wallpapers.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.model.Category
import wallgram.hd.wallpapers.ui.*
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.categories.CategoriesListAdapter
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.modo.forward
import wallgram.hd.wallpapers.util.modo.selectStack
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import wallgram.hd.wallpapers.util.afterTextChangedFlow
import kotlinx.coroutines.flow.collect
import wallgram.hd.wallpapers.databinding.FragmentHomeBinding
import wallgram.hd.wallpapers.ui.favorite.container.FavoriteContainerFragment
import wallgram.hd.wallpapers.ui.search.SearchFragment
import wallgram.hd.wallpapers.util.findCurrentFragment

class HomeFragment : BaseFragment<MainViewModel, FragmentHomeBinding>(
        FragmentHomeBinding::inflate
) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    private val categoriesAdapter: CategoriesListAdapter by lazy {
        CategoriesListAdapter(onItemClicked = {
            viewModel.onItemClicked(it, WallType.CATEGORY)
        }, tag = MainFragment::class.java.simpleName)
    }

    private val colorsAdapter: ColorTagAdapter by lazy {
        ColorTagAdapter(onItemClicked = {
            viewModel.onItemClicked(it, WallType.COLOR)
        })
    }

    override fun invalidate() {
        super.invalidate()

        binding.viewPager.findCurrentFragment(childFragmentManager)?.let{
            (it as BaseFragment<*,*>).invalidate()
        }
        binding.appbar.setExpanded(true, true)
    }

    private lateinit var suggestionAdapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.allBtn.setOnClickListener {
            viewModel.onAllCategoryClicked()
        }

        viewModel.categoriesLiveData.observe(viewLifecycleOwner, ::handleCategoriesList)

        binding.listCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(ColorsItemDecoration())
            adapter = categoriesAdapter
        }

        binding.listColor.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            addItemDecoration(ColorsItemDecoration())
            adapter = colorsAdapter
        }

        colorsAdapter.submitList(resources.getIntArray(R.array.color_tags).toList())

        binding.viewPager.apply {
            offscreenPageLimit = 3
            adapter = FeedItemsAdapter(this@HomeFragment)
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab: TabLayout.Tab, position: Int -> tab.text = resources.getStringArray(R.array.feed_list)[position] }.attach()


        suggestionAdapter = ArrayAdapter<String>(
                requireContext(),
                R.layout.dropdown_menu_popup_item,
        )


        binding.searchText.setAdapter(suggestionAdapter)
        binding.searchText.setOnKeyListener { _, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                if (!binding.searchText.text.isNullOrBlank()) {
                    setSearch(binding.searchText.text.toString())
                }
                true
            } else false
        }
        binding.searchText.setOnItemClickListener { _, _, position, _ ->
            val item = suggestionAdapter.getItem(position)
            if (!item.isNullOrBlank()) {
                setSearch(item)
            }
        }

        lifecycleScope.launchWhenResumed {
            binding.searchText.afterTextChangedFlow()
                    .filterNot { it.isNullOrBlank() }
                    .debounce(300)
                    .collect {
                        viewModel.getSuggest(it.toString())
                    }
        }

        viewModel.suggestLiveData.observe(viewLifecycleOwner, { resource ->
            when(resource){
                is Resource.Success -> {
                    resource.data?.let {
                        if(it.isNotEmpty()){
                            suggestionAdapter.clear()
                            it.forEach {
                                suggestionAdapter.add(it)
                            }
                            suggestionAdapter.notifyDataSetChanged()
                        }
                        else {
                            suggestionAdapter.clear()
                            suggestionAdapter.add("No result")
                            suggestionAdapter.notifyDataSetChanged()
                        }
                    }

                }
            }
        })
    }

    private fun setSearch(item: String) {
        viewModel.onSearchClicked()
        val fragment = requireParentFragment().requireParentFragment()
        (fragment as MainFragment).getCurrentFragment()?.let {
            (it as SearchFragment).setSearch(item)
        }
    }

    private fun handleCategoriesList(status: Resource<List<Category>>) {
        when (status) {
            is Resource.Loading -> {
                // binding.progressBar.toVisible()
            }
            is Resource.Success -> {
                //binding.progressBar.toGone()
                status.data?.let {
                    categoriesAdapter.submitList(it as List<Category>)
                }
            }
            is Resource.DataError -> {
//                binding.progressBar.toGone()
//                status.errorCode?.let { viewModel.showToastMessage(it) }
            }
        }
    }


}