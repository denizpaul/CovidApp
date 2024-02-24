package com.dennis.covidapp.ui.viewmodel

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dennis.covidapp.domain.usecases.CovidReportUseCase
import javax.inject.Inject

class CovidViewModelProviderFactory @Inject constructor(val app: Application, val covidReportUseCase: CovidReportUseCase, val res: Resources): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CovidViewModel(app, covidReportUseCase, res) as T
    }
}