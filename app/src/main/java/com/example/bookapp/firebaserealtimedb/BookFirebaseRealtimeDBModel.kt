package com.example.bookapp.firebaserealtimedb

data class BookFirebaseRealtimeDBModel(
    val bookTitle : String,
    val authorName : String,
    val publicationYear : String,
    val category : String,
    val bookCoverURL : String
)