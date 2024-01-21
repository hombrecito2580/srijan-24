package com.iitism.srijan24.data
import com.google.gson.annotations.SerializedName
data class EventTeamModel(
    @SerializedName("EventName") var eventName: String?=null,
    @SerializedName("Teams") var teams: List<MemberListModel> = mutableListOf()
)
data class MemberListModel (
    @SerializedName("MemberList") var memberList: List<MemberDataModel> = mutableListOf(),
    @SerializedName("TeamName") var teamName: String?=null,
    @SerializedName("isSponsor") var isSponsor: String?=null,
    @SerializedName("Audio") var audio: String?=null,
    @SerializedName("Accompanist") var accompanist: String?=null
)

data class MemberDataModel(
    @SerializedName("Name") var name: String?=null,
    @SerializedName("Email") var email: String?=null,
    @SerializedName("PhoneNumber") var phone: String?=null,
    @SerializedName("College") var college: String?=null,
    @SerializedName("Instrument") var instrument: String?=null
)