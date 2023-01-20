package com.wurple.presentation.flow.more.support

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentSupportBinding
import com.wurple.presentation.delegate.clipboard.ClipboardDelegate
import com.wurple.presentation.delegate.clipboard.DefaultClipboardDelegate
import com.wurple.presentation.extension.composeEmail
import com.wurple.presentation.extension.isAppInstalled
import com.wurple.presentation.extension.openTelegramChannel
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.more.support.adapter.SupportAdapter
import com.wurple.presentation.flow.more.support.adapter.SupportItem

class SupportFragment : BaseViewModelFragment<SupportViewModel>(
    layoutResId = R.layout.fragment_support
), ClipboardDelegate by DefaultClipboardDelegate() {
    override val viewModel: SupportViewModel by baseViewModel()
    override val binding: FragmentSupportBinding by viewBinding()
    private val adapter = SupportAdapter(::onItemClick, ::onItemLongClick)

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.emailLiveData.observe(::openEmailApp)
        viewModel.copyEmailLiveData.observe(::copyEmailToClipboard)
        viewModel.telegramChannelLiveData.observe(::openTelegramChannel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachClipboardDelegate(context)
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
        binding.recyclerView.adapter = adapter
    }

    private fun onItemClick(item: SupportItem) {
        when (item) {
            is SupportItem.Email -> {
                viewModel.navigateEmail()
            }
            is SupportItem.Telegram -> {
                viewModel.navigateTelegram()
            }
        }
    }

    private fun onItemLongClick(item: SupportItem) {
        if (item is SupportItem.Email) {
            viewModel.copyEmail()
        }
    }

    private fun openEmailApp(email: String) {
        context?.composeEmail(
            addresses = arrayOf(email),
            subject = getString(R.string.support_email_subject, getString(R.string.app_name))
        )
    }

    private fun copyEmailToClipboard(email: String) {
        copyToClipboard(binding.container, email)
    }

    private fun openTelegramChannel(telegramChannel: String) {
        context?.openTelegramChannel(binding.container, telegramChannel)
    }

    private fun getItems(): List<SupportItem> {
        val mutableList = mutableListOf<SupportItem>()
        mutableList.add(
            SupportItem.Email(
                iconResIdValue = R.drawable.ic_email,
                titleResIdValue = R.string.support_email
            )
        )
        if (isTelegramAppInstalled()) {
            mutableList.add(
                SupportItem.Telegram(
                    iconResIdValue = R.drawable.ic_telegram,
                    titleResIdValue = R.string.support_telegram
                )
            )
        }
        return mutableList.toList()
    }

    private fun isTelegramAppInstalled(): Boolean {
        return requireContext().isAppInstalled("org.telegram.messenger") ||
                requireContext().isAppInstalled("org.telegram.messenger.web")
    }

    companion object {
        fun newInstance(): SupportFragment = SupportFragment()
    }
}