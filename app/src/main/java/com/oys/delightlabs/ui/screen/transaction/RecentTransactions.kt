package com.oys.delightlabs.ui.screen.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oys.delightlabs.R
import com.oys.delightlabs.ui.component.HorizontalSpacer
import com.oys.delightlabs.ui.component.VerticalSpacer
import com.oys.delightlabs.ui.extension.noRippleClickable
import com.oys.delightlabs.ui.theme.Gray300
import com.oys.delightlabs.ui.theme.Gray400
import com.oys.delightlabs.ui.theme.Gray650
import com.oys.delightlabs.ui.theme.body2
import com.oys.delightlabs.ui.theme.body3
import com.oys.delightlabs.ui.theme.mainColor
import com.oys.delightlabs.ui.theme.subhead1

@Composable
fun RecentTransactions() {
    var c by remember { mutableStateOf(TransactionCategory.ALL) }   // TODO viewModel까지 이동
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.category_recent_transactions),
            style = subhead1,
            color = Color.Black
        )
        VerticalSpacer(dp = 30.dp)
        TransactionCategory(
            selectedCategory = c,
            onClick = {
                c = it
            }
        )
        VerticalSpacer(dp = 30.dp)
        TransactionList()
    }
}

enum class TransactionCategory {
    ALL,
    EXPENSE,
    INCOME
}

@Composable
fun TransactionCategory(
    selectedCategory: TransactionCategory = TransactionCategory.ALL,
    categories: List<TransactionCategory> = listOf(
        TransactionCategory.ALL,
        TransactionCategory.EXPENSE,
        TransactionCategory.INCOME
    ),
    onClick: (TransactionCategory) -> Unit = {}
) {
    Row {
        categories.forEach {
            Text(
                modifier = Modifier.noRippleClickable {
                    onClick.invoke(it)
                },
                text = it.name,
                color = if (it == selectedCategory) mainColor else Gray400,
                style = body3
            )
            HorizontalSpacer(dp = 25.dp)
        }
    }
}

@Composable
fun TransactionList() {
    repeat(20) {
        TransactionItem()
        VerticalSpacer(dp = 20.dp)
    }
}

@Composable
fun TransactionItem() {
    Row(modifier = Modifier) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Gray300)
                .size(51.dp)
        )
        HorizontalSpacer(dp = 20.dp)
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "name",
                    style = body3.copy(fontWeight = FontWeight.Medium),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "-$432.9",
                    style = body3.copy(fontWeight = FontWeight.Bold),
                    color = mainColor
                )
            }
            VerticalSpacer(dp = 1.dp)
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "name",
                    style = body2.copy(fontWeight = FontWeight.Normal),
                    color = Gray650
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "3.30 AM",
                    style = body2.copy(fontWeight = FontWeight.Normal),
                    color = Gray650
                )
            }
        }
    }
}

@Preview
@Composable()
private fun RecentTransactionsPreview() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        RecentTransactions()
    }
}