package com.wurple.data.remote.user

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wurple.R
import com.wurple.data.firebase.FirebaseManager
import com.wurple.data.model.mapper.user.FromUserRemoteDataToUserMapper
import com.wurple.data.model.user.UserRemoteData
import com.wurple.domain.date.DateManager
import com.wurple.domain.education.UserEducationManager
import com.wurple.domain.experience.UserExperienceManager
import com.wurple.domain.extension.empty
import com.wurple.domain.language.UserLanguageManager
import com.wurple.domain.log.Logger
import com.wurple.domain.model.common.StatusValue
import com.wurple.domain.model.date.Date
import com.wurple.domain.model.user.*
import com.wurple.domain.skills.UserSkillsManager
import com.wurple.domain.validation.size.MaxLength
import kotlinx.coroutines.tasks.await
import java.io.File

class DefaultUserRemoteDataSource(
    private val context: Context,
    private val firebaseManager: FirebaseManager,
    private val fromUserRemoteDataToUserMapper: FromUserRemoteDataToUserMapper,
    private val dateManager: DateManager,
    private val userSkillsManager: UserSkillsManager,
    private val userExperienceManager: UserExperienceManager,
    private val userEducationManager: UserEducationManager,
    private val userLanguageManager: UserLanguageManager,
    private val logger: Logger
) : UserRemoteDataSource {
    override suspend fun getUserById(id: String): User {
        val userDocument = Firebase.firestore
            .collection(UserRemoteData.COLLECTION)
            .document(id)
            .get()
            .await()
        val userRemoteData: UserRemoteData = userDocument.toObject<UserRemoteData>()
            ?: throw IllegalArgumentException("There is no user with id: $id")
        return fromUserRemoteDataToUserMapper.map(userRemoteData)
    }

    override suspend fun isCurrentUserExist(currentUserId: String): Boolean {
        val uid = try {
            currentUserId
        } catch (throwable: Throwable) {
            logger.e(TAG, throwable)
            return false
        }
        // a user document has a name as user id
        val userDocument = Firebase.firestore
            .collection(UserRemoteData.COLLECTION)
            .document(uid)
            .get()
            .await()
        val userRemoteData: UserRemoteData? = userDocument.toObject<UserRemoteData>()
        return userRemoteData != null
    }

    override suspend fun createUser(currentUserId: String, referrerId: String?) {
        val email = firebaseManager.currentUserEmail
        val imageUrl = firebaseManager.currentUser.photoUrl?.path ?: String.empty
        val createdAt = dateManager.getCurrentStringDate()
        val userRemoteData = UserRemoteData(
            id = currentUserId,
            referrerId = referrerId ?: String.empty,
            firstName = context.getString(R.string.edit_profile_name_first_name_stub),
            lastName = context.getString(R.string.edit_profile_name_last_name_stub),
            email = email,
            imageUrl = imageUrl,
            dob = String.empty,
            locationId = String.empty,
            username = String.empty,
            bio = String.empty,
            skills = String.empty,
            experience = String.empty,
            education = String.empty,
            languages = String.empty,
            statusValue = StatusValue.Enabled.value,
            statusDescription = String.empty,
            createdAt = createdAt,
            updatedAt = createdAt
        )
        Firebase.firestore
            .collection(UserRemoteData.COLLECTION)
            .document(currentUserId)
            .set(userRemoteData)
            .await()
    }

    override suspend fun getCurrentUser(currentUserId: String): User {
        return getUserById(currentUserId)
    }

    override suspend fun updateCurrentUser(
        currentUserId: String,
        updateRequest: UserUpdateRequest
    ): User {
        val valuesToUpdate = mutableMapOf<String, Any>()
        // firstName
        updateRequest.firstName?.let {
            valuesToUpdate[UserRemoteData.FIELD_FIRST_NAME] = it
        }
        // lastName
        updateRequest.lastName?.let {
            valuesToUpdate[UserRemoteData.FIELD_LAST_NAME] = it
        }
        // imageUrl
        updateRequest.imageUrl?.let {
            if (it == UserUpdateRequest.REMOVE_IMAGE_VALUE) {
                Firebase.storage.reference
                    .child(UserRemoteData.generateImagePath(currentUserId))
                    .delete()
                    .await()
                valuesToUpdate[UserRemoteData.FIELD_IMAGE_URL] = String.empty
            } else {
                val localImageFile = File(updateRequest.imageUrl)
                val imageFileUri = Uri.fromFile(localImageFile)
                val newImageFileName = UserRemoteData.generateImagePath(currentUserId)
                val newImageUrl = Firebase.storage.reference
                    .child(newImageFileName)
                    .putFile(imageFileUri)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
                    .toString()
                valuesToUpdate[UserRemoteData.FIELD_IMAGE_URL] = newImageUrl
            }
        }
        // dob
        updateRequest.dob?.let {
            valuesToUpdate[UserRemoteData.FIELD_DOB] =
                dateManager.defaultDateInputStringDateToStringDate(it)
        }
        // locationId
        updateRequest.locationId?.let {
            valuesToUpdate[UserRemoteData.FIELD_LOCATION_ID] = it
        }
        // username
        updateRequest.username?.let {
            if (it.isEmpty()) {
                // remove username
                valuesToUpdate[UserRemoteData.FIELD_USERNAME] = it
            } else {
                val numberOfUsersWhoUseTheSameUsername: Int = Firebase.firestore
                    .collection(UserRemoteData.COLLECTION)
                    .whereEqualTo(UserRemoteData.FIELD_USERNAME, it)
                    .get()
                    .await()
                    .size()
                if (numberOfUsersWhoUseTheSameUsername > 0) {
                    throw IllegalArgumentException(
                        context.getString(R.string.common_username_already_exists)
                    )
                } else {
                    valuesToUpdate[UserRemoteData.FIELD_USERNAME] = it
                }
            }
        }
        // bio
        updateRequest.bio?.let {
            valuesToUpdate[UserRemoteData.FIELD_BIO] = it
        }
        // languages
        updateRequest.languages?.let {
            if (it.size > MaxLength.USER_LANGUAGE_LIST_SIZE) {
                throw IllegalArgumentException(
                    context.getString(
                        R.string.languages_error_can_not_create_more_than,
                        MaxLength.USER_LANGUAGE_LIST_SIZE
                    )
                )
            } else {
                valuesToUpdate[UserRemoteData.FIELD_LANGUAGES] =
                    userLanguageManager.getJsonOfLanguagesList(it)
            }
        }
        // update remote user data
        if (valuesToUpdate.isNotEmpty()) {
            valuesToUpdate[UserRemoteData.FIELD_UPDATED_AT] = dateManager.getCurrentStringDate()
            Firebase.firestore
                .collection(UserRemoteData.COLLECTION)
                .document(currentUserId)
                .update(valuesToUpdate)
                .await()
        }
        return getCurrentUser(currentUserId)
    }

    override suspend fun updateCurrentUserEmail(currentUserId: String, email: String): User {
        firebaseManager.currentUser
            .updateEmail(email)
            .await()
        Firebase.firestore
            .collection(UserRemoteData.COLLECTION)
            .document(currentUserId)
            .update(
                mapOf(
                    UserRemoteData.FIELD_EMAIL to email,
                    UserRemoteData.FIELD_UPDATED_AT to dateManager.getCurrentStringDate()
                )
            )
            .await()
        return getCurrentUser(currentUserId)
    }

    override suspend fun updateCurrentUserSkills(
        currentUserId: String,
        updateRequest: UserSkillsUpdateRequest,
        oldUserSkills: List<UserSkill>
    ): User {
        val oldUserSkillsMutableList = oldUserSkills.toMutableList()
        val oldSkills = oldUserSkillsMutableList.filter { old ->
            updateRequest.userSkills.find { new -> old.id == new.id } != null
        }
        if (updateRequest.isDelete) {
            // an existed skill will be deleted
            oldUserSkillsMutableList.removeAll(oldSkills)
        } else {
            val newSkills = updateRequest.userSkills
            // check have we already had the same skill before
            newSkills.forEach { newSkill ->
                oldUserSkillsMutableList
                    .find { it.title == newSkill.title && it.type == newSkill.type }
                    ?.let { skill ->
                        throw IllegalStateException(
                            context.getString(
                                R.string.skills_add_skill_already_added_error,
                                skill.title
                            )
                        )
                    }
            }
            if (oldSkills.isEmpty()) {
                // a new skill will be created
                val oldPersonalSkillsListSize =
                    oldUserSkillsMutableList.filter { it.type == UserSkill.Type.Personal }.size
                val oldProfessionalSkillsListSize =
                    oldUserSkillsMutableList.filter { it.type == UserSkill.Type.Professional }.size
                // all new skills have the same type
                val newSkillType = newSkills.firstOrNull()?.type
                    ?: throw IllegalStateException(
                        context.getString(R.string.input_validation_error_empty_skill)
                    )
                when {
                    newSkillType == UserSkill.Type.Personal &&
                            oldPersonalSkillsListSize + newSkills.size > MaxLength.USER_PERSONAL_SKILL_LIST_SIZE -> {
                        throw IllegalStateException(
                            context.getString(
                                R.string.skills_personal_error_can_not_create_more_than,
                                MaxLength.USER_PERSONAL_SKILL_LIST_SIZE
                            )
                        )
                    }
                    newSkillType == UserSkill.Type.Professional &&
                            oldProfessionalSkillsListSize + newSkills.size > MaxLength.USER_PROFESSIONAL_SKILL_LIST_SIZE -> {
                        throw IllegalStateException(
                            context.getString(
                                R.string.skills_professional_error_can_not_create_more_than,
                                MaxLength.USER_PROFESSIONAL_SKILL_LIST_SIZE
                            )
                        )
                    }
                }
                oldUserSkillsMutableList.addAll(newSkills)
            } else {
                // an existed skill will be updated
                oldUserSkillsMutableList.removeAll(oldSkills)
                oldUserSkillsMutableList.addAll(newSkills)
            }
        }
        val json = userSkillsManager.getJsonOfSkillsList(oldUserSkillsMutableList)
        Firebase.firestore
            .collection(UserRemoteData.COLLECTION)
            .document(currentUserId)
            .update(
                mapOf(
                    UserRemoteData.FIELD_SKILLS to json,
                    UserRemoteData.FIELD_UPDATED_AT to dateManager.getCurrentStringDate()
                )
            )
            .await()
        return getCurrentUser(currentUserId)
    }

    override suspend fun updateCurrentUserExperience(
        currentUserId: String,
        updateRequest: UserExperienceUpdateRequest,
        oldUserExperience: List<UserExperience>
    ): User {
        val oldUserExperienceMutableList = oldUserExperience.toMutableList()
        val oldExperience = oldUserExperienceMutableList.find { it.id == updateRequest.id }
        if (updateRequest.isDelete) {
            // an existed experience will be deleted
            oldUserExperienceMutableList.remove(oldExperience)
        } else {
            val newFromDate = dateManager.convertStringDateToBaseDate(
                dateManager.defaultDateInputStringDateToStringDate(
                    updateRequest.fromDate
                )
            )
            val newToDate = dateManager.convertStringDateToBaseDate(
                dateManager.defaultDateInputStringDateToStringDate(
                    updateRequest.toDate
                )
            )
            if (updateRequest.isCurrent.not() &&
                newToDate.isAfter(newFromDate).not()
            ) {
                throw IllegalStateException(
                    context.getString(R.string.input_validation_error_position_date_to_less_than_from)
                )
            }
            val newExperience = UserExperience(
                id = updateRequest.id,
                position = updateRequest.position,
                companyName = updateRequest.companyName,
                // no matter which value title has here
                title = String.empty,
                isCurrent = updateRequest.isCurrent,
                fromDate = Date(
                    baseDate = newFromDate,
                    formattedDate = dateManager.getMonthAndYearFormattedDate(newFromDate)
                ),
                toDate = Date(
                    baseDate = newToDate,
                    formattedDate = dateManager.getMonthAndYearFormattedDate(newToDate)
                ),
                fromToFormattedDateRange = null
            )
            if (oldExperience == null) {
                // a new experience will be created
                if (oldUserExperienceMutableList.size >= MaxLength.USER_EXPERIENCE_LIST_SIZE) {
                    throw IllegalStateException(
                        context.getString(
                            R.string.experience_error_can_not_create_more_than,
                            MaxLength.USER_EXPERIENCE_LIST_SIZE
                        )
                    )
                }
                oldUserExperienceMutableList.add(newExperience)
            } else {
                // an existed experience will be updated
                oldUserExperienceMutableList.remove(oldExperience)
                oldUserExperienceMutableList.add(newExperience)
            }
        }
        val json = userExperienceManager.getJsonOfExperienceList(oldUserExperienceMutableList)
        Firebase.firestore
            .collection(UserRemoteData.COLLECTION)
            .document(currentUserId)
            .update(
                mapOf(
                    UserRemoteData.FIELD_EXPERIENCE to json,
                    UserRemoteData.FIELD_UPDATED_AT to dateManager.getCurrentStringDate()
                )
            )
            .await()
        return getCurrentUser(currentUserId)
    }

    override suspend fun updateCurrentUserEducation(
        currentUserId: String,
        updateRequest: UserEducationUpdateRequest,
        oldUserEducation: List<UserEducation>
    ): User {
        val oldUserEducationMutableList = oldUserEducation.toMutableList()
        val oldEducation = oldUserEducationMutableList.find { it.id == updateRequest.id }
        if (updateRequest.isDelete) {
            // an existed education will be deleted
            oldUserEducationMutableList.remove(oldEducation)
        } else {
            val newGraduationDate = dateManager.convertStringDateToBaseDate(
                dateManager.defaultDateInputStringDateToStringDate(
                    updateRequest.graduationDate
                )
            )
            val newEducation = UserEducation(
                id = updateRequest.id,
                degree = updateRequest.degree,
                institution = updateRequest.institution,
                graduationDate = Date(
                    baseDate = newGraduationDate,
                    formattedDate = dateManager.getMonthAndYearFormattedDate(newGraduationDate)
                )
            )
            if (oldEducation == null) {
                // a new education will be created
                if (oldUserEducationMutableList.size >= MaxLength.USER_EDUCATION_LIST_SIZE) {
                    throw IllegalStateException(
                        context.getString(
                            R.string.education_error_can_not_create_more_than,
                            MaxLength.USER_EDUCATION_LIST_SIZE
                        )
                    )
                }
                oldUserEducationMutableList.add(newEducation)
            } else {
                // an existed education will be updated
                oldUserEducationMutableList.remove(oldEducation)
                oldUserEducationMutableList.add(newEducation)
            }
        }
        val json = userEducationManager.getJsonOfEducationList(oldUserEducationMutableList)
        Firebase.firestore
            .collection(UserRemoteData.COLLECTION)
            .document(currentUserId)
            .update(
                mapOf(
                    UserRemoteData.FIELD_EDUCATION to json,
                    UserRemoteData.FIELD_UPDATED_AT to dateManager.getCurrentStringDate()
                )
            )
            .await()
        return getCurrentUser(currentUserId)
    }

    companion object {
        private const val TAG = "DefaultUserRemoteDataSource"
    }
}