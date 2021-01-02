package com.andyprojects.books.bookDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.andyprojects.books.R
import com.andyprojects.books.databinding.FragmentDetailBookBinding

class BookDetailFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil
            .inflate<FragmentDetailBookBinding>(
            inflater, R.layout.fragment_detail_book, container, false
        )
        binding.lifecycleOwner = this
        val book = BookDetailFragmentArgs.fromBundle(requireArguments()).selectedBook
        val viewModelFactory = BookDetailViewModelFactory(book)
        binding.bookDetailViewModel = ViewModelProvider(
            this, viewModelFactory)
            .get(BookDetailViewModel::class.java)

        return binding.root
    }
}