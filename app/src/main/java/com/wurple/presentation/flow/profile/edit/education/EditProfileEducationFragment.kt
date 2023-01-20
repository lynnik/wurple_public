package com.wurple.presentation.flow.profile.edit.education

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileEducationBinding
import com.wurple.domain.model.user.UserEducation
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.profile.edit.education.adapter.EditProfileEducationAdapter

class EditProfileEducationFragment : BaseViewModelFragment<EditProfileEducationViewModel>(
    layoutResId = R.layout.fragment_edit_profile_education
) {
    override val viewModel: EditProfileEducationViewModel by baseViewModel()
    override val binding: FragmentEditProfileEducationBinding by viewBinding()
    private val adapter = EditProfileEducationAdapter(::onItemClick)

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
        viewModel.educationLiveData.observe(::showEducation)
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
        binding.toolbar.inflateMenu(R.menu.menu_edit_profile_education)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemAdd -> {
                    viewModel.navigateEducationAdd()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
    }

    private fun showEducation(education: List<UserEducation>) {
        binding.emptyStateTextView.isVisible = education.isEmpty()
        adapter.submitList(education)
    }

    private fun onItemClick(item: UserEducation) {
        viewModel.navigateEditUserEducation(item.id)
    }

    companion object {
        fun newInstance(): EditProfileEducationFragment = EditProfileEducationFragment()
    }
}