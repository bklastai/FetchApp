package com.fetch.interview.network

import com.fetch.interview.domain.FetchItem
import com.fetch.interview.data.UnprocessedFetchItem

data class DownloadResult(
    private val _content: List<UnprocessedFetchItem>,
    val success: Boolean,
    val exception: Exception? = null
) {
    val content: List<FetchItem> = _content
        .filterNot { it.name.isNullOrBlank() }
        .groupBy { it.listId }.toSortedMap()
        .flatMap { entry -> entry.value.sortedBy { it.id } }
        .map { FetchItem(it.id, it.listId, it.name!!) }
}