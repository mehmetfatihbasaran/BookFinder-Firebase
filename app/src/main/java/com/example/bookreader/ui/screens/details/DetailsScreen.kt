package com.example.bookreader.ui.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bookreader.components.ReaderAppBar
import com.example.bookreader.components.RoundedButton
import com.example.bookreader.data.Resource
import com.example.bookreader.model.Item
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DetailsScreen(
    navController: NavHostController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Details",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBookInfo(bookId)
                }.value
                ShowBookDetails(bookInfo = bookInfo, navController = navController)
            }
        }
    }


}

@Composable
fun ShowBookDetails(bookInfo: Resource<Item>, navController: NavHostController) {
    val bookData = bookInfo.data?.volumeInfo  ?: return
    val googleBookId = bookInfo.data?.id

    Card(
        modifier = Modifier.padding(34.dp),
        shape = CircleShape,
        elevation = 4.dp
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = bookData?.imageLinks?.thumbnail),
            contentDescription = "book image",
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(2.dp)
        )
    }

    Text(
        text = bookData?.title.toString(),
        style = MaterialTheme.typography.h6,
        overflow = TextOverflow.Ellipsis,
        maxLines = 20,
    )
    Text(text = "Authors: ${bookData?.authors.toString()}")
    Text(text = "Page Count: ${bookData?.pageCount.toString()}")
    Text(
        text = "Categories: ${bookData?.categories.toString()}",
        style = MaterialTheme.typography.subtitle1,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
    Text(
        text = "Published: ${bookData?.publishedDate.toString()}",
        style = MaterialTheme.typography.subtitle1
    )

    Spacer(modifier = Modifier.height(5.dp))

    val cleanDescription = HtmlCompat.fromHtml(
        bookData?.description ?: "",
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()
    val localDims = LocalContext.current.resources.displayMetrics
    Surface(
        modifier = Modifier
            .height(localDims.heightPixels.dp.times(0.09f))
            .padding(4.dp),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.DarkGray)
    ) {
        LazyColumn(modifier = Modifier.padding(3.dp)) {
            item {
                Text(text = cleanDescription)
            }
        }
    }

    Row(
        modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        RoundedButton(
            label = "Save",
        ) {
            val category = if (bookData?.categories.isNullOrEmpty()) {
                "Unknown"
            } else {
                bookData?.categories?.get(0) ?: "Unknown"
            }
            val book = if (bookData != null) {
                var mBook: MBook = MBook(
                    title = bookData.title,
                    authors = bookData.authors.toString(),
                    description = bookData.description,
                    categories = category,
                    notes = "",
                    photoUrl = bookData.imageLinks?.thumbnail ?: "",
                    publishedDate = bookData.publishedDate,
                    pageCount = bookData.pageCount.toString(),
                    googleBookId = googleBookId ?: "",
                    rating = 0.0,
                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                )
                mBook
            } else {
                MBook(
                    title = "No Title",
                    authors = "No Author",
                )
            }
            saveToFirebase(
                book = book,
                navController = navController
            )
        }
        Spacer(modifier = Modifier.width(25.dp))
        RoundedButton(
            label = "Cancel",
        ) {
            navController.popBackStack()
        }
    }

}

fun saveToFirebase(book: MBook, navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")
    if (book.toString().isNotEmpty()) {
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId).update("id", docId)
                    .addOnCompleteListener{
                        navController.popBackStack()
                    }
            }
            .addOnFailureListener { e ->
                Log.w("Error", "Error adding document", e)
            }
    }

}
