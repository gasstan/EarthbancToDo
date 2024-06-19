package com.earthbanc.todo.screen.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.earthbanc.todo.R
import com.earthbanc.todo.model.TodoItem
import com.earthbanc.todo.screen.TasksViewModel
import com.earthbanc.todo.utils.onSuccess
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetail(
  taskId: String?,
  navController: NavHostController,
  viewModel: TasksViewModel = koinViewModel()
) {
  if (taskId == null) return
  val task by viewModel.getTask(taskId).collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
          IconButton(onClick = navController::popBackStack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Localized description"
            )
          }
        })
    },
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    task
      .onSuccess {
        ContentComposable(
          task = it,
          modifier = Modifier.padding(innerPadding)
        )
      }
  }
}

@Composable
private fun ContentComposable(task: TodoItem, modifier: Modifier = Modifier) {

}