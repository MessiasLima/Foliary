package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Lucide
import dev.appoutlet.foliary.core.ui.component.modifier.widthInNarrow
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.error_default_message
import foliary.foliary.generated.resources.error_default_title
import foliary.foliary.generated.resources.error_default_try_again
import foliary.foliary.generated.resources.error_hide_details
import foliary.foliary.generated.resources.error_show_details
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorIndicator(
    modifier: Modifier = Modifier,
    title: String? = stringResource(Res .string.error_default_title),
    message: String? = stringResource(Res.string.error_default_message),
    stackTrace: String? = null,
    onTryAgain: (() -> Unit)? = null,
    tryAgainText: String = stringResource(Res.string.error_default_try_again),
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.widthInNarrow(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(modifier = Modifier.size(64.dp), imageVector = Lucide.CircleX, contentDescription = null)
            Spacer(Modifier.size(16.dp))
            title?.let {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }

            message?.let {
                Spacer(Modifier.size(4.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }

            onTryAgain?.let {
                Spacer(Modifier.size(16.dp))
                Button(onClick = onTryAgain) { Text(text = tryAgainText) }
            }

            stackTrace?.let {
                var showStackTrace by rememberSaveable { mutableStateOf(false) }

                OutlinedButton(onClick = {
                    showStackTrace = showStackTrace.not()
                }) {
                    AnimatedContent(targetState = showStackTrace) {
                        if (it) {
                            Text(text = stringResource(Res.string.error_hide_details))
                        } else {
                            Text(text = stringResource(Res.string.error_show_details))
                        }
                    }
                }

                Spacer(Modifier.size(16.dp))

                AnimatedVisibility(visible = showStackTrace) {
                    Card(
                        modifier = Modifier.fillMaxWidth().widthInNarrow().padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stackTrace,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}