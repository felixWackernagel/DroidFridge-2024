package de.wackernagel.droidfridge.data

import de.wackernagel.droidfridge.MyApplication
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
            MyApplication.appCtx!!.resources.getText(id).toString()
    }
}
