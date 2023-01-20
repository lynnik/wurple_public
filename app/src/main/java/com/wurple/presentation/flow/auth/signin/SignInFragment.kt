package com.wurple.presentation.flow.auth.signin

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentSignInBinding
import com.wurple.domain.extension.empty
import com.wurple.domain.validation.size.MaxLength
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment

class SignInFragment : BaseViewModelFragment<SignInViewModel>(
    layoutResId = R.layout.fragment_sign_in
) {
    override val viewModel: SignInViewModel by baseViewModel()
    override val binding: FragmentSignInBinding by viewBinding()

    override fun onProgressVisible() {
        binding.mainProgressIndicator.show()
        binding.emailEditText.isEnabled = false
        binding.actionButton.isEnabled = false
    }

    override fun onProgressGone() {
        binding.mainProgressIndicator.hide()
        binding.emailEditText.isEnabled = true
        binding.actionButton.isEnabled = true
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.isActionButtonEnabledLiveData.observe(binding.actionButton::setEnabled)
        viewModel.privacyPolicyLiveData.observe(::openPrivacyPolicy)
        viewModel.emailErrorMessageLiveData.observe(::setupEmailError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.container)
        setupToolbar()
        setupEmailEditText()
        setupCheckBox()
        setupActionButton()
    }

    private fun setupToolbar() {
        binding.toolbar.title = getString(R.string.sign_in_welcome, getString(R.string.app_name))
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

    private fun setupEmailEditText() {
        with(binding.emailEditText) {
            filters += InputFilter.LengthFilter(MaxLength.EMAIL)
            onActionDone {
                signIn()
            }
        }
    }

    private fun openPrivacyPolicy(url: String) {
        context?.openWebPage(binding.container, url)
    }

    private fun setupEmailError(error: String?) {
        binding.emailTextInputLayout.error = error
    }

    private fun setupCheckBox() {
        val textToHighlight = getString(R.string.about_app_privacy_policy)
        binding.checkBoxTextView.clickableText(
            clickablePart = textToHighlight,
            onClick = { viewModel.navigatePrivacyPolicy() }
        )
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onAgreementsChecked(isChecked)
        }
    }

    private fun setupActionButton() {
        binding.actionButton.setOnClickListener { view ->
            view.hideKeyboard()
            signIn()
        }
    }

    private fun signIn() {
        val email = binding.emailEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
        viewModel.requestSignIn(email)
    }

    companion object {
        const val EXTRA_DEEP_LINK = "extraDeepLink"

        fun newInstance(deepLink: String?): SignInFragment {
            return SignInFragment().withArguments(EXTRA_DEEP_LINK to deepLink)
        }
    }
}