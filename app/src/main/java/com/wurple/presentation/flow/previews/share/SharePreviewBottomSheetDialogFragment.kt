package com.wurple.presentation.flow.previews.share

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.DialogSharePreviewBinding
import com.wurple.presentation.extension.setScreenBrightness
import com.wurple.presentation.flow.base.bottomsheet.BaseBottomSheetDialogFragment
import com.wurple.presentation.manager.QrManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SharePreviewBottomSheetDialogFragment :
    BaseBottomSheetDialogFragment(layoutResId = R.layout.dialog_share_preview) {
    var title: String? = null
    var deepLink: String? = null
    var onShareClickListener: ((deepLink: String) -> Unit)? = null
    var onCopyClickListener: ((deepLink: String) -> Unit)? = null
    override val binding: DialogSharePreviewBinding by viewBinding()
    private val qrManager: QrManager by inject()
    private var qrCodeForegroundAnimation: ObjectAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setScreenBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL)
        setupHapticFeedback()
        setupTitleTextView()
        setupQrCodeImage()
        setupDeepLinkTextView()
        setupShareButton()
        setupCopyButton()
    }

    override fun onDestroyView() {
        setScreenBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE)
        qrCodeForegroundAnimation?.cancel()
        super.onDestroyView()
    }

    private fun setupHapticFeedback() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(HAPTIC_FEEDBACK_DELAY_MILLIS)
            binding.nestedScrollView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    private fun setupTitleTextView() {
        binding.titleTextView.text = title
    }

    private fun setupQrCodeImage() {
        qrCodeForegroundAnimation = ObjectAnimator.ofFloat(binding.qrCodeView, View.ALPHA, 1F, 0F)
            .apply { duration = 300 }
        deepLink?.let { link ->
            viewLifecycleOwner.lifecycleScope.launch {
                qrManager.generateQrCode(link)?.let { qrCodeBitmap ->
                    binding.qrCodeCircularProgressIndicator.hide()
                    binding.qrCodeImageView.setImageBitmap(qrCodeBitmap)
                    qrCodeForegroundAnimation?.start()
                }
            }
        }
    }

    private fun setupDeepLinkTextView() {
        binding.deepLinkTextView.text = deepLink
    }

    private fun setupShareButton() {
        binding.shareButton.setOnClickListener {
            deepLink?.let { onShareClickListener?.invoke(it) }
            dismiss()
        }
    }

    private fun setupCopyButton() {
        binding.copyButton.setOnClickListener {
            deepLink?.let { onCopyClickListener?.invoke(it) }
            dismiss()
        }
    }

    companion object {
        private const val HAPTIC_FEEDBACK_DELAY_MILLIS = 100L
    }
}
