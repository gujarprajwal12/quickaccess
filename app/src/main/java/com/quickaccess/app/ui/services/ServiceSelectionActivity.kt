package com.quickaccess.app.ui.services

import android.os.Bundle
import android.widget.TextView

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.quickaccess.app.R
import com.quickaccess.app.viewmodel.SharedViewModel
import androidx.appcompat.widget.Toolbar




class ServiceSelectionActivity : AppCompatActivity() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var servicesAdapter: ServicesAdapter

    private lateinit var toolbar: Toolbar
    private lateinit var selectedCountText: TextView
    private lateinit var servicesRecyclerView: RecyclerView
    private lateinit var doneButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_service_selection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        initViews()
        initViewModel()
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        selectedCountText = findViewById(R.id.selectedCountText)
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView)
        doneButton = findViewById(R.id.doneButton)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        servicesAdapter = ServicesAdapter(viewModel) {
            updateSelectedCount()
        }

        servicesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ServiceSelectionActivity)
            adapter = servicesAdapter
        }
    }

    private fun setupClickListeners() {
        doneButton.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.allServices.observe(this) { services ->
            servicesAdapter.updateServices(services)
        }

        viewModel.selectedSubServices.observe(this) { selectedServices ->
            updateSelectedCount()
        }
    }

    private fun updateSelectedCount() {
        val selectedCount = viewModel.getSelectedCount()
        val maxCount = SharedViewModel.MAX_SELECTED_SERVICES
        selectedCountText.text = "Selected: $selectedCount/$maxCount"

        // Update done button state
        doneButton.isEnabled = selectedCount > 0
        doneButton.alpha = if (selectedCount > 0) 1.0f else 0.5f
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}