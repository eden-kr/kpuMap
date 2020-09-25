package com.example.kpumap

import android.app.Dialog
import android.content.Intent
import android.media.Image
import android.media.Rating
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmBaseAdapter
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.dialog.*
import kotlinx.android.synthetic.main.dialog_list.*
import org.w3c.dom.Text
import uk.co.senab.photoview.PhotoViewAttacher
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

//realm -> firebase로 수정하기
class ReviewActivity : AppCompatActivity() {
    val realm = Realm.getDefaultInstance()

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    //가게명마다 다르게 보이게 하기
    //가게명 번들로 받아온 후 리스트에서 걸러서 보이게 하면 됌
    //store도 받아와서 가게명 if 쓴 후 나머지만 카운트하면 됌
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        val bundle = intent.getBundleExtra("bundle")
        var storeName = bundle.getSerializable("storeName") as String

        Log.d("realm", "${realm.where(User::class.java).findAll()}")

        //툴바설정
        val reviewToolbar: Toolbar = findViewById(R.id.reviewToolbar)
        this.setSupportActionBar(reviewToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //툴바에 홈 추가
        supportActionBar?.setHomeAsUpIndicator(R.drawable.backbutton)   //백버튼 설정

        //리스트뷰 생성
        val myReview = realm.where(User::class.java).findAll()
        val reviewListView: ListView = findViewById(R.id.reviewList)
        reviewListView.adapter = ReviewAdapter(myReview, storeName)

        //데이터 변경 시마다 변경내용 적용
        myReview.addChangeListener { _ -> reviewListView.deferNotifyDataSetChanged() }

        //리뷰 클릭 시
        reviewListView.setOnItemClickListener { parent, view, postion, id ->
            var clickedDialog: View = View.inflate(this, R.layout.dialog_clicked, null)
            var dlg = AlertDialog.Builder(this)
            var id: TextView = clickedDialog.findViewById(R.id.clickedId)
            var date: TextView = clickedDialog.findViewById(R.id.date_clicked)
            var review: TextView = clickedDialog.findViewById(R.id.review_clicked)
            var rate: RatingBar = clickedDialog.findViewById(R.id.rating_clicked)
            dlg.setView(clickedDialog)
            id.text = myReview[postion].userId
            date.text = myReview[postion].date
            review.text = myReview[postion].review
            rate.rating = myReview[postion].rating.toFloat()
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }

        /*
        delete.setOnClickListener {
                val dialog: View = View.inflate(this, R.layout.delete_dialog, null)
                var dlg = AlertDialog.Builder(this)
                var ps: EditText = dialog.findViewById(R.id.deleteText)
                var password = ps.text.toString()
                dlg.setView(dialog)

                dlg.setPositiveButton("확인"){dialogInterface, i ->
                    for(i in 0 until myReview.size){
                    if(myReview[i].password==password) {
                        var check =
                            realm.where(User::class.java).equalTo("userId", myReview[i].id)
                                .findFirst()
                        check.deleteFromRealm()
                        realm.commitTransaction()
                        Log.d("realm", "data is deleted")
                    }
                    }
                }.setNegativeButton("취소",null)
                dlg.show()
            }*/


        //리뷰 작성
        edit_review.setOnClickListener {
            var dialog: View = View.inflate(this, R.layout.dialog, null)
            var dlg = AlertDialog.Builder(this)
            var id: EditText = dialog.findViewById(R.id.userID)       //아이디
            var ps: EditText = dialog.findViewById(R.id.userPs)       //비번
            var r: RatingBar = dialog.findViewById(R.id.userRating)        //별점
            var review: EditText = dialog.findViewById(R.id.userReview)    //리뷰
            dlg.setView(dialog)
            r.setOnRatingBarChangeListener { ratingBar, fl, b ->
            }
            dlg.setPositiveButton("확인") { dialogInterface, i ->
                realm.beginTransaction()
                var check =
                    realm.where(User::class.java).equalTo("userId", id.text.toString())
                        .findFirst()

                if (check == null) {
                        var dt = LocalDate.now()
                        var now = dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                        val user = realm.createObject(User::class.java, nextId())
                        user.userId = id.text.toString()
                        user.password = ps.text.toString()
                        user.review = review.text.toString()
                        user.rating = r.numStars.toDouble()
                        user.date = now
                        user.stName = storeName
                        realm.commitTransaction()
                        Log.d("realm", "리뷰가 등록되었습니다.")
                        realm.isAutoRefresh = true
                        Toast.makeText(parent, "리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                }
            }
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
    }

    private fun nextId(): Int {
        val maxId = realm.where(User::class.java).max("id")
        if (maxId != null) {
            return maxId.toInt() + 1
        }
        return 0
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}

class ReviewAdapter(realmResult: OrderedRealmCollection<User>, var storeName: String) :
    RealmBaseAdapter<User>(realmResult) {
    override fun getView(position: Int, counterView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ReviewHolder
        val realm = Realm.getDefaultInstance()


        if (counterView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.dialog_list, null)
            holder = ReviewHolder()
            holder.idList = view.findViewById(R.id.id_list)
            holder.date = view.findViewById(R.id.date_list)
            holder.review = view.findViewById(R.id.review_list)
            holder.ratingBar = view.findViewById(R.id.rating_list)
            var delete: ImageView = view.findViewById(R.id.delete_clicked)

            delete.setOnClickListener {
                val dialog: View = View.inflate(parent?.context, R.layout.delete_dialog, null)
                var dlg = parent?.context?.let { it1 -> AlertDialog.Builder(it1) }
                var ps: EditText = dialog.findViewById(R.id.deleteText)
                dlg?.setView(dialog)
                val user = adapterData!![position]

                dlg?.setPositiveButton("확인") { dialogInterface, i ->
                    if (user.password == ps.text.toString()) {
                        Log.d("realm", "진입 성공")
                        realm.beginTransaction()
                        var check =
                            realm.where(User::class.java).equalTo("userId", user.userId)
                                .findFirst()
                        check.deleteFromRealm()
                        realm.commitTransaction()
                        realm.isAutoRefresh = true
                        Log.d("realm", "data is deleted")
                    } else {
                        Toast.makeText(parent?.context, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                dlg?.setNegativeButton("취소", null)
                dlg?.show()
            }

            view.tag = holder
        } else {
            holder = counterView.tag as ReviewHolder
            view = counterView
        }

        if (adapterData != null) {
            val user = adapterData!![position]
            if (user.stName == storeName) {
                holder.idList?.text = user.userId
                holder.review?.text = user.review
                holder.date?.text = user.date
                holder.ratingBar?.rating = user.rating.toFloat()
            } else {
            }
        }

        return view
    }

    override fun getItem(position: Int): User? {
        if (adapterData != null) {
            return adapterData!![position]
        }
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getCount(): Int {
        return super.getCount()
    }

    open class ReviewHolder {
        var ratingBar: RatingBar? = null
        var idList: TextView? = null
        var review: TextView? = null
        var date: TextView? = null
    }
}
