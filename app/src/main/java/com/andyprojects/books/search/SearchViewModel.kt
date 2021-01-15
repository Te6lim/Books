package com.andyprojects.books.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.andyprojects.books.network.Book
import com.andyprojects.books.repository.BooksDataSource
import com.andyprojects.books.repository.BooksDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

enum class GoogleBooksApiStatus{ LOADING, ERROR, DONE }

class SearchViewModel : ViewModel() {

    var books: LiveData<PagedList<Book>> = MutableLiveData()

    private val _status = MutableLiveData<GoogleBooksApiStatus>()
    val status: LiveData<GoogleBooksApiStatus>
    get() = _status

    private val _selectedBook = MutableLiveData<Book?>()
    val selectedBook: LiveData<Book?>
    get() = _selectedBook

    var currentSearchKey: String = String()

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getBooks(searchKey: String, filter: String?) {
        val booksDataSourceFactory = BooksDataSourceFactory(coroutineScope, searchKey, filter, _status)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(BooksDataSource.PAGE_SIZE)
            .build()
        books = LivePagedListBuilder(booksDataSourceFactory, config).build()
    }

    fun selectBook(book: Book) {
        _selectedBook.value = book
    }

    fun bookSelected() {
        _selectedBook.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}