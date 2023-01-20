package com.wurple.presentation.flow.auth.signinemailverification

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentSignInEmailVerificationBinding
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.highlightText
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.extension.withArguments
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment

class SignInEmailVerificationFragment : BaseViewModelFragment<SignInEmailVerificationViewModel>(
    layoutResId = R.layout.fragment_sign_in_email_verification
) {
    override val viewModel: SignInEmailVerificationViewModel by baseViewModel()
    override val binding: FragmentSignInEmailVerificationBinding by viewBinding()

    override fun onProgressVisible() {
        binding.mainProgressIndicator.show()
        binding.resendButton.isEnabled = false
        binding.changeEmailButton.isEnabled = false
    }

    override fun onProgressGone() {
        binding.mainProgressIndicator.hide()
        binding.resendButton.isEnabled = true
        binding.changeEmailButton.isEnabled = true
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.secondsLiveData.observe(::setupResendText)
        viewModel.resendLiveData.observe(::setupResendButtonVisibility)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.container)
        setupToolbar()
        setupDescriptionText()
        setupResendButton()
        setupChangeEmailButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { viewModel.onChangeEmail() }
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemSupport -> {
                    viewModel.navigateSupport()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupDescriptionText() {
        val description = getString(
            R.string.sign_in_email_verification_sent,
            viewModel.email
        )
        val descriptionSpannable = description
            .let { requireContext().highlightText(it, viewModel.email) }
        binding.descriptionTextView.text = descriptionSpannable
    }

    private fun setupResendText(seconds: Int) {
        binding.resendTextView.text =
            getString(R.string.sign_in_email_verification_resend_timer, seconds)
    }

    private fun setupResendButtonVisibility(isVisible: Boolean) {
        binding.resendButton.isVisible = isVisible
        binding.resendTextView.isVisible = isVisible.not()
    }

    private fun setupResendButton() {
        binding.resendButton.setOnClickListener { view ->
            view.hideKeyboard()
            viewModel.requestSignIn()
        }
    }

    private fun setupChangeEmailButton() {
        binding.changeEmailButton.setOnClickListener {
            viewModel.onChangeEmail()
        }
    }

    companion object {
        const val EXTRA_EMAIL = "extraEmail"

        fun newInstance(email: String): SignInEmailVerificationFragment =
            SignInEmailVerificationFragment().withArguments(EXTRA_EMAIL to email)
    }
}