package com.gundogar.yazboz101

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Oyuncular(val isimler: List<String>) : Parcelable