
package com.example.poe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

/** Poem of the Day screen functionality using a Singleton holder. **/
class PoemOfTheDayFragment : Fragment() {

    private lateinit var favoritesBtn: Button
    private var currentPoem: Poem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_poem_of_the_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the UI elements
        val titleTextView = view.findViewById<TextView>(R.id.poemTitle)
        val authorTextView = view.findViewById<TextView>(R.id.poemAuthor)
        val bodyTextView = view.findViewById<TextView>(R.id.poemBody)
        favoritesBtn = view.findViewById(R.id.addToFavoritesBtn)
        
        // Initially disable button until poem loads
        favoritesBtn.isEnabled = false

        if (todaysPoem != null) {
            // If a valid poem for today exists, just display it
            Log.d("PoemOfTheDay", "Loading poem from memory holder.")
            titleTextView.text = todaysPoem.title
            authorTextView.text = "by ${todaysPoem.author}"
            bodyTextView.text = todaysPoem.lines.joinToString("\n")
        } else {
            // If no valid poem exists, fetch a new one
            Log.d("PoemOfTheDay", "No poem for today in memory. Fetching a new one.")
            fetchAndDisplayNewPoem(titleTextView, authorTextView, bodyTextView)
        }

        // Add to favorites
        heartButton.setOnClickListener {
            todaysPoem?.let { FavoritesData.addFavorite(it) }
        }
    }

    private fun fetchAndDisplayNewPoem(
        titleTextView: TextView,
        authorTextView: TextView,
        bodyTextView: TextView
    ) {
        val client = PoetryApiClient()
        client.getRandomPoem { newPoem ->

            activity?.runOnUiThread {
                if (poem != null) {
                    currentPoem = poem
                    titleTextView.text = poem.title
                    authorTextView.text = "by ${poem.author}"
                    bodyTextView.text = poem.lines.joinToString("\n")
                    
                    favoritesBtn.isEnabled = true
                    updateFavoriteButton()
                } else {
                    // Handle API failure
                    bodyTextView.text = "Failed to load poem."
                }
            }
        }
        
        favoritesBtn.setOnClickListener {
            if (currentPoem != null) {
                if (FavoritesData.favorites.contains(currentPoem)) {
                    FavoritesData.favorites.remove(currentPoem)
                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                } else {
                    FavoritesData.favorites.add(currentPoem!!)
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
                }
                updateFavoriteButton()
            }
        }
    }
    
    private fun updateFavoriteButton() {
        if (currentPoem != null) {
            if (FavoritesData.favorites.contains(currentPoem)) {
               favoritesBtn.text = "Unfavorite \u2665"
            } else {
                favoritesBtn.text = "Favorite \u2661"
            }
        }
    }
}
