package com.example.capyvocab_fe.payout.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capyvocab_fe.core.ui.components.OverlaySnackbar

@Composable
fun PayoutScreen(
    viewModel: PayoutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    state.errorMessage?.let {
        OverlaySnackbar(message = it)
    }

    PayoutScreenContent(
        amount = state.amount.toString(),
        numberAccount = state.numberAccount,
        nameBank = state.nameBank,
        isLoading = state.isLoading,
        onAmountChange = {
            viewModel.onEvent(PayoutEvent.AmountChanged(it.toString()))
        },
        onNumberAccountChange = {
            viewModel.onEvent(PayoutEvent.NumberAccountChanged(it))
        },
        onNameBankChange = {
            viewModel.onEvent(PayoutEvent.NameBankChanged(it))
        },
        onSubmit = {
            viewModel.onEvent(PayoutEvent.CreatePayout)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayoutScreenContent(
    amount: String,
    numberAccount: String,
    nameBank: String,
    isLoading: Boolean,
    onAmountChange: (Double) -> Unit,
    onNumberAccountChange: (String) -> Unit,
    onNameBankChange: (String) -> Unit,
    onSubmit: () -> Unit
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
            .padding(16.dp)
    ) {
        Text("Rút tiền", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { onAmountChange(it.toDoubleOrNull() ?: 0.0) },
            label = { Text("Số tiền") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = numberAccount,
            onValueChange = { onNumberAccountChange(it) },
            label = { Text("Số tài khoản") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // Bank dropdown selector
        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value }
        ) {
            OutlinedTextField(
                value = selectedBank.value,
                onValueChange = {}, // Read-only
                readOnly = true,
                label = { Text("Ngân hàng") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors()
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                banks.forEach { bank ->
                    DropdownMenuItem(
                        text = { Text(bank) },
                        onClick = {
                            selectedBank.value = bank
                            onNameBankChange(bank)
                            expanded.value = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Xác nhận")
            }
        }
    }
}