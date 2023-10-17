package com.jlndev.facaseupedido.ui.orderhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.fragment.BaseFragment
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.databinding.FragmentOrderHistoryBinding
import com.jlndev.facaseupedido.ui.orderhistory.adapter.OrderHistoryAdapter
import com.jlndev.facaseupedido.ui.uitls.ext.toOrderHistoryItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderHistoryFragment : BaseFragment<FragmentOrderHistoryBinding, OrderHistoryViewModel>() {

    override val viewModel: OrderHistoryViewModel by viewModel()
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter

    override fun onInitData() {
        viewModel.getAllOrders()
    }

    override fun onGetViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderHistoryBinding = FragmentOrderHistoryBinding.inflate(inflater, container, false)

    override fun onInitViews() {
        orderHistoryAdapter = OrderHistoryAdapter()
        binding.recyclerOrdersHistoryItemsView.adapter = orderHistoryAdapter
    }

    override fun onInitViewModel() {
        viewModel.getOrdersHistoryLive.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Success -> {
                    if(it.data.isNotEmpty()) {
                        orderHistoryAdapter.submitList(it.data.map { it.toOrderHistoryItem() })
                    } else {
                        showNotFoundOrdersHistoryView()
                    }
                }
                is ResponseState.Error -> {
                    showErrorView()
                }
            }
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showView() {
        with(binding) {
            errorView.root.gone()
            notFoundOrderHistoryView.root.gone()
            recyclerOrdersHistoryItemsView.visible()
        }
    }

    override fun showErrorView() {
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
}