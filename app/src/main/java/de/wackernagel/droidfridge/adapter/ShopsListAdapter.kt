package de.wackernagel.droidfridge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.data.ShopWithOpeningHours
import de.wackernagel.droidfridge.data.isClosedSoon
import de.wackernagel.droidfridge.data.isOpen
import de.wackernagel.droidfridge.databinding.ShopListItemBinding
import coil.load

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
            binding.root.setOnClickListener {
                clickListener(item.shop.id)
            }
            binding.shopName.text = item.shop.name
            binding.shopAddress.text = item.shop.street

            binding.shopAddress.visibility =
                if (item.shop.street.isNullOrBlank()) View.GONE else View.VISIBLE

            binding.shopOpeningHours.apply {
                if( item.openingHours.isNotEmpty() ) {
                    visibility = View.VISIBLE
                    if( item.isClosedSoon() ) {
                        text = context.getString( R.string.shop_list_item_state_last_hour )
                        setTextColor( ContextCompat.getColor(context, R.color.shop_last_hour ) )
                    } else if( item.isOpen() ) {
                        text = context.getString( R.string.shop_list_item_state_open )
                        setTextColor( ContextCompat.getColor(context, R.color.shop_open ) )
                    } else {
                        text = context.getString( R.string.shop_list_item_state_closed )
                        setTextColor( ContextCompat.getColor(context, R.color.shop_closed ) )
                    }
                } else {
                    visibility = View.GONE
                }
            }

            binding.image.load(item.shop.imagePath) {
                crossfade(true)
                placeholder(R.drawable.shop_list_item_default)
                error(R.drawable.shop_list_item_default)
            }
        }
    }

    class ShopDiffItemCallback : DiffUtil.ItemCallback<ShopWithOpeningHours>() {
        override fun areItemsTheSame(oldItem: ShopWithOpeningHours, newItem: ShopWithOpeningHours) = (oldItem.shop.id == newItem.shop.id)
        override fun areContentsTheSame(oldItem: ShopWithOpeningHours, newItem: ShopWithOpeningHours) = (oldItem == newItem)
    }
}