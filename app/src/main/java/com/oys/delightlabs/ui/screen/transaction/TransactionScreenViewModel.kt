package com.oys.delightlabs.ui.screen.transaction

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oys.delightlabs.data.repository.TransactionRepository
import com.oys.delightlabs.data.repository.TransactionType
import com.oys.delightlabs.ui.screen.transaction.graph.GraphModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TransactionScreenViewModel(
    private val transactionRepository: TransactionRepository = TransactionRepository()
) : ViewModel() {

    private val _uiState: MutableStateFlow<TransactionUiState> =
        MutableStateFlow(TransactionUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent: MutableSharedFlow<TransactionUiEvent> = MutableSharedFlow()


    init {
        viewModelScope.launch {
            getGraphModel(7)
            subscribeUiEvent()
            loadTransactions()

        }
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
                if (event.tab == uiState.value.selectedTransactionTab) return
                reducer { copy(selectedTransactionTab = event.tab) }
                when (event.tab) {
                    TransactionTab.WEEK -> {
//                        getTransactionsFromDate(7)
                        getGraphModel(7)
                    }

                    TransactionTab.MONTH -> {
//                        getTransactionsFromDate(30)
                        getGraphModel(30)
                    }
                }
            }

            is TransactionUiEvent.SelectTransactionCategory -> {
                reducer { copy(selectedTransactionCategory = event.category) }
            }

            TransactionUiEvent.NotificationEvent -> {
                reducer { copy(unReadNotification = true) }
            }

            TransactionUiEvent.OnClickNotification -> {
                reducer { copy(unReadNotification = false) }
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

    private fun getGraphModel(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                reducer {
                    copy(progress = true)
                }

                val incomeTransactions =
                    transactionRepository.getRecentTransactions(day, TransactionType.INCOME)
                val expenseTransactions =
                    transactionRepository.getRecentTransactions(day, TransactionType.EXPENSE)

                val incomeAmount: List<Float> = incomeTransactions.map { it.amount }
                val expenseAmount: List<Float> = expenseTransactions.map { it.amount }

                val incomeTimeStamps: List<String> = incomeTransactions.map { it.timestamp }
                val expenseTimeStamps: List<String> = expenseTransactions.map { it.timestamp }

                val max = maxOf(incomeAmount.max(), expenseAmount.max())
                val min = minOf(incomeAmount.min(), expenseAmount.min())

                val step = getStep(
                    max = max,
                    min = min,
                    divide = 30,
                )

                val xValues = IntRange(0, day - 1).toList()
                reducer {
                    copy(
                        income = GraphModel(
                            xValues = xValues,
                            yValues = generateYValues(
                                min = min,
                                max = max,
                                step = step
                            ),
                            points = incomeAmount,
                            timeStamps = incomeTimeStamps,
                            verticalStep = step,
                        ),
                        expense = GraphModel(
                            xValues = xValues,
                            yValues = generateYValues(
                                min = min,
                                max = max,
                                step = step
                            ),
                            points = expenseAmount,
                            timeStamps = expenseTimeStamps,
                            verticalStep = step,
                        )
                    )
                }
            }catch (e:Exception){
                e.printStackTrace()
            }finally {
                reducer {
                    copy(progress = false)
                }
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

    fun showTransactionNotification(context: Context) {
        viewModelScope.launch {
            delay(20000)
            Toast.makeText(context, "입출금 발생", Toast.LENGTH_SHORT).show()
            reducer {
                copy(unReadNotification = true)
            }
        }
    }

    /*
        fun getTransactionsFromDate(day: Int) {
        viewModelScope.launch {
            try {
                reducer {
                    copy(progress = true)
                }
                val (incomeTransactions, expenseTransactions) = withContext(Dispatchers.Default) {
                    val incomeDeferred = async {
                        transactionRepository.filterTransaction(
                            day = day.toLong(),
                            type = TransactionType.INCOME
                        )
                    }
                    val expenseDeferred = async {
                        transactionRepository.filterTransaction(
                            day = day.toLong(),
                            type = TransactionType.EXPENSE
                        )
                    }
                    Pair(incomeDeferred.await(), expenseDeferred.await())
                }

                val income: List<Float> = incomeTransactions.map { it.amount }
                val expense: List<Float> = expenseTransactions.map { it.amount }

                val max = maxOf(income.maxOrNull() ?: 0f, expense.maxOrNull() ?: 0f)
                val min = minOf(income.minOrNull() ?: 0f, expense.minOrNull() ?: 0f)

                val step = getStep(
                    max = max,
                    min = min,
                    divide = 30
                )

                reducer {
                    copy(
                        income = GraphModel(
                            xValues = listOf(0, 1, 2, 3, 4, 5, 6),
                            yValues = generateYValues(
                                min = min,
                                max = max,
                                step = step
                            ),
                            points = income,
                            verticalStep = step,
                        ),
                        expense = GraphModel(
                            xValues = listOf(0, 1, 2, 3, 4, 5, 6),
                            yValues = generateYValues(
                                min = min,
                                max = max,
                                step = step
                            ),
                            points = expense,
                            verticalStep = step,
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                reducer {
                    copy(progress = false)
                }
            }

        }
    }
     */
}