package com.gengqiquan.imui.ui

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.gengqiquan.imui.R
import com.gengqiquan.imui.interfaces.IimMsg
import org.jetbrains.anko.*

class ImRevokeView(val context: Context) : ImView {
    override fun get(): View {

        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent).apply {
                topPadding = dip(15)
            }
            tv_time = textView {
                background = resources.getDrawable(R.drawable.im_time_back)
                textColor = Color.WHITE
                textSize = 12f
                gravity = Gravity.CENTER
                includeFontPadding = false
                leftPadding = dip(6)
                rightPadding = dip(6)
                layoutParams = LinearLayout.LayoutParams(wrapContent, dip(20)).apply {
                    bottomMargin = dip(20)
                    gravity = Gravity.CENTER_HORIZONTAL
                }

            }
            tv_content = textView {
                background = resources.getDrawable(R.drawable.im_audio_back)
                textColor = Color.WHITE
                textSize = 12f
                gravity = Gravity.CENTER
                includeFontPadding = false
                leftPadding = dip(6)
                rightPadding = dip(6)
                layoutParams = LinearLayout.LayoutParams(wrapContent, dip(20)).apply {
                    bottomMargin = dip(20)
                    gravity = Gravity.CENTER_HORIZONTAL
                }

            }

        }
    }

    private var tv_content: TextView? = null
    private var tv_time: TextView? = null
    override fun decorator(item: IimMsg) {
        tv_time?.isShow(!item.time().isNullOrEmpty())
        tv_time?.text = item.time() ?: ""
        val text = if (item.isSelf()) "您" else "\"${item.sender()}\""
        tv_content?.text = text + "撤回了一条消息"
    }


}