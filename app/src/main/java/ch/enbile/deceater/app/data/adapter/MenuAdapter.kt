package ch.enbile.deceater.app.data.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ch.enbile.deceater.app.R
import ch.enbile.deceater.app.data.model.Menu


class MenuAdapter : RecyclerView.Adapter<MenuViewHolder>() {
    private var menuList: ArrayList<Menu> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, vt: Int): MenuViewHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(
            R.layout.menu_list,
            parent,
            false
        )
        return MenuViewHolder(
            view,
            view.findViewById(R.id.list_item_string),
            view.findViewById(R.id.dislike_btn),
            view.findViewById(R.id.delete_btn)
        )
    }

    override fun onBindViewHolder(
        holder: MenuViewHolder,
        position: Int
    ) {
        val menu: Menu = menuList[position]
        holder.menuName.text = menu.displayName
        if(menu.disliked) {
            holder.menuName.paintFlags = holder.menuName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.menuName.paintFlags = holder.menuName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        val dislikeValue = if (!menu.disliked) "dislike" else "un-dislike"
        holder.dislike.text = dislikeValue
        holder.dislike.setOnClickListener {
            Toast.makeText(
                it.context,
                "${menu.displayName} was ${dislikeValue}d",
                Toast.LENGTH_LONG
            ).show()
            menu.disliked = !menu.disliked
            menuList[position] = menu
            updateMenues(menuList)
        }
        holder.delete.setOnClickListener {
            Toast.makeText(
                it.context,
                "${menu.displayName} was deleted",
                Toast.LENGTH_LONG
            ).show()
            menuList.remove(menu)
            updateMenues(menuList)
        }
    }

    fun updateMenues(menues: ArrayList<Menu>) {
        menuList = menues
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return menuList.size
    }
}