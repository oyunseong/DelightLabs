package com.oys.delightlabs.ui.screen.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oys.delightlabs.R
import com.oys.delightlabs.data.repository.Transaction
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
fun RecentTransactions(
    categories: List<TransactionCategory> = emptyList(),
    selectedCategory: TransactionCategory,
    transactionPage: List<TransactionPage> = emptyList(),
    onSelect: (TransactionCategory) -> Unit = {}
) {
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
            selectedCategory = selectedCategory,
            categories = categories,
            onClick = {
                onSelect(it)
            }
        )
        VerticalSpacer(dp = 30.dp)
        transactionPage.find { it.category == selectedCategory }?.let {
            TransactionPage(
                page = it
            )
        }
    }
}

@Composable
fun TransactionCategory(
    categories: List<TransactionCategory> = emptyList(),
    selectedCategory: TransactionCategory = TransactionCategory.ALL,
    onClick: (TransactionCategory) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50)),
    ) {
        categories.forEach { category ->
            val selected = selectedCategory == category
            Text(
                modifier = Modifier.noRippleClickable {
                    onClick.invoke(category)
                },
                text = category.text,
                color = if (selected) mainColor else Gray400,
                style = body3
            )
            HorizontalSpacer(dp = 25.dp)
        }
    }
}

@Composable
fun TransactionPage(
    page: TransactionPage
) {
    repeat(page.transaction.size) {
        TransactionItem(page.transaction[it])
        VerticalSpacer(dp = 20.dp)
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
) {
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
                    modifier = Modifier.weight(1f),
                    text = transaction.name,
                    style = body3.copy(fontWeight = FontWeight.Medium),
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                HorizontalSpacer(dp = 4.dp)
                Text(
                    text = if (transaction.amount < 0) {
                        "-$${-transaction.amount}"
                    } else {
                        "+$${transaction.amount}"
                    },
                    style = body3.copy(fontWeight = FontWeight.Bold),
                    color = mainColor,
                    maxLines = 1,
                )
            }
            VerticalSpacer(dp = 1.dp)
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = transaction.type,
                    style = body2.copy(fontWeight = FontWeight.Normal),
                    color = Gray650,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${transaction.time}",
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
        RecentTransactions(
            categories = TransactionCategory.INITIAL,
            selectedCategory = TransactionCategory.ALL
        )
    }
}