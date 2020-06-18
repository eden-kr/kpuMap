package com.example.kpumap
import java.io.Serializable

open class beer(
    var id: Int,
    var beerName: String,
    var beerPlace: String,
    var beerCall: String,
    var beerIntroduce: String
) :Serializable{}

open class restaurant(
    var id: Int,
    var resPlace: String,
    var resCall: String,
    var resIntroduce: String,
    var resName: String
) :Serializable{}

open class cafe(
    var id: Int,
    var cafeName: String,
    var cafePlace: String,
    var cafeCall: String,
    var cafeIntroduce: String
) :Serializable{}
open class store (
    var storeName : String,
    var storePlace : String,
    var storeCall : String,
    var storeIntro : String,
    var storeMenu : String,
    var storeTime : String
):Serializable{}
