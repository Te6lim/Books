package com.andyprojects.books.bookDetail

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.andyprojects.books.R
import com.andyprojects.books.network.Book
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("titleText")
fun bindHeader(view: TextView, title: String?) {
    title?.let { view.text = title }
}

@BindingAdapter("isbnTen")
fun bindIsbnTen(view: TextView, isbn: Array<Book.VolumeInfo.BookNum>?) {
    isbn?.let {
        view.text = isbn[0].identifier
    }
}

@BindingAdapter("isbnThirteen")
fun bindIsbnThirteen(view: TextView, isbn: Array<Book.VolumeInfo.BookNum>?) {
    isbn?.let {
        view.text = isbn[1].identifier
    }
}

@BindingAdapter("bookCover")
fun bindCover(view: ImageView, url: String?) {
    url?.let {
        val uri = url.toUri().buildUpon().scheme("http").build()
        Glide.with(view)
            .load(uri)
            .apply(RequestOptions().placeholder(R.mipmap.ic_image)
                .error(R.mipmap.ic_broken_image))
            .into(view)
    }
}

@BindingAdapter("pageCount")
fun bindPageCount(view: TextView, count: Int?) {
    count?.let {
        view.text = count.toString()
    }
}

@BindingAdapter("description")
fun bindDescription(view: TextView, desc: String?) {
    desc?.let {
        view.text = desc
    }
}