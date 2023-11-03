package team.iscode.igor.calculosinais

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewInput = findViewById(R.id.recyclerViewEntrada)
        recyclerViewOutput = findViewById(R.id.recyclerViewSaida)
        spinnerAdapterInput = findViewById(R.id.spinnerEntrada)
        spinnerAdapterOutput = findViewById(R.id.spinnerSaida)
        buttonConfig = findViewById(R.id.floatBTN)


        buttonConfig.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setView(R.layout.layout_config_dialog)
                .setPositiveButton("Salvar") { dialog, which ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }



        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet(){
        val inputData = listOf(
            Item(0,0.1f),
            Item(25,-2323f),
            Item(50,0.3243f),
            Item(75,0f),
            Item(100,0f)
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