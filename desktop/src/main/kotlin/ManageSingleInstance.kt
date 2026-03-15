import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ApplicationScope
import io.github.kdroidfilter.nucleus.core.runtime.DeepLinkHandler
import io.github.kdroidfilter.nucleus.core.runtime.SingleInstanceManager


@Composable
inline fun ApplicationScope.manageSingleInstance(
    crossinline onRestoreRequest: (Boolean) -> Unit,
    onSecondInstance: () -> Unit
) {
    val isSingle = remember {
        SingleInstanceManager.isSingleInstance(
            onRestoreFileCreated = {
                DeepLinkHandler.writeUriTo(this)
            },
            onRestoreRequest = {
                DeepLinkHandler.readUriFrom(this)
                onRestoreRequest(true)
            },
        )
    }

    if (!isSingle) {
        exitApplication()
        onSecondInstance()
    }
}
