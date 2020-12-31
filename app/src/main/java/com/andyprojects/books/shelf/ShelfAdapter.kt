package com.andyprojects.books.shelf

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andyprojects.books.R
import com.andyprojects.books.databinding.ItemBookBinding
import com.andyprojects.books.network.Book

class ShelfAdapter(private val onClickListener: OnClickListener)
    : ListAdapter<Book, ShelfAdapter.BookViewHolder>(DiffCallback) {

    companion object DiffCallback: DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

    }

    inner class BookViewHolder(private val itemBookBinding: ItemBookBinding)
        : RecyclerView.ViewHolder(itemBookBinding.root) {
        fun bind(book: Book) {
            itemBookBinding.book = book
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemBinding = DataBindingUtil
            .inflate<ItemBookBinding>(
                LayoutInflater
                    .from(parent.context), R.layout.item_book, parent, false
            )
        return BookViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(book)
        }
        holder.bind(book)
    }

    class OnClickListener(val clickListener: (Book) -> Unit) {
        fun onClick(book: Book) = clickListener(book)
    }
}