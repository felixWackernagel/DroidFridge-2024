package com.example.myapplication.data

import com.example.myapplication.MyApplication
import com.example.myapplication.R

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
