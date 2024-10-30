package com.fetch.interview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.unit.dp
import com.fetch.interview.R
import com.fetch.interview.domain.FetchItem

@Composable
fun FetchItemUI(
    data: FetchItem,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.padding(
            integerResource(id = R.integer.generic_list_item_padding).dp)
    ) {
        Text(text = data.listId.toString())
        Text(text = data.id.toString())
        Text(text = data.name)
    }
}