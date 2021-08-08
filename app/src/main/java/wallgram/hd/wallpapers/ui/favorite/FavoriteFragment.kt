package wallgram.hd.wallpapers.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.databinding.FragmentFavoriteBinding
import wallgram.hd.wallpapers.model.Category
import wallgram.hd.wallpapers.ui.categories.CategoriesListAdapter
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.main.MainFragment
import wallgram.hd.wallpapers.ui.wallpapers.*
import wallgram.hd.wallpapers.util.args
import wallgram.hd.wallpapers.util.dp
import wallgram.hd.wallpapers.util.modo.externalForward
import wallgram.hd.wallpapers.util.modo.forward
import wallgram.hd.wallpapers.util.withArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import wallgram.hd.wallpapers.ui.ColorsItemDecoration
import wallgram.hd.wallpapers.ui.FeedItemsAdapter
import wallgram.hd.wallpapers.util.SimpleItemDecoration
import wallgram.hd.wallpapers.util.modo.selectStack


class FavoriteFragment : BaseFragment<FavoriteViewModel, FragmentFavoriteBinding>(
        FragmentFavoriteBinding::inflate
) {

    companion object {
        private const val ARG_TYPE = "arg_type"
        fun create(type: WallType) = FavoriteFragment().withArgs(
                ARG_TYPE to type
        )
    }

    private val type: WallType by args(ARG_TYPE, WallType.FAVORITE)

    private val favoritesAdapter: FavoritesAdapter by lazy {
        FavoritesAdapter(onItemClicked = {position, id ->
            viewModel.itemClicked(position, id)
        }, onItemDelete = {
            viewModel.deleteItem(it)
        })
    }

    private val categoriesAdapter: CategoriesListAdapter by lazy {
        CategoriesListAdapter(onItemClicked = {
            viewModel.onItemClicked(Screens.CategoriesList(it, type = WallType.CATEGORY))
        }, tag = MainFragment::class.java.simpleName)
    }

    private val popularAdapter: PopularAdapter by lazy {
        PopularAdapter { position, id ->
            viewModel.onItemClicked(Screens.Wallpaper(position, id), true)
        }
    }

    private val tagsAdapter: TagsAdapter by lazy{
        TagsAdapter{
            viewModel.onItemClicked(Screens.CategoriesList(type = WallType.TAG, category = it))
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavorites(type)

        viewModel.favoritesLiveData.observe(viewLifecycleOwner, { status ->
            when (status) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    status.data?.let {
                        setContentView(it.isEmpty())
                        favoritesAdapter.submitList(it)
                    }
                }
                is Resource.DataError -> {
//                binding.progressBar.toGone()
//                status.errorCode?.let { viewModel.showToastMessage(it) }
                }
            }
        })

        with(binding) {

            emptyView.allBtn.setOnClickListener {
                viewModel.onSelectStack(1)
            }

            emptyView.listCategory.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(ColorsItemDecoration())
                adapter = categoriesAdapter
            }

            emptyView.viewPager.apply {
                offscreenPageLimit = 3
                adapter = FeedItemsAdapter(this@FavoriteFragment)
            }

            TabLayoutMediator(emptyView.tabLayout, emptyView.viewPager) { tab: TabLayout.Tab, position: Int -> tab.text = resources.getStringArray(R.array.feed_list)[position] }.attach()

            favoritesView.allBtn.setOnClickListener{
                viewModel.onItemClicked(Screens.CategoriesList(type = WallType.POPULAR, category = Category()))
            }

            favoritesView.list.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                addItemDecoration(SimpleItemDecoration(2.dp))
                adapter = favoritesAdapter
            }

            favoritesView.recommendList.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                addItemDecoration(TagsOffsetDecoration())
                adapter = tagsAdapter
            }

            favoritesView.popularList.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(PopularItemDecoration())
                adapter = popularAdapter
            }
        }

    }

    private fun clearAll() {
        if(favoritesAdapter.itemCount > 0){
            MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.clear))
                    .setMessage(resources.getString(R.string.clear_confirm))
                    .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.ok_btn)) { dialog, _ ->
                        viewModel.clearAll(type)
                        dialog.dismiss()
                    }
                    .show()
        }
    }

    private fun setContentView(isEmpty: Boolean){
        if(isEmpty){
            subscibeOnEmpty()
            viewModel.getCategories()
        }
        else{
            subscribeOnNotEmpty()
            viewModel.getTags()
            viewModel.getPopular()
        }
        binding.emptyView.root.isVisible = isEmpty
        binding.favoritesView.root.isVisible = !isEmpty
    }

    private fun subscibeOnEmpty() {
        viewModel.categoriesLiveData.observe(viewLifecycleOwner, ::handleCategoriesList)
    }

    private fun handleCategoriesList(status: Resource<List<Category>>) {
        when (status) {
            is Resource.Loading -> {
                binding.progressBar.isVisible = true
            }
            is Resource.Success -> {
                binding.progressBar.isVisible = false
                status.data?.let {
                    categoriesAdapter.submitList(it)
                }
            }
            is Resource.DataError -> {
//                binding.progressBar.toGone()
//                status.errorCode?.let { viewModel.showToastMessage(it) }
            }
        }
    }

    private fun subscribeOnNotEmpty(){
        viewModel.tagsLiveData.observe(viewLifecycleOwner, { status ->
            when (status) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    status.data?.let {
                        tagsAdapter.submitList(it.list)
                    }
                }
                is Resource.DataError -> {
//                binding.progressBar.toGone()
//                status.errorCode?.let { viewModel.showToastMessage(it) }
                }
            }
        })

        viewModel.popularLiveData.observe(viewLifecycleOwner, { status ->
            when (status) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    status.data?.let {
                        popularAdapter.submitList(it.list)
                    }
                }
                is Resource.DataError -> {
//                binding.progressBar.toGone()
//                status.errorCode?.let { viewModel.showToastMessage(it) }
                }
            }
        })
    }

    override fun getViewModel(): Class<FavoriteViewModel> = FavoriteViewModel::class.java

}