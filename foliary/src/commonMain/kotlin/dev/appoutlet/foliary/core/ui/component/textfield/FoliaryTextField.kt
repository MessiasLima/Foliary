package dev.appoutlet.foliary.core.ui.component.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCard

@Composable
fun FoliaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit = {},
    placeholder: @Composable () -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    val placeholderAlpha by animateFloatAsState(targetValue = if (value.isBlank()) 1f else 0f)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
    ) { innerTextField ->
        FoliaryCard {
            Column(
                modifier = Modifier.padding(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProvideLabelStyle(content = label)

                Box {
                    innerTextField()
                    ProvidePlaceholderStyle(placeholderAlpha) {
                        placeholder()
                    }
                }
            }
        }
    }
}

@Composable
private fun ProvideLabelStyle(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onBackground,
        LocalTextStyle provides MaterialTheme.typography.labelMedium,
        content = content
    )
}

@Composable
private fun ProvidePlaceholderStyle(alpha: Float, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onBackground.copy(alpha = alpha),
        LocalTextStyle provides MaterialTheme.typography.bodyMedium,
        content = content
    )
}
