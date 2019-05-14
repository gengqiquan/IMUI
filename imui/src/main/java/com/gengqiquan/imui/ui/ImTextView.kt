package com.gengqiquan.imui.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.gengqiquan.imui.R
import com.gengqiquan.imui.interfaces.IimMsg
import com.gengqiquan.imui.model.MenuAction
import org.jetbrains.anko.*

class ImTextView(context: Context) : RealImView(context) {
    override fun floatBaseView() = tv_content!!

    var tv_content: TextView? = null
    override fun getMenuAction(actions: MutableList<MenuAction>): MutableList<MenuAction> {
        val list = mutableListOf<MenuAction>()
        list.add(MenuAction("复制") {
            val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clip.primaryClip = ClipData.newPlainText("text", tv_content?.text.toString())
        })
        list.addAll(super.getMenuAction(actions))
        return list
    }

    override fun createItemView(contentView: RelativeLayout): View {
        tv_content = TextView(context).apply {
            textColor = Color.BLACK
            textSize = 18f
            includeFontPadding = false
        }
        return tv_content!!
    }


    override fun decoratorItemView(item: IimMsg) {
        tv_content?.text = item.text()
        tv_content?.layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent).apply {
            if (item.isSelf()) alignParentRight() else alignParentLeft()
        }
        tv_content?.setBackgroundResource(if (item.isSelf()) R.drawable.im_text_self else R.drawable.im_text)
    }


}