package com.converter.malody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author asdwadsxc
 * @create 2022-01-07 10:07
 */
public class ChartList {

    private static String metaInfo;

    public static String getMetaInfo() {
        return metaInfo;
    }

    public static void setMetaInfo(String metaInfo) {
        ChartList.metaInfo = metaInfo;
    }

    public static List<String> wavInfo = new ArrayList<>();
    public static List<String> bpmInfo = new ArrayList<>();
    public static List<String> bgmInfo = new ArrayList<>();
    public static List<String> noteInfo = new ArrayList<>();
    public static List<String> lnNoteInfo = new ArrayList<>();

}
