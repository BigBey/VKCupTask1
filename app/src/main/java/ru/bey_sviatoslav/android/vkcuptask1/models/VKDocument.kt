package ru.bey_sviatoslav.android.vkcuptask1.models

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class VKDocument(
    val id: Int = 0,
    val title : String = "",
    val size : Long = 0,
    val ext : String = "",
    val url : String = "",
    val date : Long = 0,
    val type : Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readLong()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()!!,
        parcel.readInt()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeLong(size)
        parcel.writeString(ext)
        parcel.writeString(url)
        parcel.writeLong(date)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VKDocument> {
        override fun createFromParcel(parcel: Parcel): VKDocument {
            return VKDocument(parcel)
        }

        override fun newArray(size: Int): Array<VKDocument?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject)
                = VKDocument(id = json.optInt("id", 0),
            title = json.optString("title", ""),
            size = json.optLong("size", 0),
            ext = json.optString("ext", ""),
            url = json.optString("url", ""),
            date = json.optLong("date", 0),
            type = json.optInt("type", 0))
    }
}