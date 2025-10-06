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