package com.example.kpumap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_slide_acitivity.*
import org.w3c.dom.Text
import uk.co.senab.photoview.PhotoViewAttacher
import java.lang.Exception


//받아올거 text이름,인덱스,비트맵 사진
class SlideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_acitivity)
        //데이터 받아오기
        val bundle = intent.getBundleExtra("bundle")
        val stName = bundle.getSerializable("SelectedStore") as String
        val selectedIndex = bundle.getSerializable("SelectedIndex") as Int
        Log.d("myTag","activity is started")
        left.bringToFront()
        right.bringToFront()
        selectedText.text = stName

        val pager : ViewPager = findViewById(R.id.pager)
        val customAdapter : CustomSliderView = CustomSliderView(layoutInflater, bitmapArray)
        pager.adapter = customAdapter
        pager.currentItem = selectedIndex       //내가 원하는 위치에서 실행되도록

        //백버튼
        back.setOnClickListener {
            onBackPressed()
            //finish()
        }
        //
        left.setOnClickListener {
            var pos = pager.currentItem
            pager.setCurrentItem(pos-1,true)
        }
        right.setOnClickListener {
            var pos = pager.currentItem
            pager.setCurrentItem(pos+1,true)
        }

    }

}
class CustomSliderView(var inflater: LayoutInflater,var bitmapArr : ArrayList<Bitmap>) : PagerAdapter(){

    //스크롤을 통해 보여지는 view 생성
    //1. ViewPager 2.viewPager가 보여줄 view의 위치
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view : View? = null
        view = inflater.inflate(R.layout.viewpager,null)
        var viewPagerImage : ImageView = view.findViewById(R.id.selectedImage)
        viewPagerImage.setImageBitmap(bitmapArr[position])
        var mAttacher = PhotoViewAttacher(viewPagerImage)
        mAttacher.setZoomable(true)
        mAttacher.update()
        container.addView(view)

        return view
    }
    //리턴된 object가 view인지 확인
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun getCount(): Int {
        return bitmapArr.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}
