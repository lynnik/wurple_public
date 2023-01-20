package com.wurple.presentation.flow.you

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.ScrollingView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentYouBinding
import com.wurple.presentation.extension.gone
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.extension.visible
import com.wurple.presentation.extension.visibleOrGone
import com.wurple.presentation.model.preview.PreviewUi

class YouFragment : MainChildFragment<YouViewModel>(
    layoutResId = R.layout.fragment_you
) {
    override val viewModel: YouViewModel by baseViewModel()
    override val binding: FragmentYouBinding by viewBinding()
    private var animation: AnimatorSet? = null

    override fun getScrollingView(): ScrollingView = binding.nestedScrollView

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
        viewModel.isFillOutProfileViewVisibleLiveData
            .observe(binding.fillOutProfileCardView::visibleOrGone)
        viewModel.previewUiLiveData.observe(::showPreviewUi)
        viewModel.showSkeletonLiveData.observe { showSkeleton() }
        viewModel.hideSkeletonLiveData.observe { hideSkeleton() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeRefreshLayout()
        setupFillOutProfileView()
    }

    override fun onDestroyView() {
        animation?.cancel()
        super.onDestroyView()
    }

    fun navigateEditProfile() {
        viewModel.navigateEditProfile()
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            viewModel.fetchUser(shouldShowProgress = false)
        }
    }

    private fun setupFillOutProfileView() {
        binding.fillOutProfilePositiveActionButton.setOnClickListener {
            viewModel.navigateFillOutProfile()
        }
        binding.fillOutProfileNegativeActionButton.setOnClickListener {
            viewModel.hideFillOutProfileView()
        }
    }

    private fun hideSwipeRefreshLayout() {
        if (binding.swipeRefreshLayout.isRefreshing) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showSkeleton() {
        binding.skeletonGroup.visible()
        binding.nestedScrollView.gone()
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
            binding.nestedScrollView,
            View.ALPHA,
            alphaMin,
            alphaMax
        )
        animation = AnimatorSet().apply {
            duration = 300
            playSequentially(skeletonAnimation, contentAnimation)
            doOnStart {
                binding.nestedScrollView.alpha = alphaMin
                binding.nestedScrollView.visible()
            }
            doOnEnd { binding.skeletonGroup.gone() }
        }
        animation?.start()
    }

    private fun showPreviewUi(previewUi: PreviewUi) {
        binding.previewView.setData(previewUi)
    }

    companion object {
        fun newInstance(): YouFragment = YouFragment()
    }
}