package com.gengqiquan.imui.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IdRes
import com.gengqiquan.imui.R
import com.gengqiquan.imui.help.LongPressHelp
import com.gengqiquan.imui.interfaces.IimMsg
import com.gengqiquan.imui.model.MenuAction
import org.jetbrains.anko.*

abstract class RealImView(context: Context) : LinearLayout(context), ImView {

    private var tv_header: TextView? = null
    private var tv_time: TextView? = null
    override fun get(): View {

        return this
    }

    @SuppressLint("ResourceType")
    @IdRes
    private val localId = 0xff8800

    init {
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
        frameLayout {
            tv_header = textView {
                textColor = Color.WHITE
                textSize = 15f
                gravity = Gravity.CENTER
                background = resources.getDrawable(R.drawable.im_header_back)
                layoutParams = FrameLayout.LayoutParams(dip(41), dip(41)).apply {
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                }
            }
            relativeLayout {
                horizontalPadding = dip(63)
                iv_fail = imageView {
                    backgroundColor = Color.BLACK
                }
                itemView = relativeLayout {
                    id = localId
                    createItemView(this)
                }
            }

        }
    }

    private var iv_fail: ImageView? = null
    private var itemView: View? = null
    abstract fun createItemView(contentView: RelativeLayout): View

    override fun decorator(item: IimMsg) {
        tv_header?.layoutParams = (tv_header?.layoutParams as FrameLayout.LayoutParams).apply {
            gravity = if (item.isSelf()) Gravity.RIGHT else Gravity.LEFT
        }
        itemView?.layoutParams = RelativeLayout.LayoutParams(wrapContent, wrapContent).apply {
//            addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//            addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            if (item.isSelf()) alignParentRight() else alignParentLeft()
        }
        iv_fail?.layoutParams =  RelativeLayout.LayoutParams(dip(30), dip(30)).apply {
            centerVertically()
            if (item.isSelf()) leftOf(localId) else rightOf(localId)
        }
        iv_fail?.isShow(item.status() == 3)
        var name = item.sender().toString()
        if (name.length > 2) {
            name = name.substring(name.length - 2)
        }
        tv_header?.text = name
        tv_time?.isShow(!item.time().isNullOrEmpty())
        tv_time?.text = item.time() ?: ""
        decoratorItemView(item)
        floatBaseView().setOnLongClickListener {

            LongPressHelp.showPopAction(
                context, item.realData(),
                getMenuAction(LongPressHelp.getActions().filter { !it.isOnlySelf || item.isSelf() } as MutableList<MenuAction>),
                rootView,
                floatBaseView()
            )
            false
        }
    }

    open fun getMenuAction(actions: MutableList<com.gengqiquan.imui.model.MenuAction>): MutableList<com.gengqiquan.imui.model.MenuAction> {
        return actions
    }

    //    fun dip(value: Int) =this.dip(value)
    abstract fun decoratorItemView(item: IimMsg)

    abstract fun floatBaseView(): View
}