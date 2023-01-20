package com.wurple.presentation.flow.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentMainBinding
import com.wurple.presentation.extension.withArguments
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.more.MoreFragment
import com.wurple.presentation.flow.previews.PreviewsFragment
import com.wurple.presentation.flow.you.MainChildFragment
import com.wurple.presentation.flow.you.YouFragment

class MainFragment : BaseViewModelFragment<MainViewModel>(
    layoutResId = R.layout.fragment_main
) {
    override val viewModel: MainViewModel by baseViewModel()
    override val binding: FragmentMainBinding by viewBinding()
    private val fragments = listOf(
        YouFragment.newInstance(),
        PreviewsFragment.newInstance(),
        MoreFragment.newInstance()
    )

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.toolbarStateLiveData.observe(::showToolbarState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupViewPager()
        setupBottomNavigationView()
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }

    fun showProgress() {
        binding.mainProgressIndicator.show()
    }

    fun hideProgress() {
        binding.mainProgressIndicator.hide()
    }

    fun setAppBarLayoutLiftable(isLiftable: Boolean) {
        binding.appBarLayout.setLiftable(isLiftable)
    }

    private fun setupToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.menu.findItem(R.id.itemEdit).isVisible = false
        binding.toolbar.menu.findItem(R.id.itemAdd).isVisible = false
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemEdit -> {
                    navigateEditProfile()
                    true
                }
                R.id.itemAdd -> {
                    navigatePreviewsAdd()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupViewPager() {
        with(binding.viewPager) {
            isUserInputEnabled = false
            adapter = ScreenSlidePagerAdapter(this@MainFragment)
        }
    }

    private fun setupBottomNavigationView() {
        with(binding.bottomNavigationView) {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.itemYou -> {
                        setFragment<YouFragment>()
                        viewModel.setState(MainViewModel.ToolbarState.You)
                        true
                    }
                    R.id.itemPreviews -> {
                        setFragment<PreviewsFragment>()
                        viewModel.setState(MainViewModel.ToolbarState.Previews)
                        true
                    }
                    R.id.itemMore -> {
                        setFragment<MoreFragment>()
                        viewModel.setState(MainViewModel.ToolbarState.More)
                        true
                    }
                    else -> false
                }
            }
            setOnItemReselectedListener {
                val currentFragment = fragments.getOrNull(binding.viewPager.currentItem)
                if (currentFragment is MainChildFragment) {
                    currentFragment.scrollToTop()
                }
            }
        }
    }

    private fun showToolbarState(toolbarState: MainViewModel.ToolbarState) {
        when (toolbarState) {
            MainViewModel.ToolbarState.You -> {
                binding.toolbar.title = getString(R.string.main_you)
                binding.toolbar.menu.findItem(R.id.itemEdit).isVisible = true
                binding.toolbar.menu.findItem(R.id.itemAdd).isVisible = false
            }
            MainViewModel.ToolbarState.Previews -> {
                binding.toolbar.title = getString(R.string.main_previews)
                binding.toolbar.menu.findItem(R.id.itemEdit).isVisible = false
                binding.toolbar.menu.findItem(R.id.itemAdd).isVisible = true
            }
            MainViewModel.ToolbarState.More -> {
                binding.toolbar.title = getString(R.string.main_more)
                binding.toolbar.menu.findItem(R.id.itemEdit).isVisible = false
                binding.toolbar.menu.findItem(R.id.itemAdd).isVisible = false
            }
        }
    }

    private fun navigateEditProfile() {
        val currentFragment = fragments.getOrNull(binding.viewPager.currentItem)
        if (currentFragment is YouFragment) {
            currentFragment.navigateEditProfile()
        }
    }

    private fun navigatePreviewsAdd() {
        val currentFragment = fragments.getOrNull(binding.viewPager.currentItem)
        if (currentFragment is PreviewsFragment) {
            currentFragment.navigatePreviewsAdd()
        }
    }

    private inline fun <reified F> setFragment() {
        fragments
            .find { it is F }
            ?.let { fragment ->
                val index = fragments.indexOf(fragment)
                // if there is no fragment in the list indexOf will return -1
                if (index != -1) {
                    binding.viewPager.setCurrentItem(index, false)
                }
            }
    }

    private inner class ScreenSlidePagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]
    }

    companion object {
        const val EXTRA_DEEP_LINK = "extraDeepLink"

        fun newInstance(deepLink: String?): MainFragment {
            return MainFragment().withArguments(EXTRA_DEEP_LINK to deepLink)
        }
    }
}