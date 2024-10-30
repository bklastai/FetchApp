package com.fetch.interview.data

import kotlinx.serialization.Serializable

@Serializable
data class UnprocessedFetchItem(
    val id: Int,
    val listId: Int,
    val name: String?
)