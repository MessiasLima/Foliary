package dev.appoutlet.foliary.core.testing

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString

@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.printTree(useUnmergedTree: Boolean = true, maxDepth: Int = Int.MAX_VALUE) {
    onRoot(useUnmergedTree).printTree(maxDepth)
}

fun SemanticsNodeInteraction.printTree(maxDepth: Int = Int.MAX_VALUE): SemanticsNodeInteraction {
    println(printToString(maxDepth))
    return this
}
