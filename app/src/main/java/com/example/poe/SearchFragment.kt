package com.example.poe

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/** Search screen functionality. **/
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PoemAdapter
    private lateinit var poems: List<Poem>
    private var searchCategory: String = "Poet" // Default search category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeData() // Load initial data
        setupRecyclerView(view)
        setupSpinner(view)
        setupSearchView(view)
    }

    private fun initializeData() {
        // Start with an empty list as requested
        poems = listOf()
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView) // Make sure you have a RecyclerView with this ID
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PoemAdapter(poems)
        recyclerView.adapter = adapter
    }

    private fun setupSpinner(view: View) {
        val spinner: Spinner = view.findViewById(R.id.category)
        val items = arrayOf("Poet", "Title")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Use the selected item to guide the search
                searchCategory = parent.getItemAtPosition(position).toString()
                // Optional: Trigger a new search when the category changes
                adapter.filter(searchView.query.toString(), searchCategory)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Left empty to prevent crash
            }
        }
    }

    private fun setupSearchView(view: View) {
        searchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                // Pass both the query and the selected category to the filter
                adapter.filter(query ?: "", searchCategory)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                // Left empty is fine if you don't need to handle submit explicitly
                return false // Return false to let the SearchView perform the default action (if any)
            }
        })
    }
}
