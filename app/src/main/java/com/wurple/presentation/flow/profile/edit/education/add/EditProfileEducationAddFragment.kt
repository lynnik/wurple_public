package com.wurple.presentation.flow.profile.edit.education.add

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileEducationAddBinding
import com.wurple.domain.date.DateManager
import com.wurple.domain.extension.empty
import com.wurple.domain.model.user.UserEducation
import com.wurple.domain.validation.size.MaxLength
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class EditProfileEducationAddFragment : BaseViewModelFragment<EditProfileEducationAddViewModel>(
    layoutResId = R.layout.fragment_edit_profile_education_add
) {
    override val viewModel: EditProfileEducationAddViewModel by baseViewModel()
    override val binding: FragmentEditProfileEducationAddBinding by viewBinding()
    private val dateManager: DateManager by inject()

    override fun onProgressVisible() {
        binding.degreeEditText.isEnabled = false
        binding.institutionEditText.isEnabled = false
        binding.dateGraduationEditText.isEnabled = false
        binding.saveButton.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.degreeEditText.isEnabled = true
        binding.institutionEditText.isEnabled = true
        binding.dateGraduationEditText.isEnabled = true
        binding.saveButton.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.titleLiveData.observe(::showTitle)
        viewModel.userEducationLiveData.observe(::showUserEducation)
        viewModel.isDeleteButtonVisibleLiveData.observe(::setupDeleteButton)
        viewModel.isNewEducationInputLiveData.observe(::setupSaveButtonVisibility)
        viewModel.degreeErrorMessageLiveData.observe(::setupDegreeError)
        viewModel.institutionErrorMessageLiveData.observe(::setupInstitutionError)
        viewModel.graduationDateErrorMessageLiveData.observe(::setupGraduationDateError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupDegreeEditText()
        setupInstitutionEditText()
        setupDateGraduationEditText()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
    }

    private fun setupDegreeEditText() {
        binding.degreeEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.USER_DEGREE))
        binding.degreeEditText.onTextChanged()
            .onEach { checkIsNewEducationInput() }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupInstitutionEditText() {
        binding.institutionEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.USER_INSTITUTION))
        binding.institutionEditText.onTextChanged()
            .onEach { checkIsNewEducationInput() }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupDateGraduationEditText() {
        binding.dateGraduationEditText.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(MaxLength.DEFAULT_DATE))
        binding.dateGraduationEditText.onBaseDateTextChanged()
            .onEach { checkIsNewEducationInput() }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        binding.dateGraduationEditText.onActionDone {
            binding.container.hideKeyboard()
            addOrUpdateUserEducation()
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener { view ->
            view.hideKeyboard()
            addOrUpdateUserEducation()
        }
    }

    private fun showTitle(title: EditProfileEducationAddViewModel.Title) {
        binding.toolbar.title = getString(
            when (title) {
                EditProfileEducationAddViewModel.Title.Add -> R.string.education_add_title
                EditProfileEducationAddViewModel.Title.Edit -> R.string.education_add_edit_title
            }
        )
    }

    private fun showUserEducation(userEducation: UserEducation) {
        binding.degreeEditText.setText(userEducation.degree)
        binding.institutionEditText.setText(userEducation.institution)
        binding.dateGraduationEditText.setText(
            dateManager.baseDateToFormattedText(userEducation.graduationDate.baseDate)
        )
    }

    private fun setupDeleteButton(isVisible: Boolean) {
        if (isVisible) {
            binding.toolbar.inflateMenu(R.menu.menu_edit_profile_education_add)
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.itemDelete -> {
                        showDeleteEducationDialog()
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

    private fun setupDegreeError(error: String?) {
        binding.degreeTextInputLayout.error = error
    }

    private fun setupInstitutionError(error: String?) {
        binding.institutionTextInputLayout.error = error
    }

    private fun setupGraduationDateError(error: String?) {
        binding.dateGraduationTextInputLayout.error = error
    }

    private fun showDeleteEducationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.education_delete)
            .setMessage(R.string.education_delete_are_you_sure)
            .setNegativeButton(R.string.common_decline) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
                viewModel.deleteUserEducation()
                dialog.dismiss()
            }
            .show()
    }

    private fun checkIsNewEducationInput() {
        viewModel.checkIsNewEducationInput(
            newDegree = getDegree(),
            newInstitution = getInstitution(),
            newGraduationDate = getGraduationDate()
        )
    }

    private fun addOrUpdateUserEducation() {
        viewModel.addOrUpdateUserEducation(
            newDegree = getDegree(),
            newInstitution = getInstitution(),
            newGraduationDate = getGraduationDate()
        )
    }

    private fun getDegree(): String {
        return binding.degreeEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    private fun getInstitution(): String {
        return binding.institutionEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    private fun getGraduationDate(): String {
        return binding.dateGraduationEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    companion object {
        const val EXTRA_EDUCATION_ID = "extraEducationId"

        fun newInstance(educationId: String?): EditProfileEducationAddFragment {
            return if (educationId == null) {
                EditProfileEducationAddFragment()
            } else {
                EditProfileEducationAddFragment().withArguments(EXTRA_EDUCATION_ID to educationId)
            }
        }
    }
}