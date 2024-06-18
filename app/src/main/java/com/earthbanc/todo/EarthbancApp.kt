package com.earthbanc.todo

import android.app.Application
import com.earthbanc.todo.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EarthbancApp : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@EarthbancApp)
      modules(appModule)
    }
  }
}