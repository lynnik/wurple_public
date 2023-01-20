package com.wurple.app.di

import com.wurple.data.di.dataModule
import com.wurple.data.di.useCaseModule
import com.wurple.presentation.di.presentationModule
import com.wurple.presentation.di.viewModelModule
import org.koin.core.module.Module

val appModule: List<Module>
    get() = listOf(
        presentationModule,
        viewModelModule,
        useCaseModule,
        dataModule
    )
