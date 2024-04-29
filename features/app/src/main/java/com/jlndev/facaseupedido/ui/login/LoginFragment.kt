package com.jlndev.facaseupedido.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.fragment.BaseFragment
import com.jlndev.coreandroid.customview.DialogCustomView
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.hideKeyboard
import com.jlndev.coreandroid.ext.observerErrorListener
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.FragmentLoginBinding
import com.jlndev.firebaseservice.model.LoginError
import com.jlndev.firebaseservice.model.User
import com.montapp.montapp.view.ui.recover_password.RecoverPasswordFragment.Companion.EXTRA_USER_EMAIL
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    private val firebaseAuth: FirebaseAuth by inject()

    override val viewModel: LoginViewModel by viewModel()
    override fun onInitData() {

    }

    override fun onGetViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onInitViews() {
        with(binding) {
            emailInputView.observerErrorListener(emailInputLayoutView) {
                isValidInput()
            }

            passwordInputView.observerErrorListener(passwordInputLayoutView) {
                isValidInput()
            }

            loginButtonView.setOnClickListener {
                it.hideKeyboard()
                requestLogin()
            }

            forgotPasswordView.setOnClickListener {
                it.hideKeyboard()
                findNavController().navigate(R.id.action_nav_login_to_nav_recover_password, Bundle().apply {
                    putString(EXTRA_USER_EMAIL, binding.emailInputView.text.toString())
                })
            }

            registerButtonView.setOnClickListener {
                it.hideKeyboard()
                findNavController().navigate(R.id.action_login_to_register_user)
            }
        }
    }

    override fun onInitViewModel() {
        viewModel.loginLive.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Loading -> {
                    if(it.isLoading) {
                        showLoading()
                        binding.loginButtonView.isEnabled = false
                    } else {
                        hideLoading()
                    }
                }
                is ResponseState.Success<User> -> {
                    hideLoading()
                    showSuccess()
                }
                is ResponseState.Error -> {
                    hideLoading()
                    binding.loginButtonView.isEnabled = true
                    val message = it.throwable.message ?: getString(com.jlndev.coreandroid.R.string.unknown_error)
                    val actionText = if (it.throwable == LoginError.NetworkError) getString(com.jlndev.coreandroid.R.string.try_again) else null
                    showError(message, actionText) {
                        requestLogin()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.loadingView.visible()
    }

    private fun hideLoading() {
        binding.loadingView.gone()
    }

    private fun showSuccess() {
    firebaseAuth.currentUser?.let {
            findNavController().navigate(R.id.action_login_to_home)
        }
    }

    private fun requestLogin() {
        viewModel.login(binding.emailInputView.text.toString(), binding.passwordInputView.text.toString())
    }

    private fun isValidInput() {
        val emailValid = binding.emailInputLayoutView.error == null && binding.emailInputView.text!!.isNotEmpty()
        val passwordValid = binding.passwordInputLayoutView.error == null && binding.passwordInputView.text!!.isNotEmpty()

        binding.loginButtonView.isEnabled = emailValid && passwordValid
    }
}