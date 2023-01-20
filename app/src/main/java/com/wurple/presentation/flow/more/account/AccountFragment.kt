package com.wurple.presentation.flow.more.account

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wurple.R
import com.wurple.databinding.FragmentAccountBinding
import com.wurple.presentation.extension.applyViewNavigationBarsInsets
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.more.account.adapter.AccountAdapter
import com.wurple.presentation.flow.more.account.adapter.AccountItem

class AccountFragment : BaseViewModelFragment<AccountViewModel>(
    layoutResId = R.layout.fragment_account
) {
    override val viewModel: AccountViewModel by baseViewModel()
    override val binding: FragmentAccountBinding by viewBinding()
    private val adapter = AccountAdapter(::onItemClick)
    private var isRecyclerViewClickEnabled = true

    override fun onProgressVisible() {
        binding.mainProgressIndicator.show()
        isRecyclerViewClickEnabled = false
    }

    override fun onProgressGone() {
        binding.mainProgressIndicator.hide()
        isRecyclerViewClickEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupRecyclerView()
        adapter.submitList(getItems())
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@AccountFragment.adapter
            applyViewNavigationBarsInsets()
        }
    }

    private fun onItemClick(item: AccountItem) {
        if (isRecyclerViewClickEnabled.not()) {
            return
        }
        when (item) {
            is AccountItem.EditEmail -> {
                viewModel.navigateEditProfileEmail()
            }
            is AccountItem.SignOut -> {
                showSignOutDialog()
            }
            is AccountItem.DeleteAccount -> {
                viewModel.navigateCreateDeleteAccountRequest()
            }
        }
    }

    private fun showSignOutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.account_sign_out)
            .setMessage(R.string.account_sign_out_are_you_sure)
            .setNegativeButton(R.string.common_decline) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
                viewModel.signOut()
                dialog.dismiss()
            }
            .show()
    }

    private fun getItems(): List<AccountItem> =
        listOf(
            AccountItem.EditEmail(
                iconResIdValue = R.drawable.ic_email,
                titleResIdValue = R.string.account_edit_email
            ),
            AccountItem.SignOut(
                iconResIdValue = R.drawable.ic_logout,
                titleResIdValue = R.string.account_sign_out
            ),
            AccountItem.DeleteAccount(
                iconResIdValue = R.drawable.ic_delete,
                titleResIdValue = R.string.account_delete_account
            )
        )

    companion object {
        fun newInstance(): AccountFragment = AccountFragment()
    }
}
