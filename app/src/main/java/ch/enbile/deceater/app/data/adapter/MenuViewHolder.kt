package ch.enbile.deceater.app.data.adapter

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuViewHolder(
    parent: View?,
    var menuName: TextView,
    var dislike: Button,
    var delete: Button
) :
    RecyclerView.ViewHolder(parent!!)
