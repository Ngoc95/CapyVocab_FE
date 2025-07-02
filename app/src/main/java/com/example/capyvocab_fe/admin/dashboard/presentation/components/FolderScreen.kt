package com.example.capyvocab_fe.admin.dashboard.presentation.components

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.dashboard.domain.model.FolderStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopFolderDetail
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

@Composable
fun FolderScreen(
    stats: FolderStats,
    topFolders: List<TopFolderDetail>
) {
    if (stats.totalFolders == 0){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F9FF))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Chưa có folder nào")
        }
    }
    else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F8FF))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Tổng quan folder
            FolderOverviewCard(stats)

            Spacer(modifier = Modifier.height(16.dp))

            // Giá trung bình
            AvgPriceCard(stats.avgPaidFolderPrice)

            Spacer(modifier = Modifier.height(16.dp))

            // Top folder
            if(topFolders.isNotEmpty()){
                Text(
                    text = "Top folder phổ biến",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                topFolders.forEach { folder ->
                    FolderCard(folder)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun FolderOverviewCard(stats: FolderStats) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Miễn phí", color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Text("${stats.freeFolders}", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                }

                DividerLine()

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Có phí", color = Color.Black,fontWeight = FontWeight.SemiBold, fontSize = 18.sp,)
                    Text("${stats.paidFolders}", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                }

                DividerLine()

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tổng", color = Color.Black, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Text("${stats.totalFolders}", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            FolderPieChart(stats)
        }
    }
}

@Composable
fun FolderPieChart(stats: FolderStats) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        factory = { context ->
            PieChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400)
                isDrawHoleEnabled = true
                holeRadius = 40f
                transparentCircleRadius = 45f
                setUsePercentValues(true)
                setDrawEntryLabels(false)
                description.isEnabled = false
                animateY(1000)

                legend.apply {
                    isEnabled = true
                    typeface = Typeface.DEFAULT_BOLD
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    textSize = 14f
                    form = Legend.LegendForm.CIRCLE
                    xEntrySpace = 20f // <- tăng khoảng cách giữa các mục legend
                }
            }
        },
        update = { chart ->

            //ko có folder nào
            if (stats.totalFolders == 0) {
                chart.clear()
                chart.setCenterTextSize(16f)
                return@AndroidView
            }

            //có ít nhất 1 folder
            val entries = listOf(
                PieEntry(stats.freeFolders.toFloat(), "Miễn phí"),
                PieEntry(stats.paidFolders.toFloat(), "Có phí")
            )

            val dataSet = PieDataSet(entries, "").apply {
                colors = listOf(
                    Color(0xFF42A5F5).toArgb(),
                    Color(0xFF0866FF).toArgb()
                )
                valueTextColor = android.graphics.Color.BLACK
                valueTextSize = 14f
                sliceSpace = 3f
                yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                valueLinePart1Length = 0.5f
                valueLinePart2Length = 0.5f
                valueLineColor = android.graphics.Color.BLACK
            }

            chart.data = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter(chart))
            }

            chart.invalidate()
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun AvgPriceCard(price: Double) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Giá trung bình folder có phí",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = String.format("%,.0f đ", price),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun FolderCard(
    folder: TopFolderDetail
) {
    val creator = folder.createdBy

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Tên Folder
            Text(
                folder.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Số lượng từ vựng
            Box(
                modifier = Modifier
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("${folder.questionCount} từ vựng", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Avatar + Email người tạo
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (creator?.avatar != "N/A" && creator?.avatar.isNullOrEmpty()) {
                    AsyncImage(
                        model = creator?.avatar,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.default_avt),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(creator?.email ?: "Ẩn danh", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price + Lượt tham gia
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val formattedPrice = String.format("%,.0f đ", folder.price)
                Text(
                    if (folder.price > 0) formattedPrice else "Miễn phí",
                    fontWeight = FontWeight.SemiBold
                )

                Text("Lượt tham gia: ${folder.attemptCount}", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lượt thích + Nhận xét
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Lượt thích: ${folder.voteCount}", fontSize = 14.sp)

                Text("Nhận xét: ${folder.commentCount}", fontSize = 14.sp)
            }
        }
    }

}

@Composable
fun DividerLine() {
    Box(
        modifier = Modifier
            .width(3.dp)
            .height(50.dp)
            .background(Color.Gray)
            .clip(RoundedCornerShape(50.dp))
    )
}