package com.jlndev.coreandroid.customview

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.jlndev.coreandroid.databinding.AlertViewItemBinding
import com.jlndev.coreandroid.ext.visible

class DialogCustomView(private val builder: Builder) {
    fun show() {
        builder.alertDialog.show()
    }

    class Builder(val context: Context) {
        private val binding = AlertViewItemBinding.inflate(LayoutInflater.from(context))
        internal val alertDialog by lazy {
            AlertDialog.Builder(context)
                .setView(binding.root)
                .create()
        }

        fun setTitle(title: String): Builder {
            binding.alertTitleView.apply {
                visible()
                text = title
            }
            return this
        }

        fun setSubtitle(subtitle: String): Builder {
            binding.alertSubtitleView.apply {
                visible()
                text = subtitle
            }
            return this
        }

        fun setPositiveButton(nameButton: String? = "OK", action: () -> Unit): Builder {
            binding.alertYesBtnView.apply {
                visible()
                text = nameButton
                setOnClickListener {
                    action.invoke()
                    alertDialog.dismiss()
                }
            }
            return this
        }

        fun setNegativeButton(nameButton: String? = "CANCEL", action: (() -> Unit)? = null): Builder {
            binding.alertNoBtnView.apply {
                visible()
                text = nameButton
                setOnClickListener {
                    action?.invoke()
                    alertDialog.dismiss()
                }
            }
            return this
        }

//        fun setRoundEdges(isPaddingApply: Boolean): Builder {
//            alertDialog.apply {
//                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            }
//            binding.dialogViewGroup.apply {
//                setBackgroundResource(R.drawable.diaolog_rounded_background)
//                if (isPaddingApply) {
//                   setPadding(5,5,5,5)
//                }
//            }
//            return this
//        }

        fun setCanceledOnTouchOutside(): Builder {
            alertDialog.setCanceledOnTouchOutside(false)
            return this
        }

        fun build(): DialogCustomView {
            return DialogCustomView(this)
        }
    }
}
