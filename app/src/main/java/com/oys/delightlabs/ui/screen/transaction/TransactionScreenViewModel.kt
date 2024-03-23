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
                copy(transactionPages = pages)
            }
        }
    }

    private fun getGraphModel(): GraphModel {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.filterTransaction(7).also {
                it.forEach {
                    Log.d("++##", "it=$it")
                }
            }
        }


////        val xValues = IntRange(1, t.size).toList()
//        val a = t.map { it.amount }
//        val yStep = calculateYStep(
//            points = a,
//            stepCount = a.size
//        ).also {
//            Log.d("++##", "yStep=$it")
//        }
//
//        val max = a.max()
//        val min = a.min()
//
//
//        val divide = 10
//        val range = max - min
//        val step = range / divide

        return GraphModel(
            xValues = listOf(1, 2, 3, 4, 5, 6, 7),
            yValues = listOf(1f, 2f, 3f),
            points = listOf(1f, 2f), //그래프,
            verticalStep = 2000f,
        )
    }


    fun generateYValues(points: List<Float>, step: Float): List<Float> {
        val min = points.minOrNull() ?: 0f
        val max = points.maxOrNull() ?: 0f

        var currentValue = min
        val result = mutableListOf<Float>()
//        while (true) {
//            if (result.isEmpty()) {
//                result.add(min)
//                Log.d("++##","add min $min / result : $result")
//            } else {
//                currentValue += step
//                if (currentValue > max) {
//                    Log.d("++##","break / result : $result")
//                    break
//                }else{
//                    Log.d("++##","add $currentValue / result : $result")
//                    result.add(currentValue)
//                }
//            }
//        }
        Log.d("++##", "result : $result")
        return result
    }

    fun calculateYStep(points: List<Float>, stepCount: Int = 7): Int {
        val minValue = points.minOrNull() ?: 0f
        val maxValue = points.maxOrNull() ?: 0f

        val range = if (minValue < 0) {
            maxValue + minValue
        } else {
            maxValue - minValue
        }

        return if (range > 0) {
            val step = range / stepCount
            val roundedStep = (step / 10).toInt() * 10 // 10의 배수로 반올림
            if (roundedStep > 0) roundedStep else 10 // 최소 yStep은 10으로 설정
        } else {
            10 // 기본값은 10으로 설정
        }
    }
}