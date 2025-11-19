package com.example.poe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.poe.databinding.FragmentPoemDetailBinding // Using ViewBinding

class PoemDetailFragment : Fragment() {

    private var _binding: FragmentPoemDetailBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.


    companion object {
        private const val ARG_POEM = "poem_arg"

        // Factory method to create a new instance and pass the Poem object
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
    ): View {
        _binding = FragmentPoemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the poem from the arguments
        val poem = arguments?.getSerializable(ARG_POEM) as? Poem

        if (poem != null) {
            // Populate the UI with the poem's details
            binding.poemDetailTitle.text = poem.title
            binding.poemDetailAuthor.text = "by ${poem.author}"
            binding.poemDetailLines.text = poem.lines.joinToString("\n")
        } else {
            // Handle the error case where the poem is null
            binding.poemDetailTitle.text = "Error"
            binding.poemDetailLines.text = "Could not load poem details."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
