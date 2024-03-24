package com.oys.delightlabs.ui.screen.transaction.graph

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oys.delightlabs.ui.component.HorizontalSpacer
import com.oys.delightlabs.ui.component.VerticalSpacer
import com.oys.delightlabs.ui.extension.pxToDp
import com.oys.delightlabs.ui.extension.toPx
import com.oys.delightlabs.ui.theme.Gray400
import com.oys.delightlabs.ui.theme.body3
import com.oys.delightlabs.ui.theme.caption1
import com.oys.delightlabs.ui.theme.mainColor
import com.oys.delightlabs.ui.theme.subColor
import kotlinx.coroutines.delay


@Composable
fun GraphScreen(
    incomeGraphModel: GraphModel,
    expenseGraphModel: GraphModel,
) {
    var flag by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = {
        delay(1)
        flag = true
    })
    var incomeBoxOffset by remember { mutableStateOf<Offset?>(null) }
    var expenseBoxOffset by remember { mutableStateOf<Offset?>(null) }
    Box {
        Column {
            Row {
                GraphIndicator(
                    color = mainColor,
                    title = "Income"
                )
                HorizontalSpacer(dp = 20.dp)
                GraphIndicator(
                    color = subColor,
                    title = "Expense"
                )
            }
            VerticalSpacer(dp = 47.dp)
            AnimatedVisibility(
                visible = flag,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 2000, easing = FastOutLinearInEasing)
                ),
                content = {
                    Graph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(161.dp),
                        incomeGraphModel = incomeGraphModel,
                        expenseGraphModel = expenseGraphModel,
                        graphAppearance = GraphAppearance(
                            mainColor,
                            MaterialTheme.colorScheme.primary,
                            2.dp.toPx(),
                            true,
                            mainColor.copy(alpha = 0.13f),
                            true,
                            MaterialTheme.colorScheme.secondary,
                        ),
                        onTouchIncomeChart = {
                            incomeBoxOffset = it
                        },
                        onTouchExpenseChart = {
                            expenseBoxOffset = it
                        }
                    )
                }
            )
            Row {
                Text(
                    text = "DD1",
                    style = body3.copy(fontWeight = FontWeight.Medium),
                    color = Gray400
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "DD2",
                    style = body3.copy(fontWeight = FontWeight.Medium),
                    color = Gray400
                )
            }
        }


        incomeBoxOffset?.let {
            var size by remember { mutableFloatStateOf(0f) }
            InfoBox(
                modifier = Modifier.offset(
                    x = pxToDp(pixels = it.x - size),
                    y = pxToDp(pixels = it.y - 3f)
                ),
                amount = 1.1f,
                background = mainColor,
                boxWidth = { width ->
                    size = width / 2f
                }
            )
        }

        expenseBoxOffset?.let {
            var size by remember { mutableFloatStateOf(0f) }
            InfoBox(
                modifier = Modifier.offset(
                    x = pxToDp(pixels = it.x - size),
                    y = pxToDp(pixels = it.y - 3f)
                ),
                amount = 1.1f,
                background = subColor,
                boxWidth = { width ->
                    size = width / 2f
                }
            )
        }

    }

}

@Composable
fun GraphIndicator(
    color: Color,
    title: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(5.dp)
                .background(color = color)
        )
        HorizontalSpacer(dp = 9.dp)
        Text(
            text = title,
            style = caption1,
            color = mainColor
        )
    }
}
