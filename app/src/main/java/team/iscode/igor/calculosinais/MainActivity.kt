package team.iscode.igor.calculosinais

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import team.iscode.igor.calculosinais.adapters.ItemAdapter
import team.iscode.igor.calculosinais.models.Item
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewInput: RecyclerView
    private lateinit var recyclerViewOutput: RecyclerView

    private lateinit var itemAdapterInput: ItemAdapter
    private lateinit var itemAdapterOutput: ItemAdapter
    private lateinit var spinnerAdapterInput: Spinner
    private lateinit var spinnerAdapterOutput: Spinner
    private lateinit var buttonConfig: FloatingActionButton

    private lateinit var unidadeMedidaEntrada: TextView
    private lateinit var zeroValueEntrada: String
    private lateinit var cemValueEntrada: String

    private lateinit var unidadeMedidaSaida: TextView
    private lateinit var zeroValueSaida: String
    private lateinit var cemValueSaida: String
    private var outputResult: Map<Int,Float> = emptyMap()
    private var inputResult: Map<Int,Float> = emptyMap()

    private lateinit var cardBoxInput: CardView
    private lateinit var cardBoxOutput: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewInput = findViewById(R.id.recyclerViewEntrada)
        recyclerViewOutput = findViewById(R.id.recyclerViewSaida)
        spinnerAdapterInput = findViewById(R.id.spinnerEntrada)
        spinnerAdapterOutput = findViewById(R.id.spinnerSaida)
        buttonConfig = findViewById(R.id.floatBTN)
        unidadeMedidaEntrada = findViewById(R.id.uMedidaEntradaValue)
        unidadeMedidaSaida = findViewById(R.id.uMedidaSaidaValue)

        cardBoxInput = findViewById(R.id.cardViewEntrada)
        cardBoxOutput = findViewById(R.id.cardViewSaida)

        initRecyclerView()


        buttonConfig.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.layout_config_dialog, null)
            val alertDialog = AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setView(dialogView)
                .setPositiveButton("Salvar", null)
                .create()

            alertDialog.setOnShowListener {
                val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    val unidadeMedidaEntradaEditText = dialogView.findViewById<EditText>(R.id.unidadeMedidaEntrada)
                    val zeroValueEntradaEditText = dialogView.findViewById<EditText>(R.id.zeroValueEntrada)
                    val cemValueEntradaEditText = dialogView.findViewById<EditText>(R.id.cemValueEntrada)
                    val unidadeMedidaSaidaEditText = dialogView.findViewById<EditText>(R.id.unidadeMedidaSaida)
                    val zeroValueSaidaEditText = dialogView.findViewById<EditText>(R.id.zeroValueSaida)
                    val cemValueSaidaEditText = dialogView.findViewById<EditText>(R.id.cemValueSaida)

                    if (zeroValueEntradaEditText.text.isEmpty()) {
                        zeroValueEntradaEditText.error = "Campo obrigatório"
                    } else {
                        zeroValueEntradaEditText.error = null
                    }

                    if (cemValueEntradaEditText.text.isEmpty()) {
                        cemValueEntradaEditText.error = "Campo obrigatório"
                    } else {
                        cemValueEntradaEditText.error = null
                    }

                    if (zeroValueSaidaEditText.text.isEmpty()) {
                        zeroValueSaidaEditText.error = "Campo obrigatório"
                    } else {
                        zeroValueSaidaEditText.error = null
                    }

                    if (cemValueSaidaEditText.text.isEmpty()) {
                        cemValueSaidaEditText.error = "Campo obrigatório"
                    } else {
                        cemValueSaidaEditText.error = null
                    }

                    // Verifique se todos os campos estão preenchidos
                    val allFieldsFilled = zeroValueEntradaEditText.text.isNotEmpty() &&
                            cemValueEntradaEditText.text.isNotEmpty() &&
                            zeroValueSaidaEditText.text.isNotEmpty() &&
                            cemValueSaidaEditText.text.isNotEmpty()

                    if (allFieldsFilled) {

                        // VALORES ENTRADA
                        unidadeMedidaEntrada.text = unidadeMedidaEntradaEditText.text.toString()
                        zeroValueEntrada = zeroValueEntradaEditText.text.toString()
                        cemValueEntrada = cemValueEntradaEditText.text.toString()


                        // VALORES SAIDA

                        unidadeMedidaSaida.text = unidadeMedidaSaidaEditText.text.toString()
                        zeroValueSaida = zeroValueSaidaEditText.text.toString()
                        cemValueSaida = cemValueSaidaEditText.text.toString()

                        outputResult = calcOutput(zeroValueEntrada.toFloat(),cemValueEntrada.toFloat(), zeroValueSaida.toFloat(), cemValueSaida.toFloat())
                        inputResult = calcInput(zeroValueEntrada.toFloat(),cemValueEntrada.toFloat(), zeroValueSaida.toFloat(), cemValueSaida.toFloat())
                        addDataSet(outputResult, inputResult)

                        alertDialog.dismiss()

                    }
                }
            }

            alertDialog.show()
        }


        cardBoxInput.setOnClickListener {
            if (isVariablesInitialized()) {
                val intent = Intent(this, TabsActivity::class.java)
                intent.putExtra("zeroInput", zeroValueEntrada.toFloat())
                intent.putExtra("zeroOutput", zeroValueSaida.toFloat())
                intent.putExtra("cemInput", cemValueEntrada.toFloat())
                intent.putExtra("cemOutput", cemValueSaida.toFloat())
                startActivity(intent)
            } else {
                // Variáveis não inicializadas, exiba uma mensagem de erro em um diálogo
                val alertDialog = AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Erro")
                    .setMessage("Por favor, preencha os valores no diálogo de configuração.")
                    .setPositiveButton("OK", null)
                    .create()
                alertDialog.show()
            }
        }

        cardBoxOutput.setOnClickListener {
            if (isVariablesInitialized()) {
                val intent = Intent(this, TabsActivity::class.java)
                intent.putExtra("zeroInput", zeroValueEntrada.toFloat())
                intent.putExtra("zeroOutput", zeroValueSaida.toFloat())
                intent.putExtra("cemInput", cemValueEntrada.toFloat())
                intent.putExtra("cemOutput", cemValueSaida.toFloat())
                startActivity(intent)
            } else {
                // Variáveis não inicializadas, exiba uma mensagem de erro em um diálogo
                val alertDialog = AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Erro")
                    .setMessage("Por favor, preencha os valores no diálogo de configuração.")
                    .setPositiveButton("OK", null)
                    .create()
                alertDialog.show()
            }
        }


        //actions when spinner changes
        spinnerAdapterInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // update results if possible
                if (isVariablesInitialized()) {
                    outputResult = calcOutput(zeroValueEntrada.toFloat(), cemValueEntrada.toFloat(), zeroValueSaida.toFloat(), cemValueSaida.toFloat())
                    inputResult = calcInput(zeroValueEntrada.toFloat(), cemValueEntrada.toFloat(), zeroValueSaida.toFloat(), cemValueSaida.toFloat())
                    addDataSet(outputResult, inputResult)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //actions when spinner changes
        spinnerAdapterOutput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // update results if possible
                if (isVariablesInitialized()) {
                    outputResult = calcOutput(zeroValueEntrada.toFloat(), cemValueEntrada.toFloat(), zeroValueSaida.toFloat(), cemValueSaida.toFloat())
                    inputResult = calcInput(zeroValueEntrada.toFloat(), cemValueEntrada.toFloat(), zeroValueSaida.toFloat(), cemValueSaida.toFloat())
                    addDataSet(outputResult, inputResult)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    //check if variables are initialized
    private fun isVariablesInitialized(): Boolean {
        return ::zeroValueEntrada.isInitialized &&
                ::cemValueEntrada.isInitialized &&
                ::zeroValueSaida.isInitialized &&
                ::cemValueSaida.isInitialized
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
                val cRaiz = (sqrt((sRaiz-zeroValueOutput)/rangeOut)*rangeOut)+zeroValueOutput
                resultMap[percentage] = String.format("%.2f",cRaiz).replace(",",".").toFloat()

            }else{
                resultMap[percentage] = String.format("%.2f",sRaiz).replace(",",".").toFloat()
            }
        }


        return resultMap
    }


    private fun addDataSet(outputCalcResult: Map<Int, Float>, inputCalcResult: Map<Int,Float>){
        val inputData = listOf(
            Item(0, outputCalcResult[0] ?: 0.0f),
            Item(25,outputCalcResult[25] ?: 0.0f),
            Item(50,outputCalcResult[50] ?: 0.0f),
            Item(75,outputCalcResult[75] ?: 0.0f),
            Item(100,outputCalcResult[100] ?: 0.0f)
        )

        itemAdapterInput.submitList(inputData)

        val outputData = listOf(
            Item(0,inputCalcResult[0] ?: 0.0f),
            Item(25,inputCalcResult[25] ?: 0.0f),
            Item(50,inputCalcResult[50] ?: 0.0f),
            Item(75,inputCalcResult[75] ?: 0.0f),
            Item(100,inputCalcResult[100] ?: 0.0f)
        )

        itemAdapterOutput.submitList(outputData)
        // Notifique os adaptadores de que os dados foram alterados
        itemAdapterInput.notifyDataSetChanged()
        itemAdapterOutput.notifyDataSetChanged()
    }

    private fun initRecyclerView(){
        recyclerViewInput.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAdapterInput = ItemAdapter()
            adapter = itemAdapterInput
        }

        recyclerViewOutput.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAdapterOutput = ItemAdapter()
            adapter = itemAdapterOutput
        }

    }

}