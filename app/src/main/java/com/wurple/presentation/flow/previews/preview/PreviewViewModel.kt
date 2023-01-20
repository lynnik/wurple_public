package com.wurple.presentation.flow.previews.preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wurple.domain.date.DateManager
import com.wurple.domain.dispatcher.DispatcherProvider
import com.wurple.domain.log.Logger
import com.wurple.domain.usecase.preview.GetPreviewUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.flow.base.viewmodel.SingleLiveEvent
import com.wurple.presentation.model.mapper.FromUserPreviewToPreviewUiMapper
import com.wurple.presentation.model.preview.PreviewUi
import kotlinx.coroutines.launch

class PreviewViewModel(
    private val previewId: String,
    private val dateManager: DateManager,
    private val getPreviewUseCase: GetPreviewUseCase,
    private val fromUserPreviewToPreviewUiMapper: FromUserPreviewToPreviewUiMapper,
    private val dispatcherProvider: DispatcherProvider,
    logger: Logger
) : BaseViewModel(logger) {
    val emptyStateLiveData = SingleLiveEvent<EmptyState>()
    val previewExpDateLiveData = MutableLiveData<PreviewExpDate>()
    val previewUiLiveData = MutableLiveData<PreviewUi>()

    init {
        getPreview()
    }

    fun navigateBack() {
        navigateBackward()
    }

    private fun getPreview() {
        getPreviewUseCase.launch(
            param = GetPreviewUseCase.Params(id = previewId),
            block = {
                onComplete = { userPreview ->
                    when {
                        userPreview == null -> {
                            emptyStateLiveData.value = EmptyState.Removed
                        }
                        userPreview.preview.isExpired -> {
                            emptyStateLiveData.value = EmptyState.Expired
                        }
                        else -> {
                            previewExpDateLiveData.value = PreviewExpDate(
                                formattedExpDate = userPreview.preview.expDate.formattedDate,
                                differenceBetweenCurrentDateAndExpDate = dateManager.getDifferenceFromCurrentDateToDateInDays(
                                    userPreview.preview.expDate.baseDate
                                )
                            )
                            viewModelScope.launch(dispatcherProvider.main) {
                                val previewUi = fromUserPreviewToPreviewUiMapper.map(userPreview)
                                previewUiLiveData.value = previewUi
                            }
                        }
                    }
                }
                onError = ::handleError
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }

    sealed class EmptyState {
        object Removed : EmptyState()
        object Expired : EmptyState()
    }

    data class PreviewExpDate(
        val formattedExpDate: String,
        val differenceBetweenCurrentDateAndExpDate: Int
    )
}