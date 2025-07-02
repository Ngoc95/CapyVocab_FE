package com.example.capyvocab_fe.admin.dashboard.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.dashboard.domain.model.RevenueStats
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun RevenueScreen(revenueStats: RevenueStats) {
    var selectedPeriod by remember { mutableStateOf("WEEKLY") }

    val data = when (selectedPeriod) {
        "MONTHLY" -> revenueStats.monthly
        "YEARLY" -> revenueStats.yearly
        else -> revenueStats.weekly
    }

    val totalRevenue = revenueStats.total
    val todayRevenue = revenueStats.weekly.lastOrNull()?.total ?: 0.0
    val yesterdayRevenue = revenueStats.weekly.getOrNull(data.size - 2)?.total ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5FAFF))
            .padding(16.dp)
    ) {
        // Tổng doanh thu
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tổng tiền lời",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${NumberFormat.getNumberInstance(Locale.US).format(totalRevenue)} đ",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hôm nay / Ngày trước
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Hôm nay", color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)
                    Text(
                        "${NumberFormat.getNumberInstance(Locale.US).format(todayRevenue)} đ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Divider(
                    modifier = Modifier
                        .height(50.dp)
                        .width(1.dp)
                        .background(Color.LightGray)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ngày trước", color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)
                    Text(
                        "${NumberFormat.getNumberInstance(Locale.US).format(yesterdayRevenue)} đ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Period Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PeriodButton("WEEKLY", selectedPeriod) { selectedPeriod = "WEEKLY" }
            PeriodButton("MONTHLY", selectedPeriod) { selectedPeriod = "MONTHLY" }
            PeriodButton("YEARLY", selectedPeriod) { selectedPeriod = "YEARLY" }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Biểu đồ
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context ->
                LineChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    description.isEnabled = false
                    legend.isEnabled = false
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    axisRight.isEnabled = false
                    setTouchEnabled(true)
                    setPinchZoom(true)
                }
            },
            update = { chart ->
                val (entries, labels) = when (selectedPeriod) {
                    "MONTHLY" -> {
                        val dataPoints = revenueStats.monthly
                        val entries = dataPoints.mapIndexed { index, point ->
                            Entry(index.toFloat(), point.total.toFloat() )
                        }
                        val labels = dataPoints.map { it.week } // Tuần 1, Tuần 2,...
                        entries to labels
                    }
                    "YEARLY" -> {
                        val dataPoints = revenueStats.yearly
                        val entries = dataPoints.mapIndexed { index, point ->
                            Entry(index.toFloat(), point.total.toFloat())
                        }
                        val labels = dataPoints.map { it.month } // T1, T2,...
                        entries to labels
                    }
                    else -> {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
                        val dataPoints = revenueStats.weekly
                        val entries = dataPoints.mapIndexed { index, point ->
                            Entry(index.toFloat(), point.total.toFloat())
                        }
                        val labels = dataPoints.map {
                            val date = inputFormat.parse(it.date)
                            outputFormat.format(date!!)
                        }
                        entries to labels
                    }
                }

                val dataSet = LineDataSet(entries, "Doanh thu").apply {
                    color = Color(0xFFFF9800).hashCode()
                    valueTextColor = Color.Black.hashCode()
                    lineWidth = 2f
                    setDrawCircles(true)
                    setCircleColor(Color(0xFFFF9800).hashCode())
                    setDrawValues(false)
                }

                chart.data = LineData(dataSet)
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                chart.xAxis.granularity = 1f
                chart.xAxis.labelRotationAngle = 0f

                //Add marker
                val marker = RevenueMarkerView(chart.context, labels)
                marker.chartView = chart
                chart.marker = marker

                chart.invalidate()
            }

        )
    }
}

@Composable
fun PeriodButton(period: String, selected: String, onClick: () -> Unit) {
    val isSelected = period == selected
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .defaultMinSize(minWidth = 80.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF1565C0) else Color.LightGray,
            contentColor = if (isSelected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = when (period) {
                "WEEKLY" -> "Tuần"
                "MONTHLY" -> "Tháng"
                "YEARLY" -> "Năm"
                else -> period
            },
            fontSize = 14.sp
        )
    }
}

class RevenueMarkerView(
    context: Context,
    private val labels: List<String>
) : MarkerView(context, R.layout.marker_view) {

    private val tvContent: TextView = findViewById(R.id.tvContent)

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val label = labels.getOrNull(e.x.toInt()) ?: ""
            val value = e.y.toLong()
            val formatted = NumberFormat.getNumberInstance(Locale.US).format(value)
            tvContent.text = "$label: $formatted đ"
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}



