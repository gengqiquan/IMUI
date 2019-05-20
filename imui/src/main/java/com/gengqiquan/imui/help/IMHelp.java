package com.gengqiquan.imui.help;

import android.content.Context;
import androidx.annotation.Nullable;
import com.gengqiquan.imui.interfaces.*;
import com.gengqiquan.imui.ui.DefaultIMViewFactory;
import com.gengqiquan.imui.ui.DefaultSessionItemView;
import com.gengqiquan.imui.ui.ISessionItemView;

import java.io.File;
import java.util.*;

/**
 * 即时通讯调度类。
 * 提供各种文件路径
 * 提供各种工厂 ：聊天view工厂
 * 提供各种策略：消息生成策略。图片展示策略
 *
 * @author gengqiquan
 * @date 2019/4/3 4:02 PM
 */
public class IMHelp {
    final static String TAG = "MediaHelp";
    public final static String IMAGE_PATH = "IMAGE_PATH";
    public final static String VIDEO_PATH = "VIDEO_PATH";
    public final static String AUDIO_PATH = "AUDIO_PATH";


    private static final long LIMIT_SIZE = 100 * 1024 * 1024;//100MB防止后面压缩没有空间了
    private static Map<Context, IMsgSender> msgSenderMap = new HashMap<>();

    public static void registerMsgSender(Context context, IMsgSender msgSender) {
        msgSenderMap.put(context, msgSender);
    }

    public static void unRegisterMsgSender(Context context) {
        msgSenderMap.remove(context);
    }

    public static @Nullable IMsgSender getMsgSender(Context context) {
        return msgSenderMap.get(context);
    }

    private static IMsgBuildPolicy msgBuildPolicy;

    public static IMsgBuildPolicy getMsgBuildPolicy() {
        return msgBuildPolicy;
    }

    private static ImImageDisplayer imageDisplayer;

    public static ImImageDisplayer getImageDisplayer() {
        return imageDisplayer;
    }

    private static List<IimViewFactory> viewFactory = new ArrayList();

    public static void addImViewFactory(IimViewFactory imViewFactory) {
        viewFactory.add(0, imViewFactory);
    }

    public static List<IimViewFactory> getImViewFactory() {
        return viewFactory;
    }

    private static IAudioRecorder audioRecord;

    public static IAudioRecorder getAudioRecorder() {
        return audioRecord;
    }

    public static String getAudioPath() {
        return pathProvider.mediaDir("audio").getAbsolutePath();
    }

    public static String getImagePath() {
        return pathProvider.mediaDir("image").getAbsolutePath();
    }

    public static String getVideoPath() {
        return pathProvider.mediaDir("video").getAbsolutePath();
    }

    public static int getAudioRecordMaxTime() {
        return 60;
    }

    public static int getVideoRecordMaxTime() {
        return 10;
    }

    public static void init(Context context, IAudioRecorder recorder, IMsgBuildPolicy buildPolicy, ImImageDisplayer displayer) {
        if (pathProvider == null) {
            synchronized (IMHelp.class) {
                if (pathProvider == null) {
                    applicationContext = context.getApplicationContext();
                    pathProvider = new PathProvider(applicationContext);
                    viewFactory.add(new DefaultIMViewFactory());
                    msgBuildPolicy = buildPolicy;
                    audioRecord = recorder;
                    imageDisplayer = displayer;
                }
            }
        }
    }

    private static Context applicationContext;

    public static Context getAppContext() {
        return applicationContext;
    }

    private static volatile PathProvider pathProvider;

    private static class PathProvider {
        final File CACHE_DIR;

        private PathProvider(Context context) {
            CACHE_DIR = context.getCacheDir();
        }

        File photoDir(Context context) {
            return context.getDir("photo", Context.MODE_PRIVATE);
        }

        File mediaDir(String type) {
            File file = new File(CACHE_DIR, type + File.separator);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }

        private String randomString() {
            String time = System.currentTimeMillis() + "";
            String id = UUID.randomUUID().toString();//生成的id942cd30b-16c8-449e-8dc5-028f38495bb5中间含有横杠
            id = id.replace("-", "");//替换掉中间的那个斜杠
            return MD5.md5(id + time);
        }

    }
    private static ISessionItemView session = new DefaultSessionItemView();

    public static ISessionItemView getSession() {
        return session;
    }

    public static void setSession(ISessionItemView session) {
        IMHelp.session = session;
    }
}
