package com.quickaccess.app.ui.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quickaccess.app.R
import com.quickaccess.app.data.model.SubService
import com.quickaccess.app.viewmodel.SharedViewModel

class SubServicesAdapter(
    private val viewModel: SharedViewModel,
    private val onSelectionChanged: () -> Unit
) : RecyclerView.Adapter<SubServicesAdapter.SubServiceViewHolder>() {

    private var subServices = mutableListOf<SubService>()

    fun updateSubServices(newSubServices: List<SubService>) {
        subServices.clear()
        subServices.addAll(newSubServices)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sub_service, parent, false)
        return SubServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubServiceViewHolder, position: Int) {
        val subService = subServices[position]
        holder.bind(subService)
    }

    override fun getItemCount(): Int = subServices.size

    inner class SubServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val subServiceIcon: ImageView = itemView.findViewById(R.id.subServiceIcon)
        private val subServiceName: TextView = itemView.findViewById(R.id.subServiceName)
        private val subServiceCheckbox: CheckBox = itemView.findViewById(R.id.subServiceCheckbox)

        fun bind(subService: SubService) {
            subServiceIcon.setImageResource(subService.icon)
            subServiceName.text = subService.name

            // Update checkbox state without triggering listener
            subServiceCheckbox.setOnCheckedChangeListener(null)
            subServiceCheckbox.isChecked = viewModel.isSubServiceSelected(subService)

            // Handle checkbox state changes
            subServiceCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!viewModel.canSelectMore() && !viewModel.isSubServiceSelected(subService)) {
                        // Revert checkbox state if max limit reached
                        subServiceCheckbox.isChecked = false
                        return@setOnCheckedChangeListener
                    }
                    viewModel.addSelectedSubService(subService)
                } else {
                    viewModel.removeSelectedSubService(subService)
                }
                onSelectionChanged()
            }

            // Handle item click
            itemView.setOnClickListener {
                if (subServiceCheckbox.isChecked) {
                    subServiceCheckbox.isChecked = false
                } else if (viewModel.canSelectMore() || viewModel.isSubServiceSelected(subService)) {
                    subServiceCheckbox.isChecked = true
                }
            }
        }
    }
}