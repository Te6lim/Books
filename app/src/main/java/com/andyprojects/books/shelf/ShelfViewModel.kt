package com.andyprojects.books.shelf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andyprojects.books.network.Book
import com.andyprojects.books.network.BooksAPi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class GoogleBooksApiStatus{ LOADING, ERROR, DONE }

class ShelfViewModel : ViewModel() {

    companion object {
        const val MAX_RESULT = 40
    }

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
    get() = _books

    private val _status = MutableLiveData<GoogleBooksApiStatus>()
    val status: LiveData<GoogleBooksApiStatus>
    get() = _status

    private val _selectedBook = MutableLiveData<Book>()
    val selectedBook: LiveData<Book>
    get() = _selectedBook

    private var searchKey: String? = null

    private var pageCount: Int = 0

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getBooks(searchKey: String, startIndex: Int = pageCount * (MAX_RESULT + 1)) {
        coroutineScope.launch {
            val getBooksDeferred = BooksAPi.retrofitService
                .getBooksAsync(searchKey, startIndex, MAX_RESULT)
            try {
                _status.value = GoogleBooksApiStatus.LOADING
                val library = getBooksDeferred.await()
                if(library.items.isNotEmpty()) {
                    val books: MutableList<Book> = ( _books.value as MutableList<Book> )
                    for(book in library.items)
                        books.add(book)
                    _books.value = books
                    ++pageCount
                    _status.value = GoogleBooksApiStatus.DONE
                }
            }
            catch(t: Throwable) {
                _status.value = GoogleBooksApiStatus.ERROR
                _books.value = ArrayList()
            }
        }
    }

    fun setSearchKey(key: String) {
        searchKey = key
    }

    fun getSearchKey() = searchKey

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