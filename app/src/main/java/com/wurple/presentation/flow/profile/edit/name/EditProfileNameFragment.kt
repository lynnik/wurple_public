package com.wurple.presentation.flow.profile.edit.name

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileNameBinding
import com.wurple.domain.extension.empty
import com.wurple.domain.model.user.UserName
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EditProfileNameFragment : BaseViewModelFragment<EditProfileNameViewModel>(
    layoutResId = R.layout.fragment_edit_profile_name
) {
    override val viewModel: EditProfileNameViewModel by baseViewModel()
    override val binding: FragmentEditProfileNameBinding by viewBinding()

    override fun onProgressVisible() {
        binding.firstNameEditText.isEnabled = false
        binding.lastNameEditText.isEnabled = false
        binding.saveButton.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.firstNameEditText.isEnabled = true
        binding.lastNameEditText.isEnabled = true
        binding.saveButton.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.userNameLiveData.observe(::showUserName)
        viewModel.isNewNameInputLiveData.observe(::setupSaveButtonVisibility)
        viewModel.firstNameErrorMessageLiveData.observe(::setupFirstNameError)
        viewModel.lastNameErrorMessageLiveData.observe(::setupLastNameError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupFirstNameEditText()
        setupLastNameEditText()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
    }

    private fun setupFirstNameEditText() {
        binding.firstNameEditText.onTextChanged()
            .onEach {
                viewModel.checkIsNewNameInput(
                    newFirstName = getFirstNameInput(),
                    newLastName = getLastNameInput()
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupLastNameEditText() {
        binding.lastNameEditText.onTextChanged()
            .onEach {
                viewModel.checkIsNewNameInput(
                    newFirstName = getFirstNameInput(),
                    newLastName = getLastNameInput()
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        binding.lastNameEditText.onActionDone {
            updateName()
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener { view ->
            view.hideKeyboard()
            updateName()
        }
    }

    private fun showUserName(userName: UserName) {
        binding.firstNameEditText.setText(userName.firstName)
        binding.lastNameEditText.setText(userName.lastName)
    }

    private fun setupSaveButtonVisibility(isVisible: Boolean) {
        binding.saveButton.isVisible = isVisible
    }

    private fun setupFirstNameError(error: String?) {
        binding.firstNameTextInputLayout.error = error
    }

    private fun setupLastNameError(error: String?) {
        binding.lastNameTextInputLayout.error = error
    }

    private fun updateName() {
        viewModel.updateName(
            newFirstName = getFirstNameInput(),
            newLastName = getLastNameInput()
        )
    }

    private fun getFirstNameInput(): String {
        return binding.firstNameEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    private fun getLastNameInput(): String {
        return binding.lastNameEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    companion object {
        fun newInstance(): EditProfileNameFragment = EditProfileNameFragment()
    }
}