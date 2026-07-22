package dev.appoutlet.foliary.feature.createtask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Lucide
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.button.FoliaryPrimaryButton
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.core.ui.component.modifier.widthInCompact
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.create_task_back_content_description
import foliary.foliary.generated.resources.create_task_description_label
import foliary.foliary.generated.resources.create_task_description_placeholder
import foliary.foliary.generated.resources.create_task_save
import foliary.foliary.generated.resources.create_task_title
import foliary.foliary.generated.resources.create_task_title_label
import foliary.foliary.generated.resources.create_task_title_placeholder
import foliary.foliary.generated.resources.create_task_title_required
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateTaskScreen() {
    val viewModel = koinViewModel<CreateTaskViewModel>()

    Screen(
        screenName = "CreateTaskScreen",
        viewModelProvider = { viewModel },
        onAction = ::onAction
    ) { viewData: CreateTaskViewData ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = { viewModel.onEvent(CreateTaskEvent.OnBackClick) }
                        ) {
                            Icon(
                                imageVector = Lucide.ArrowLeft,
                                contentDescription = stringResource(Res.string.create_task_back_content_description)
                            )
                        }
                    },
                    title = { Text(text = stringResource(Res.string.create_task_title)) }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .widthInCompact(480.dp)
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    OutlinedTextField(
                        value = viewData.title,
                        onValueChange = { viewModel.onEvent(CreateTaskEvent.OnTitleChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(Res.string.create_task_title_label)) },
                        placeholder = { Text(text = stringResource(Res.string.create_task_title_placeholder)) },
                        isError = viewData.showTitleError,
                        supportingText = if (viewData.showTitleError) {
                            { Text(text = stringResource(Res.string.create_task_title_required)) }
                        } else null,
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = viewData.description,
                        onValueChange = { viewModel.onEvent(CreateTaskEvent.OnDescriptionChanged(it)) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
                        label = { Text(text = stringResource(Res.string.create_task_description_label)) },
                        placeholder = { Text(text = stringResource(Res.string.create_task_description_placeholder)) },
                        minLines = 3
                    )

                    FoliaryPrimaryButton(
                        onClick = { viewModel.onEvent(CreateTaskEvent.OnSaveClick) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewData.isSaving.not()
                    ) {
                        Text(text = stringResource(Res.string.create_task_save))
                    }
                }
            }
        }
    }
}

private fun onAction(action: CreateTaskAction, navigator: Navigator) {
    when (action) {
        CreateTaskAction.NavigateBack -> navigator.goBack()
    }
}
