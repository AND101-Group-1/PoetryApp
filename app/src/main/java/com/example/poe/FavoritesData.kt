package com.example.poe
/** This object stores the favorite poems array. **/
object FavoritesData {
    val favorites = mutableListOf<Poem>()

    fun addFavorite(poem: Poem) {
        // Avoid duplicates
        if (!favorites.any { it.title == poem.title && it.author == poem.author }) {
            favorites.add(poem)
        }
    }
}