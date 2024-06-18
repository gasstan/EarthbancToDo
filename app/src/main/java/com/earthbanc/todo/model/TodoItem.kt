package com.earthbanc.todo.model

import kotlinx.serialization.SerialName
import java.time.LocalDateTime

data class TodoItem(
  @SerialName("createdAt")
  var createdAt: LocalDateTime,
  @SerialName("title")
  var title: String,
  @SerialName("description")
  var description: String,
  @SerialName("completed")
  var completed: Boolean,
  @SerialName("id")
  var id: String
)

