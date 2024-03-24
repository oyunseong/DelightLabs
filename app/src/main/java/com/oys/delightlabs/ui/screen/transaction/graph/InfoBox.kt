package com.oys.delightlabs.ui.screen.transaction.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.oys.delightlabs.R
import com.oys.delightlabs.ui.component.VerticalSpacer
import com.oys.delightlabs.ui.theme.body1
import com.oys.delightlabs.ui.theme.body2
import com.oys.delightlabs.ui.theme.mainColor

@Composable
fun InfoBox(
    modifier: Modifier = Modifier,
    amount: Float,
    background: Color,
    boxWidth: (Float) -> Unit = {},
) {
    Column(
        modifier = modifier
            .onSizeChanged { boxWidth(it.width.toFloat()) }

    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = background)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 7.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = if (amount < 0) {
                        "-$${-amount}"
                    } else {
                        "+$${amount}"
                    },
                    style = body2,
                    color = Color.White
                )
                VerticalSpacer(dp = 1.dp)
                Text(
                    text = "Nov 23, 00:00",
                    style = body1,
                    color = Color.White
                )
            }
        }
        Icon(
            modifier = Modifier
                .size(7.dp)
                .offset(y = (-3.5).dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.ic_rhombus),
            contentDescription = "",
            tint = background
        )
    }

}