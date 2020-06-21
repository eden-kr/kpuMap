package com.example.kpumap

import android.app.Dialog
import android.app.LauncherActivity
import android.content.Context
import java.io.Serializable
import android.content.Intent
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
    R.drawable.b9, R.drawable.r1, R.drawable.r2, R.drawable.r4, R.drawable.r5, R.drawable.r6,
    R.drawable.r7, R.drawable.r8, R.drawable.r9, R.drawable.r10, R.drawable.r11,
    R.drawable.r12, R.drawable.r13
)


class MainActivity : AppCompatActivity() {

    var storeArr = arrayListOf<store>()


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
        //Log.d("myTag","이미지 url은${imgArr[1].imgRss}") 이미지리소스확인

       /* for(i in 0..storeArr.size-1) {
            Log.d("myTag", "   가게명${storeArr[i].storeName}")
        }*/
        //검색어 가져오기
        //돋보기 클릭 시 검색 한 가게 액티비티 실행
        searchBtn.setOnClickListener {
            var searchText : String = searchEdit.text.toString()

           for (i in 0 until storeArr.size) {
                if(storeArr[i].storeName == searchText) {
                    //클릭시 가게명 액티비티를 실행
                    val bundle = Bundle()
                    bundle.putSerializable("imageArr",storeImageArray)
                    bundle.putSerializable("storeArr", storeArr)
                    bundle.putSerializable("position",i)
                    bundle.putSerializable("name",storeArr[i].storeName)
                    bundle.putSerializable("img", storeImageArray[i])
                    bundle.putSerializable("phoneNum", storeArr[i].storeCall)
                    val intent = Intent(this,StoreActivity::class.java)
                    intent.putExtra("bundle",bundle)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"가게명을 찾을 수 없습니다.",Toast.LENGTH_SHORT).show()
                }
                if(searchText == ""){
                    Toast.makeText(this,"가게명을 입력해주세요.",Toast.LENGTH_SHORT).show()
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
            bundle.putSerializable("img", storeImageArray[position])
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
        getBookmarkList.setOnClickListener {
            val intent = Intent(this,BookmarkActivity::class.java)
            startActivity(intent)
        }
    }
}

//메인 리스트뷰 생성
class mainList(val context: Context, val storeList: ArrayList<store>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val holder : ViewHolder

        //convertView == null 화면을 최초로 실행할 때
        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem, null)
            holder = ViewHolder()
            holder.callView = view.findViewById(R.id.list_call)     //가게 전화번호
            holder.addressView  = view.findViewById(R.id.list_address)       //가게 주소
            holder.titleView = view.findViewById(R.id.list_title)       //가게에 대한 간략한 설명
            holder.picView = view.findViewById(R.id.list_picture)        //가게사진
            holder.nameView = view.findViewById(R.id.list_name)         //가게 이름

            //홀더에 각각의 아이디 설정 후
            view.tag =holder   //태그 변경
        }else {         //만들어진 뷰가 있으면 tag갱신
            holder = convertView.tag as ViewHolder
            view = convertView
        }
        val store = storeList[position]
        holder.nameView?.text = store.storeName
        holder.callView?.text = store.storeCall
        holder.addressView?.text = store.storePlace
        holder.titleView?.text = store.storeIntro
        holder.picView?.setImageResource(storeImageArray[position])

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
    open class ViewHolder {
        var callView : TextView? = null
        var addressView : TextView? = null
        var titleView : TextView? = null
        var nameView : TextView? = null
        var picView : ImageView? = null
    }
}


