package com.neko.hiepdph.toiletseries.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MonsterModel(
    val id :Int,
    val image: Int,
    val content: String,
) : Parcelable {}