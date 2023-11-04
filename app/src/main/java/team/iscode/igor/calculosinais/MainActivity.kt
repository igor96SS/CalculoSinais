package team.iscode.igor.calculosinais

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import team.iscode.igor.calculosinais.adapters.ItemAdapter
import team.iscode.igor.calculosinais.models.Item

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewInput: RecyclerView
    private lateinit var recyclerViewOutput: RecyclerView

    private lateinit var itemAdapterInput: ItemAdapter
    private lateinit var itemAdapterOutput: ItemAdapter
    private lateinit var spinnerAdapterInput: Spinner
    private lateinit var spinnerAdapterOutput: Spinner
    private lateinit var buttonConfig: FloatingActionButton

    private lateinit var unidadeMedidaEntrada: String
    private lateinit var zeroValueEntrada: String
    private lateinit var cemValueEntrada: String

    private lateinit var unidadeMedidaSaida: String
    private lateinit var zeroValueSaida: String
    private lateinit var cemValueSaida: String
    private var outputResult: Map<Int,Float> = emptyMap()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewInput = findViewById(R.id.recyclerViewEntrada)
        recyclerViewOutput = findViewById(R.id.recyclerViewSaida)
        spinnerAdapterInput = findViewById(R.id.spinnerEntrada)
        spinnerAdapterOutput = findViewById(R.id.spinnerSaida)
        buttonConfig = findViewById(R.id.floatBTN)


        buttonConfig.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.layout_config_dialog, null)
            val alertDialog = AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setView(dialogView)
                .setPositiveButton("Salvar") { dialog, which ->

                    //VALORES ENTRADA

                    val unidadeMedidaEntradaEditText = dialogView.findViewById<EditText>(R.id.unidadeMedidaEntrada)
                    val zeroValueEntradaEditText = dialogView.findViewById<EditText>(R.id.zeroValueEntrada)
                    val cemValueEntradaEditText = dialogView.findViewById<EditText>(R.id.cemValueEntrada)

                    // Agora você pode acessar os valores inseridos nos campos de entrada
                    unidadeMedidaEntrada = unidadeMedidaEntradaEditText.text.toString()
                    zeroValueEntrada = zeroValueEntradaEditText.text.toString()
                    cemValueEntrada = cemValueEntradaEditText.text.toString()

                    // VALORES SAIDA

                    val unidadeMedidaSaidaEditText = dialogView.findViewById<EditText>(R.id.unidadeMedidaSaida)
                    val zeroValueSaidaEditText = dialogView.findViewById<EditText>(R.id.zeroValueSaida)
                    val cemValueSaidaEditText = dialogView.findViewById<EditText>(R.id.cemValueSaida)

                    // Agora você pode acessar os valores inseridos nos campos de entrada
                    unidadeMedidaSaida = unidadeMedidaSaidaEditText.text.toString()
                    zeroValueSaida = zeroValueSaidaEditText.text.toString()
                    cemValueSaida = cemValueSaidaEditText.text.toString()

                    outputResult = calcOutput(zeroValueEntrada.toFloat(),cemValueEntrada.toFloat(), zeroValueSaida.toFloat(), cemValueSaida.toFloat())
                    //calcInput(unidadeMedidaSaida, zeroValueSaida, cemValueSaida )
                    addDataSet(outputResult)


                }
                .create()

            alertDialog.show()
        }


        initRecyclerView()
        addDataSet(outputResult)

    }

    private fun calcOutput(zeroValueInput: Float, cemValueInput: Float, zeroValueOutput: Float, cemValueOutput: Float): Map<Int,Float>{

        val spinnerValue = spinnerAdapterInput.selectedItem.toString()
        val resultMap = mutableMapOf<Int,Float>()

        val rangeOut = cemValueOutput-zeroValueOutput
        val rangeIn = cemValueInput-zeroValueInput


        if (spinnerValue == "Linear"){

            for(percentage in listOf(0,25,50,75,100)){
                val valorOut = rangeOut*(percentage.toFloat()/100)
                val sRaiz = (((valorOut - zeroValueOutput)*rangeIn)/rangeOut)+zeroValueInput
                resultMap[percentage] = String.format("%.2f",sRaiz).toFloat()
            }

        }

        return resultMap

    }

    /*
    private fun calcInput(unidadeMedida: String, zeroValue: String, cemValue: String): Map<Int,Float>{

    }

     */

    private fun addDataSet(outputCalcResult: Map<Int, Float>){
        val inputData = listOf(
            Item(0, outputCalcResult[0] ?: 0.0f),
            Item(25,outputCalcResult[25] ?: 0.0f),
            Item(50,outputCalcResult[50] ?: 0.0f),
            Item(75,outputCalcResult[75] ?: 0.0f),
            Item(100,outputCalcResult[100] ?: 0.0f)
        )

        itemAdapterInput.submitList(inputData)

        val outputData = listOf(
            Item(0,1.1f),
            Item(25,-13f),
            Item(50,1.43f),
            Item(75,1f),
            Item(100,15f)
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