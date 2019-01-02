package example.mcnewz.com.poclogfirebase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Econsent(
    var name: String? = "",
    var data: String? = ""
)