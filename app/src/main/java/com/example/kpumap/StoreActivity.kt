package com.example.kpumap

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.Serializable



class StoreActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val bundle = intent.getBundleExtra("bundle")
        val imgArr = bundle.getSerializable("imageArr") as ArrayList<Int>
        val storeArr = bundle.getSerializable("storeArr") as ArrayList<store>
        val imgName = bundle.getSerializable("name") as String
        val phoneNum = bundle.getSerializable("phoneNum") as String
        clickMainList(imgName,storeArr,imgArr)


        //가게에 전화걸기
        call_detail.setOnClickListener {
            val uri = Uri.parse("tel:$phoneNum")
            val intent = Intent(Intent.ACTION_DIAL,uri)
            startActivity(intent)
        }
        //뒤로가기 버튼
        var toolbar: Toolbar? = null
        toolbar = findViewById(R.id.storeToolbar)
        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //툴바에 홈 추가
        supportActionBar?.setHomeAsUpIndicator(R.drawable.backbutton)   //백버튼 설정

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun clickMainList(storeName : String, storeArr : ArrayList<store>, imgList : ArrayList<Int>) {
        val imageSrc: ImageView =  this.findViewById(R.id.pic1)
        val detailName: TextView = this.findViewById(R.id.title_detail)    //가게명
        val detailIntro: TextView = this.findViewById(R.id.intro_detail)   //가게소개
        val detailCall: TextView = this.findViewById(R.id.call_phone_detail)   //가게전번
        val detailPlace: TextView = this.findViewById(R.id.place_detail)
        val detailTiming: TextView = this.findViewById(R.id.time_detail)   //영업시간
        val detailMenu: TextView = this.findViewById(R.id.menu_detail)     //메뉴및 가게

        for (i in 0..storeArr.size - 1) {
            if (storeArr[i].storeName.equals(storeName)) {
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
}

