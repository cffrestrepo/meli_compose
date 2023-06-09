package com.test.meli.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.test.meli.R
import com.test.meli.commons.Constants.Companion.GREEN_BACK
import com.test.meli.commons.Constants.Companion.PRODUCT
import com.test.meli.commons.Constants.Companion.RED_BACK
import com.test.meli.databinding.FragmentSearchBinding
import com.test.meli.presentation.adapters.ProductsAdapter
import com.test.meli.presentation.events.SearchEvents
import com.test.meli.commons.Extensions.withColor
import com.test.meli.commons.PermissionManager
import com.test.meli.presentation.states.SearchScreenStates
import com.test.meli.presentation.viewmodel.SearchViewModel
import com.test.meli.repository.models.ResultsModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    @Inject
    lateinit var picasso: Picasso
    private lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: SearchViewModel
    private val productsAdapter: ProductsAdapter by lazy {
        ProductsAdapter(productSetOnClickListener = ::goToProductDetail, picasso = picasso)
    }

    private var productsPreviewResultsFiltered: List<ResultsModel> = listOf()
    private var productsPreviewResults: List<ResultsModel> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        permissionManager = PermissionManager.from(this)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSearchView()
        observerBase()
        observer()
        initEvent()
    }

    private fun initEvent() {
        viewModel.postEvent(SearchEvents.InitEvent)
    }

    private fun observer() {
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchScreenStates.NavigateToProductsState -> {
                    if (state.data) {
                        setEmptySearch()
                        findNavController().navigate(R.id.action_SearchFragment_to_ProductsFragment)
                        viewModel.postEvent(SearchEvents.InactiveNavigateToProductsEvent)
                    }
                }
                is SearchScreenStates.HandledErrorState -> {
                    handledError(state.error)
                }
                is SearchScreenStates.LoadingState -> {
                    binding.progressIndicator.isVisible = state.isVisible
                }
                is SearchScreenStates.HistoryProductsLoadedState -> {
                    loadProductsPreviewSearch(state.data)
                }
            }
        }
    }

    private fun initViews() {
        with(binding.recyclerviewProducts) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productsAdapter
        }
    }

    /***
     * Shows the list of products from the last search
     */
    private fun loadProductsPreviewSearch(data: List<ResultsModel>) {
        productsPreviewResults = data
        productsPreviewResultsFiltered = data
        productsAdapter.submitList(productsPreviewResultsFiltered)

        if (data.isNotEmpty()) {
            visiblePreviewResults(true)
        }
    }

    private fun visiblePreviewResults(isVisible: Boolean) {
        if (isVisible) {
            with(binding) {
                imgWhitOutResults.isVisible = false
                tvTitlePreviewResult.isVisible = true
                recyclerviewProducts.isVisible = true
            }
        } else {
            with(binding) {
                imgWhitOutResults.isVisible = true
                tvTitlePreviewResult.isVisible = false
                recyclerviewProducts.isVisible = false
            }
        }
    }

    private fun setEmptySearch() {
        binding.searchView.setQuery("", false)
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrBlank()) {
                    Snackbar.make(
                        binding.searchView,
                        requireContext().getString(R.string.empty_search),
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    // TODO text of example: "Motorola%20G6#json"
                    viewModel.postEvent(SearchEvents.SearchEvent(query))
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    productsPreviewResultsFiltered = if (newText.isNotEmpty()) {
                        productsPreviewResults.filter {
                            it.title.contains(newText)
                        }
                    } else {
                        productsPreviewResults
                    }

                    if (productsPreviewResultsFiltered.isNotEmpty()) {
                        productsAdapter.submitList(productsPreviewResultsFiltered)
                        productsAdapter.notifyDataSetChanged()
                        visiblePreviewResults(true)
                    } else {
                        visiblePreviewResults(false)
                    }
                } ?: visiblePreviewResults(false)

                return false
            }
        })
    }

    private fun goToProductDetail(product: ResultsModel) {
        val bundle = Bundle()
        bundle.putParcelable(PRODUCT, product)
        findNavController().navigate(R.id.action_SearchFragment_to_DetailFragment, bundle)
    }

    override fun successPermission(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .withColor(Color.parseColor(GREEN_BACK))
            .show()
    }

    override fun errorPermission(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .withColor(Color.parseColor(RED_BACK))
            .show()
    }
}
