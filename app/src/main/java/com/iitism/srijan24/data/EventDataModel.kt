import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class EventDataModel(
    @SerializedName("EventName") var eventName: String? = null,
    @SerializedName("Zone") var zone: String? = null,
    @SerializedName("Poster") var poster: String? = null,
    @SerializedName("miniDescription") var miniDescription: String? = null,
    @SerializedName("Description") var description: String? = null,
    @SerializedName("Venue") var venue: String? = null,
    @SerializedName("Fees") var fees: String? = null,
    @SerializedName("Contactdetails") var contact: List<ContactDataModel>? = null,
    @SerializedName("Rules") var rules: List<String>? = null,
    @SerializedName("RuleBookLink") var ruleBookLink: String? = null,
    @SerializedName("Minimummembers") var minMembers: Number? = null,
    @SerializedName("Maximummembers") var maxMembers: Number? = null,
    @SerializedName("teamName") var teamName: Boolean? = null,
    @SerializedName("sponsor") var sponsor: Boolean? = null,
    @SerializedName("audio") var audio: Boolean? = null,
    @SerializedName("accompanist") var accompanist: Boolean? = null
) : Parcelable {
    // Parcelable implementation
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(ContactDataModel),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readValue(Number::class.java.classLoader) as Number?,
        parcel.readValue(Number::class.java.classLoader) as Number?,
        parcel.readValue(Boolean::class.java.classLoader) as Boolean?,
        parcel.readValue(Boolean::class.java.classLoader) as Boolean?,
        parcel.readValue(Boolean::class.java.classLoader) as Boolean?,
        parcel.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventName)
        parcel.writeString(zone)
        parcel.writeString(poster)
        parcel.writeString(miniDescription)
        parcel.writeString(description)
        parcel.writeString(venue)
        parcel.writeString(fees)
        parcel.writeTypedList(contact)
        parcel.writeStringList(rules)
        parcel.writeString(ruleBookLink)
        parcel.writeValue(minMembers)
        parcel.writeValue(maxMembers)
        parcel.writeValue(teamName)
        parcel.writeValue(sponsor)
        parcel.writeValue(audio)
        parcel.writeValue(accompanist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventDataModel> {
        override fun createFromParcel(parcel: Parcel): EventDataModel {
            return EventDataModel(parcel)
        }

        override fun newArray(size: Int): Array<EventDataModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class ContactDataModel(
    @SerializedName("name") var name: String? = null,
    @SerializedName("phone") var phoneNumber: String? = null
) : Parcelable {
    // Parcelable implementation
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(phoneNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactDataModel> {
        override fun createFromParcel(parcel: Parcel): ContactDataModel {
            return ContactDataModel(parcel)
        }

        override fun newArray(size: Int): Array<ContactDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
