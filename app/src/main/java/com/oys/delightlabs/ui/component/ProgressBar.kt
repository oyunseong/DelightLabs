package com.oys.delightlabs.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oys.delightlabs.ui.extension.noRippleClickable

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    enabledBackKey: Boolean = false,
    enabledOutsideTouch: Boolean = false,
) {
    if (!enabledBackKey) {
        BackHandler {
            // 백키 이벤트 무시
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (enabledOutsideTouch) Modifier
                else Modifier.noRippleClickable { }
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}