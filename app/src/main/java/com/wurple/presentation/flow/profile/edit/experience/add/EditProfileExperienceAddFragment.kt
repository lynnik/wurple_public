package com.wurple.presentation.flow.profile.edit.experience.add

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileExperienceAddBinding
import com.wurple.domain.date.DateManager
import com.wurple.domain.extension.empty
import com.wurple.domain.model.user.UserExperience
import com.wurple.domain.validation.size.MaxLength
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class EditProfileExperienceAddFragment : BaseViewModelFragment<EditProfileExperienceAddViewModel>(
    layoutResId = R.layout.fragment_edit_profile_experience_add
) {
    override val viewModel: EditProfileExperienceAddViewModel by baseViewModel()
    override val binding: FragmentEditProfileExperienceAddBinding by viewBinding()
    private val dateManager: DateManager by inject()

    override fun onProgressVisible() {
        binding.positionEditText.isEnabled = false
        binding.companyNameEditText.isEnabled = false
        binding.dateFromEditText.isEnabled = false
        binding.dateToEditText.isEnabled = false
        binding.isCurrentSwitch.isEnabled = false
        binding.saveButton.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.positionEditText.isEnabled = true
        binding.companyNameEditText.isEnabled = true
        binding.dateFromEditText.isEnabled = true
        binding.dateToEditText.isEnabled = true
        binding.isCurrentSwitch.isEnabled = true
        binding.saveButton.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.titleLiveData.observe(::showTitle)
        viewModel.userExperienceLiveData.observe(::showUserExperience)
        viewModel.isDeleteButtonVisibleLiveData.observe(::setupDeleteButton)
        viewModel.isNewExperienceInputLiveData.observe(::setupSaveButtonVisibility)
        viewModel.positionErrorMessageLiveData.observe(::setupPositionError)
        viewModel.companyNameErrorMessageLiveData.observe(::setupCompanyNameError)
        viewModel.fromDateErrorMessageLiveData.observe(::setupFromDateError)
        viewModel.toDateErrorMessageLiveData.observe(::setupToDateError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupPositionEditText()
        setupCompanyNameEditText()
        setupDateFromEditText()
        setupDateToEditText()
        setupIsCurrentSwitch()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
    }

    private fun setupPositionEditText() {
        binding.positionEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.USER_POSITION))
        binding.positionEditText.onTextChanged()
            .onEach { checkIsNewExperienceInput() }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupCompanyNameEditText() {
        binding.companyNameEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.COMPANY_NAME))
        binding.companyNameEditText.onTextChanged()
            .onEach { checkIsNewExperienceInput() }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupDateFromEditText() {
        binding.dateFromEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.DEFAULT_DATE))
        binding.dateFromEditText.onBaseDateTextChanged()
            .onEach { checkIsNewExperienceInput() }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        binding.dateFromEditText.onActionDone {
            binding.container.hideKeyboard()
            addOrUpdateUserExperience()
        }
    }

    private fun setupDateToEditText() {
        binding.dateToEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.DEFAULT_DATE))
        binding.dateToEditText.onBaseDateTextChanged()
            .onEach { checkIsNewExperienceInput() }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        binding.dateToEditText.onActionDone {
            binding.container.hideKeyboard()
            addOrUpdateUserExperience()
        }
    }

    private fun setupIsCurrentSwitch() {
        binding.isCurrentSwitch.setOnCheckedChangeListener { _, isChecked ->
            checkIsNewExperienceInput()
            binding.dateToTextInputLayout.isVisible = isChecked.not()
            binding.dateToEditText.setText(String.empty)
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener { view ->
            view.hideKeyboard()
            addOrUpdateUserExperience()
        }
    }

    private fun showTitle(title: EditProfileExperienceAddViewModel.Title) {
        binding.toolbar.title = getString(
            when (title) {
                EditProfileExperienceAddViewModel.Title.Add -> R.string.experience_add_title
                EditProfileExperienceAddViewModel.Title.Edit -> R.string.experience_add_edit_title
            }
        )
    }

    private fun showUserExperience(userExperience: UserExperience) {
        binding.positionEditText.setText(userExperience.position)
        binding.companyNameEditText.setText(userExperience.companyName)
        binding.dateFromEditText.setText(
            dateManager.baseDateToFormattedText(userExperience.fromDate.baseDate)
        )
        binding.dateToEditText.setText(
            dateManager.baseDateToFormattedText(userExperience.toDate.baseDate)
        )
        binding.isCurrentSwitch.isChecked = userExperience.isCurrent
    }

    private fun setupDeleteButton(isVisible: Boolean) {
        if (isVisible) {
            binding.toolbar.inflateMenu(R.menu.menu_edit_profile_experience_add)
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.itemDelete -> {
                        showDeleteExperienceDialog()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupSaveButtonVisibility(isVisible: Boolean) {
        binding.saveButton.isVisible = isVisible
    }

    private fun setupPositionError(error: String?) {
        binding.positionTextInputLayout.error = error
    }

    private fun setupCompanyNameError(error: String?) {
        binding.companyNameTextInputLayout.error = error
    }

    private fun setupFromDateError(error: String?) {
        binding.dateFromTextInputLayout.error = error
    }

    private fun setupToDateError(error: String?) {
        binding.dateToTextInputLayout.error = error
    }

    private fun showDeleteExperienceDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.experience_delete)
            .setMessage(R.string.experience_delete_are_you_sure)
            .setNegativeButton(R.string.common_decline) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
                viewModel.deleteUserExperience()
                dialog.dismiss()
            }
            .show()
    }

    private fun checkIsNewExperienceInput() {
        viewModel.checkIsNewExperienceInput(
            newPosition = getPosition(),
            newCompanyName = getCompanyName(),
            newIsCurrent = getIsCurrent(),
            newFromDate = getFromDate(),
            newToDate = getToDate()
        )
    }

    private fun addOrUpdateUserExperience() {
        viewModel.addOrUpdateUserExperience(
            newPosition = getPosition(),
            newCompanyName = getCompanyName(),
            newIsCurrent = getIsCurrent(),
            newFromDate = getFromDate(),
            newToDate = getToDate()
        )
    }

    private fun getPosition(): String {
        return binding.positionEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    private fun getCompanyName(): String {
        return binding.companyNameEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    private fun getIsCurrent(): Boolean = binding.isCurrentSwitch.isChecked

    private fun getFromDate(): String {
        return binding.dateFromEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    private fun getToDate(): String {
        return binding.dateToEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    companion object {
        const val EXTRA_EXPERIENCE_ID = "extraExperienceId"

        fun newInstance(experienceId: String?): EditProfileExperienceAddFragment {
            return if (experienceId == null) {
                EditProfileExperienceAddFragment()
            } else {
                EditProfileExperienceAddFragment().withArguments(EXTRA_EXPERIENCE_ID to experienceId)
            }
        }
    }
}