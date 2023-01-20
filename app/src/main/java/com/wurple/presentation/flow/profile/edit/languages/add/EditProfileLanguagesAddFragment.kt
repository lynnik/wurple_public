package com.wurple.presentation.flow.profile.edit.languages.add

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileLanguagesAddBinding
import com.wurple.domain.model.language.Language
import com.wurple.domain.model.language.LanguageLevel
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.extension.withArguments
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment

class EditProfileLanguagesAddFragment : BaseViewModelFragment<EditProfileLanguagesAddViewModel>(
    layoutResId = R.layout.fragment_edit_profile_languages_add
) {
    override val viewModel: EditProfileLanguagesAddViewModel by baseViewModel()
    override val binding: FragmentEditProfileLanguagesAddBinding by viewBinding()

    override fun onProgressVisible() {
        binding.languageAutoCompleteTextView.isEnabled = false
        binding.languageLevelAutoCompleteTextView.isEnabled = false
        binding.saveButton.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.languageAutoCompleteTextView.isEnabled = true
        binding.languageLevelAutoCompleteTextView.isEnabled = true
        binding.saveButton.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.titleLiveData.observe(::showTitle)
        viewModel.languagesLiveData.observe(::showLanguages)
        viewModel.languageLevelsLiveData.observe(::showLanguageLevels)
        viewModel.selectedLanguageLiveData.observe(::selectLanguage)
        viewModel.selectedLanguageLevelLiveData.observe(::selectLanguageLevel)
        viewModel.isDeleteButtonVisibleLiveData.observe(::setupDeleteButton)
        viewModel.alreadyAddedLanguageErrorMessageLiveData.observe {
            showErrorMessage(getString(R.string.languages_add_languages_already_added_error))
        }
        viewModel.isNewUserLanguageInputLiveData.observe(::setupSaveButtonVisibility)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            viewModel.addUserLanguage()
        }
    }

    private fun showTitle(title: EditProfileLanguagesAddViewModel.Title) {
        binding.toolbar.title = getString(
            when (title) {
                EditProfileLanguagesAddViewModel.Title.Add -> R.string.languages_add_title
                EditProfileLanguagesAddViewModel.Title.Edit -> R.string.languages_add_edit_title
            }
        )
    }

    private fun showLanguages(languages: List<Language>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown,
            languages.map { it.name }
        )
        binding.languageAutoCompleteTextView.setAdapter(adapter)
        binding.languageAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectLanguage(languages[position])
        }
    }

    private fun showLanguageLevels(languageLevels: List<LanguageLevel>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_dropdown,
            languageLevels.map { it.name }
        )
        binding.languageLevelAutoCompleteTextView.setAdapter(adapter)
        binding.languageLevelAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectLanguageLevel(languageLevels[position])
        }
    }

    private fun selectLanguage(language: Language) {
        binding.languageAutoCompleteTextView.setText(language.name, false)
    }

    private fun selectLanguageLevel(languageLevel: LanguageLevel) {
        binding.languageLevelAutoCompleteTextView.setText(languageLevel.name, false)
    }

    private fun setupDeleteButton(isVisible: Boolean) {
        if (isVisible) {
            binding.toolbar.inflateMenu(R.menu.menu_edit_profile_language_add)
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.itemDelete -> {
                        showDeleteLanguageDialog()
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

    private fun showDeleteLanguageDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.languages_delete)
            .setMessage(R.string.languages_delete_are_you_sure)
            .setNegativeButton(R.string.common_decline) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
                viewModel.deleteUserLanguage()
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        const val EXTRA_LANGUAGE_ID = "extraLanguageId"
        const val EXTRA_LANGUAGE_LEVEL_ID = "extraLanguageLevelId"

        fun newInstance(languageId: Int?, languageLevelId: Int?): EditProfileLanguagesAddFragment {
            return if (languageId == null && languageLevelId == null) {
                EditProfileLanguagesAddFragment()
            } else {
                EditProfileLanguagesAddFragment().withArguments(
                    EXTRA_LANGUAGE_ID to languageId,
                    EXTRA_LANGUAGE_LEVEL_ID to languageLevelId
                )
            }
        }
    }
}