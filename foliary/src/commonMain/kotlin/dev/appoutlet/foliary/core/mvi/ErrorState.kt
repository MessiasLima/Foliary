package dev.appoutlet.foliary.core.mvi

data class ErrorState(
    val error: Throwable,
    val title: String? = null,
    val message: String? = null
)
