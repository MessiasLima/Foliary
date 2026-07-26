package dev.appoutlet.foliary.core.testing

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import dev.appoutlet.foliary.core.ui.component.semantics.Alpha

fun isTransparent(): SemanticsMatcher = SemanticsMatcher.expectValue(Alpha, 0f)

fun SemanticsNodeInteraction.assertIsTransparent(): SemanticsNodeInteraction = assert(isTransparent())
fun SemanticsNodeInteraction.assertIsNotTransparent(): SemanticsNodeInteraction = assert(isTransparent().not())
