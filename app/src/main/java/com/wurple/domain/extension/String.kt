package com.wurple.domain.extension

val String.Companion.empty: String
    get() = ""

val String.Companion.space: String
    get() = " "

val String.Companion.comma: String
    get() = ","

fun String.Companion.random(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (0 until length)
        .map { allowedChars.random() }
        .joinToString(String.empty)
}