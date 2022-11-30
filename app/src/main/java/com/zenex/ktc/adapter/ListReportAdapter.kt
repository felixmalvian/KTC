package com.zenex.ktc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.zenex.ktc.R
import com.zenex.ktc.fragment.FaultReportFragment
import com.zenex.ktc.fragment.FaultReportFragmentDirections

class ListReportAdapter(
    private val ctx: Context,
    private val items: ArrayList<String>,
    private val type: String,
    private val screen: String,
    private val fragment: Fragment
): RecyclerView.Adapter<ListReportAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cvHolder: MaterialCardView = itemView.findViewById(R.id.cvHolder)
        val tvFaultReportNo: TextView = itemView.findViewById(R.id.tvFaultReportNo)
        val tvSite: TextView = itemView.findViewById(R.id.tvSite)
        val tvAssetId: TextView = itemView.findViewById(R.id.tvAssetId)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        val btnDropdown: ImageButton = itemView.findViewById(R.id.btnDropdown)
        val btnFile: ImageButton = itemView.findViewById(R.id.btnFile)

        val tlDetails: TableLayout = itemView.findViewById(R.id.tlDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(ctx).inflate(R.layout.card_fault_report, parent, false)
        return ViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvFaultReportNo.text = "$screen ${position + 15}"
        var dropdown = true
        holder.btnDropdown.setOnClickListener{
            if (dropdown){
//                Toast.makeText(ctx, "Dropping down..", Toast.LENGTH_SHORT).show()
                holder.btnDropdown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                holder.tlDetails.visibility = VISIBLE
            } else {
//                Toast.makeText(ctx, "Dropping down..", Toast.LENGTH_SHORT).show()
                holder.btnDropdown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                holder.tlDetails.visibility = GONE
            }
            dropdown = !dropdown
        }

        if (fragment is FaultReportFragment){
            holder.btnFile.setOnClickListener{
                val direction = FaultReportFragmentDirections.actionFaultReportFragmentToAssignMechanicFragment("${position + 15}", item)
                fragment.findNavController().navigate(direction)
            }
        } else {
            holder.btnFile.visibility = GONE
        }


        when (type){
            "New" -> holder.cvHolder.setBackgroundResource(R.drawable.shape_background_cardview_yellow)
            "In Progress" -> holder.cvHolder.setBackgroundResource(R.drawable.shape_background_cardview_blue)
            "Completed" -> holder.cvHolder.setBackgroundResource(R.drawable.shape_background_cardview_green)
        }

        holder.tvAssetId.text = item
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