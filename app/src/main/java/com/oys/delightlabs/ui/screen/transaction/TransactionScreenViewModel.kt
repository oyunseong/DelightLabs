package com.oys.delightlabs.ui.screen.transaction

import android.util.Log
import androidx.core.util.toRange
import androidx.lifecycle.ViewModel
import com.oys.delightlabs.data.repository.TransactionRepository
import com.oys.delightlabs.ui.screen.transaction.graph.GraphModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class TransactionScreenViewModel(
    private val transactionRepository: TransactionRepository = TransactionRepository()

) : ViewModel() {

    private val _uiState: MutableStateFlow<TransactionUiState> =
        MutableStateFlow(TransactionUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUiState(action: TransactionUiState.() -> TransactionUiState) {
        _uiState.update {
            action.invoke(it)
        }
    }

    fun getGraphModel(): GraphModel {
        val t = transactionRepository.filterTransaction().also {
            val amounts = it.map { it.amount }.sorted()
            Log.d("++##", amounts.toString())
        }

//        val xValues = IntRange(1, t.size).toList()
        val a = t.map { it.amount }
        val yStep = calculateYStep(
            points = a,
            stepCount = a.size
        ).also {
            Log.d("++##", "yStep=$it")
        }

        val max = a.max()
        val min = a.min()


        val divide = 10
        val range = max - min
        val step = range / divide

        return GraphModel(
            xValues = listOf(1, 2, 3, 4, 5, 6, 7),
            yValues = generateYValues(a, step),
            points = a, //그래프,
            verticalStep = step,
        )
    }


    fun generateYValues(points: List<Float>, step: Float): List<Float> {
        val min = points.minOrNull() ?: 0f
        val max = points.maxOrNull() ?: 0f

        var currentValue = min
        val result = mutableListOf<Float>()
        while (true) {
            if (result.isEmpty()) {
                result.add(min)
                Log.d("++##","add min $min / result : $result")
            } else {
                currentValue += step
                if (currentValue > max) {
                    Log.d("++##","break / result : $result")
                    break
                }else{
                    Log.d("++##","add $currentValue / result : $result")
                    result.add(currentValue)
                }
            }
        }
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