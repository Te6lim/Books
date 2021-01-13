package com.andyprojects.books.shelf

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andyprojects.books.R
import com.andyprojects.books.databinding.FragmentShelfBinding

class ShelfFragment: Fragment() {

    private val shelfViewModel: ShelfViewModel by lazy {
        ViewModelProvider(this).get(ShelfViewModel::class.java)
    }

    private lateinit var searchButton: Button
    private lateinit var editSearch: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_shelf, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

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
                shelfViewModel.books.observe(viewLifecycleOwner, {
                    (recyclerView.adapter as ShelfAdapter).submitList(it)
                })
            } else Toast.makeText(
                requireContext(), "No search entry", Toast.LENGTH_SHORT)
                .show()
        }

        shelfViewModel.status.observe(viewLifecycleOwner, {
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

        shelfViewModel.selectedBook.observe(viewLifecycleOwner, {
            findNavController()
                .navigate(ShelfFragmentDirections
                    .actionShelfFragmentToBookDetailFragment(it))
            shelfViewModel.bookSelected()
        })

        return binding.root
    }
}