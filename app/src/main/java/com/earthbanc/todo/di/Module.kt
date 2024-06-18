package com.earthbanc.todo.di

import com.earthbanc.todo.network.TasksApi
import com.earthbanc.todo.network.provideKtorHttpClient
import com.earthbanc.todo.repository.TasksRepository
import com.earthbanc.todo.screen.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
  single { provideKtorHttpClient() }

  single { TasksApi(get()) }
  single { TasksRepository(get()) }

  viewModel { HomeViewModel(get()) }
}