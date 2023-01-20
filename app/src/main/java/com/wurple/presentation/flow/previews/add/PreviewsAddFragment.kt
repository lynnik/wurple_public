package com.wurple.presentation.flow.previews.add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wurple.R
import com.wurple.databinding.FragmentPreviewsAddBinding
import com.wurple.domain.extension.empty
import com.wurple.domain.model.preview.PreviewLifetimeOption
import com.wurple.domain.model.preview.PreviewVisibilityValue
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.previews.add.dialog.PreviewOptionBottomSheetDialogFragment
import com.wurple.presentation.flow.previews.add.dialog.adapter.PreviewOptionItem
import com.wurple.presentation.model.preview.PreviewUi

class PreviewsAddFragment : BaseViewModelFragment<PreviewsAddViewModel>(
    layoutResId = R.layout.fragment_previews_add
) {
    override val viewModel: PreviewsAddViewModel by baseViewModel()
    override val binding: FragmentPreviewsAddBinding by viewBinding()
    private val titleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            /*NOOP*/
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.updatePreviewTitle(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            /*NOOP*/
        }
    }

    override fun onProgressVisible() {
        binding.mainProgressIndicator.show()
        binding.titleEditText.isEnabled = false
        binding.viewButton.isEnabled = false
    }

    override fun onProgressGone() {
        binding.mainProgressIndicator.hide()
        binding.titleEditText.isEnabled = true
        binding.viewButton.isEnabled = true
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.modeLiveData.observe(::setupMode)
        viewModel.isAddButtonEnabledLiveData.observe(::setupAddButtonState)
        viewModel.isDeleteButtonVisibleLiveData.observe(::setupDeleteButtonState)
        viewModel.previewHeaderLiveData.observe(::showPreviewHeader)
        viewModel.emptyStateLiveData.observe(::showEmptyState)
        viewModel.previewUiLiveData.observe(::showPreviewUi)
        viewModel.userImagePreviewOptionItemsLiveData.observe(::showUserImagePreviewOptionItems)
        viewModel.userNamePreviewOptionItemsLiveData.observe(::showUserNamePreviewOptionItems)
        viewModel.userPositionPreviewOptionItemsLiveData.observe(::showUserPositionPreviewOptionItems)
        viewModel.userLocationPreviewOptionItemsLiveData.observe(::showUserLocationPreviewOptionItems)
        viewModel.userEmailPreviewOptionItemsLiveData.observe(::showUserEmailPreviewOptionItems)
        viewModel.userUsernamePreviewOptionItemsLiveData.observe(::showUserUsernamePreviewOptionItems)
        viewModel.userBioPreviewOptionItemsLiveData.observe(::showUserBioPreviewOptionItems)
        viewModel.userSkillsPreviewOptionItemsLiveData.observe(::showUserSkillsPreviewOptionItems)
        viewModel.userExperiencePreviewOptionItemsLiveData.observe(::showUserExperiencePreviewOptionItems)
        viewModel.userEducationPreviewOptionItemsLiveData.observe(::showUserEducationPreviewOptionItems)
        viewModel.userLanguagesPreviewOptionItemsLiveData.observe(::showUserLanguagesPreviewOptionItems)
        viewModel.previewTitleErrorMessageLiveData.observe(::setupPreviewTitleError)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setBottomSystemBarInset(binding.constraintLayout)
        setupToolbar()
        setupPreviewView()
        setupViewButton()
        setupAddButton()
    }

    override fun onDestroyView() {
        binding.titleEditText.removeTextChangedListener(titleTextWatcher)
        super.onDestroyView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
        binding.toolbar.inflateMenu(R.menu.menu_previews_add)
        binding.toolbar.menu.findItem(R.id.itemDelete).isVisible = false
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemDelete -> {
                    showDeletePreviewDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViewButton() {
        binding.viewButton.setOnClickListener { viewModel.navigateTempPreview() }
    }

    private fun setupAddButton() {
        binding.addButton.setOnClickListener { viewModel.createPreview() }
    }

    private fun setupPreviewView() {
        binding.previewView.onUserImagePreviewVisibilityClickListener = {
            viewModel.onUserImageItemClick()
        }
        binding.previewView.onUserNamePreviewVisibilityClickListener = {
            viewModel.onUserNameItemClick()
        }
        binding.previewView.onUserPositionPreviewVisibilityClickListener = {
            viewModel.onUserPositionItemClick()
        }

        binding.previewView.onUserLocationPreviewVisibilityClickListener = {
            viewModel.onUserLocationItemClick()
        }
        binding.previewView.onUserEmailPreviewVisibilityClickListener = {
            viewModel.onUserEmailItemClick()
        }
        binding.previewView.onUserUsernamePreviewVisibilityClickListener = {
            viewModel.onUserUsernameItemClick()
        }
        binding.previewView.onUserBioPreviewVisibilityClickListener = {
            viewModel.onUserBioItemClick()
        }

        binding.previewView.onUserSkillsPreviewVisibilityClickListener = {
            viewModel.onUserSkillsItemClick()
        }
        binding.previewView.onUserExperiencePreviewVisibilityClickListener = {
            viewModel.onUserExperienceItemClick()
        }
        binding.previewView.onUserEducationPreviewVisibilityClickListener = {
            viewModel.onUserEducationItemClick()
        }
        binding.previewView.onUserLanguagesPreviewVisibilityClickListener = {
            viewModel.onUserLanguagesItemClick()
        }
    }

    private fun setupMode(mode: PreviewsAddViewModel.Mode) {
        when (mode) {
            PreviewsAddViewModel.Mode.Add -> {
                binding.toolbar.title = getString(R.string.previews_add_title)
                binding.addButton.text = getString(R.string.common_add)
            }
            PreviewsAddViewModel.Mode.Edit -> {
                binding.toolbar.title = getString(R.string.previews_add_edit_title)
                binding.addButton.text = getString(R.string.common_edit)
            }
        }
    }

    private fun setupAddButtonState(isEnabled: Boolean) {
        binding.addButton.isEnabled = isEnabled
    }

    private fun setupDeleteButtonState(isVisible: Boolean) {
        binding.toolbar.menu.findItem(R.id.itemDelete).isVisible = isVisible
    }

    private fun showPreviewUi(previewUi: PreviewUi) {
        binding.previewView.setData(previewUi)
        binding.constraintLayout.visible()
        binding.linearLayout.visible()
        binding.emptyStateTextView.gone()
    }

    private fun onLifetimeItemClick(item: PreviewLifetimeOption) {
        viewModel.updatePreviewLifetimeOption(item)
    }

    private fun onVisibilityItemClick(item: PreviewVisibilityPattern) {
        viewModel.updatePreviewVisibilityOption(item)
    }

    private fun showUserImagePreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.previews_add_image),
            items = items,
            onItemClickListener = { viewModel.updateUserImagePreviewVisibilityValue(it) }
        )
    }

    private fun showUserNamePreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.edit_profile_full_name),
            items = items,
            onItemClickListener = { viewModel.updateUserNamePreviewVisibilityValue(it) }
        )
    }

    private fun showUserPositionPreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.previews_add_current_position),
            items = items,
            onItemClickListener = { viewModel.updateUserPositionPreviewVisibilityValue(it) }
        )
    }

    private fun showUserLocationPreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.edit_profile_location),
            items = items,
            onItemClickListener = { viewModel.updateUserLocationPreviewVisibilityValue(it) }
        )
    }

    private fun showUserEmailPreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.edit_profile_email),
            items = items,
            onItemClickListener = { viewModel.updateUserEmailPreviewVisibilityValue(it) }
        )
    }

    private fun showUserUsernamePreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.edit_profile_username),
            items = items.map { previewOptionItem ->
                if (previewOptionItem is PreviewOptionItem.Visible) {
                    previewOptionItem.copy(
                        visibleSubtext = getString(
                            R.string.common_username_with_prefix,
                            previewOptionItem.visibleSubtext
                        )
                    )
                } else {
                    previewOptionItem
                }
            },
            onItemClickListener = { viewModel.updateUserUsernamePreviewVisibilityValue(it) }
        )
    }

    private fun showUserBioPreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.edit_profile_bio),
            items = items,
            onItemClickListener = { viewModel.updateUserBioPreviewVisibilityValue(it) }
        )
    }

    private fun showUserSkillsPreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.skills_title),
            items = items,
            onItemClickListener = { viewModel.updateUserSkillsPreviewVisibilityValue(it) }
        )
    }

    private fun showUserExperiencePreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.experience_title),
            items = items.map { previewOptionItem ->
                if (previewOptionItem is PreviewOptionItem.PartiallyVisible) {
                    previewOptionItem.copy(
                        partiallyVisibleSubtext = getString(
                            R.string.previews_add_experience_company_names_are_hidden
                        )
                    )
                } else {
                    previewOptionItem
                }
            },
            onItemClickListener = { viewModel.updateUserExperiencePreviewVisibilityValue(it) }
        )
    }

    private fun showUserEducationPreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.education_title),
            items = items.map { previewOptionItem ->
                if (previewOptionItem is PreviewOptionItem.PartiallyVisible) {
                    previewOptionItem.copy(
                        partiallyVisibleSubtext = getString(
                            R.string.previews_add_education_institution_names_are_hidden
                        )
                    )
                } else {
                    previewOptionItem
                }
            },
            onItemClickListener = { viewModel.updateUserEducationPreviewVisibilityValue(it) }
        )
    }

    private fun showUserLanguagesPreviewOptionItems(items: List<PreviewOptionItem>) {
        showPreviewOptionItems(
            title = getString(R.string.languages_title),
            items = items,
            onItemClickListener = { viewModel.updateUserLanguagesPreviewVisibilityValue(it) }
        )
    }

    private fun setupPreviewTitleError(error: String?) {
        binding.titleTextInputLayout.error = error
    }

    private fun showPreviewOptionItems(
        title: String,
        items: List<PreviewOptionItem>,
        onItemClickListener: (previewVisibilityValue: PreviewVisibilityValue) -> Unit
    ) {
        val dialog = PreviewOptionBottomSheetDialogFragment()
        dialog.title = title
        dialog.items = items
        dialog.onItemClickListener = onItemClickListener
        dialog.show(childFragmentManager, String.empty)
    }

    private fun showPreviewHeader(previewHeader: PreviewsAddViewModel.PreviewHeader) {
        binding.titleEditText.removeTextChangedListener(titleTextWatcher)
        if (previewHeader.titleString != binding.titleEditText.text.toString()) {
            binding.titleEditText.setText(previewHeader.titleString)
        }
        binding.titleEditText.addTextChangedListener(titleTextWatcher)
        binding.expChipGroup.setOnCheckedStateChangeListener(null)
        binding.expChipGroup.check(
            when (previewHeader.previewLifetimeOption) {
                PreviewLifetimeOption.Option1 -> binding.exp1Chip.id
                PreviewLifetimeOption.Option2 -> binding.exp2Chip.id
                PreviewLifetimeOption.Option3 -> binding.exp3Chip.id
                PreviewLifetimeOption.Option4 -> binding.exp4Chip.id
                PreviewLifetimeOption.Option5 -> binding.exp5Chip.id
                PreviewLifetimeOption.Option6 -> binding.exp6Chip.id
            }
        )
        binding.expChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val option = when {
                checkedIds.contains(binding.exp1Chip.id) -> PreviewLifetimeOption.Option1
                checkedIds.contains(binding.exp2Chip.id) -> PreviewLifetimeOption.Option2
                checkedIds.contains(binding.exp3Chip.id) -> PreviewLifetimeOption.Option3
                checkedIds.contains(binding.exp4Chip.id) -> PreviewLifetimeOption.Option4
                checkedIds.contains(binding.exp5Chip.id) -> PreviewLifetimeOption.Option5
                checkedIds.contains(binding.exp6Chip.id) -> PreviewLifetimeOption.Option6
                else -> null
            }
            option?.let { onLifetimeItemClick(it) }
        }
        binding.optionChipGroup.setOnCheckedStateChangeListener(null)
        when (previewHeader.previewVisibilityPattern) {
            PreviewVisibilityPattern.Visible ->
                binding.optionChipGroup.check(binding.visibleOptionChip.id)
            PreviewVisibilityPattern.Anonymous ->
                binding.optionChipGroup.check(binding.anonymousOptionChip.id)
            else -> binding.optionChipGroup.clearCheck()
        }
        binding.optionChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val option = when {
                checkedIds.contains(binding.visibleOptionChip.id) -> PreviewVisibilityPattern.Visible
                checkedIds.contains(binding.anonymousOptionChip.id) -> PreviewVisibilityPattern.Anonymous
                else -> null
            }
            option?.let { onVisibilityItemClick(it) }
        }
    }

    private fun showEmptyState(emptyState: PreviewsAddViewModel.EmptyState) {
        binding.emptyStateTextView.text = getString(
            when (emptyState) {
                PreviewsAddViewModel.EmptyState.Removed -> R.string.preview_empty_state_removed
            }
        )
        binding.emptyStateTextView.visible()
        binding.linearLayout.gone()
    }

    private fun showDeletePreviewDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.previews_add_delete)
            .setMessage(R.string.previews_add_delete_are_you_sure)
            .setNegativeButton(R.string.common_decline) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
                viewModel.deletePreview()
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        const val EXTRA_PREVIEW_ID = "extraPreviewId"

        fun newInstance(previewId: String?): PreviewsAddFragment {
            return if (previewId == null) {
                PreviewsAddFragment()
            } else {
                PreviewsAddFragment().withArguments(EXTRA_PREVIEW_ID to previewId)
            }
        }
    }
}