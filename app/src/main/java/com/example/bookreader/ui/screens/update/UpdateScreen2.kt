package com.example.bookreader.ui.screens.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.data.DataOrException
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.ui.screens.home.HomeScreenViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UpdateScreen2(
    navController: NavHostController,
    bookItemId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Update Book",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) { it ->
        val bookInfo = produceState(
            initialValue = DataOrException(emptyList<MBook>(), true, Exception(""))
        ) {
            value = viewModel.data.value
        }.value

        val myBook = viewModel.data.value.data?.filter { mBook ->
            mBook.googleBookId == bookItemId
        }?.toList()?.get(0)

        val newNote = remember { mutableStateOf("") }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(3.dp)
        ) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = myBook?.notes ?: "No notes")
                TextField(
                    value = newNote.value, onValueChange = { note ->
                        newNote.value = note
                    }
                )
                Button(
                    onClick = {
                        myBook?.let {book ->
                            if (book.notes != newNote.value) {
                                book.notes = newNote.value
                                FirebaseFirestore.getInstance()
                                    .collection("books")
                                    .document(book.id!!)
                                    .set(book)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            navController.navigate(ReaderScreens.HomeScreen.name)
                                        }
                                    }
                            }
                        }
                    }
                ) {
                    Text(text = "Save")
                }

            }
        }
    }
}