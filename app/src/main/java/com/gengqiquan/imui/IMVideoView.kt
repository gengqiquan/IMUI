package com.gengqiquan.imui

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class IMVideoView(val context: Context,parent: ViewGroup) : IimView(parent) {
    var itemView: TextView? = null
    override fun get(): View {
        itemView = TextView(context)
        return itemView!!
    }

    override fun decorator(item: IimMsg) {
        val msg = item.realData<TXMsg>()
        itemView?.text="暂不支持"
    }
}