package dev.appoutlet.foliary.feature.createtask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import foliary.foliary.generated.resources.create_task_description_placeholder
import foliary.foliary.generated.resources.create_task_save
import foliary.foliary.generated.resources.create_task_title
import foliary.foliary.generated.resources.create_task_title_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateTaskScreen(viewData: CreateTaskViewData, onEvent: (CreateTaskEvent) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                modifier = Modifier.padding(top = getWindowDecorationPadding()),
                navigationIcon = {
                    FoliaryBackIconButton(onClick = { onEvent(CreateTaskEvent.OnBackClick) })
                },
                title = {
                    Text(
                        text = stringResource(Res.string.create_task_title),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )

            FoliaryTextField(
                modifier = Modifier.widthInCompact().fillMaxWidth().padding(horizontal = 16.dp),
                value = viewData.title,
                onValueChange = { onEvent(CreateTaskEvent.OnTitleChanged(it)) },
                placeholder = { Text(text = stringResource(Res.string.create_task_title_placeholder)) },
                singleLine = true
            )

            FoliaryTextField(
                modifier = Modifier.widthInCompact().fillMaxWidth().padding(horizontal = 16.dp),
                value = viewData.description,
                onValueChange = { onEvent(CreateTaskEvent.OnDescriptionChanged(it)) },
                placeholder = { Text(text = stringResource(Res.string.create_task_description_placeholder)) },
                minLines = 3
            )
        }

        FoliaryPrimaryButton(
            modifier = Modifier.widthInCompact()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            onClick = { onEvent(CreateTaskEvent.OnSaveClick) },
            enabled = viewData.isSaving.not(),
            content = { Text(text = stringResource(Res.string.create_task_save)) }
        )
    }
}
