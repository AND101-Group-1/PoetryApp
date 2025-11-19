package com.example.poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/** Poem of the Day screen functionality. **/
class PoemOfTheDayFragment : Fragment() {

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

        val client = PoetryApiClient()
        client.getRandomPoem { poem ->
            activity?.runOnUiThread {
                if (poem != null) {
                    titleTextView.text = poem.title
                    authorTextView.text = "by ${poem.author}"
                    bodyTextView.text = poem.lines.joinToString("\n")
                } else {
                    bodyTextView.text = "Failed to load poem."
                }
            }
        }
    }
}