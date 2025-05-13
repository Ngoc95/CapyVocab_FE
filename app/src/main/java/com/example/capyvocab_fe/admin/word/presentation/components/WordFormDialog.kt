package com.example.capyvocab_fe.admin.word.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.core.ui.components.FormActionButtons
import com.example.capyvocab_fe.core.ui.components.OverlaySnackbar
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@Composable
fun WordFormDialog(
    word: Word?,
    errorMessage: String,
    onDismiss: () -> Unit,
    onSave: (Word, Uri?) -> Unit,
    onDelete: (() -> Unit)
) {
    var content by remember { mutableStateOf(word?.content ?: "") }
    var pronunciation by remember { mutableStateOf(word?.pronunciation ?: "") }
    var position by remember { mutableStateOf(word?.position ?: "") }
    var meaning by remember { mutableStateOf(word?.meaning ?: "") }
    var example by remember { mutableStateOf(word?.example ?: "") }
    var translateExample by remember { mutableStateOf(word?.translateExample ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedAudioUri by remember { mutableStateOf<Uri?>(null) }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val dialogWidth = if (screenWidth > 600.dp) 500.dp else screenWidth - 32.dp

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF66E6FF),
                modifier = Modifier
                    .width(dialogWidth)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(13.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    WordFormHeader(
                        word = word,
                        selectedImageUri = selectedImageUri,
                        selectedAudioUri = selectedAudioUri,
                        onImageSelected = { selectedImageUri = it },
                        onAudioSelected = { selectedAudioUri = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    WordFormFields(
                        content = content,
                        onContentChange = { content = it },
                        pronunciation = pronunciation,
                        onPronunciationChange = { pronunciation = it },
                        position = position,
                        onPositionChange = { position = it },
                        meaning = meaning,
                        onMeaningChange = { meaning = it },
                        example = example,
                        onExampleChange = { example = it },
                        translateExample = translateExample,
                        onTranslateExampleChange = { translateExample = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FormActionButtons(
                        isEditMode = word != null,
                        onDelete = onDelete,
                        onCancel = onDismiss,
                        onSave = {
                            val updatedWord = word?.copy(
                                content = content,
                                pronunciation = pronunciation,
                                position = position,
                                meaning = meaning,
                                rank = word.rank,
                                audio = selectedAudioUri?.toString() ?: "",
                                image = selectedImageUri?.toString() ?: "",
                                example = example,
                                translateExample = translateExample
                            ) ?: Word(
                                id = 0,
                                content = content,
                                pronunciation = pronunciation,
                                position = position,
                                meaning = meaning,
                                rank = "",
                                audio = "",
                                image = "",
                                example = example,
                                translateExample = translateExample
                            )
                            onSave(updatedWord, selectedImageUri)
                        }
                    )
                }
            }
        }
        OverlaySnackbar(message = errorMessage)
    }
}

@Composable
fun WordFormHeader(
    word: Word?,
    selectedImageUri: Uri?,
    selectedAudioUri: Uri?,
    onImageSelected: (Uri) -> Unit,
    onAudioSelected: (Uri) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        WordImagePicker(
            imageUrl = selectedImageUri?.toString() ?: word?.image,
            onImageSelected = onImageSelected,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (word != null) {
                Text(
                    text = "ID: ${word.id}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            WordAudioPicker(
                audioUrl = selectedAudioUri?.toString() ?: word?.audio,
                onAudioSelected = onAudioSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )

        }
    }
}

@Composable
fun WordFormFields(
    content: String,
    onContentChange: (String) -> Unit,
    pronunciation: String,
    onPronunciationChange: (String) -> Unit,
    position: String,
    onPositionChange: (String) -> Unit,
    meaning: String,
    onMeaningChange: (String) -> Unit,
    example: String,
    onExampleChange: (String) -> Unit,
    translateExample: String,
    onTranslateExampleChange: (String) -> Unit
) {
    WordTextField(title = "Từ vựng", value = content, onValueChange = onContentChange)
    WordTextField(title = "Phát âm", value = pronunciation, onValueChange = onPronunciationChange)
    WordTextField(title = "Loại từ", value = position, onValueChange = onPositionChange)
    WordTextField(title = "Ý nghĩa", value = meaning, onValueChange = onMeaningChange)
    WordTextField(title = "Ví dụ", value = example, onValueChange = onExampleChange)
    WordTextField(title = "Dịch ví dụ", value = translateExample, onValueChange = onTranslateExampleChange)
}

@Composable
private fun WordTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 7.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp, max = 120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun WordImagePicker(
    imageUrl: String?,
    onImageSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .clickable { imagePickerLauncher.launch("image/*") }
    ) {
        if (imageUrl.isNullOrBlank()) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Pick Image",
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Select Image",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun WordAudioPicker(
    audioUrl: String?,
    onAudioSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onAudioSelected(it) }
    }

    val fileName = audioUrl?.substringAfterLast("/") ?: "Select audio"

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { audioPickerLauncher.launch("audio/*") }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = "Audio Icon",
                tint = Color.DarkGray
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = fileName,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordFormDialogPreview() {
    CapyVocab_FETheme {
        val sampleWord = Word(
            id = 1,
            content = "apple",
            pronunciation = "/ˈæpl/",
            position = "noun",
            meaning = "a round fruit with red or green skin",
            rank = "2",
            audio = "https://example.com/audio.mp3",
            image = "https://example.com/image.jpg",
            example = "She ate an apple for lunch.",
            translateExample = "Cô ấy đã ăn một quả táo vào bữa trưa ahihih hihihi."
        )

        WordFormDialog (
            word = null,
            errorMessage = "",
            onDismiss = {},
            onSave = {Word, Uri -> },
            onDelete = {},
        )
    }
}


