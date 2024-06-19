package com.earthbanc.todo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.earthbanc.todo.model.TodoItem

@Database(entities = [TodoItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class EarthbancDatabase : RoomDatabase() {
  abstract fun todoItemDao(): TodoItemDao
}

fun provideDatabase(context: Context) =
  Room.databaseBuilder(
    context = context,
    klass = EarthbancDatabase::class.java,
    name = "EarthbancDatabase"
  ).build()