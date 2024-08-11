package de.wackernagel.droidfridge.data

import de.wackernagel.droidfridge.DroidFridgeApplication
import de.wackernagel.droidfridge.R

class Validation {
    companion object {
        val nameValidation = { value: String? ->
            when {
                value.isNullOrBlank() -> getResourceText(R.string.validation_no_name)
                else -> null
            }
        }
        private fun getResourceText(id: Int) =
            DroidFridgeApplication.appCtx!!.resources.getText(id).toString()
    }
}
