package com.gengqiquan.imui.ui

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.gengqiquan.imui.help.IMHelp
import com.gengqiquan.imui.interfaces.DisplayListener
import com.gengqiquan.imui.interfaces.IimMsg
import org.jetbrains.anko.*

class ImImageView(context: Context) : RealImView(context) {
    var iv_img: ImageView? = null
    override fun floatBaseView() = iv_img!!

    override fun createItemView(contentView: RelativeLayout): View {
        iv_img = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
        return iv_img!!
    }

    override fun decoratorItemView(item: IimMsg) {
        iv_img?.setImageDrawable(null)
        val img = item.img()
        val url = img.url ?: return
        val w = img.width
        val h = img.height
        val max = dip(140)
        if (h <= 0) {
            return
        }
        val newWidth: Int
        val newHeight: Int
        val per = h / w.toFloat()
        Log.d("display", w.toString() + ":" + h.toString() + "++" + per)
        if (w < max && h < max) {
            newWidth = max
            newHeight = max
            iv_img?.scaleType = ImageView.ScaleType.FIT_XY
        } else if (per < 1) {
            newWidth = max
            newHeight = (h * max / w.toFloat()).toInt()
            iv_img?.scaleType = ImageView.ScaleType.CENTER_CROP
        } else if (per > 3) {
            newWidth = dip(56)
            newHeight = max
            iv_img?.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            newWidth = (w * max / h.toFloat()).toInt()
            newHeight = max
            iv_img?.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        iv_img?.layoutParams = RelativeLayout.LayoutParams(newWidth, newHeight).apply {
            if (item.isSelf()) alignParentRight() else alignParentLeft()
        }

        IMHelp.getImageDisplayer().display(url, iv_img!!, object : DisplayListener {
            override fun ready() {
                iv_img?.singleClick {
                    context.startActivity<ImagePreviewActivity>(IMHelp.IMAGE_PATH to url)
                }
            }

            override fun error() {
            }

            override fun start() {
            }
        })
    }

}