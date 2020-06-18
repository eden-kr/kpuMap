package com.example.kpumap

import android.content.Context
import android.content.res.AssetManager
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.NullPointerException


class DBHelper : SQLiteOpenHelper {

    //static
    companion object {
        private var DB_PATH: String = "";    //asset내 있으면 공백
        private var DB_NAME: String = "databaseKpu.db"       //SQLite명
        private var mContext: Context? = null
        private var mDatabase: SQLiteDatabase? = null
    }

    //생성자
    constructor(context: Context) : super(context, DB_NAME, null, 1) {
        DB_PATH = if (android.os.Build.VERSION.SDK_INT >= 17) {
            context.applicationInfo.dataDir + "/databases/";
        } else {
            "/data/data/" + context.packageName + "/databases/";
        }
        mContext = context
    }

    //경로에 파일 존재하는지 체크
    private fun checkDataBase(): Boolean {
        val dbFile = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    @Throws(IOException::class)
    fun CreateDB() {
        var assetManager: AssetManager = mContext!!.resources.assets
        var myDbExist = checkDataBase()

        //asset에서 파일 받아오기
        if (!myDbExist) {
            this.readableDatabase
            this.close()

            try {
                var mInput: InputStream =
                    assetManager.open("databaseKpu.db", AssetManager.ACCESS_BUFFER)
                // var mInput: InputStream = mContext!!.assets.open(DB_NAME)
                var fileName = DB_PATH + DB_NAME
                var mOutPut = FileOutputStream(fileName)
                val buffer = ByteArray(5000)
                var length: Int

                do {
                    length = mInput.read(buffer)
                    mOutPut.write(buffer, 0, length)
                } while (length > 0)


                mOutPut.flush()
                mOutPut.close()
                mInput.close()
            } catch (e: IOException) {
                throw IOException()
            }
        }
            Log.d("myTag","db is created")
    }

    //데이터베이스 파일 오픈
    @Throws(SQLException::class)
    fun OpenDB(): Boolean {
        val mPath = DB_PATH + DB_NAME
        mDatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS)
        return mDatabase != null
    }

    //데이터베이스 파일 닫기
    @Synchronized
    override fun close(): Unit {
        if (mDatabase != null) mDatabase!!.close()
        super.close()
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.disableWriteAheadLogging()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //TODO: implement later

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO: implement later
    }
}