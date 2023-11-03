package team.iscode.igor.calculosinais.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import team.iscode.igor.calculosinais.R
import team.iscode.igor.calculosinais.models.Item

class ItemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var inputList: List<Item> = ArrayList()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InputViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false))
    }

    override fun getItemCount(): Int {
        return inputList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is InputViewHolder ->{
                holder.bind(inputList.get(position))
            }
        }
    }

    fun submitList(signalList: List<Item>){
        inputList = signalList
    }

    class InputViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        val percentage: TextView = itemView.findViewById(R.id.percentageValue)
        val inputValue: TextView = itemView.findViewById(R.id.signalValue)
        val unitMeasure: TextView = itemView.findViewById(R.id.unitMeasurementValue)

        fun bind(signalValue: Item){
            percentage.setText(signalValue.percentage)
            inputValue.setText(signalValue.signalValue)
            unitMeasure.setText(signalValue.unitMeasurement)
        }
    }

}