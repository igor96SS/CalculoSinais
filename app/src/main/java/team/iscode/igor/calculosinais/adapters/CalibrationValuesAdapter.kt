package team.iscode.igor.calculosinais.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import team.iscode.igor.calculosinais.R
import team.iscode.igor.calculosinais.models.CalibrationValues
import java.util.ArrayList
import kotlin.math.pow

class CalibrationValuesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var calibrationList: List<CalibrationValues> = ArrayList()
    private var zeroInput: Float = 0f
    private var cemInput: Float = 0f
    private var isLinear: Boolean? = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InputViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.calibration_values_view,parent,false))
    }

    override fun getItemCount(): Int {
        return calibrationList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is InputViewHolder -> holder.bind(
                calibrationList[position],
                zeroInput,
                cemInput,
                isLinear
            )
        }
    }

    fun setParams(zero: Float, cem: Float, linear: Boolean?) {
        zeroInput = zero
        cemInput = cem
        isLinear = linear
        notifyDataSetChanged() // ou apenas se necessário
    }

    fun setCalibrationList(newList: List<CalibrationValues>) {
        calibrationList = newList
        notifyDataSetChanged()
    }

    class InputViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val percentageET: EditText = itemView.findViewById(R.id.percentageValueET)
        private val signalTV: TextView = itemView.findViewById(R.id.signalValueTv)

        fun bind(item: CalibrationValues, zeroInput: Float, cemInput: Float, isLinear: Boolean?) {
            percentageET.setText(item.percentage.toString())
            signalTV.text = item.inputValues.toString()

            percentageET.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val newPercentage = s.toString().toFloat()
                    val range = cemInput - zeroInput

                    if (isLinear == true){
                        val newSignal = (range*newPercentage/100)+zeroInput
                        signalTV.text = String.format("%.2f", newSignal)
                    }
                    else{
                        val newSignal = (range*newPercentage/100).pow(2)+range+zeroInput
                        signalTV.text = String.format("%.2f", newSignal)
                    }
                }
            })
        }

    }


}