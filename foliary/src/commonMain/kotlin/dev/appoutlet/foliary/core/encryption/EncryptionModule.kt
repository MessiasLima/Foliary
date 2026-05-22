package dev.appoutlet.foliary.core.encryption

import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(includes = [PlatformEncryptionModule::class])
@Configuration
class EncryptionModule

@Module
expect object PlatformEncryptionModule
