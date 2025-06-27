package com.example.capyvocab_fe.user.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser

@Composable
fun ConfirmCodeScreen(
    user: ProfileUser,
    onBackClick:() -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController,
) {


    ConfirmCodeScreenContent(
        onBackClick = onBackClick,
        errorMessage = "",
        onReSendCode = {},
        onConfirmCode = { },
    )

}


@Composable
fun ConfirmCodeScreenContent(
    onConfirmCode: (String) -> Unit,
    onReSendCode: () -> Unit,
    onBackClick:() -> Unit,
    errorMessage: String = "",

) {
    var code by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                text = "Xác nhận mã",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Text("Nhập mã xác nhận đã gửi qua email", style = MaterialTheme.typography.titleMedium,)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = code,
            onValueChange = { code = it },
            placeholder = { Text("Mã xác nhận") },
            singleLine = true
        )

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(onClick = { onReSendCode() }) {
            Text("Gửi lại mã")
        }

        Button(onClick = { onConfirmCode(code) }) {
            Text("Xác nhận")
        }

    }
}


