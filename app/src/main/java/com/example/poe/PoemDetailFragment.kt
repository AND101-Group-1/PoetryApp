package com.example.poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class PoemDetailFragment : Fragment() {

    private var currentPoem: Poem? = null
    private lateinit var titleTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var bodyTextView: TextView
    private lateinit var favoritesBtn: Button

    companion object {
        private const val ARG_POEM = "poem_arg"

        fun newInstance(poem: Poem): PoemDetailFragment {
            val fragment = PoemDetailFragment()
            val args = Bundle().apply {
                putSerializable(ARG_POEM, poem)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poem_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView = view.findViewById(R.id.poemDetailTitle)
        authorTextView = view.findViewById(R.id.poemDetailAuthor)
        bodyTextView = view.findViewById(R.id.poemDetailLines)
        favoritesBtn = view.findViewById(R.id.addToFavoritesBtn)

        currentPoem = arguments?.getSerializable(ARG_POEM) as? Poem

        if (currentPoem != null) {
            titleTextView.text = currentPoem!!.title
            authorTextView.text = "by ${currentPoem!!.author}"
            bodyTextView.text = currentPoem!!.lines.joinToString("\n")
            
            updateFavoriteButton()

            favoritesBtn.setOnClickListener {
                if (FavoritesData.favorites.contains(currentPoem)) {
                    FavoritesData.favorites.remove(currentPoem)
                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                } else {
                    FavoritesData.favorites.add(currentPoem!!)
                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show()
                }
                updateFavoriteButton()
            }

        } else {
            titleTextView.text = "Error"
            bodyTextView.text = "Could not load poem details."
            favoritesBtn.isEnabled = false
        }
    }

    private fun updateFavoriteButton() {
        if (currentPoem != null) {
            if (FavoritesData.favorites.contains(currentPoem)) {
               favoritesBtn.text = "Unfavorite \u2665" // Filled heart character
            } else {
                favoritesBtn.text = "Favorite \u2661" // Outline heart character
            }
        }
    }
}
