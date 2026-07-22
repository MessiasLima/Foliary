package dev.appoutlet.foliary.feature.createtask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.component.button.FoliaryBackIconButton
import dev.appoutlet.foliary.core.ui.component.button.FoliaryPrimaryButton
import dev.appoutlet.foliary.core.ui.component.modifier.widthInCompact
import dev.appoutlet.foliary.core.ui.component.textfield.FoliaryTextField
import dev.appoutlet.foliary.feature.main.getWindowDecorationPadding
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.create_task_description_label
import foliary.foliary.generated.resources.create_task_description_placeholder
import foliary.foliary.generated.resources.create_task_save
import foliary.foliary.generated.resources.create_task_title
import foliary.foliary.generated.resources.create_task_title_label
import foliary.foliary.generated.resources.create_task_title_placeholder
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CreateTaskScreen(viewData: CreateTaskViewData, onEvent: (CreateTaskEvent) -> Unit) {
    Scaffold(
        topBar = { CreateTaskTopBar(onEvent) }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.size(16.dp))
                TitleField(viewData.title, onEvent)
                DescriptionField(viewData.description, onEvent)
                Spacer(Modifier.size(64.dp))
            }

            FoliaryPrimaryButton(
                modifier = Modifier.widthInCompact()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                onClick = { onEvent(CreateTaskEvent.SaveClicked) },
                enabled = viewData.saveButtonEnabled,
                content = { Text(text = stringResource(Res.string.create_task_save)) }
            )
        }
    }
}

@Composable
private fun CreateTaskTopBar(onEvent: (CreateTaskEvent) -> Unit) {
    TopAppBar(
        modifier = Modifier.padding(top = getWindowDecorationPadding()),
        navigationIcon = {
            FoliaryBackIconButton(onClick = { onEvent(CreateTaskEvent.BackClicked) })
        },
        title = {
            Text(
                text = stringResource(Res.string.create_task_title),
                color = MaterialTheme.colorScheme.primary
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
    )
}

@OptIn(FlowPreview::class)
@Composable
private fun TitleField(title: String, onEvent: (CreateTaskEvent) -> Unit) {
    var value by rememberSaveable { mutableStateOf(title) }

    LaunchedEffect(Unit) {
        snapshotFlow { value }
            .debounce(500.milliseconds)
            .collect { onEvent(CreateTaskEvent.TitleChanged(it)) }
    }

    FoliaryTextField(
        modifier = Modifier.widthInCompact().fillMaxWidth().padding(horizontal = 16.dp),
        value = value,
        onValueChange = { value = it },
        label = { Text(text = stringResource(Res.string.create_task_title_label)) },
        placeholder = { Text(text = stringResource(Res.string.create_task_title_placeholder)) },
        singleLine = true
    )
}

@OptIn(FlowPreview::class)
@Composable
private fun DescriptionField(description: String?, onEvent: (CreateTaskEvent) -> Unit) {
    var value by rememberSaveable { mutableStateOf(description ?: "") }

    LaunchedEffect(Unit) {
        snapshotFlow { value }
            .debounce(500.milliseconds)
            .collect {
                val valueToEmit = it.ifBlank { null }
                onEvent(CreateTaskEvent.DescriptionChanged(valueToEmit))
            }
    }

    FoliaryTextField(
        modifier = Modifier.widthInCompact().fillMaxWidth().padding(horizontal = 16.dp),
        value = value,
        onValueChange = { value = it },
        label = { Text(text = stringResource(Res.string.create_task_description_label)) },
        placeholder = { Text(text = stringResource(Res.string.create_task_description_placeholder)) },
        minLines = 3
    )
}

