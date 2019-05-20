package com.gengqiquan.imui.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.wrapContent

/**
 * @author mazhichao
 * @date 2019/05/13 11:43
 */
class SessionUI(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {


    var data:MutableList<ISession> = arrayListOf()

    init {
        orientation = VERTICAL
        recyclerView {
            layoutParams = LayoutParams(matchParent, 0).apply{
                weight = 1f
                layoutManager = LinearLayoutManager(context)
                overScrollMode = View.OVER_SCROLL_NEVER
                adapter = listAdapter
            }
        }
    }


    private val listAdapter by lazy {
        object : RecyclerView.Adapter<SessionHolder>(){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionHolder {
                return SessionHolder.createView(context)
            }

            override fun getItemCount(): Int {
                return data.size
            }

            override fun onBindViewHolder(holder: SessionHolder, position: Int) {
                holder.item.bindView(data[position])
            }
        }
    }
}