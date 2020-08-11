package com.example.app_demo

import android.annotation.TargetApi
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_demo.Adapter.StudentListAdapter
import com.example.app_demo.Database.StudentRoomDatabase
import com.example.app_demo.Object.Student
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope {
    private var mStudentDB: StudentRoomDatabase? = null
    var studentListAdapter: StudentListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setTitle("")

        mStudentDB = StudentRoomDatabase.getDatabase(this)
        recycler_student.setHasFixedSize(true)
        recycler_student.layoutManager = LinearLayoutManager(this)
        studentListAdapter = StudentListAdapter(this, mStudentDB!!)
        recycler_student.adapter = studentListAdapter


        btn_new.setOnClickListener(this)
    }


    override fun onResume() {
        super.onResume()
        getAllStudent()
    }

    fun getAllStudent() {
        launch {
            var list: MutableList<Student>? =
                mStudentDB?.studentDao()?.getAllStudent() as MutableList<Student>
            if (list != null) {
                studentListAdapter?.setList(list)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_new -> {
                val intent: Intent = Intent(this, NewStudentActivity::class.java)
                intent.putExtra("key1", 0)
                startActivity(intent)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        var searchItem = menu?.findItem(R.id.menu_search)
        var searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Enter id or name"
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                launch {
                    var findName =
                        mStudentDB?.studentDao()
                            ?.findStudentByName(query.toString()) as MutableList<Student>
                    if (findName.size != 0) {
                        studentListAdapter?.setList(findName)
                    } else {
                        var findId =
                            mStudentDB?.studentDao()
                                ?.findStudentById(query.toString()) as MutableList<Student>
                        if (findId.size != 0) {
                            studentListAdapter?.setList(findId)
                        } else {
                            AlertDialog.Builder(this@MainActivity).setTitle("No information")
                                .setMessage("The information you are looking for is not available")
                                .setNegativeButton(
                                    "OK ",
                                    DialogInterface.OnClickListener { dialogInterface, i ->
                                        dialogInterface.cancel()
                                    }).show()
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                launch {
                    if (newText.equals("")) {
                        getAllStudent()
                    }
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
                    R.id.menu_sort1 -> {
                        launch {
                            var list: MutableList<Student>? =
                                mStudentDB?.studentDao()
                                    ?.getAllStudentSortName1() as MutableList<Student>
                            if (list != null) {
                                studentListAdapter?.setList(list)
                            }
                        }
                        return true
                    }
                    R.id.menu_sort2 -> {
                        launch {
                            var list: MutableList<Student>? =
                                mStudentDB?.studentDao()
                                    ?.getAllStudentSortName2() as MutableList<Student>
                            if (list != null) {
                                studentListAdapter?.setList(list)
                            }
                        }
                        return true
                    }
                    R.id.menu_sort3 -> {
                        getAllStudent()
                        return true
                    }
        }
        return super.onOptionsItemSelected(item)
    }
}