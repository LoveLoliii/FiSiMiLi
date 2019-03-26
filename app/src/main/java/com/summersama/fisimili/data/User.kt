package com.summersama.je_a.entity

import android.os.Parcel
import android.os.Parcelable

class User():Parcelable {
    var login:String=""
    var id:Int=0
    var node_id:String=""
    var avatar_url:String=""
    var gravatar_id:String=""
    var url:String=""
    var html_url:String=""
    var followers_url:String=""
    var following_url:String=""
    var gists_url:String=""
    var starred_url:String=""
    var subscriptions_url:String=""
    var organizations_url:String=""
    var repos_url:String=""
    var events_url:String=""
    var received_events_url:String=""
    var type:String=""
    var site_admin:Boolean=false

    constructor(parcel: Parcel) : this() {
        login = parcel.readString()
        id = parcel.readInt()
        node_id = parcel.readString()
        avatar_url = parcel.readString()
        gravatar_id = parcel.readString()
        url = parcel.readString()
        html_url = parcel.readString()
        followers_url = parcel.readString()
        following_url = parcel.readString()
        gists_url = parcel.readString()
        starred_url = parcel.readString()
        subscriptions_url = parcel.readString()
        organizations_url = parcel.readString()
        repos_url = parcel.readString()
        events_url = parcel.readString()
        received_events_url = parcel.readString()
        type = parcel.readString()
        site_admin = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeInt(id)
        parcel.writeString(node_id)
        parcel.writeString(avatar_url)
        parcel.writeString(gravatar_id)
        parcel.writeString(url)
        parcel.writeString(html_url)
        parcel.writeString(followers_url)
        parcel.writeString(following_url)
        parcel.writeString(gists_url)
        parcel.writeString(starred_url)
        parcel.writeString(subscriptions_url)
        parcel.writeString(organizations_url)
        parcel.writeString(repos_url)
        parcel.writeString(events_url)
        parcel.writeString(received_events_url)
        parcel.writeString(type)
        parcel.writeByte(if (site_admin) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
