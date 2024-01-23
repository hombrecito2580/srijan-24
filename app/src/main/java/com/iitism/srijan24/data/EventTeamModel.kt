package com.iitism.srijan24.data
import com.google.gson.annotations.SerializedName
data class EventTeamModel(
    @SerializedName("EventName") var eventName: String="",
    @SerializedName("Teams") var teams: List<MemberListModel> = emptyList()
)
data class MemberListModel (
    @SerializedName("MembersList") var memberList: List<MemberDataModel> = emptyList(),
    @SerializedName("TeamName") var teamName: String?=null,
    @SerializedName("IsSponsor") var isSponsor: String?=null,
    @SerializedName("Audio") var audio: String?=null,
    @SerializedName("Accompanist") var accompanist: String?=null,
    @SerializedName("ReferralID") var referralId: String? = null,
    @SerializedName("Genre") var genre: List<String>? = null
)

data class MemberDataModel(
    @SerializedName("Name") var name: String?=null,
    @SerializedName("Email") var email: String?=null,
    @SerializedName("PhoneNumber") var phone: String?=null,
    @SerializedName("College") var college: String?=null,
    @SerializedName("Instrument") var instrument: String?=null
)