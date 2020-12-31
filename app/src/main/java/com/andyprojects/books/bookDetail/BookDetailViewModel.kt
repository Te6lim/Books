package com.andyprojects.books.bookDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andyprojects.books.network.Book

class BookDetailViewModel(book: Book): ViewModel() {
    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook: LiveData<Book>
    get() = _selectedBook

    init {
        _selectedBook.value = book
    }
}