package com.oys.delightlabs.ui.screen.transaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oys.delightlabs.data.repository.TransactionRepository
import com.oys.delightlabs.data.repository.TransactionType
import com.oys.delightlabs.ui.screen.transaction.graph.GraphModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TransactionScreenViewModel(
    private val transactionRepository: TransactionRepository = TransactionRepository()
) : ViewModel() {

    private val _uiState: MutableStateFlow<TransactionUiState> =
        MutableStateFlow(TransactionUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent: MutableSharedFlow<TransactionUiEvent> = MutableSharedFlow()

    init {
        getGraphModel()
        subscribeUiEvent()
        loadTransactions()
    }

    fun emitUiEvent(event: TransactionUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun subscribeUiEvent() {
        viewModelScope.launch {
            _uiEvent.collect { event ->
                handleUiEvent(event)
            }
        }
    }

    private fun handleUiEvent(event: TransactionUiEvent) {
        when (event) {
            is TransactionUiEvent.SelectTransactionTab -> {
                reducer { copy(selectedTransactionTab = event.tab) }
            }

            is TransactionUiEvent.SelectTransactionCategory -> {
                reducer { copy(selectedTransactionCategory = event.category) }
            }
        }
    }

    private fun reducer(action: TransactionUiState.() -> TransactionUiState) {
        _uiState.update {
            action.invoke(it)
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            val pages = listOf(
                TransactionPage(
                    category = TransactionCategory.ALL,
                    transaction = transactionRepository.getRecentTransactions(
                        size = 20,
                        type = TransactionType.All
                    )
                ),
                TransactionPage(
                    category = TransactionCategory.EXPENSE,
                    transaction = transactionRepository.getRecentTransactions(
                        size = 10,
                        type = TransactionType.EXPENSE
                    )
                ),
                TransactionPage(
                    category = TransactionCategory.INCOME,
                    transaction = transactionRepository.getRecentTransactions(
                        size = 10,
                        type = TransactionType.INCOME
                    )
                ),
            )

            reducer {
                copy(
                    transactionPages = pages,
                )
            }
        }
    }

    fun updateUiState(action: TransactionUiState.() -> TransactionUiState) {
        _uiState.update {
            action.invoke(it)
        }
    }
    fun getGraphModel() {
        viewModelScope.launch(Dispatchers.IO) {
            val t = transactionRepository.getRecentTransactions(7, TransactionType.INCOME).also {
                val amounts = it.map { it.amount }.sorted()
                Log.d("++##", amounts.toString())
            }
            val t1= transactionRepository.getRecentTransactions(7, TransactionType.EXPENSE)
            val income: List<Float> = t.map { it.amount }
            val expense: List<Float> = t1.map { it.amount }

            val max = maxOf(income.max(), expense.max())
            val min = minOf(income.min(), expense.min())

            val step = getStep(
                max = max,
                min = min,
                divide = 30
            )

            updateUiState {
                copy(
                    income = GraphModel(
                        xValues = listOf(0, 1, 2, 3, 4, 5, 6),
                        yValues = generateYValues(
                            min = min,
                            max = max,
                            step
                        ),
                        points = income, //그래프,
                        verticalStep = step,
                    ),
                    expense = GraphModel(
                        xValues = listOf(0, 1, 2, 3, 4, 5, 6),
                        yValues = generateYValues(
                            min = min,
                            max = max,
                            step
                        ),
                        points = expense, //그래프,
                        verticalStep = step,
                    )
                )
            }

        }
    }


    private fun getStep(
        max: Float,
        min: Float,
        divide: Int = 10,
    ): Float {
        return (max - min) / divide
    }

    private fun generateYValues(
        min: Float,
        max: Float,
        step: Float
    ): List<Float> {
        val result = mutableListOf<Float>()
        var currentValue = min
        while (currentValue <= max) {
            result.add(currentValue)
            currentValue += step
        }
        return result
    }
}