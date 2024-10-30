package com.fetch.interview.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fetch.interview.R

@Composable
fun ColumnScope.FetchDataButton(
    onClick: () -> Unit,
    modifier: Modifier
) = OutlinedButton(
    onClick = onClick,
    modifier = Modifier
        .wrapContentSize()
        .align(alignment = Alignment.CenterHorizontally)
        .padding(integerResource(id = R.integer.generic_button_padding).dp)
) {
    Text(text = stringResource(id = R.string.fetch_data))
}