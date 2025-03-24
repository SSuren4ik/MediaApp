package com.mediaapp.registration.presentation

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mediaapp.core.utils.ResourceProvider
import com.mediaapp.registration.R
import com.mediaapp.registration.databinding.FragmentLoginBinding
import com.mediaapp.registration.di.RegistrationDepsProvider
import com.mediaapp.registration.domain.models.Email
import com.mediaapp.registration.domain.models.Password
import com.mediaapp.registration.domain.models.ResponseStatusModel
import com.mediaapp.registration.presentation.viewmodel.LoginViewModel
import com.mediaapp.registration.presentation.viewmodel.LoginViewModelFactory
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(requireActivity().application as ResourceProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDI()
        setUi()
        changeFragment()

        binding.loginBtn.setOnClickListener {
            binding.loginBtn.isEnabled = false
                viewModel.loginUser(Email("Test123@mail.ru"), Password("Test123"))
//            viewModel.loginUser(getEmail(), getPassword())
        }

        lifecycleScope.launch {
            observeLoginState()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email", binding.emailLinear.getText())
        outState.putString("password", binding.passwordLinear.getText())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            binding.emailLinear.setText(it.getString("email", ""))
            binding.passwordLinear.setText(it.getString("password", ""))
        }
    }

    private fun initDI() {
        val component =
            (requireActivity().application as RegistrationDepsProvider).getRegistrationComponent()
        component.inject(viewModel)
    }

    private fun changeFragment() {
        binding.goToRegisterActivtityTv.setOnClickListener {
            val signUpFragment =
                parentFragmentManager.findFragmentByTag(SignUpFragment::class.java.simpleName)
            if (signUpFragment == null) {
                parentFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    SignUpFragment.newInstance(),
                    SignUpFragment::class.java.simpleName
                ).addToBackStack(SignUpFragment::class.java.simpleName).commit()
            } else {
                parentFragmentManager.popBackStack(SignUpFragment::class.java.simpleName, 0)
            }
        }
    }

    private suspend fun observeLoginState() {
        viewModel.responseStatus.collect { result ->
            when (result) {
                is ResponseStatusModel.Error -> showToast(result.message)
                ResponseStatusModel.Success -> (activity as RegistrationActivity).onRegistrationComplete()
            }
            binding.loginBtn.isEnabled = true
        }
    }

    private fun setUi() {
        setEmailEditTextSettings()
        setPasswordEditTextSettings()
        setPaddingLoginLinear()
        setPaddingImage()
    }

    private fun setPaddingLoginLinear() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.loginLinear) { view, insets ->
            val innerPadding = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(0, 0, 0, innerPadding.bottom)
            insets
        }
    }

    private fun setPaddingImage() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.loginImage) { view, insets ->
            val innerPadding = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(0, innerPadding.top, 0, 0)
            insets
        }
    }

    private fun setPasswordEditTextSettings() {
        with(binding.passwordLinear) {
            setImageResource(R.drawable.password_icon)
            setHint(resources.getString(R.string.password_edit_text_hint))
            setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
        }
    }

    private fun setEmailEditTextSettings() {
        with(binding.emailLinear) {
            setImageResource(R.drawable.email_icon)
            setHint(resources.getString(R.string.email_edit_text_hint))
            setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        }
    }

    private fun getEmail(): Email = Email(binding.emailLinear.getText())
    private fun getPassword(): Password = Password(binding.passwordLinear.getText())

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
