package com.wurple.presentation.flow.profile.edit.bio

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileBioBinding
import com.wurple.domain.extension.empty
import com.wurple.domain.validation.size.MaxLength
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EditProfileBioFragment : BaseViewModelFragment<EditProfileBioViewModel>(
    layoutResId = R.layout.fragment_edit_profile_bio
) {
    override val viewModel: EditProfileBioViewModel by baseViewModel()
    override val binding: FragmentEditProfileBioBinding by viewBinding()

    override fun onProgressVisible() {
        binding.bioEditText.isEnabled = false
        binding.saveButton.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.bioEditText.isEnabled = true
        binding.saveButton.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.bioLiveData.observe(::showBio)
        viewModel.isNewBioInputLiveData.observe(::setupSaveButtonVisibility)
        viewModel.bioErrorMessageLiveData.observe(::setupBioError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupBioEditText()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
    }

    private fun setupBioEditText() {
        binding.bioTextInputLayout.counterMaxLength = MaxLength.USER_BIO
        binding.bioEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.USER_BIO))
        binding.bioEditText.onTextChanged()
            .onEach { viewModel.checkIsNewBioInput(newBio = getBioInput()) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener { view ->
            view.hideKeyboard()
            updateBio()
        }
    }

    private fun showBio(bio: String) {
        binding.bioEditText.setText(bio)
    }

    private fun setupSaveButtonVisibility(isVisible: Boolean) {
        binding.saveButton.isVisible = isVisible
    }

    private fun setupBioError(error: String?) {
        binding.bioTextInputLayout.error = error
    }

    private fun updateBio() {
        viewModel.updateBio(newBio = getBioInput())
    }

    private fun getBioInput(): String {
        return binding.bioEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    companion object {
        fun newInstance(): EditProfileBioFragment = EditProfileBioFragment()
    }
}