package wallgram.hd.wallpapers.presentation.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.databinding.FragmentCategoriesBinding
import wallgram.hd.wallpapers.presentation.base.BaseFragment
import wallgram.hd.wallpapers.presentation.base.CustomRecyclerView
import wallgram.hd.wallpapers.presentation.base.NoLimitRecycledViewPool
import wallgram.hd.wallpapers.presentation.base.adapter.CarouselItemDecoration
import wallgram.hd.wallpapers.presentation.base.adapter.GenericAdapter
import wallgram.hd.wallpapers.presentation.base.adapter.HeaderItemDecoration
import wallgram.hd.wallpapers.presentation.base.adapter.InsetItemDecoration
import wallgram.hd.wallpapers.presentation.colors.ColorUi
import wallgram.hd.wallpapers.presentation.filters.FilterUi
import wallgram.hd.wallpapers.util.dp

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<CategoriesViewModel, FragmentCategoriesBinding>(
    FragmentCategoriesBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorAdapter = CustomRecyclerView.ColorsAdapter()

        val recycledViewPool: RecyclerView.RecycledViewPool = NoLimitRecycledViewPool()

        val categoryAdapter = CategoryAdapter(colorAdapter, recycledViewPool)

        val gridLayoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false).apply {
                spanSizeLookup = categoryAdapter.getSpanSizeLookup(2)
            }

        with(binding){
            swipeRefreshLayout.apply {
                setColorSchemeResources(R.color.colorYellow)
                setOnRefreshListener {
                    viewModel.loadData()
                }
            }

            categoriesList.apply {
                layoutManager = gridLayoutManager

                addItemDecoration(HeaderItemDecoration(12.dp, 0))
                addItemDecoration(ColorCarouselItemDecoration(16.dp, 4))
                addItemDecoration(InsetItemDecoration(16.dp, 1))
                adapter = categoryAdapter
            }
        }

        viewModel.categoriesLiveData.observe(
            viewLifecycleOwner
        ) { filtersUi ->
            filtersUi.map(categoryAdapter)
        }

        viewModel.progressLiveData.observe(viewLifecycleOwner){ refreshing ->
            refreshing.apply(binding.swipeRefreshLayout)
        }
    }

    override fun invalidate() {
        super.invalidate()
        binding.categoriesList.smoothScrollToPosition(0)
    }

    override val viewModel: CategoriesViewModel by viewModels()
}