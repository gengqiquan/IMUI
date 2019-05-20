package com.gengqiquan.imui.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gengqiquan.imui.help.IMHelp

/**
 * @author mazhichao
 * @date 2019/05/13 14:21
 */
class SessionHolder(val item:ISessionItemView, context: Context):RecyclerView.ViewHolder(item.createView(context)) {

    companion object{
        fun createView(context:Context):SessionHolder{
            val view = IMHelp.getSession()
            return SessionHolder(view,context)
        }
    }

}