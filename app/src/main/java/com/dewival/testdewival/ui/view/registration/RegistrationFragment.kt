package com.dewival.testdewival.ui.view.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.dewival.testdewival.R
import com.dewival.testdewival.databinding.RegistrationFragmentBinding
import com.dewival.testdewival.ui.models.UserUi
import com.dewival.testdewival.ui.view.BaseFragment

import com.dewival.testdewival.ui.viewmodel.*
import com.dewival.testdewival.ui.viewmodel.RegistrationViewModel
import com.dewival.testdewival.ui.viewmodel.RegistrationViewModelImpl
import com.dewival.testdewival.utils.viewModels


class RegistrationFragment : BaseFragment() {

    private var _binding: RegistrationFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val regViewModel: RegistrationViewModel by viewModels { RegistrationViewModelImpl() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onLogInClick()
        regViewModel.result.observe(viewLifecycleOwner,::handleRegistrationResult)
    }

    private fun onLogInClick() {
        binding.logIn.setOnClickListener {
            val login = binding.loginEt.text.toString()
            val password = binding.passwordEt.text.toString()
            regViewModel.logInUI(UserUi(login,password))
        }
    }

    private fun handleRegistrationResult(result: RegistrationResult) {
        when (result) {
            is ValidResult -> {
                val directions = RegistrationFragmentDirections.actionRegistrationFragmentToSenderFragment(result.tokenModel.token)
                findNavController().navigate(directions)
            }
            is ErrorResult -> result.e.message?.let { showError(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}