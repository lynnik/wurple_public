package com.wurple.presentation.flow.more.account.createdeleteaccountrequest

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentCreateDeleteAccountRequestBinding
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment

class CreateDeleteAccountRequestFragment :
    BaseViewModelFragment<CreateDeleteAccountRequestViewModel>(
        layoutResId = R.layout.fragment_create_delete_account_request
    ) {
    override val viewModel: CreateDeleteAccountRequestViewModel by baseViewModel()
    override val binding: FragmentCreateDeleteAccountRequestBinding by viewBinding()

    override fun onProgressVisible() {
        binding.mainProgressIndicator.show()
        binding.firstCheckCheckBox.isEnabled = false
        binding.actionButton.isEnabled = false
    }

    override fun onProgressGone() {
        binding.mainProgressIndicator.hide()
        binding.firstCheckCheckBox.isEnabled = true
        binding.actionButton.isEnabled = true && binding.firstCheckCheckBox.isChecked
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.container)
        setupToolbar()
        setupActionButton()
        setupFirstCheckCheckBox()
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

    private fun setupFirstCheckCheckBox() {
        binding.firstCheckCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.actionButton.isEnabled = isChecked
        }
    }

    companion object {
        fun newInstance(): CreateDeleteAccountRequestFragment = CreateDeleteAccountRequestFragment()
    }
}