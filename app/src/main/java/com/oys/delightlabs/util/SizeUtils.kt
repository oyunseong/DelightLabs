package com.oys.delightlabs.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }

@Composable
fun pxToDp(pixels: Float) = with(LocalDensity.current) { pixels.toDp() }


@Composable
fun Dp.toPx(): Float {
    val dp = this
    val density = LocalDensity.current
    return with(density) { dp.toPx() }
}