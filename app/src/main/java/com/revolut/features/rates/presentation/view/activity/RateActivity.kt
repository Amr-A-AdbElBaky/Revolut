package com.revolut.features.rates.presentation.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.revolut.R
import com.revolut.core.di.module.ViewModelFactory
import com.revolut.core.extension.action
import com.revolut.core.extension.getSnack
import com.revolut.features.rates.domain.entity.Rate
import com.revolut.features.rates.presentation.view.adapter.RatesAdapter
import com.revolut.features.rates.presentation.viewmodel.RatesViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_rates.*
import javax.inject.Inject

class RateActivity :DaggerAppCompatActivity() {


    private var snackBar :Snackbar?  = null
    private val mRatesAdapter by lazy { RatesAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
            .get(RatesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rates)

        initViews()
        viewModel.getCurrencies(Rate())
        initViewModelObservations()
    }

    private fun initViewModelObservations() {

        viewModel.ratesObservableResource.observe(this,
            doOnSuccess = {
                setSuccessLayout(it.rates)
            },
            doOnLoading = {
                svRates.setLoading()
            }, doOnError = {
                setErrorView()
            })


    }

    private fun setSuccessLayout(rates:MutableList<Rate>) {
        svRates.setContent()
        setSnackBarVisibility(false)
        if (mRatesAdapter.isEmpty()){
            rates.add(0 , viewModel.currentCurrency!!)
            mRatesAdapter.setItemsFirstTime(rates)
        }else{
            mRatesAdapter.replaceItems(rates)
        }


    }
    private fun setErrorView(){
        if (mRatesAdapter.isEmpty())
            svRates.setUnexpectedError { viewModel.getCurrencies(Rate())}
        else{
            svRates.setContent()
            setSnackBarVisibility(true)
        }
    }
    private fun setSnackBarVisibility(isVisible :Boolean){
        if (snackBar == null || ! snackBar?.isShown!!){
            if (isVisible){
            snackBar = rootView.getSnack(getString(R.string.screens_error_messages_unExpected))
            snackBar?.show()
                snackBar?.action(getString(R.string.label_retry) , R.color.colorRollingStone, listener = {
                    viewModel.getCurrencies(viewModel.currentCurrency!!)
                })
            }else{
                snackBar?.dismiss()
            }
        }
    }

    private fun initViews() {
        with(rvRates){
            layoutManager = LinearLayoutManager(this@RateActivity)
            adapter = mRatesAdapter
        }
        initAdapterListeners()
    }

    private fun initAdapterListeners() {
        mRatesAdapter.onCurrencyClick = {
            viewModel.getCurrencies(it)
        }
    }

    companion object{
        fun startMa(activity: Activity){
            activity.startActivity(Intent(activity , RateActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
        }
    }
}