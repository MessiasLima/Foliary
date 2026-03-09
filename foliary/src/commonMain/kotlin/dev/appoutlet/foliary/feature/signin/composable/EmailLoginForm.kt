package dev.appoutlet.foliary.feature.signin.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowRight
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import dev.appoutlet.foliary.core.ui.component.button.FoliaryPrimaryButton
import dev.appoutlet.foliary.feature.signin.SignInEvent
import dev.appoutlet.foliary.feature.signin.SignInViewData
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.sign_in_email_placeholder
import foliary.foliary.generated.resources.sign_in_invalid_email
import foliary.foliary.generated.resources.sign_in_magic_link_sent
import foliary.foliary.generated.resources.sign_in_send_magic_link
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

private val EmailRegex = Regex(
    "^[A-Za-z0-9]+[A-Za-z0-9+_.-]+@[A-Za-z0-9]+[A-Za-z0-9-]+(\\.[A-Za-z0-9-]{2,})+$",
)

@Composable
fun EmailLoginForm(
    viewData: SignInViewData,
    onEvent: (SignInEvent) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var email by rememberSaveable { mutableStateOf(viewData.email) }
    val emailValid by derivedStateOf {
        EmailRegex.matches(email)
    }
    var emailErrorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(emailValid) {
        if (emailValid) {
            emailErrorMessage = null
        }
    }

    OutlinedTextField(
        value = email,
        onValueChange = {
            email = it
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = stringResource(Res.string.sign_in_email_placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = Lucide.Mail,
                contentDescription = null,
            )
        },
        shape = MaterialTheme.shapes.extraLarge,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        isError = emailErrorMessage.isNullOrBlank().not(),
        supportingText = {
            AnimatedVisibility(visible = emailErrorMessage != null) {
                Text(
                    text = emailErrorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    FoliaryPrimaryButton(
        onClick = {
            if (emailValid) {
                onEvent(SignInEvent.OnSendMagicLink(email))
            } else {
                coroutineScope.launch {
                    emailErrorMessage = getString(Res.string.sign_in_invalid_email)
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(Res.string.sign_in_send_magic_link),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Lucide.ArrowRight,
            contentDescription = null,
        )
    }

    if (viewData.isMagicLinkSent) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.sign_in_magic_link_sent),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}