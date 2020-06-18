package com.example.kpumap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList


fun clickMainList(context: Context, storeName : String, storeArr : ArrayList<store>,imgList : ArrayList<Int>){
    var view = LayoutInflater.from(context).inflate(R.layout.activity_detail,null)
    /*
    var inflater : LayoutInflater? = null
    var view = inflater?.inflate(R.layout.activity_detail,null)
    */

    val imageSrc : ImageView = view.findViewById(R.id.pic1) //이미지리소스
    val detailName : TextView = view.findViewById(R.id.title_detail)    //가게명
    val detailIntro : TextView = view.findViewById(R.id.intro_detail)   //가게소개
    val detailCall : TextView =view.findViewById(R.id.call_phone_detail)   //가게전번
    val detailPlace : TextView = view.findViewById(R.id.place_detail)
    val detailTiming : TextView = view.findViewById(R.id.time_detail)   //영업시간
    val detailMenu : TextView = view.findViewById(R.id.menu_detail)     //메뉴및 가게

    for(i in 0..storeArr.size-1){
        if(storeArr[i].storeName.equals(storeName)){
            imageSrc.setImageResource(imgList[i])
            detailName.text = storeArr[i].storeName
            detailIntro.text = storeArr[i].storeIntro
            detailCall.text = storeArr[i].storeCall
            detailPlace.text = storeArr[i].storePlace
            detailTiming.text = storeArr[i].storeTime
            detailMenu.text = storeArr[i].storeMenu
        }
    }

}