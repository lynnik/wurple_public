package com.wurple.presentation.flow.profile.edit

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileBinding
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.profile.edit.adapter.EditProfileAdapter
import com.wurple.presentation.flow.profile.edit.adapter.EditProfileItem

class EditProfileFragment : BaseViewModelFragment<EditProfileViewModel>(
    layoutResId = R.layout.fragment_edit_profile
) {
    override val viewModel: EditProfileViewModel by baseViewModel()
    override val binding: FragmentEditProfileBinding by viewBinding()
    private val adapter = EditProfileAdapter(::onItemClick)

    override fun onProgressVisible() {
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        hideSwipeRefreshLayout()
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        hideSwipeRefreshLayout()
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.editProfileItemsLiveData.observe(::showEditProfileItems)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setBottomSystemBarInset(binding.recyclerView)
        setupToolbar()
        setupSwipeRefreshLayout()
        setupRecyclerView()
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { viewModel.navigateBack() }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            viewModel.fetchUser(shouldShowProgress = false)
        }
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@EditProfileFragment.adapter
        }
    }

    private fun hideSwipeRefreshLayout() {
        if (binding.swipeRefreshLayout.isRefreshing) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showEditProfileItems(editProfileItems: List<EditProfileItem>) {
        adapter.submitList(editProfileItems)
    }

    private fun onItemClick(item: EditProfileItem) {
        when (item) {
            is EditProfileItem.Image -> viewModel.navigateEditProfileImage()
            is EditProfileItem.UserFullName -> viewModel.navigateEditProfileName()
            is EditProfileItem.UserDob -> viewModel.navigateEditProfileDob()
            is EditProfileItem.UserLocation -> viewModel.navigateEditProfileLocation()
            is EditProfileItem.UserEmail -> viewModel.navigateEditProfileEmail()
            is EditProfileItem.UserUsername -> viewModel.navigateEditProfileUsername()
            is EditProfileItem.UserBio -> viewModel.navigateEditProfileBio()
            EditProfileItem.UserSkills -> viewModel.navigateEditProfileSkills()
            EditProfileItem.UserExperience -> viewModel.navigateEditProfileExperience()
            EditProfileItem.UserEducation -> viewModel.navigateEditProfileEducation()
            EditProfileItem.UserLanguages -> viewModel.navigateEditProfileLanguages()
        }
    }

    companion object {
        fun newInstance(): EditProfileFragment = EditProfileFragment()
    }
}