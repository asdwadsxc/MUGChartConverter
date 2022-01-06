package com.converter.formatted;

import java.util.ArrayList;
import java.util.List;

/**
 * @author asdwadsxc
 * @create 2021-12-29 12:50
 */
public class FormattedChart {

    private static int player;
    private static String title;
    private static String artist;
    private static String bpm;
    private static int rank;

    public static int getPlayer() {
        return player;
    }

    public static void setPlayer(int player) {
        FormattedChart.player = player;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        FormattedChart.title = title;
    }

    public static String getArtist() {
        return artist;
    }

    public static void setArtist(String artist) {
        FormattedChart.artist = artist;
    }

    public static String getBpm() {
        return bpm;
    }

    public static void setBpm(String bpm) {
        FormattedChart.bpm = bpm;
    }

    public static int getRank() {
        return rank;
    }

    public static void setRank(int rank) {
        FormattedChart.rank = rank;
    }

    public static List<String[]> bpmList = new ArrayList<>();//时间，BPM，节拍[x,x,x]，节拍计算结果
    public static List<String[]> wavList = new ArrayList<>();//编号，文件名
    public static List<String[]> bgmList = new ArrayList<>();//节拍[x,x,x]，编号，时间
    public static List<String[]> noteP1List = new ArrayList<>();//节拍[x,x,x]，列，编号
    public static List<String[]> lnP1List = new ArrayList<>();//节拍[x,x,x]，列，编号
    public static List<String[]> noteP2List = new ArrayList<>();//节拍[x,x,x]，列，编号
    public static List<String[]> lnP2List = new ArrayList<>();//节拍[x,x,x]，列，编号

}
