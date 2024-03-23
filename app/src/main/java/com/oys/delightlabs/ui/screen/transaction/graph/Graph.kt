package com.oys.delightlabs.ui.screen.transaction.graph

import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oys.delightlabs.ui.theme.subColor

@Composable
fun Graph(
    modifier: Modifier,
    incomeGraphModel: GraphModel,
    expenseGraphModel: GraphModel,
    paddingSpace: Dp = 0.dp,
    graphAppearance: GraphAppearance,
    onTouch: (Offset) -> Unit,
) {
    var incomeCoordinates = remember { mutableListOf<PointF>() }
    var expenseCoordinates = remember { mutableListOf<PointF>() }

    val incomeIndicator = remember { mutableStateOf<Offset?>(null) }
    val expenseIndicator = remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = modifier,
        contentAlignment = Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    val touchScope = 52  // 터치 영역
                    detectTapGestures { offset ->
                        incomeCoordinates.forEach {
                            val isX = offset.x < it.x + touchScope && offset.x > it.x - touchScope
                            val isY = offset.y < it.y + touchScope && offset.y > it.y - touchScope
                            if (isX && isY) {
                                Log.d("++@@", "incomeCoordinates x,y : $it")
                                incomeIndicator.value = Offset(it.x, it.y)
                                onTouch.invoke(Offset(it.x, it.y))
                            }
                        }
                        expenseCoordinates.forEach {
                            val isX = offset.x < it.x + touchScope && offset.x > it.x - touchScope
                            val isY = offset.y < it.y + touchScope && offset.y > it.y - touchScope
                            if (isX && isY) {
                                Log.d("++@@", "expenseCoordinates x,y : $it")
                                expenseIndicator.value = Offset(it.x, it.y)
                                onTouch.invoke(Offset(it.x, it.y))
                            }
                        }
                    }
                }
        ) {
            if(incomeIndicator.value != null){
                drawCircle(
                    color = Color.Red,
                    radius = 10f,
                    center = incomeIndicator.value!!
                )
            }

            if(expenseIndicator.value != null){
                drawCircle(
                    color = Color.Red,
                    radius = 10f,
                    center = expenseIndicator.value!!
                )
            }
            val incomeControlPoints1 = mutableListOf<PointF>()
            val incomeControlPoints2 = mutableListOf<PointF>()
            incomeCoordinates = mutableListOf()

            val expenseControlPoints1 = mutableListOf<PointF>()
            val expenseControlPoints2 = mutableListOf<PointF>()
            expenseCoordinates = mutableListOf()

            val expenseYAxisSpace = size.height / expenseGraphModel.yValues.size
            val incomeYAxisSpace = size.height / incomeGraphModel.yValues.size
            val yAxisSpace = maxOf(expenseYAxisSpace, incomeYAxisSpace) // y축 간격
            val xAxisSpace = (size.width - paddingSpace.toPx()) /
                    maxOf(
                        expenseGraphModel.xValues.size - 1,
                        incomeGraphModel.xValues.size - 1
                    )    // x축 간격
            val expenseMinValue = expenseGraphModel.points.minOrNull() ?: 0f
            val incomeMinValue = incomeGraphModel.points.minOrNull() ?: 0f
            val verticalStep = maxOf(expenseGraphModel.verticalStep, incomeGraphModel.verticalStep)

            val min = minOf(expenseMinValue, incomeMinValue)
            incomeGraphModel.points.forEachIndexed { i, it ->
                val x1 = (xAxisSpace * i)
                val y1 = size.height - (yAxisSpace * ((it - min) / verticalStep))
                incomeCoordinates.add(PointF(x1, y1))
//                if (graphAppearance.isCircleVisible) {  // 그래프 점 표시
//                    drawCircle(
//                        color = mainColor,
//                        radius = 10f,
//                        center = Offset(x1, y1)
//                    )
//                }
            }

            expenseGraphModel.points.forEachIndexed { i, it ->
                val x1 = xAxisSpace * i
                val y1 = size.height - (yAxisSpace * ((it - min) / verticalStep))
                expenseCoordinates.add(PointF(x1, y1))
//                if (graphAppearance.isCircleVisible) {
//                    drawCircle(
//                        color = subColor,
//                        radius = 10f,
//                        center = Offset(x1, y1)
//                    )
//                }
            }

            // 3차 베지에 곡선을 그리기 위한 좌표 저장
            for (i in 1 until incomeCoordinates.size) {
                incomeControlPoints1.add(
                    PointF(
                        (incomeCoordinates[i].x + incomeCoordinates[i - 1].x) / 2,
                        incomeCoordinates[i - 1].y
                    )
                )
                incomeControlPoints2.add(
                    PointF(
                        (incomeCoordinates[i].x + incomeCoordinates[i - 1].x) / 2,
                        incomeCoordinates[i].y
                    )
                )
            }

            for (i in 1 until expenseCoordinates.size) {
                expenseControlPoints1.add(
                    PointF(
                        (expenseCoordinates[i].x + expenseCoordinates[i - 1].x) / 2,
                        expenseCoordinates[i - 1].y
                    )
                )
                expenseControlPoints2.add(
                    PointF(
                        (expenseCoordinates[i].x + expenseCoordinates[i - 1].x) / 2,
                        expenseCoordinates[i].y
                    )
                )
            }


            // 그래프 그리는 코드
            if (incomeCoordinates.isNotEmpty()) {
                val stroke = Path().apply {
                    reset()
                    moveTo(incomeCoordinates.first().x, incomeCoordinates.first().y)
                    for (i in 0 until incomeCoordinates.size - 1) {
                        cubicTo(
                            incomeControlPoints1[i].x, incomeControlPoints1[i].y,
                            incomeControlPoints2[i].x, incomeControlPoints2[i].y,
                            incomeCoordinates[i + 1].x, incomeCoordinates[i + 1].y
                        )
                    }
                }

                val fillPath = android.graphics.Path(stroke.asAndroidPath())
                    .asComposePath()
                    .apply {
                        lineTo(incomeCoordinates.last().x, size.height - paddingSpace.toPx())
                        lineTo(incomeCoordinates.first().x, size.height - paddingSpace.toPx())
                        close()
                    }

//             그라데이션 색상 추가
                if (graphAppearance.iscolorAreaUnderChart) {
                    drawPath(
                        fillPath,
                        brush = Brush.verticalGradient(
                            listOf(
                                graphAppearance.colorAreaUnderChart,
                                Color.Transparent,
                            ),
                            endY = size.height
                        ),
                    )
                }
                drawPath(
                    stroke,
                    color = graphAppearance.graphColor,
                    style = Stroke(
                        width = graphAppearance.graphThickness,
                        cap = StrokeCap.Round
                    )
                )


            }



            if (expenseCoordinates.isNotEmpty()) {
                // 그래프 그리는 코드
                val expenseStroke = Path().apply {
                    reset()
                    moveTo(expenseCoordinates.first().x, expenseCoordinates.first().y)
                    for (i in 0 until expenseCoordinates.size - 1) {
                        cubicTo(
                            expenseControlPoints1[i].x, expenseControlPoints1[i].y,
                            expenseControlPoints2[i].x, expenseControlPoints2[i].y,
                            expenseCoordinates[i + 1].x, expenseCoordinates[i + 1].y
                        )
                    }
                }

                val expenseFillPath = android.graphics.Path(expenseStroke.asAndroidPath())
                    .asComposePath()
                    .apply {
                        lineTo(expenseCoordinates.last().x, size.height - paddingSpace.toPx())
                        lineTo(expenseCoordinates.first().x, size.height - paddingSpace.toPx())
                        close()
                    }

//             그라데이션 색상 추가
                if (graphAppearance.iscolorAreaUnderChart) {
                    drawPath(
                        expenseFillPath,
                        brush = Brush.verticalGradient(
                            listOf(
                                subColor,
                                Color.Transparent,
                            ),
                            endY = size.height
                        ),
                    )
                }
                drawPath(
                    expenseStroke,
                    color = subColor,
                    style = Stroke(
                        width = graphAppearance.graphThickness,
                        cap = StrokeCap.Round
                    ),
                )
            }
        }
    }
}