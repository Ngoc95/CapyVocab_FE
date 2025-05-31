package com.example.capyvocab_fe.user.community.presentation.components

import com.example.capyvocab_fe.R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.MyGray
import com.example.capyvocab_fe.ui.theme.MyLightBlue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostThumbsGrid(
    images: List<String>?,
    onImageClick: (String) -> Unit
) {
    if(images.isNullOrEmpty() == false) {
        when (images.size) {
            0 -> {
            }

            1 -> {
                AsyncImage(
                    model = images[0],
                    contentDescription = "Image 1",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable { onImageClick(images[0]) },
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.default_avt),
                )
            }

            2 -> {
                Row(modifier = Modifier.fillMaxWidth()) {
                    images.forEachIndexed { index, url ->
                        AsyncImage(
                            model = url,
                            contentDescription = "Image $index",
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable { onImageClick(images[index]) },
                            contentScale = ContentScale.Crop
                        )
                        if (index == 0 ) Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            3 -> {
                Row(modifier = Modifier.fillMaxWidth()) {
                    images.forEachIndexed { index, url ->
                        AsyncImage(
                            model = url,
                            contentDescription = "Image $index",
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable { onImageClick(images[index]) },
                            contentScale = ContentScale.Crop
                        )
                        if (index == 0 || index == 1) Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            else -> {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(images.size) { index ->
                            AsyncImage(
                                model = images[index],
                                contentDescription = "Image $index",
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clickable { onImageClick(images[index]) },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PostImageGridPreview()
{
    CapyVocab_FETheme {

        PostThumbsGrid(
            images = listOf("1", "ff", "2", "3", "6", "3", "4", "44"),
            onImageClick = {},
        )
    }
}