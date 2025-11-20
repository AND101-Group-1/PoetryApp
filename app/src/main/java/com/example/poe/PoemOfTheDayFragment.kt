package com.example.poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

/** Poem of the Day screen functionality. **/
class PoemOfTheDayFragment : Fragment() {

    private lateinit var favoritesBtn: Button
    private var currentPoem: Poem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poem_of_the_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView = view.findViewById<TextView>(R.id.poemTitle)
        val authorTextView = view.findViewById<TextView>(R.id.poemAuthor)
        val bodyTextView = view.findViewById<TextView>(R.id.poemBody)
        favoritesBtn = view.findViewById(R.id.addToFavoritesBtn)
        
        // Initially disable button until poem loads
        favoritesBtn.isEnabled = false

        val client = PoetryApiClient()
        client.getRandomPoem { poem ->
            activity?.runOnUiThread {
                if (poem != null) {
                    currentPoem = poem
                    titleTextView.text = poem.title
                    authorTextView.text = "by ${poem.author}"
                    bodyTextView.text = poem.lines.joinToString("\n")
                    
                    favoritesBtn.isEnabled = true
                    updateFavoriteButton()
                } else {
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
