package com.andyprojects.books.bookDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andyprojects.books.network.Book
import java.lang.IllegalArgumentException

class BookDetailViewModelFactory(private val book: Book): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookDetailViewModel::class.java))
            return BookDetailViewModel(book) as T
        throw IllegalArgumentException("class is not assignable")
    }
}