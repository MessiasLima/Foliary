package dev.appoutlet.foliary.core.ui.component.datepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCard
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.create_task_due_date_clear_a11y
import foliary.foliary.generated.resources.date_picker_cancel
import foliary.foliary.generated.resources.date_picker_confirm
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

private const val EndOfDayHour = 23
private const val EndOfDayMinute = 59
private const val EndOfDaySecond = 59
private const val EndOfDayNanosecond = 999_999_999
private const val PlaceholderAlpha = 0.6f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoliaryDatePicker(
    label: String,
    placeholder: String,
    selectedDate: Instant?,
    onDateSelected: (Instant?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.toPickerMillis()
    )

    Box(modifier = modifier) {
        DatePickerCard(
            label = label,
            placeholder = placeholder,
            selectedDate = selectedDate,
            onClick = { showBottomSheet = true },
            onClear = { onDateSelected(null) }
        )
    }

    if (showBottomSheet) {
        DatePickerBottomSheet(
            datePickerState = datePickerState,
            onDismiss = { showBottomSheet = false },
            onConfirm = { onDateSelected(it.toEndOfDayInstant()) }
        )
    }
}

@Composable
private fun DatePickerCard(
    label: String,
    placeholder: String,
    selectedDate: Instant?,
    onClick: () -> Unit,
    onClear: () -> Unit,
) {
    FoliaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("FoliaryDatePicker:Card")
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = selectedDate?.let { formatDate(it) } ?: placeholder,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .testTag("FoliaryDatePicker:Value"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = datePickerValueColor(selectedDate)
                )

                if (selectedDate != null) {
                    DateClearButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = onClear
                    )
                }
            }
        }
    }
}

@Composable
private fun datePickerValueColor(selectedDate: Instant?): Color = if (selectedDate != null) {
    MaterialTheme.colorScheme.onSurface
} else {
    MaterialTheme.colorScheme.onBackground.copy(alpha = PlaceholderAlpha)
}

@Composable
private fun DateClearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.testTag("FoliaryDatePicker:ClearButton")
    ) {
        Icon(
            imageVector = Lucide.X,
            contentDescription = stringResource(Res.string.create_task_due_date_clear_a11y),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerBottomSheet(
    datePickerState: DatePickerState,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        DatePicker(state = datePickerState)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.date_picker_cancel))
            }

            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let(onConfirm)
                    onDismiss()
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(text = stringResource(Res.string.date_picker_confirm))
            }
        }
    }
}

private fun formatDate(instant: Instant): String = instant.toLocalDate().toString()

private fun Instant.toLocalDate(): LocalDate = toLocalDateTime(TimeZone.currentSystemDefault()).date

private fun Instant.toPickerMillis(): Long = toLocalDate()
    .atTime(LocalTime(0, 0))
    .toInstant(TimeZone.currentSystemDefault())
    .toEpochMilliseconds()

private fun Long.toEndOfDayInstant(): Instant {
    val localDate = Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault()).date
    return localDate.toEndOfDayInstant()
}

private fun LocalDate.toEndOfDayInstant(): Instant =
    atTime(LocalTime(EndOfDayHour, EndOfDayMinute, EndOfDaySecond, EndOfDayNanosecond))
        .toInstant(TimeZone.currentSystemDefault())
