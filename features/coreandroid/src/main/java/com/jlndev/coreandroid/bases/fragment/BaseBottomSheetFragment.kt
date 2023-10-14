package com.jlndev.coreandroid.bases.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jlndev.coreandroid.R
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel

abstract class BaseBottomSheetFragment<VB : ViewBinding, VM : BaseViewModel>: BottomSheetDialogFragment(),
    BaseFragmentInterface<VB, VM> {

    override var _binding: VB? = null
    override val binding: VB
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitData()
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.apply {
            dismissWithAnimation = true
            setOnShowListener { dialogInterface ->
                val bsDialog = dialogInterface as BottomSheetDialog
                val bottomSheet =
                    bsDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                val height = binding.root.measuredHeight

                BottomSheetBehavior.from(bottomSheet!!).peekHeight = height
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED

                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = height
                bottomSheet.layoutParams = layoutParams
            }
            return dialog
        }
    }
}