package com.wurple.presentation.flow.profile.edit.languages

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileLanguagesBinding
import com.wurple.domain.model.user.UserLanguage
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.profile.edit.languages.adapter.EditProfileLanguageAdapter

class EditProfileLanguagesFragment : BaseViewModelFragment<EditProfileLanguagesViewModel>(
    layoutResId = R.layout.fragment_edit_profile_languages
) {
    override val viewModel: EditProfileLanguagesViewModel by baseViewModel()
    override val binding: FragmentEditProfileLanguagesBinding by viewBinding()
    private val adapter = EditProfileLanguageAdapter(::onItemClick)

    override fun onProgressVisible() {
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.languagesLiveData.observe(::showLanguages)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setBottomSystemBarInset(binding.recyclerView)
        setupToolbar()
        setupRecyclerView()
    }

    override fun onDestroyView() {
        binding.recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
        binding.toolbar.inflateMenu(R.menu.menu_edit_profile_language)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemAdd -> {
                    viewModel.navigateLanguagesAdd()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
    }

    private fun showLanguages(languages: List<UserLanguage>) {
        binding.emptyStateTextView.isVisible = languages.isEmpty()
        adapter.submitList(languages)
    }

    private fun onItemClick(item: UserLanguage) {
        viewModel.navigateEditUserLanguage(item.language.id, item.languageLevel.id)
    }

    companion object {
        fun newInstance(): EditProfileLanguagesFragment = EditProfileLanguagesFragment()
    }
}