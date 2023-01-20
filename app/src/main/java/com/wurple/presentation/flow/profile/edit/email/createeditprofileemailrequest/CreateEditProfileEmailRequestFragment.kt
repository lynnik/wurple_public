package com.wurple.presentation.flow.profile.edit.email.createeditprofileemailrequest

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentCreateEditProfileEmailRequestBinding
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment

class CreateEditProfileEmailRequestFragment :
    BaseViewModelFragment<CreateEditProfileEmailRequestViewModel>(
        layoutResId = R.layout.fragment_create_edit_profile_email_request
    ) {
    override val viewModel: CreateEditProfileEmailRequestViewModel by baseViewModel()
    override val binding: FragmentCreateEditProfileEmailRequestBinding by viewBinding()

    override fun onProgressVisible() {
        binding.mainProgressIndicator.show()
        binding.actionButton.isEnabled = false
    }

    override fun onProgressGone() {
        binding.mainProgressIndicator.hide()
        binding.actionButton.isEnabled = true
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.container)
        setupToolbar()
        setupActionButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }

    private fun setupActionButton() {
        binding.actionButton.setOnClickListener {
            viewModel.createRequest()
        }
    }

    companion object {
        fun newInstance(): CreateEditProfileEmailRequestFragment = CreateEditProfileEmailRequestFragment()
    }
}