package com.andyprojects.books.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.andyprojects.books.network.Book
import com.andyprojects.books.network.BooksApi
import com.andyprojects.books.search.GoogleBooksApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class BooksDataSource(
    private val coroutineScope: CoroutineScope,
    private val searchKey: String,
    private val filter: String?,
    private val status: MutableLiveData<GoogleBooksApiStatus>
): PageKeyedDataSource<Int, Book>() {

    companion object {
        const val PAGE_SIZE = 40
        const val FIRST_PAGE = 0
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Book>
    ) {
        coroutineScope.launch {
            val responseDeferred =
                if(filter != null) BooksApi.retrofitService
                    .getBooksWithFilterAsync(searchKey, filter, FIRST_PAGE)
            else BooksApi.retrofitService
                    .getBooksAsync(searchKey, FIRST_PAGE)
            try {
                status.value = GoogleBooksApiStatus.LOADING
                val response = responseDeferred.await()
                if(response.items.isNotEmpty())
                    callback.onResult(response.items, null, PAGE_SIZE)
                status.value = GoogleBooksApiStatus.DONE
            } catch(t: Throwable) {
                status.value = GoogleBooksApiStatus.ERROR
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Book>) {
        coroutineScope.launch {
            val responseDeferred =
                if(filter != null) BooksApi.retrofitService
                    .getBooksWithFilterAsync(searchKey, filter, FIRST_PAGE)
                else BooksApi.retrofitService
                    .getBooksAsync(searchKey, FIRST_PAGE)
            try {
                status.value = GoogleBooksApiStatus.LOADING
                val response = responseDeferred.await()
                val key = if(params.key > PAGE_SIZE) params.key - PAGE_SIZE
                else null
                if(response.items.isNotEmpty())
                    callback.onResult(response.items, key)
                status.value = GoogleBooksApiStatus.DONE
            } catch(r: Throwable) {
                status.value = GoogleBooksApiStatus.ERROR
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Book>) {
        coroutineScope.launch {
            val responseDeferred =
                if(filter != null) BooksApi.retrofitService
                    .getBooksWithFilterAsync(searchKey, filter, FIRST_PAGE)
                else BooksApi.retrofitService
                    .getBooksAsync(searchKey, FIRST_PAGE)
            try {
                status.value = GoogleBooksApiStatus.LOADING
                val response = responseDeferred.await()
                val key = if(params.key < response.totalItems) params.key + PAGE_SIZE
                else null
                if(response.items.isNotEmpty())
                    callback.onResult(response.items, key)
                status.value = GoogleBooksApiStatus.DONE
            } catch(t: Throwable) {
                status.value = GoogleBooksApiStatus.ERROR
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        coroutineScope.coroutineContext.job.cancel()
    }
}