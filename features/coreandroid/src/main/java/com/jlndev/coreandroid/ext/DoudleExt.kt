package com.jlndev.coreandroid.ext

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun Double.toCurrency(): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    if (numberFormat is DecimalFormat) {
        numberFormat.isParseBigDecimal = true
    }
    return numberFormat.format(this)
}
