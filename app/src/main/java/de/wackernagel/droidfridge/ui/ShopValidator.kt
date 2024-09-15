package de.wackernagel.droidfridge.ui

class ShopValidator {

    fun validate( name: String ) : Result<Unit, ShopError> {
        if( name.isBlank() ) {
            return Result.Error( ShopError.NO_NAME )
        }

        return Result.Success( Unit )
    }

    enum class ShopError: Error {
        NO_NAME
    }

}