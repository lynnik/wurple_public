package com.wurple.presentation.flow.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemOnboardingBinding
import com.wurple.presentation.flow.base.adapter.BaseAdapter

class OnboardingAdapter : BaseAdapter<OnboardingPage, OnboardingViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOnboardingBinding.inflate(inflater, parent, false)
        return OnboardingViewHolder(binding)
    }

    private class DiffCallback : DiffUtil.ItemCallback<OnboardingPage>() {
        override fun areItemsTheSame(
            oldItem: OnboardingPage,
            newItem: OnboardingPage
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: OnboardingPage,
            newItem: OnboardingPage
        ): Boolean = oldItem == newItem
    }
}
