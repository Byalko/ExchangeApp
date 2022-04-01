package com.example.exchangeapp.presentation.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeapp.databinding.CurrencyItemBinding

class CurrencyAdapter(
    private val clickListener: (String, Double) -> Unit
) : ListAdapter<Currency, CurrencyAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: CurrencyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rate: Currency, clickListener: (String, Double) -> Unit) = with(binding) {
            title.text = rate.name
            value.text = rate.value.toString()
            icon.setOnClickListener {
                clickListener(rate.name, rate.value)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    CurrencyItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<Currency>(){
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.name == newItem.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}