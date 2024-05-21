package com.hcc.common.utils;

public class KeyConvertUtils {
    public static String countKeyConvert(int teamId, int paradigmId) {
        return "teamId" + teamId + "paradigm" + paradigmId;
    }

    public static String taskingKeyConvert(int teamId, int paradigmId) {
        return "teamId" + teamId + "paradigm" + paradigmId + "on";
    }
}
