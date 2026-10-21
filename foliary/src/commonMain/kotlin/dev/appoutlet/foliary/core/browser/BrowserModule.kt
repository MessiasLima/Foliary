package dev.appoutlet.foliary.core.browser

import org.koin.core.annotation.Module

@Module(includes = [PlatformBrowserModule::class])
class BrowserModule

@Module
expect object PlatformBrowserModule
