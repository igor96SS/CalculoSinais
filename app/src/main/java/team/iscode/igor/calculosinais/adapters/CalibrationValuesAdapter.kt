package team.iscode.igor.calculosinais.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
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
        when (holder) {
            is InputViewHolder -> holder.bind(calibrationList[position])
        }
    }

    fun setCalibrationList(newList: List<CalibrationValues>) {
        calibrationList = newList
        notifyDataSetChanged()
    }

    class InputViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val percentageET: EditText = itemView.findViewById(R.id.percentageValueET)
        private val signalTV: TextView = itemView.findViewById(R.id.signalValueTv)
        private val maxErrorEt: EditText = itemView.findViewById(R.id.maxErrorPercentEt)
        private val acceptanceLimitTv: TextView = itemView.findViewById(R.id.acceptanceLimitTv)

        fun bind(item: CalibrationValues) {
            percentageET.setText(item.percentage.toString())
            signalTV.text = item.inputValues.toString()

            maxErrorEt.setText("")
            acceptanceLimitTv.text = 0.toString()/* valor calculado */
        }
    }


}