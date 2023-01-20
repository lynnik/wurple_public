package com.wurple.presentation.flow.previews.preview

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentPreviewBinding
import com.wurple.presentation.delegate.clipboard.ClipboardDelegate
import com.wurple.presentation.delegate.clipboard.DefaultClipboardDelegate
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.model.preview.PreviewUi

class PreviewFragment : BaseViewModelFragment<PreviewViewModel>(
    layoutResId = R.layout.fragment_preview
), ClipboardDelegate by DefaultClipboardDelegate() {
    override val viewModel: PreviewViewModel by baseViewModel()
    override val binding: FragmentPreviewBinding by viewBinding()

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
        viewModel.emptyStateLiveData.observe(::showEmptyState)
        viewModel.previewExpDateLiveData.observe(::showPreviewExpDate)
        viewModel.previewUiLiveData.observe(::showPreviewUi)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachClipboardDelegate(context)
        setTopSystemBarInset(binding.appBarLayout)
        setBottomSystemBarInset(binding.nestedScrollView)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }

    private fun showEmptyState(emptyState: PreviewViewModel.EmptyState) {
        binding.emptyStateTextView.text = getString(
            when (emptyState) {
                PreviewViewModel.EmptyState.Removed -> R.string.preview_empty_state_removed
                PreviewViewModel.EmptyState.Expired -> R.string.preview_empty_state_expired
            }
        )
        binding.emptyStateTextView.visible()
        binding.linearLayout.gone()
    }

    private fun showPreviewExpDate(previewExpDate: PreviewViewModel.PreviewExpDate) {
        binding.dateTextView.text = resources.getQuantityString(
            R.plurals.previews_add_available_days,
            previewExpDate.differenceBetweenCurrentDateAndExpDate,
            previewExpDate.formattedExpDate,
            previewExpDate.differenceBetweenCurrentDateAndExpDate
        )
    }

    private fun showPreviewUi(previewUi: PreviewUi) {
        binding.previewView.onUserEmailCopiedClickListener = {
            copyToClipboard(binding.container, previewUi.secondaryInfo.userEmail)
        }
        binding.previewView.setData(previewUi)
        binding.linearLayout.visible()
        binding.emptyStateTextView.gone()
    }

    companion object {
        const val EXTRA_PREVIEW_ID = "extraPreviewId"

        fun newInstance(previewId: String): PreviewFragment {
            return PreviewFragment().withArguments(EXTRA_PREVIEW_ID to previewId)
        }
    }
}
