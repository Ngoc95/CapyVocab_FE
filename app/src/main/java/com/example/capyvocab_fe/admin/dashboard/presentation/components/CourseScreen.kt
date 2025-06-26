package com.example.capyvocab_fe.admin.dashboard.presentation.components

import android.graphics.Typeface
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.capyvocab_fe.admin.dashboard.domain.model.CourseProgressStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.CourseStats
import com.example.capyvocab_fe.admin.dashboard.domain.model.TopCourseDetail
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.accompanist.flowlayout.FlowRow
import android.graphics.Color as AndroidColor

// Color constants
val PrimaryBlue = Color(0xFF0066FF)
val MidBlue = Color(0xFF00CFFF)
val TextDark = Color(0xFF212121)
val CompletedColor = AndroidColor.rgb(102, 204, 255)     // Light blue
val InProgressColor = AndroidColor.rgb(0, 102, 255)       // Blue
val NotStartedColor = AndroidColor.rgb(0, 51, 102)        // Dark blue

@Composable
fun CourseScreen(courseStats: CourseStats, topCourses: List<TopCourseDetail>) {
    if(courseStats.totalCourses == 0){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F9FF))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Chưa có khóa học nào")
        }
    }
    else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF1F9FF))
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CourseOverviewCard(courseStats)
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                CourseProgressChart(courseStats.progressStats)
            }
            item {
                Text(
                    "Khóa học phổ biến",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            itemsIndexed(topCourses) { index, course ->
                TopCourseCard(
                    rank = index + 1,
                    title = course.title,
                    level = course.level,
                    learnerCount = course.learnerCount
                )
            }
        }
    }
}

val levelNameMap = mapOf(
    "Beginner" to "Sơ cấp",
    "Intermediate" to "Trung cấp",
    "Advance" to "Nâng cao"
)

@Composable
fun CourseOverviewCard(stats: CourseStats) {
    val levelColorMap = mapOf(
        "Beginner" to PrimaryBlue,
        "Intermediate" to MidBlue,
        "Advance" to Color(0xFF888888)
    )
    val total = stats.courseCountByLevel.sumOf { it.count }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            //Header
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Tổng quan khóa học",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "$total khóa tất cả",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Progress bar chia theo tỉ lệ level
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(50))
            ) {
                stats.courseCountByLevel.forEach { item ->
                    val percent = (item.count.toFloat() / total).takeIf { it > 0f } ?: 0.01f
                    val color = levelColorMap[item.level] ?: Color.LightGray

                    Box(
                        modifier = Modifier
                            .weight(percent)
                            .fillMaxHeight()
                            .background(color)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legend with level + count
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 16.dp,
                crossAxisSpacing = 12.dp
            ) {
                stats.courseCountByLevel.forEach { item ->
                    val color = levelColorMap[item.level] ?: Color.LightGray
                    val displayName = levelNameMap[item.level] ?: item.level

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(end = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(10.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(50))
                                .background(color)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // level and count
                        Column {
                            Text(
                                text = displayName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${item.count} Khóa",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseProgressChart(progress: CourseProgressStats) {
    Column {
        Text("Tỷ lệ hoàn thành khóa học",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp)

        Spacer(modifier = Modifier.height(8.dp))

        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 400)

                    val entries = listOf(
                        PieEntry(progress.completed.toFloat(), "Đã hoàn thành"),
                        PieEntry(progress.inProgress.toFloat(), "Đang học"),
                        PieEntry(progress.notStarted.toFloat(), "Chưa học")
                    )

                    val dataSet = PieDataSet(entries, "").apply {
                        colors = listOf(CompletedColor, InProgressColor, NotStartedColor)
                        valueTextColor = AndroidColor.BLACK
                        valueTextSize = 18f
                        sliceSpace = 3f
                        yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                        valueLinePart1Length = 0.5f
                        valueLinePart2Length = 0.5f
                        valueLineColor = AndroidColor.BLACK
                    }


                    val pieData = PieData(dataSet).apply {
                        setValueFormatter(PercentFormatter())
                    }

                    this.data = pieData

                    setUsePercentValues(true)
                    description.isEnabled = false
                    legend.apply {
                        isEnabled = true
                        textColor = AndroidColor.BLACK
                        textSize = 14f
                        typeface = Typeface.DEFAULT_BOLD
                        form = Legend.LegendForm.CIRCLE
                        horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                        verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                        orientation = Legend.LegendOrientation.HORIZONTAL
                        xEntrySpace = 20f // <- tăng khoảng cách giữa các mục legend
                    }

                    setEntryLabelColor(AndroidColor.BLACK)
                    setDrawEntryLabels(false)
                    isDrawHoleEnabled = true
                    holeRadius = 40f
                    transparentCircleRadius = 45f
                    animateY(1000)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Composable
fun TopCourseCard(rank: Int, title: String, level: String, learnerCount: Int) {

    val displayLevel = levelNameMap[level] ?: level // fallback nếu không map được

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Số thứ tự: #1, #2, ...
        Text(
            text = "#${rank}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = 8.dp)
                .width(32.dp),
            textAlign = TextAlign.Center
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.background(PrimaryBlue.copy(alpha = 0.9f))) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFF00D0FF))
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(title, fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Row(
                    modifier = Modifier
                        .background(Color(0xFFB3ECFF))
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Cấp độ: $displayLevel", style = MaterialTheme.typography.bodySmall, color = TextDark)
                    Text("Lượt học: $learnerCount", style = MaterialTheme.typography.bodySmall, color = TextDark)
                }
            }
        }
    }
}
