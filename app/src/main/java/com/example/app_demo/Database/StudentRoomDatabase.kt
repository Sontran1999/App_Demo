package com.example.app_demo.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app_demo.Object.Student


@Database(entities = [Student::class], version = 1)

abstract class StudentRoomDatabase : RoomDatabase() {

    abstract fun studentDao():Dao

    companion object {
        private var INSTANCE: StudentRoomDatabase?= null
        private val DB_NAME = "student_db"

        fun getDatabase(context: Context): StudentRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentRoomDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}