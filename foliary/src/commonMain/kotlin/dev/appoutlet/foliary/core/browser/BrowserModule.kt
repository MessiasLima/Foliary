package dev.appoutlet.foliary.core.browser

import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(includes = [PlatformBrowserModule::class])
@Configuration
class BrowserModule

@Module
expect object PlatformBrowserModule
