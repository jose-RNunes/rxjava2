package model

data class People(
    var birth_year:String = "",
    var eye_color:String = "",
    var films:List<String> = ArrayList(),
    var gender:String = "",
    var name:String = "",
    var vehicles:List<String> = ArrayList()
)