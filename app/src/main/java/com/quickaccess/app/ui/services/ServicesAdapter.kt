package com.quickaccess.app.ui.services


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quickaccess.app.R
import com.quickaccess.app.data.model.Service
import com.quickaccess.app.viewmodel.SharedViewModel

class ServicesAdapter(
    private val viewModel: SharedViewModel,
    private val onSubServiceSelectionChanged: () -> Unit
) : RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    private var services = mutableListOf<Service>()

    fun updateServices(newServices: List<Service>) {
        services.clear()
        services.addAll(newServices)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.bind(service)
    }

    override fun getItemCount(): Int = services.size

    inner class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serviceIcon: ImageView = itemView.findViewById(R.id.serviceIcon)
        private val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        private val expandIcon: ImageView = itemView.findViewById(R.id.expandIcon)
        private val subServicesRecyclerView: RecyclerView = itemView.findViewById(R.id.subServicesRecyclerView)
        private val serviceHeader: View = itemView.findViewById(R.id.serviceHeader)

        private lateinit var subServicesAdapter: SubServicesAdapter

        fun bind(service: Service) {
            serviceIcon.setImageResource(service.icon)
            serviceName.text = service.name

            // Setup sub-services RecyclerView
            subServicesAdapter = SubServicesAdapter(viewModel) {
                onSubServiceSelectionChanged()
            }
            subServicesRecyclerView.apply {
                layoutManager = LinearLayoutManager(itemView.context)
                adapter = subServicesAdapter
            }
            subServicesAdapter.updateSubServices(service.subServices)

            // Update UI based on expansion state
            updateExpansionState(service.isExpanded)

            // Handle service header click
            serviceHeader.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    services[position].isExpanded = !services[position].isExpanded
                    updateExpansionState(services[position].isExpanded)
                }
            }
        }

        private fun updateExpansionState(isExpanded: Boolean) {
            if (isExpanded) {
                subServicesRecyclerView.visibility = View.VISIBLE
                expandIcon.rotation = 180f
            } else {
                subServicesRecyclerView.visibility = View.GONE
                expandIcon.rotation = 0f
            }
        }
    }
}