package com.example.poe

import java.io.Serializable

/** Poem class. Contains data for each poem. Title, author, lines, and line count. **/
data class Poem(
    val title: String,
    val author: String,
    val lines: List<String> = emptyList()
): Serializable
