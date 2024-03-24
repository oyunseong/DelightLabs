package com.oys.delightlabs.ui.component

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oys.delightlabs.R
import com.oys.delightlabs.ui.extension.noRippleClickable
import com.oys.delightlabs.ui.theme.subhead2

@Composable
fun ToolBar(
    modifier: Modifier = Modifier,
    title: String = "",
    isUnread: Boolean = false,
    onClickTitle: () -> Unit = {},  // 알람을 오게하기 위함
    onClickAlarm: () -> Unit = {},
    height: Int = 76 // 36 + 20 + 20
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .padding(horizontal = 28.dp, vertical = 20.dp),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .noRippleClickable { onClickTitle.invoke() },
            text = title,
            style = subhead2,
            color = Color.Black
        )

        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(24.dp)
                .noRippleClickable { onClickAlarm.invoke() },
            painter = painterResource(id = if (isUnread) R.drawable.ic_unread_notification else R.drawable.ic_notification),
            contentDescription = ""
        )
    }

}

@Preview
@Composable
private fun ToolBarPreview() {
    ToolBar(title = "Transactions")
}

