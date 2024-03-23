package com.oys.delightlabs.ui.screen.transaction.graph

data class GraphModel(
    val xValues: List<Int>,
    val yValues: List<Float>,
    val points: List<Float>,
    val verticalStep: Float,
)