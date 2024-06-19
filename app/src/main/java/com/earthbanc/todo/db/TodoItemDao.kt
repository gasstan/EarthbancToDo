package com.earthbanc.todo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.earthbanc.todo.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTask(todoItem: TodoItem)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTasks(todoItems: List<TodoItem>)

  @Query("SELECT * FROM TodoItems")
  fun getAllTasksFlow(): Flow<List<TodoItem>>

  @Query("SELECT (SELECT COUNT(*) FROM TodoItems) == 0")
  fun isEmpty(): Boolean
}