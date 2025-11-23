package com.example.poe

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/** Search screen functionality. **/
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PoemAdapter
    private var poems: MutableList<Poem> = mutableListOf()
    private var searchCategory: String = "Poet" // Default search category
    private val poetryApiClient = PoetryApiClient()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initializeData() // No need for fake data anymore
        setupRecyclerView(view)
        setupSpinner(view)
        setupSearchView(view)
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PoemAdapter(poems) // Initialize with empty list
        recyclerView.adapter = adapter
    }

    private fun setupSpinner(view: View) {
        val spinner: Spinner = view.findViewById(R.id.category)
        val items = arrayOf("Poet", "Title")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                searchCategory = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setupSearchView(view: View) {
        searchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    performSearch(query)
                }
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        poems.clear()
        adapter.notifyDataSetChanged()

        if (searchCategory == "Poet") {
            poetryApiClient.searchByAuthor(query) { resultPoems ->
                activity?.runOnUiThread {
                    if (resultPoems.isNotEmpty()) {
                        poems.clear()
                        poems.addAll(resultPoems)
                        // Re-initialize adapter because the dataset reference might change inside adapter otherwise
                        // Or better, update the adapter's data method if it exists. 
                        // For simplicity here, assuming adapter takes the list reference directly or we notify change.
                        // Since we passed 'poems' (mutable list) to adapter, modifying it and notifying should work
                        // providing the adapter uses the same list instance.
                        // Let's make sure adapter updates correctly.
                        adapter = PoemAdapter(poems)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(context, "No poems found for author: $query", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (searchCategory == "Title") {
            poetryApiClient.searchByTitle(query) { resultPoems ->
                activity?.runOnUiThread {
                    if (resultPoems.isNotEmpty()) {
                        poems.clear()
                        poems.addAll(resultPoems)
                         adapter = PoemAdapter(poems)
                        recyclerView.adapter = adapter
                    } else {
                         Toast.makeText(context, "No poems found with title: $query", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
