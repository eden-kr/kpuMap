package com.example.kpumap

import android.Manifest.permission.*
import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.drawToBitmap
import uk.co.senab.photoview.PhotoViewAttacher
import java.io.*
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.function.BinaryOperator
import java.util.jar.Manifest
import javax.net.ssl.HttpsURLConnection
var bitmapArray = arrayListOf<Bitmap>()

//이미지뷰에 이미지 설정
class ImageListActivity : AppCompatActivity() {

    var imgArr = arrayListOf<ImageResource>()

    private fun LoadDB() {
        val helper = DbAdapter(this)
        helper.createDatabase()       //db생성
        helper.open()         //db복사

        imgArr = helper.GetImageResource()    //레스토랑
        helper.close()  //닫기
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_list)

        try {
            LoadDB()
        } catch (e: IndexOutOfBoundsException) {
            throw IndexOutOfBoundsException()
        }
        val bundle = intent.getBundleExtra("bd")
        var strName =
            bundle.getSerializable("storeName") as String        //어느 가게의 이미지가 클릭되었는지 확인하는 가게명

        //툴바 설정
        val toolbar: Toolbar = findViewById(R.id.imgListToolbar)
        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   //툴바에 홈 추가
        supportActionBar?.setHomeAsUpIndicator(R.drawable.backbutton)   //백버튼 설정
        supportActionBar?.title = strName

        //동적 그리드뷰 생성
        var grdView: GridView = findViewById(R.id.grdView)
        bitmapArray = ToBitmapArray(imgArr, strName)
        var imgGridList = ImageGridList(this, bitmapArray, strName)
        grdView.adapter = imgGridList

        //리스너 달아주기
        grdView.setOnItemClickListener { adapterView, view, position, id ->
            val bundle = Bundle()
            bundle.putSerializable("SelectedIndex", position)
            bundle.putSerializable("SelectedStore", strName)
            val intent = Intent(this, SlideActivity::class.java)
            intent.putExtra("bundle", bundle)
            startActivity(intent)
        }
    }

    //뒤로가기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //비트맵배열 만들기
    fun ToBitmapArray(imgArr: ArrayList<ImageResource>, strName: String): ArrayList<Bitmap> {
        var bitmapArray = arrayListOf<Bitmap>()
        for (i in 0 until imgArr.size) {
            if (imgArr[i].imgRssName == strName) {
                getBitmapImage(imgArr[i].imgRss)?.let { bitmapArray.add(it) }
            }
        }
        return bitmapArray
    }

    //sqlite내 저장된 url -> Bitmap
    //안드로이드에서 네트워크 관련 작업 시 별도의 스레드를 생성
    fun getBitmapImage(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        val th = Thread {
            var inputStream: InputStream? = null
            try {
                var imgUrl = URL(url)
                //웹에서 이미지 가져오기
                var con: HttpURLConnection = imgUrl.openConnection() as HttpURLConnection
                con.setRequestProperty(
                    "User-Agent",
                    "User-Agent :Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36"
                )
                con.doInput = true
                con.connect()   //연결

                //inputStream -> Bitmap
                Log.d("myTag", " 번째 응답코드 ${con.responseCode}")       //403

                if (con.responseCode == 200) {
                    inputStream = con.inputStream
                    bitmap = BitmapFactory.decodeStream(inputStream)
                }

            } catch (e: MalformedURLException) {
                throw MalformedURLException()
            } finally {
                inputStream?.close()
            }
        }
        th.start()

        try {
            th.join()
            th.interrupt()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return bitmap
    }
}

//인텐트로 전달받은 가게명과 비교해서 같으면 비트맵 변환 후
//비트맵 이미지를 이미지뷰에 붙이고
//그리드뷰 동적생성
//그리드뷰에 붙이기
//1. this, 2.url주소 들어있는 리스트 3.클릭된 가게위치

//타이틀과 맞는 이미지 url만 걸러내서 넣을 수 있도록
//인덱스? 타이틀?
class ImageGridList(
    var context: Context,
    var bitmapArr: ArrayList<Bitmap>,
    var strName: String
) : BaseAdapter() {

    override fun getView(postion: Int, counterView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ImageListViewHolder

        if (counterView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.holder_image, null)
            holder = ImageListViewHolder()
            holder.imgViewHolder = view.findViewById(R.id.holderImage)

            view.tag = holder
        } else {
            holder = counterView.tag as ImageListViewHolder
            view = counterView
        }

        holder.imgViewHolder?.setPadding(2, 2, 2, 2)
        holder.imgViewHolder?.scaleType = ImageView.ScaleType.FIT_XY

        //비트맵 이미지 설정
        holder.imgViewHolder?.setImageBitmap(bitmapArr[postion])

        /*
        holder.imgViewHolder?.setOnClickListener {

            var dialog : View = View.inflate(this.context,R.layout.dialog,null)
            var dlg = AlertDialog.Builder(this.context)
            var iv : ImageView = dialog.findViewById(R.id.dialogImg)
            iv.setImageBitmap(bitmapArr[postion])
            var mAttacher = PhotoViewAttacher(iv)
            mAttacher.setZoomable(true)
            mAttacher.update()
            dlg.setView(dialog)
            dlg.setNegativeButton("close",null)
            dlg.show()
        }*/

        return view
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return bitmapArr.size
    }

    open class ImageListViewHolder {
        var imgViewHolder: ImageView? = null
    }
}