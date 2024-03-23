package com.oys.delightlabs.ui.screen.transaction.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.oys.delightlabs.ui.theme.mainColor

// x 1..7 or 1..30
// y amount
//https://www.droidcon.com/2022/06/22/creating-a-graph-in-jetpack-compose/
@Composable
fun GraphScreen(
    graphModel: GraphModel,
) {
    Graph(
        modifier = Modifier
            .fillMaxWidth()
            .height(161.dp),
        xValues = graphModel.xValues,
        yValues = graphModel.yValues,
        points = graphModel.points,
        paddingSpace = 0.dp,
        verticalStep = graphModel.verticalStep,
        graphAppearance = GraphAppearance(
            Color.White,
            MaterialTheme.colorScheme.primary,
            2f,
            true,
            mainColor,
            false,
            MaterialTheme.colorScheme.secondary,
            Color.Yellow
//                MaterialTheme.colorScheme.background
        )
    )
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.DarkGray)
//    ) {
//
//    }
}
