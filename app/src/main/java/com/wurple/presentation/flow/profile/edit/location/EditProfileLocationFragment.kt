package com.wurple.presentation.flow.profile.edit.location

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileLocationBinding
import com.wurple.domain.extension.empty
import com.wurple.domain.model.location.Location
import com.wurple.domain.model.location.LocationSearch
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.onActionSearch
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseViewModelFragment
import com.wurple.presentation.flow.profile.edit.location.adapter.EditProfileLocationAdapter

class EditProfileLocationFragment : BaseViewModelFragment<EditProfileLocationViewModel>(
    layoutResId = R.layout.fragment_edit_profile_location
) {
    override val viewModel: EditProfileLocationViewModel by baseViewModel()
    override val binding: FragmentEditProfileLocationBinding by viewBinding()
    private val adapter = EditProfileLocationAdapter(::onItemClick)
    private var areListItemsEnabled = false

    override fun onProgressVisible() {
        areListItemsEnabled = false
        binding.searchEditText.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        areListItemsEnabled = true
        binding.searchEditText.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.locationsLiveData.observe(::showLocations)
        viewModel.currentUserLocationLiveData.observe(::showCurrentUserLocation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setBottomSystemBarInset(binding.recyclerView)
        setupToolbar()
        setupSearchView()
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
    }

    private fun setupSearchView() {
        binding.searchEditText.onActionSearch {
            viewModel.searchForCurrentUserLocation(getSearchQuery())
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
    }

    private fun showLocations(locations: List<LocationSearch>) {
        binding.emptyStateTextView.isVisible = locations.isEmpty()
        adapter.submitList(locations)
    }

    private fun showCurrentUserLocation(location: Location) {
        binding.searchEditText.setText(location.city)
    }

    private fun onItemClick(item: LocationSearch) {
        binding.container.hideKeyboard()
        if (areListItemsEnabled) {
            viewModel.updateLocation(item.id)
        }
    }

    private fun getSearchQuery(): String {
        return binding.searchEditText.text
            ?.toString()
            ?.trim()
            ?: String.empty
    }

    companion object {
        fun newInstance(): EditProfileLocationFragment = EditProfileLocationFragment()
    }
}