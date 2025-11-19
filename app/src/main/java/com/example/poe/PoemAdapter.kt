package com.example.poe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/** Poem adapter used by the RecyclerView for the search and favorites screens. **/
class PoemAdapter (private val originalList: List<Poem>) : RecyclerView.Adapter<PoemAdapter.ViewHolder>() {

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



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_poem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoemAdapter.ViewHolder, position: Int) {
        val poem = filteredList[position]
        holder.bind(poem)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
}