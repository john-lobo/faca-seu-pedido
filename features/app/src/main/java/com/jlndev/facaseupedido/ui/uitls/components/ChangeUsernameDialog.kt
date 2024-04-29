package com.jlndev.facaseupedido.ui.uitls.components

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.textfield.TextInputLayout
import com.jlndev.coreandroid.ext.observerErrorListener
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.ChangeUsernameDialogBinding
import com.jlndev.facaseupedido.databinding.ItemChangePasswordDialogBinding
import com.jlndev.facaseupedido.databinding.ItemQuantityDialogBinding

class ChangeUsernameDialog(private val context: Context) {
    fun show(onQuantityEntered: (String) -> Unit) {
        val binding = ChangeUsernameDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)

        with(binding) {
            builder.create().also { dialog ->
                dialog.setView(binding.root)
                titleDialogView.text = context.getString(R.string.change_username)

                val fields = listOf(
                    nameInputView,
                )

                fields.forEach { inputView ->
                    inputView.observerErrorListener(inputView.parent.parent as TextInputLayout) {
                        val fullNameValid =
                            binding.nameInputLayoutView.error == null && binding.nameInputView.text!!.isNotEmpty()
                        binding.confirmDialogView.isEnabled = fullNameValid
                    }
                }

                confirmDialogView.setOnClickListener {
                    onQuantityEntered(binding.nameInputView.text.toString())
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }
}

