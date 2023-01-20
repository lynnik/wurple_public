package com.wurple.presentation.flow.base.activity

import android.os.Bundle
import androidx.lifecycle.LiveData
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

abstract class BaseViewModelActivity<VM : BaseViewModel> : BaseNavigationActivity() {
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModelState()
    }

    protected inline fun <reified T : VM> baseViewModel(): Lazy<T> {
        return viewModel(parameters = { parametersOf(intent.extras) })
    }

    protected open fun observeViewModelState() {
        viewModel.isProgressVisibleLiveData.observe(::updateProgressState)
        viewModel.errorMessageLiveData.observe(::showErrorMessage)
        viewModel.navigationEventLiveData.observe(::handleNavigationEvent)
    }

    private fun updateProgressState(isVisible: Boolean) {
        if (isVisible) {
            onProgressVisible()
        } else {
            onProgressGone()
        }
    }

    protected inline fun <reified T> LiveData<T>.observe(crossinline callback: (T) -> Unit) {
        observe(this@BaseViewModelActivity) { callback.invoke(it) }
    }
}