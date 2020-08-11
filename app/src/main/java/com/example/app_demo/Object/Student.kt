package com.example.app_demo.Object

import android.graphics.Bitmap
import androidx.room.*
import java.io.Serializable


@Entity(tableName = "Student")
class Student(
    @PrimaryKey(autoGenerate = true) val number: Int = 0,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "sex") val sex: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "major") val major: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray
) : Serializable


@Entity(indices = [Index(value = ["first_name", "last_name"], unique = true)])
class User {
    @PrimaryKey
    var id = 0

    @ColumnInfo(name = "first_name")
    var firstName: String? = null

    @ColumnInfo(name = "last_name")
    var lastName: String? = null

    @Ignore
    var picture: Bitmap? = null
}