package com.jlndev.coreandroid.ext

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.observerErrorListener(
    textInputLayout: TextInputLayout,
    errorMessage: String = "",
    onValidInput: () -> Unit = {}
) {
    this.addTextChangedListener(
        ErrorTextInputWatcher(textInputLayout, errorMessage, onValidInput)
    )
}

fun TextInputEditText.isValidInput(): Boolean {
    return this.text?.isNotBlank() ?: false
}

fun String.isValidEmailAndNotEmpty(): Boolean {
    return this.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
fun TextInputEditText.isValidEmail(): Boolean {
    return this.text?.toString()?.isValidEmailAndNotEmpty() ?: false
}