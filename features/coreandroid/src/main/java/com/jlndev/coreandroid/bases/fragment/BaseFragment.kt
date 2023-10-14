package com.jlndev.coreandroid.bases.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>: Fragment(),
    BaseFragmentInterface<VB, VM> {

    override var _binding: VB? = null
    override val binding: VB
        get() = _binding!!

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitViews()
        onInitViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}