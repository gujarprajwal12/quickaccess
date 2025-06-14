package com.quickaccess.app.ui.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.quickaccess.app.R
import com.quickaccess.app.data.model.SubService

class SelectedServicesAdapter(
    private val onRemoveClick: (SubService) -> Unit
) : RecyclerView.Adapter<SelectedServicesAdapter.ViewHolder>() {

    private var selectedServices = mutableListOf<SubService>()

    fun updateServices(services: List<SubService>) {
        selectedServices.clear()
        selectedServices.addAll(services)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selected_service, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subService = selectedServices[position]

        holder.serviceIcon.setImageResource(subService.icon)
        holder.serviceName.text = subService.name

        holder.removeIcon.setOnClickListener {
            onRemoveClick(subService)
        }
    }

    override fun getItemCount(): Int = selectedServices.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceIcon: ImageView = itemView.findViewById(R.id.serviceIcon)
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        val removeIcon: ImageView = itemView.findViewById(R.id.removeIcon)
    }
}