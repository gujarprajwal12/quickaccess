package com.quickaccess.app.data.model

data class Service(
 val id: Int,
 val name: String,
 val icon: Int,
 val subServices: List<SubService>,
 var isExpanded: Boolean = false
)