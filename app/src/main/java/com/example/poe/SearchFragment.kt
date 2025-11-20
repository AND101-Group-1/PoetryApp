package com.example.poe

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/** Search screen functionality. **/
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PoemAdapter
    private var poems: ArrayList<Poem> = arrayListOf()
    private var searchCategory: String = "Poet" // Default search category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeData() // Load initial data
        setupRecyclerView(view)
        setupSpinner(view)
        setupSearchView(view)
    }

    private fun initializeData() {

        // 1. Create Retrofit instance
        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("https://poetrydb.org/")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
        // 2. Create the API Service
        val service = retrofit.create(PoetryApiService::class.java)
        // 3. Call the API (Fetching 20 random poems for the initial view)
        // Ensure your PoetryApiService has a method: @GET("random/20") fun getRandomPoems(): Call<List<Poem>>
        val call = service.getRandomPoems()
        call.enqueue(object : retrofit2.Callback<List<Poem>> {
            override fun onResponse(call: retrofit2.Call<List<Poem>>, response: retrofit2.Response<List<Poem>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Convert the immutable List from API to an ArrayList
                    poems = ArrayList(response.body()!!)
                    adapter.updateData(poems)
                }
            }
            override fun onFailure(call: retrofit2.Call<List<Poem>>, t: Throwable) {
                // Handle failure (e.g., log error or show toast)
                t.printStackTrace()
            }
        })
    }



    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialize adapter with the empty list first
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
                // FIX: Use the selected item to guide the search
                searchCategory = parent.getItemAtPosition(position).toString()
                // Optional: Trigger a new search when the category changes
                adapter.filter(searchView.query.toString(), searchCategory)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // FIX: Left empty to prevent crash
            }
        }
    }

    private fun setupSearchView(view: View) {
        searchView = view.findViewById(R.id.searchView)
        val categorySpinner: Spinner = view.findViewById(R.id.category) // Ensure you have the spinner ID

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {

            // This runs when the user presses the Search button on the keyboard
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    val category = categorySpinner.selectedItem.toString()
                    performSearch(query, category) // Call the API function
                    searchView.clearFocus() // Hides the keyboard
                }
                return true
            }

            // This runs every time a letter is typed (Optional: leave empty if you only want search on submit)
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
    private fun performSearch(query: String, category: String) {
        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("https://poetrydb.org/")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PoetryApiService::class.java)

        val call = if (category.equals("Poet", ignoreCase = true)) {
            service.getPoemsByAuthor(query)
        } else {
            service.getPoemsByTitle(query)
        }


        call.enqueue(object : retrofit2.Callback<List<Poem>> {
            override fun onResponse(call: retrofit2.Call<List<Poem>>, response: retrofit2.Response<List<Poem>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Convert to ArrayList and update adapter
                    val results = ArrayList(response.body()!!)
                    adapter.updateData(results)
                } else {
                    // Optional: Handle "No poems found"
                    adapter.updateData(arrayListOf())
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Poem>>, t: Throwable) {
                t.printStackTrace() // Handle network errors
            }
        })
    }

}
