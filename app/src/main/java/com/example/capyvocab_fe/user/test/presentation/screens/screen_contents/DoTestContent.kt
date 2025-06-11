package com.example.capyvocab_fe.user.test.presentation.screens.screen_contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.user.test.domain.model.Folder
import com.example.capyvocab_fe.user.test.presentation.screens.components.TestFolderCard
import kotlinx.coroutines.delay

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { onSearchQueryChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Tìm kiếm") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        shape = RoundedCornerShape(30.dp),
        singleLine = true,
        colors = defaultTextFieldColors()
    )
}

@Composable
fun DoTestContent(
    folders: List<Folder>,
    isLoading: Boolean,
    isEndReached: Boolean,
    onFolderClick: (Folder) -> Unit,
    onSearchFolders: (String) -> Unit,
    onLoadMoreFolders: () -> Unit,
    onVoteClick: (Int) -> Unit,
    onUnVoteClick: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Debounce tìm kiếm để tránh gọi API quá nhiều khi người dùng đang nhập
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            isSearching = true
            delay(500) // Đợi 500ms sau khi người dùng ngừng gõ
            onSearchFolders(searchQuery)
        } else {
            // Nếu xóa hết từ khóa tìm kiếm, load lại tất cả folder
            onSearchFolders("")
        }
    }

    // Detect khi cuộn đến gần cuối
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()?.index ?: 0
                val totalItems = listState.layoutInfo.totalItemsCount
                if (lastVisibleItem >= totalItems - 2 && !isLoading && !isEndReached) {
                    onLoadMoreFolders()
                }
            }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )

            if (isSearching && folders.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color(0xFF42B3FF)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(folders) { index, folder ->
                        TestFolderCard(
                            folder = folder,
                            isOwner = false,
                            onClick = { onFolderClick(folder) },
                            onVoteClick = onVoteClick,
                            onUnVoteClick = onUnVoteClick,
                            onSettingClick = {}
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        // Load thêm nếu gần cuối
                        if (folders.size - 1 == index && !isLoading && !isEndReached) {
                            onLoadMoreFolders()
                        }
                    }
                    if (isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}