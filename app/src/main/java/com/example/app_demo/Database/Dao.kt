package com.example.app_demo.Database

import androidx.room.*
import androidx.room.Dao
import com.example.app_demo.Object.Student

@Dao
interface Dao {

    @Query("SELECT * FROM Student ORDER BY id ASC")
    suspend fun getAllStudent(): List<Student>

    @Query("SELECT * FROM Student ORDER BY name ASC")
    suspend fun getAllStudentSortName1(): List<Student>

    @Query("SELECT * FROM Student ORDER BY name DESC")
    suspend fun getAllStudentSortName2(): List<Student>

    @Query("SELECT * FROM Student WHERE id like :id")
    suspend fun findStudentById(id: String): List<Student>?

    @Query("SELECT * FROM Student WHERE name LIKE :name")
    suspend fun findStudentByName(name: String): List<Student>?

    @Insert
    suspend fun insert(student: Student)

    @Delete
    suspend fun delete(student: Student)

    @Query("UPDATE Student SET name = :name, date = :date, sex = :sex, address = :address, major =:major, image = :image WHERE id = :id")
    suspend fun update(
        id: String,
        name: String,
        date: String,
        sex: String,
        address: String,
        major: String,
        image: ByteArray
    )
}