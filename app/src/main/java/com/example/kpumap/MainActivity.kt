package com.example.kpumap

import android.app.Dialog
import android.app.LauncherActivity
import android.content.Context
import java.io.Serializable
import android.content.Intent
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_start.*
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.sql.SQLException
import java.util.ArrayList

//getidentifier 사용해도 됌
var storeImageArray = arrayListOf<Int>(
    R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5,
    R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9, R.drawable.b1, R.drawable.b2,
    R.drawable.b3, R.drawable.b4, R.drawable.b5, R.drawable.b6, R.drawable.b7, R.drawable.b8,
    R.drawable.b9, R.drawable.r1, R.drawable.r2, R.drawable.r3, R.drawable.r4, R.drawable.r5,
    R.drawable.r6, R.drawable.r7, R.drawable.r8, R.drawable.r9, R.drawable.r10,
    R.drawable.r11, R.drawable.r12, R.drawable.r13
)
var num : String? = null

class MainActivity : AppCompatActivity() {


    //static
    companion object {
        var storeArr = arrayListOf<store>()
    }

    private fun LoadDB() {
        val helper = DbAdapter(this)
        helper.createDatabase()       //db생성
        helper.open()                 //db복사

        storeArr = helper.GetAllData()      //모든 객체 가져오기
        helper.close()  //닫기
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            LoadDB()
        } catch (e: IndexOutOfBoundsException) {
            throw IndexOutOfBoundsException()
        }
        //Log.d("myTag", "카페 명은  " + cafeArr[0].cafeName + cafeArr[0].cafeIntroduce)
        //Log.d("myTag", "술집소개! " + beerArr[0].beerName + beerArr[0].beerIntroduce)
        //Log.d("myTag", "길이"+resArr.size)
        //Log.d("myTag",packageName.toString())

        /*
        for(i in 0..storeArr.size-1) {
            Log.d("myTag", "${storeArr[i].storeMenu}   영업시간${storeArr[i].storeTime}")
        }*/

        //검색어 가져오기
        //돋보기 클릭 시 검색 한 가게 액티비티 실행
        searchBtn.setOnClickListener {
            var searchText = searchEdit.text.toString()

            for (i in 0 until storeArr.size-1) {
                if (searchText==storeArr[i].storeName) {
                    //클릭시 가게명 액티비티를 실행
                    val bundle = Bundle()
                    bundle.putSerializable("imageArr",storeImageArray)
                    bundle.putSerializable("storeArr", storeArr)
                    bundle.putSerializable("name",storeArr[i].storeName)
                    bundle.putSerializable("phoneNum", storeArr[i].storeCall)
                    val intent = Intent(this,StoreActivity::class.java)
                    intent.putExtra("bundle",bundle)
                    startActivity(intent)
                    break
                }
                else if(searchText == ""){
                    Toast.makeText(this,"가게명을 입력해주세요.",Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(this,"가게명을 찾을 수 없습니다.",Toast.LENGTH_SHORT).show()
                }
            }
        }

        //리스트뷰 생성
        val viewMain: ListView = findViewById(R.id.mainListView)
        viewMain.adapter = mainList(this, storeArr)

        //리스트뷰 클릭 이벤트 감지
        //1- 클릭된 아이템을 보여주는 객체
        //2- 클릭된 아이템 뷰
        //3- 클릭된 아이템  위치
        //4 - 클릭된 아이템 아이디
        viewMain.setOnItemClickListener { adapterView, view, position, id ->
            val bundle = Bundle()
            bundle.putSerializable("imageArr",storeImageArray)
            bundle.putSerializable("storeArr", storeArr)
            bundle.putSerializable("name",storeArr[position].storeName)
            bundle.putSerializable("phoneNum", storeArr[position].storeCall)
            val intent = Intent(this,StoreActivity::class.java)
            intent.putExtra("bundle",bundle)
            startActivity(intent)
        }

        //맵 버튼 클릭 시 지도 보여주기
        mapButton.setOnClickListener {

        }
        //이미지 클릭시 RestaurantActivity 인텐트 보내기
        restaurant.setOnClickListener {
            val intent = Intent(this, RestaurantActivity::class.java)
            startActivity(intent)
        }
        //술집 리스트 액티비티 실행
        beer.setOnClickListener {
            val intent = Intent(this, BeerActivity::class.java)
            startActivity(intent)
        }
        //카페 리스트 액티비티 실행
        cafe.setOnClickListener {
            val intent = Intent(this, CafeActivity::class.java)
            startActivity(intent)
        }
    }/*
    //리스트 뷰 클릭 시 다이얼로그 생성
    fun clickMainList(storeName : String, storeArr : ArrayList<store>, imgList : ArrayList<Int>){
     //   val dialog = Dialog(this)
        var inflater = LayoutInflater.from(this.applicationContext).inflate(R.layout.activity_detail,null)

        val imageSrc : ImageView = inflater.findViewById(R.id.pic1) //이미지리소스
        val detailName : TextView = inflater.findViewById(R.id.title_detail)    //가게명
        val detailIntro : TextView = inflater.findViewById(R.id.intro_detail)   //가게소개
        val detailCall : TextView =inflater.findViewById(R.id.call_phone_detail)   //가게전번
        val detailPlace : TextView = inflater.findViewById(R.id.place_detail)
        val detailTiming : TextView = inflater.findViewById(R.id.time_detail)   //영업시간
        val detailMenu : TextView = inflater.findViewById(R.id.menu_detail)     //메뉴및 가게

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

       // dialog.setContentView(inflater)
      //  dialog.show()
    }*/
}

//메인 리스트뷰 생성
class mainList(val context: Context, val storeList: ArrayList<store>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view: View = LayoutInflater.from(context).inflate(R.layout.listitem, null)

        var callView: TextView = view.findViewById(R.id.list_call)     //가게 전화번호
        var addressView: TextView = view.findViewById(R.id.list_address)       //가게 주소
        var titleView: TextView = view.findViewById(R.id.list_title)       //가게에 대한 간략한 설명
        var picView: ImageView = view.findViewById(R.id.list_picture)        //가게사진
        var nameView: TextView = view.findViewById(R.id.list_name)         //가게 이름

        val store = storeList[position]
        nameView.text = store.storeName
        callView.text = store.storeCall
        addressView.text = store.storePlace
        titleView.text = store.storeIntro
        picView.setImageResource(storeImageArray[position])

        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return storeList.size
    }

}


