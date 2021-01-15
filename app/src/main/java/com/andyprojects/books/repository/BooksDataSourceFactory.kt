package com.andyprojects.books.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.andyprojects.books.network.Book
import com.andyprojects.books.search.GoogleBooksApiStatus
import kotlinx.coroutines.CoroutineScope

class BooksDataSourceFactory(
    private val coroutineScope: CoroutineScope,
    private val searchKey: String,
    private val filter: String?,
    private val status: MutableLiveData<GoogleBooksApiStatus>): DataSource.Factory<Int, Book>() {

    override fun create(): DataSource<Int, Book> {
        return BooksDataSource(coroutineScope, searchKey, filter, status)
    }
}