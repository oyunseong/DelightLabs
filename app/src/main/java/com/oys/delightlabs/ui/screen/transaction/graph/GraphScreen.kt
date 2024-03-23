package com.oys.delightlabs.ui.screen.transaction.graph

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.oys.delightlabs.ui.extension.pxToDp
import com.oys.delightlabs.ui.extension.toPx
import com.oys.delightlabs.ui.theme.mainColor
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
    var offset by remember { mutableStateOf<Offset?>(null) }
    Box {
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
                    onTouch = {
                        offset = it
                    }
                )
            }
        )

        offset?.let {
            InfoBox(
                modifier = Modifier.offset(
                    x = pxToDp(pixels = it.x),
                    y = pxToDp(pixels = it.y)
                )
            )
        }

    }


}
