package com.wurple.presentation.flow.profile.edit.skills.add

import android.os.Bundle
import android.text.*
import android.text.style.ImageSpan
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileSkillsAddBinding
import com.wurple.domain.extension.comma
import com.wurple.domain.extension.empty
import com.wurple.domain.model.user.UserSkill
import com.wurple.domain.validation.size.MaxLength
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.extension.withArguments
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment

class EditProfileSkillsAddFragment : BaseViewModelFragment<EditProfileSkillsAddViewModel>(
    layoutResId = R.layout.fragment_edit_profile_skills_add
) {
    override val viewModel: EditProfileSkillsAddViewModel by baseViewModel()
    override val binding: FragmentEditProfileSkillsAddBinding by viewBinding()
    private val addTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            /*NOOP*/
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString()

            val list = text.split(String.comma)
            var size = 0
            val resultList = list
                .filter { it.isNotEmpty() }
                .map {
                    val res = size to it
                    size += it.length + 1
                    res
                }

            val spannableString = SpannableString(text)
            resultList.forEach { (startIndex, text) ->
                val chipDrawable =
                    ChipDrawable.createFromResource(requireContext(), R.xml.chip_skill)
                chipDrawable.text = text
                chipDrawable.setBounds(
                    0,
                    0,
                    chipDrawable.intrinsicWidth,
                    chipDrawable.intrinsicHeight
                )
                val span = ImageSpan(chipDrawable)
                spannableString.setSpan(
                    span,
                    startIndex,
                    startIndex + text.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            binding.skillEditText.removeTextChangedListener(this)
            binding.skillEditText.setText(spannableString)
            binding.skillEditText.setSelection(spannableString.length)
            binding.skillEditText.addTextChangedListener(this)

            viewModel.setSkill(getSkill())
        }

        override fun afterTextChanged(s: Editable?) {
            /*NOOP*/
        }
    }
    private val editTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            /*NOOP*/
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.setSkill(getSkill())
        }

        override fun afterTextChanged(s: Editable?) {
            /*NOOP*/
        }
    }

    override fun onProgressVisible() {
        binding.skillEditText.isEnabled = false
        binding.skillTypeAutoCompleteTextView.isEnabled = false
        binding.saveButton.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.skillEditText.isEnabled = true
        binding.skillTypeAutoCompleteTextView.isEnabled = true
        binding.saveButton.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.titleLiveData.observe(::showTitle)
        viewModel.isDescVisibleLiveData.observe(::showDesc)
        viewModel.skillInputLiveData.observe(::showSkillInput)
        viewModel.userSkillTypesLiveData.observe(::showSkillTypes)
        viewModel.userSkillLiveData.observe(::showUserSkill)
        viewModel.selectedSkillTypeLiveData.observe(::selectSkillType)
        viewModel.isDeleteButtonVisibleLiveData.observe(::setupDeleteButton)
        viewModel.isNewUserSkillInputLiveData.observe(::setupSaveButtonVisibility)
        viewModel.skillErrorMessageLiveData.observe(::setupSkillError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupSaveButton()
    }

    override fun onDestroyView() {
        binding.skillEditText.removeTextChangedListener(addTextWatcher)
        binding.skillEditText.removeTextChangedListener(editTextWatcher)
        super.onDestroyView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            viewModel.addOrUpdateUserSkills(getSkill())
        }
    }

    private fun showTitle(title: EditProfileSkillsAddViewModel.Title) {
        binding.toolbar.title = getString(
            when (title) {
                EditProfileSkillsAddViewModel.Title.Add -> R.string.skills_add_title
                EditProfileSkillsAddViewModel.Title.Edit -> R.string.skills_add_edit_title
            }
        )
    }

    private fun showDesc(isVisible: Boolean) {
        binding.descriptionTextView.isVisible = isVisible
    }

    private fun showSkillInput(skillInput: EditProfileSkillsAddViewModel.SkillInput) {
        binding.skillEditText.addTextChangedListener(
            editTextWatcher
            // TODO research how to add Chips inside EditText correctly
            //  or create another implementation of adding several skills at the same time separated by commas
            //  (left a simple solution without formatted Chips (addTextWatcher))
            /*when (skillInput) {
                EditProfileSkillsAddViewModel.SkillInput.Add -> addTextWatcher
                EditProfileSkillsAddViewModel.SkillInput.Edit -> editTextWatcher
            }*/
        )
        val maxLength = when (skillInput) {
            EditProfileSkillsAddViewModel.SkillInput.Add -> MaxLength.USER_SKILLS
            EditProfileSkillsAddViewModel.SkillInput.Edit -> MaxLength.USER_SKILL
        }
        binding.skillTextInputLayout.counterMaxLength = maxLength
        binding.skillEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }

    private fun showSkillTypes(types: List<UserSkill.Type>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown,
            types.map { getSkillTypeText(it) }
        )
        binding.skillTypeAutoCompleteTextView.setAdapter(adapter)
        binding.skillTypeAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectSkillType(types[position])
        }
    }

    private fun showUserSkill(userSkill: UserSkill) {
        binding.skillEditText.setText(userSkill.title)
    }

    private fun selectSkillType(type: UserSkill.Type) {
        binding.skillTypeAutoCompleteTextView.setText(getSkillTypeText(type), false)
    }

    private fun setupDeleteButton(isVisible: Boolean) {
        if (isVisible) {
            binding.toolbar.inflateMenu(R.menu.menu_edit_profile_skills_add)
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.itemDelete -> {
                        showDeleteSkillDialog()
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

    private fun setupSkillError(error: String?) {
        binding.skillTextInputLayout.error = error
    }

    private fun showDeleteSkillDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.skills_delete)
            .setMessage(R.string.skills_delete_are_you_sure)
            .setNegativeButton(R.string.common_decline) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
                viewModel.deleteUserSkill()
                dialog.dismiss()
            }
            .show()
    }

    private fun getSkillTypeText(type: UserSkill.Type): String {
        return when (type) {
            UserSkill.Type.Personal -> getString(R.string.skills_personal)
            UserSkill.Type.Professional -> getString(R.string.skills_professional)
        }
    }

    private fun getSkill(): String {
        return binding.skillEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    companion object {
        const val EXTRA_SKILL_ID = "extraSkillId"

        fun newInstance(skillId: String?): EditProfileSkillsAddFragment {
            return if (skillId == null) {
                EditProfileSkillsAddFragment()
            } else {
                EditProfileSkillsAddFragment().withArguments(EXTRA_SKILL_ID to skillId)
            }
        }
    }
}