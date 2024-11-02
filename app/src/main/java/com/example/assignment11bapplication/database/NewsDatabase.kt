package com.example.assignment11bapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [DataBaseNews::class], version = 1, exportSchema = false)
abstract class NewsDatabase:RoomDatabase() {
    abstract fun userDao(): NewsDao
    companion object{
        @Volatile
        private var Instance: NewsDatabase?=null


        fun getDatabase(context: Context): NewsDatabase {
            val tempInstance= Instance
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news_database"
                ).build()
                Instance =instance
                return instance
            }
        }
    }

}