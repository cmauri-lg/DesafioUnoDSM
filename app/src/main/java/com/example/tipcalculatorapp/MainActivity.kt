package com.example.tipcalculatorapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tipcalculatorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalcular.setOnClickListener {
            calcularPropina()
        }

        binding.btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun calcularPropina() {
        val montoStr = binding.editTextMonto.text.toString()
        val personasStr = binding.editTextPersonas.text.toString()

        if (montoStr.isEmpty() || personasStr.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val monto = montoStr.toDoubleOrNull()
        val personas = personasStr.toIntOrNull()

        if (monto == null || monto <= 0 || personas == null || personas <= 0) {
            Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.rbOtro.isChecked) {
            val otroStr = binding.editTextOtro.text.toString()
            val porcentajeOtro = otroStr.toDoubleOrNull()
            if (porcentajeOtro == null || porcentajeOtro <= 0) {
                Toast.makeText(this, "Ingresa un porcentaje válido", Toast.LENGTH_SHORT).show()
                return
            }
        }
        val incluirIVA = binding.switchIVA.isChecked
        if (!binding.rb10.isChecked && !binding.rb15.isChecked &&
            !binding.rb20.isChecked && !binding.rbOtro.isChecked) {
            Toast.makeText(this, "Selecciona un porcentaje de propina", Toast.LENGTH_SHORT).show()
            return
        }
        val porcentaje = when {
            binding.rb10.isChecked -> 10.0
            binding.rb15.isChecked -> 15.0
            binding.rb20.isChecked -> 20.0
            binding.rbOtro.isChecked -> binding.editTextOtro.text.toString().toDoubleOrNull() ?: 0.0
            else -> 0.0
        }

        val iva = if (incluirIVA) monto * 0.16 else 0.0
        val propina = monto * porcentaje / 100
        val total = monto + iva + propina
        val porPersona = try {
            total / personas
        } catch (e: ArithmeticException) {
            Toast.makeText(this, "Error al dividir entre personas", Toast.LENGTH_SHORT).show()
            return
        }

        val resultado = "Propina: $%.2f\nIVA: $%.2f\nTotal a pagar: $%.2f\nPor persona: $%.2f".format(
            propina, iva, total, porPersona)

        binding.textViewResultado.text = resultado
    }

    private fun limpiarCampos() {
        binding.editTextMonto.text.clear()
        binding.editTextPersonas.text.clear()
        binding.radioGroupPropina.clearCheck()
        binding.editTextOtro.text.clear()
        binding.switchIVA.isChecked = false
        binding.textViewResultado.text = ""
    }
}
