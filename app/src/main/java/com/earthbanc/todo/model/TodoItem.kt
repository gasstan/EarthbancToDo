package com.earthbanc.todo.model

import com.earthbanc.todo.utils.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class TodoItem(
  @SerialName("createdAt")
  @Serializable(with = InstantSerializer::class)
  var createdAt: Instant,
  @SerialName("title")
  var title: String,
  @SerialName("description")
  var description: String,
  @SerialName("completed")
  var completed: Boolean,
  @SerialName("id")
  var id: String
)

