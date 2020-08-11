package com.example.app_demo

import android.app.ActionBar
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_demo.Adapter.StudentListAdapter
import com.example.app_demo.Database.StudentRoomDatabase
import com.example.app_demo.Object.Student
import kotlinx.android.synthetic.main.activity_new_student.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext

class NewStudentActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope {
    var calendar: Calendar = Calendar.getInstance()
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH)
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var mStudentDB: StudentRoomDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_student)
        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showView()

        btn_date.setOnClickListener(this)
        mStudentDB = StudentRoomDatabase.getDatabase(this)
        btn_save.setOnClickListener(this)
        btnChoose.setOnClickListener(this)
    }

    private fun choosePicture() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, 200)
    }//one can be replaced with any action code

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK) {
            try {
                //xử lý lấy ảnh chọn từ điện thoại:
                val imageUri = data?.data
                var selectedBitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                imgPicture.setImageBitmap(selectedBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun showView() {
        var bundle = intent.getBundleExtra("data")
        if (bundle != null) {
            var key2: Int? = bundle.getInt("key2")
            if (key2 == 1) {
                edt_id.isEnabled = false
                if (bundle != null) {
                    var student = bundle.getSerializable("object") as Student
                    edt_id.setText(student.id)
                    edt_name.setText(student.name)
                    edt_date.setText(student.date)
                    if (student.sex.equals("male")) {
                        rd_male.isChecked = true
                    } else {
                        rd_female.isChecked = true
                    }
                    edt_address.setText(student.address)
                    edt_major.setText(student.major)
                    imgPicture.setImageBitmap(byteArrayToBitmap(student.image))
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_date -> {
                var date = DatePickerDialog(
                    this,
                    AlertDialog.THEME_HOLO_DARK,
                    DatePickerDialog.OnDateSetListener { p0, year, month, day ->
                        edt_date.setText("" + day + "/" + (month + 1) + "/" + year)
                    },
                    year,
                    month,
                    day
                )
                date.show()
            }
            R.id.btn_save -> {
                save()
            }
            R.id.btnChoose -> {
                choosePicture()
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private fun imageToBitmap(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        Log.d("aaa", stream.toByteArray().toString())
        return stream.toByteArray()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun save() {
        var id = edt_id.text.toString()
        var name = edt_name.text.toString()
        var date = edt_date.text.toString()
        var sex = if (rd_male.isChecked) "male" else "female"
        var address = edt_address.text.toString()
        var major = edt_major.text.toString()
        var image = imageToBitmap(imgPicture)
//        var list: MutableList<Student>? = mStudentDB?.studentDao()?.findStudentById(id = id) as MutableList<Student>?
//        list?.forEachIndexed { index, student ->
//            if(student.id.equals(id)){
//                Toast.makeText(this, "Id đã tồn tại", Toast.LENGTH_SHORT).show()
//            }
//        }
        if (id.equals("")) {
            Toast.makeText(this, "Không được để trống Id", Toast.LENGTH_SHORT).show()
        } else if (name.equals("")) {
            Toast.makeText(this, "Không được để trống Name", Toast.LENGTH_SHORT).show()
        } else if (date.equals("")) {
            Toast.makeText(this, "Không được để trống Date", Toast.LENGTH_SHORT).show()
        } else if (address.equals("")) {
            Toast.makeText(this, "Không được để trống Address", Toast.LENGTH_SHORT).show()
        } else if (major.equals("")) {
            Toast.makeText(this, "Không được để trống Major", Toast.LENGTH_SHORT).show()
        } else {
            var bundle = intent.getBundleExtra("data")
            if (bundle != null) {
                launch {
                    mStudentDB?.studentDao()?.update(
                        id = id,
                        name = name,
                        date = date,
                        sex = sex,
                        address = address,
                        major = major,
                        image = image
                    )
                    finish()
                }
            } else {
                launch {
                    mStudentDB?.studentDao()?.insert(
                        Student(
                            id = id,
                            name = name,
                            date = date,
                            sex = sex,
                            address = address,
                            major = major,
                            image = image
                        )
                    )
                    finish()
                }
            }
        }

    }
    fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        val arrayInputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(arrayInputStream)
    }
}