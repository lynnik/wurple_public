package com.wurple.presentation.flow.profile.edit.username

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileUsernameBinding
import com.wurple.domain.extension.empty
import com.wurple.domain.validation.size.MaxLength
import com.wurple.domain.validation.size.MinLength
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

class EditProfileUsernameFragment : BaseViewModelFragment<EditProfileUsernameViewModel>(
    layoutResId = R.layout.fragment_edit_profile_username
) {
    override val viewModel: EditProfileUsernameViewModel by baseViewModel()
    override val binding: FragmentEditProfileUsernameBinding by viewBinding()

    override fun onProgressVisible() {
        binding.usernameEditText.isEnabled = false
        binding.saveButton.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.usernameEditText.isEnabled = true
        binding.saveButton.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.usernameLiveData.observe(::showUsername)
        viewModel.isNewUsernameInputLiveData.observe(::setupSaveButtonVisibility)
        viewModel.usernameErrorMessageLiveData.observe(::setupUsernameError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupUsernameEditText()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
    }

    private fun setupUsernameEditText() {
        binding.usernameTextInputLayout.helperText = getString(
            R.string.edit_profile_username_helper,
            MinLength.USER_USERNAME
        )
        binding.usernameTextInputLayout.counterMaxLength = MaxLength.USER_USERNAME
        binding.usernameEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.USER_USERNAME))
        binding.usernameEditText.onTextChanged()
            .onEach { viewModel.checkIsNewUsernameInput(newUsername = getUsernameInput()) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        binding.usernameEditText.onActionDone {
            updateUsername()
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener { view ->
            view.hideKeyboard()
            updateUsername()
        }
    }

    private fun showUsername(username: String) {
        binding.usernameEditText.setText(username)
    }

    private fun setupSaveButtonVisibility(isVisible: Boolean) {
        binding.saveButton.isVisible = isVisible
    }

    private fun setupUsernameError(error: String?) {
        binding.usernameTextInputLayout.error = error
    }

    private fun updateUsername() {
        viewModel.updateUsername(newUsername = getUsernameInput())
    }

    private fun getUsernameInput(): String {
        return binding.usernameEditText.text
            ?.toString()
            ?.trim()
            ?.lowercase(Locale.getDefault())
            ?: String.empty
    }

    companion object {
        fun newInstance(): EditProfileUsernameFragment = EditProfileUsernameFragment()
    }
}