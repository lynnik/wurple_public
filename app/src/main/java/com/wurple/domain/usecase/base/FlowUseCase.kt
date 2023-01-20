package com.wurple.domain.usecase.base

import com.wurple.domain.error.handler.ErrorHandler
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

typealias FlowBlock<T> = FlowUseCase.Request<T>.() -> Unit

abstract class FlowUseCase<T, in Params>(
    private val coroutineContext: CoroutineContext,
    private val errorHandler: ErrorHandler
) {
    suspend fun execute(params: Params, block: FlowBlock<T>) {
        val response = Request<T>().apply(block)

        executeOnBackground(params)
            .flowOn(coroutineContext)
            .onEach { response.onNext(it) }
            .catch { throwable ->
                if (throwable is CancellationException) {
                    response.onCancel(throwable)
                } else {
                    response.onError(errorHandler.handleError(throwable))
                }
            }
            .collect()
    }

    abstract suspend fun executeOnBackground(params: Params): Flow<T>

    class Request<T> {
        var onNext: (T) -> Unit = { }
        var onError: ((Throwable) -> Unit) = { }
        var onCancel: ((CancellationException) -> Unit) = { }
    }
}