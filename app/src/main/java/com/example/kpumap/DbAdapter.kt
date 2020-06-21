package com.example.kpumap

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.IOException
import java.lang.NullPointerException
import java.sql.SQLException


class DbAdapter {

    companion object {
        private var mContext: Context? = null
        private var myDbHelper: DBHelper? = null
        private var mDb: SQLiteDatabase? = null

    }

    constructor(context: Context) {
        mContext = context
        myDbHelper = DBHelper(mContext!!)
    }

    @Throws(SQLException::class)
    fun createDatabase(): DbAdapter? {
        try {
            myDbHelper?.CreateDB()
        } catch (mIOException: IOException) {
        }
        return this
    }

    @Throws(SQLException::class)
    fun open(): DbAdapter? {
        try {
            myDbHelper?.OpenDB()
            myDbHelper?.close()
            mDb = myDbHelper?.readableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }
        return this
    }

    fun close() {
        myDbHelper?.close()
    }

    //술집 데이터 가져오기
    fun GetBeerData(): ArrayList<beer> {
        var arr = arrayListOf<beer>()

        try {
            var c: Cursor = mDb!!.rawQuery("select * from BEER", null)
            c.moveToFirst()
            do {
                var beer = beer(
                    c.getInt(0), c.getString(1), c.getString(2),
                    c.getString(3), c.getString(4)
                )
                arr.add(beer)
            } while (c.moveToNext())
        } catch (e: NullPointerException) {
        }

        return arr
    }

    //맛집 데이터 가져오기
    fun GetResData(): ArrayList<restaurant> {
        var arr = arrayListOf<restaurant>()

        try {
            var c: Cursor = mDb!!.rawQuery("select * from RESTAURANT", null)
            c.moveToFirst()

            do {
                var res = restaurant(
                    c.getInt(0), c.getString(1), c.getString(2),
                    c.getString(3), c.getString(4)
                )

                arr.add(res)
            } while (c.moveToNext())
        } catch (e: NullPointerException) {
        }

        return arr
    }

    //카페 데이터 가져오기
    fun GetCafeData(): ArrayList<cafe> {

        var arr = arrayListOf<cafe>()

        try {
            var c: Cursor = mDb!!.rawQuery("select * from CAFE", null)
            c.moveToFirst()
            do {
                var cf = cafe(
                    c.getInt(0), c.getString(1), c.getString(2),
                    c.getString(3), c.getString(4)
                )

                arr.add(cf)
            } while (c.moveToNext())
        } catch (e: NullPointerException) {
        }

        return arr
    }
    fun GetAllData() : ArrayList<store>{
        var arr = arrayListOf<store>()
        try {
            var c : Cursor = mDb!!.rawQuery("select * from CAFE", null)
            var b : Cursor = mDb!!.rawQuery("select * from BEER", null)
            var r : Cursor = mDb!!.rawQuery("select * from RESTAURANT",null)
            //카페
            c.moveToFirst()
            do {
                var cf = store(
                    c.getString(1), c.getString(2),
                    c.getString(3), c.getString(4),
                    c.getString(5),c.getString(6),
                    c.getString(7).toFloat(),c.getString(8).toFloat()
                )
                arr.add(cf)
            } while (c.moveToNext())
            //술집
            b.moveToFirst()
            do {
                var beer = store(
                    b.getString(1), b.getString(2),
                    b.getString(3), b.getString(4),
                    b.getString(5),b.getString(6),
                    b.getString(7).toFloat(),b.getString(8).toFloat()
                )
                arr.add(beer)
            } while (b.moveToNext())
            //레스토랑
            r.moveToFirst()
            do {
                var res = store(
                    r.getString(4), r.getString(1),
                    r.getString(2), r.getString(3),
                    r.getString(5),r.getString(6),
                    r.getString(7).toFloat(),r.getString(8).toFloat()
                )
                arr.add(res)
            } while (r.moveToNext())
        } catch (e: NullPointerException) {
        }

        return arr
    }
    //카페 데이터 가져오기
    fun GetImageResource(): ArrayList<ImageResource> {

        var arr = arrayListOf<ImageResource>()

        try {
            var c : Cursor = mDb!!.rawQuery("select * from IMAGE", null)
            c.moveToFirst()
            do {
                var img = ImageResource(
                    c.getString(1),c.getString(2)
                )

                arr.add(img)
            } while (c.moveToNext())
        } catch (e: NullPointerException) {
        }

        return arr
    }
}
