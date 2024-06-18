package com.earthbanc.todo.repository

import com.earthbanc.todo.model.TodoItem
import com.earthbanc.todo.network.TasksApi
import com.earthbanc.todo.utils.Resource
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.ServerResponseException
import kotlinx.coroutines.flow.flow

class TasksRepository(
  private val tasksApi: TasksApi
  //TODO private val database
) {

  suspend fun getTasks() = flow<Resource<List<TodoItem>>> {
    emit(Resource.Loading())
    //Todo check if data was alreadz downloaded from api
    val tasks = try {
      tasksApi.getTasks()
    } catch (e: ServerResponseException) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown exception"))
      return@flow
    } catch (e: ClientRequestException) {
      emit(Resource.Error("${e.response.status} ${e.localizedMessage ?: "Unknown exception"}"))
      return@flow
    }

    if (tasks.isEmpty()) {
      emit(Resource.Empty())
    } else {
      //Todo save tasks to database
      emit(Resource.Success(tasks))
    }
  }
}