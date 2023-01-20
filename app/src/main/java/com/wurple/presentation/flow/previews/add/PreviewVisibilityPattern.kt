package com.wurple.presentation.flow.previews.add

sealed class PreviewVisibilityPattern {
    object Visible : PreviewVisibilityPattern()
    object Anonymous : PreviewVisibilityPattern()
}