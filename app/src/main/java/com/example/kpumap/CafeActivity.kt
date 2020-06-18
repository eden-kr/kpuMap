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
import kotlinx.android.synthetic.main.activity_cafe.*
import java.lang.IndexOutOfBoundsException
import java.util.ArrayList

class CafeActivity : AppCompatActivity() {
    companion object {
        var cafeArr = arrayListOf<cafe>()
    }

    private fun LoadDB() {
        val helper = DbAdapter(this)
        helper.createDatabase()       //db생성
        helper.open()         //db복사

        cafeArr = helper.GetCafeData()   //카페
        helper.close()  //닫기
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cafe)
        try {
            LoadDB()
        } catch (e: IndexOutOfBoundsException) {
            throw IndexOutOfBoundsException()
        }
        //백버튼 추가
        val cafeToolbar : Toolbar = findViewById(R.id.cfToolbar)
        this.setSupportActionBar(cafeToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //툴바에 홈 추가
        supportActionBar?.setHomeAsUpIndicator(R.drawable.backbutton)   //백버튼 설정


        //리스트뷰 만들기
        val cafeListView : ListView = findViewById(R.id.cafeListView)
        cafeListView.adapter = CafeView(this,cafeArr)
        cafeListView.setOnItemClickListener { adapterView, view, position, id ->
            val bundle = Bundle()
            bundle.putSerializable("imageArr",storeImageArray)
            bundle.putSerializable("storeArr", MainActivity.storeArr)
            bundle.putSerializable("name", cafeArr[position].cafeName)
            bundle.putSerializable("phoneNum", cafeArr[position].cafeCall)
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
class CafeView(val context: Context, val cafeList: ArrayList<cafe>) : BaseAdapter() {
    var cafeImageArray = arrayListOf<Int>(R.drawable.c1,R.drawable.c2, R.drawable.c3,R.drawable.c4,R.drawable.c5,
        R.drawable.c6,R.drawable.c7,R.drawable.c8, R.drawable.c9)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View = LayoutInflater.from(context).inflate(R.layout.listitem, null)

        var callView: TextView = view.findViewById(R.id.list_call)     //가게 전화번호
        var addressView: TextView = view.findViewById(R.id.list_address)       //가게 주소
        var titleView: TextView = view.findViewById(R.id.list_title)       //가게에 대한 간략한 설명
        var picView: ImageView = view.findViewById(R.id.list_picture)        //가게사진
        var nameView: TextView = view.findViewById(R.id.list_name)         //가게 이름

        val cafe = cafeList[position]
        callView.text = cafe.cafeCall
        addressView.text = cafe.cafePlace
        titleView.text = cafe.cafeIntroduce
        nameView.text = cafe.cafeName
        picView.setImageResource(cafeImageArray[position])

        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return cafeList.size
    }

}
