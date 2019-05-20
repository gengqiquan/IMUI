package com.gengqiquan.imui.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.gengqiquan.imui.R
import org.jetbrains.anko.*

/**
 * @author mazhichao
 * @date 2019/05/13 15:49
 */
class DefaultSessionItemView() : ISessionItemView {

    var tv_header: TextView? = null
    var tv_name: TextView? = null
    var tv_msg: TextView? = null
    var tv_time: TextView? = null
    var tv_count: TextView? = null

    override fun createView(context: Context):View {
        return RelativeLayout(context).apply{
            layoutParams = RelativeLayout.LayoutParams(matchParent, wrapContent).apply {
                topPadding = dip(12)
                bottomPadding = dip(12)
                leftPadding = dip(15)
                rightPadding = dip(15)
            }

            tv_header = textView {
                textColor = Color.WHITE
                textSize = 15f
                gravity = Gravity.CENTER
                background = resources.getDrawable(R.drawable.im_header_back)
                layoutParams = RelativeLayout.LayoutParams(dip(49), dip(49)).apply {
                    rightMargin = dip(10)
                    alignParentLeft()
                    alignParentTop()
                }
            }

            tv_name = textView {
                textColor = 0x333333
                textSize = 17f
                layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent).apply {
                    alignParentTop()
                    rightOf(tv_header!!)
                    bottomMargin = dip(7)
                }
            }

            tv_msg = textView {
                textColor = 0x999999
                textSize = 14f
                layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent).apply {
                    bottomOf(tv_name!!)
                    rightOf(tv_header!!)
                }
            }

            tv_time = textView {
                textColor = 0x999999
                textSize = 12f
                layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent).apply {
                    alignParentRight()
                    alignParentTop()
                }
            }

            tv_count = textView {
                textColor = Color.WHITE
                textSize = 10f
                gravity = Gravity.CENTER
                background = resources.getDrawable(R.drawable.im_header_back)
                layoutParams = RelativeLayout.LayoutParams(wrapContent, dip(14)).apply {
                    alignParentRight()
                    alignParentBottom()
                }
            }
        }
    }

    override fun bindView(item: ISession) {
        tv_header?.text = item.head()?:""
        tv_name?.text = item.name()?:""
        tv_msg?.text = item.msg()?:""
        tv_time?.text = item.time()?:""
        tv_count?.text = item.unRead()?:""
    }
}