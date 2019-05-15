package com.gengqiquan.imui.input

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.gengqiquan.imui.R
import com.gengqiquan.imui.help.IMHelp
import com.gengqiquan.imui.help.ToastHelp
import org.jetbrains.anko.*

class ImAudioInputView(context: Context) : TextView(context) {
    private var audioCancel: Boolean = false
    private var startRecordY: Float = 0.toFloat()
//    var inputHandler: InputHandler? = null

    init {

        background = resources.getDrawable(R.drawable.im_edit_back)
        includeFontPadding = false
        textColor = Color.BLACK
        textSize = 18f
        gravity = Gravity.CENTER
        text = "按住 说话"
        visibility = View.GONE
        setOnTouchListener(object : OnTouchListener {
            private var start: Long = 0
            override fun onTouch(v: View, motionEvent: MotionEvent): Boolean {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    audioCancel = true
                    startRecordY = motionEvent.getY()

                    startRecord()
                    start = System.currentTimeMillis()
                    IMHelp.getAudioRecorder().startRecord {
                        if (it < 1000 && !audioCancel) {
                            tooShort()
                            return@startRecord
                        }

                        if (!audioCancel) {
                            stopRecord()
                            IMHelp.getMsgSender(context)?.send(
                                IMHelp.getMsgBuildPolicy().buildAudioMessage(
                                    IMHelp.getAudioRecorder().recordAudioPath,
                                    it.toInt()
                                )
                            )
                        }
                    }
                    return true
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if (motionEvent.getY() - startRecordY < -100) {
                        if (!audioCancel) {
                            moveOut()
                            audioCancel = true
                        }

                    } else {
                        if (audioCancel) {
                            moveIn()
                            audioCancel = false
                        }

                    }
                    return true
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    audioCancel = motionEvent.getY() - startRecordY < -100
                    if (audioCancel) {
                        stopRecord()
                    }
                    IMHelp.getAudioRecorder().stopRecord()
                }
                return true
            }
        })
        layoutParams = LinearLayout.LayoutParams(0, dip(38)).apply {
            topMargin = dip(8)
            bottomMargin = dip(8)
            weight = 1f
        }
    }


    fun startRecord() {
        apply {
            background = resources.getDrawable(R.drawable.im_audio_back)
            text = "松开 结束"
        }
        showPopAction()
    }

    fun stopRecord() {
        apply {
            background = resources.getDrawable(R.drawable.im_edit_back)
            text = "按住 说话"
        }
        popup?.dismiss()
    }

    fun tooShort() {
        animationDrawable?.stop()
        iv_animate?.padding = dip(12)
        iv_animate?.imageResource = R.drawable.im_too_short
        tv_tips?.text = "说话时间太短"
        postDelayed({
            stopRecord()
        }, 1000)
    }

    var popup: PopupWindow? = null
    var iv_animate: ImageView? = null
    var tv_tips: TextView? = null
    fun showPopAction() {

        if (popup == null) {
            val content = LinearLayout(context).apply {
                gravity = Gravity.CENTER
                background = resources.getDrawable(R.drawable.im_record_volume_pop_back)
                orientation = LinearLayout.VERTICAL
                iv_animate = imageView {
                    layoutParams = LinearLayout.LayoutParams(dip(90), dip(90))
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                    setImageResource(R.drawable.im_recording_volume)
                }
                tv_tips = textView {
                    gravity = Gravity.CENTER
                    text = "手指上滑，取消发送"
                    horizontalPadding = dip(5)
                    verticalPadding = dip(3)
                    includeFontPadding = false
                    textColor = 0xffeeeeee.toInt()
                    layoutParams = LinearLayout.LayoutParams(wrapContent, wrapContent).apply {
                        topMargin = dip(10)
                    }
                }
            }

            popup = PopupWindow(content, dip(150), dip(150))
            popup?.isOutsideTouchable = true
            popup?.isFocusable = true
        }
        popup?.showAtLocation(rootView, Gravity.CENTER, 0, 0)
        iv_animate?.setImageResource(R.drawable.im_recording_volume)
        animationDrawable = iv_animate?.drawable as AnimationDrawable
        iv_animate?.post {
            animationDrawable?.start()
        }

        popup?.setOnDismissListener { animationDrawable?.stop() }
    }

    var animationDrawable: AnimationDrawable? = null
    fun moveOut() {
        tv_tips?.text = "松开手指，取消发送"
        tv_tips?.backgroundColor = 0xff680008.toInt()
        animationDrawable?.stop()
        iv_animate?.padding = dip(15)
        iv_animate?.imageResource = R.drawable.im_voice_cancel
    }

    fun moveIn() {
        tv_tips?.text = "手指上滑，取消发送"
        tv_tips?.backgroundColor = 0x00680008
        iv_animate?.padding = dip(6)
        iv_animate?.setImageResource(R.drawable.im_recording_volume)
        animationDrawable = iv_animate?.drawable as AnimationDrawable?
        iv_animate?.post {
            animationDrawable?.start()
        }
    }


    inline fun View.dip(value: Int): Int = context.dip(value)
    inline fun View.dip(value: Float): Int = context.dip(value)
}