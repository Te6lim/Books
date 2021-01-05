package com.andyprojects.books.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Book(
    val kind: String?,
    val id: String?,
    val etag: String?,
    val selfLink: String?,
    val volumeInfo: VolumeInfo?
): Parcelable {
    @Parcelize
    class VolumeInfo(
        val title: String?,
        val publisher: String?,
        val authors: Array<String?>?,
        val averageRating: Double?,
        val imageLinks: ImageLinks?,
        val industryIdentifiers: Array<BookNum>?,
        val description: String?,
        val pageCount: Int?
    ): Parcelable {
        @Parcelize
        class ImageLinks(
            val smallThumbnail: String?,
            val thumbnail: String?
        ): Parcelable

        @Parcelize
        class BookNum(
            val type: String,
            val identifier: String
        ): Parcelable
    }
}