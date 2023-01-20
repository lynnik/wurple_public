package com.wurple.presentation.flow.base.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import com.wurple.presentation.flow.base.activity.BaseNavigationActivity
import com.wurple.presentation.flow.base.viewmodel.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

abstract class BaseViewModelFragment<VM : BaseViewModel>(
    @LayoutRes layoutResId: Int
) : BaseNavigationFragment(layoutResId) {
    abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()
    }

    protected inline fun <reified T : VM> baseViewModel(): Lazy<T> {
        return viewModel(parameters = { parametersOf(arguments) })
    }

    protected open fun observeViewModelState() {
        viewModel.isProgressVisibleLiveData.observe(::updateProgressState)
        viewModel.errorMessageLiveData.observe(::showErrorMessage)
        viewModel.navigationEventLiveData.observe { navigationEvent ->
            (activity as? BaseNavigationActivity)?.handleNavigationEvent(navigationEvent)
        }
    }

    private fun updateProgressState(isVisible: Boolean) {
        if (isVisible) {
            onProgressVisible()
        } else {
            onProgressGone()
        }
    }

    protected inline fun <reified T> LiveData<T>.observe(crossinline callback: (T) -> Unit) {
        observe(this@BaseViewModelFragment.viewLifecycleOwner) { callback.invoke(it) }
    }
}