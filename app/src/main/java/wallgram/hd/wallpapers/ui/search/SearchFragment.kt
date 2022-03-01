package wallgram.hd.wallpapers.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import wallgram.hd.wallpapers.R
import wallgram.hd.wallpapers.data.Resource
import wallgram.hd.wallpapers.ui.base.BaseFragment
import wallgram.hd.wallpapers.ui.wallpapers.WallpapersAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.launch
import wallgram.hd.wallpapers.databinding.FragmentSearchBinding
import wallgram.hd.wallpapers.ui.main.withMainFragment
import wallgram.hd.wallpapers.ui.wallpapers.ItemOffsetDecoration
import wallgram.hd.wallpapers.ui.wallpapers.ReposLoadStateAdapter
import wallgram.hd.wallpapers.util.afterTextChangedFlow

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {

    private lateinit var suggestionAdapter: ArrayAdapter<String>

    private val wallpapersAdapter: WallpapersAdapter by lazy {
        WallpapersAdapter { position, id ->
            viewModel.onItemClicked(position, id)
        }
    }

    override fun invalidate() {
        super.invalidate()
        binding.list.scrollToPosition(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        suggestionAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
        )


        with(binding) {

            swipeRefreshLayout.setColorSchemeResources(R.color.colorYellow)
            swipeRefreshLayout.setOnRefreshListener {
                wallpapersAdapter.refresh()
            }

            searchField.setStartIconOnClickListener {
                requireActivity().onBackPressed()
            }

            searchText.setAdapter(suggestionAdapter)
            searchText.setOnKeyListener { _, i, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    if (!binding.searchText.text.isNullOrBlank()) {
                        lifecycleScope.launch {
                            viewModel.getFlow(binding.searchText.text.toString())
                                .collectLatest { pagingData ->
                                    wallpapersAdapter.submitData(pagingData as PagingData<Any>)
                                }
                        }
                    }
                    true
                } else false
            }

            searchText.onItemClickListener = AdapterView.OnItemClickListener { parent, _,
                                                                               position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                lifecycleScope.launch {
                    viewModel.getFlow(selectedItem).collectLatest { pagingData ->
                        wallpapersAdapter.submitData(pagingData as PagingData<Any>)
                    }
                }
            }

            lifecycleScope.launchWhenResumed {
                binding.searchText.afterTextChangedFlow()
                    .filterNot { it.isNullOrBlank() }
                    .debounce(300)
                    .collect {
                        viewModel.getSuggest(it.toString())
//                            viewModel.search(it.toString()).collect{
//                                searchLiveDataPrivate.value = it
//                            }
                    }
            }

            list.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                setHasFixedSize(true)
                addItemDecoration(ItemOffsetDecoration(24))
                adapter = wallpapersAdapter.withLoadStateHeaderAndFooter(
                    header = ReposLoadStateAdapter { wallpapersAdapter.retry() },
                    footer = ReposLoadStateAdapter { wallpapersAdapter.retry() }
                )
            }
            wallpapersAdapter.addLoadStateListener { loadState ->
                // Only show the list if refresh succeeds.
                _binding?.list?.isVisible = loadState.source.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                _binding?.progressBar?.isVisible = loadState.source.refresh is LoadState.Loading
                if (swipeRefreshLayout.isRefreshing)
                    swipeRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                //  binding.errorLayout.root.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
//                    Toast.makeText(
//                            this,
//                            "\uD83D\uDE28 Wooops ${it.error}",
//                            Toast.LENGTH_LONG
//                    ).show()
                }
            }
//            errorLayout.btnRetry.setOnClickListener {
//                wallpapersAdapter.retry()
//            }

        }

        viewModel.suggestLiveData.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        if (it.isNotEmpty()) {
                            suggestionAdapter.clear()
                            it.forEach {
                                suggestionAdapter.add(it)
                            }
                            suggestionAdapter.notifyDataSetChanged()
                        } else {
                            suggestionAdapter.clear()
                            suggestionAdapter.add("No result")
                            suggestionAdapter.notifyDataSetChanged()
                        }
                    }

                }
            }
        })
    }

    override fun getViewModel(): Class<SearchViewModel> = SearchViewModel::class.java
    fun setSearch(text: String) {
        binding.searchText.setText(text)
        lifecycleScope.launch {
            viewModel.getFlow(text).collectLatest { pagingData ->
                wallpapersAdapter.submitData(pagingData as PagingData<Any>)
            }
        }
    }
}