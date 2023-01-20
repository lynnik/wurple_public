package com.wurple.presentation.flow.profile.edit.image

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wurple.R
import com.wurple.databinding.FragmentEditProfileImageBinding
import com.wurple.domain.model.user.UserUpdateRequest
import com.wurple.presentation.extension.hideKeyboard
import com.wurple.presentation.extension.snackbar
import com.wurple.presentation.flow.base.fragment.BaseImagePickerViewModelFragment

class EditProfileImageFragment : BaseImagePickerViewModelFragment<EditProfileImageViewModel>(
    layoutResId = R.layout.fragment_edit_profile_image
) {
    override val viewModel: EditProfileImageViewModel by baseViewModel()
    override val binding: FragmentEditProfileImageBinding by viewBinding()

    override fun onSourceUri(uri: Uri) {
        viewModel.getSourceAndDestinationUri(uri)
    }

    override fun onPickImage(path: String) {
        viewModel.updateImageUrl(path)
    }

    override fun onProgressVisible() {
        binding.chooseImageButton.isEnabled = false
        binding.deleteImageButton.isEnabled = false
        binding.mainProgressIndicator.show()
    }

    override fun onProgressGone() {
        binding.chooseImageButton.isEnabled = true
        binding.deleteImageButton.isEnabled = true
        binding.mainProgressIndicator.hide()
    }

    override fun onShowErrorMessage(error: String) {
        binding.container.snackbar(error)
    }

    override fun observeViewModelState() {
        super.observeViewModelState()
        viewModel.imageUrlLiveData.observe(::showImageUrl)
        viewModel.sourceAndDestinationUriLiveData.observe { (sourceUri, destinationUri) ->
            cropImage(sourceUri, destinationUri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopSystemBarInset(binding.appBarLayout)
        setupToolbar()
        setupChooseImageButton()
        setupDeleteImageButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.hideKeyboard()
            viewModel.navigateBack()
        }
    }

    private fun setupChooseImageButton() {
        binding.chooseImageButton.setOnClickListener { view ->
            view.hideKeyboard()
            openMediaStoreWithPermissionsCheck()
        }
    }

    private fun setupDeleteImageButton() {
        binding.deleteImageButton.setOnClickListener { view ->
            view.hideKeyboard()
            showDeleteImageDialog()
        }
    }

    private fun showImageUrl(imageUrl: String) {
        binding.userImageImageView.load(imageUrl) {
            error(R.drawable.image_user)
        }
        binding.deleteImageButton.isVisible = imageUrl.isNotEmpty()
    }

    private fun showDeleteImageDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.edit_profile_image_delete)
            .setMessage(R.string.edit_profile_image_delete_are_you_sure)
            .setNegativeButton(R.string.common_decline) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
                viewModel.updateImageUrl(newImageUrl = UserUpdateRequest.REMOVE_IMAGE_VALUE)
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        fun newInstance(): EditProfileImageFragment = EditProfileImageFragment()
    }
}