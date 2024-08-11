package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.Shop
import com.example.myapplication.databinding.ShopListItemBinding

class ShopsListAdapter( private val clickListener: (shopId: Long) -> Unit ):
    ListAdapter<Shop, ShopsListAdapter.ViewHolder>(ShopDiffItemCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem( position )
        holder.bind( item, clickListener )
    }

    class ViewHolder( val binding: ShopListItemBinding): RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShopListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Shop, clickListener: (vaccinationTypeId: Long) -> Unit) {
            binding.shop = item
            binding.root.setOnClickListener {
                clickListener(item.id)
            }
        }
    }

    class ShopDiffItemCallback : DiffUtil.ItemCallback<Shop>() {
        override fun areItemsTheSame(oldItem: Shop, newItem: Shop) = (oldItem.id == newItem.id)
        override fun areContentsTheSame(oldItem: Shop, newItem: Shop) = (oldItem == newItem)
    }
}