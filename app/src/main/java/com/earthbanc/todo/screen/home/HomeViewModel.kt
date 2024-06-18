package com.earthbanc.todo.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthbanc.todo.model.TodoItem
import com.earthbanc.todo.repository.TasksRepository
import com.earthbanc.todo.utils.Resource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val tasksRepo: TasksRepository) : ViewModel() {

  val tasks: StateFlow<Resource<List<TodoItem>>> =
    tasksRepo.getTasks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Resource.Empty())
}