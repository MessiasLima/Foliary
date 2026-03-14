package dev.appoutlet.foliary.feature.signin.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Lucide
import dev.appoutlet.foliary.core.ui.component.button.FoliarySecondaryButton
import dev.appoutlet.foliary.feature.signin.SignInEvent
import dev.appoutlet.foliary.feature.signin.SignInViewData
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.sign_in_magic_link_sent
import foliary.foliary.generated.resources.sign_in_magic_link_sent_expiration
import foliary.foliary.generated.resources.sign_in_magic_link_sent_message
import foliary.foliary.generated.resources.sign_in_magic_link_sent_message_2
import foliary.foliary.generated.resources.sign_in_magic_link_sent_new_email
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun MagicLinkSent(
    state: SignInViewData.MagicLinkSent,
    onEvent: (SignInEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var remainingTime by rememberSaveable { mutableStateOf(360) }
    LaunchedEffect(state) {
        snapshotFlow { remainingTime }
            .onEach { delay(1.seconds) }
            .collect {
                if (remainingTime > 0) {
                    remainingTime -= 1
                }
            }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.sign_in_magic_link_sent),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(Res.string.sign_in_magic_link_sent_message, state.email),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
            text = state.email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(Res.string.sign_in_magic_link_sent_message_2),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = stringResource(Res.string.sign_in_magic_link_sent_expiration, remainingTime),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        FoliarySecondaryButton(
            enabled = remainingTime == 0,
            onClick = {
                onEvent(SignInEvent.OnSelectNewEmail)
            },
        ) {
            Icon(imageVector = Lucide.ArrowLeft, contentDescription = null)
            Spacer(Modifier.size(16.dp))
            Text(text = stringResource(Res.string.sign_in_magic_link_sent_new_email))
        }
    }
}
