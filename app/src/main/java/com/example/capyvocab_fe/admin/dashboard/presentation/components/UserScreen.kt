package com.example.capyvocab_fe.admin.dashboard.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.dashboard.domain.model.UserStats
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

@SuppressLint("ClickableViewAccessibility")
@Composable
fun UserScreen(userStats: UserStats) {
    var selectedRange by remember { mutableStateOf("7 ngày qua") }

    val (activeData, newData) = when (selectedRange) {
        "30 ngày qua" -> userStats.activeUsers30Days to userStats.newUsers30Days
        else -> userStats.activeUsers7Days to userStats.newUsers7Days
    }

    val activeEntries = activeData.mapIndexed { index, point ->
        Entry(index.toFloat(), point.count.toFloat())
    }
    val newEntries = newData.mapIndexed { index, point ->
        Entry(index.toFloat(), point.count.toFloat())
    }

    val dateFormatter = SimpleDateFormat("dd/MM", Locale.getDefault())
    val labels = activeData.map { point ->
        try {
            val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(point.date)
            dateFormatter.format(parsedDate!!)
        } catch (e: Exception) {
            point.date // fallback
        }
    }

    val context = LocalContext.current
    val markerView = remember(labels) {
        TooltipMarkerView(context, labels)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5FAFF))
            .padding(16.dp)
    ) {
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
                    text = "Tổng người dùng",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    userStats.total.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1565C0)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PeriodButton("7 ngày qua", selectedRange) { selectedRange = "7 ngày qua" }
            PeriodButton("30 ngày qua", selectedRange) { selectedRange = "30 ngày qua" }
        }

        Spacer(modifier = Modifier.height(16.dp))

        var chartRef by remember { mutableStateOf<LineChart?>(null) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp) // tăng thêm 20dp cho padding bên dưới
                .pointerInput(Unit) {
                    detectTapGestures {
                        // Mỗi khi chạm ngoài chart, xoá marker
                        chartRef?.highlightValues(null)
                    }
                }
                .padding(bottom = 16.dp) // tạo khoảng cách giữa chart và legend
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                factory = { context ->
                    LineChart(context).apply {
                        chartRef = this
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        description.isEnabled = false
                        setTouchEnabled(true)
                        setPinchZoom(true)
                        axisRight.isEnabled = false
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        xAxis.setDrawGridLines(true)
                        axisLeft.setDrawGridLines(true)
                        legend.isEnabled = true
                    }
                },
                update = { chart ->
                    // Cập nhật marker mỗi lần update
                    chartRef = chart
                    chart.marker = markerView

                    val activeDataSet = LineDataSet(activeEntries, "Người dùng hoạt động").apply {
                        color = Color(0xFFFF9800).hashCode()
                        setCircleColor(Color(0xFFFF9800).hashCode())
                        lineWidth = 2f
                        setDrawCircles(true)
                        setDrawValues(false)
                    }

                    val newDataSet = LineDataSet(newEntries, "Người dùng mới").apply {
                        color = Color(0xFF1565C0).hashCode()
                        setCircleColor(Color(0xFF1565C0).hashCode())
                        lineWidth = 2f
                        setDrawCircles(true)
                        setDrawValues(false)
                    }

                    chart.data = LineData(listOf(newDataSet, activeDataSet))

                    chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                    chart.xAxis.granularity = 1f
                    chart.xAxis.labelRotationAngle = 0f

                    // Tuỳ chỉnh Legend
                    val legend = chart.legend
                    legend.textSize = 16f // tăng kích thước chữ
                    legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    legend.orientation = Legend.LegendOrientation.HORIZONTAL
                    legend.setDrawInside(false)

                    legend.formToTextSpace = 20f // khoảng cách giữa chart và text
                    legend.xEntrySpace = 20f    // khoảng cách giữa các mục legend

                    chart.invalidate()

                    val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                        override fun onSingleTapUp(e: MotionEvent): Boolean {
                            val highlight = chart.getHighlightByTouchPoint(e.x, e.y)
                            if (highlight == null) {
                                chart.highlightValues(null)
                            }
                            return true
                        }
                    })

                    chart.setOnTouchListener { _, event ->
                        gestureDetector.onTouchEvent(event)
                        false
                    }

                    chart.setExtraOffsets(10f, 10f, 10f, 30f)

                }

            )
        }
    }
}
class TooltipMarkerView(
    context: Context,
    private val labels: List<String>
) : MarkerView(context, R.layout.tooltip_marker) {

    private val bgColor = 0xCC000000.toInt()
    private val textColor = 0xFFFFFFFF.toInt()

    private val paint = Paint().apply {
        color = bgColor
        textSize = 40f
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
    }

    private val textPadding = 16f
    private val radius = 16f
    private var valueText = ""

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val rawIndex = e?.x ?: 0f
        val index = rawIndex.roundToInt().coerceIn(0, labels.lastIndex)
        val label = labels.getOrNull(index) ?: ""
        val value = e?.y?.toInt() ?: 0
        valueText = "$label: $value"
        super.refreshContent(e, highlight)
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        val textWidth = paint.measureText(valueText)
        val boxWidth = textWidth + textPadding * 2
        val boxHeight = 60f

        var x = posX
        var y = posY - boxHeight

        if (x + boxWidth > canvas.width) x -= boxWidth
        if (y < 0) y = 0f

        // Draw background
        val rect = RectF(x, y, x + boxWidth, y + boxHeight)
        paint.color = bgColor
        canvas.drawRoundRect(rect, radius, radius, paint)

        // Draw text
        paint.color = textColor
        canvas.drawText(valueText, x + textPadding, y + 42f, paint)
    }

    override fun getOffset(): MPPointF = MPPointF(0f, 0f)
}
