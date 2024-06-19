package com.earthbanc.todo.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTaskBottomSheet(onDismiss: () -> Unit, onAddNewTask: (String, String) -> Unit) {
  val sheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = false,
  )
  var title by remember { mutableStateOf("") }
  var isTitleError by remember {
    mutableStateOf(false)
  }
  var description by remember { mutableStateOf("") }

  ModalBottomSheet(
    modifier = Modifier
      .fillMaxHeight()
      .padding(8.dp),
    sheetState = sheetState,
    onDismissRequest = onDismiss,
  ) {
    Column(modifier = Modifier.padding(8.dp)) {
      Text(
        text = "Add new task",
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(16.dp))
      TextField(
        value = title,
        onValueChange = {
          title = it
          if (it.isNotEmpty()) isTitleError = false
        },
        supportingText = { if (isTitleError) Text(text = "* this field is mandatory") },
        isError = isTitleError,
        label = { Text(text = "Title") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(8.dp))
      TextField(
        value = description,
        onValueChange = { description = it },
        label = { Text(text = "Description") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(32.dp))
      Button(
        onClick = {
          if (title.isEmpty()) {
            isTitleError = true
            return@Button
          }
          onAddNewTask(title, description)
          onDismiss()
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.align(Alignment.End)
      ) {
        Text(text = "Add")
      }
    }
  }
}