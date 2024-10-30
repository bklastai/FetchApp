package com.fetch.interview.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fetch.interview.R
import com.fetch.interview.domain.FetchItem
import com.fetch.interview.ui.theme.Purple80
import com.fetch.interview.ui.theme.PurpleGrey80

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FetchItemList(
    items: SnapshotStateList<FetchItem>,
    modifier: Modifier
) = LazyColumn(
    modifier = modifier
) {
    if (items.isNotEmpty()) {
        stickyHeader {
            Surface {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(integerResource(id = R.integer.generic_list_item_padding).dp)
                ) {
                    Text(text = stringResource(id = R.string.list_id))
                    Text(text = stringResource(id = R.string.item_id))
                    Text(text = stringResource(id = R.string.item_name))
                }
            }
        }
    }
    itemsIndexed(
        items = items,
        key = { _, i -> i.id },
        contentType = { _, item -> item.listId}
    ) {_, item ->
        FetchItemUI(
            data = item,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    if (item.listId % 2 == 0) PurpleGrey80 else Purple80
                )
        )
    }
}