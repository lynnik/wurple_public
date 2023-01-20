package com.wurple.presentation.di

import com.wurple.presentation.manager.QrManager
import com.wurple.presentation.model.mapper.FromUserPreviewToPreviewUiMapper
import com.wurple.presentation.model.mapper.FromUserTempPreviewToPreviewUiOnAddMapper
import com.wurple.presentation.model.mapper.FromUserTempPreviewToPreviewUiOnPreviewMapper
import com.wurple.presentation.model.mapper.FromUserToPreviewUiMapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val presentationModule: Module = module {
    // Mapper
    singleOf(::FromUserToPreviewUiMapper)
    singleOf(::FromUserTempPreviewToPreviewUiOnAddMapper)
    singleOf(::FromUserTempPreviewToPreviewUiOnPreviewMapper)
    singleOf(::FromUserPreviewToPreviewUiMapper)
    // Manager
    singleOf(::QrManager)
}