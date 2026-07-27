package dev.appoutlet.foliary.core.ui.component.datepicker

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
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
import dev.appoutlet.foliary.core.ui.component.button.FoliarySecondaryButton
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCard
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.create_task_due_date_clear_a11y
import foliary.foliary.generated.resources.general_cancel
import foliary.foliary.generated.resources.general_confirm
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoliaryDatePicker(
    label: String,
    placeholder: String,
    selectedDate: Long?,
    selectedDateDisplayText: String?,
    onDateSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier) {
        DatePickerCard(
            label = label,
            placeholder = placeholder,
            selectedDateDisplayText = selectedDateDisplayText,
            onClick = { showBottomSheet = true },
            onClear = { onDateSelected(null) }
        )
    }

    if (showBottomSheet) {
        DatePickerBottomSheet(
            selectedDateMillis = selectedDate,
            onDismiss = { showBottomSheet = false },
            onConfirm = { onDateSelected(it) }
        )
    }
}

@Composable
private fun DatePickerCard(
    label: String,
    placeholder: String,
    selectedDateDisplayText: String?,
    onClick: () -> Unit,
    onClear: () -> Unit,
) {
    val dateTextColor by animateColorAsState(
        targetValue = if (selectedDateDisplayText != null) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )

    FoliaryCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("FoliaryDatePicker:Card")
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(16.dp).weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    modifier = Modifier.testTag("FoliaryDatePicker:Value"),
                    text = selectedDateDisplayText ?: placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = dateTextColor
                )
            }


            if (selectedDateDisplayText != null) {
                DateClearButton(
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = onClear,
                )
            }
        }
    }

}

@Composable
private fun DateClearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier.testTag("FoliaryDatePicker:ClearButton"),
        onClick = onClick,
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
    selectedDateMillis: Long?,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
) {
    // TODO add min date..
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.general_cancel))
            }

            FoliarySecondaryButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let(onConfirm)
                    onDismiss()
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(text = stringResource(Res.string.general_confirm))
            }
        }
    }
}

