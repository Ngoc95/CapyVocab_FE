package com.example.capyvocab_fe.user.community.presentation

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.Black
import com.example.capyvocab_fe.ui.theme.MyLightBlue
import com.example.capyvocab_fe.user.community.presentation.components.SelectImage
import com.example.capyvocab_fe.user.community.presentation.components.TagsList
import kotlinx.coroutines.delay

@Composable
fun CreatePostScreen(
    viewModel: CommunityViewModel = hiltViewModel(),
    onBackClick:() -> Unit,
    navController: NavController
)
{
    BackHandler(enabled = true) {
        Log.d("CreatePostScreen", "clearScreenPost called")
        viewModel.onEvent(CommunityEvent.ClearScreenPost)
        navController.navigate(Route.UserCoursesScreen.route) {
            popUpTo(Route.UserCoursesScreen.route) {
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val state by viewModel.state.collectAsState()
    var visibleError by remember { mutableStateOf("") }

    var selectedImage by remember { mutableStateOf<String?>(null) }
    var tagsList = remember { mutableStateListOf<String>() }
    var content by remember { mutableStateOf("") }
    var imageList = remember { mutableStateListOf<Uri>() }


    // Khi errorMessage thay đổi, show  njm/snackbar trong 3 giây
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(3000) // hiện 3 giây
            visibleError = "" // ẩn sau 3 giây
        }
    }

    if (selectedImage != null) {
        Dialog( onDismissRequest = { selectedImage = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable { selectedImage = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris: List<Uri> ->
        uris.forEach { uri ->
            imageList.add(uri)
        }
    }

    CreatePostScreenContent(
        tagsList = tagsList,
        content = content,
        imgList = imageList,
        onAddTag = {tag -> if(tag !in tagsList) tagsList.add(tag) },
        onRemoveTag = { tag -> tagsList.remove(tag) },
        onBackClick = {
            onBackClick()
        },
        onSelectImage = { launcher.launch(arrayOf("image/*")) },
        onRemoveImage = {img -> imageList.remove(img) },
        onChangeContent = {str -> content = str},
        onSavePost = {
            if (tagsList.isEmpty() && content.isBlank() && imageList.isEmpty())
                visibleError = "Bài viết không được để trống"
            else
            {
                viewModel.onEvent(CommunityEvent.CreatePost(content, tagsList, imageList))
            }
        }
    )

}

@Composable
fun CreatePostScreenContent(
    tagsList: List<String>?,
    content: String?,
    imgList:List<Uri>?,
    onSelectImage:() -> Unit,
    onRemoveImage:(Uri) -> Unit,
    onAddTag:(String) -> Unit,
    onRemoveTag:(String) -> Unit,
    onBackClick:() -> Unit,
    onChangeContent:(String) -> Unit,
    onSavePost:() -> Unit,
)
{

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 20.dp, start = 8.dp, bottom = 10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.backicon),
                modifier = Modifier.size(40.dp)
                    .clickable { onBackClick() },
                contentDescription = null,
            )
            Text(
                text = "Tạo bài viết",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Scaffold(
            bottomBar = {
                Button(
                    onClick = {
                        onSavePost()
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MyLightBlue,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(50.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation()
                ) {
                    Text(
                        text = "Xong",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }
        ){ innerPadding ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.White)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Box(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 0.dp, bottom = 8.dp))
                    {
                        TagsList(
                            tags = tagsList,
                            onAddTag = { tag -> onAddTag(tag) },
                            onDeleteTag = { tag -> onRemoveTag(tag) }
                        )
                    }

                    Box(modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 0.dp))
                    {
                        OutlinedTextField(
                            value = if (content == null) "" else content,
                            onValueChange = {str -> onChangeContent(str) },
                            label = { Text("Nội dung bài viết") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .heightIn(min = 150.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Transparent,
                                focusedContainerColor = Transparent,
                                cursorColor = Color(0xFF5E4A45),
                                focusedIndicatorColor = Black,
                                unfocusedIndicatorColor = Color.Black,
                            ),
                        )
                    }
                    SelectImage(
                        imgList = imgList,
                        onAddImage = onSelectImage,
                        onDeleteImage = {image -> onRemoveImage(image)}
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePostPreview() {
    CreatePostScreenContent(
        tagsList = emptyList(),
        onAddTag = {},
        onRemoveTag = {},
        content = null,
        imgList = emptyList(),
        onBackClick = { },
        onSelectImage = { },
        onRemoveImage = { },
        onChangeContent = { },
        onSavePost = { }
    )
}






