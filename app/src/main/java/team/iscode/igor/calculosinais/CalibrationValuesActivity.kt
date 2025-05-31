package team.iscode.igor.calculosinais

import android.os.Bundle
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import team.iscode.igor.calculosinais.adapters.CalibrationValuesAdapter
import team.iscode.igor.calculosinais.adapters.ItemAdapter
import team.iscode.igor.calculosinais.databinding.ActivityCalibrationValuesBinding
import team.iscode.igor.calculosinais.models.CalibrationValues
import kotlin.math.pow

class CalibrationValuesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalibrationValuesBinding
    private lateinit var calibrationValuesAdapter: CalibrationValuesAdapter
    private lateinit var spinnerAdapterInput: Spinner
    //private lateinit var spinnerAdapterOutput: Spinner
    private var calibrationValuesResult: Map<Int,Float> = emptyMap()


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

        spinnerAdapterInput = findViewById(R.id.spinnerEntrada)
        //spinnerAdapterOutput = findViewById(R.id.spinnerSaida)

        initRecyclerView()

        calibrationValuesResult = calcOutput(zeroInput.toFloat(),cemInput.toFloat(), zeroOutput.toFloat(), cemOutput.toFloat())
        addDataSet(calibrationValuesResult)



    }

    //Calculation of output values
    private fun calcOutput(zeroValueInput: Float, cemValueInput: Float, zeroValueOutput: Float, cemValueOutput: Float): Map<Int,Float>{

        val spinnerValue = spinnerAdapterInput.selectedItem.toString()
        val resultMap = mutableMapOf<Int,Float>()

        val rangeOut = cemValueOutput-zeroValueOutput
        val rangeIn = cemValueInput-zeroValueInput


        for(percentage in listOf(0,25,50,75,100)){
            val valorOut = (rangeOut*(percentage.toFloat()/100))+zeroValueOutput
            val sRaiz = (((valorOut - zeroValueOutput)*rangeIn)/rangeOut)+zeroValueInput

            if (spinnerValue == "Não Linear"){
                val cRaiz = (sRaiz - zeroValueInput).pow(2f) /rangeIn + zeroValueInput
                resultMap[percentage] = String.format("%.2f",cRaiz).replace(",",".").toFloat()

            }else{
                resultMap[percentage] = String.format("%.2f",sRaiz).replace(",",".").toFloat()
            }
        }

        return resultMap

    }

    /*
    //Calculation of input values
    private fun calcInput(zeroValueInput: Float, cemValueInput: Float, zeroValueOutput: Float, cemValueOutput: Float): Map<Int,Float>{
        val spinnerValue = spinnerAdapterOutput.selectedItem.toString()
        val resultMap = mutableMapOf<Int,Float>()

        val rangeOut = cemValueOutput-zeroValueOutput
        val rangeIn = cemValueInput-zeroValueInput


        for(percentage in listOf(0,25,50,75,100)){
            val valorIn = rangeIn*(percentage.toFloat()/100)+zeroValueInput
            val sRaiz = (((valorIn - zeroValueInput)*rangeOut)/rangeIn)+zeroValueOutput

            if (spinnerValue == "Não Linear"){
                val cRaiz = (sqrt((sRaiz-zeroValueOutput)/rangeOut) *rangeOut)+zeroValueOutput
                resultMap[percentage] = String.format("%.2f",cRaiz).replace(",",".").toFloat()

            }else{
                resultMap[percentage] = String.format("%.2f",sRaiz).replace(",",".").toFloat()
            }
        }


        return resultMap
    }
    */

    private fun addDataSet(calibrationResult: Map<Int, Float>){
        val calibrationData = listOf(
            CalibrationValues(0, calibrationResult[0] ?: 0.0f),
            CalibrationValues(25,calibrationResult[25] ?: 0.0f),
            CalibrationValues(50,calibrationResult[50] ?: 0.0f),
            CalibrationValues(75,calibrationResult[75] ?: 0.0f),
            CalibrationValues(100,calibrationResult[100] ?: 0.0f)
        )

        calibrationValuesAdapter.setCalibrationList(calibrationData)

    }

    private fun initRecyclerView(){
        binding.recyclerViewEntrada.apply {
            layoutManager = LinearLayoutManager(this@CalibrationValuesActivity)
            calibrationValuesAdapter = CalibrationValuesAdapter()
            adapter = calibrationValuesAdapter
        }
    }

}