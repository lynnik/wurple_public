package com.wurple.presentation.flow.previews.temppreview

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentTempPreviewBinding
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.extension.visible
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.model.preview.PreviewUi

class TempPreviewFragment : BaseViewModelFragment<TempPreviewViewModel>(
    layoutResId = R.layout.fragment_temp_preview
) {
    override val viewModel: TempPreviewViewModel by baseViewModel()
    override val binding: FragmentTempPreviewBinding by viewBinding()

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
        viewModel.titleLiveData.observe(binding.toolbar::setTitle)
        viewModel.previewUiLiveData.observe(::showPreviewUi)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setBottomSystemBarInset(binding.nestedScrollView)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }

    private fun showPreviewUi(previewUi: PreviewUi) {
        binding.previewView.setData(previewUi)
        binding.linearLayout.visible()
    }

    companion object {
        fun newInstance(): TempPreviewFragment {
            return TempPreviewFragment()
        }
    }
}
