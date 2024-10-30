package com.fetch.interview.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fetch.interview.R
import com.fetch.interview.domain.MainViewModel
import com.fetch.interview.ui.theme.FetchAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FetchApp(
    modifier: Modifier = Modifier,
    vm: MainViewModel = viewModel(factory = MainViewModel.Factory),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    FetchAppTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val fetchHappened = remember { mutableStateOf(false) }

        ExecuteOnVisible(
            lifecycleOwner = LocalLifecycleOwner.current,
            action = { fetchData(scope, vm::fetchData, fetchHappened) }
        )

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = { FetchAppTopBar(scope, vm::fetchData, fetchHappened) },
            modifier = modifier
        ) { innerPadding ->
            FetchItemList(
                items = vm.itemsFlow,
                modifier = modifier.padding(innerPadding)
            )

            if (vm.itemsFlow.isEmpty() && fetchHappened.value) {
                showErrorSnackBar(
                    scope = scope,
                    message = stringResource(id = R.string.somethings_wrong),
                    snackbarHostState = snackbarHostState,
                    resetErrorState = { fetchHappened.value = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FetchAppTopBar(
    scope: CoroutineScope,
    getDataInternal: suspend () -> Unit,
    fetchHappened: MutableState<Boolean>
) = CenterAlignedTopAppBar(
    title = { Text(text = stringResource(id = R.string.app_name)) },
    actions = {
        IconButton(onClick = { fetchData(scope, getDataInternal, fetchHappened) }) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(id = R.string.fetch_data)
            )
        }
    }
)

private fun fetchData(
    scope: CoroutineScope,
    fetchDataInternal: suspend () -> Unit,
    fetchHappened: MutableState<Boolean>
) {
        scope.launch {
            fetchDataInternal()
        }.invokeOnCompletion {
            fetchHappened.value = true
        }
}

private fun showErrorSnackBar(
    scope: CoroutineScope,
    message: String,
    snackbarHostState: SnackbarHostState,
    resetErrorState: () -> Unit
) {
    scope.launch {
        snackbarHostState.showSnackbar(message)
    }.invokeOnCompletion {
        resetErrorState()
    }
}