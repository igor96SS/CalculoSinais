package team.iscode.igor.calculosinais.adapters

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import team.iscode.igor.calculosinais.R
import team.iscode.igor.calculosinais.interfaces.OnCalibrationChangedListener
import team.iscode.igor.calculosinais.models.CalibrationValues
import java.util.ArrayList
import kotlin.math.pow

class CalibrationValuesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var calibrationList: MutableList<CalibrationValues> = mutableListOf()
    private var zeroInput: Float = 0f
    private var cemInput: Float = 0f
    private var isLinear: Boolean? = true
    private var listener: OnCalibrationChangedListener? = null

    // Lista dos valores de entrada calculados (atualizada dinamicamente)
    private val inputValues: MutableList<Float> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InputViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.calibration_values_view, parent, false)
        )
    }

    override fun getItemCount(): Int = calibrationList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is InputViewHolder) {
            holder.bind(
                calibrationList[position],
                zeroInput,
                cemInput,
                isLinear,
                position,
                ::updateInputValueAt,
                listener
            )
        }
    }

    fun setOnCalibrationChangedListener(listener: OnCalibrationChangedListener) {
        this.listener = listener
    }

    fun setParams(zero: Float, cem: Float, linear: Boolean?) {
        zeroInput = zero
        cemInput = cem
        isLinear = linear
        notifyDataSetChanged()
    }

    fun setCalibrationList(newList: List<CalibrationValues>) {
        calibrationList = newList.toMutableList()

        // <-- INICIALIZA OS VALORES DE ENTRADA AQUI
        inputValues.clear()
        inputValues.addAll(newList.map { it.inputValues })

        notifyDataSetChanged()
    }



    fun getInputValues(): List<Float> {
        return inputValues
    }

    private fun updateInputValueAt(position: Int, value: Float) {
        if (position in inputValues.indices) {
            inputValues[position] = value
        }
    }

    fun getPercentages(): List<Float> {
        return calibrationList.map { it.percentage.toFloat() }
    }


    class InputViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val percentageET: EditText = itemView.findViewById(R.id.percentageValueET)
        private val signalTV: TextView = itemView.findViewById(R.id.signalValueTv)

        fun bind(
            item: CalibrationValues,
            zeroInput: Float,
            cemInput: Float,
            isLinear: Boolean?,
            position: Int,
            onInputUpdated: (Int, Float) -> Unit,
            listener: OnCalibrationChangedListener?
        ) {
            percentageET.setText(item.percentage.toString())
            signalTV.text = item.inputValues.toString()

            percentageET.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val newPercentage = s?.toString()?.toFloatOrNull()

                    newPercentage?.let {
                        item.percentage = it.toInt() // <- Atualiza a lista
                        val range = cemInput - zeroInput
                        val newSignal = if (isLinear == true) {
                            (range * it / 100) + zeroInput
                        } else {
                            (range * it / 100).pow(2) / range + zeroInput
                        }

                        signalTV.text = String.format("%.2f", newSignal)

                        onInputUpdated(position, newSignal)
                        listener?.onOutputValueChanged(adapterPosition, it, newSignal)

                    } ?: run {
                        signalTV.text = ""
                        onInputUpdated(position, 0f)
                    }
                }

            })
        }
    }
}
