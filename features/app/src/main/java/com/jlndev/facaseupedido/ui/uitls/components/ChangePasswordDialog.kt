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

class ChangePasswordDialog(private val context: Context) {

    fun show(onChangePassword: (String, String) -> Unit) {
        val binding = ItemChangePasswordDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)

        with(binding) {
            builder.create().also { dialog ->
                dialog.setView(binding.root)
                titleDialogView.text = context.getString(R.string.change_password)

                val fields = listOf(
                    newPasswordInputView, oldPasswordInputView
                )

                fields.forEach { inputView ->
                    inputView.observerErrorListener(inputView.parent.parent as TextInputLayout) {
                        val oldPasswordValid =
                            binding.oldPasswordInputLayoutView.error == null && binding.oldPasswordInputView.text!!.isNotEmpty()
                        val newPasswordValid =
                            binding.newPasswordInputLayoutView.error == null && binding.newPasswordInputView.text!!.isNotEmpty()
                        binding.confirmDialogView.isEnabled = newPasswordValid && oldPasswordValid
                    }
                }

                confirmDialogView.setOnClickListener {
                    onChangePassword(
                        binding.oldPasswordInputView.text.toString(),
                        binding.newPasswordInputView.text.toString()
                    )
                    dialog.dismiss()
                }
                dialog.show()
            }
        }

    }
}

