package com.jlndev.coreandroid.ext

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import com.jlndev.coreandroid.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ErrorTextInputWatcher(
    private val textInputLayout: TextInputLayout,
    private val errorMessage: String,
    private val onValidInput: () -> Unit
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        val inputText = s.toString()

        if (inputText.isBlank()) {
            textInputLayout.error = errorMessage
            onValidInput.invoke()
            return
        } else {
            textInputLayout.error = null
        }

        with(textInputLayout.context) {
            when (textInputLayout.hint) {
                getString(R.string.email) -> handleEmailValidation(inputText)
                getString(R.string.cpf) -> handleCPFValidation(inputText)
                getString(R.string.birthday) -> handleDateOfBirthValidation(inputText)
                getString(R.string.full_name) -> handleFullNameValidation(inputText)
                getString(R.string.password), getString(R.string.confirm_password) -> handlePasswordValidation(inputText)
            }
        }

        onValidInput.invoke()
    }

    private fun handleEmailValidation(inputText: String) {
        val pattern = Patterns.EMAIL_ADDRESS
        val matcher = pattern.matcher(inputText)

        if (!matcher.matches()) {
            textInputLayout.error = "E-mail inválido"
        } else {
            textInputLayout.error = null
        }
    }

    private fun handleCPFValidation(inputText: String) {
        val cpf = inputText.replace(".", "").replace("-", "")
        if (!isCPF(cpf)) {
            textInputLayout.error = "CPF inválido"
        } else {
            textInputLayout.error = null
        }
    }

    private fun handleDateOfBirthValidation(inputText: String) {
        try {
            val inputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            inputDateFormat.isLenient = false // torna a validação da data mais restrita
            val date = inputDateFormat.parse(inputText)

            if (date == null || date.after(Date()) || date.before(inputDateFormat.parse("01/01/1923"))) {
                textInputLayout.error = "Data de nascimento inválida"
            } else {
                textInputLayout.error = null // limpa a mensagem de erro
            }
        } catch (e: ParseException) {
            textInputLayout.error = "Data de nascimento inválida"
        }
    }

    private fun handleFullNameValidation(inputText: String) {
        val name = inputText.trim()
        val nameParts = name.split(" ")
        if (nameParts.size < 2) {
            textInputLayout.error = "Nome incompleto"
        } else if (nameParts.any { !it.matches(Regex("[a-zA-ZÀ-ÖØ-öø-ÿ']{3,}")) }) {
            textInputLayout.error = "Nome inválido"
        } else {
            textInputLayout.error = null
        }
    }

    private fun handlePasswordValidation(inputText: String) {
        if (inputText.length < 6) {
            textInputLayout.error = "A senha deve ter pelo menos 6 caracteres"
        } else {
            textInputLayout.error = null
        }
    }

    private fun isCPF(document: String): Boolean {
        if (document.isEmpty()) return false

        val numbers = document.filter { it.isDigit() }.map {
            it.toString().toInt()
        }

        if (numbers.size != 11) return false
        //repeticao
        if (numbers.all { it == numbers[0] }) return false
        //digito 1
        val dv1 = ((0..8).sumOf { (it + 1) * numbers[it] }).rem(11).let {
            if (it >= 10) 0 else it
        }
        val dv2 = ((0..8).sumOf { it * numbers[it] }.let { (it + (dv1 * 9)).rem(11) }).let {
            if (it >= 10) 0 else it
        }
        return numbers[9] == dv1 && numbers[10] == dv2
    }
}




