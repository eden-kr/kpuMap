package com.example.kpumap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var searchText : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //검색어 가져오기
        searchText = searchEdit.text.toString()

        //검색 버튼 클릭 시
        searchBtn.setOnClickListener {

        }
        //레스토랑 버튼 클릭 시
        restaurant.setOnClickListener {

        }
        //술집 버튼 클릭 시
        beer.setOnClickListener {

        }
        //카페 버튼 클릭 시
        cafe.setOnClickListener {

        }
    }
}
