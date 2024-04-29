package com.montapp.montapp.view.ui.recover_password

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.fragment.BaseFragment
import com.jlndev.coreandroid.customview.DialogCustomView
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.hideKeyboard
import com.jlndev.coreandroid.ext.observerErrorListener
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.databinding.FragmentRecoverPasswordBinding
import com.jlndev.facaseupedido.ui.recover_password.RecoverPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecoverPasswordFragment : BaseFragment<FragmentRecoverPasswordBinding, RecoverPasswordViewModel>() {

    override val viewModel : RecoverPasswordViewModel by viewModel()

    override fun onInitData() {
        // nothing
    }

    override fun onGetViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRecoverPasswordBinding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)

    override fun onInitViews() {
        with(binding) {
            recoverPasswordInputView.setText(arguments?.getString(EXTRA_USER_EMAIL, ""))

            sendEmailButtonView.setOnClickListener {
                it.hideKeyboard()
                sendRecoverCodeEmail()
            }

            recoverPasswordInputView.observerErrorListener(recoverPasswordInputLayoutView, "Por favor, insira seu e-mail") {
                isValidInput()
            }
        }
    }

    override fun onInitViewModel() {
        viewModel.recoverPasswordLive.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ResponseState.Loading -> {
                    if (viewState.isLoading) {
                        binding.loadingView.visible()
                    } else {
                        binding.loadingView.gone()
                    }
                }

                is ResponseState.Success -> {
                    showAlert()
                }

                is ResponseState.Error -> {
                    showError(viewState.throwable.message!!, view = binding.loadingView)
                }
            }
        }
    }

    companion object {
        const val EXTRA_USER_EMAIL = "USER_EMAIL"
    }

    private fun showAlert() {
        DialogCustomView.Builder(mContext)
            .setCanceledOnTouchOutside()
            .setTitle("Verifique seu e-mail!")
            .setSubtitle("Enviamos um e-mail para que você redefina sua senha!")
            .setPositiveButton("Recebido") {
                findNavController().popBackStack()
            }
            .setNegativeButton("Reenviar") {
                sendRecoverCodeEmail()
            }
            .build()
            .show()
    }

    private fun sendRecoverCodeEmail() {
        viewModel.sendRecoveryCodeForEmail(binding.recoverPasswordInputView.text.toString())
    }

    private fun isValidInput() {
        val emailValid = binding.recoverPasswordInputLayoutView.error == null && binding.recoverPasswordInputView.text!!.isNotEmpty()

        binding.sendEmailButtonView.isEnabled = emailValid
    }

}