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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
            MaterialTheme {
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
private fun DiscountRadioButtons(
    dishCount: String,
    modifier: Modifier = Modifier
) {
    val discountOptions = listOf(
        "1-2 блюда: 3%" to 3f,
        "3-5 блюд: 5%" to 5f,
        "6-10 блюд: 7%" to 7f,
        "Более 10 блюд: 10%" to 10f
    )

    val currentDiscount = calculateDiscount(dishCount)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        discountOptions.forEach { (label, discount) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = currentDiscount == discount,
                    onClick = { /* Selection is automatic based on dish count */ }
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        if (currentDiscount > 0f) {
            Text(
                text = "Текущая скидка: ${currentDiscount.toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// Главный экран
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipCalculatorScreen() {
    var state by remember { mutableStateOf(CalculatorState()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Калькулятор чаевых и скидок",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Поле для суммы заказа
        OutlinedTextField(
            value = state.orderAmount,
            onValueChange = { newValue ->
                if (newValue.matches(Regex("^\\d*\\.?\\d*$")) || newValue.isEmpty()) {
                    state = state.copy(orderAmount = newValue)
                    state = calculateTotal(state)
                }
            },
            label = { Text("Сумма заказа") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Введите сумму") }
        )

        // Поле для количества блюд
        OutlinedTextField(
            value = state.dishCount,
            onValueChange = { newValue ->
                if (newValue.matches(Regex("^\\d*$")) || newValue.isEmpty()) {
                    state = state.copy(dishCount = newValue)
                    val discount = calculateDiscount(newValue)
                    state = state.copy(discountPercentage = discount)
                    state = calculateTotal(state)
                }
            },
            label = { Text("Количество блюд") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Введите количество") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Слайдер для чаевых
        Text(
            text = "Чаевые: ${state.tipPercentage.toInt()}%",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )

        Slider(
            value = state.tipPercentage,
            onValueChange = { newValue ->
                state = state.copy(tipPercentage = newValue)
                state = calculateTotal(state)
            },
            valueRange = 0f..25f,
            steps = 24,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Группа радиокнопок для скидки
        Text(
            text = "Скидка в зависимости от количества блюд:",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )

        DiscountRadioButtons(
            dishCount = state.dishCount,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Итоговая сумма
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Итоговая сумма:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (state.totalAmount.isNotEmpty()) state.totalAmount else "0.00",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Детали расчета
                if (state.orderAmount.isNotEmpty()) {
                    val orderAmount = state.orderAmount.toDoubleOrNull() ?: 0.0
                    val discountAmount = orderAmount * (state.discountPercentage / 100)
                    val tipAmount = orderAmount * (state.tipPercentage / 100)

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Сумма заказа: ${"%.2f".format(orderAmount)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Скидка: ${"%.2f".format(discountAmount)} (${state.discountPercentage.toInt()}%)",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Чаевые: ${"%.2f".format(tipAmount)} (${state.tipPercentage.toInt()}%)",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    // Используем стандартную тему для превью
    MaterialTheme {
        TipCalculatorScreen()
    }
}