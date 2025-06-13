package com.example.capyvocab_fe.admin.dashboard.presentation.components

import android.graphics.Typeface
import android.view.ViewGroup
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopTopicDetail
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopicProgressStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopicStats
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

@Composable
fun TopicScreen(
    stats: TopicStats,
    popularTopics: List<TopTopicDetail>,
    modifier: Modifier = Modifier
) {
    if (stats.totalTopics == 0){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F9FF))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Chưa có chủ đề nào")
        }
    }
    else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF1F9FF))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card tổng số chủ đề
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Tổng số chủ đề",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${stats.totalTopics}",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                }
            }

            // Card tỷ lệ hoàn thành chủ đề
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(
                        text = "Tỷ lệ hoàn thành chủ đề",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TopicCompletionChart(stats.progressStats)
                }
            }

            // Danh sách chủ đề phổ biến
            Text(
                text = "Chủ đề phổ biến",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                popularTopics.forEachIndexed { index, topic ->
                    PopularTopicCard(
                        index = index,
                        topic = topic,
                        color = topicColors.getOrElse(index) { Color.Cyan }
                    )
                }
            }
        }
    }
}

@Composable
fun TopicCompletionChart(progressStats: TopicProgressStats) {
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
                setEntryLabelColor(android.graphics.Color.BLACK)
                setEntryLabelTextSize(12f)
                description.isEnabled = false
                animateY(1000)

                legend.apply {
                    isEnabled = true
                    typeface = Typeface.DEFAULT_BOLD
                    orientation = Legend.LegendOrientation.VERTICAL
                    verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                    horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    textSize = 14f
                    form = Legend.LegendForm.CIRCLE
                }
            }
        },
        update = { chart ->
            val entries = listOf(
                PieEntry(progressStats.completed.toFloat(), "Đã hoàn thành"),
                PieEntry(progressStats.notCompleted.toFloat(), "Chưa hoàn thành")
            )

            val dataSet = PieDataSet(entries, "").apply {
                colors = topicPieColors.map { it.toArgb() }
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

@Composable
fun PopularTopicCard(
    index: Int,
    topic: TopTopicDetail,
    color: Color,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Số thứ tự: #1, #2, ...
        Text(
            text = "#${index + 1}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = 8.dp)
                .width(32.dp),
            textAlign = TextAlign.Center
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Column {
                // Phần tiêu đề
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = topic.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Phần chi tiết
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFB5EEFF))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Số lượng từ: ${topic.wordCount}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                    Text(
                        text = "Lượt học: ${topic.completeCount}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

val topicPieColors = listOf(
    Color(0xFF70BBF7), // Đã hoàn thành
    Color(0xFF2196F3)  // Chưa hoàn thành
)

val topicColors = listOf(
    Color(0xFF1BCCFD),
    Color(0xFF00B1EB),
    Color(0xFF0099E5),
    Color(0xFF007FB9),
    Color(0xFF00567E),
)