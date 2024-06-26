package com.jlndev.facaseupedido.ui.orderhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.jlndev.baseservice.state.ViewState
import com.jlndev.coreandroid.bases.fragment.BaseFragment
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.FragmentOrderHistoryBinding
import com.jlndev.facaseupedido.ui.item.DetailsFragment
import com.jlndev.facaseupedido.ui.orderhistory.adapter.OrderHistoryAdapter
import com.jlndev.facaseupedido.ui.orderhistory.adapter.OrderHistoryAdapterListener
import com.jlndev.facaseupedido.ui.uitls.ext.toOrderHistoryItem
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderHistoryFragment : BaseFragment<FragmentOrderHistoryBinding, OrderHistoryViewModel>() {

    override val viewModel: OrderHistoryViewModel by viewModel()
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter

    override fun onInitData() {
        viewModel.getUser()
    }

    override fun onGetViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderHistoryBinding = FragmentOrderHistoryBinding.inflate(inflater, container, false)

    override fun onInitViews() {
        orderHistoryAdapter = OrderHistoryAdapter(object : OrderHistoryAdapterListener {
            override fun clickedProductItem(item: ProductItem) {
                findNavController().navigate(
                    R.id.action_order_history_to_details,
                    bundleOf(
                        DetailsFragment.KEY_PRODUCT_ITEM to item,
                        DetailsFragment.KEY_SHOW_BUTTON to false
                    )
                )
            }
        })
        with(binding) {
            recyclerOrdersHistoryItemsView.adapter = orderHistoryAdapter
            notFoundOrderHistoryView.retryButton.setOnClickListener {
                navToHome()
            }
            errorView.retryButton.setOnClickListener {
                viewModel.getAllOrders()
            }
        }
    }

    override fun onInitViewModel() {
        with(viewModel) {
            getOrdersHistoryLive.observe(viewLifecycleOwner) {
                when (it) {
                    is ViewState.Success -> {
                        val items = it.data.map { it.toOrderHistoryItem() }
                        orderHistoryAdapter.submitList(items)
                        if (items.isNotEmpty()) {
                            showView()
                        } else {
                            showNotFoundOrdersHistoryView()
                        }
                    }

                    is ViewState.Error -> {
                        showErrorView()
                    }

                    else -> {}
                }
            }

            userLive.observe(viewLifecycleOwner) {
                when (it) {
                    is ViewState.Success -> {
                        it.data?.let {
                            viewModel.getAllOrders()
                        } ?: run {
                            findNavController().navigate(R.id.action_order_history_to_login)
                        }
                    }

                    is ViewState.Error -> {
                        findNavController().navigate(R.id.action_order_history_to_login)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun showView() {
        with(binding) {
            errorView.root.gone()
            notFoundOrderHistoryView.root.gone()
            recyclerOrdersHistoryItemsView.visible()
        }
    }

    private fun showErrorView() {
        with(binding) {
            notFoundOrderHistoryView.root.gone()
            recyclerOrdersHistoryItemsView.gone()
            errorView.root.visible()
        }
    }

    private fun showNotFoundOrdersHistoryView() {
        with(binding) {
            errorView.root.gone()
            recyclerOrdersHistoryItemsView.gone()
            notFoundOrderHistoryView.root.visible()
        }
    }

    private fun navToHome() {
        findNavController().popBackStack()
    }
}