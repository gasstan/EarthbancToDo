package com.earthbanc.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.earthbanc.todo.screen.home.HomeScreen
import com.earthbanc.todo.ui.theme.EarthbancToDoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      EarthbancToDoTheme {
        HomeScreen()
      }
    }
  }
}