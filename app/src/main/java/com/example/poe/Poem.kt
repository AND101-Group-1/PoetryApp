package com.example.poe

/** Poem class. Contains data for each poem. Title, author, lines, and line count. **/
data class Poem(
    val title: String,
    val author: String,
    val lines: List<String> = emptyList()
)
