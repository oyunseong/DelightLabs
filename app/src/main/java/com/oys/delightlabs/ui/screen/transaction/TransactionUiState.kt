package com.oys.delightlabs.ui.screen.transaction

import com.oys.delightlabs.data.repository.Transaction
import com.oys.delightlabs.ui.screen.transaction.graph.GraphModel

sealed interface TransactionUiEvent {
    data class SelectTransactionTab(val tab: TransactionTab) : TransactionUiEvent
    data class SelectTransactionCategory(val category: TransactionCategory) : TransactionUiEvent
}

data class TransactionUiState(
    val transactionTabs: List<TransactionTab> = listOf(TransactionTab.WEEK, TransactionTab.MONTH),
    val selectedTransactionTab: TransactionTab = TransactionTab.WEEK,
    val transactionCategories: List<TransactionCategory> = TransactionCategory.INITIAL,
    val selectedTransactionCategory: TransactionCategory = TransactionCategory.ALL,
    val transactionPages: List<TransactionPage> = emptyList(),
    val progress: Boolean = false, //todo ui에 추가하기
    val income: GraphModel = GraphModel(),
    val expense: GraphModel = GraphModel(),
)

enum class TransactionTab(val text: String) {
    WEEK("Week"), MONTH("Month"),
}

enum class TransactionCategory(val text: String) {
    ALL("ALL"),
    EXPENSE("EXPENSE"),
    INCOME("INCOME");

    companion object {
        val INITIAL = listOf(ALL, EXPENSE, INCOME)
    }
}

data class TransactionPage(
    val category: TransactionCategory,
    val transaction: List<Transaction> = emptyList(),
)