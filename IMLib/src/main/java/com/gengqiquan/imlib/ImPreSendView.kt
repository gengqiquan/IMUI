package com.gengqiquan.imlib

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import com.gengqiquan.imlib.model.CustomElem
import com.gengqiquan.imlib.model.CustomType
import com.gengqiquan.imlib.model.ShareElem
import com.gengqiquan.imui.help.IMHelp
import com.gengqiquan.imui.interfaces.ISenderListener
import com.gengqiquan.imui.interfaces.IimMsg
import com.gengqiquan.imui.ui.ImView
import com.gengqiquan.imui.ui.singleClick
import com.tencent.imsdk.ext.message.TIMMessageExt
import org.jetbrains.anko.*

class ImPreSendView(val context: Context) : ImView {
    override fun get(): View {
        return LinearLayout(context).apply {

            backgroundResource = R.drawable.im_share_msg_back
            padding = dip(13)
            layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent).apply {
                verticalMargin = dip(20)
                horizontalMargin = dip(15)
            }
            iv_img = imageView {
                layoutParams = LinearLayout.LayoutParams(dip(80), dip(80))
                scaleType = ImageView.ScaleType.FIT_XY
            }

            linearLayout {
                orientation = VERTICAL
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent).apply {
                    leftMargin = dip(13)
                }
                tv_title = textView {
                    textColor = Color.BLACK
                    textSize = 15f
                    includeFontPadding = false
                    maxLines = 2

                }
                tv_desc = textView {
                    textColor = 0xff999999.toInt()
                    textSize = 12f
                    includeFontPadding = false
                    maxLines = 3
                    layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent).apply {
                        topMargin = dip(10)
                    }
                }
                tv_send = textView {
                    backgroundResource = R.drawable.im_send_back
                    text = "发送TA"
                    textColor = Color.WHITE
                    textSize = 12f
                    gravity = Gravity.CENTER
                    includeFontPadding = false
                    layoutParams = LinearLayout.LayoutParams(dip(67), dip(25)).apply {
                        topMargin = dip(10)
                        gravity = Gravity.BOTTOM xor Gravity.RIGHT
                    }
                }
            }
        }

    }

    override fun decorator(item: IimMsg) {
        val elem = item.extra() as CustomElem
        when (elem.type) {
            CustomType.share -> {
                val data = elem.data as ShareElem
                tv_title?.text = data.msg.title
                tv_desc?.text = data.msg.content
                IMHelp.getImageDisplayer().display(data.msg.pic_url, iv_img!!)
                tv_send?.singleClick {
                    TIMMessageExt(item.realData()).customInt = 0
                    IMHelp.getMsgSender(context)?.send(item.realData())
                }
            }
            else -> null
        }
    }

    var tv_title: TextView? = null
    var tv_desc: TextView? = null
    var tv_send: TextView? = null
    var iv_img: ImageView? = null


}