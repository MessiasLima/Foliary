package dev.appoutlet.foliary

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@Module
@ComponentScan("dev.appoutlet.foliary")
@Configuration
class AppModule

@KoinApplication
class FoliaryKoinApplication
