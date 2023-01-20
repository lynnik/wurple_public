package com.wurple.presentation.flow.profile.edit.skills

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileSkillsBinding
import com.wurple.domain.model.user.UserSkill
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment

class EditProfileSkillsFragment : BaseViewModelFragment<EditProfileSkillsViewModel>(
    layoutResId = R.layout.fragment_edit_profile_skills
) {
    override val viewModel: EditProfileSkillsViewModel by baseViewModel()
    override val binding: FragmentEditProfileSkillsBinding by viewBinding()

    override fun onProgressVisible() {
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.skillsLiveData.observe(::showSkills)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setBottomSystemBarInset(binding.nestedScrollView)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
        binding.toolbar.inflateMenu(R.menu.menu_edit_profile_skills)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemAdd -> {
                    viewModel.navigateSkillsAdd()
                    true
                }
                else -> false
            }
        }
    }

    private fun showSkills(skills: List<UserSkill>) {
        val personalSkills = skills.filter { it.type == UserSkill.Type.Personal }
        val professionalSkills = skills.filter { it.type == UserSkill.Type.Professional }
        binding.emptyStateTextView.isVisible = skills.isEmpty()
        binding.personalSkillsTitleTextView.isVisible = personalSkills.isEmpty().not()
        binding.personalSkillsChipGroup.isVisible = personalSkills.isEmpty().not()
        binding.professionalSkillsTitleTextView.isVisible = professionalSkills.isEmpty().not()
        binding.professionalSkillsChipGroup.isVisible = professionalSkills.isEmpty().not()
        if (personalSkills.isNotEmpty()) {
            addChips(binding.personalSkillsChipGroup, personalSkills)
        }
        if (professionalSkills.isNotEmpty()) {
            addChips(binding.professionalSkillsChipGroup, professionalSkills)
        }
    }

    private fun addChips(container: ChipGroup, skills: List<UserSkill>) {
        container.removeAllViews()
        skills.forEach { skill ->
            addChip(container, skill)
        }
    }

    private fun addChip(container: ChipGroup, userSkill: UserSkill) {
        val chip = Chip(requireContext()).apply {
            text = userSkill.title
            setEnsureMinTouchTargetSize(false)
            setOnClickListener { onItemClick(userSkill) }
        }
        container.addView(chip)
    }

    private fun onItemClick(item: UserSkill) {
        viewModel.navigateEditUserSkills(item.id)
    }

    companion object {
        fun newInstance(): EditProfileSkillsFragment = EditProfileSkillsFragment()
    }
}