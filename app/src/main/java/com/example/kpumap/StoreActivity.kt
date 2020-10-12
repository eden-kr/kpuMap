package com.example.kpumap

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Paint
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import io.realm.*
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.Serializable
import java.lang.IllegalStateException
import java.lang.IndexOutOfBoundsException
import java.lang.NullPointerException


class StoreActivity : AppCompatActivity() {
    var getImage: Int = 0
    val realm = Realm.getDefaultInstance()

    override fun onResume() {
        super.onResume()
        //북마크 클릭 저장
        var f = realm.where(BookmarkStore::class.java).findAll()
        for(i in 0 until f.size){
            if(f[i].stName == this.title_detail.text.toString()){
                if(f[i].stIsClicked) bookmark.setImageResource(R.drawable.clickedbookmark)
            }
        }

        val count = realm.where(User::class.java).findAll()
        var cnt = 0
        if(count != null) {
            for(i in 0 until count.size) {
                if(count[i].stName == title_detail.text.toString()) {
                    cnt++
                }
            }
        }
        review_num.text = cnt.toString()
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()   //종료 시 닫아줌
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val bundle = intent.getBundleExtra("bundle")
        val imgArr = bundle.getSerializable("imageArr") as ArrayList<Int>
        val storeArr = bundle.getSerializable("storeArr") as ArrayList<store>
        val imgName = bundle.getSerializable("name") as String
        val phoneNum = bundle.getSerializable("phoneNum") as String
        getImage = bundle.getSerializable("img") as Int             //클릭된 위치의 해당 이미지
        clickMainList(imgName, storeArr, imgArr)


        //가게에 전화걸기
        call_detail.setOnClickListener {
            val uri = Uri.parse("tel:$phoneNum")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
        }
        //뒤로가기 버튼
        var toolbar: Toolbar? = null
        toolbar = findViewById(R.id.storeToolbar)
        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //툴바에 홈 추가
        supportActionBar?.setHomeAsUpIndicator(R.drawable.backbutton)   //백버튼 설정

        Log.d("realm", "${realm.where(BookmarkStore::class.java).findAll()}")

        //즐겨찾기 구현
        bookmark.setOnClickListener {
            bookmark.isSelected = !bookmark.isSelected
            if (bookmark.isSelected) {        //버튼이 눌리면
                bookmark.setImageResource(R.drawable.clickedbookmark)
                Insert()
                Log.d("realm", "${title_detail.text}")
            } else {
                bookmark.setImageResource(R.drawable.ic_star_black_24dp)
                Delete()
            }
        }
        //리뷰 리스너
        review_num.setOnClickListener {
            val intent = Intent(this,ReviewActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable("storeName",title_detail.text.toString())
            intent.putExtra("bundle",bundle)
            startActivity(intent)
        }

        //이미지 리스트 출력
        pic1.setOnClickListener {
            var title: TextView = findViewById(R.id.title_detail)
            var storeName = title.text.toString()

            val bd = Bundle()
            bd.putSerializable("storeName", storeName)
            val intent = Intent(this, ImageListActivity::class.java)
            intent.putExtra("bd", bd)
            startActivity(intent)
        }
    }

    //Realm
    //db Insert
    private fun Insert() {
        realm.beginTransaction()
        var check =
            realm.where(BookmarkStore::class.java).equalTo("stName", title_detail.text.toString())
                .findFirst()

        if (check == null) {
            val store = realm.createObject(BookmarkStore::class.java, nextId())
            store.stName = title_detail.text.toString()
            store.stPhonNum = call_phone_detail.text.toString()
            store.stAddress = place_detail.text.toString()
            store.stIntro = intro_detail.text.toString()
            store.stImage = getImage
            store.stIsClicked = bookmark.isSelected
            realm.commitTransaction()
            Log.d("realm", "db is inserted")

        } else {

        }
    }

    private fun nextId(): Int {
        val maxId = realm.where(BookmarkStore::class.java).max("id")
        if (maxId != null) {
            return maxId.toInt() + 1
        }
        return 0
    }

    //db delete
    private fun Delete() {
        var check = realm.where<BookmarkStore>(BookmarkStore::class.java)
            .equalTo("stName", title_detail.text.toString()).findFirst()

        check.deleteFromRealm()
        realm.commitTransaction()

        Log.d("realm", "db is deleted")

    }

    //db 전체삭제
    private fun Clear() {
        realm.beginTransaction()
        realm.deleteAll()
        realm.commitTransaction()
        Log.d("realm", "db is cleared")
    }

    //툴바 백버튼 눌릴 때 동작
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun clickMainList(storeName: String, storeArr: ArrayList<store>, imgList: ArrayList<Int>) {

        val imageSrc: ImageView = this.findViewById(R.id.pic1)
        val detailName: TextView = this.findViewById(R.id.title_detail)    //가게명
        val detailIntro: TextView = this.findViewById(R.id.intro_detail)   //가게소개
        val detailCall: TextView = this.findViewById(R.id.call_phone_detail)   //가게전번
        val detailPlace: TextView = this.findViewById(R.id.place_detail)
        val detailTiming: TextView = this.findViewById(R.id.time_detail)   //영업시간
        val detailMenu: TextView = this.findViewById(R.id.menu_detail)     //메뉴및 가게
        val detailReview : TextView = this.findViewById(R.id.review_num)

        //밑줄 치기
        detailReview.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        for (i in 0 until storeArr.size - 1) {
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

