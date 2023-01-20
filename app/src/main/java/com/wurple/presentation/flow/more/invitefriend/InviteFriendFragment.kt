package com.wurple.presentation.flow.more.invitefriend

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentInviteFriendBinding
import com.wurple.presentation.delegate.clipboard.ClipboardDelegate
import com.wurple.presentation.delegate.clipboard.DefaultClipboardDelegate
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.manager.QrManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class InviteFriendFragment : BaseViewModelFragment<InviteFriendViewModel>(
    layoutResId = R.layout.fragment_invite_friend
), ClipboardDelegate by DefaultClipboardDelegate() {
    override val viewModel: InviteFriendViewModel by baseViewModel()
    override val binding: FragmentInviteFriendBinding by viewBinding()
    private val qrManager: QrManager by inject()
    private var qrCodeForegroundAnimation: ObjectAnimator? = null

    override fun onProgressVisible() {
        binding.nestedScrollView.gone()
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.nestedScrollView.visible()
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.deepLinkLiveData.observe(::showDeepLink)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachClipboardDelegate(context)
        setScreenBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
    }

    override fun onDestroyView() {
        setScreenBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE)
        qrCodeForegroundAnimation?.cancel()
        super.onDestroyView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { viewModel.navigateBack() }
    }

    private fun showDeepLink(deepLink: String) {
        qrCodeForegroundAnimation =
            ObjectAnimator.ofFloat(binding.deepLinkQrCodeView, View.ALPHA, 1F, 0F)
                .apply { duration = 300 }
        viewLifecycleOwner.lifecycleScope.launch {
            qrManager.generateQrCode(deepLink)?.let { qrCodeBitmap ->
                binding.deepLinkQrCodeCircularProgressIndicator.hide()
                binding.deepLinkQrCodeImageView.setImageBitmap(qrCodeBitmap)
                qrCodeForegroundAnimation?.start()
            }
        }
        binding.deepLinkTextView.text = deepLink
        binding.shareButton.setOnClickListener {
            context?.share(deepLink)
        }
        binding.copyButton.setOnClickListener {
            copyToClipboard(container = binding.container, text = deepLink)
        }
    }

    companion object {
        fun newInstance(): InviteFriendFragment = InviteFriendFragment()
    }
}