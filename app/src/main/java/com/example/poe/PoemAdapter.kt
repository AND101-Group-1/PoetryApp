package com.example.poe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.addAll
import kotlin.text.clear

/** Poem adapter used by the RecyclerView for the search and favorites screens. **/
class PoemAdapter (private var originalList: ArrayList<Poem>) : RecyclerView.Adapter<PoemAdapter.ViewHolder>() {

    private var filteredList : MutableList<Poem> = originalList.toMutableList()
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.title)
        val poet: TextView = view.findViewById(R.id.poet)
        fun bind(poem: Poem){
            title.text = poem.title
            poet.text = poem.author
        }
    }
    // Add this method inside your PoemAdapter class

    fun filter(query: String, category: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(originalList)
        } else {
            val lowerCaseQuery = query.lowercase().trim()
            val results = originalList.filter { poem ->
                when (category) {
                    "Poet" -> poem.author.lowercase().contains(lowerCaseQuery)
                    "Title" -> poem.title.lowercase().contains(lowerCaseQuery)
                    else -> false
                }
            }
            filteredList.addAll(results)
        }
        notifyDataSetChanged()
    }

    fun updateData(newPoems: List<Poem>) {
        // 1. Clear the existing data
        filteredList.clear()
        originalList.clear()

        // 2. Add the new data from the APIoriginalList.addAll(newPoems)
        filteredList.addAll(newPoems)

        // 3. Refresh the RecyclerView to show the new items
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_poem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoemAdapter.ViewHolder, position: Int) {
        val poem = filteredList[position]
        holder.bind(poem)

        holder.itemView.setOnClickListener {
            // Get the context from the clicked view, which is needed to get the FragmentManager
            val context = holder.itemView.context

            // Create an instance of the PoemDetailFragment, passing the specific clicked poem
            // Make sure your Poem data class is Serializable for this to work
            val detailFragment = PoemDetailFragment.newInstance(poem)

            // Get the FragmentManager from the hosting Activity and start the transaction
            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                // Replace the content of your fragment container with the detail fragment
                .replace(R.id.fragment_container, detailFragment) // IMPORTANT: Use the ID of your FragmentContainerView from your Activity's layout
                // Add the transaction to the back stack so the user can press the back button
                .addToBackStack(null)
                // Commit the transaction to apply the changes
                .commit()
        }

    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
}