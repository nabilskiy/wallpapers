package wallgram.hd.wallpapers.ui.categories

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import wallgram.hd.wallpapers.App
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.Screens
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.databinding.FragmentCategoriesBinding
import wallgram.hd.wallpapers.model.Category
import wallgram.hd.wallpapers.model.request.FeedRequest
import wallgram.hd.wallpapers.ui.HeaderAdapter
import wallgram.hd.wallpapers.ui.ItemOffsetDecoration
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.wallpapers.WallType
import wallgram.hd.wallpapers.util.modo.forward

class CategoriesFragment : BaseFragment<CategoriesViewModel, FragmentCategoriesBinding>(
        FragmentCategoriesBinding::inflate
) {
    private val modo = wallgram.hd.wallpapers.App.modo

    private val categoriesAdapter: CategoriesListAdapter by lazy {
        CategoriesListAdapter(onItemClicked = {
            modo.forward(Screens.CategoriesList(FeedRequest(type = WallType.CATEGORY, category = it.id, categoryName = it.name)))
        }, tag = CategoriesFragment::class.java.simpleName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.categoriesLiveData.observe(viewLifecycleOwner, ::handleCategoriesList)

        binding.categoriesList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int = if (position == 0) 2 else 1
                }
            }
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration())
            adapter = ConcatAdapter(HeaderAdapter(getString(R.string.category)),
                    categoriesAdapter)
        }

    }

    override fun invalidate() {
        super.invalidate()
        binding.categoriesList.smoothScrollToPosition(0)
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
                binding.progressBar.isVisible = false
            }
        }
    }

    override fun getViewModel(): Class<CategoriesViewModel> = CategoriesViewModel::class.java

}