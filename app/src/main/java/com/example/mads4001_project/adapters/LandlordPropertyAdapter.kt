package com.example.mads4001_project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mads4001_project.R
import com.example.mads4001_project.models.Property


class LandlordPropertyAdapter(
    private val propertyList:MutableList<Property>,
    private val rowClickHandler: (Int) -> Unit,
    private val deleteBtnClickHandler: (Int) -> Unit,
    private val editBtnClickHandler: (Int) -> Unit) : RecyclerView.Adapter<LandlordPropertyAdapter.LandlordPropertyViewHolder>() {

    inner class LandlordPropertyViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
        init {
            itemView.setOnClickListener {
                rowClickHandler(adapterPosition)
            }
            itemView.findViewById<Button>(R.id.btnEdit).setOnClickListener {
                editBtnClickHandler(adapterPosition)
            }
            itemView.findViewById<Button>(R.id.btnDelete).setOnClickListener {
                deleteBtnClickHandler(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandlordPropertyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_landlord_property, parent, false)
        return LandlordPropertyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    override fun onBindViewHolder(holder: LandlordPropertyViewHolder, position: Int) {

        val currProperty: Property = propertyList.get(position)

        val tvTitle = holder.itemView.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = currProperty.address

        val tvDetail = holder.itemView.findViewById<TextView>(R.id.tvDetail)
        tvDetail.text = currProperty.type

    }
}
