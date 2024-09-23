package com.bs.sriwilis.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bs.sriwilis.data.repository.modelhelper.CardNasabah

class UserSpinnerAdapter(context: Context, private val users: List<CardNasabah>) :
    ArrayAdapter<CardNasabah>(context, android.R.layout.simple_spinner_item, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        (view as TextView).text = users[position].nama_nasabah  // Display the name in the spinner
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        (view as TextView).text = users[position].nama_nasabah  // Display the name in the dropdown
        return view
    }

    fun getPhone(position: Int): String {
        return users[position].no_hp_nasabah  // Access the phone number when needed
    }
}
