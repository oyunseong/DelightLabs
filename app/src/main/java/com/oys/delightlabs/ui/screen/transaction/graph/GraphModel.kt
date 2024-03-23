package com.oys.delightlabs.ui.screen.transaction.graph

data class GraphModel(
    val xValues: List<Int> = emptyList(),
    val yValues: List<Float> = emptyList(),
    val points: List<Float> = emptyList(),
    val verticalStep: Float = 0f,
) {
    companion object {
        val empty get() = GraphModel()
    }
}