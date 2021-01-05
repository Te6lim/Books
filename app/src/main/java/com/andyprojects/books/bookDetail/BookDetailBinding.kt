package com.andyprojects.books.bookDetail

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.andyprojects.books.R
import com.andyprojects.books.network.Book
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.math.floor

@BindingAdapter("titleText")
fun bindHeader(view: TextView, title: String?) {
    title?.let { view.text = title }
}

@BindingAdapter("isbnTen")
fun bindIsbnTen(view: TextView, isbn: Array<Book.VolumeInfo.BookNum>?) {
    isbn?.let { view.text = it[0].identifier }
}

@BindingAdapter("isbnThirteen")
fun bindIsbnThirteen(view: TextView, isbn: Array<Book.VolumeInfo.BookNum>?) {
    isbn?.let {
        view.text = if(it[0].identifier != it[it.lastIndex].identifier)
            it[it.lastIndex].identifier
        else " "
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

@BindingAdapter("starRating")
fun bindRating(view: ConstraintLayout, rating: Double?) {
    rating?.let {
        val rem = (rating - floor(rating)) * 10
        when(floor(rating).toInt()) {
            1 -> {
                if(rem >= 5) {
                    view.findViewById<ImageView>(R.id.star_1)
                        .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                    view.findViewById<ImageView>(R.id.star_2)
                        .setImageDrawable(view.context.getDrawable(R.drawable.ic_star_half_black_24dp))
                } else {
                    view.findViewById<ImageView>(R.id.star_1)
                        .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                }
            }
            2 -> {
                if(rem >= 5) {
                    view.apply {
                        findViewById<ImageView>(R.id.star_1)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_2)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_3)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star_half_black_24dp))

                    }
                } else {
                    view.apply {
                        findViewById<ImageView>(R.id.star_1)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_2)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                    }
                }
            }
            3 -> {
                if(rem >= 5) {
                    view.apply {
                        findViewById<ImageView>(R.id.star_1)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_2)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_3)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_4)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star_half_black_24dp))
                    }

                } else {
                    view.apply {
                        findViewById<ImageView>(R.id.star_1)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_2)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_3)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                    }
                }
            }
            4 -> {
                if (rem >= 0) {
                    view.apply {
                        findViewById<ImageView>(R.id.star_1)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_2)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_3)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_4)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_4)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star_half_black_24dp))
                    }
                } else {
                    view.apply {
                        findViewById<ImageView>(R.id.star_1)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_2)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_3)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                        view.findViewById<ImageView>(R.id.star_4)
                            .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                    }
                }
            }
            5 -> {
                view.apply {
                    findViewById<ImageView>(R.id.star_1)
                        .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                    view.findViewById<ImageView>(R.id.star_2)
                        .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                    view.findViewById<ImageView>(R.id.star_3)
                        .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                    view.findViewById<ImageView>(R.id.star_4)
                        .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                    view.findViewById<ImageView>(R.id.star_5)
                        .setImageDrawable(view.context.getDrawable(R.drawable.ic_star))
                }
            }
            else -> {}
        }
    }
}

@BindingAdapter("description")
fun bindDescription(view: TextView, desc: String?) {
    desc?.let {
        view.text = desc
    }
}