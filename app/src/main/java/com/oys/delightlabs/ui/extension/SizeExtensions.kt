package com.oys.delightlabs.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


//TODO 유틸 ?
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