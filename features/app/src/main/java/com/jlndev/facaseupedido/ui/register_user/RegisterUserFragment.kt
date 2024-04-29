package com.jlndev.facaseupedido.ui.register_user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.fragment.BaseFragment
import com.jlndev.coreandroid.customview.DialogCustomView
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.hideKeyboard
import com.jlndev.coreandroid.ext.observerErrorListener
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.FragmentRegisterUserBinding
import com.jlndev.firebaseservice.model.User
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterUserFragment : BaseFragment<FragmentRegisterUserBinding, RegisterUserViewModel>() {

    private val firebaseAuth: FirebaseAuth by inject()

    override val viewModel: RegisterUserViewModel by viewModel()

    override fun onInitData() {
        //nothing
    }

    override fun onGetViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterUserBinding = FragmentRegisterUserBinding.inflate(inflater, container, false)

    override fun onInitViews() {
        with(binding) {
            regiterButtonView.setOnClickListener {
                it.hideKeyboard()
                registerUser()
            }

            val fields = listOf(
                emailInputView,
                nameInputView,
                passwordInputView
            )

            fields.forEach { inputView ->
                inputView.observerErrorListener(inputView.parent.parent as TextInputLayout) {
                    isValidInput()
                }
            }

            loginButtonView.setOnClickListener {
                val backStack = findNavController().previousBackStackEntry
                if (backStack?.destination?.id == R.id.nav_login) {
                    findNavController().popBackStack()
                } else {
                    findNavController().navigate(R.id.action_register_user_to_login)
                }
            }
        }
    }

    override fun onInitViewModel() {
        viewModel.registerUserLive.observe(viewLifecycleOwner) { viewState ->
            when(viewState) {
                is ResponseState.Loading -> {
                   if(viewState.isLoading) {
                       binding.loadingView.visible()
                   } else {
                       binding.loadingView.gone()
                   }
                }
                is ResponseState.Success -> {
                    showAlert(viewState.data)
                }
                is ResponseState.Error -> {
                    showError(viewState.throwable.message!!, getString(com.jlndev.coreandroid.R.string.try_again)) {
                        registerUser()
                    }
                }
            }
        }
    }

    private fun registerUser() {
        viewModel.registerUser(getUser())
    }

    private fun showAlert(user: User) {
        DialogCustomView.Builder(mContext)
            .setCanceledOnTouchOutside()
            .setTitle("E-mail de verificação enviado!")
            .setSubtitle("Enviamos um e-mail para ${user.email} com um link verificação!")
            .setPositiveButton("OK") {
                findNavController().navigate(R.id.action_register_user_to_login)
                firebaseAuth.signOut()
            }
            .build()
            .show()
    }

    private fun getUser() : User {
        with(binding) {
            return User(
                name = nameInputView.text.toString(),
                email = emailInputView.text.toString(),
                password = passwordInputView.text.toString()
            )
        }
    }

    private fun isValidInput() {
        val fullNameValid = binding.nameInputLayoutView.error == null && binding.nameInputView.text!!.isNotEmpty()
        val emailValid = binding.emailInputLayoutView.error == null && binding.emailInputView.text!!.isNotEmpty()
        val passwordValid = binding.passwordInputLayoutView.error == null && binding.passwordInputView.text!!.isNotEmpty()

        binding.regiterButtonView.isEnabled = emailValid && fullNameValid && passwordValid
    }
}