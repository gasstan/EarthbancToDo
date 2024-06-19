package com.earthbanc.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.earthbanc.todo.navigation.AppNavHost
import com.earthbanc.todo.ui.theme.EarthbancToDoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      EarthbancToDoTheme {
        AppNavHost(navController = rememberNavController())
      }
    }
  }
}