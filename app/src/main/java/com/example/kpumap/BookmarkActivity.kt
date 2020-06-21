package com.example.kpumap

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.kpumap.BeerActivity.Companion.storeArr
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmBaseAdapter
import kotlinx.android.synthetic.main.activity_bookmark.*
import java.lang.IndexOutOfBoundsException
import java.lang.NullPointerException

//저장목록 구현
class BookmarkActivity : AppCompatActivity() {
    val realm = Realm.getDefaultInstance()
    var storeArray = arrayListOf<store>()

    private fun LoadDB() {
        val helper = DbAdapter(this)
        helper.createDatabase()       //db생성
        helper.open()                 //db복사

        storeArray = helper.GetAllData()      //모든 객체 가져오기
        helper.close()  //닫기
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
        try {
            LoadDB()
        } catch (e: IndexOutOfBoundsException) {
            throw IndexOutOfBoundsException()
        }

        val myBookmarkList = realm.where(BookmarkStore::class.java).findAll()
        //리스트뷰
        val bookmarkView : ListView = findViewById(R.id.listView_bookmark)
        bookmarkView.adapter = BookmarkListAdapter(myBookmarkList)
        //데이터 변경 시 어댑터에 알림
        myBookmarkList.addChangeListener { _ -> bookmarkView.deferNotifyDataSetChanged()}

        bookmarkView.setOnItemClickListener { parent, view, position, id ->
            val bundle = Bundle()
            bundle.putSerializable("imageArr", storeImageArray)
            bundle.putSerializable("storeArr", storeArray)
            bundle.putSerializable("name",myBookmarkList[position].stName)
            Log.d("realm","이름  ---  ${myBookmarkList[position].stName}")
            bundle.putSerializable("phoneNum", myBookmarkList[position].stPhonNum)
            bundle.putSerializable("img",myBookmarkList[position].stImage)
            val intent = Intent(this,StoreActivity::class.java)
            intent.putExtra("bundle",bundle)
            startActivity(intent)
        }
        //백버튼
        back_bookmark.setOnClickListener {
            onBackPressed()
        }
    }
}

//인자로 arr을 안받아도 됌.
//RealmBaseAdapter 는 adapterData라는 array를 제공
class BookmarkListAdapter(realmResult : OrderedRealmCollection<BookmarkStore>) :
    RealmBaseAdapter<BookmarkStore>(realmResult) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        //convertView == null 화면을 최초로 실행할 때
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.listitem, parent,false)
            holder = ViewHolder()
            holder.callView = view.findViewById(R.id.list_call)     //가게 전화번호
            holder.addressView = view.findViewById(R.id.list_address)       //가게 주소
            holder.titleView = view.findViewById(R.id.list_title)       //가게에 대한 간략한 설명
            holder.picView = view.findViewById(R.id.list_picture)        //가게사진
            holder.nameView = view.findViewById(R.id.list_name)         //가게 이름

            //홀더에 각각의 아이디 설정 후
            view.tag = holder   //태그 변경
        } else {         //만들어진 뷰가 있으면 tag갱신
            holder = convertView.tag as ViewHolder
            view = convertView
        }
        //RealmBaseAdapter는 adapterData 프로퍼티 제공
        if(adapterData != null) {
            val store = adapterData!![position]
            holder.nameView?.text = store.stName
            holder.callView?.text = store.stPhonNum
            holder.addressView?.text = store.stAddress
            holder.titleView?.text = store.stIntro
            holder.picView?.setImageResource(store.stImage)
        }
        return view
    }

    override fun getItem(position: Int): BookmarkStore? {
        if (adapterData != null) {
            return adapterData!![position]
        }
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return adapterData!!.size
    }
    open class ViewHolder {
        var callView: TextView? = null
        var addressView: TextView? = null
        var titleView: TextView? = null
        var nameView: TextView? = null
        var picView: ImageView? = null
    }
}
