package com.quickaccess.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quickaccess.app.data.model.Service
import com.quickaccess.app.data.model.SubService


class SharedViewModel : ViewModel() {

    private val _selectedSubServices = MutableLiveData<MutableList<SubService>>(mutableListOf())
    val selectedSubServices: LiveData<MutableList<SubService>> = _selectedSubServices

    private val _allServices = MutableLiveData<List<Service>>()
    val allServices: LiveData<List<Service>> = _allServices

    companion object {
        const val MAX_SELECTED_SERVICES = 8
    }

    init {
        loadServices()
    }

    private fun loadServices() {
        val services = listOf(
            Service(
                id = 1,
                name = "Healthcare",
                icon = android.R.drawable.ic_menu_help,
                subServices = listOf(
                    SubService(1, "Doctor Consultation", android.R.drawable.ic_menu_call, 1),
                    SubService(2, "Lab Tests", android.R.drawable.ic_menu_search, 1),
                    SubService(3, "Medicine Delivery", android.R.drawable.ic_menu_send, 1),
                    SubService(4, "Emergency Services", android.R.drawable.ic_dialog_alert, 1)
                )
            ),
            Service(
                id = 2,
                name = "Home Services",
                icon = android.R.drawable.ic_menu_myplaces,
                subServices = listOf(
                    SubService(5, "Cleaning", android.R.drawable.ic_menu_delete, 2),
                    SubService(6, "Plumbing", android.R.drawable.ic_menu_edit, 2),
                    SubService(7, "Electrical", android.R.drawable.ic_menu_agenda, 2),
                    SubService(8, "Painting", android.R.drawable.ic_menu_gallery, 2),
                    SubService(9, "Carpentry", android.R.drawable.ic_menu_compass, 2)
                )
            ),
            Service(
                id = 3,
                name = "Transportation",
                icon = android.R.drawable.ic_menu_directions,
                subServices = listOf(
                    SubService(10, "Taxi Booking", android.R.drawable.ic_menu_mylocation, 3),
                    SubService(11, "Bus Tickets", android.R.drawable.ic_menu_mapmode, 3),
                    SubService(12, "Train Tickets", android.R.drawable.ic_menu_recent_history, 3),
                    SubService(13, "Flight Booking", android.R.drawable.ic_menu_upload, 3)
                )
            ),
            Service(
                id = 4,
                name = "Food & Dining",
                icon = android.R.drawable.ic_menu_agenda,
                subServices = listOf(
                    SubService(14, "Food Delivery", android.R.drawable.ic_menu_send, 4),
                    SubService(15, "Restaurant Booking", android.R.drawable.ic_menu_today, 4),
                    SubService(16, "Grocery Delivery", android.R.drawable.ic_menu_sort_by_size, 4)
                )
            ),
            Service(
                id = 5,
                name = "Education",
                icon = android.R.drawable.ic_menu_info_details,
                subServices = listOf(
                    SubService(17, "Online Courses", android.R.drawable.ic_menu_slideshow, 5),
                    SubService(18, "Tutoring", android.R.drawable.ic_menu_edit, 5),
                    SubService(19, "Help", android.R.drawable.ic_menu_help, 5),
                    SubService(20, "Skill Development", android.R.drawable.ic_menu_manage, 5)
                )
            )
        )
        _allServices.value = services
    }

    fun addSelectedSubService(subService: SubService): Boolean {
        val currentList = _selectedSubServices.value?.toMutableList() ?: mutableListOf()
        if (currentList.size >= MAX_SELECTED_SERVICES) {
            return false
        }

        if (!currentList.any { it.id == subService.id }) {
            currentList.add(subService.copy(isSelected = true))
            _selectedSubServices.value = currentList
            return true
        }
        return false
    }

    fun removeSelectedSubService(subService: SubService) {
        val currentList = _selectedSubServices.value?.toMutableList() ?: mutableListOf()
        currentList.removeAll { it.id == subService.id }
        _selectedSubServices.value = currentList
    }

    fun toggleSubServiceSelection(subService: SubService): Boolean {
        return if (isSubServiceSelected(subService)) {
            removeSelectedSubService(subService)
            false
        } else {
            addSelectedSubService(subService)
        }
    }

    fun isSubServiceSelected(subService: SubService): Boolean {
        return _selectedSubServices.value?.any { it.id == subService.id } ?: false
    }

    fun getSelectedCount(): Int {
        return _selectedSubServices.value?.size ?: 0
    }

    fun canSelectMore(): Boolean {
        return getSelectedCount() < MAX_SELECTED_SERVICES
    }

    fun refreshSelectedServices() {
        // Trigger LiveData update
        _selectedSubServices.value = _selectedSubServices.value
    }
}