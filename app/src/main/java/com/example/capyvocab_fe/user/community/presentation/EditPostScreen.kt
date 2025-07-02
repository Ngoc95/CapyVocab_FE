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
import androidx.compose.foundation.layout.imePadding
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
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.core.ui.components.LoadingDialog
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.Black
import com.example.capyvocab_fe.ui.theme.MyLightBlue
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.presentation.components.SelectImage
import com.example.capyvocab_fe.user.community.presentation.components.TagsList
import kotlinx.coroutines.delay

// Wrapper class to handle both local Uri and remote URLs
sealed class ImageItem {
    data class LocalImage(val uri: Uri) : ImageItem()
    data class RemoteImage(val url: String) : ImageItem()
    
    fun getDisplayUri(): Uri {
        return when (this) {
            is LocalImage -> uri
            is RemoteImage -> Uri.parse(url)
        }
    }
    
    fun getUrlString(): String {
        return when (this) {
            is LocalImage -> uri.toString()
            is RemoteImage -> url
        }
    }
}

@Composable
fun EditPostScreen(
    post: Post,
    viewModel: CommunityViewModel = hiltViewModel(),
    onBackClick:() -> Unit,
    navController: NavController
)
{
    BackHandler(enabled = true) {
        Log.d("CreatePostScreen", "clearScreenPost called")
        viewModel.onEvent(CommunityEvent.ClearScreenPost)
        navController.popBackStack()
    }
    val state by viewModel.state.collectAsState()
    var visibleError by remember { mutableStateOf("") }

    var selectedImage by remember { mutableStateOf<String?>(null) }
    val tagsList = remember { mutableStateListOf<String>().apply { addAll(post.tags ?: emptyList()) } }
    var content by remember { mutableStateOf(post.content ?: "") }
    val imageList = remember {
        mutableStateListOf<ImageItem>().apply {
            addAll((post.thumbnails ?: emptyList()).map { 
                ImageItem.RemoteImage(it)
            })
        }
    }

    // Khi errorMessage thay đổi, show  njm/snackbar trong 3 giây
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(3000) // hiện 3 giây
            visibleError = "" // ẩn sau 3 giây
        }
    }

    // Khi post được cập nhật thành công, navigate back
    LaunchedEffect(state.isPostUpdated) {
        if (state.isPostUpdated) {
            viewModel.onEvent(CommunityEvent.ResetPostUpdated)
            onBackClick()
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
            imageList.add(ImageItem.LocalImage(uri))
        }
    }

    LoadingDialog(isLoading = state.isLoading)
    
    FocusComponent {
        EditPostScreenContent(
            tagsList = tagsList,
            content = content,
            imgList = imageList,
            errorMessage = visibleError,
            isLoading = state.isLoading,
            onAddTag = {tag -> if(tag !in tagsList) tagsList.add(tag) },
            onRemoveTag = { tag -> tagsList.remove(tag) },
            onBackClick = {
                onBackClick()
            },
            onSelectImage = { launcher.launch(arrayOf("image/*")) },
            onRemoveImage = {img -> imageList.remove(img) },
            onChangeContent = {str -> content = str},
            onSavePost = {
                if (tagsList.isEmpty() && content.isBlank() && imageList.isEmpty()) {
                    visibleError = "Bài viết không được để trống"
                } else {
                    // Convert ImageItems to Uri list for upload
                    val localImages = imageList.filterIsInstance<ImageItem.LocalImage>().map { it.uri }
                    val existingThumbnails = imageList.filterIsInstance<ImageItem.RemoteImage>().map { it.url }
                    viewModel.onEvent(CommunityEvent.UpdatePost(post.id, content, tagsList, localImages, existingThumbnails))
                }
            }
        )
    }

}

@Composable
fun EditPostScreenContent(
    tagsList: List<String>?,
    content: String?,
    imgList:List<ImageItem>?,
    errorMessage: String,
    isLoading: Boolean,
    onSelectImage:() -> Unit,
    onRemoveImage:(ImageItem) -> Unit,
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
            .imePadding()
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
                text = "Sửa bài viết",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Scaffold(
            bottomBar = {
                Button(
                    onClick = {
                        onSavePost()
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
                                cursorColor = Black,
                                focusedIndicatorColor = Black,
                                unfocusedIndicatorColor = Color.Black,
                            ),
                        )
                    }
                    SelectImage(
                        imgList = imgList?.map { it.getDisplayUri() },
                        onAddImage = onSelectImage,
                        onDeleteImage = {image -> 
                            imgList?.find { it.getDisplayUri().toString() == image.toString() }?.let { onRemoveImage(it) }
                        }
                    )

                }
            }
        }
        if (errorMessage.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.Red, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage,
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditPostPreview() {
    EditPostScreenContent(
        tagsList = emptyList(),
        onAddTag = {},
        onRemoveTag = {},
        content = null,
        imgList = emptyList(),
        onBackClick = { },
        onSelectImage = { },
        onRemoveImage = { },
        onChangeContent = { },
        onSavePost = { },
        errorMessage = "hgege",
        isLoading = false
    )
}






