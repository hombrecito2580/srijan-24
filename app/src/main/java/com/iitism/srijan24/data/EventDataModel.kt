package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName


class EventDataModel (
    @SerializedName("EventName") var eventName: String,
    @SerializedName("Zone") var zone: String,
    @SerializedName("MiniDescription") var miniDescription: String,
    @SerializedName("Description") var description: String,
    @SerializedName("Venue") var venue: String,
    @SerializedName("Fees") var fees: String,
    @SerializedName("Contact") var contact: List<ContactDataModel>,
    @SerializedName("Rules") var rules: List<String>,
    @SerializedName("PosterURL") var posterURL: String,
    @SerializedName("MinMenbers") var minMembers: Number,
    @SerializedName("MaxMembers") var maxMembers: Number
)

class ContactDataModel (
    @SerializedName("Name") var name:String,
    @SerializedName("PhoneNumber") var phoneNumber:String
)
class ExtraDetailsDataModel(
    var key: String,
    var type: List<String>
)

class StagesDataModel(
    var description: String,
    var venue: String,
    var timing: String,
    var calendarLink: String
)