package com.example.hocdiffutilslistadapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hocdiffutilslistadapter.adapter.AdapterUser
import com.example.hocdiffutilslistadapter.adapter.CallbackEditText
import com.example.hocdiffutilslistadapter.model.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    CallbackEditText {

    private var listUser: MutableList<User> = createData() as MutableList<User>

    @SuppressLint("SetTextI18n")
    override fun sendData(user: User) {
        edtInputId.setText(user.id.toString())
        edtInputEmail.setText(user.email)
        btnUpdate.setOnClickListener {
            val user1 = User(edtInputId.text.toString().toInt(), edtInputEmail.text.toString())
            adapterUser.updateData(updateData(user1))
            recyclerView.scrollToPosition(adapterUser.itemCount - 1)
            edtInputEmail.setText("")
            edtInputId.setText("")
        }
    }


    override fun sendList(list: List<User>) {
        listUser.clear()
        listUser.addAll(list)
    }

    private fun updateData(checkUser: User): MutableList<User> {
        val list: MutableList<User> = mutableListOf()
        for (user in this.listUser) {
            if (user.id == checkUser.id) {
                val newUser = User(
                    id = checkUser.id,
                    email = checkUser.email
                )
                list.add(newUser)
            } else {
                list.add(user)
            }
        }
        return list
    }


    private fun createData(): List<User> {
        val user1 = User(1, "Cuong")
        val user2 = User(2, "Duong")
        val user3 = User(3, "Hoa")
        val user4 = User(4, "Kieu")
        val user11 = User(5, "Cuong")
        val user22 = User(6, "Duong")
        val user33 = User(7, "Hoa")
        val user44 = User(8, "Kieu")
        val user111 = User(9, "Cuong")
        val user222 = User(10, "Duong")
        val user333 = User(11, "Hoa")
        val user444 = User(12, "Kieu")

        return mutableListOf(
            user1,
            user2,
            user3,
            user11,
            user22,
            user33,
            user4,
            user44,
            user111,
            user222,
            user333,
            user444
        )
    }

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterUser: AdapterUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapterUser = AdapterUser(
            listUser,
            this,
            this
        )
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView = findViewById(R.id.recycleView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapterUser
        btnAdd.setOnClickListener {
            val newUser = User(
                edtInputId.text.toString().toInt(), edtInputEmail.text.toString()
            )
            val newList = arrayListOf<User>()
            newList.add(newUser)
            adapterUser.insertData(newList)
            recyclerView.scrollToPosition(adapterUser.itemCount - 1)
        }

        btnChange.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

    }


}

