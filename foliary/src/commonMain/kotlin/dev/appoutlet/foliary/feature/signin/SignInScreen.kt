package dev.appoutlet.foliary.feature.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Apple
import com.composables.icons.lucide.ArrowRight
import com.composables.icons.lucide.Chromium
import com.composables.icons.lucide.Leaf
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.button.FoliaryPrimaryButton
import dev.appoutlet.foliary.core.ui.component.button.FoliarySecondaryButton
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCard
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.core.ui.component.modifier.widthInNarrow
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.ic_foliary
import foliary.foliary.generated.resources.sign_in_app_logo_description
import foliary.foliary.generated.resources.sign_in_continue_with_apple
import foliary.foliary.generated.resources.sign_in_continue_with_google
import foliary.foliary.generated.resources.sign_in_email_placeholder
import foliary.foliary.generated.resources.sign_in_helper_text
import foliary.foliary.generated.resources.sign_in_magic_link_sent
import foliary.foliary.generated.resources.sign_in_or_divider
import foliary.foliary.generated.resources.sign_in_send_magic_link
import foliary.foliary.generated.resources.sign_in_subtitle
import foliary.foliary.generated.resources.sign_in_title
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen() {
    val viewModel = koinViewModel<SignInViewModel>()
    Screen(
        screenName = "SignInScreen",
        viewModelProvider = { viewModel },
        onAction = ::onAction,
    ) { viewData: SignInViewData ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                Column(
                    modifier = Modifier.widthInNarrow().align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SignInHeader()
                    SignInForm(
                        viewData = viewData,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun SignInHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                painter = painterResource(Res.drawable.ic_foliary),
                contentDescription = stringResource(Res.string.sign_in_app_logo_description),
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.sign_in_title),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(Res.string.sign_in_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SignInForm(
    viewData: SignInViewData,
    onEvent: (SignInEvent) -> Unit
) {
    FoliaryCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SocialLoginButtons(onEvent = onEvent)

            Spacer(modifier = Modifier.height(24.dp))

            OrDivider()

            Spacer(modifier = Modifier.height(24.dp))

            EmailLoginForm(
                viewData = viewData,
                onEvent = onEvent
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.sign_in_helper_text),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SocialLoginButtons(onEvent: (SignInEvent) -> Unit) {
    FoliarySecondaryButton(
        onClick = { onEvent(SignInEvent.OnGoogleSignInClick) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Lucide.Chromium,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(Res.string.sign_in_continue_with_google),
            style = MaterialTheme.typography.bodyMedium
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    FoliarySecondaryButton(
        onClick = { onEvent(SignInEvent.OnAppleSignInClick) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Lucide.Apple,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(Res.string.sign_in_continue_with_apple),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun OrDivider() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        Text(
            text = stringResource(Res.string.sign_in_or_divider),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
private fun EmailLoginForm(
    viewData: SignInViewData,
    onEvent: (SignInEvent) -> Unit
) {
    OutlinedTextField(
        value = viewData.email,
        onValueChange = { onEvent(SignInEvent.OnEmailChanged(it)) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = stringResource(Res.string.sign_in_email_placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = Lucide.Mail,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    FoliaryPrimaryButton(
        onClick = { onEvent(SignInEvent.OnSendMagicLink) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(Res.string.sign_in_send_magic_link),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Lucide.ArrowRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
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

@Suppress("UnusedParameter")
private fun onAction(action: SignInAction, navigator: Navigator) {
    when (action) {
        SignInAction.NavigateToMain -> {
            // Navigate to main screen once implemented
        }
    }
}
