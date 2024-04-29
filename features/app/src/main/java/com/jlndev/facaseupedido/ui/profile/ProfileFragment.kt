package com.jlndev.facaseupedido.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.fragment.BaseFragment
import com.jlndev.coreandroid.ext.showSnackbar
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.FragmentProfileBinding
import com.jlndev.facaseupedido.ui.uitls.components.ChangePasswordDialog
import com.jlndev.facaseupedido.ui.uitls.components.ChangeUsernameDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override val viewModel: ProfileViewModel by viewModel()

    override fun onInitData() {
        viewModel.getUser()
    }
    override fun onGetViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onInitViews() {
        binding.nameView.setOnClickListener {
            ChangeUsernameDialog(requireContext()).show { username ->
                viewModel.changeUsername(username)
            }
        }

        binding.changePasswordView.setOnClickListener {
            ChangePasswordDialog(requireContext()).show { oldPassword, newPassword ->
                viewModel.changePassword(newPassword, oldPassword)
            }
        }

        binding.logoutView.setOnClickListener {
            viewModel.logout()
        }
    }

    override fun onInitViewModel() {
        with(viewModel) {
            userLive.observe(viewLifecycleOwner) {
                when (it) {
                    is ResponseState.Success -> {
                        it.data?.let {
                            binding.nameView.text = "Nome: ${it.name}"
                            binding.emailView.text = "Email: ${it.email}"
                        } ?: run {
                            findNavController().navigate(R.id.action_profile_to_login)
                        }
                    }

                    is ResponseState.Error -> {
                        findNavController().navigate(R.id.action_profile_to_login)
                    }

                    else -> {}
                }
            }

            changePasswordLive.observe(viewLifecycleOwner) {
                when (it) {
                    is ResponseState.Loading -> {}
                    is ResponseState.Success -> {
                        requireView().showSnackbar(getString(R.string.change_username_success))
                    }
                    is ResponseState.Error -> {
                        requireView().showSnackbar(getString(R.string.change_username_error))
                    }
                }
            }
        }
    }
}