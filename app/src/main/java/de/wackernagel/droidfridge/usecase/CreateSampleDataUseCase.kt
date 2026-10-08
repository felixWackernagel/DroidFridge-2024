package de.wackernagel.droidfridge.usecase

import android.util.Log
import de.wackernagel.droidfridge.dao.OpeningHoursDao
import de.wackernagel.droidfridge.dao.ShopDao
import de.wackernagel.droidfridge.data.OpeningHours
import de.wackernagel.droidfridge.data.Shop
import javax.inject.Inject

class CreateSampleDataUseCase @Inject constructor(
    private val shopDao: ShopDao,
    private val openingHoursDao: OpeningHoursDao
) {
    suspend operator fun invoke() {
        Log.e(TAG,"Create Sample-Data")

        val frida = Shop()
        frida.name = "Frida"
        frida.street = "Lahmannring"
        frida.streetNumber = "19"
        frida.postalCode = "01324"
        frida.city = "Dresden"
        frida.country = "Deutschland"
        frida.phone = "0351 2632936"
        frida.latitude = 51.06292795819131
        frida.longitude = 13.821817521744753
        frida.imagePath = "https://lh3.googleusercontent.com/p/AF1QipNOIVPD0FWevkxEtCGHnKV2DdojSwpUAFflQcfV=w408-h306-k-no"
        frida.details = "Lotto, Frischetheke"
        frida.category = "Lebensmittel"
        val fridaId = shopDao.insert(frida)

        openingHours( openingHoursDao, fridaId, "08:00", "20:00", 6 )
        Log.e(TAG,"frida created")

        val dm = Shop()
        dm.name = "dm-Markt"
        dm.street = "Bautzner Landstraße"
        dm.streetNumber = "6b"
        dm.postalCode = "01324"
        dm.city = "Dresden"
        dm.country = "Deutschland"
        dm.phone = "0351 27187240"
        dm.latitude = 51.06340817919479
        dm.longitude = 13.821316572481708
        dm.imagePath = "https://lh3.googleusercontent.com/gps-cs-s/AHVAweqLKLx4Mo_z2nudgFs3yPpYCa9bChw3uAXqGAUm9VPM3Sdt7qdyrEQRs2Nv86JMo18WuXQL2LON6jKKjHbWU6zzgR1u3KnOUtwMo_KsJfyanoNO4D7f35SrdM0Bf0Uu6cQ26uzu=s680-w680-h510-rw"
        dm.category = "Drogerie"
        val dmId = shopDao.insert(dm)

        openingHours( openingHoursDao, dmId, "08:00", "20:00", 6 )
        Log.e(TAG,"dm-Markt created")

        val lidl = Shop()
        lidl.name = "Lidl"
        lidl.street = "Bautzner Landstraße"
        lidl.streetNumber = "112-114"
        lidl.postalCode = "01324"
        lidl.city = "Dresden"
        lidl.country = "Deutschland"
        lidl.phone = "030 22005500"
        lidl.latitude = 51.061825493203365
        lidl.longitude = 13.844338979976143
        lidl.imagePath = "https://lh3.googleusercontent.com/p/AF1QipOkvU0d2B0JF_My06OBQpuBAY5_qAZaEE6-taoL=s680-w680-h510-rw"
        lidl.category = "Lebensmittel"
        lidl.details = "DHL Packstation, Ottendorfer Mühlenbäcker, Toilette"
        val lidlId = shopDao.insert(lidl)

        openingHours( openingHoursDao, lidlId, "07:00", "21:00", 6 )
        Log.e(TAG,"lidl created")

        val lts = Shop()
        lts.name = "LTS Presseshop"
        lts.street = "Bautzner Landstraße"
        lts.streetNumber = "15"
        lts.postalCode = "01324"
        lts.city = "Dresden"
        lts.country = "Deutschland"
        lts.phone = "0351 2630908"
        lts.latitude = 51.06350885961888
        lts.longitude = 13.823011152149055
        lts.details = "DHL Post"
        lts.category = "Schreibwaren"
        val ltsId = shopDao.insert(lts)

        openingHours( openingHoursDao, ltsId, "09:00", "13:15", 5 )
        openingHours( openingHoursDao, ltsId, "14:00", "18:00", 5 )
        openingHoursDao.insert(OpeningHours(0L, ltsId, "09:00", "12:00", 6))
        Log.i(TAG, "LTS Shop created")
    }

    /**
     * Creates for days the same opening hours. For example 5 means from monday to friday.
     */
    private suspend fun openingHours( dao: OpeningHoursDao, shopId: Long, start: String, end: String, days: Int ) {
        for( day in 1..days ) {
            val openingHour = OpeningHours( 0L, shopId, start, end, day )
            dao.insert( openingHour )
        }
    }

    companion object {
        private const val TAG = "DroidFridge"
    }
}