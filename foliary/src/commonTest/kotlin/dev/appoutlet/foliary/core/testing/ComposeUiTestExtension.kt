package dev.appoutlet.foliary.core.testing

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString

/**
 * Prints the full semantics tree starting from the root node of the current compose test.
 *
 * This is a convenience helper for debugging UI tests. It should never be committed to production
 * code, since it only logs to stdout.
 *
 * @param useUnmergedTree When true, prints the unmerged semantics tree, showing children that would
 * otherwise be merged into their parents. Defaults to false.
 * @param maxDepth Maximum depth of the tree to print. Defaults to [Int.MAX_VALUE] to print the
 * entire tree.
 */
@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.printTree(useUnmergedTree: Boolean = false, maxDepth: Int = Int.MAX_VALUE) {
    onRoot(useUnmergedTree).printTree(maxDepth)
}

/**
 * Prints the semantics tree starting from this node.
 *
 * This is a convenience helper for debugging UI tests. It should never be committed to production
 * code, since it only logs to stdout.
 *
 * @param maxDepth Maximum depth of the tree to print. Defaults to [Int.MAX_VALUE] to print the
 * entire tree.
 * @return This [SemanticsNodeInteraction], allowing chained calls.
 */
fun SemanticsNodeInteraction.printTree(maxDepth: Int = Int.MAX_VALUE): SemanticsNodeInteraction {
    println(printToString(maxDepth))
    return this
}
