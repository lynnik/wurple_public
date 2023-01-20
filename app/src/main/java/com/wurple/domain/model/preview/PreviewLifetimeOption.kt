package com.wurple.domain.model.preview

sealed class PreviewLifetimeOption(val value: Int) {
    object Option1 : PreviewLifetimeOption(0)
    object Option2 : PreviewLifetimeOption(1)
    object Option3 : PreviewLifetimeOption(2)
    object Option4 : PreviewLifetimeOption(3)
    object Option5 : PreviewLifetimeOption(4)
    object Option6 : PreviewLifetimeOption(5)

    companion object {
        fun getByValue(value: Int): PreviewLifetimeOption {
            return when (value) {
                Option1.value -> Option1
                Option2.value -> Option2
                Option3.value -> Option3
                Option4.value -> Option4
                Option5.value -> Option5
                Option6.value -> Option6
                else -> throw IllegalArgumentException(
                    "There is no PreviewLifetimeOption with value: $value"
                )
            }
        }
    }
}