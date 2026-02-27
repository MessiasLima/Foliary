package dev.appoutlet.foliary.feature.common

import dev.appoutlet.foliary.core.navigation.Navigation
import org.koin.core.annotation.Single

@Single
class NavigationAggregator(val navigation: List<Navigation<*>>)