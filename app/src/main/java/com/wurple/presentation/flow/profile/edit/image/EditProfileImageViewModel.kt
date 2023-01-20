package com.wurple.presentation.flow.profile.edit.image

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.wurple.domain.extension.empty
import com.wurple.domain.log.Logger
import com.wurple.domain.model.user.User
import com.wurple.domain.model.user.UserUpdateRequest
import com.wurple.domain.usecase.user.GetCurrentUserImageFileUseCase
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.domain.usecase.user.UpdateCurrentUserUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent

class EditProfileImageViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    private val getCurrentUserImageFileUseCase: GetCurrentUserImageFileUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val imageUrlLiveData = MutableLiveData<String>()
    val sourceAndDestinationUriLiveData = SingleLiveEvent<Pair<Uri, Uri>>()
    private var user: User? = null

    init {
        observeUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun getSourceAndDestinationUri(sourceUri: Uri) {
        getCurrentUserImageFileUseCase.launch(
            param = GetCurrentUserImageFileUseCase.Params(),
            block = {
                onComplete = { file ->
                    val destinationUri = Uri.fromFile(file)
                    sourceAndDestinationUriLiveData.value = sourceUri to destinationUri
                }
                onError = ::handleError
            }
        )
    }

    fun updateImageUrl(newImageUrl: String) {
        val userUpdateRequest = UserUpdateRequest(imageUrl = newImageUrl)
        updateCurrentUserUseCase.launch(
            param = UpdateCurrentUserUseCase.Params(userUpdateRequest),
            block = {
                onComplete = { navigateBack() }
                onError = {
                    handleError(it)
                    showError(it.message)
                }
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    this@EditProfileImageViewModel.user = user
                    imageUrlLiveData.value = user.userImage?.url ?: String.empty
                }
            }
        )
    }
}