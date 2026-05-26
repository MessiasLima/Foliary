import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.rememberWindowState
import dev.appoutlet.foliary.BuildKonfig
import dev.appoutlet.foliary.core.logging.getLogger
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme
import io.github.kdroidfilter.nucleus.window.material.MaterialDecoratedWindow
import io.github.kdroidfilter.nucleus.window.material.MaterialTitleBar

private val log = getLogger("Decorated Window")

private const val WindowMinimumWidth = 350
private const val WindowMinimumHeight = 900

@Composable
fun ApplicationScope.DecoratedWindow(content: @Composable () -> Unit){
    var restoreRequested by remember { mutableStateOf(false) }

    manageSingleInstance(
        onRestoreRequest = { requested ->
            restoreRequested = requested
        },
        onSecondInstance = {
            log.i { "Second instance started" }
            return@DecoratedWindow
        }
    )

    FoliaryTheme {
        MaterialDecoratedWindow(
            title = "Foliary",
            state = rememberWindowState(width = 800.dp, height = WindowMinimumHeight.dp),
            onCloseRequest = ::exitApplication,
            minimumSize = DpSize(WindowMinimumWidth.dp, WindowMinimumHeight.dp),
        ) {
            val appTitle = remember {
                buildString {
                    append("Foliary")
                    if (BuildKonfig.isDebug) append(" (debug)")
                }
            }

            MaterialTitleBar() {
                Row(
                    modifier = Modifier.align(Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = appTitle,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }

            LaunchedEffect(restoreRequested) {
                if (restoreRequested) {
                    window.toFront()
                    restoreRequested = false
                }
            }

            content()
        }
    }
}
