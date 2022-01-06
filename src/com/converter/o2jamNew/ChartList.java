package com.converter.o2jamNew;

import java.util.ArrayList;
import java.util.List;

/**
 * @author asdwadsxc
 * @create 2022-01-04 9:38
 */
public class ChartList {

    private static double bpm;

    public static double getBpm() {
        return bpm;
    }

    public static void setBpm(double bpm) {
        ChartList.bpm = bpm;
    }

    public static List<String[]> bgmInfo = new ArrayList<>();
    public static List<String[]> bpmInfo = new ArrayList<>();
    public static List<String[]> noteInfo = new ArrayList<>();

}
