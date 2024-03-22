package com.jlndev.coreandroid.bases.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.coreandroid.ext.showSnackbar

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>: Fragment(),
    BaseFragmentInterface<VB, VM> {

    override var _binding: VB? = null
    override val binding: VB
        get() = _binding!!

    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = onGetViewBinding(inflater, container)
        mContext = binding.root.context
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitViews()
        onInitViewModel()
    }

    fun showError(message: String,actionText: String? = null, view: View? = null ,action: (() -> Unit)? = null) {
        binding.root.showSnackbar(message, actionText, view, action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}