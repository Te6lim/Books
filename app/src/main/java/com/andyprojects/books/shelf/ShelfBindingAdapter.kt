package com.andyprojects.books.shelf

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.andyprojects.books.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("toast")
fun View.toastBinding(stat: GoogleBooksApiStatus?) {
    stat?.let { Toast.makeText(context, stat.name, Toast.LENGTH_SHORT).show() }
}

@BindingAdapter("imgUrl")
fun View.bindImage(imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("http").build()
        Glide.with(this as ImageView)
            .load(imgUri)
            .apply(RequestOptions().placeholder(R.mipmap.ic_image)
                .error(R.mipmap.ic_broken_image))
            .into(this)
    }
}

@BindingAdapter("bookTitle")
fun View.bindTitle(title: String?) {
    title?.let {
        (this as TextView).text = it
    }
}

@BindingAdapter("bookAuthors")
fun View.bindAuthors(authors: Array<String?>?) {
    authors?.let {
        val auths = StringBuffer()
        for(auth in it){
            if(auth != it[it.lastIndex])
                auths.append("$auth, ")
            else auths.append(auth)
        }
        (this as TextView).text = auths.toString()
    }
}

@BindingAdapter("bookPublisher")
fun View.bindPublisher(pub: String?) {
    pub?.let { (this as TextView).text = it }
}