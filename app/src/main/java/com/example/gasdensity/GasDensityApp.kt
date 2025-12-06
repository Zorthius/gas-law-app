package com.example.gasdensity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.exp

@Composable
fun GasDensityApp() {
    val gases = listOf("Hydrogen", "Helium", "Oxygen", "Air")
    var selectedGas by remember { mutableStateOf(gases.first()) }
    var temperature by remember { mutableStateOf("20") }
    var altitude by remember { mutableStateOf("0") }
    var result by remember { mutableStateOf("") }
    var atmDesign by remember { mutableStateOf("") }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()) {
                Text("Gas Density Calculator", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(12.dp))

                // Dropdown
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    TextField(
                        value = selectedGas,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Gas Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        gases.forEach { gas ->
                            DropdownMenuItem(text = { Text(gas) }, onClick = {
                                selectedGas = gas
                                expanded = false
                            })
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = temperature,
                    onValueChange = { temperature = it },
                    label = { Text("Temperature (°C)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Spacer(Modifier.height(8.dp))

                TextField(
                    value = altitude,
                    onValueChange = { altitude = it },
                    label = { Text("Altitude (km)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                Button(onClick = {
                    val tempC = temperature.toDoubleOrNull() ?: 20.0
                    val altKm = altitude.toDoubleOrNull() ?: 0.0
                    val densityVal = calculateDensity(selectedGas, tempC, altKm)
                    val atmVal = atmosphereDesign(altKm)
                    result = "Density: %.6f kg/m³".format(densityVal)
                    atmDesign = atmVal
                }) {
                    Text("Calculate Density")
                }

                Spacer(Modifier.height(16.dp))
                Text(result)
                Spacer(Modifier.height(8.dp))
                Text("Atmosphere: $atmDesign")
            }
        }
    }
}

// Calculation functions
fun calculateDensity(gas: String, tempC: Double, altitudeKm: Double): Double {
    val molarMass = when (gas) {
        "Hydrogen" -> 0.002016
        "Helium" -> 0.0040026
        "Oxygen" -> 0.031998
        else -> 0.02897 // Air
    }

    val tempK = tempC + 273.15

    val pressure0 = 101325.0
    val scaleHeight = 8.4 // km
    val pressure = pressure0 * exp(-altitudeKm / scaleHeight)

    val R = 8.314462618 // universal gas constant J/(mol·K)
    return (pressure * molarMass) / (R * tempK)
}

fun atmosphereDesign(altitudeKm: Double): String {
    return when {
        altitudeKm < 12 -> "Troposphere"
        altitudeKm < 50 -> "Stratosphere"
        altitudeKm < 80 -> "Mesosphere"
        else -> "Thermosphere"
    }
}
