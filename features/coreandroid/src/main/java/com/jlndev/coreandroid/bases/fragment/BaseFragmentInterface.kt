package com.jlndev.coreandroid.bases.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface BaseFragmentInterface<VB : ViewBinding, VM> {

    var _binding: VB?
    val binding: VB
    val viewModel: VM

    fun onInitData()
    fun onGetViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    fun onInitViews()
    fun onInitViewModel()

    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = onGetViewBinding(inflater, container)
        return binding.root
    }
}
