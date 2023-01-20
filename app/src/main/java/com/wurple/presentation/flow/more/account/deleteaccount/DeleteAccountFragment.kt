package com.wurple.presentation.flow.more.account.deleteaccount

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wurple.R
import com.wurple.databinding.FragmentDeleteAccountBinding
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment

class DeleteAccountFragment : BaseViewModelFragment<DeleteAccountViewModel>(
    layoutResId = R.layout.fragment_delete_account
) {
    override val viewModel: DeleteAccountViewModel by baseViewModel()
    override val binding: FragmentDeleteAccountBinding by viewBinding()

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
            showDeleteAccountDialog()
        }
    }

    private fun showDeleteAccountDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.account_delete_account)
            .setMessage(R.string.delete_account_are_you_sure)
            .setNegativeButton(R.string.common_decline) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
                viewModel.deleteAccount()
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        fun newInstance(): DeleteAccountFragment = DeleteAccountFragment()
    }
}