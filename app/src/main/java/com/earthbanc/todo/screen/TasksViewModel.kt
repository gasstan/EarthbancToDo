package com.earthbanc.todo.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthbanc.todo.model.TodoItem
import com.earthbanc.todo.repository.TasksRepository
import com.earthbanc.todo.utils.Resource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TasksViewModel(private val tasksRepo: TasksRepository) : ViewModel() {

  val tasks: StateFlow<Resource<List<TodoItem>>> =
    tasksRepo.getTasks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Resource.Empty())

  fun getTask(id: String) = tasksRepo.getTask(id)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Resource.Empty())

  fun updateTask(todoItem: TodoItem) {
    Log.e("HomeViewModel", "updating task")
    addNewTask(todoItem)
  }

  fun addNewTask(todoItem: TodoItem) {
    viewModelScope.launch { tasksRepo.insertTask(todoItem) }
  }
}