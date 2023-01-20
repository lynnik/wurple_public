package com.wurple.presentation.flow.previews

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.ScrollingView
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentPreviewsBinding
import com.wurple.domain.extension.empty
import com.wurple.presentation.delegate.clipboard.ClipboardDelegate
import com.wurple.presentation.delegate.clipboard.DefaultClipboardDelegate
import com.wurple.presentation.extension.*
import com.wurple.presentation.flow.previews.adapter.PreviewsAdapter
import com.wurple.presentation.flow.previews.adapter.PreviewsItemDecoration
import com.wurple.presentation.flow.previews.share.SharePreviewBottomSheetDialogFragment
import com.wurple.presentation.flow.you.MainChildFragment
import com.wurple.presentation.model.preview.PreviewListItem

class PreviewsFragment : MainChildFragment<PreviewsViewModel>(
    layoutResId = R.layout.fragment_previews
), ClipboardDelegate by DefaultClipboardDelegate() {
    override val viewModel: PreviewsViewModel by baseViewModel()
    override val binding: FragmentPreviewsBinding by viewBinding()
    private val adapter = PreviewsAdapter(::onEditClick, ::onShareClick, ::onItemClick)
    private var animation: AnimatorSet? = null

    override fun getScrollingView(): ScrollingView = binding.recyclerView

    override fun onProgressVisible() {
        getMainFragment()?.showProgress()
    }

    override fun onProgressGone() {
        hideSwipeRefreshLayout()
        getMainFragment()?.hideProgress()
    }

    override fun onShowErrorMessage(error: String) {
        hideSwipeRefreshLayout()
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.previewsLiveData.observe(::showPreviews)
        viewModel.previewDeepLinkLiveData.observe { (title, deepLink) ->
            showPreviewDeepLink(title, deepLink)
        }
        viewModel.showSkeletonLiveData.observe { showSkeleton() }
        viewModel.hideSkeletonLiveData.observe { hideSkeleton() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachClipboardDelegate(context)
        setupSwipeRefreshLayout()
        setupRecyclerView()
        setupAddPreviewButton()
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        animation?.cancel()
        super.onDestroyView()
    }

    fun navigatePreviewsAdd() {
        viewModel.navigatePreviewsAdd()
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            viewModel.fetchPreview(shouldShowProgress = false)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(PreviewsItemDecoration())
    }

    private fun setupAddPreviewButton() {
        binding.addPreviewButton.setOnClickListener { navigatePreviewsAdd() }
    }

    private fun hideSwipeRefreshLayout() {
        if (binding.swipeRefreshLayout.isRefreshing) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showSkeleton() {
        binding.skeletonGroup.visible()
        binding.recyclerView.gone()
        binding.addPreviewCardView.gone()
    }

    private fun hideSkeleton() {
        val alphaMin = 0F
        val alphaMax = 1F
        val translationMin = 0F
        val translationMax = resources.getDimensionPixelSize(R.dimen.size_x_large).toFloat()
        val skeletonAnimation = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(
                    binding.skeleton1MaterialCardView,
                    View.ALPHA,
                    alphaMax,
                    alphaMin
                ),
                ObjectAnimator.ofFloat(
                    binding.skeleton2MaterialCardView,
                    View.ALPHA,
                    alphaMax,
                    alphaMin
                ),
                ObjectAnimator.ofFloat(
                    binding.skeleton3MaterialCardView,
                    View.ALPHA,
                    alphaMax,
                    alphaMin
                ),
                ObjectAnimator.ofFloat(
                    binding.skeleton4MaterialCardView,
                    View.ALPHA,
                    alphaMax,
                    alphaMin
                ),
                ObjectAnimator.ofFloat(
                    binding.skeleton1MaterialCardView,
                    View.TRANSLATION_Y,
                    translationMin,
                    translationMax
                ),
                ObjectAnimator.ofFloat(
                    binding.skeleton2MaterialCardView,
                    View.TRANSLATION_Y,
                    translationMin,
                    translationMax
                ),
                ObjectAnimator.ofFloat(
                    binding.skeleton3MaterialCardView,
                    View.TRANSLATION_Y,
                    translationMin,
                    translationMax
                ),
                ObjectAnimator.ofFloat(
                    binding.skeleton4MaterialCardView,
                    View.TRANSLATION_Y,
                    translationMin,
                    translationMax
                )
            )
        }
        val contentAnimation = ObjectAnimator.ofFloat(
            binding.recyclerView,
            View.ALPHA,
            alphaMin,
            alphaMax
        )
        val emptyStateAnimation = ObjectAnimator.ofFloat(
            binding.addPreviewCardView,
            View.ALPHA,
            alphaMin,
            alphaMax
        )
        animation = AnimatorSet().apply {
            duration = 300
            playSequentially(
                skeletonAnimation,
                AnimatorSet().apply { playTogether(contentAnimation, emptyStateAnimation) }
            )
            doOnStart {
                binding.recyclerView.alpha = alphaMin
                binding.recyclerView.visible()
                binding.addPreviewCardView.alpha = alphaMin
                binding.addPreviewCardView.visible()
            }
            doOnEnd { binding.skeletonGroup.gone() }
        }
        animation?.start()
    }

    private fun showPreviews(previews: List<PreviewListItem>) {
        binding.addPreviewCardView.isVisible = previews.isEmpty()
        adapter.submitList(previews)
    }

    private fun showPreviewDeepLink(title: String, deepLink: String) {
        val dialog = SharePreviewBottomSheetDialogFragment().apply {
            this.title = title
            this.deepLink = deepLink
            onShareClickListener = { deepLink -> context?.share(deepLink) }
            onCopyClickListener = { deepLink -> copyTextToClipboard(deepLink = deepLink) }
        }
        dialog.show(childFragmentManager, String.empty)
    }

    private fun onEditClick(preview: PreviewListItem) {
        viewModel.navigateEditPreview(preview.previewId)
    }

    private fun onShareClick(preview: PreviewListItem) {
        viewModel.showPreviewDeeplink(preview.previewId, preview.title)
    }

    private fun onItemClick(preview: PreviewListItem) {
        viewModel.navigatePreview(preview.previewId)
    }

    private fun copyTextToClipboard(deepLink: String) {
        copyToClipboard(container = binding.container, text = deepLink)
    }

    companion object {
        fun newInstance(): PreviewsFragment = PreviewsFragment()
    }
}
