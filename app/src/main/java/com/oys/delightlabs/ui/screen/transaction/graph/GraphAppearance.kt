package com.oys.delightlabs.ui.screen.transaction.graph

import androidx.compose.ui.graphics.Color

data class GraphAppearance(
    val graphColor: Color,
    val graphAxisColor: Color,
    val graphThickness: Float,
    val iscolorAreaUnderChart: Boolean,
    val colorAreaUnderChart: Color,
    val isCircleVisible: Boolean,
    val circleColor: Color,
)