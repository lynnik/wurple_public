package com.wurple.presentation.flow.previews.temppreview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wurple.domain.dispatcher.DispatcherProvider
import com.wurple.domain.extension.empty
import com.wurple.domain.log.Logger
import com.wurple.domain.usecase.preview.GetTempPreviewUseCase
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import com.wurple.presentation.model.mapper.FromUserTempPreviewToPreviewUiOnPreviewMapper
import com.wurple.presentation.model.preview.PreviewUi
import kotlinx.coroutines.launch

class TempPreviewViewModel(
    private val getTempPreviewUseCase: GetTempPreviewUseCase,
    private val fromUserTempPreviewToPreviewUiOnPreviewMapper: FromUserTempPreviewToPreviewUiOnPreviewMapper,
    private val dispatcherProvider: DispatcherProvider,
    logger: Logger
) : BaseViewModel(logger) {
    val titleLiveData = MutableLiveData<String>()
    val previewUiLiveData = MutableLiveData<PreviewUi>()

    init {
        getTempPreview()
    }

    fun navigateBack() {
        navigateBackward()
    }

    private fun getTempPreview() {
        getTempPreviewUseCase.launch(
            param = GetTempPreviewUseCase.Params(),
            block = {
                onComplete = { userTempPreview ->
                    titleLiveData.value =
                        userTempPreview.createPreviewRequest?.title ?: String.empty
                    viewModelScope.launch(dispatcherProvider.main) {
                        previewUiLiveData.value =
                            fromUserTempPreviewToPreviewUiOnPreviewMapper.map(userTempPreview)
                    }
                }
                onError = ::handleError
                onStart = ::showProgress
                onTerminate = ::hideProgress
            }
        )
    }
}
