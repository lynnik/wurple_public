package com.wurple.presentation.flow.more.account.emailverification

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentDeleteAccountEmailVerificationBinding
import com.wurple.domain.model.user.User
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.highlightText
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment

class DeleteAccountEmailVerificationFragment :
    BaseViewModelFragment<DeleteAccountEmailVerificationViewModel>(
        layoutResId = R.layout.fragment_delete_account_email_verification
    ) {
    override val viewModel: DeleteAccountEmailVerificationViewModel by baseViewModel()
    override val binding: FragmentDeleteAccountEmailVerificationBinding by viewBinding()

    override fun onProgressVisible() {
        binding.mainProgressIndicator.show()
        binding.resendButton.isEnabled = false
    }

    override fun onProgressGone() {
        binding.mainProgressIndicator.hide()
        binding.resendButton.isEnabled = true
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.userLiveData.observe(::setupDescriptionText)
        viewModel.secondsLiveData.observe(::setupResendText)
        viewModel.resendLiveData.observe(::setupResendButtonVisibility)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.container)
        setupToolbar()
        setupResendButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { viewModel.navigateBack() }
    }

    private fun setupResendText(seconds: Int) {
        binding.resendTextView.text =
            getString(R.string.sign_in_email_verification_resend_timer, seconds)
    }

    private fun setupDescriptionText(user: User) {
        val description = getString(
            R.string.sign_in_email_verification_sent,
            user.userEmail
        )
        val descriptionSpannable = description
            .let { requireContext().highlightText(it, user.userEmail) }
        binding.descriptionTextView.text = descriptionSpannable
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

    companion object {
        fun newInstance(): DeleteAccountEmailVerificationFragment =
            DeleteAccountEmailVerificationFragment()
    }
}