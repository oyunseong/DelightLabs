package com.oys.delightlabs.ui.screen.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oys.delightlabs.R
import com.oys.delightlabs.ui.component.ToolBar
import com.oys.delightlabs.ui.component.VerticalSpacer
import com.oys.delightlabs.ui.extension.noRippleClickable
import com.oys.delightlabs.ui.screen.transaction.graph.GraphModel
import com.oys.delightlabs.ui.screen.transaction.graph.GraphScreen
import com.oys.delightlabs.ui.theme.Gray100
import com.oys.delightlabs.ui.theme.Gray650
import com.oys.delightlabs.ui.theme.body1
import com.oys.delightlabs.ui.theme.body2
import com.oys.delightlabs.ui.theme.body3
import com.oys.delightlabs.ui.theme.caption1
import com.oys.delightlabs.ui.theme.mainColor

@Composable
fun TransactionsScreen(
    vm: TransactionScreenViewModel = viewModel(),
) {
    val uiState by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        ToolBar(
            title = stringResource(id = R.string.transaction),
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            VerticalSpacer(dp = 2.dp)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TabRowComponent(
                    transactionTabs = uiState.transactionTabs,
                    selectedTransactionTab = uiState.selectedTransactionTab,
                    onSelect = {
                        vm.emitUiEvent(TransactionUiEvent.SelectTransactionTab(it))
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "MM DD, YYYY",
                    textAlign = TextAlign.End,
                    style = caption1,
                    color = mainColor
                )
            }
            VerticalSpacer(dp = 10.dp)
            if (uiState.income != GraphModel.empty && uiState.expense != GraphModel.empty) {
                GraphScreen(
                    incomeGraphModel = uiState.income,
                    expenseGraphModel = uiState.expense,
                )
            }
            VerticalSpacer(dp = 40.dp)
            RecentTransactions(
                categories = uiState.transactionCategories,
                selectedCategory = uiState.selectedTransactionCategory,
                transactionPage = uiState.transactionPages,
                onSelect = {
                    vm.emitUiEvent(TransactionUiEvent.SelectTransactionCategory(it))
                }
            )
        }
    }
}

@Composable
fun TabRowComponent(
    transactionTabs: List<TransactionTab> = emptyList(),
    selectedTransactionTab: TransactionTab = TransactionTab.WEEK,
    onSelect: (TransactionTab) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Gray100),
        verticalAlignment = Alignment.CenterVertically
    ) {
        transactionTabs.forEach { tab ->
            val selected = selectedTransactionTab == tab
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (selected) mainColor else Gray100)
                    .padding(vertical = 5.dp, horizontal = 17.dp)
                    .noRippleClickable {
                        onSelect(tab)
                    },
                text = tab.text,
                style = body3,
                color = if (selected) Color.White else Gray650
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.background(Color.White)) {
        TabRowComponent(
            transactionTabs = listOf(TransactionTab.WEEK, TransactionTab.MONTH)
        )
    }
}