package com.earthbanc.todo.repository

import com.earthbanc.todo.db.EarthbancDatabase
import com.earthbanc.todo.model.TodoItem
import com.earthbanc.todo.network.TasksApi
import com.earthbanc.todo.utils.Resource
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.ServerResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TasksRepository(
  private val tasksApi: TasksApi,
  private val database: EarthbancDatabase
) {

  fun getTasks() =
    flow<Resource<List<TodoItem>>> {
      emit(Resource.Loading())
      if (database.todoItemDao().isEmpty()) {
        emitAll(getTasksFromNetwork())
      } else {
        emitAll(getTasksFromDatabase())
      }
    }.flowOn(Dispatchers.Default)

  fun getTask(id: String) = flow {
    emit(Resource.Loading())
    val item = database.todoItemDao().getTask(id)
    if (item == null) {
      emit(Resource.Empty())
    } else {
      emit(Resource.Success(item))
    }
  }

  suspend fun insertTask(todoItem: TodoItem) {
    database.todoItemDao().insertTask(todoItem)
  }

  private suspend fun getTasksFromNetwork(): Flow<Resource<List<TodoItem>>> =
    flow {
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
        database.todoItemDao().insertTasks(tasks)
        emitAll(getTasksFromDatabase())
      }
    }.flowOn(Dispatchers.Default)

  private suspend fun getTasksFromDatabase(): Flow<Resource<List<TodoItem>>> =
    withContext(Dispatchers.IO) {
      return@withContext database.todoItemDao()
        .getAllTasksFlow()
        .distinctUntilChanged()
        .map { tasks ->
          if (tasks.isEmpty()) {
            Resource.Empty()
          } else {
            Resource.Success(tasks)
          }
        }
    }
}

