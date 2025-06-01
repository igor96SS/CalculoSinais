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
private const val TEXT_WATCHER_TAG = 123456789  // valor qualquer, mas único
private var EditText.textWatcher: TextWatcher?
    get() = getTag(TEXT_WATCHER_TAG) as? TextWatcher
    set(value) = setTag(TEXT_WATCHER_TAG, value)

class VerificationValuesAdapter(private val inputValues: List<Float>) : RecyclerView.Adapter<VerificationValuesAdapter.InputViewHolder>() {

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

    fun setVerificationList(newList: List<VerificationValues>) {
        verificationValuesList.clear()
        verificationValuesList.addAll(newList)
        notifyDataSetChanged()
    }

    fun getList(): List<VerificationValues> = verificationValuesList

    inner class InputViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val outputValues: TextView = itemView.findViewById(R.id.outputValues)
        private val readValues: EditText = itemView.findViewById(R.id.readValues)
        private val error: TextView = itemView.findViewById(R.id.error)

        fun bind(item: VerificationValues) {
            outputValues.text = String.format(Locale.US, "%.2f", item.outputValues)

            // Remove existing TextWatcher if present
            readValues.textWatcher?.let { readValues.removeTextChangedListener(it) }

            // Create new TextWatcher
            val watcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val newValue = s?.toString()?.toFloatOrNull()
                    item.readValues = newValue

                    val inputValue = inputValues.getOrNull(adapterPosition) ?: 0f
                    item.error = if (newValue != null) {
                        inputValue - newValue
                    } else {
                        0f
                    }

                    error.text = if (newValue != null) {
                        String.format(Locale.US, "%.2f", item.error)
                    } else {
                        ""
                    }

                    onDataChanged?.invoke()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            readValues.textWatcher = watcher
            readValues.addTextChangedListener(watcher)

            // Always show the current error
            error.text = String.format(Locale.US, "%.2f", item.error)
        }
    }
}


