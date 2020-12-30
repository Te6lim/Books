package com.andyprojects.books.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Library(
    val kind: String,
    val totalItems: Int,
    val items: List<Book>
): Parcelable