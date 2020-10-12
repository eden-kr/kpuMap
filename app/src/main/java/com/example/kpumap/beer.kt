package com.example.kpumap

import android.app.Application
import android.graphics.Bitmap
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class beer(
    var id: Int,
    var beerName: String,
    var beerPlace: String,
    var beerCall: String,
    var beerIntroduce: String

) : Serializable {}

open class restaurant(
    var id: Int,
    var resPlace: String,
    var resCall: String,
    var resIntroduce: String,
    var resName: String

) : Serializable {}

open class cafe(
    var id: Int,
    var cafeName: String,
    var cafePlace: String,
    var cafeCall: String,
    var cafeIntroduce: String
) : Serializable {}

open class store(
    var storeName: String,
    var storePlace: String,
    var storeCall: String,
    var storeIntro: String,
    var storeMenu: String,
    var storeTime: String,
    var lat: Float,            //위도
    var lng: Float
) : Serializable {}

//이미지이름, 가게명에 따른 url
open class ImageResource(var imgRssName: String, var imgRss: String) : Serializable {
    var bitmap: Bitmap? = null
}

//저장목록에 저장될 data
open class BookmarkStore(
    @PrimaryKey var id: Int = 0,
    var stName: String = "",
    var stPhonNum: String = "",
    var stAddress: String = "",
    var stIntro: String = "",
    var stImage: Int = 0,
    var stIsClicked: Boolean = false
) : RealmObject() {}
//리뷰 작성시 사용될 데이터
open class User(
    @PrimaryKey var id: Int = 0,
    var userId: String = "",
    var password: String = "",
    var rating: Double = 0.0,
    var review: String = "",
    var date: String = "",
    var stName: String = ""
) :RealmObject(){}

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)


/*
        val  config = RealmConfiguration.Builder()
        .deleteRealmIfMigrationNeeded()
        .build();

        Realm.setDefaultConfiguration(config);
*/
    }

}