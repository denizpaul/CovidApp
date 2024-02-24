package com.dennis.covidapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dennis.covidapp.R
import com.dennis.covidapp.databinding.FragmentDashboardBinding
import com.dennis.covidapp.ui.viewmodel.CovidViewModel
import com.dennis.covidapp.ui.DashboardActivity
import com.dennis.covidapp.ui.viewmodel.CovidViewModelProviderFactory
import com.dennis.covidapp.util.ScreenState
import com.google.android.material.snackbar.Snackbar
import com.hbb20.countrypicker.dialog.launchCountryPickerDialog
import com.hbb20.countrypicker.models.CPCountry
import dagger.hilt.android.AndroidEntryPoint
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var countryIsoList: Array<String?>
    lateinit var binding: FragmentDashboardBinding

    @Inject
    lateinit var viewModelFactory: CovidViewModelProviderFactory
    private val covidViewModel: CovidViewModel by viewModels() {viewModelFactory}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)

        //Worldwide covid data observer
        observeGlobalCovidData()

        //Region(country) list observer
        observeRegionListData()

        //Region specific data observer
        observeRegionSpecificCovidData()

        //Fetches all data from API
        covidViewModel.getGlobalReport()
        covidViewModel.getRegions()
        covidViewModel.getRegionSpecificReport("AUS")

        binding.refreshButton.setOnClickListener {
            covidViewModel.getGlobalReport()
            covidViewModel.getRegions()
            binding.cLayoutBottomCard.loadSkeleton()
            binding.cLayoutTopCard.loadSkeleton()
        }

        binding.countryPicker.setOnClickListener {
            context?.launchCountryPickerDialog(
                customMasterCountries = countryIsoList.joinToString()
            ) { selectedCountry: CPCountry? ->
                if (selectedCountry != null) {
                    binding.countryPicker.text = selectedCountry.name
                    covidViewModel.getRegionSpecificReport(selectedCountry.alpha3)
                }
            }
        }

    }

    private fun observeGlobalCovidData() {
        covidViewModel.globalReportLiveData.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                is ScreenState.Success<*> -> {
                    screenState.data?.let {
                        with(binding) {
                            val covidData = it.covidData ?: return@with // Safety check for nullability
                            topLeftValueTextView.text = covidData.confirmed.toString()
                            topRightValueTextView.text = covidData.deaths.toString()
                            leftBottomValueTextView.text = covidViewModel.formatFatalityRatePercentage(covidData.fatalityRate ?: 0.0)
                            dateTextView.text = resources.getString(R.string.last_updated_on) + covidData.lastUpdate
                            cLayoutTopCard.hideSkeleton()
                        }
                    }
                }
                is ScreenState.Loading<*> ->
                    binding.cLayoutTopCard.loadSkeleton()
                is ScreenState.Error<*> -> {
                    showErrorMessage(screenState.errorMessage)
                }
                else -> {}
            }

        })
    }

    private fun observeRegionSpecificCovidData() {
        covidViewModel.regionReportLiveData.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                is ScreenState.Success<*> -> {
                    screenState.data?.let {
                        val covidData = it.covidData ?: return@let
                        with(binding) {
                            topLeftValueTextViewCountry.text = covidData.confirmed.toString()
                            topRightValueTextViewCountry.text = covidData.deaths.toString()
                            leftBottomValueTextViewCountry.text = covidViewModel.formatFatalityRatePercentage(covidData.fatalityRate ?: 0.0)
                        }
                        hideCountryDataLoading()
                    }
                }
                is ScreenState.Loading<*> ->
                    showCountryDataLoading()
                is ScreenState.Error<*> -> {
                    hideCountryDataLoading()
                    showRegionEmptyFields()
                    showErrorMessage(screenState.errorMessage)
                }
                else -> {}
            }
        })
    }

    private fun observeRegionListData() {
        covidViewModel.regionsLiveData.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                is ScreenState.Success<*> -> {
                    screenState.data?.let {
                        countryIsoList = it.data.map { it.iso }.toTypedArray()
                        binding.cLayoutBottomCard.hideSkeleton()
                    }
                }
                is ScreenState.Loading<*> ->
                    binding.cLayoutBottomCard.loadSkeleton()
                is ScreenState.Error<*> -> {
                    showErrorMessage(screenState.errorMessage)
                }
                else -> {}
            }
        })
    }

    private fun showCountryDataLoading() {
        with(binding) {
            topLeftValueTextViewCountry.loadSkeleton()
            topRightValueTextViewCountry.loadSkeleton()
            leftBottomValueTextViewCountry.loadSkeleton()
        }
    }

    private fun hideCountryDataLoading() {
        with(binding) {
            topLeftValueTextViewCountry.hideSkeleton()
            topRightValueTextViewCountry.hideSkeleton()
            leftBottomValueTextViewCountry.hideSkeleton()
        }
    }

    private fun showRegionEmptyFields() {
        with(binding) {
            topLeftValueTextViewCountry.text = "--"
            topRightValueTextViewCountry.text = "--"
            leftBottomValueTextViewCountry.text = "--"
        }
    }

    private fun showErrorMessage(message: String?){
        Snackbar.make(binding.rootView, message ?: "", Snackbar.LENGTH_SHORT).show()
    }
}