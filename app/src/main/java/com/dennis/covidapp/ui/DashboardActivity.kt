package com.dennis.covidapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dennis.covidapp.data.api.CovidApi
import com.dennis.covidapp.data.repository.CovidRepository
import com.dennis.covidapp.databinding.ActivityDashboardBinding
import com.dennis.covidapp.domain.usecases.CovidReportUseCase
import com.dennis.covidapp.ui.viewmodel.CovidViewModel
import com.dennis.covidapp.ui.viewmodel.CovidViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    lateinit var covidViewModel: CovidViewModel

    @Inject
    lateinit var covidApi: CovidApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val covidRepository = CovidRepository(covidApi)
        val covidReportUseCase = CovidReportUseCase(covidRepository)
        val covidViewModelProviderFactory = CovidViewModelProviderFactory(application, covidReportUseCase, resources)
        covidViewModel = ViewModelProvider(this, covidViewModelProviderFactory).get(CovidViewModel::class.java)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}