package team.iscode.igor.calculosinais

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import team.iscode.igor.calculosinais.databinding.ActivityCalibrationValuesBinding

class CalibrationValuesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalibrationValuesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalibrationValuesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val zeroInput = intent.getFloatExtra("zeroInput", 0.0f)
        val zeroOutput = intent.getFloatExtra("zeroOutput", 0.0f)
        val cemInput = intent.getFloatExtra("cemInput", 0.0f)
        val cemOutput = intent.getFloatExtra("cemOutput", 0.0f)
        val uMedSaida = intent.getStringExtra("uMedSaida")
        val uMedEntrada = intent.getStringExtra("uMedEntrada")


    }
}