package com.summersama.je_a.entity


import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

class IssuesInfo(): Parcelable {



    var url: String = ""
    var repository_url: String = ""
    var labels_url: String = ""
    var comments_url: String = ""
    var events_url: String = ""
    var html_url: String = ""
    var id: Int = 0
    var node_id: String = ""
    var number: Int = 0
    var title: String = ""
    var user: User = User()
    var labels:List<JSONObject> = ArrayList() // fixme 不知道该转成啥 就先转JSONObject了
    var state: String = ""
    var locked: Boolean = false
    var assignee: JSONObject = JSONObject() //null
    var assignees: List<JSONObject> = ArrayList()
    var milestone:JSONObject=JSONObject()
    var comments:Int=0
    var created_at:String=""
    var updated_at:String=""
    var closed_at:JSONObject = JSONObject()
    var author_association:String=""
    var body:String=""
    var score:Float=0f

    constructor(parcel: Parcel) : this() {
        url = parcel.readString()
        repository_url = parcel.readString()
        labels_url = parcel.readString()
        comments_url = parcel.readString()
        events_url = parcel.readString()
        html_url = parcel.readString()
        id = parcel.readInt()
        node_id = parcel.readString()
        number = parcel.readInt()
        title = parcel.readString()
        user = parcel.readParcelable(User::class.java.classLoader)
        state = parcel.readString()
        locked = parcel.readByte() != 0.toByte()
        comments = parcel.readInt()
        created_at = parcel.readString()
        updated_at = parcel.readString()

        author_association = parcel.readString()
        body = parcel.readString()
        score = parcel.readFloat()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(repository_url)
        parcel.writeString(labels_url)
        parcel.writeString(comments_url)
        parcel.writeString(events_url)
        parcel.writeString(html_url)
        parcel.writeInt(id)
        parcel.writeString(node_id)
        parcel.writeInt(number)
        parcel.writeString(title)
        parcel.writeParcelable(user, flags)
        parcel.writeString(state)
        parcel.writeByte(if (locked) 1 else 0)
        parcel.writeInt(comments)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)

        parcel.writeString(author_association)
        parcel.writeString(body)
        parcel.writeFloat(score)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IssuesInfo> {
        override fun createFromParcel(parcel: Parcel): IssuesInfo {
            return IssuesInfo(parcel)
        }

        override fun newArray(size: Int): Array<IssuesInfo?> {
            return arrayOfNulls(size)
        }
    }


}
