package com.example.exchangeapp.presentation.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeapp.databinding.CurrencyItemDbBinding
import com.example.exchangeapp.db.CurrencyDto

class CurrencyDbAdapter(
    private val clickListener: (String, String) -> Unit
) : ListAdapter<CurrencyDto, CurrencyDbAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: CurrencyItemDbBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rate: CurrencyDto, clickListener: (String, String) -> Unit) = with(binding) {
            baseName.text = rate.baseName
            baseValue.text = rate.baseValue.toString()
            name.text = rate.name
            value.text = rate.value.toString()
            icon.setOnClickListener {
                clickListener(rate.name, rate.baseName)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    CurrencyItemDbBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<CurrencyDto>(){
        override fun areItemsTheSame(oldItem: CurrencyDto, newItem: CurrencyDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CurrencyDto, newItem: CurrencyDto): Boolean {
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