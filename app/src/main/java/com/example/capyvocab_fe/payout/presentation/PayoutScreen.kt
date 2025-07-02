package com.example.capyvocab_fe.payout.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.core.ui.components.OverlaySnackbar
import com.example.capyvocab_fe.core.ui.components.SnackbarType
import com.example.capyvocab_fe.ui.theme.dimens
import com.example.capyvocab_fe.util.PriceUtils.formatPrice
import com.example.capyvocab_fe.util.PriceUtils.unformatPrice
import kotlinx.coroutines.delay

@Composable
fun PayoutScreen(
    viewModel: PayoutViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    var visibleSuccess by remember { mutableStateOf("") }
    var visibleError by remember { mutableStateOf("") }

    // Show success message for 3 seconds
    LaunchedEffect(state.successMessage) {
        if (state.successMessage.isNotEmpty()) {
            visibleSuccess = state.successMessage
            delay(2000)
            visibleSuccess = ""
        }
    }
    // Show error message for 3 seconds
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(2000)
            visibleError = ""
        }
    }

    FocusComponent {
        PayoutScreenContent(
            amountText = state.amount,
            userBalance = state.currentUser?.balance.toString(),
            numberAccount = state.numberAccount,
            nameBank = state.nameBank,
            isLoading = state.isLoading,
            successMessage = visibleSuccess,
            errorMessage = visibleError,
            onAmountChange = {
                viewModel.onEvent(PayoutEvent.AmountChanged(it))
            },
            onNumberAccountChange = {
                viewModel.onEvent(PayoutEvent.NumberAccountChanged(it))
            },
            onNameBankChange = {
                viewModel.onEvent(PayoutEvent.NameBankChanged(it))
            },
            onSubmit = {
                viewModel.onEvent(PayoutEvent.CreatePayout)
            },
            onBack = {
                navController.popBackStack()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayoutScreenContent(
    amountText: TextFieldValue,
    userBalance: String,
    numberAccount: String,
    nameBank: String,
    isLoading: Boolean,
    successMessage: String,
    errorMessage: String,
    onAmountChange: (TextFieldValue) -> Unit,
    onNumberAccountChange: (String) -> Unit,
    onNameBankChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    // Static list of banks
    val banks = listOf(
        "Vietcombank",
        "Techcombank",
        "BIDV",
        "VietinBank",
        "ACB",
        "Sacombank",
        "VPBank",
        "MB Bank",
        "TPBank",
        "SHB"
    )
    val expanded = remember { mutableStateOf(false) }
    val selectedBank = remember(nameBank) { mutableStateOf(nameBank) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.dimens.small2)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Rút tiền",
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(MaterialTheme.dimens.medium1)
                        .clickable(onClick = onBack)
                )
            },
        )
        Spacer(Modifier.height(MaterialTheme.dimens.small2))

        Text(text = "Số dư: ${formatPrice(userBalance.toDoubleOrNull() ?: 0.0)}")

        Spacer(Modifier.height(MaterialTheme.dimens.small2))
        OutlinedTextField(
            value = amountText,
            onValueChange = { newValue ->
                val oldUnformatted = amountText.text.replace(",", "")
                val newUnformatted = newValue.text.replace(",", "")
                val formatted = if (newUnformatted.isNotEmpty()) formatPrice(newUnformatted.toDouble()) else ""
                // Calculate new cursor position
                val diff = formatted.length - newValue.text.length
                val newCursor = (newValue.selection.end + diff).coerceIn(0, formatted.length)
                onAmountChange(
                    TextFieldValue(
                        text = formatted,
                        selection = androidx.compose.ui.text.TextRange(newCursor)
                    )
                )
            },
            label = { Text("Số tiền", style = MaterialTheme.typography.bodyMedium) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(MaterialTheme.dimens.small1))

        OutlinedTextField(
            value = numberAccount,
            onValueChange = { onNumberAccountChange(it) },
            label = { Text("Số tài khoản", style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(Modifier.height(MaterialTheme.dimens.small1))

        // Bank dropdown selector
        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value }
        ) {
            OutlinedTextField(
                value = selectedBank.value,
                onValueChange = {}, // Read-only
                readOnly = true,
                label = { Text("Ngân hàng", style = MaterialTheme.typography.bodyMedium) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(horizontal = MaterialTheme.dimens.small2)
                    .heightIn(max = 320.dp)
            ) {
                banks.forEach { bank ->
                    DropdownMenuItem(
                        text = { Text(bank, style = MaterialTheme.typography.bodyMedium) },
                        onClick = {
                            selectedBank.value = bank
                            onNameBankChange(bank)
                            expanded.value = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(MaterialTheme.dimens.small2))

        Button(
            onClick = onSubmit,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Xác nhận", style = MaterialTheme.typography.titleMedium)
            }
        }
        OverlaySnackbar(message = successMessage, type = SnackbarType.Success)
        OverlaySnackbar(message = errorMessage, type = SnackbarType.Error)
    }
}

@Preview(showBackground = true)
@Composable
private fun PayoutScreenPreview() {
    PayoutScreenContent(
        amountText = TextFieldValue("1000000"),
        userBalance = "5000000",
        numberAccount = "123456789",
        nameBank = "Vietcombank",
        isLoading = false,
        successMessage = "",
        errorMessage = "",
        onAmountChange = {},
        onNumberAccountChange = {},
        onNameBankChange = {},
        onSubmit = {},
        onBack = {}
    )
}