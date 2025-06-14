package com.quickaccess.app.data.model

data class SubService(
    val id: Int,
    val name: String,
    val icon: Int,
    val serviceId: Int,
    var isSelected: Boolean = false
)