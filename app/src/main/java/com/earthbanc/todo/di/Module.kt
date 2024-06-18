package com.earthbanc.todo.di

import com.earthbanc.todo.network.TasksApi
import com.earthbanc.todo.network.provideKtorHttpClient
import com.earthbanc.todo.repository.TasksRepository
import org.koin.dsl.module

val appModule = module {
  single { provideKtorHttpClient() }

  single { TasksApi(get()) }
  single { TasksRepository(get()) }
}