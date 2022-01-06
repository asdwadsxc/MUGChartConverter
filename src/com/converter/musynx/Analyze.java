package com.converter.musynx;


import com.converter.formatted.FormattedChart;
import com.converter.util.BeatToGroup;
import com.converter.util.TimeToBeat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author asdwadsxc
 * @create 2022-01-03 9:30
 */
public class Analyze {

    public static void analyze() {

        analyzeHead();

        analyzeBpm();
        ChartList.bpmInfo.clear();

        analyzeBgmNote(ChartList.bgmInfo);
        ChartList.bgmInfo.clear();

        analyzeNote();
        ChartList.noteInfo.clear();

        analyzeLnNote();
        ChartList.lnNoteInfo.clear();

    }

    private static void analyzeHead() {

        FormattedChart.setArtist("");
        FormattedChart.setTitle("");
        FormattedChart.setPlayer(1);

    }

    private static void analyzeBpm() {

        for (int i = 0; i < ChartList.bpmInfo.size(); i++) {
            String[] strings = ChartList.bpmInfo.get(i);
            double timeOri = Integer.parseInt(strings[1]) * 0.0001;
            long time = Math.round(timeOri);
            String[] toBeat = TimeToBeat.timeToBeat((int) time);
            String[] toGroup = BeatToGroup.beatToGroup(toBeat);
            FormattedChart.bpmList.add(new String[]{String.valueOf(time), strings[2], toGroup[0], toGroup[1], toGroup[2], toBeat[3]});
        }

    }

    private static void analyzeBgmNote(List<String[]> bgmInfo) {

        List<String[]> bgmInfoRe = new ArrayList<>();
        int flag = -1;
        for (int i = 0; i < bgmInfo.size(); i++) {

            String[] strings = bgmInfo.get(i);
            double timeOri = Integer.parseInt(strings[1]) * 0.0001;
            long time = Math.round(timeOri);

            if (time != flag) {
                String[] toBeat = TimeToBeat.timeToBeat((int) time);
                String[] toGroup = BeatToGroup.beatToGroup(toBeat);
                flag = (int) time;
                FormattedChart.bgmList.add(new String[]{toGroup[0], toGroup[1], toGroup[2], strings[3], String.valueOf(time)});
            } else {
                bgmInfoRe.add(strings);
            }
        }
        if (!bgmInfoRe.isEmpty()) {
            analyzeBgmNote(bgmInfoRe);
        }

    }

    private static void analyzeNote() {

        for (int i = 0; i < ChartList.noteInfo.size(); i++) {
            String[] strings = ChartList.noteInfo.get(i);
            double timeOri = Integer.parseInt(strings[1]) * 0.0001;
            long time = Math.round(timeOri);
            String[] toBeat = TimeToBeat.timeToBeat((int) time);
            String[] toGroup = BeatToGroup.beatToGroup(toBeat);
            FormattedChart.noteP1List.add(new String[]{toGroup[0], toGroup[1], toGroup[2], strings[2], strings[3]});
        }

    }

    private static void analyzeLnNote() {

        for (int i = 0; i < ChartList.lnNoteInfo.size(); i++) {

            String[] strings = ChartList.lnNoteInfo.get(i);
            double timeOriStart = Integer.parseInt(strings[1]) * 0.0001;
            long timeStart = Math.round(timeOriStart);
            double timeOriEnd = Integer.parseInt(strings[4]) * 0.0001;
            long timeEnd = Math.round(timeOriEnd);

            String[] toBeatStart = TimeToBeat.timeToBeat((int) timeStart);
            String[] toGroupStart = BeatToGroup.beatToGroup(toBeatStart);
            String[] toBeatEnd = TimeToBeat.timeToBeat((int) timeEnd);
            String[] toGroupEnd = BeatToGroup.beatToGroup(toBeatEnd);

            FormattedChart.lnP1List.add(new String[]{toGroupStart[0], toGroupStart[1], toGroupStart[2], strings[2], strings[3]});
            FormattedChart.lnP1List.add(new String[]{toGroupEnd[0], toGroupEnd[1], toGroupEnd[2], strings[2], strings[3]});
        }
    }

}
