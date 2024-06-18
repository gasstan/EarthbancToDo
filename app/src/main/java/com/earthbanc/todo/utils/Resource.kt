package com.earthbanc.todo.utils


sealed interface Resource<T> {
  class Loading<T> : Resource<T>
  class Empty<T> : Resource<T>
  class Error<T>(val message: String) : Resource<T>
  class Success<T>(val data: T) : Resource<T>

  fun getDataOrNull() = if (this is Success) data else null
  fun getErrorOrNull() = if (this is Error) message else null
}
