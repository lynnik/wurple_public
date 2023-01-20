package com.wurple.data.remote.preview

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.wurple.data.model.mapper.preview.FromPreviewRemoteDataToPreviewMapper
import com.wurple.data.model.preview.PreviewRemoteData
import com.wurple.domain.date.DateManager
import com.wurple.domain.extension.empty
import com.wurple.domain.log.Logger
import com.wurple.domain.model.common.StatusValue
import com.wurple.domain.model.preview.CreatePreviewRequest
import com.wurple.domain.model.preview.Preview
import com.wurple.domain.model.preview.PreviewLifetimeOption
import kotlinx.coroutines.tasks.await

class DefaultPreviewRemoteDataSource(
    private val dateManager: DateManager,
    private val fromPreviewRemoteDataToPreviewMapper: FromPreviewRemoteDataToPreviewMapper,
    private val logger: Logger
) : PreviewRemoteDataSource {
    override suspend fun getPreviewById(id: String): Preview? {
        val previewRemoteData: PreviewRemoteData? = try {
            val data = hashMapOf(PREVIEW_ID_FIREBASE_CLOUD_FUNCTION_PARAM to id)
            val previewTask = Firebase.functions
                .getHttpsCallable(PREVIEW_FIREBASE_CLOUD_FUNCTION_NAME)
                .call(data)
                .await()
            val previewJson: String = previewTask?.data.toString()
            Gson().fromJson(previewJson, PreviewRemoteData::class.java)
        } catch (throwable: Throwable) {
            logger.e(TAG, throwable)
            null
        }
        return previewRemoteData?.let { fromPreviewRemoteDataToPreviewMapper.map(it) }
    }

    override suspend fun getPreviews(currentUserId: String): List<Preview> {
        val data = hashMapOf(PREVIEW_UID_FIREBASE_CLOUD_FUNCTION_PARAM to currentUserId)
        val previewsTask = Firebase.functions
            .getHttpsCallable(PREVIEWS_BY_UID_FIREBASE_CLOUD_FUNCTION_NAME)
            .call(data)
            .await()
        val previewJson: String = previewsTask?.data.toString()
        val previewRemoteDataList: List<PreviewRemoteData> = try {
            val list = object : TypeToken<List<PreviewRemoteData>>() {}.type
            Gson().fromJson<List<PreviewRemoteData>>(previewJson, list) ?: listOf()
        } catch (exception: JsonSyntaxException) {
            logger.e(TAG, exception)
            listOf()
        }
        return previewRemoteDataList.map { fromPreviewRemoteDataToPreviewMapper.map(it) }
    }

    override suspend fun createPreview(
        currentUserId: String,
        createPreviewRequest: CreatePreviewRequest
    ) {
        val id = PreviewRemoteData.generatePreviewId()
        val expDate = getExpDateByPreviewLifetimeOption(createPreviewRequest.previewLifetimeOption)
        val createdAt = dateManager.getCurrentStringDate()
        val previewRemoteData = PreviewRemoteData(
            id = id,
            title = createPreviewRequest.title,
            previewLifetimeOption = createPreviewRequest.previewLifetimeOption.value,
            expDate = expDate,
            userId = currentUserId,
            userImageVisibility = createPreviewRequest.userImageVisibility.value,
            userNameVisibility = createPreviewRequest.userNameVisibility.value,
            userCurrentPositionVisibility = createPreviewRequest.userCurrentPositionVisibility.value,
            userLocationVisibility = createPreviewRequest.userLocationVisibility.value,
            userEmailVisibility = createPreviewRequest.userEmailVisibility.value,
            userUsernameVisibility = createPreviewRequest.userUsernameVisibility.value,
            userBioVisibility = createPreviewRequest.userBioVisibility.value,
            userSkillsVisibility = createPreviewRequest.userSkillsVisibility.value,
            userExperienceVisibility = createPreviewRequest.userExperienceVisibility.value,
            userEducationVisibility = createPreviewRequest.userEducationVisibility.value,
            userLanguagesVisibility = createPreviewRequest.userLanguagesVisibility.value,
            statusValue = StatusValue.Enabled.value,
            statusDescription = String.empty,
            createdAt = createdAt,
            updatedAt = createdAt
        )
        Firebase.firestore
            .collection(PreviewRemoteData.COLLECTION)
            .document(id)
            .set(previewRemoteData)
            .await()
    }

    override suspend fun updatePreview(
        id: String,
        updatePreviewRequest: CreatePreviewRequest
    ) {
        val valuesToUpdate = mutableMapOf<String, Any>()
        // title
        valuesToUpdate[PreviewRemoteData.FIELD_TITLE] = updatePreviewRequest.title
        // lifetime option
        valuesToUpdate[PreviewRemoteData.FIELD_PREVIEW_LIFETIME_OPTION] =
            updatePreviewRequest.previewLifetimeOption.value
        // exp date
        valuesToUpdate[PreviewRemoteData.FIELD_EXP_DATE] =
            getExpDateByPreviewLifetimeOption(updatePreviewRequest.previewLifetimeOption)
        // image visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_IMAGE_VISIBILITY] =
            updatePreviewRequest.userImageVisibility.value
        // name visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_NAME_VISIBILITY] =
            updatePreviewRequest.userNameVisibility.value
        // current position visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_CURRENT_POSITION_VISIBILITY] =
            updatePreviewRequest.userCurrentPositionVisibility.value
        // location visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_LOCATION_VISIBILITY] =
            updatePreviewRequest.userLocationVisibility.value
        // email visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_EMAIL_VISIBILITY] =
            updatePreviewRequest.userEmailVisibility.value
        // username visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_USERNAME_VISIBILITY] =
            updatePreviewRequest.userUsernameVisibility.value
        // bio visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_BIO_VISIBILITY] =
            updatePreviewRequest.userBioVisibility.value
        // skills visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_SKILLS_VISIBILITY] =
            updatePreviewRequest.userSkillsVisibility.value
        // experience visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_EXPERIENCE_VISIBILITY] =
            updatePreviewRequest.userExperienceVisibility.value
        // education visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_EDUCATION_VISIBILITY] =
            updatePreviewRequest.userEducationVisibility.value
        // languages visibility
        valuesToUpdate[PreviewRemoteData.FIELD_USER_LANGUAGES_VISIBILITY] =
            updatePreviewRequest.userLanguagesVisibility.value
        // updated at visibility
        valuesToUpdate[PreviewRemoteData.FIELD_UPDATED_AT] = dateManager.getCurrentStringDate()
        Firebase.firestore
            .collection(PreviewRemoteData.COLLECTION)
            .document(id)
            .update(valuesToUpdate)
            .await()
    }

    override suspend fun deletePreview(id: String) {
        Firebase.firestore
            .collection(PreviewRemoteData.COLLECTION)
            .document(id)
            .delete()
            .await()
    }

    private fun getExpDateByPreviewLifetimeOption(option: PreviewLifetimeOption): String {
        return when (option) {
            PreviewLifetimeOption.Option1 -> dateManager.getCurrentStringDatePlusDays(1)
            PreviewLifetimeOption.Option2 -> dateManager.getCurrentStringDatePlusDays(3)
            PreviewLifetimeOption.Option3 -> dateManager.getCurrentStringDatePlusDays(7)
            PreviewLifetimeOption.Option4 -> dateManager.getCurrentStringDatePlusDays(14)
            PreviewLifetimeOption.Option5 -> dateManager.getCurrentStringDatePlusMonths(1)
            PreviewLifetimeOption.Option6 -> dateManager.getCurrentStringDatePlusMonths(3)
        }
    }

    companion object {
        private const val TAG = "DefaultPreviewRemoteDataSource"
        private const val PREVIEW_FIREBASE_CLOUD_FUNCTION_NAME = "getPreview"
        private const val PREVIEWS_BY_UID_FIREBASE_CLOUD_FUNCTION_NAME = "getPreviewsByUid"
        private const val PREVIEW_ID_FIREBASE_CLOUD_FUNCTION_PARAM = "id"
        private const val PREVIEW_UID_FIREBASE_CLOUD_FUNCTION_PARAM = "uid"
    }
}