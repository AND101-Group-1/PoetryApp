package com.example.poe

import android.util.Log
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

/** API functionality. Algorithms for getRandomPoem, searchByTitle, and searchByAuthor**/
class PoetryApiClient {

    /**function to get 1 random poem**/
    fun getRandomPoem(callback: (Poem?) -> Unit) {

        val client = AsyncHttpClient()

        client.get("https://poetrydb.org/random/1.json", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Poem", "response successful: $json")

                try {
                    val result = json.jsonArray
                    val poemObject = result.getJSONObject(0)

                    //get poem elements
                    val title = poemObject.getString("title")
                    val author = poemObject.getString("author")
                    val linesArray = poemObject.getJSONArray("lines")

                    val lines = ArrayList<String>()
                    for (i in 0 until linesArray.length()) {
                        lines.add(linesArray.getString(i))
                    }

                    val poem = Poem(title, author, lines)
                    callback(poem)
                    
                    //print poem elements to logcat
                    Log.d("Poem", "Title: $title")
                    Log.d("Poem", "Author: $author")
                    Log.d("Poem", "Lines: $linesArray")
                } catch (e: Exception) {
                    Log.e("Poem", "Error parsing poem", e)
                    callback(null)
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Poem Error", errorResponse)
                callback(null)
            }
        })

    }

    /**function to get all poems by an author
    it gets the title, lines (poem itself), linecount (number of lines in the poem)**/
    fun searchByAuthor(author: String){
        val client = AsyncHttpClient()

        client.get("https://poetrydb.org/author/${author}/title,lines,linecount.json", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Poem", "response successful for author $author: $json")


                val resultsArray = json.jsonArray

                //traverse resultsArray to get all poems and view them in the logcat
                for (i in 0 until resultsArray.length()) {
                    // Get the poem object at the current position
                    val poemObject = resultsArray.getJSONObject(i)

                    // Get poem elements from the current object
                    val title = poemObject.getString("title")
                    val linesArray = poemObject.getJSONArray("lines")
                    val linecount = poemObject.getString("linecount")

                    // Print in the logcat the elements for each poem
                    Log.d("Poem", "Title: $title")
                    Log.d("Poem", "Line Count: $linecount")
                    Log.d("Poem", "Lines: $linesArray")
                    Log.d("Poem", "---")
                }


            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Poem Error for author $author", errorResponse)
            }
        })
    }

    /**function to search a poem by Title
    it gets the author, title, lines (poem itself), linecount (number of lines in the poem) **/
    fun searchByTitle(title: String){
        val client = AsyncHttpClient()

        client.get("https://poetrydb.org/title/$title/author,title,lines,linecount.json", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Poem", "response successful for title $title: $json")


                val result = json.jsonArray
                val poemObject = result.getJSONObject(0)

                //get poem elements
                val title = poemObject.getString("title")
                val author = poemObject.getString("author")
                val linesArray = poemObject.getJSONArray("lines")

                //print poem elements to logcat
                Log.d("Poem", "Title: $title")
                Log.d("Poem", "Author: $author")
                Log.d("Poem", "Lines: $linesArray")


            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Poem Error for title $title", errorResponse)
            }
        })
    }

}