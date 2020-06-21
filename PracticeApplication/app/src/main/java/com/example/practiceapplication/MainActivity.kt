package com.example.practiceapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance()
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arr = arrayListOf<store>()
        arr.add(store("김예하"))
        arr.add(store("백경돈"))
        arr.add(store("예하"))
        arr.add(store("경돈"))
        arr.add(store("유령"))



        btn.setOnClickListener {
            var pos = 0
            btn.isSelected = !btn.isSelected
            if(btn.isSelected){
                txt1.text = arr[pos].name
                Insert()
                pos++
                Log.d("myTag","${realm.where(dog::class.java).findAll()}")
            }else{
                var txt = txt1.text.toString()
                pos--
                txt1.text = arr[pos].name
                Delete(txt)
                Log.d("myTag","${realm.where(dog::class.java).findAll()}")
            }
        }

    }
    private fun Insert(){
        realm.beginTransaction()
        val dg = realm.createObject(dog::class.java,nextId())
        dg.name = txt1.text.toString()
        realm.commitTransaction()
        realm.close()
    }
    private fun nextId():Int{
        //저장된 가장 큰 max 값의 기본키를 찾고 다음 id 반환
        val maxId = realm.where(dog::class.java).max("id")
        if(maxId != null){
            return maxId.toInt()+1
        }
        return 0
    }
    private fun Delete(name : String){
        realm.beginTransaction()
        val ddog = realm.where<dog>(dog::class.java).equalTo("name",name).findFirst()!!
        ddog.deleteFromRealm()
        realm.commitTransaction()
        realm.close()
    }

}
