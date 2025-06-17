package com.example.capyvocab_fe.payout.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.payout.domain.model.Payout
import com.example.capyvocab_fe.ui.theme.dimens
import com.example.capyvocab_fe.util.formatCurrency
import com.example.capyvocab_fe.util.formatDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminPayoutCard(
    payout: Payout,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
    ) {
        Text("Người dùng: ${payout.createdBy.username} (${payout.createdBy.email})", style = MaterialTheme.typography.titleMedium)
        Text("Số tiền: ${formatCurrency(payout.amount)}")
        Text("Ngân hàng: ${payout.nameBank}")
        Text("Số TK: ${payout.numberAccount}")
        Text("Ngày yêu cầu: ${formatDate(payout.createdAt)}")
        Text("Trạng thái: ${payout.status}", color = when (payout.status) {
            "PENDING" -> Color.Gray
            "SUCCESS" -> Color.Green
            "FAILED" -> Color.Red
            else -> Color.Black
        })

        if (payout.status == "PENDING") {
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Button(onClick = onApprove, modifier = Modifier.weight(1f)) {
                    Text("Duyệt")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onReject,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Từ chối")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AdminPayoutCardPreview() {
    val samplePayout = Payout(
        id = 1,
        createdBy = User(
            username = "user123", email = "xxx",
            id = 1,
            avatar = "N/A",
            roleId = 1
        ),
        amount = 1000000.0,
        nameBank = "Ngân hàng ABC",
        numberAccount = "123456789",
        createdAt = "2023-10-01",
        status = "2"
    )
    AdminPayoutCard(
        payout = samplePayout,
        onApprove = { /* Handle approve action */ },
        onReject = { /* Handle reject action */ }
    )
}
