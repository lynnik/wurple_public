package com.wurple.presentation.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

fun EditText.onActionDone(action: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            action()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun EditText.onActionSearch(action: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            action()
            return@setOnEditorActionListener true
        }
        false
    }
}

fun EditText.onTextChanged(): Flow<String> {
    val flow = callbackFlow {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                /*NOOP*/
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                trySend(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                /*NOOP*/
            }
        }
        addTextChangedListener(textWatcher)
        awaitClose { removeTextChangedListener(textWatcher) }
    }
    return flow.conflate()
}

fun EditText.onBaseDateTextChanged(): Flow<String> {
    val dateDivider = "/"
    val flow = callbackFlow {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                /*NOOP*/
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /*NOOP*/
            }

            override fun afterTextChanged(editable: Editable?) {
                // Format user input to appropriate format:
                // from 121993 to 12/1993
                editable ?: return
                val filters = editable.filters
                editable.filters = arrayOf()
                val stringBuilder = StringBuilder(editable.toString().replace(dateDivider, ""))
                if (stringBuilder.length > 2 && stringBuilder[2].toString() != dateDivider) {
                    stringBuilder.insert(2, dateDivider)
                }
                this@onBaseDateTextChanged.removeTextChangedListener(this)
                editable.replace(0, editable.length, stringBuilder.toString())
                this@onBaseDateTextChanged.addTextChangedListener(this)
                editable.filters = filters
                trySend(editable.toString())
            }
        }
        addTextChangedListener(textWatcher)
        awaitClose { removeTextChangedListener(textWatcher) }
    }
    return flow.conflate()
}
