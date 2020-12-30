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
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>>
    get() = _books

    private val _status = MutableLiveData<GoogleBooksApiStatus>()
    val status: LiveData<GoogleBooksApiStatus>
    get() = _status

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getBooks(searchKey: String) {
        coroutineScope.launch {
            val getBooksDeferred = BooksAPi.retrofitService.getBooksAsync(searchKey)
            try {
                _status.value = GoogleBooksApiStatus.LOADING
                val library = getBooksDeferred.await()
                if(library.items.isNotEmpty()) {
                    _books.value = library.items
                    _status.value = GoogleBooksApiStatus.DONE
                }
            }
            catch(t: Throwable) {
                _status.value = GoogleBooksApiStatus.ERROR
                _books.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}