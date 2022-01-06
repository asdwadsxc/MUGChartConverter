package com.converter.iidx;

import com.converter.formatted.FormattedChart;
import com.converter.util.BeatToGroup;
import com.converter.util.TimeToBeat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author asdwadsxc
 * @create 2021-12-29 12:46
 */
public class Analyze {

    public static void analyze() {

        analyzeHead();

        analyzeWav();

        analyzeBpm();
        ChartList.bpmInfo.clear();

        analyzeBgmNote(ChartList.bgmInfo);
        ChartList.bgmInfo.clear();

        analyzeP1Note();
        ChartList.noteP1Info.clear();

        analyzeP2Note();
        ChartList.noteP2Info.clear();

    }

    private static void analyzeHead() {

        if (ChartList.noteP2Info.isEmpty()) {
            FormattedChart.setPlayer(1);
        } else {
            FormattedChart.setPlayer(2);
        }

        byte[] bytes = ChartList.bpmInfo.get(0);
        short aShort = ByteBuffer.wrap(bytes, 6, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
        double bpm = (double) aShort / bytes[5];
        ChartList.bpmInfo.remove(0);

        FormattedChart.setBpm(String.valueOf(bpm));
        FormattedChart.setRank(3);
        FormattedChart.setTitle("");
        FormattedChart.setArtist("");

    }

    private static void analyzeWav() {

        FormattedChart.wavList.addAll(ChartList.wavInfo);

    }

    private static void analyzeBpm() {

        for (int i = 0; i < ChartList.bpmInfo.size(); i++) {
            byte[] bytes = ChartList.bpmInfo.get(i);
            int time = ByteBuffer.wrap(bytes, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            short bpmBase = ByteBuffer.wrap(bytes, 6, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
            double bpm = (double) bpmBase / bytes[5];
            String[] toBeat = TimeToBeat.timeToBeat(time);
            String[] toGroup = BeatToGroup.beatToGroup(toBeat);
            FormattedChart.bpmList.add(new String[]{String.valueOf(time), String.valueOf(bpm), toGroup[0], toGroup[1], toGroup[2], toBeat[3]});
        }
    }

    private static void analyzeBgmNote(List<byte[]> bgmInfo) {

        List<byte[]> bgmInfoRe = new ArrayList<>();
        int flag = -1;
        for (int i = 0; i < bgmInfo.size(); i++) {
            byte[] bytes = bgmInfo.get(i);
            int time = ByteBuffer.wrap(bytes, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            if (time != flag) {
                short indexInfo = ByteBuffer.wrap(bytes, 6, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
                String string = Integer.toString(indexInfo, 36).toUpperCase();
                if (string.length() == 1) {
                    string = "0" + string;
                }
                String[] toBeat = TimeToBeat.timeToBeat(time);
                String[] toGroup = BeatToGroup.beatToGroup(toBeat);
                flag = time;
                FormattedChart.bgmList.add(new String[]{toGroup[0], toGroup[1], toGroup[2], string, String.valueOf(time)});
            } else {
                bgmInfoRe.add(bytes);
            }
        }
        if (!bgmInfoRe.isEmpty()) {
            analyzeBgmNote(bgmInfoRe);
        }
    }

    private static void analyzeP1Note() {

        String[] index = new String[8];
        String[] indexRow = new String[]{"2", "3", "4", "5", "6", "7", "8", "1"};
        for (int i = 0; i < ChartList.noteP1Info.size(); i++) {
            byte[] bytes = ChartList.noteP1Info.get(i);
            int time = ByteBuffer.wrap(bytes, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            short indexInfo = ByteBuffer.wrap(bytes, 6, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
            if (bytes[4] == 2) {
                String string = Integer.toString(indexInfo, 36).toUpperCase();
                if (string.length() == 1) {
                    string = "0" + string;
                }
                index[bytes[5]] = string;
            } else {
                String[] toBeat = TimeToBeat.timeToBeat(time);
                String[] toGroup = BeatToGroup.beatToGroup(toBeat);
                if (indexInfo != 0) {
                    int lnTime = time + indexInfo;
                    String[] toBeatLn = TimeToBeat.timeToBeat(lnTime);
                    String[] toGroupLn = BeatToGroup.beatToGroup(toBeatLn);
                    FormattedChart.lnP1List.add(new String[]{toGroup[0], toGroup[1], toGroup[2], indexRow[bytes[5]], index[bytes[5]]});
                    FormattedChart.lnP1List.add(new String[]{toGroupLn[0], toGroupLn[1], toGroupLn[2], indexRow[bytes[5]], index[bytes[5]]});
                } else {
                    FormattedChart.noteP1List.add(new String[]{toGroup[0], toGroup[1], toGroup[2], indexRow[bytes[5]], index[bytes[5]]});
                }
            }
        }
    }

    private static void analyzeP2Note() {

        String[] index = new String[8];
        String[] indexRow = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
        for (int i = 0; i < ChartList.noteP2Info.size(); i++) {
            byte[] bytes = ChartList.noteP2Info.get(i);
            int time = ByteBuffer.wrap(bytes, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
            short indexInfo = ByteBuffer.wrap(bytes, 6, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
            if (bytes[4] == 3) {
                String string = Integer.toString(indexInfo, 36).toUpperCase();
                if (string.length() == 1) {
                    string = "0" + string;
                }
                index[bytes[5]] = string;
            } else {
                String[] toBeat = TimeToBeat.timeToBeat(time);
                String[] toGroup = BeatToGroup.beatToGroup(toBeat);
                if (indexInfo != 0) {
                    int lnTime = time + indexInfo;
                    String[] toBeatLn = TimeToBeat.timeToBeat(lnTime);
                    String[] toGroupLn = BeatToGroup.beatToGroup(toBeatLn);
                    FormattedChart.lnP2List.add(new String[]{toGroup[0], toGroup[1], toGroup[2], indexRow[bytes[5]], index[bytes[5]]});
                    FormattedChart.lnP2List.add(new String[]{toGroupLn[0], toGroupLn[1], toGroupLn[2], indexRow[bytes[5]], index[bytes[5]]});
                } else {
                    FormattedChart.noteP2List.add(new String[]{toGroup[0], toGroup[1], toGroup[2], indexRow[bytes[5]], index[bytes[5]]});
                }
            }
        }
    }
}
