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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Apple
import com.composables.icons.lucide.ArrowRight
import com.composables.icons.lucide.Chrome
import com.composables.icons.lucide.Leaf
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.button.FoliaryPrimaryButton
import dev.appoutlet.foliary.core.ui.component.button.FoliarySecondaryButton
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCard
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.core.ui.isApplePlatform
import org.koin.compose.viewmodel.koinViewModel

private const val LogoSize = 64
private const val LogoIconSize = 32
private const val LogoCornerRadius = 20
private const val HeaderSpacer = 48
private const val ContentSpacer = 24
private const val SubtitleWidth = 280
private const val CardSpacer = 40
private const val ButtonIconSize = 20
private const val DividerSpacer = 24
private const val FormSpacer = 16
private const val FooterSpacer = 8
private const val SuccessColor = 0xFF2E7D32

@Composable
fun SignInScreen() {
    val viewModel = koinViewModel<SignInViewModel>()
    Screen(
        screenName = "SignInScreen",
        viewModelProvider = { viewModel },
        onAction = ::onAction,
    ) { viewData: SignInViewData ->
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SignInHeader()
                SignInForm(viewModel, viewData)
            }
        }
    }
}

@Composable
private fun SignInHeader() {
    Spacer(modifier = Modifier.height(HeaderSpacer.dp))

    Box(
        modifier = Modifier
            .size(LogoSize.dp)
            .clip(RoundedCornerShape(LogoCornerRadius.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Lucide.Leaf,
            contentDescription = "App Logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(LogoIconSize.dp)
        )
    }

    Spacer(modifier = Modifier.height(ContentSpacer.dp))

    Text(
        text = "Pomodoro",
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Medium
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "A calm space for your deep work. Log in to continue your focus sessions.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.width(SubtitleWidth.dp)
    )

    Spacer(modifier = Modifier.height(CardSpacer.dp))
}

@Composable
private fun SignInForm(viewModel: SignInViewModel, viewData: SignInViewData) {
    FoliaryCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SocialLoginButtons(viewModel)

            Spacer(modifier = Modifier.height(DividerSpacer.dp))

            OrDivider()

            Spacer(modifier = Modifier.height(DividerSpacer.dp))

            EmailLoginForm(viewModel, viewData)

            Spacer(modifier = Modifier.height(FooterSpacer.dp))

            Text(
                text = "No password required. We'll send you a secure link to instantly sign in.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SocialLoginButtons(viewModel: SignInViewModel) {
    FoliarySecondaryButton(
        onClick = { viewModel.onEvent(SignInEvent.OnGoogleSignInClicked) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Lucide.Chrome,
            contentDescription = null,
            modifier = Modifier.size(ButtonIconSize.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = "Continue with Google", fontSize = 15.sp)
    }

    if (isApplePlatform) {
        Spacer(modifier = Modifier.height(12.dp))
        FoliarySecondaryButton(
            onClick = { viewModel.onEvent(SignInEvent.OnAppleSignInClicked) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Lucide.Apple,
                contentDescription = null,
                modifier = Modifier.size(ButtonIconSize.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Continue with Apple", fontSize = 15.sp)
        }
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
            text = "Or",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp),
            letterSpacing = 0.5.sp
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
private fun EmailLoginForm(viewModel: SignInViewModel, viewData: SignInViewData) {
    OutlinedTextField(
        value = viewData.email,
        onValueChange = { viewModel.onEvent(SignInEvent.OnEmailChanged(it)) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "name@example.com") },
        leadingIcon = {
            Icon(
                imageVector = Lucide.Mail,
                contentDescription = null,
                modifier = Modifier.size(ButtonIconSize.dp)
            )
        },
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(FormSpacer.dp))

    FoliaryPrimaryButton(
        onClick = { viewModel.onEvent(SignInEvent.OnSendMagicLink) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Send Magic Link", fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Lucide.ArrowRight,
            contentDescription = null,
            modifier = Modifier.size(ButtonIconSize.dp)
        )
    }

    if (viewData.isMagicLinkSent) {
        Spacer(modifier = Modifier.height(FooterSpacer.dp))
        Text(
            text = "Magic link sent! Check your inbox.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(SuccessColor),
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
