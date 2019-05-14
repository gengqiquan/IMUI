package com.gengqiquan.imui.interfaces;

import java.util.List;

public interface IMsgBuildPolicy {
    Object buildTextMessage(String message);

    Object buildAudioMessage(String recordPath, int duration);

    List<Object> buildImgMessageList(List<String> paths);

    Object buildImgMessage(String path);

    Object buildCustomMessage(String json);

    Object buildVideoMessage(String imgPath, String videoPath, int imgWidth, int imgHeight, long duration);
}