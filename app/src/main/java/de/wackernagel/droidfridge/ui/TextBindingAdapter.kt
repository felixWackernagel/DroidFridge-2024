package de.wackernagel.droidfridge.ui

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import de.wackernagel.droidfridge.R
import de.wackernagel.droidfridge.data.ShopWithOpeningHours
import de.wackernagel.droidfridge.data.isClosedSoon
import de.wackernagel.droidfridge.data.isOpen

@BindingAdapter("openingState")
fun TextView.setOpeningState( shopWithOpeningHours: ShopWithOpeningHours) {
    if( shopWithOpeningHours.openingHours.isNotEmpty() ) {
        visibility = View.VISIBLE
        if( shopWithOpeningHours.isClosedSoon() ) {
            text = context.getString( R.string.shop_last_hour )
            setTextColor( ContextCompat.getColor( context, R.color.shop_last_hour ) )
        } else if( shopWithOpeningHours.isOpen() ) {
            text = context.getString( R.string.shop_open )
            setTextColor( ContextCompat.getColor( context, R.color.shop_open ) )
        } else {
            text = context.getString( R.string.shop_closed )
            setTextColor( ContextCompat.getColor( context, R.color.shop_closed ) )
        }
    } else {
        visibility = View.GONE
    }
}