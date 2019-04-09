package model

data class Peoples( var count:Int = 0,
                    var next:String = "",
                    var previous:String = "",
                    var results:List<People> = ArrayList())