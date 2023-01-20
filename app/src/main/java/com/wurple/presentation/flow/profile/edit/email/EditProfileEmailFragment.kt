package com.wurple.presentation.flow.profile.edit.email

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileEmailBinding
import com.wurple.domain.extension.empty
import com.wurple.domain.validation.size.MaxLength
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EditProfileEmailFragment : BaseViewModelFragment<EditProfileEmailViewModel>(
    layoutResId = R.layout.fragment_edit_profile_email
) {
    override val viewModel: EditProfileEmailViewModel by baseViewModel()
    override val binding: FragmentEditProfileEmailBinding by viewBinding()

    override fun onProgressVisible() {
        binding.emailEditText.isEnabled = false
        binding.saveButton.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.emailEditText.isEnabled = true
        binding.saveButton.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.emailLiveData.observe(::showEmail)
        viewModel.isNewEmailInputLiveData.observe(::setupSaveButtonVisibility)
        viewModel.emailErrorMessageLiveData.observe(::setupEmailError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupEmailEditText()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
    }

    private fun setupEmailEditText() {
        binding.emailEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.EMAIL))
        binding.emailEditText.onTextChanged()
            .onEach { viewModel.checkIsNewEmailInput(newEmail = getEmailInput()) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        binding.emailEditText.onActionDone {
            navigateUpdateEmail()
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener { view ->
            view.hideKeyboard()
            navigateUpdateEmail()
        }
    }

    private fun showEmail(email: String) {
        binding.emailEditText.setText(email)
    }

    private fun setupSaveButtonVisibility(isVisible: Boolean) {
        binding.saveButton.isVisible = isVisible
    }

    private fun setupEmailError(error: String?) {
        binding.emailTextInputLayout.error = error
    }

    private fun navigateUpdateEmail() {
        viewModel.updateEmail(newEmail = getEmailInput())
    }

    private fun getEmailInput(): String {
        return binding.emailEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    companion object {
        fun newInstance(): EditProfileEmailFragment = EditProfileEmailFragment()
    }
}