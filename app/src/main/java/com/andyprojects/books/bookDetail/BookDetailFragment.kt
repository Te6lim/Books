package com.andyprojects.books.bookDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andyprojects.books.databinding.FragmentDetailBookBinding

private lateinit var titleText: TextView

class BookDetailFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBookBinding
            .inflate(inflater)
        binding.lifecycleOwner = this
        val book = BookDetailFragmentArgs.fromBundle(requireArguments()).selectedBook
        val viewModelFactory = BookDetailViewModelFactory(book)
        binding.bookDetailViewModel = ViewModelProvider(
            this, viewModelFactory)
            .get(BookDetailViewModel::class.java)

        return binding.root
    }
}