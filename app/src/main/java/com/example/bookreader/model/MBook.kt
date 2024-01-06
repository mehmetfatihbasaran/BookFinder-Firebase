package com.example.bookreader.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class MBook(

    @Exclude var id: String? = null,

    var title: String? = null,
    var authors: String? = null,

    @get:PropertyName("notes")
    @set:PropertyName("notes")
    var notes: String? = null,

    @get:PropertyName("book_photo_url")
    @set:PropertyName("book_photo_url")
    var photoUrl: String? = "http://books.google.com/books/content?id=_0JqDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
    var categories: String? = null,

    @get:PropertyName("published_date")
    @set:PropertyName("published_date")
    var publishedDate: String? = null,

    var rating: Double? = null,
    var description: String? = null,

    @get:PropertyName("page_count")
    @set:PropertyName("page_count")
    var pageCount: String? = null,

    @get:PropertyName("started_reading_at")
    @set:PropertyName("started_reading_at")
    var startedReading: Timestamp? = null,

    @get:PropertyName("finished_reading_at")
    @set:PropertyName("finished_reading_at")
    var finishedReading: Timestamp? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,

    @get:PropertyName("google_book_id")
    @set:PropertyName("google_book_id")
    var googleBookId: String? = null
)