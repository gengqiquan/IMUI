package com.gengqiquan.imui.ui

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gengqiquan.imui.R
import com.gengqiquan.imui.interfaces.IimMsg
import org.jetbrains.anko.*

class ImUnKnowView(val context: Context) : ImView {
    override fun get(): View {
        return FrameLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent)
            textView {
                textColor = Color.BLACK
                textSize = 14f
                backgroundResource = R.drawable.im_edit_back
                gravity = Gravity.CENTER
                includeFontPadding = false
                text = "当前版本暂不支持此消息"
                horizontalPadding = dip(20)
                layoutParams = FrameLayout.LayoutParams(wrapContent, dip(40)).apply {
                    gravity = Gravity.CENTER
                }
            }
        }
    }

    override fun decorator(item: IimMsg) {
    }


}