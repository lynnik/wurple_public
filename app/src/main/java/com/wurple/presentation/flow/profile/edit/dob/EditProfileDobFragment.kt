package com.wurple.presentation.flow.profile.edit.dob

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileDobBinding
import com.wurple.domain.extension.empty
import com.wurple.domain.validation.size.MaxLength
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.onActionDone
import com.wurple.presentation.extension.onBaseDateTextChanged
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EditProfileDobFragment : BaseViewModelFragment<EditProfileDobViewModel>(
    layoutResId = R.layout.fragment_edit_profile_dob
) {
    override val viewModel: EditProfileDobViewModel by baseViewModel()
    override val binding: FragmentEditProfileDobBinding by viewBinding()

    override fun onProgressVisible() {
        binding.dobEditText.isEnabled = false
        binding.saveButton.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.dobEditText.isEnabled = true
        binding.saveButton.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.dobLiveData.observe(::showDob)
        viewModel.isNewDobInputLiveData.observe(::setupSaveButtonVisibility)
        viewModel.dobErrorMessageLiveData.observe(::setupDobError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupDobEditText()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
    }

    private fun setupDobEditText() {
        binding.dobEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.DEFAULT_DATE))
        binding.dobEditText.onBaseDateTextChanged()
            .onEach { viewModel.checkIsNewDobInput(newDob = getDobInput()) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        binding.dobEditText.onActionDone {
            updateDob()
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener { view ->
            view.hideKeyboard()
            updateDob()
        }
    }

    private fun showDob(dob: String) {
        binding.dobEditText.setText(dob)
    }

    private fun setupSaveButtonVisibility(isVisible: Boolean) {
        binding.saveButton.isVisible = isVisible
    }

    private fun setupDobError(error: String?) {
        binding.dobTextInputLayout.error = error
    }

    private fun updateDob() {
        viewModel.updateDob(newDob = getDobInput())
    }

    private fun getDobInput(): String {
        return binding.dobEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    companion object {
        fun newInstance(): EditProfileDobFragment = EditProfileDobFragment()
    }
}