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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oys.delightlabs.data.repository.fakeToday
import com.oys.delightlabs.ui.component.HorizontalSpacer
import com.oys.delightlabs.ui.component.VerticalSpacer

import com.oys.delightlabs.ui.screen.transaction.TransactionTab
import com.oys.delightlabs.ui.theme.Gray400
import com.oys.delightlabs.ui.theme.body3
import com.oys.delightlabs.ui.theme.caption1
import com.oys.delightlabs.ui.theme.mainColor
import com.oys.delightlabs.ui.theme.subColor
import com.oys.delightlabs.util.pxToDp
import com.oys.delightlabs.util.toDate
import com.oys.delightlabs.util.toDay
import com.oys.delightlabs.util.toDayAfterDays
import com.oys.delightlabs.util.toFormattedDateAfter30Days
import com.oys.delightlabs.util.toFormattedDateTime
import com.oys.delightlabs.util.toFormattedDateType2
import com.oys.delightlabs.util.toPx
import kotlinx.coroutines.delay


@Composable
fun GraphScreen(
    type: TransactionTab,
    incomeGraphModel: GraphModel,
    expenseGraphModel: GraphModel,
) {
    var flag by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = {
        delay(1)
        flag = true
    })
    var incomeBoxOffset by remember { mutableStateOf<TouchedValue?>(null) }
    var expenseBoxOffset by remember { mutableStateOf<TouchedValue?>(null) }
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
                        graphThickness = 2.dp.toPx(),
                        iscolorAreaUnderChart = true,
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
                    text = if (type == TransactionTab.WEEK) "${fakeToday.toDay()}" else fakeToday.toFormattedDateType2(),
                    style = body3.copy(fontWeight = FontWeight.Medium),
                    color = Gray400
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (type == TransactionTab.WEEK) "${fakeToday.toDayAfterDays(7)}" else fakeToday.toFormattedDateAfter30Days(),
                    style = body3.copy(fontWeight = FontWeight.Medium),
                    color = Gray400
                )
            }
        }


        incomeBoxOffset?.let {
            var size by remember { mutableFloatStateOf(0f) }
            InfoBox(
                modifier = Modifier.offset(
                    x = pxToDp(pixels = it.offset.x - size),
                    y = pxToDp(pixels = it.offset.y - 3f)
                ),
                amount = it.value,
                time = it.timeStamp.toDate()!!.time.toFormattedDateTime(),
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
                    x = pxToDp(pixels = it.offset.x - size),
                    y = pxToDp(pixels = it.offset.y - 3f)
                ),
                amount = it.value,
                time = it.timeStamp.toDate()!!.time.toFormattedDateTime(),
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
