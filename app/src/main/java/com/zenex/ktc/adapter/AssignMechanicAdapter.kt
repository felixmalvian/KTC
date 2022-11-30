package com.zenex.ktc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.zenex.ktc.R
import com.zenex.ktc.data.MechanicDetails
import com.zenex.ktc.fragment.AssignMechanicFragment

class AssignMechanicAdapter(
    private val ctx: Context,
    private val items: ArrayList<MechanicDetails>,
    private val fragment: Fragment,
): RecyclerView.Adapter<AssignMechanicAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvSpecialisation: TextView = itemView.findViewById(R.id.tvSpecialisation)
        val tvWorkCenter: TextView = itemView.findViewById(R.id.tvWorkCenter)
        val tvDateAssigned: TextView = itemView.findViewById(R.id.tvDateAssigned)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(ctx).inflate(R.layout.card_assigned_mechanic, parent, false)
        return ViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvName.text = item.name
        holder.tvSpecialisation.text = item.specialisation
        holder.tvWorkCenter.text = item.workCenter
        holder.tvDateAssigned.text = "Date Assigned: ${item.dateAssigned}"

        holder.btnDelete.setOnClickListener {
            deleteItem(position)
        }
    }

    private fun deleteItem(position: Int){
//        items.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, items.size)
        if (fragment is AssignMechanicFragment){
            fragment.listAssignedMechanic.removeAt(position)
            fragment.updateRv()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}