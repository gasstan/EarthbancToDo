package com.earthbanc.todo.screen.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.earthbanc.todo.R
import com.earthbanc.todo.model.TodoItem
import com.earthbanc.todo.navigation.Destinations
import com.earthbanc.todo.screen.TasksViewModel
import com.earthbanc.todo.utils.onSuccess
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: TasksViewModel = koinViewModel()) {
  val tasks by viewModel.tasks.collectAsStateWithLifecycle()
  var showBottomSheet by remember { mutableStateOf(false) }

  var openDeleteItemDialog by remember { mutableStateOf(false) }
  var taskToDelete: TodoItem? by remember { mutableStateOf(null) }

  if (openDeleteItemDialog) {
    DeleteItemDialog(
      onDismissRequest = { openDeleteItemDialog = false },
      onConfirmation = {
        openDeleteItemDialog = false
        taskToDelete?.let { viewModel.deleteTask(it) }
        taskToDelete = null
      },
      dialogTitle = "Delete Item",
      dialogText = "Do you really want to delete this item?",
      icon = Icons.Default.Delete
    )
  }

  Scaffold(
    topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showBottomSheet = true },
        shape = CircleShape
      ) {
        Icon(
          Icons.Default.Add,
          contentDescription = "Add"
        )
      }
    },
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    if (showBottomSheet) {
      AddNewTaskBottomSheet(
        onAddNewTask = viewModel::addNewTask,
        onDismiss = { showBottomSheet = false }
      )
    }
    tasks
      .onSuccess { tasks ->
        ContentComposable(
          tasks = tasks,
          onCheckBoxClick = viewModel::updateTask,
          onItemClick = { todoItem -> navController.navigate(Destinations.Detail(todoItem.id.toString()).route) },
          onDeleteTask = {
            taskToDelete = it
            openDeleteItemDialog = true
          },
          modifier = Modifier.padding(innerPadding)
        )
      }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContentComposable(
  tasks: List<TodoItem>,
  onCheckBoxClick: (TodoItem) -> Unit,
  onItemClick: (TodoItem) -> Unit,
  onDeleteTask: (TodoItem) -> Unit,
  modifier: Modifier = Modifier
) {

  LazyColumn(
    modifier = modifier.animateContentSize(),
    contentPadding = PaddingValues(horizontal = 16.dp)
  ) {
    items(tasks, key = { it.id }) { task ->
      SwipeToDelete(
        modifier = Modifier.animateItemPlacement(),
        onDelete = { onDeleteTask(task) }
      ) {
        TodoItemComposable(
          item = task,
          onItemClick = onItemClick,
          onCheckBoxClick = onCheckBoxClick
        )
      }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDelete(
  modifier: Modifier = Modifier,
  onDelete: () -> Unit,
  content: @Composable () -> Unit,
) {
  val swipeState = rememberSwipeToDismissBoxState()

  val alignment = when (swipeState.dismissDirection) {
    SwipeToDismissBoxValue.StartToEnd -> {
      Alignment.CenterStart
    }

    else -> {
      Alignment.CenterEnd
    }
  }

  SwipeToDismissBox(
    state = swipeState,
    backgroundContent = {
      Box(
        contentAlignment = alignment,
        modifier = Modifier
          .fillMaxSize()
      ) {
        Icon(
          modifier = Modifier.minimumInteractiveComponentSize(),
          tint = Color.Red,
          imageVector = Icons.Outlined.Delete, contentDescription = null
        )
      }
    },
    modifier = modifier
  ) {
    content()
  }

  LaunchedEffect(swipeState.currentValue) {
    when (swipeState.currentValue) {
      SwipeToDismissBoxValue.Settled -> {}
      else -> {
        onDelete()
        swipeState.reset()
      }
    }
  }
}
