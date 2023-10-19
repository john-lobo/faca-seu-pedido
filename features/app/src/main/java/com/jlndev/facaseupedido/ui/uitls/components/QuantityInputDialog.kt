package com.jlndev.facaseupedido.ui.uitls.components

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.widget.EditText
import com.jlndev.facaseupedido.R

class QuantityInputDialog(private val context: Context, private val  quantity: Double = 1.0) {
    fun show(onQuantityEntered: (Double) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.informe_quantity))

        val input = EditText(context)
        input.hint = context.getString(R.string.quantity)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        input.setText(quantity.toString())
        builder.setView(input)

        builder.setPositiveButton(context.getString(R.string.ok)) { dialog, _ ->
            val quantityText = input.text.toString()
            if (quantityText.isNotEmpty()) {
                val quantity = quantityText.toDouble()
                onQuantityEntered(quantity)
                dialog.dismiss()
            }
        }
        builder.setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }
}
