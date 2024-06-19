package com.earthbanc.todo.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.earthbanc.todo.R
import com.earthbanc.todo.model.TodoItem
import com.earthbanc.todo.navigation.Destinations
import com.earthbanc.todo.screen.TasksViewModel
import com.earthbanc.todo.utils.onEmpty
import com.earthbanc.todo.utils.onFailure
import com.earthbanc.todo.utils.onLoading
import com.earthbanc.todo.utils.onSuccess
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: TasksViewModel = koinViewModel()) {
  val tasks by viewModel.tasks.collectAsStateWithLifecycle()

  Scaffold(
    topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) },
    floatingActionButton = {
      FloatingActionButton(onClick = { }, shape = CircleShape) {
        Icon(Icons.Default.Add, contentDescription = "Add")
      }
    },
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    tasks
      .onSuccess { tasks ->
        ContentComposable(
          tasks = tasks,
          onCheckBoxClick = viewModel::updateTask,
          onItemClick = { todoItem -> navController.navigate(Destinations.Detail(todoItem.id.toString()).route) },
          modifier = Modifier.padding(innerPadding)
        )
      }
      .onEmpty { }
      .onLoading { }
      .onFailure { }
  }
}

@Composable
private fun ContentComposable(
  tasks: List<TodoItem>,
  onCheckBoxClick: (TodoItem) -> Unit,
  onItemClick: (TodoItem) -> Unit,
  modifier: Modifier = Modifier
) {

  LazyColumn(modifier = modifier, contentPadding = PaddingValues(horizontal = 16.dp)) {
    items(tasks) { todoItem ->
      TodoItemComposable(
        item = todoItem,
        onItemClick = onItemClick,
        onCheckBoxClick = onCheckBoxClick
      )
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
private fun TodoItemComposable(
  item: TodoItem,
  onItemClick: (TodoItem) -> Unit,
  onCheckBoxClick: (TodoItem) -> Unit
) {
  var checked by remember { mutableStateOf(item.completed) }

  Card(
    onClick = { onItemClick(item) },
    modifier = Modifier
      .fillMaxWidth()
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Checkbox(checked = checked, onCheckedChange = {
        checked = it
        onCheckBoxClick(item.copy(completed = it))
      })
      Text(text = item.title)
    }
  }
}