package com.gengqiquan.imlib;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.util.Log;
import androidx.annotation.MainThread;
import com.gengqiquan.imlib.model.CustomElem;
import com.gengqiquan.imui.interfaces.IimMsg;
import com.gengqiquan.imui.model.ImImage;
import com.gengqiquan.imui.model.ImVideo;
import com.tencent.imsdk.*;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RealMsg implements IimMsg {
    TIMMessage timMsg;
    Date time;
    TIMElem elem;


    private RealMsg(TIMMessage timMsg, TIMElem elem, Date time) {
        this.timMsg = timMsg;
        this.elem = elem;
        this.time = time;
    }

    public static List<RealMsg> create(TIMMessage timMsg) {
        List<RealMsg> list = new ArrayList<>();
        if (timMsg.status() == TIMMessageStatus.HasDeleted) {
            return list;
        }
        if (timMsg.getElementCount() == 0L) {
            return list;
        }
        for (int i = 0; i < timMsg.getElementCount(); i++) {
            TIMElem elem = timMsg.getElement(i);
            Date time = i == timMsg.getElementCount() - 1 ? new Date(timMsg.timestamp() * 1000) : null;
            list.add(new RealMsg(timMsg, elem, time));
        }
        if (timMsg.getElementCount() > 1) {
            Log.e("getElementCount", timMsg.getElementCount() + "");
        }
        return list;
    }

    public static RealMsg decorate(TIMMessage timMsg) {
        TIMElem elem = timMsg.getElement(0);
        Date time = new Date(timMsg.timestamp() * 1000);
        return new RealMsg(timMsg, elem, time);
    }

    @Override
    public String text() {
        if (elem.getType() != TIMElemType.Text) {
            throw new IllegalArgumentException("can not call img() that is not of the type: Text");
        }
        return ((TIMTextElem) elem).getText();
    }

    @Override
    public ImImage img() {
        if (elem.getType() != TIMElemType.Image) {
            throw new IllegalArgumentException("can not call img() that is not of the type: image");
        }
        TIMImageElem timImageElem = ((TIMImageElem) elem);
        if (timImageElem.getImageList().size() == 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(timImageElem.getPath(), options);
            int imgWidth = options.outWidth;
            int imgHeight = options.outHeight;
            return new ImImage(timImageElem.getPath(), imgWidth, imgHeight);
        }
        TIMImage timImage = ((TIMImageElem) elem).getImageList().get(0);
        return new ImImage(timImage.getUrl(), timImage.getWidth(), timImage.getHeight());
    }

    @NotNull
    @Override
    public ImVideo video() {
        if (elem.getType() != TIMElemType.Video) {
            throw new IllegalArgumentException("can not call img() that is not of the type: Video");
        }
        final TIMVideoElem videoElem = (TIMVideoElem) elem;
        TIMSnapshot snapshot = videoElem.getSnapshotInfo();
        TIMVideo videoInfo = videoElem.getVideoInfo();
        return new ImVideo(videoElem, new ImImage(videoElem.getSnapshotPath(), snapshot.getWidth(), snapshot.getHeight()));
    }


    @NotNull
    @Override
    public String sound() {
        if (elem.getType() != TIMElemType.Sound) {
            throw new IllegalArgumentException("can not call img() that is not of the type: sound");
        }
        return ((TIMSoundElem) elem).getPath();
    }

    @Override
    public long duration() {
        if (elem.getType() != TIMElemType.Sound) {
            throw new IllegalArgumentException("can not call img() that is not of the type: sound");
        }
        return ((TIMSoundElem) elem).getDuration();
    }

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat formatYear = new SimpleDateFormat("yyyy年MM月dd日");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat formatMonth = new SimpleDateFormat("MM-dd HH:mm");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat formatHours = new SimpleDateFormat("HH:mm");

    @MainThread
    public static String format(Date record) {
        Date now = new Date();
//        if (now.getYear() != record.getYear()) {
//            return formatYear.format(record) + tag + formatHours.format(record);
//        }
        int days = now.getDay() - record.getDay();

        if (days == 0) {
            return formatHours.format(record);
        }
        if (days == 1) {
            return "昨天 " + formatHours.format(record);
        }
        if (days == 2) {
            return "前天 " + formatHours.format(record);
        }
        if (days <= 7) {
            Calendar c = Calendar.getInstance();
            c.setTime(record);
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            return "星期" + weekStr[weekday] + " " + formatHours.format(record);
        }
        return formatYear.format(record) + " " + formatHours.format(record);
    }

    final static String[] weekStr = new String[]{"日", "一", "二", "三", "四", "五", "六",};

    @NotNull
    @Override
    public String time() {
        if (time != null)
            return format(time);
        return null;
    }

    /**
     * getCustomInt 自定义消息字段 等于-1的情况是这个消息为待发送的一个消息
     * 返回类型直接加上1000 解析对应view时判断是否千位为1可知这个消息为待发送的一个消息
     * 返回-1 代表撤回的消息
     *
     * @author gengqiquan
     * @date 2019-05-09 14:57
     */
    @Override
    public int uiType() {
        if (timMsg.status() == TIMMessageStatus.HasRevoked) {
            return -1;
        }
        if (elem.getType() == TIMElemType.Custom) {
            TIMCustomElem customElem = (TIMCustomElem) elem;
            customData = CustomElem.create(new String(customElem.getData()));
            int type = 0;
            switch (customData.getType()) {
                case share:
                    type = 5;
                    break;
            }
            if (new TIMMessageExt(timMsg).getCustomInt() == -1) {
                type = 1000 + type;
            }
            return type;
        }
        switch (elem.getType()) {
            case Text:
                return 1;
            case Image:
                return 2;
            case Video:
                return 3;
            case Sound:
                return 4;
            default:
                return 0;
        }
    }


    @Override
    public TIMMessage realData() {
        return timMsg;
    }

    @Override
    public boolean isSelf() {
        return timMsg.isSelf();
    }


    @NotNull
    @Override
    public String sender() {
        return timMsg.getSenderProfile().getNickName();
    }

    CustomElem customData;

    @NotNull
    @Override
    public Object extra() {
        return customData;
    }

    @Override
    public boolean equals(Object o) {


        if (this == o) return true;
        if (!(o instanceof RealMsg)) return false;

        RealMsg realMsg = (RealMsg) o;
        if (timMsg == null || realMsg.timMsg == null || elem == null || realMsg.elem == null) {
            return false;
        }
        return timMsg.getMsgUniqueId() == realMsg.timMsg.getMsgUniqueId();
    }

    @Override
    public int hashCode() {
        int result = timMsg != null ? timMsg.hashCode() : 0;
        result = 31 * result + (elem != null ? elem.hashCode() : 0);
        return result;
    }

    int status = 0;

    public void failure() {
        status = 3;
    }

    public void success() {
        status = 2;
    }

    @Override
    public @IimMsg.Companion.SendType
    int status() {
        if (status > 1) {
            return status;
        }

        if (TIMMessageStatus.Sending == timMsg.status()) {
            status = 1;
        }
        if (TIMMessageStatus.SendSucc == timMsg.status()) {
            status = 2;
        }
        if (TIMMessageStatus.SendFail == timMsg.status()) {
            status = 3;
        }
        return status;
    }
}
