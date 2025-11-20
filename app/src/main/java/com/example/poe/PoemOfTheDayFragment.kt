
package com.example.poe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

/** Poem of the Day screen functionality using a Singleton holder. **/
class PoemOfTheDayFragment : Fragment() {

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
        val heartButton = view.findViewById<FloatingActionButton>(R.id.favoriteBtn)
        // Try to get the poem from our in-memory holder first
        val todaysPoem = PoemOfTheDayHolder.getPoemForToday()

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
                if (newPoem != null) {
                    // 1. Display the new poem
                    titleTextView.text = newPoem.title
                    authorTextView.text = "by ${newPoem.author}"
                    bodyTextView.text = newPoem.lines.joinToString("\n")

                    // 2. IMPORTANT: Update singleton holder with the new poem
                    PoemOfTheDayHolder.updatePoem(newPoem)
                    Log.d("PoemOfTheDay", "New poem fetched and saved to memory holder.")
                } else {
                    // Handle API failure
                    bodyTextView.text = "Failed to load poem."
                }
            }
        }
    }
}
