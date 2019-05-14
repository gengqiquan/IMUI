package com.gengqiquan.imlib

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gengqiquan.imlib.model.CustomElem
import com.gengqiquan.imlib.model.CustomType
import com.gengqiquan.imlib.model.ShareElem
import com.gengqiquan.imui.help.IMHelp
import com.gengqiquan.imui.interfaces.IimMsg
import com.gengqiquan.imui.ui.RealImView
import com.gengqiquan.imui.ui.isShow
import org.jetbrains.anko.*

class ImShareView(context: Context) : RealImView(context) {
    var ll_content: LinearLayout? = null
    var tv_title: TextView? = null
    var tv_desc: TextView? = null
    var tv_from: TextView? = null
    var iv_img: ImageView? = null
    var v_line: View? = null

    override fun floatBaseView() = ll_content!!
    override fun createItemView(contentView: FrameLayout): View {
        return contentView.apply {
            ll_content = linearLayout {
                orientation = VERTICAL
                backgroundResource = R.drawable.im_share_msg_back
                horizontalPadding = dip(13)
                topPadding = dip(13)
                layoutParams = FrameLayout.LayoutParams(matchParent, wrapContent)
                tv_title = textView {
                    textColor = Color.BLACK
                    textSize = 15f
                    includeFontPadding = false
                    maxLines = 2

                }
                linearLayout {
                    layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent).apply {
                        topMargin = dip(10)
                    }

                    tv_desc = textView {
                        textColor = 0xff999999.toInt()
                        textSize = 12f
                        includeFontPadding = false
                        maxLines = 3
                        layoutParams = LinearLayout.LayoutParams(0, wrapContent).apply {
                            weight = 1f
                        }
                    }
                    iv_img = imageView {
                        layoutParams = LinearLayout.LayoutParams(dip(45), dip(45)).apply {
                            gravity = Gravity.RIGHT xor Gravity.BOTTOM
                            bottomMargin = dip(13)
                        }
                        scaleType = ImageView.ScaleType.FIT_XY
                    }

                }
                v_line = view {
                    backgroundColor = 0xfff0f0f0.toInt()
                    layoutParams = LinearLayout.LayoutParams(matchParent, dip(0.5f))
                }
                tv_from = textView {
                    textColor = 0xff999999.toInt()
                    textSize = 11f
                    gravity = Gravity.CENTER_VERTICAL
                    includeFontPadding = false
                    layoutParams = LinearLayout.LayoutParams(matchParent, dip(25))
                }
            }

        }
    }


    override fun decoratorItemView(item: IimMsg) {
        val elem = item.extra() as CustomElem
        when (elem.type) {
            CustomType.share -> {
                val data = elem.data as ShareElem
                tv_title?.text = data.msg.title
                tv_desc?.text = data.msg.content
                tv_from?.isShow(!TextUtils.isEmpty(data.msg.module))
                v_line?.isShow(!TextUtils.isEmpty(data.msg.module))
                tv_from?.text = data.msg.module
                IMHelp.getImageDisplayer().display(data.msg.pic_url, iv_img!!)
            }
            else -> null
        }


    }


}