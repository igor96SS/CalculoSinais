package team.iscode.igor.calculosinais

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import team.iscode.igor.calculosinais.adapters.ItemAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewInput: RecyclerView

    private lateinit var itemAdapter: ItemAdapter
    private lateinit var spinnerAdapterInput: Spinner
    private lateinit var spinnerAdapterOutput: Spinner



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewInput = findViewById(R.id.recyclerViewEntrada)
        spinnerAdapterInput = findViewById(R.id.spinnerEntrada)
        spinnerAdapterOutput = findViewById(R.id.spinnerSaida)


        initRecyclerView()
        addDataSet()
    }

    private fun addDataSet(){
        //val data =
    }

    private fun initRecyclerView(){
        recyclerViewInput.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAdapter = ItemAdapter()
            adapter = itemAdapter
        }

    }
}