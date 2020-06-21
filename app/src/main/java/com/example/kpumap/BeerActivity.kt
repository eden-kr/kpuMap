package com.example.kpumap

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_beer.*
import kotlinx.android.synthetic.main.listitem.*
import java.lang.IndexOutOfBoundsException
import java.util.ArrayList

var beerImageArray = arrayListOf<Int>(R.drawable.b1,R.drawable.b2, R.drawable.b3,R.drawable.b4,R.drawable.b5,
    R.drawable.b6,R.drawable.b7,R.drawable.b8, R.drawable.b9)
class BeerActivity : AppCompatActivity() {
    companion object {
        var beerArr = arrayListOf<beer>()
        var storeArr = arrayListOf<store>()
    }

    private fun LoadDB() {
        val helper = DbAdapter(this)
        helper.createDatabase()       //db생성
        helper.open()         //db복사

        beerArr = helper.GetBeerData()   //술집
        storeArr = helper.GetAllData()      //모든 객체 가져오기

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
            bundle.putSerializable("storeArr", storeArr)
            bundle.putSerializable("name", beerArr[position].beerName)
            bundle.putSerializable("position",position)
            bundle.putSerializable("img", beerImageArray[position])
            bundle.putSerializable("phoneNum", beerArr[position].beerCall)
            val intent = Intent(this,StoreActivity::class.java)
            intent.putExtra("bundle",bundle)
            startActivity(intent)
        }
        //search
        beerBtn.setOnClickListener {
            var searchText : String = beerEdit.text.toString()

            for (i in 0 until storeArr.size) {
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
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    /*
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

        for(i in 0 until storeArr.size){
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
    }*/
}

class BeerView(val context: Context, val beerList: ArrayList<beer>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder : BeerViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem, null)
            holder = BeerViewHolder()

            holder.callView = view.findViewById(R.id.list_call)     //가게 전화번호
            holder.addressView = view.findViewById(R.id.list_address)       //가게 주소
            holder.titleView = view.findViewById(R.id.list_title)       //가게에 대한 간략한 설명
            holder.picView = view.findViewById(R.id.list_picture)        //가게사진
            holder.nameView = view.findViewById(R.id.list_name)         //가게 이름

            view.tag = holder
        } else {
            holder = convertView.tag as BeerViewHolder
            view = convertView
        }

        val beer = beerList[position]
        holder.callView?.text = beer.beerCall
        holder.addressView?.text = beer.beerPlace
        holder.titleView?.text = beer.beerIntroduce
        holder.nameView?.text = beer.beerName
        holder.picView?.setImageResource(beerImageArray[position])

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

    open class BeerViewHolder {
        var callView: TextView? = null
        var addressView: TextView? = null
        var titleView: TextView? = null
        var nameView: TextView? = null
        var picView: ImageView? = null
    }
}