package com.example.capyvocab_fe.user.test.presentation.screens.screen_contents

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.user.test.domain.model.Folder
import com.example.capyvocab_fe.user.test.presentation.screens.components.TestFolderCard

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CreatedTestsContent(
    folders: List<Folder>,
    isLoading: Boolean,
    currentUser: User?,
    onFolderClick: (Folder) -> Unit,
    onVoteFolder: (Int) -> Unit,
    onUnvoteFolder: (Int) -> Unit
) {
    val createdFolders = folders.filter { it.createdBy?.id == currentUser?.id }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF42B3FF)
            )
        } else if (createdFolders.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Bạn chưa tạo folder nào",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(createdFolders) { item ->
                    TestFolderCard(
                        folder = item,
                        onClick = { onFolderClick(item) },
                        onVoteClick = onVoteFolder,
                        onUnVoteClick = onUnvoteFolder
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}