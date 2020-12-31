package com.andyprojects.books.shelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andyprojects.books.databinding.FragmentShelfBinding

class ShelfFragment: Fragment() {

    private val shelfViewModel: ShelfViewModel by lazy {
        ViewModelProvider(this).get(ShelfViewModel::class.java)
    }

    private lateinit var searchButton: Button
    private lateinit var editSearch: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = FragmentShelfBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = shelfViewModel

        recyclerView = binding.booksList

        recyclerView.apply {
            adapter = ShelfAdapter(ShelfAdapter.OnClickListener {
                shelfViewModel.selectBook(it)
            })
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        editSearch = binding.searchBar
        searchButton = binding.searchButton
        searchButton.setOnClickListener {
            if(editSearch.text.isNotEmpty()) {
                shelfViewModel.getBooks(editSearch.text.toString())
            } else Toast.makeText(
                requireContext(), "No search entry", Toast.LENGTH_SHORT)
                .show()
        }

        shelfViewModel.status.observe(viewLifecycleOwner, Observer {
            when(it) {
                GoogleBooksApiStatus.ERROR -> {
                    recyclerView.visibility = View.GONE
                    binding.failedScreen.visibility = View.VISIBLE
                }
                else -> {
                    recyclerView.visibility = View.VISIBLE
                    binding.failedScreen.visibility = View.GONE
                }
            }
        })

        shelfViewModel.selectedBook.observe(viewLifecycleOwner, Observer {
            findNavController()
                .navigate(ShelfFragmentDirections
                    .actionShelfFragmentToBookDetailFragment(it))
        })

        return binding.root
    }
}