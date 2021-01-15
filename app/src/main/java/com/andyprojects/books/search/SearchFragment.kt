package com.andyprojects.books.search

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andyprojects.books.R
import com.andyprojects.books.databinding.FragmentSearchBinding
import com.andyprojects.books.network.BooksFilter

class SearchFragment: Fragment() {

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private lateinit var searchButton: Button
    private lateinit var editSearch: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_shelf, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.show_all -> {
                    loadBooks(searchViewModel.currentSearchKey, BooksFilter.SHOW_ALL.filter)
                return true
            }
            R.id.e_books -> {
                    loadBooks(searchViewModel.currentSearchKey, BooksFilter.E_BOOKS.filter)
                return true
            }
            R.id.free_eBooks -> {
                    loadBooks(searchViewModel.currentSearchKey, BooksFilter.FREE_EBOOKS.filter)
                return true
            }
            R.id.paid_eBooks -> {
                    loadBooks(searchViewModel.currentSearchKey, BooksFilter.PAID_EBOOKS.filter)
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = FragmentSearchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = searchViewModel
        recyclerView = binding.booksList
        recyclerView.apply {
            this.adapter = SearchAdapter(SearchAdapter.OnClickListener {
                searchViewModel.selectBook(it)
            })
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        adapter = recyclerView.adapter as SearchAdapter

        editSearch = binding.searchBar
        searchButton = binding.searchButton
        searchButton.setOnClickListener {
            if(editSearch.text.isNotEmpty()) {
                searchViewModel.currentSearchKey = editSearch.text.toString()
                loadBooks(searchViewModel.currentSearchKey, null)
            } else Toast.makeText(
                requireContext(), "No search entry", Toast.LENGTH_SHORT)
                .show()
        }

        searchViewModel.status.observe(viewLifecycleOwner, {
            when(it) {
                GoogleBooksApiStatus.ERROR -> {
                    if(searchViewModel.books.value.isNullOrEmpty()) {
                        recyclerView.visibility = View.GONE
                        binding.failedScreen.visibility = View.VISIBLE
                        searchButton.isEnabled = true
                    }
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

        searchViewModel.selectedBook.observe(viewLifecycleOwner, {
            it?.let {
                findNavController()
                    .navigate(SearchFragmentDirections
                        .actionSearchFragmentToBookDetailFragment(it))
                searchViewModel.bookSelected()
            }
            if(it == null) adapter.submitList(searchViewModel.books.value)
        })

        return binding.root
    }

    private fun loadBooks(searchKey: String, filter: String?) {
        if(searchKey.isNotEmpty()) {
            searchViewModel.getBooks(searchKey, filter)
            searchViewModel.books.observe(viewLifecycleOwner, {
                adapter.submitList(it)
            })
        }
    }
}