package com.example.exchangeapp.presentation.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.exchangeapp.R
import com.example.exchangeapp.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.flow.collect

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val viewModel: FavoriteViewModel by activityViewModels()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CurrencyDbAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        adapter = CurrencyDbAdapter { name, baseName ->
            viewModel.deleteCurrency(name, baseName)
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

    }

    private fun render(it: ExchangeEvent) {
        when (it) {
            is ExchangeEvent.Success -> {
                adapter.submitList(it.result)
                binding.progressBar.visibility = View.GONE
                binding.txtEmpty.visibility = View.GONE
            }
            is ExchangeEvent.Loading -> binding.progressBar.visibility = View.VISIBLE
            is ExchangeEvent.Empty -> {
                adapter.submitList(emptyList())
                binding.progressBar.visibility = View.GONE
                binding.txtEmpty.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}