package com.example.exchangeapp.presentation.popular

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.exchangeapp.R
import com.example.exchangeapp.databinding.FragmentPopularBinding
import kotlinx.coroutines.flow.collect

class PopularFragment : Fragment(R.layout.fragment_popular) {

    private val viewModel: PopularViewModel by activityViewModels()
    private var _binding: FragmentPopularBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CurrencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)
        adapter = CurrencyAdapter { name, value ->
            Toast.makeText(requireContext(), getString(R.string.save_db, name), Toast.LENGTH_SHORT).show()
            viewModel.saveCurrency(name,value)
        }
        binding.recycler.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.data.collect {
                render(it)
            }
        }
        binding.spFromCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.getExchangeData(p0?.selectedItem.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        binding.icSort.setOnClickListener {
            SortDialogFragment{
                viewModel.sortList(it)
            }.show(
                childFragmentManager, SortDialogFragment.TAG)
        }
    }

    private fun render(it: ExchangeEvent) {
        when (it) {
            is ExchangeEvent.Success -> {
                binding.progressBar.isVisible = false
                adapter.submitList(it.result.currencies)
            }
            is ExchangeEvent.Failure -> {
                binding.progressBar.isVisible = false
                showToast(requireContext(), it.errorText)
            }
            is ExchangeEvent.Loading -> binding.progressBar.isVisible = true
            is ExchangeEvent.Empty -> { }
        }
    }

    private fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

