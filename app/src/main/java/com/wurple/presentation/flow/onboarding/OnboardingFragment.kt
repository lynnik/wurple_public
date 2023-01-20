package com.wurple.presentation.flow.onboarding

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.wurple.R
import com.wurple.databinding.FragmentOnboardingBinding
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.onboarding.adapter.OnboardingAdapter
import com.wurple.presentation.flow.onboarding.adapter.OnboardingPage

class OnboardingFragment : BaseViewModelFragment<OnboardingViewModel>(
    layoutResId = R.layout.fragment_onboarding
) {
    override val viewModel: OnboardingViewModel by baseViewModel()
    override val binding: FragmentOnboardingBinding by viewBinding()
    private val items = listOf(
        OnboardingPage.First,
        OnboardingPage.Second,
        OnboardingPage.Third
    )
    private val adapter = OnboardingAdapter()
    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val lastPosition = items.size - 1
            binding.button.isInvisible = position != lastPosition
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setBottomSystemBarInset(binding.frameLayout)
        setupToolbar()
        setupViewPager()
        setupButton()
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        binding.viewPager.setPageTransformer(null)
        super.onDestroyView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        adapter.submitList(items)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, _ ->
            tab.view.isClickable = false
        }.attach()
    }

    private fun setupButton() {
        binding.button.setOnClickListener {
            viewModel.navigateBack()
        }
    }

    companion object {
        fun newInstance(): OnboardingFragment {
            return OnboardingFragment()
        }
    }
}
