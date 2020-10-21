package ch.enbile.deceater.app.data.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ch.enbile.deceater.app.MainActivity
import ch.enbile.deceater.app.R
import ch.enbile.deceater.app.data.MenuRepository
import ch.enbile.deceater.app.data.model.LoggedInUser
import ch.enbile.deceater.app.data.model.Menu
import ch.enbile.deceater.app.data.service.DailyMenuBroadcastReceiver
import ch.enbile.deceater.app.data.service.MenuNotificationService
import ch.enbile.deceater.app.ui.login.LoggedInUserView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MenuAdapter(private val activity: MainActivity, private val loggedInUser: LoggedInUserView, private val menuRepository: MenuRepository) : RecyclerView.Adapter<MenuViewHolder>() {
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
        holder.menuName.text = menu.name
        if(menu.disliked) {
            holder.menuName.paintFlags = holder.menuName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.menuName.paintFlags = holder.menuName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        val dislikeImage = if (!menu.disliked) R.drawable.ic_dislike else R.drawable.ic_like
        holder.dislike.setCompoundDrawablesWithIntrinsicBounds( dislikeImage, 0, 0, 0)
        holder.dislike.setOnClickListener {
            GlobalScope.launch {
                val result : Boolean
                val dislikeText : String
                if(menu.disliked){
                     result = menuRepository.tryUndislikeMenu(menu)
                }
                else {
                    result = menuRepository.tryDislikeMenu(menu)
                }
                if (!menu.disliked) {
                    dislikeText = it.context.getString(R.string.dislike)
                } else {
                    dislikeText = it.context.getString(R.string.un_dislike)
                }
                if (!result) {
                    activity.runOnUiThread {
                        Toast.makeText(
                            it.context,
                            it.context.getString(R.string.menu_like_dislike_error, menu.name, dislikeText),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    activity.runOnUiThread {
                        Toast.makeText(
                            it.context,
                            it.context.getString(R.string.menu_like_dislike_success, menu.name, dislikeText),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    val list = menuRepository.getMenues()
                    activity.runOnUiThread {
                        updateMenues(ArrayList(list))
                    }
                }

                val intent = Intent(it.context, MenuNotificationService::class.java)
                intent.putExtra("loggedInUser", loggedInUser)
                it.context.startService(intent)
            }
        }
        holder.delete.setOnClickListener {

            GlobalScope.launch {
                val result = menuRepository.tryDeleteMenu(menu)

                if (!result) {
                    activity.runOnUiThread {
                        Toast.makeText(
                            it.context,
                            it.context.getString(R.string.menu_delete_error, menu.name),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    activity.runOnUiThread {
                        Toast.makeText(
                            it.context,
                            it.context.getString(R.string.menu_delete_success, menu.name),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    val list = menuRepository.getMenues()
                    activity.runOnUiThread {
                        updateMenues(ArrayList(list))
                    }
                }
            }
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