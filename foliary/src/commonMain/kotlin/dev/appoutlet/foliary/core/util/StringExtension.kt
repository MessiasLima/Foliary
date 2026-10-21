package dev.appoutlet.foliary.core.util

fun String.toDomainDisplayText(): String {
    return substringAfter("://", this)
        .substringBefore('/')
        .substringBefore('?')
        .substringBefore('#')
}
