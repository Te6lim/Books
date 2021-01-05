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
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                var loading = true
                var pastVisibleItems = 0
                var visibleItemCount = 0
                var totalItemCount = 0
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    if(dy > 0) {
                        visibleItemCount = layoutManager.childCount
                        totalItemCount = layoutManager.itemCount
                        pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                        if(loading) {
                            if(visibleItemCount + pastVisibleItems >= totalItemCount) {
                                loading = false
                                shelfViewModel.getSearchKey()?.let { shelfViewModel.getBooks(it) }
                                loading = true
                            }
                        }
                    }
                }
            })
        }

        editSearch = binding.searchBar
        searchButton = binding.searchButton
        searchButton.setOnClickListener {
            if(editSearch.text.isNotEmpty()) {
                val searchKey = editSearch.text.toString()
                shelfViewModel.setSearchKey(searchKey)
                shelfViewModel.getBooks(searchKey)
            } else Toast.makeText(
                requireContext(), "No search entry", Toast.LENGTH_SHORT)
                .show()
        }

        shelfViewModel.status.observe(viewLifecycleOwner, Observer {
            when(it) {
                GoogleBooksApiStatus.ERROR -> {
                    recyclerView.visibility = View.GONE
                    binding.failedScreen.visibility = View.VISIBLE
                    searchButton.isEnabled = true
                }
                GoogleBooksApiStatus.LOADING -> {
                    searchButton.isEnabled = false
                }
                else -> {
                    recyclerView.visibility = View.VISIBLE
                    binding.failedScreen.visibility = View.GONE
                    searchButton.isEnabled = true
                }
            }
        })

        shelfViewModel.selectedBook.observe(viewLifecycleOwner, Observer {
                it?.let {
                    findNavController()
                        .navigate(ShelfFragmentDirections
                            .actionShelfFragmentToBookDetailFragment(it))
                    shelfViewModel.bookSelected()
                }
        })

        return binding.root
    }
}