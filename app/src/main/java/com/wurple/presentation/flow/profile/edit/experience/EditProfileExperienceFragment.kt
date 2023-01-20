package com.wurple.presentation.flow.profile.edit.experience

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileExperienceBinding
import com.wurple.domain.model.user.UserExperience
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.profile.edit.experience.adapter.EditProfileExperienceAdapter

class EditProfileExperienceFragment : BaseViewModelFragment<EditProfileExperienceViewModel>(
    layoutResId = R.layout.fragment_edit_profile_experience
) {
    override val viewModel: EditProfileExperienceViewModel by baseViewModel()
    override val binding: FragmentEditProfileExperienceBinding by viewBinding()
    private val adapter = EditProfileExperienceAdapter(::onItemClick)

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
        viewModel.experienceLiveData.observe(::showExperience)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setBottomSystemBarInset(binding.recyclerView)
        setupToolbar()
        setupRecyclerView()
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
        binding.toolbar.inflateMenu(R.menu.menu_edit_profile_experience)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemAdd -> {
                    viewModel.navigateExperienceAdd()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
    }

    private fun showExperience(experience: List<UserExperience>) {
        binding.emptyStateTextView.isVisible = experience.isEmpty()
        adapter.submitList(experience)
    }

    private fun onItemClick(item: UserExperience) {
        viewModel.navigateEditUserExperience(item.id)
    }

    companion object {
        fun newInstance(): EditProfileExperienceFragment = EditProfileExperienceFragment()
    }
}