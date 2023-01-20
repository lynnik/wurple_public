package com.wurple.presentation.flow.more

import android.os.Bundle
import android.view.View
import androidx.core.view.ScrollingView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentMoreBinding
import com.wurple.presentation.flow.more.adapter.MoreAdapter
import com.wurple.presentation.flow.more.adapter.MoreItem
import com.wurple.presentation.flow.you.MainChildFragment

class MoreFragment : MainChildFragment<MoreViewModel>(
    layoutResId = R.layout.fragment_more
) {
    override val viewModel: MoreViewModel by baseViewModel()
    override val binding: FragmentMoreBinding by viewBinding()
    private val adapter = MoreAdapter(::onItemClick)

    override fun getScrollingView(): ScrollingView = binding.recyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        adapter.submitList(getItems())
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
    }

    private fun onItemClick(item: MoreItem) {
        when (item) {
            is MoreItem.Account -> {
                viewModel.navigateAccount()
            }
            is MoreItem.Support -> {
                viewModel.navigateSupport()
            }
            is MoreItem.InAppPurchases -> {
                // TODO
            }
            is MoreItem.InviteFriend -> {
                viewModel.navigateInviteFriend()
            }
            is MoreItem.Settings -> {
                // TODO
            }
            is MoreItem.AboutApp -> {
                viewModel.navigateAboutApp()
            }
        }
    }

    private fun getItems(): List<MoreItem> =
        listOf(
            MoreItem.Account(
                iconResIdValue = R.drawable.ic_account_box,
                titleResIdValue = R.string.account_title
            ),
            MoreItem.Support(
                iconResIdValue = R.drawable.ic_support_agent,
                titleResIdValue = R.string.common_support
            ),
            /*MoreItem.InAppPurchases(
                iconResIdValue = R.drawable.ic_paid,
                titleResIdValue = R.string.in_app_purchases_title
            ),*/
            MoreItem.InviteFriend(
                iconResIdValue = R.drawable.ic_person_add,
                titleResIdValue = R.string.invite_friend_title
            ),
            /*MoreItem.Settings(
                iconResIdValue = R.drawable.ic_settings,
                titleResIdValue = R.string.settings_title
            ),*/
            MoreItem.AboutApp(
                iconResIdValue = R.drawable.ic_info,
                titleResIdValue = R.string.about_app_title
            )
        )

    companion object {
        fun newInstance(): MoreFragment = MoreFragment()
    }
}