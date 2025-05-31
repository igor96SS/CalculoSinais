package team.iscode.igor.calculosinais.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import team.iscode.igor.calculosinais.R
import team.iscode.igor.calculosinais.models.VerificationValues
import java.util.ArrayList

class VerificationValuesAdapter  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var verificationValuesList: List<VerificationValues> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InputViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.verification_values_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return verificationValuesList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is InputViewHolder -> holder.bind(verificationValuesList[position])
        }
    }

    fun setVerificationList(newList: List<VerificationValues> ){
        verificationValuesList = newList
        notifyDataSetChanged()
    }

    class InputViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val outputValues: TextView = itemView.findViewById(R.id.outputValues)
        private val readValues: EditText = itemView.findViewById(R.id.readValues)
        private val error: TextView = itemView.findViewById(R.id.error)

        fun bind(item: VerificationValues) {
            outputValues.text = (item.outputValues.toString())
            readValues.setText(item.readValues.toString())
            error.text = item.error.toString()
        }
    }
}