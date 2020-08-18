package com.example.hocdiffutilslistadapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.example.hocdiffutilslistadapter.model.Rating

class DiffUtilRating(val oldList: List<Rating>, val newList: List<Rating>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idPhong == newList[newItemPosition].idPhong
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }


}