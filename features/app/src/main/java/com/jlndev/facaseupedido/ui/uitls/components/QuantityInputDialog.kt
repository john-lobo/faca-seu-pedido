package com.jlndev.facaseupedido.ui.uitls.components

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.ItemQuantityDialogBinding

class QuantityInputDialog(private val context: Context, private val quantity: Long = 1) {
    fun show(onQuantityEntered: (Long) -> Unit) {
        val binding = ItemQuantityDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)

        with(binding) {
            builder.create().also { dialog ->
                dialog.setView(binding.root)
                titleDialogView.text = context.getString(R.string.inform_quantity)
                quantityDialogView.setText(quantity.toString())

                confirmDialogView.setOnClickListener {
                    val quantityText = quantityDialogView.text.toString()
                    if (quantityText.isNotEmpty()) {
                        val quantity = quantityText.toLong()
                        onQuantityEntered(quantity)
                        dialog.dismiss()
                    }
                }
                dialog.show()
            }
        }
    }
}

