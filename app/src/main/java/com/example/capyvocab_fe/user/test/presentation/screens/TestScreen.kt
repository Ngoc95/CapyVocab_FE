package com.example.capyvocab_fe.user.test.presentation.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.payment.presentation.PaymentUiEvent
import com.example.capyvocab_fe.user.payment.presentation.PaymentUiState
import com.example.capyvocab_fe.user.payment.presentation.PaymentViewModel
import com.example.capyvocab_fe.user.test.data.remote.model.CreateFolderRequest
import com.example.capyvocab_fe.user.test.domain.model.Folder
import com.example.capyvocab_fe.user.test.presentation.screens.screen_contents.CreateTestContent
import com.example.capyvocab_fe.user.test.presentation.screens.screen_contents.CreatedTestsContent
import com.example.capyvocab_fe.user.test.presentation.screens.screen_contents.DoTestContent
import com.example.capyvocab_fe.user.test.presentation.screens.screen_contents.EnterCodeContent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseViewModel
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TestScreen(
    viewModel: ExerciseViewModel = hiltViewModel(),
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val paymentState by paymentViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedFolder by remember { mutableStateOf<Folder?>(null) }
    
    // WebView payment launcher
    val paymentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val vnpParams = listOf(
                "vnp_Amount", "vnp_BankCode", "vnp_BankTranNo", "vnp_CardType",
                "vnp_OrderInfo", "vnp_PayDate", "vnp_ResponseCode", "vnp_TmnCode", "vnp_TransactionNo",
                "vnp_TransactionStatus", "vnp_TxnRef", "vnp_SecureHash", "isSuccess", "orderId"
            )
            val paramMap = vnpParams.associateWith { data?.getStringExtra(it) ?: "" }
            paymentViewModel.onEvent(
                PaymentUiEvent.PaymentCompleted(paramMap)
            )
        }
    }

    // Show loading overlay when processing payment return
    if (paymentState.isProcessingPayment) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Color.White
            )
        }
    }
    LaunchedEffect(state.currentTab) {
        if (state.currentTab == 2) { // Khi chuyển sang tab CreatedTests
            viewModel.onEvent(ExerciseEvent.LoadFolders()) // Load lại danh sách folder
        }
    }
    LaunchedEffect(Unit) {
        viewModel.onEvent(ExerciseEvent.LoadFolders())
    }
    // Handle payment navigation
    LaunchedEffect(paymentState.paymentUrl) {
        paymentState.paymentUrl?.let { url ->
            val intent = Intent(context, com.example.capyvocab_fe.user.payment.presentation.PaymentWebViewActivity::class.java)
            intent.putExtra("orderUrl", url)
            paymentLauncher.launch(intent)
            paymentViewModel.onEvent(PaymentUiEvent.ClearPaymentUrl)
        }
    }
    // Handle payment success dialog
    LaunchedEffect(paymentState.showSuccessDialog) {
        if (paymentState.showSuccessDialog) {
            // Show success dialog for 2 seconds
            delay(2000)
            paymentViewModel.onEvent(PaymentUiEvent.DismissSuccess)
            // Navigate back to the test list
            navController.navigate(Route.UserTestScreen.route) {
                popUpTo(Route.UserTestScreen.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(state.error, state.currentFolder) {
        if (selectedFolder == null) return@LaunchedEffect

        if (state.error != null) {
            // Show payment dialog
            paymentViewModel.onEvent(PaymentUiEvent.LoadFolder(selectedFolder!!))
            paymentViewModel.onEvent(PaymentUiEvent.InitiatePayment)
            viewModel.onEvent(ExerciseEvent.ResetError)
        } else if (state.currentFolder != null) {
            // Navigate to detail screen
            navController.navigate("${Route.TestDetailScreen.route}/${state.currentFolder!!.id}")
        }
    }

    FocusComponent {
        TestScreenContent(
            folders = state.folders,
            user = state.currentUser,
            currentTab = state.currentTab,
            isLoading = state.isLoading,
            isEndReached = state.isEndReached,
            successMessage = state.successMessage,
            paymentState = paymentState,
            navController = navController,
            onNavigateToDoTest = { viewModel.onEvent(ExerciseEvent.NavigateToDoTest) },
            onNavigateToEnterCode = { viewModel.onEvent(ExerciseEvent.NavigateToEnterCode) },
            onNavigateToCreatedTests = { viewModel.onEvent(ExerciseEvent.NavigateToCreatedTests) },
            onNavigateToCreateTest = { viewModel.onEvent(ExerciseEvent.NavigateToCreateTest) },
            onLoadFolders = { name, code ->
                viewModel.onEvent(ExerciseEvent.LoadFolders(name, code))
            },
            onLoadMoreFolders = { viewModel.onEvent(ExerciseEvent.LoadMoreFolders) },
            onCreateFolder = { request, onSuccess, onError ->
                viewModel.onEvent(ExerciseEvent.CreateFolder(request, onSuccess, onError))
            },
            onVoteFolder = { id -> viewModel.onEvent(ExerciseEvent.VoteFolder(id)) },
            onUnvoteFolder = { id -> viewModel.onEvent(ExerciseEvent.UnvoteFolder(id)) },
            onClearSuccessMessage = { viewModel.onEvent(ExerciseEvent.ResetSuccess) },
            onFolderClick = { folder ->
                selectedFolder = folder
                viewModel.onEvent(ExerciseEvent.GetFolderById(folder.id))
            },
            onConfirmPayment = { paymentViewModel.onEvent(PaymentUiEvent.ConfirmPayment) },
            onCancelPayment = { paymentViewModel.onEvent(PaymentUiEvent.CancelPayment) },
            onDismissPaymentError = { paymentViewModel.onEvent(PaymentUiEvent.DismissError) },
            onDismissSuccess = { paymentViewModel.onEvent(PaymentUiEvent.DismissSuccess) }
        )
    }
}

@Composable
fun TestScreenContent(
    folders: List<Folder>,
    user: User?,
    currentTab: Int,
    isLoading: Boolean,
    isEndReached: Boolean,
    successMessage: String?,
    paymentState: PaymentUiState,
    navController: NavController,
    onNavigateToDoTest: () -> Unit,
    onNavigateToEnterCode: () -> Unit,
    onNavigateToCreatedTests: () -> Unit,
    onNavigateToCreateTest: () -> Unit,
    onLoadFolders: (String?, String?) -> Unit,
    onLoadMoreFolders: () -> Unit,
    onCreateFolder: (CreateFolderRequest, (Folder) -> Unit, (String) -> Unit) -> Unit,
    onVoteFolder: (Int) -> Unit,
    onUnvoteFolder: (Int) -> Unit,
    onClearSuccessMessage: () -> Unit,
    onFolderClick: (Folder) -> Unit,
    onConfirmPayment: () -> Unit,
    onCancelPayment: () -> Unit,
    onDismissPaymentError: () -> Unit,
    onDismissSuccess: () -> Unit
) {
    val tabs = listOf("Làm test", "Nhập code", "Đã tạo", "Tạo mới")

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header với thông tin người dùng
        UserInfoHeader(
            user = user
        )

        // Thanh điều hướng chức năng
        TabRow(
            selectedTabIndex = currentTab,
            containerColor = Color.White
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = currentTab == index,
                    onClick = {
                        // Gửi sự kiện navigation tương ứng
                        when (index) {
                            0 -> onNavigateToDoTest()
                            1 -> onNavigateToEnterCode()
                            2 -> onNavigateToCreatedTests()
                            3 -> onNavigateToCreateTest()
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp),
                    content = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Biểu tượng cho từng tab
                            Icon(
                                painter = when (index) {
                                    0 -> painterResource(id = R.drawable.user_test_do)
                                    1 -> painterResource(id = R.drawable.user_test_code)
                                    2 -> painterResource(id = R.drawable.user_test_created)
                                    else -> painterResource(id = R.drawable.user_test_create)
                                },
                                contentDescription = title,
                                modifier = Modifier.size(30.dp),
                                tint = Color.Unspecified
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Văn bản dưới biểu tượng
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                color = if (currentTab == index) Color(0xFF42B3FF) else Color.Black
                            )
                        }
                    }
                )
            }
        }

        // Hiển thị thông báo thành công nếu có
        successMessage?.let { message ->
            Text(
                text = message,
                color = Color(0xFF4CAF50),
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Xóa thông báo sau 3 giây
            LaunchedEffect(message) {
                delay(3000)
                onClearSuccessMessage()
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Phần nội dung - mặc định là Làm test
        Box(modifier = Modifier.fillMaxHeight()) {
            when (currentTab) {
                0 -> DoTestContent(
                    currentUser = user,
                    folders = folders,
                    onFolderClick = onFolderClick,
                    onSearchFolders = { query ->
                        onLoadFolders(query, null)
                    },
                    onLoadMoreFolders = onLoadMoreFolders,
                    isLoading = isLoading,
                    isEndReached = isEndReached,
                    onVoteClick = onVoteFolder,
                    onUnVoteClick = onUnvoteFolder,
                    onSettingClick = { folder ->
                        navController.navigate("${Route.TestSettingScreen.route}/${folder.id}")
                    }
                )

                1 -> EnterCodeContent(
                    folders = folders,
                    isSearching = isLoading,
                    onSearchByCode = { code ->
                        onLoadFolders(null, code)
                    },
                    onFolderFound = { folder ->
                        navController.navigate("${Route.TestDetailScreen.route}/${folder.id}")
                    }
                )

                2 -> CreatedTestsContent(
                    folders = folders,
                    isLoading = isLoading,
                    isEndReached = isEndReached,
                    currentUser = user,
                    navController = navController,
                    onFolderClick = { folder ->
                        navController.navigate("${Route.TestDetailScreen.route}/${folder.id}")
                    },
                    onLoadMoreFolders = onLoadMoreFolders,
                    onVoteFolder = onVoteFolder,
                    onUnvoteFolder = onUnvoteFolder
                )

                3 -> CreateTestContent(
                    isCreating = isLoading,
                    onCreateFolder = onCreateFolder,
                    onFolderCreated = { folder ->
                        onNavigateToCreatedTests()
                    }
                )
            }
        }
    }
    // Payment Loading Overlay
    if (paymentState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF42B3FF))
        }
    }

    // Payment Confirmation Dialog
    if (paymentState.showPaymentDialog && paymentState.folder != null) {
        PaymentConfirmationDialog(
            folder = paymentState.folder!!,
            onConfirm = onConfirmPayment,
            onCancel = onCancelPayment
        )
    }

    // Payment Success Dialog
    if (paymentState.showSuccessDialog) {
        AlertDialog(
            onDismissRequest = onDismissSuccess,
            title = { Text("Thanh toán thành công") },
            text = { Text("Bạn có thể bắt đầu làm bài test ngay bây giờ!") },
            confirmButton = {
                TextButton(
                    onClick = onDismissSuccess
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Payment Error Dialog
    paymentState.error?.let { error ->
        AlertDialog(
            onDismissRequest = onDismissPaymentError,
            title = { Text("Lỗi thanh toán") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = onDismissPaymentError) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun PaymentConfirmationDialog(
    folder: Folder,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(
                text = "Xác nhận thanh toán",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text("Bạn cần thanh toán để truy cập vào folder:")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = folder.name,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF42B3FF)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (folder.price > 0) {
                    val formattedPrice = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                        .format(folder.price)
                    Text(
                        text = "Giá: $formattedPrice",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                } else {
                    Text(
                        text = "Miễn phí",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF42B3FF)
                )
            ) {
                Text("Xác nhận", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Hủy")
            }
        }
    )
}

@Composable
fun UserInfoHeader(
    user: User?
) {
    // Thông tin người dùng
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFFB5EEFF)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(5.dp))
        // Ảnh đại diện
        AsyncImage(
            model = user?.avatar,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.default_avt),
            error = painterResource(R.drawable.default_avt),
            fallback = painterResource(R.drawable.default_avt)
        )
        Spacer(modifier = Modifier.width(12.dp))
        // Thông tin ID và mô tả
        InfoRow("Username", user?.username.toString())
    }
}

@Composable
fun InfoRow(label: String, text: String) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            ) {
                append("$label: ")
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            ) {
                append(text)
            }
        }
    )
}

@Preview
@Composable
private fun TestScreenPreview() {
    CapyVocab_FETheme {
        TestScreen(navController = rememberNavController())
    }
}