package com.wurple.presentation.flow.more.aboutapp

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentAboutAppBinding
import com.wurple.domain.extension.space
import com.wurple.presentation.extension.openPlayMarket
import com.wurple.presentation.extension.openWebPage
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment

class AboutAppFragment : BaseViewModelFragment<AboutAppViewModel>(
    layoutResId = R.layout.fragment_about_app
) {
    override val viewModel: AboutAppViewModel by baseViewModel()
    override val binding: FragmentAboutAppBinding by viewBinding()

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.rateAppLiveData.observe(::openRateApp)
        viewModel.privacyPolicyLiveData.observe(::openPrivacyPolicy)
        viewModel.termsAndConditionsLiveData.observe(::openTermsAndConditions)
        viewModel.appVersionNameLiveData.observe(::setupAppVersionName)
        viewModel.appPackageNameLiveData.observe(::setupAppPackageName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setBottomSystemBarInset(binding.nestedScrollView)
        setupToolbar()
        setupOnboardingTextView()
        setupRateAppTextView()
        setupPrivacyPolicyTextView()
        setupTermsAndConditionsTextView()
        setupAppVersionTextView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }

    private fun setupOnboardingTextView() {
        binding.onboardingTextView.setOnClickListener {
            viewModel.navigateOnboarding()
        }
    }

    private fun setupRateAppTextView() {
        binding.rateAppTextView.setOnClickListener {
            viewModel.navigateRateApp()
        }
    }

    private fun setupPrivacyPolicyTextView() {
        binding.privacyPolicyTextView.setOnClickListener {
            viewModel.navigatePrivacyPolicy()
        }
    }

    private fun setupTermsAndConditionsTextView() {
        /*TODO use it when terms will be created*/
        /*binding.termsAndConditionsTextView.setOnClickListener {
            viewModel.navigateTermsAndConditions()
        }*/
    }

    private fun setupAppVersionTextView() {
        binding.appVersionTextView.setOnLongClickListener { view ->
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            val appNameText = binding.appNameTextView.text.toString()
            val sauropodedAppNameText = "$appNameText${String.space}${getString(R.string.sauropod)}"
            binding.appNameTextView.text = sauropodedAppNameText
            true
        }
    }

    private fun openRateApp(packageName: String) {
        context?.openPlayMarket(binding.container, packageName)
    }

    private fun openPrivacyPolicy(url: String) {
        context?.openWebPage(binding.container, url)
    }

    private fun openTermsAndConditions(url: String) {
        context?.openWebPage(binding.container, url)
    }

    private fun setupAppVersionName(appVersionName: String) {
        binding.appVersionTextView.text = appVersionName
    }

    private fun setupAppPackageName(appPackageName: String) {
        binding.appPackageNameTextView.text = appPackageName
    }

    companion object {
        fun newInstance(): AboutAppFragment = AboutAppFragment()
    }
}