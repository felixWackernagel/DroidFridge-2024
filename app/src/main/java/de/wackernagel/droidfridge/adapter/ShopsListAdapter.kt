package de.wackernagel.droidfridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.wackernagel.droidfridge.data.ShopWithOpeningHours
import de.wackernagel.droidfridge.databinding.ShopListItemBinding

class ShopsListAdapter( private val clickListener: (shopId: Long) -> Unit ):
    ListAdapter<ShopWithOpeningHours, ShopsListAdapter.ViewHolder>(ShopDiffItemCallback()){

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

        fun bind(item: ShopWithOpeningHours, clickListener: (vaccinationTypeId: Long) -> Unit) {
            binding.shopWithOpeningHours = item
            binding.root.setOnClickListener {
                clickListener(item.shop.id)
            }
        }
    }

    class ShopDiffItemCallback : DiffUtil.ItemCallback<ShopWithOpeningHours>() {
        override fun areItemsTheSame(oldItem: ShopWithOpeningHours, newItem: ShopWithOpeningHours) = (oldItem.shop.id == newItem.shop.id)
        override fun areContentsTheSame(oldItem: ShopWithOpeningHours, newItem: ShopWithOpeningHours) = (oldItem == newItem)
    }
}