package com.example.kpumap

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import java.lang.IndexOutOfBoundsException
import java.util.ArrayList

class BeerActivity : AppCompatActivity() {
    companion object {
        var beerArr = arrayListOf<beer>()
    }

    private fun LoadDB() {
        val helper = DbAdapter(this)
        helper.createDatabase()       //db생성
        helper.open()         //db복사

        beerArr = helper.GetBeerData()   //술집
        helper.close()  //닫기
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer)
        try {
            LoadDB()
        } catch (e: IndexOutOfBoundsException) {
            throw IndexOutOfBoundsException()
        }

        val bToolbar : Toolbar = findViewById(R.id.beerToolBar)
        this.setSupportActionBar(bToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //툴바에 홈 추가
        supportActionBar?.setHomeAsUpIndicator(R.drawable.backbutton)   //백버튼 설정

        //리스트뷰 설정
        val listView : ListView = findViewById(R.id.BeerListView)
        listView.adapter = BeerView(this, beerArr)
        listView.setOnItemClickListener{ adapterView, view, position, id ->
            val bundle = Bundle()
            bundle.putSerializable("imageArr",storeImageArray)
            bundle.putSerializable("storeArr", MainActivity.storeArr)
            bundle.putSerializable("name", beerArr[position].beerName)
            bundle.putSerializable("phoneNum", beerArr[position].beerCall)
            val intent = Intent(this,StoreActivity::class.java)
            intent.putExtra("bundle",bundle)
            startActivity(intent)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun clickMainList(storeName : String, storeArr : ArrayList<store>, imgList : ArrayList<Int>){
        val dialog = Dialog(this)
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

        dialog.setContentView(inflater)
        dialog.show()
    }
}

class BeerView(val context: Context, val beerList: ArrayList<beer>) : BaseAdapter() {

    var beerImageArray = arrayListOf<Int>(R.drawable.b1,R.drawable.b2, R.drawable.b3,R.drawable.b4,R.drawable.b5,
        R.drawable.b6,R.drawable.b7,R.drawable.b8, R.drawable.b9)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View = LayoutInflater.from(context).inflate(R.layout.listitem, null)

        var callView: TextView = view.findViewById(R.id.list_call)     //가게 전화번호
        var addressView: TextView = view.findViewById(R.id.list_address)       //가게 주소
        var titleView: TextView = view.findViewById(R.id.list_title)       //가게에 대한 간략한 설명
        var picView: ImageView = view.findViewById(R.id.list_picture)        //가게사진
        var nameView: TextView = view.findViewById(R.id.list_name)         //가게 이름
        val beer = beerList[position]
        callView.text = beer.beerCall
        addressView.text = beer.beerPlace
        titleView.text = beer.beerIntroduce
        nameView.text = beer.beerName
        picView.setImageResource(beerImageArray[position])

        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return beerList.size
    }

}