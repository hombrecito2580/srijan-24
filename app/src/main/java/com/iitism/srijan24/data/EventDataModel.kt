package com.iitism.srijan24.data


class EventDataModel (
    var posterMobile: String, //image link
    var posterWeb: String, // image link
    var name: String,
    var mode: String, // online , offline
    var descriptionEvent: String, // description about event
    var descriptionOrganizer: String, // description about Organizer
    var type: Number, // 1=club event , 2= departmental event, 3=informal event
    var organizer: String, // club name , department name
    var rules: List<String>, // event rules
    var registrationStatus: Number, //1=active ,2=inactive, 3=other platform(unstop,googleForm, etc.)
    var registrationLink: String,
    var prizes: String, // 1 , 2 , 3 prize money
    var contacts: List<ContactDataModel>,
    var fees: String, // paid or free event ( if paid then cost )
    var pdfLink: String, // link of the rulebook pdf of event
    var minTeamSize: Number,
    var maxTeamSize: Number,
    var problemStatements: String, // can be multiple
    var extraDetails: List<ExtraDetailsDataModel>,
    var teams: List<Void>, // registered teams
    var stages: List<StagesDataModel>
)

class ContactDataModel (
    var name:String,
    var phoneNumber:String
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