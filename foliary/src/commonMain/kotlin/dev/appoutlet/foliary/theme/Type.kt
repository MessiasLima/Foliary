package dev.appoutlet.foliary.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import foliary.foliary.generated.resources.Domine_VariableFont_wght
import foliary.foliary.generated.resources.Inter_VariableFont_opsz_wght
import foliary.foliary.generated.resources.Res
import org.jetbrains.compose.resources.Font


@Composable
fun getTypography(): Typography {
    val baseline = MaterialTheme.typography
    val displayFontFamily = getDisplayFontFamily()

    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = getBodyFontFamily(baseline.titleMedium)),
        titleSmall = baseline.titleSmall.copy(fontFamily = getBodyFontFamily(baseline.titleSmall)),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = getBodyFontFamily(baseline.bodyLarge)),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = getBodyFontFamily(baseline.bodyMedium)),
        bodySmall = baseline.bodySmall.copy(fontFamily = getBodyFontFamily(baseline.bodySmall)),
        labelLarge = baseline.labelLarge.copy(fontFamily = getBodyFontFamily(baseline.labelLarge)),
        labelMedium = baseline.labelMedium.copy(fontFamily = getBodyFontFamily(baseline.labelMedium)),
        labelSmall = baseline.labelSmall.copy(fontFamily = getBodyFontFamily(baseline.labelSmall)),
    )
}

@Composable
private fun getDisplayFontFamily() = FontFamily(
    Font(
        resource = Res.font.Domine_VariableFont_wght,
        weight = FontWeight.Bold,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(FontWeight.Bold.weight))
    ),
    Font(
        resource = Res.font.Domine_VariableFont_wght,
        weight = FontWeight.Medium,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(FontWeight.Medium.weight))
    ),
    Font(
        resource = Res.font.Domine_VariableFont_wght,
        weight = FontWeight.Normal,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(FontWeight.Normal.weight))
    ),
    Font(
        resource = Res.font.Domine_VariableFont_wght,
        weight = FontWeight.SemiBold,
        style = FontStyle.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(FontWeight.SemiBold.weight))
    )
)

@Composable
private fun getBodyFontFamily(textStyle: TextStyle) = FontFamily(
    Font(
        resource = Res.font.Inter_VariableFont_opsz_wght,
        weight = textStyle.fontWeight ?: FontWeight.Normal,
        style = textStyle.fontStyle ?: FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(textStyle.fontWeight?.weight ?: FontWeight.Normal.weight),
            FontVariation.opticalSizing(textStyle.fontSize)
        )
    )
)