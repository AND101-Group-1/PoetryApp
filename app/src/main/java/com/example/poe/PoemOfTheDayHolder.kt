package com.example.poe

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Object to hold the poem of the day in memory
object PoemOfTheDayHolder {

    // Properties to store the poem and the date it was fetched
    private var dailyPoem: Poem? = null
    private var dateFetched: String? = null

    // Function to get the poem if it's still valid for today
    fun getPoemForToday(): Poem? {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // If the saved date is today, return the saved poem
        if (dateFetched == todayDate) {
            return dailyPoem
        }
        // Otherwise, the poem is stale, so return null
        return null
    }

    // Function to update the holder with a new poem
    fun updatePoem(newPoem: Poem) {
        dailyPoem = newPoem
        dateFetched = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}
