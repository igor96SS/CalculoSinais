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
import team.iscode.igor.calculosinais.models.VerificationValues
import java.util.Locale

// storing a reference to the textWatcher on the EditText
// prevent adding multiple TextWatchers when onBindViewHolder() is called multiple times


class VerificationValuesAdapter(initialInputValues: List<Float>) : RecyclerView.Adapter<VerificationValuesAdapter.InputViewHolder>() {

    private var inputValues = initialInputValues.toMutableList()

    private val verificationValuesList = mutableListOf<VerificationValues>()
    var onDataChanged: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InputViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.verification_values_view, parent, false)
        return InputViewHolder(view)
    }

    override fun getItemCount(): Int = verificationValuesList.size

    override fun onBindViewHolder(holder: InputViewHolder, position: Int) {
        holder.bind(verificationValuesList[position])
    }

    fun updateInputValueAt(position: Int, newInputValue: Float) {
        if (position in inputValues.indices) {
            // Cria uma nova lista com o novo valor atualizado
            inputValues = inputValues.toMutableList().also {
                it[position] = newInputValue
            }
            notifyItemChanged(position)
        }
    }

    fun getCurrentList(): List<VerificationValues> {
        return verificationValuesList.toList() // cópia imutável
    }


    fun setVerificationList(newList: List<VerificationValues>) {
        verificationValuesList.clear()
        verificationValuesList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateOutputValueAt(position: Int, newOutputValue: Float) {
        if (position in verificationValuesList.indices) {
            val currentItem = verificationValuesList[position]
            verificationValuesList[position] = currentItem.copy(outputValues = newOutputValue)
            notifyItemChanged(position)
        }
    }

    fun updateInputValues(newValues: List<Float>) {
        inputValues.clear()
        inputValues.addAll(newValues)
        recalculateErrors()
        notifyDataSetChanged()
    }



    private fun recalculateErrors() {
        verificationValuesList.forEachIndexed { index, item ->
            val inputValue = inputValues.getOrNull(index) ?: 0f
            item.error = item.readValues?.let { inputValue - it } ?: 0f
        }
        notifyDataSetChanged()
    }




    inner class InputViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val outputValues: TextView = itemView.findViewById(R.id.outputValues)
        private val readValues: EditText = itemView.findViewById(R.id.readValues)
        private val error: TextView = itemView.findViewById(R.id.error)

        private var currentWatcher: TextWatcher? = null

        fun bind(item: VerificationValues) {
            // Atualiza a UI no início
            updateUI(item)

            // Remove watcher anterior (caso exista)
            currentWatcher?.let { readValues.removeTextChangedListener(it) }

            // Cria novo watcher
            val watcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val newValue = s?.toString()?.toFloatOrNull()
                    item.readValues = newValue

                    // Atualiza UI com base no novo valor introduzido
                    updateUI(item)

                    onDataChanged?.invoke()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            currentWatcher = watcher
            readValues.addTextChangedListener(watcher)

            // Preenche o campo de leitura com o valor atual (se aplicável)
            readValues.setText(item.readValues?.toString() ?: "")
        }

        private fun updateUI(item: VerificationValues) {
            outputValues.text = String.format(Locale.US, "%.2f", item.outputValues)

            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val inputValue = inputValues.getOrNull(position) ?: 0f

            item.error = item.readValues?.let { inputValue - it } ?: 0f

            error.text = item.readValues?.let {
                String.format(Locale.US, "%.2f", item.error)
            } ?: ""
        }

    }

}


