package com.example.practiceapplication

import android.app.Application
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class dog(
    @PrimaryKey var id : Int = 0,
    var name : String = ""
):RealmObject(){}

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}
open class store(
    var name : String
): Serializable{}

