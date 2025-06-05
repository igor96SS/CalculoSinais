package team.iscode.igor.calculosinais

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import team.iscode.igor.calculosinais.adapters.CalibrationValuesAdapter
import team.iscode.igor.calculosinais.adapters.VerificationValuesAdapter
import team.iscode.igor.calculosinais.databinding.ActivityCalibrationValuesBinding
import team.iscode.igor.calculosinais.interfaces.OnCalibrationChangedListener
import team.iscode.igor.calculosinais.models.CalibrationValues
import team.iscode.igor.calculosinais.models.VerificationValues
import java.util.Locale
import java.util.Objects
import kotlin.math.pow

class CalibrationValuesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalibrationValuesBinding
    private lateinit var calibrationValuesAdapter: CalibrationValuesAdapter
    private lateinit var verificationValuesAdapter: VerificationValuesAdapter
    private lateinit var spinnerAdapterInput: Spinner
    private var calibrationValuesResult: Map<Int,Float> = emptyMap()
    private var inputValues = mutableListOf<Float>()


    private var verificationValuesResult: List<Float> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalibrationValuesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back Button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val zeroInput = intent.getFloatExtra("zeroInput", 0.0f)
        val zeroOutput = intent.getFloatExtra("zeroOutput", 0.0f)
        val cemInput = intent.getFloatExtra("cemInput", 0.0f)
        val cemOutput = intent.getFloatExtra("cemOutput", 0.0f)
        val uMedSaida = intent.getStringExtra("uMedSaida")
        val uMedEntrada = intent.getStringExtra("uMedEntrada")

        spinnerAdapterInput = findViewById(R.id.spinnerEntrada)
        //spinnerAdapterOutput = findViewById(R.id.spinnerSaida)

        initRecyclerView()

        calibrationValuesResult = calcOutput(zeroInput, cemInput, zeroOutput, cemOutput)
        val calibrationData = addCalibrationSet(calibrationValuesResult)
        calibrationValuesAdapter.setParams(zeroInput,cemInput,true)

        // Pass input values from calibration data to the verification adapter for error calculation
        inputValues = calibrationData.map { it.inputValues }.toMutableList()
        verificationValuesAdapter = VerificationValuesAdapter(inputValues)
        binding.recyclerViewVerification.adapter = verificationValuesAdapter

        // Update output values when percentage change
        calibrationValuesAdapter.setOnCalibrationChangedListener(object :
            OnCalibrationChangedListener {
            override fun onOutputValueChanged(position: Int, newPercentageValue: Float, newInputValue: Float) {
                // Calcula novo sinal (inputValue)
                val range = cemOutput - zeroOutput
                val newOutputValue = (range * newPercentageValue / 100f) + zeroOutput

                // Atualiza o valor no adapter de verificação
                verificationValuesAdapter.updateInputValueAt(position, newInputValue)

                verificationValuesAdapter.updateOutputValueAt(position, newOutputValue)
            }
        })

        // Sending values to adapter
        verificationValuesResult = calcInput(zeroInput, cemInput, zeroOutput, cemOutput)
        setupVerificationSection(verificationValuesResult)

        spinnerAdapterInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currentFocus?.clearFocus()
                recalculateCalibrationValues(zeroInput, cemInput, zeroOutput, cemOutput)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        binding.uMedidaEntradaValue.text = uMedEntrada
        binding.uMedidaSaidaValue.text = uMedSaida

        binding.sUncertaintyEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val uncertainty = s?.toString()?.toFloatOrNull()
                val errorPercent = binding.maxErrorPercentEt.text?.toString()?.toIntOrNull()

                if (uncertainty != null && errorPercent != null) {
                    val acceptanceLimit = limitCalculation(errorPercent, zeroInput, cemInput, uncertainty)
                    binding.acceptanceLimitTv.text = String.format(Locale.US, "%.2f", acceptanceLimit)
                } else {
                    binding.acceptanceLimitTv.hint = getString(R.string.limite_aceitaco)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.maxErrorPercentEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val errorPercent = s?.toString()?.toIntOrNull()
                val uncertainty = binding.sUncertaintyEt.text?.toString()?.toFloatOrNull()

                if (errorPercent != null && uncertainty != null) {
                    val acceptanceLimit = limitCalculation(errorPercent, zeroInput, cemInput, uncertainty)
                    binding.acceptanceLimitTv.text = String.format(Locale.US, "%.2f", acceptanceLimit)
                } else {
                    binding.acceptanceLimitTv.hint = getString(R.string.limite_aceitaco)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

    private fun recalculateCalibrationValues(
        zeroInput: Float,
        cemInput: Float,
        zeroOutput: Float,
        cemOutput: Float
    ) {
        val currentPercentages = calibrationValuesAdapter.getPercentages()

        calibrationValuesResult = calcOutput(zeroInput, cemInput, zeroOutput, cemOutput, currentPercentages)
        val calibrationData = addCalibrationSet(calibrationValuesResult, currentPercentages)

        calibrationValuesAdapter.setParams(
            zeroInput,
            cemInput,
            spinnerAdapterInput.selectedItem.toString() != "Linear"
        )

        inputValues = calibrationData.map { it.inputValues }.toMutableList()
        verificationValuesResult = calcInput(zeroInput, cemInput, zeroOutput, cemOutput)

        setupVerificationSection(verificationValuesResult)
    }


    //Calculation of output values
    private fun calcOutput(
        zeroValueInput: Float,
        cemValueInput: Float,
        zeroValueOutput: Float,
        cemValueOutput: Float,
        customPercentages: List<Float> = listOf(0f, 25f, 50f, 75f, 100f)
    ): Map<Int, Float> {
        val spinnerValue = spinnerAdapterInput.selectedItem.toString()
        val resultMap = mutableMapOf<Int, Float>()

        val rangeOut = cemValueOutput - zeroValueOutput
        val rangeIn = cemValueInput - zeroValueInput

        for (percentage in customPercentages) {
            val valorOut = (rangeOut * (percentage / 100f)) + zeroValueOutput
            val sRaiz = (((valorOut - zeroValueOutput) * rangeIn) / rangeOut) + zeroValueInput

            val input = if (spinnerValue == "Não Linear") {
                ((sRaiz - zeroValueInput).pow(2f) / rangeIn) + zeroValueInput
            } else {
                sRaiz
            }

            resultMap[percentage.toInt()] = String.format("%.2f", input).replace(",", ".").toFloat()
        }

        return resultMap
    }


    //Calculation of input values
    private fun calcInput(zeroValueInput: Float, cemValueInput: Float, zeroValueOutput: Float, cemValueOutput: Float): List<Float>{
        val resultMap = mutableListOf<Float>()

        val rangeOut = cemValueOutput-zeroValueOutput
        val rangeIn = cemValueInput-zeroValueInput

        for(percentage in listOf(0,25,50,75,100)){
            val valorIn = rangeIn*(percentage.toFloat()/100)+zeroValueInput
            val sRaiz = (((valorIn - zeroValueInput)*rangeOut)/rangeIn)+zeroValueOutput

            resultMap.add(String.format("%.2f",sRaiz).replace(",",".").toFloat())
        }

        return resultMap
    }


    private fun addCalibrationSet(
        calibrationResult: Map<Int, Float>,
        customPercentages: List<Float> = listOf(0f, 25f, 50f, 75f, 100f)
    ): List<CalibrationValues> {
        val calibrationData = customPercentages.map {
            CalibrationValues(it.toInt(), calibrationResult[it.toInt()] ?: 0f)
        }

        calibrationValuesAdapter.setCalibrationList(calibrationData)
        return calibrationData
    }


    private fun setupVerificationSection(outputValues: List<Float>) {
        val currentList = verificationValuesAdapter.getCurrentList()

        val updatedList = outputValues.mapIndexed { index, output ->
            val existing = currentList.getOrNull(index)
            VerificationValues(
                outputValues = output,
                readValues = existing?.readValues, // preserva o valor digitado
                error = existing?.error ?: 0f
            )
        }

        verificationValuesAdapter.setVerificationList(updatedList)
    }


    private fun initRecyclerView(){
        binding.recyclerViewCalibration.apply {
            layoutManager = LinearLayoutManager(this@CalibrationValuesActivity)
            calibrationValuesAdapter = CalibrationValuesAdapter()
            adapter = calibrationValuesAdapter
        }

        binding.recyclerViewVerification.apply {
            layoutManager = LinearLayoutManager(this@CalibrationValuesActivity)
        }
    }

    private fun limitCalculation(maxErrorPercent:Int?, zeroValueInput: Float, cemValueInput: Float, sUncertainty: Float?): Float{
        return ((cemValueInput-zeroValueInput)* maxErrorPercent!! /100)-sUncertainty!!
    }

    // Handles back button click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish() // Closes current activity
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}