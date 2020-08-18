package com.example.hocdiffutilslistadapter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hocdiffutilslistadapter.R
import com.example.hocdiffutilslistadapter.diffutil.DiffUtilUser
import com.example.hocdiffutilslistadapter.model.User
import kotlinx.android.synthetic.main.customview.view.*

class AdapterUser(
    private var list: MutableList<User>,
    val context: Context,
    val callbackEditText: CallbackEditText
) :
    RecyclerView.Adapter<AdapterUser.ViewHolder>() {

    fun insertData(newList: List<User>) {
        val diffCallBack = DiffUtilUser(list, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallBack)
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateData(newList: List<User>) {
        val diffCallBack = DiffUtilUser(list, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallBack)
        diffResult.dispatchUpdatesTo(this)
        callbackEditText.sendList(newList)
    }

    fun deleteData(user: User) {
        val removeList: MutableList<User> = mutableListOf()
        removeList.addAll(list)
        removeList.remove(user)
        val diffCallBack = DiffUtilUser(list, removeList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallBack)
        diffResult.dispatchUpdatesTo(this)
        callbackEditText.sendList(removeList)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtID: TextView = itemView.txtID
        val txtEmail: TextView = itemView.txtEmail
        val spinner: Spinner = itemView.spinner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customview, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount() = list.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = list[position]
        holder.txtID.text = user.id.toString() + ""
        holder.txtEmail.text = user.email
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            context, android.R.layout.simple_spinner_item,
            context.resources.getStringArray(R.array.option)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        holder.spinner.adapter = adapter
        holder.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(context, "Khong chon", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                p3: Long
            ) {

                when (position) {
                    1 -> {
                        callbackEditText.sendData(user)
                    }
                    2 -> {
                        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                        deleteData(user)
                    }
                    0 -> {

                    }
                }
            }

        }
    }


}

interface CallbackEditText {
    fun sendData(user: User)
    fun sendList(list: List<User>)
}