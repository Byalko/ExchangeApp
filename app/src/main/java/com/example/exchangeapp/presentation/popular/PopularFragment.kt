package com.example.exchangeapp.presentation.popular

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.exchangeapp.R
import kotlinx.coroutines.flow.collect

class PopularFragment : Fragment(R.layout.fragment_popular) {

    private val viewModel: PopularViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getExchangeData()
        lifecycleScope.launchWhenStarted {
            viewModel.data.collect {
                when (it){
                    is ExchangeEvent.Success -> showToast(requireContext(), it.result.base_code)
                    is ExchangeEvent.Failure -> showToast(requireContext(), it.errorText)
                }
            }
        }
    }

    private fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}