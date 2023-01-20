package com.wurple.presentation.flow.profile.edit

import androidx.lifecycle.MutableLiveData
import com.wurple.domain.extension.empty
import com.wurple.domain.log.Logger
import com.wurple.domain.navigation.Screen
import com.wurple.domain.usecase.user.FetchCurrentUserUseCase
import com.wurple.domain.usecase.user.ObserveCurrentUserUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.profile.edit.adapter.EditProfileItem

class EditProfileViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    logger: Logger
) : BaseViewModel(logger) {
    val editProfileItemsLiveData = MutableLiveData<List<EditProfileItem>>()

    init {
        observeUser()
        fetchUser()
    }

    fun navigateBack() {
        navigateBackward()
    }

    fun navigateEditProfileImage() {
        navigateForwardTo(Screen.EditProfileImage)
    }

    fun navigateEditProfileName() {
        navigateForwardTo(Screen.EditProfileName)
    }

    fun navigateEditProfileDob() {
        navigateForwardTo(Screen.EditProfileDob)
    }

    fun navigateEditProfileLocation() {
        navigateForwardTo(Screen.EditProfileLocation)
    }

    fun navigateEditProfileEmail() {
        navigateForwardTo(Screen.CreateEditProfileEmailRequest)
    }

    fun navigateEditProfileUsername() {
        navigateForwardTo(Screen.EditProfileUsername)
    }

    fun navigateEditProfileBio() {
        navigateForwardTo(Screen.EditProfileBio)
    }

    fun navigateEditProfileSkills() {
        navigateForwardTo(Screen.EditProfileSkills)
    }

    fun navigateEditProfileExperience() {
        navigateForwardTo(Screen.EditProfileExperience)
    }

    fun navigateEditProfileEducation() {
        navigateForwardTo(Screen.EditProfileEducation)
    }

    fun navigateEditProfileLanguages() {
        navigateForwardTo(Screen.EditProfileLanguages)
    }

    fun fetchUser(shouldShowProgress: Boolean = true) {
        fetchCurrentUserUseCase.launch(
            param = FetchCurrentUserUseCase.Params(),
            block = {
                if (shouldShowProgress) {
                    onStart = { showProgress() }
                }
                onTerminate = { hideProgress() }
                onError = ::handleError
            }
        )
    }

    private fun observeUser() {
        observeCurrentUserUseCase.launch(
            param = ObserveCurrentUserUseCase.Params(),
            block = {
                onNext = { user ->
                    val listItems = listOf(
                        EditProfileItem.Image(imageUrl = user.userImage?.url),
                        EditProfileItem.UserFullName(
                            text = user.userName?.fullName ?: String.empty
                        ),
                        // TODO think about do we need dob in the app
                        /*EditProfileItem.UserDob(
                            text = user.userDob?.date?.formattedDate ?: String.empty
                        ),*/
                        EditProfileItem.UserLocation(
                            text = user.userLocation?.formattedLocation ?: String.empty
                        ),
                        EditProfileItem.UserEmail(text = user.userEmail),
                        EditProfileItem.UserUsername(text = user.userUsername),
                        EditProfileItem.UserBio(text = user.userBio),
                        EditProfileItem.UserSkills,
                        EditProfileItem.UserExperience,
                        EditProfileItem.UserEducation,
                        EditProfileItem.UserLanguages
                    )
                    editProfileItemsLiveData.value = listItems
                }
            }
        )
    }
}