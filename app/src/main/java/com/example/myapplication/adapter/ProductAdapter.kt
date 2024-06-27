package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Product
import com.example.myapplication.databinding.ProductListItemBinding

class ProductAdapter( val clickListener: (vaccinationTypeId: Long) -> Unit ):
    ListAdapter<Product, ProductAdapter.ViewHolder>(ProductDiffItemCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem( position )
        holder.bind( item, clickListener )
    }

    class ViewHolder( val binding: ProductListItemBinding): RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Product, clickListener: (vaccinationTypeId: Long) -> Unit) {
            binding.product = item
            binding.root.setOnClickListener {
                clickListener(item.id)
            }
        }
    }

    class ProductDiffItemCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) = (oldItem.id == newItem.id)
        override fun areContentsTheSame(oldItem: Product, newItem: Product) = (oldItem == newItem)
    }
}