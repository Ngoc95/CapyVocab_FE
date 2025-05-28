package com.example.capyvocab_fe.user.review.presentation

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.core.ui.components.BottomFeedbackCard
import com.example.capyvocab_fe.core.ui.components.CustomProgressBarWithIcon
import com.example.capyvocab_fe.core.ui.components.TopBarTitle
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.user.navigator.components.UserTopBar
import com.example.capyvocab_fe.user.review.presentation.components.ChooseMeaningQuestion
import com.example.capyvocab_fe.user.review.presentation.components.FillInQuestion
import com.example.capyvocab_fe.user.review.presentation.components.ListenAndChooseQuestion
import com.example.capyvocab_fe.user.review.presentation.components.TrueFalseQuestion
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state
    val allWords = viewModel.allWords
    var showExitConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onEvent(ReviewEvent.LoadWords)
        viewModel.onEvent(ReviewEvent.LoadProgressSummary)
    }

    if (showExitConfirmation) {
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = { Text("Quay lại") },
            text = { Text("Tiến trình sẽ không được lưu. Bạn có chắc muốn thoát?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitConfirmation = false
                    navController.popBackStack(Route.UserReviewScreen.route, inclusive = true)
                }) { Text(text = "Thoát", color = Color.Black) }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmation = false }) {
                    Text(text = "Hủy", color = Color.Black)
                }
            }
        )
    }

    // chỉ khi học mới bắt back
    if (state.hasStarted) {
        BackHandler {
            showExitConfirmation = true
        }
    }

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Đang tải từ ôn tập...", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        state.completed -> {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.reset()
                        navController.navigate(Route.UserReviewScreen.route) {
                            popUpTo(Route.UserReviewScreen.route) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) { Text("Tiếp tục") }
                },
                title = { Text("Hoàn tất ôn tập") },
                text = { Text("Bạn đã ôn được ${state.totalUpdated} từ") }
            )
        }

        state.isEmpty -> {
            Column {
                // Top bar
                UserTopBar()

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProgressChartView(items = state.progressChart, total = state.totalLearnWord)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Bạn không có từ nào cần ôn tập, hãy học từ mới!",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                        )
                    }

                }
            }
        }

        !state.hasStarted -> {
            ReviewOverviewScreen(
                totalWords = state.totalWordsToReview,
                preparedCount = state.preparedCount,
                onStart = { viewModel.onEvent(ReviewEvent.StartReview) },
                progressItems = state.progressChart,
                totalLearned = state.totalLearnWord
            )
        }

        else -> {
            val word = state.words.firstOrNull()
            if (word != null) {
                val questionType = remember(word.id to state.questionSessionId) {
                    generateQuestionType(word)
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 30.dp, start = 16.dp, end = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        // Thanh tiến độ
                        CustomProgressBarWithIcon(
                            progress = state.correctCount / state.preparedCount.toFloat(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Các câu hỏi
                        key(word.id) {
                            when (questionType) {
                                QuestionType.ChooseMeaning -> {
                                    val options = remember(word.id) { getRandomMeanings(word, allWords) }

                                    LaunchedEffect(word.id) {
                                        viewModel.setCorrectAnswer(word.meaning)
                                    }
                                    ChooseMeaningQuestion(
                                        word = word,
                                        options = options,
                                        onAnswer = {
                                            viewModel.onEvent(ReviewEvent.SubmitAnswer(it))
                                        },
                                        isAnswerShown = state.showFeedback,
                                        correctAnswer = word.meaning,
                                        selectedAnswer = state.selectedAnswer
                                    )
                                }

                                QuestionType.TrueFalse -> {
                                    val isCorrect = remember(word.id) { Random.nextBoolean() }
                                    val meaning = remember(word.id) {
                                        if (isCorrect) word.meaning else getRandomIncorrectMeaning(word, allWords)
                                    }
                                    val correctAnswer = if (meaning == word.meaning) "Đúng" else "Sai"

                                    LaunchedEffect(word.id) {
                                        viewModel.setCorrectAnswer(correctAnswer)
                                    }
                                    TrueFalseQuestion(
                                        word = word,
                                        displayedMeaning = meaning,
                                        onAnswer = { userSelected ->
                                            val answerText = if (userSelected) "Đúng" else "Sai"
                                            viewModel.onEvent(ReviewEvent.SubmitAnswer(answerText))
                                        },
                                        isAnswerShown = state.showFeedback,
                                        isCorrectAnswer = state.isCorrectAnswer,
                                        selectedAnswerBoolean = state.selectedAnswerBoolean
                                    )
                                }

                                QuestionType.FillIn -> {
                                    LaunchedEffect(word.id) {
                                        viewModel.setCorrectAnswer(word.content)
                                    }

                                    FillInQuestion(
                                        word = word,
                                        userAnswer = state.selectedAnswer ?: "",
                                        onAnswer = {
                                            viewModel.onEvent(ReviewEvent.SubmitAnswer(it))
                                        },
                                        isAnswerShown = state.showFeedback,
                                        correctAnswer = word.content
                                    )
                                }

                                QuestionType.ListenAndChoose -> {
                                    val options = remember(word.id) { getRandomChoices(word, allWords) }
                                    LaunchedEffect(word.id) {
                                        viewModel.setCorrectAnswer(word.content)
                                    }

                                    ListenAndChooseQuestion(
                                        word = word,
                                        options = options,
                                        onAnswer = {
                                            viewModel.onEvent(ReviewEvent.SubmitAnswer(it))
                                        },
                                        isAnswerShown = state.showFeedback,
                                        correctAnswer = word.content,
                                        selectedAnswer = state.selectedAnswer
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(100.dp))
                    }

                    if (state.showFeedback) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            BottomFeedbackCard(
                                isCorrect = state.isCorrectAnswer,
                                word = word,
                                onContinue = { viewModel.onEvent(ReviewEvent.Continue) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class QuestionType { FillIn, ListenAndChoose, ChooseMeaning, TrueFalse }

fun generateQuestionType(word: Word): QuestionType {
    val types = mutableListOf<QuestionType>()
    if (word.audio.isNotBlank()) types.add(QuestionType.ListenAndChoose)
    if (word.content.isNotBlank() && word.meaning.isNotBlank()) {
        types.addAll(listOf(QuestionType.FillIn, QuestionType.ChooseMeaning, QuestionType.TrueFalse))
    }
    return types.random()
}

fun getRandomChoices(correctWord: Word, words: List<Word>, size: Int = 4): List<String> {
    val options = words.filter { it.id != correctWord.id }.shuffled().take(size - 1).map { it.content }
    return (options + correctWord.content).shuffled()
}

fun getRandomMeanings(correctWord: Word, words: List<Word>, size: Int = 4): List<String> {
    val options = words.filter { it.id != correctWord.id }.shuffled().take(size - 1).map { it.meaning }
    return (options + correctWord.meaning).shuffled()
}

fun getRandomIncorrectMeaning(correctWord: Word, words: List<Word>): String {
    return words.firstOrNull { it.id != correctWord.id }?.meaning ?: "Sai nghĩa"
}

@Composable
fun ReviewOverviewScreen(
    totalWords: Int,
    preparedCount: Int,
    onStart: () -> Unit,
    progressItems: List<ProgressChartItem>,
    totalLearned: Int
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        UserTopBar()

        Spacer(Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            ProgressChartView(items = progressItems, total = totalLearned)

            Spacer(modifier = Modifier.height(20.dp))

//            Text("Bạn có $totalWords từ cần ôn", style = MaterialTheme.typography.headlineSmall)
            Text("Chuẩn bị ôn tập: $preparedCount từ", style = MaterialTheme.typography.titleMedium, fontSize = 16.sp)

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF4FC3F7), Color(0xFF1565C0))
                        )
                    )
                    .clickable { onStart() }
                    .padding(horizontal = 48.dp, vertical = 16.dp)
            ) {
                Text(
                    "Ôn tập ngay",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ProgressChartView(items: List<ProgressChartItem>, total: Int) {
    if (items.isEmpty()) return

    val maxCount = items.maxOfOrNull { it.count } ?: 1
    val barColors = listOf(
        Color(0xFFF44336), // đỏ
        Color(0xFFFFC107), // vàng
        Color(0xFF03A9F4), // xanh dương nhạt
        Color(0xFF3F51B5), // xanh dương đậm
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       // Text("Tổng từ đã học: $total" , style = MaterialTheme.typography.titleMedium)
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color(0xFF920000))) {
                    append("$total")
                }
                append("  từ đã học chia theo cấp độ")
            },
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Biểu đồ cột
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            items.forEachIndexed { index, item ->
                val ratio = item.count.toFloat() / maxCount
                val barHeight = if (item.count == 0) 8.dp else 180.dp * ratio

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text("${item.count} từ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .height(barHeight)
                            .width(28.dp)
                            .background(
                                color = barColors.getOrElse(index) { Color.Gray },
                                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                            )
                    )
                }
            }
        }

        // Đường kẻ ngang đặt riêng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(50)) // Bo tròn tối đa
                .background(Color(0xFFDDDDDD))
                .padding(top = 2.dp, start = 20.dp, end = 20.dp)
        )

//        val maxBarHeight = 200.dp
//        val minBarHeight = 8.dp // để cột "0 từ" vẫn hiển thị màu
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(maxBarHeight + 30.dp), // bar + count text (bỏ label ở đây)
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            verticalAlignment = Alignment.Bottom
//        ) {
//            items.forEachIndexed { index, item ->
//                val ratio = item.count.toFloat() / maxCount
//                val barHeight = if (item.count == 0) minBarHeight else maxBarHeight * ratio
//
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Bottom
//                ) {
//                    // Số lượng từ phía trên cột
//                    Text("${item.count} từ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    // Cột màu
//                    Box(
//                        modifier = Modifier
//                            .height(barHeight)
//                            .width(28.dp)
//                            .background(
//                                color = barColors.getOrNull(index) ?: Color.Gray,
//                                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
//                            )
//                    )
//                }
//            }
//        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row mới chứa label 1 2 3 4, căn đều với bar phía trên
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, _ ->
                Text(
                    (index + 1).toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(28.dp), // bằng với width của cột bar
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
