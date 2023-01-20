package com.wurple.presentation.flow.base.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wurple.R
import com.wurple.domain.extension.empty
import com.wurple.presentation.extension.getColorCompat
import com.wurple.presentation.extension.openAppSettings
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity

abstract class BaseImagePickerViewModelFragment<VM : BaseViewModel>(
    @LayoutRes layoutResId: Int
) : BaseViewModelFragment<VM>(layoutResId) {
    private val getContentLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onSourceUri(it) }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        when {
            isGranted -> {
                openMediaStore()
            }
            shouldShowRequestPermissionRationale(getReadMediaImagesPermission()).not() -> {
                showPermissionSettingsDialog()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            UCrop.REQUEST_CROP -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val path = UCrop.getOutput(data)?.path ?: String.empty
                    onPickImage(path)
                }
            }
        }
    }

    protected open fun onSourceUri(uri: Uri) {
        /*NOOP*/
    }

    protected open fun onPickImage(path: String) {
        /*NOOP*/
    }

    protected fun openMediaStoreWithPermissionsCheck() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                getReadMediaImagesPermission()
            ) == PackageManager.PERMISSION_GRANTED -> {
                openMediaStore()
            }
            shouldShowRequestPermissionRationale(getReadMediaImagesPermission()) -> {
                showPermissionRationalsDialog()
            }
            else -> {
                requestReadExternalStoragePermission()
            }
        }
    }

    protected fun cropImage(sourceUri: Uri, destinationUri: Uri) {
        val uCropOptions = UCrop.Options().apply {
            setCompressionQuality(CROP_COMPRESSION_QUALITY)
            setCompressionFormat(Bitmap.CompressFormat.JPEG)
            setHideBottomControls(true)
            setToolbarTitle(String.empty)
            setCropGridStrokeWidth(resources.getDimensionPixelSize(R.dimen.size_min))
            setCropGridColor(getColorCompat(R.color.md_theme_background))
            setToolbarColor(getColorCompat(R.color.md_theme_background))
            setStatusBarColor(getColorCompat(R.color.md_theme_background))
            setToolbarWidgetColor(getColorCompat(R.color.md_theme_primary))
            setRootViewBackgroundColor(getColorCompat(R.color.md_theme_background))
            setDimmedLayerColor(getColorCompat(R.color.md_theme_background))
            setCropFrameColor(getColorCompat(R.color.md_theme_primary))
            setCropGridColor(getColorCompat(R.color.md_theme_primary))
            setLogoColor(getColorCompat(R.color.md_theme_background))
            setToolbarCancelDrawable(R.drawable.ic_clear)
            setToolbarCropDrawable(R.drawable.ic_done)
            setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL)
        }
        UCrop
            .of(sourceUri, destinationUri)
            .withOptions(uCropOptions)
            .withAspectRatio(CROP_ASPECT_RATIO_VALUE, CROP_ASPECT_RATIO_VALUE)
            .withMaxResultSize(CROP_MAX_IMAGE_SIZE_IN_PIXELS, CROP_MAX_IMAGE_SIZE_IN_PIXELS)
            .start(requireContext(), this)
    }

    private fun requestReadExternalStoragePermission() {
        requestPermissionLauncher.launch(getReadMediaImagesPermission())
    }

    private fun openMediaStore() {
        // TODO check do we need the intent query at the manifest
        getContentLauncher.launch("image/*")
    }

    private fun showPermissionSettingsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.edit_profile_image_read_external_storage_permissions_settings_message)
            .setNegativeButton(R.string.common_decline) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
                openAppSettings()
                dialog.dismiss()
            }
            .show()
    }

    private fun showPermissionRationalsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.edit_profile_image_read_external_storage_permissions_rationals_message)
            .setNegativeButton(R.string.common_decline) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
                requestReadExternalStoragePermission()
                dialog.dismiss()
            }
            .show()
    }

    private fun getReadMediaImagesPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    companion object {
        private const val CROP_COMPRESSION_QUALITY = 80
        private const val CROP_ASPECT_RATIO_VALUE = 1F
        private const val CROP_MAX_IMAGE_SIZE_IN_PIXELS = 1024
    }
}