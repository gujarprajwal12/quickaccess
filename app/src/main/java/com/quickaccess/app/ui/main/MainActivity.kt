package com.quickaccess.app.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.quickaccess.MyApplication
import com.quickaccess.app.R
import com.quickaccess.app.databinding.ActivityMainBinding
import com.quickaccess.app.ui.services.SelectedServicesAdapter
import com.quickaccess.app.ui.services.ServiceSelectionActivity
import com.quickaccess.app.viewmodel.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: SharedViewModel
    private lateinit var selectedServicesAdapter: SelectedServicesAdapter
    private lateinit var selectedServicesRecyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var emptyStateText: android.widget.TextView
    private lateinit var addServiceButton: com.google.android.material.button.MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        initViewModel()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun initViews() {
        selectedServicesRecyclerView = findViewById(R.id.selectedServicesRecyclerView)
        emptyStateText = findViewById(R.id.emptyStateText)
        addServiceButton = findViewById(R.id.addServiceButton)
    }

    private fun initViewModel() {
        viewModel = (application as MyApplication).sharedViewModel
    }

    private fun setupRecyclerView() {
        selectedServicesAdapter = SelectedServicesAdapter { subService ->
            viewModel.removeSelectedSubService(subService)
        }

        selectedServicesRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = selectedServicesAdapter
        }
    }

    private fun setupClickListeners() {
        addServiceButton.setOnClickListener {
            val intent = Intent(this, ServiceSelectionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.selectedSubServices.observe(this) { selectedServices ->
            selectedServicesAdapter.updateServices(selectedServices)
            updateEmptyState(selectedServices.isEmpty())
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            emptyStateText.visibility = View.VISIBLE
            selectedServicesRecyclerView.visibility = View.GONE
        } else {
            emptyStateText.visibility = View.GONE
            selectedServicesRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        // Force refresh when returning from service selection
        viewModel.selectedSubServices.value?.let { services ->
            selectedServicesAdapter.updateServices(services)
            updateEmptyState(services.isEmpty())
        }
    }

    override fun onRestart() {
        super.onRestart()
        // Additional refresh on restart
        viewModel.selectedSubServices.value?.let { services ->
            selectedServicesAdapter.updateServices(services)
            updateEmptyState(services.isEmpty())
        }
    }
}