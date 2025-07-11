package com.gundogar.yazboz101.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Player(val name: String,val scores: List<Int>) : Parcelable
