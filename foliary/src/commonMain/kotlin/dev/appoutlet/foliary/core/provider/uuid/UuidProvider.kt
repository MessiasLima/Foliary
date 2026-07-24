package dev.appoutlet.foliary.core.provider.uuid

import org.koin.core.annotation.Factory
import kotlin.uuid.Uuid

interface UuidProvider {
    fun random(): Uuid
}

@Factory
class DefaultUuidProvider : UuidProvider {
    override fun random() = Uuid.random()
}
