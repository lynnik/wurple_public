package com.wurple.domain.usecase.base

import com.wurple.domain.error.handler.ErrorHandler
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

typealias CompletionBlock<T> = CoroutineUseCase.Request<T>.() -> Unit

abstract class CoroutineUseCase<T, in Params>(
    private val coroutineContext: CoroutineContext,
    private val errorHandler: ErrorHandler
) {
    suspend fun execute(params: Params, block: CompletionBlock<T>) {
        val response = Request<T>().apply(block).also { it.onStart() }

        try {
            val result = withContext(coroutineContext) {
                executeOnBackground(params)
            }
            response.onComplete(result)
        } catch (cancellationException: CancellationException) {
            response.onCancel(cancellationException)
        } catch (throwable: Throwable) {
            response.onError(errorHandler.handleError(throwable))
        } finally {
            response.onTerminate()
        }
    }

    abstract suspend fun executeOnBackground(params: Params): T

    class Request<T> {
        var onComplete: (T) -> Unit = { }
        var onError: ((Throwable) -> Unit) = { }
        var onCancel: ((CancellationException) -> Unit) = { }
        var onStart: (() -> Unit) = { }
        var onTerminate: (() -> Unit) = { }
    }
}