package de.wackernagel.droidfridge.ui

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.data.Shop
import de.wackernagel.droidfridge.data.ShopWithOpeningHours
import de.wackernagel.droidfridge.data.isClosedSoon
import de.wackernagel.droidfridge.data.isOpen

@BindingAdapter("openingState")
fun TextView.setOpeningState( shopWithOpeningHours: ShopWithOpeningHours) {
    if( shopWithOpeningHours.openingHours.isNotEmpty() ) {
        visibility = View.VISIBLE
        if( shopWithOpeningHours.isClosedSoon() ) {
            text = context.getString( R.string.shop_list_item_state_last_hour )
            setTextColor( ContextCompat.getColor( context, R.color.shop_last_hour ) )
        } else if( shopWithOpeningHours.isOpen() ) {
            text = context.getString( R.string.shop_list_item_state_open )
            setTextColor( ContextCompat.getColor( context, R.color.shop_open ) )
        } else {
            text = context.getString( R.string.shop_list_item_state_closed )
            setTextColor( ContextCompat.getColor( context, R.color.shop_closed ) )
        }
    } else {
        visibility = View.GONE
    }
}

@BindingAdapter("goneUnless")
fun goneUnless( view: View, value: String? ) {
    view.visibility = if (!value.isNullOrBlank()) View.VISIBLE else View.GONE
}

@BindingAdapter("shopAddress")
fun TextView.setShopAddress( shop: Shop? ) {
    if( shop == null ) {
        visibility = View.GONE
    } else {
        val line1 = StringBuilder()
        if( !TextUtils.isEmpty( shop.street ) ) line1.append( shop.street )
        if( !TextUtils.isEmpty( shop.streetNumber ) ) {
            if( line1.isNotEmpty() ) line1.append( " " )
            line1.append( shop.streetNumber )
        }
        val line2 = StringBuilder()
        if( !TextUtils.isEmpty( shop.postalCode ) ) line2.append( shop.postalCode )
        if( !TextUtils.isEmpty( shop.city ) ) {
            if( line2.isNotEmpty() ) line2.append( " " )
            line2.append( shop.city )
        }
        val address = StringBuilder()
        if( line1.isNotEmpty() ) address.append( line1.toString() )
        if( line1.isNotEmpty() && line2.isNotEmpty() ) address.append( "\n" )
        if( line2.isNotEmpty() ) address.append( line2.toString() )
        if( address.isNotEmpty() && !TextUtils.isEmpty( shop.country ) ) address.append( "\n" )
        if( !TextUtils.isEmpty( shop.country ) ) address.append( shop.country )

        text = address.toString()
        visibility = if( address.isNotEmpty() ) View.VISIBLE else View.GONE
    }
}