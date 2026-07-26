package dev.appoutlet.foliary.core.ui.component.datepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
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
        FoliaryCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("FoliaryDatePicker:Card")
                .clickable { showBottomSheet = true }
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
                        color = if (selectedDate != null) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = PlaceholderAlpha)
                        }
                    )

                    if (selectedDate != null) {
                        IconButton(
                            onClick = { onDateSelected(null) },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .testTag("FoliaryDatePicker:ClearButton")
                        ) {
                            Icon(
                                imageVector = Lucide.X,
                                contentDescription = stringResource(Res.string.create_task_due_date_clear_a11y),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            DatePicker(state = datePickerState)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                TextButton(
                    onClick = { showBottomSheet = false }
                ) {
                    Text(text = stringResource(Res.string.date_picker_cancel))
                }

                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDateSelected(millis.toEndOfDayInstant())
                        }
                        showBottomSheet = false
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text(text = stringResource(Res.string.date_picker_confirm))
                }
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
    atTime(LocalTime(23, 59, 59, 999_999_999))
        .toInstant(TimeZone.currentSystemDefault())
