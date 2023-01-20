package com.wurple.presentation.flow.onboarding.adapter

import com.wurple.R
import com.wurple.databinding.ItemOnboardingBinding
import com.wurple.presentation.flow.base.adapter.BaseViewHolder

class OnboardingViewHolder(
    private val binding: ItemOnboardingBinding
) : BaseViewHolder<OnboardingPage>(binding.root) {
    override fun onBind(item: OnboardingPage) {
        val context = binding.root.context
        when (item) {
            OnboardingPage.First -> {
                binding.textView.text = context.getString(R.string.onboarding_page1_title)
                binding.subtextView.text = context.getString(R.string.onboarding_page1_desc)
                binding.imageView.setImageResource(R.drawable.image_onboarding1)
            }
            OnboardingPage.Second -> {
                binding.textView.text = context.getString(R.string.onboarding_page2_title)
                binding.subtextView.text = context.getString(R.string.onboarding_page2_desc)
                binding.imageView.setImageResource(R.drawable.image_onboarding2)
            }
            OnboardingPage.Third -> {
                binding.textView.text = context.getString(R.string.onboarding_page3_title)
                binding.subtextView.text = context.getString(R.string.onboarding_page3_desc)
                binding.imageView.setImageResource(R.drawable.image_onboarding3)
            }
        }
    }
}
