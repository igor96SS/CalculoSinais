package team.iscode.igor.calculosinais.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import team.iscode.igor.calculosinais.R
import team.iscode.igor.calculosinais.adapters.ItemAdapter.InputViewHolder
import team.iscode.igor.calculosinais.models.CalibrationValues
import java.util.ArrayList

class CalibrationValuesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var calibrationList: List<CalibrationValues> = ArrayList()
    private var maxPercentError: Int = 0
    private var acceptanceLimit: Float = 0.0f

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InputViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.calibration_values_view,parent,false))
    }

    override fun getItemCount(): Int {
        return calibrationList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}