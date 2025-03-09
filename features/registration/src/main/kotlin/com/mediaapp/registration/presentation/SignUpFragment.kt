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
import com.mediaapp.registration.databinding.FragmentSignUpBinding
import com.mediaapp.registration.di.RegistrationDepsProvider
import com.mediaapp.registration.domain.models.Email
import com.mediaapp.registration.domain.models.Password
import com.mediaapp.registration.domain.models.ResponseStatusModel
import com.mediaapp.registration.domain.models.UserName
import com.mediaapp.registration.presentation.viewmodel.SignUpViewModel
import com.mediaapp.registration.presentation.viewmodel.SignUpViewModelFactory
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModelFactory(requireActivity().application as ResourceProvider)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDI()
        setUi()
        changeFragment()

        binding.signUpBtn.setOnClickListener {
            binding.signUpBtn.isEnabled = false
            viewModel.registerUser(getEmail(), getUsername(), getPassword())
        }

        lifecycleScope.launch {
            observeSignUpState()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email", binding.emailLinear.getText())
        outState.putString("password", binding.passwordLinear.getText())
        outState.putString("username", binding.usernameLinear.getText())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            binding.emailLinear.setText(it.getString("email", ""))
            binding.passwordLinear.setText(it.getString("password", ""))
            binding.usernameLinear.setText(it.getString("username", ""))
        }
    }

    private fun initDI() {
        val component =
            (requireActivity().application as RegistrationDepsProvider).getRegistrationComponent()
        component.inject(viewModel)
    }

    private fun changeFragment() {
        binding.goToLoginActivtityTv.setOnClickListener {
            val loginFragment =
                parentFragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)
            if (loginFragment == null) {
                parentFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    LoginFragment.newInstance(),
                    LoginFragment::class.java.simpleName
                ).addToBackStack(LoginFragment::class.java.simpleName).commit()
            } else {
                parentFragmentManager.popBackStack(LoginFragment::class.java.simpleName, 0)
            }
        }
    }

    private suspend fun observeSignUpState() {
        viewModel.responseStatus.collect { result ->
            when (result) {
                is ResponseStatusModel.Error -> showToast(result.message)
                ResponseStatusModel.Success -> (activity as RegistrationActivity).onRegistrationComplete()
            }
            binding.signUpBtn.isEnabled = true
        }
    }

    private fun setUi() {
        setEmailEditTextSettings()
        setUserNameEditTextSettings()
        setPasswordEditTextSettings()
        setPaddingRegisterLinear()
        setPaddingImage()
    }

    private fun setPaddingRegisterLinear() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.registrationLinear) { view, insets ->
            val innerPadding = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(0, 0, 0, innerPadding.bottom)
            insets
        }
    }

    private fun setPaddingImage() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.registrationImage) { view, insets ->
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

    private fun setUserNameEditTextSettings() {
        with(binding.usernameLinear) {
            setImageResource(R.drawable.username_icon)
            setHint(resources.getString(R.string.username_edit_text_hint))
            setInputType(InputType.TYPE_CLASS_TEXT)
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
    private fun getUsername(): UserName = UserName(binding.usernameLinear.getText())

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}