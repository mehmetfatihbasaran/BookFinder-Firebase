package com.example.bookreader.ui.screens.details

import androidx.lifecycle.ViewModel
import com.example.bookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    suspend fun getBookInfo(bookId: String) = repository.getBookInfo(bookId)

}