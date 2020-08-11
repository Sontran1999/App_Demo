package com.example.app_demo.Adapter

import android.app.Dialog
import android.bluetooth.BluetoothClass
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.app_demo.Database.StudentRoomDatabase
import com.example.app_demo.MainActivity
import com.example.app_demo.NewStudentActivity
import com.example.app_demo.Object.Student
import com.example.app_demo.R
import kotlinx.android.synthetic.main.custom_dialog_view.*
import kotlinx.android.synthetic.main.student_item.*
import kotlinx.android.synthetic.main.student_item.tv_id
import kotlinx.android.synthetic.main.student_item.tv_name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import kotlin.coroutines.CoroutineContext


class StudentListAdapter(var mContext: Context, val studentDB: StudentRoomDatabase) :
    RecyclerView.Adapter<StudentListAdapter.ViewHoder>(), CoroutineScope {
    private var mStudent: MutableList<Student> = arrayListOf()

    class ViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: TextView = itemView.findViewById(R.id.tv_id)
        var name: TextView = itemView.findViewById(R.id.tv_name)
        var sex: TextView = itemView.findViewById(R.id.tv_sex)
        var avatar: ImageView = itemView.findViewById(R.id.img_avatar)
        var delete: ImageButton = itemView.findViewById(R.id.btn_delete)
        var edit: ImageButton = itemView.findViewById(R.id.btn_edit)
    }

    override fun getItemCount(): Int {
        return mStudent.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val studentView: View = layoutInflater.inflate(R.layout.student_item, parent, false)
        var viewHoder: ViewHoder = ViewHoder(studentView)
        return viewHoder
    }

    override fun onBindViewHolder(holder: ViewHoder, position: Int) {
        holder.id.text = mStudent[position].id
        holder.name.text = mStudent[position].name
        holder.sex.text = mStudent[position].sex
        holder.avatar.setImageBitmap(byteArrayToBitmap(mStudent[position].image))
        holder.delete.setOnClickListener {
            launch {
                studentDB.studentDao()?.delete(mStudent[position])
                var list = studentDB.studentDao()?.getAllStudent() as MutableList
                setList(list)
            }
        }
        holder.avatar.setOnClickListener {
            byteArrayToBitmap(mStudent[position].image)?.let { it1 ->
                dialogInfor(
                    mStudent[position].id,
                    mStudent[position].name,
                    mStudent[position].date,
                    mStudent[position].sex,
                    mStudent[position].address,
                    mStudent[position].major,
                    it1
                )
            }
        }
        holder.edit.setOnClickListener{
            var bundle: Bundle = Bundle()
            bundle.putSerializable("object",mStudent[position])
            bundle.putInt("key2",1)
            val intent: Intent = Intent(it.context, NewStudentActivity::class.java)
            intent.putExtra("data",bundle)
            it.context.startActivity(intent)
        }
    }

    fun setList(list: MutableList<Student>) {
        this.mStudent = list
        notifyDataSetChanged()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        val arrayInputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(arrayInputStream)
    }

    fun dialogInfor(
        id: String,
        name: String,
        date: String,
        sex: String,
        address: String,
        major: String,
        image: Bitmap
    ) {
        var dialog = Dialog(mContext)
        dialog.setContentView(R.layout.custom_dialog_view)
        dialog.window?.setLayout(800, 1100)
        dialog.tv_id.text = id
        dialog.tv_name.text = name
        dialog.tv_date.text = date
        dialog.tv_sex2.text = sex
        dialog.tv_address.text = address
        dialog.tv_major.text = major
        dialog.img_Picture.setImageBitmap(image)
        dialog.show()
    }
}