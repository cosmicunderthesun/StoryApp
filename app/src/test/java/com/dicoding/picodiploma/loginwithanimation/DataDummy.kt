package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.remote.respone.ListStoryItem

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                i.toString(),
                "createdAt $i",
                "name  $i",
                "desc $i",
                i.toDouble(),
                "id $i",
                i.toDouble()
            )
            items.add(quote)
        }
        return items
    }
}