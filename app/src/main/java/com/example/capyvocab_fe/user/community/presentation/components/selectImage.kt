package com.example.capyvocab_fe.user.community.presentation.components

import android.R
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SelectImage(
    imgList: List<Uri>?,
    onAddImage:() -> Unit,
    onDeleteImage:(Uri) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth()
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        )
        {
            Button(
                onClick = onAddImage,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                ),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Xóa",
                    tint = Color.White,
                    modifier = Modifier
                        .size(50.dp)
                )
            }
            if (!imgList.isNullOrEmpty())
            {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(15.dp),
                )
                {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(15.dp)),
                    )
                    {
                        AsyncImage(
                            model = imgList[0],
                            contentDescription = null,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { onDeleteImage(imgList[0]) }
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Xóa",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(2.dp)
                                .size(22.dp) // icon nhỏ gọn
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
            else{
                Spacer(modifier = Modifier.weight(1f))
            }

        }
        if(!imgList.isNullOrEmpty() && imgList.size > 1)
        {
            val itemsList = imgList.drop(1);
            itemsList.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { item ->
                        if(item != null) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(15.dp),
                            )
                            {
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(15.dp)),
                                )
                                {
                                    AsyncImage(
                                        model = item,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .background(Color.LightGray)
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .clickable { onDeleteImage(item) }
                                        .align(Alignment.TopEnd)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Xóa",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .size(22.dp) // icon nhỏ gọn
                                            .clip(CircleShape)
                                            .background(Color.LightGray)
                                            .align(Alignment.Center)
                                    )
                                }


                            }
                        }
                    }
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800)
@Composable
fun SelectImagePreview() {

    val imgList = listOf(
        Uri.parse("fkdf"),
        Uri.parse("gkfdhg"),
        Uri.parse("fdsg"),
        Uri.parse("fdsg"),
        Uri.parse("fdsg")
    )

    SelectImage(
        imgList = imgList,
        onDeleteImage = {},
        onAddImage = {}
    )
}

