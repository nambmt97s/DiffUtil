package com.example.hocdiffutilslistadapter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hocdiffutilslistadapter.diffutil.DiffUtilRating
import com.example.hocdiffutilslistadapter.R
import com.example.hocdiffutilslistadapter.model.Rating

class AdapterRating(val listRating: MutableList<Rating>, val context: Context, val handle: Handle) :
    RecyclerView.Adapter<AdapterRating.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.customview_danhgia, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return listRating.size
    }

    fun insertData(newList: List<Rating>) {
        val diffUtilRating = DiffUtilRating(listRating, newList)
        listRating.addAll(newList)
        val diffResult = DiffUtil.calculateDiff(diffUtilRating)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateData(newList: List<Rating>) {
        val diffUtilRating = DiffUtilRating(listRating, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtilRating)
        diffResult.dispatchUpdatesTo(this)
        handle.sendList(newList)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rating = listRating[position]
        holder.txtContent.text = rating.noidung
        holder.txtDate.text = rating.ngayDanhGia
        holder.txtRate.text = rating.sao.toString() + ""
        holder.txtPosition.text = "Đánh giá $positioń"
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            context, android.R.layout.simple_spinner_item,
            context.resources.getStringArray(R.array.option)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        holder.spinner2.adapter = adapter
        holder.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position) {
                    1 -> {
                        handle.editRating(rating)
                    }
                    2 -> {

                    }
                }
            }

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtRate = itemView.findViewById<TextView>(R.id.txtRate)
        var txtDate = itemView.findViewById<TextView>(R.id.txtDateRating)
        var txtContent = itemView.findViewById<TextView>(R.id.txtContenRating)
        var txtPosition = itemView.findViewById<TextView>(R.id.txtPositionRating)
        var spinner2 = itemView.findViewById<Spinner>(R.id.spinner2)

    }
}

interface Handle {
    fun editRating(rating: Rating)
    fun sendList(list: List<Rating>)
}