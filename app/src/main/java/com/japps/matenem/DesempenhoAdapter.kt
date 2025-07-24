package com.japps.matenem

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.japps.matenem.db.DataBase
import com.japps.matenem.entities.Desempenho

class DesempenhoAdapter(val list: List<Desempenho>, val context: Context): RecyclerView.Adapter<DesempenhoAdapter.DesempenhoViewHolder>() {

    private val dataBase: DataBase = DataBase(context)

    inner class DesempenhoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val data: TextView = itemView.findViewById(R.id.dataValue)
        val totalQuestoes: TextView = itemView.findViewById(R.id.questoesValue)
        val aproveitamento: TextView = itemView.findViewById(R.id.aproveitamentoValue)
        val deleteDesempenhoButton: ImageButton = itemView.findViewById(R.id.deleteDesempenhoButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesempenhoViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.desempenho_item, parent, false)
        return DesempenhoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DesempenhoViewHolder, position: Int) {
        holder.data.text = list[position].data
        holder.totalQuestoes.text = "Questões: "+list[position].quant_questoes.toString()
        holder.aproveitamento.setTextColor(textColorByAproveitamento(list[position].aproveitamento))
        holder.aproveitamento.text = String.format("%.2f%%", list[position].aproveitamento)
        holder.deleteDesempenhoButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Atenção")
                .setMessage("Deseja realmente excluir esse registro ?")
                .setPositiveButton("Sim"){ dialog, _ ->
                    list[position].iddesempenho?.let { it -> dataBase.deleteDesempenho(it) }
                }
                .setNegativeButton("Não"){ dialog, _->
                    dialog.dismiss()
                }
                .show()
        }
    }

    fun textColorByAproveitamento(aproveitamento: Float): Int{
        return if(aproveitamento <= 50) context.resources.getColor(R.color.red)
        else if(aproveitamento > 50 && aproveitamento < 70) context.resources.getColor(R.color.orange)
        else context.resources.getColor(R.color.green)
    }
}