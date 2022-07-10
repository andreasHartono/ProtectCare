package com.ubaya.protectcare04

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_history.view.*
import kotlinx.android.synthetic.main.history_card.view.*
import java.text.SimpleDateFormat

class HistoryAdapter(val history:ArrayList<History>):RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(val view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.history_card, parent, false)
        return HistoryViewHolder(view)

    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val arrHistory = history[position]
        with(holder.view){
            var ciFormat = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(arrHistory.checkIn)
            var coFormat = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(arrHistory.checkOut)
            var dateFormat = SimpleDateFormat("EEEE, yyyy-MM-dd HH:mm")
            textHistoryPlace.text = arrHistory.name
            txtHistoryCheckIn.text = dateFormat.format(ciFormat.time).toString()
            txtHistoryCheckOut.text = arrHistory.checkOut
            if(txtHistoryCheckOut.text == "0000-00-00 00:00:00")
            {
                txtHistoryCheckOut.text = "Not Yet Checked Out"
            }
            else
            {
                txtHistoryCheckOut.text = dateFormat.format(coFormat.time).toString()
            }
            if(arrHistory.vaccine ==1)
            {
                cardVHistory.setCardBackgroundColor(Color.YELLOW)
            }
            else if(arrHistory.vaccine == 2)
            {
                cardVHistory.setCardBackgroundColor(Color.GREEN)
            }
        }
    }

    override fun getItemCount() = history.size
}