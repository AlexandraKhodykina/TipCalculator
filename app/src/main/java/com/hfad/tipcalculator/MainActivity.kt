package com.hfad.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hfad.tipcalculator.ui.theme.TipCalculatorTheme

data class CalculatorState(
    val orderAmount: String = "",        // Сумма заказа (текст для гибкости ввода)
    val dishCount: String = "",          // Количество блюд
    val tipPercentage: Float = 0f,       // Процент чаевых (0-25%)
    val discountPercentage: Float = 0f,  // Процент скидки (рассчитывается автоматически)
    val totalAmount: String = ""         // Итоговая сумма
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipCalculatorScreen()
                }

            }
        }
    }
}
//Эта функция принимает количество блюд и возвращает процент скидки исходя из заданных условий
private fun calculateDiscount(dishCount: String): Float {
    return when (val count = dishCount.toIntOrNull() ?: 0) {
        in 1..2 -> 3f
        in 3..5 -> 5f
        in 6..10 -> 7f
        else -> if (count > 10) 10f else 0f
    }
}

//Данная функция вычисляет итоговую сумму заказа
private fun calculateTotal(state: CalculatorState): CalculatorState {
    val orderAmount = state.orderAmount.toDoubleOrNull() ?: 0.0
    val tipAmount = orderAmount * (state.tipPercentage / 100)
    val discountAmount = orderAmount * (state.discountPercentage / 100)
    val total = orderAmount - discountAmount + tipAmount
    return state.copy(totalAmount = "%.2f".format(total))
}


@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

@Preview(showBackground = true)
@Composable
//fun GreetingPreview() {
//    TipCalculatorTheme {
//        Greeting("Android")
//    }
//}