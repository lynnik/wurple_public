package com.wurple.presentation.flow.previews.add.dialog

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.DialogPreviewOptionsBinding
import com.wurple.domain.model.preview.PreviewVisibilityValue
import com.wurple.presentation.flow.base.bottomsheet.BaseBottomSheetDialogFragment
import com.wurple.presentation.flow.previews.add.dialog.adapter.PreviewOptionAdapter
import com.wurple.presentation.flow.previews.add.dialog.adapter.PreviewOptionItem

class PreviewOptionBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment(layoutResId = R.layout.dialog_preview_options) {
    var title: String? = null
    var items: List<PreviewOptionItem>? = null
    var onItemClickListener: ((previewVisibilityValue: PreviewVisibilityValue) -> Unit)? = null
    override val binding: DialogPreviewOptionsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitle()
        setupRecyclerView()
    }

    private fun setupTitle() {
        binding.titleTextView.text = title
    }

    private fun setupRecyclerView() {
        val adapter = PreviewOptionAdapter(
            onItemClickListener = { previewVisibilityValue ->
                onItemClickListener?.invoke(previewVisibilityValue)
                dismiss()
            }
        )
        binding.recyclerView.adapter = adapter
        items?.let { adapter.submitList(it) }
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }
}