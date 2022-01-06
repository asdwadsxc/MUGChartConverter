package com.converter.bmx;

import com.converter.formatted.FormattedChart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author asdwadsxc
 * @create 2022-01-01 11:40
 */
public class WriteChart {

    public static void writeChart(String path) throws IOException {

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));

        List<String> headList = headWriter();
        int sizeHead = headList.size();
        for (int i = 0; i < sizeHead; i++) {
            bufferedWriter.write(headList.get(i));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        List<String> wavList = wavWriter();
        int sizeWav = wavList.size();
        for (int i = 0; i < sizeWav; i++) {
            bufferedWriter.write(wavList.get(i));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        List<String> bpmList = bpmWriter();
        int sizeBpm = bpmList.size();
        for (int i = 0; i < sizeBpm; i++) {
            bufferedWriter.write(bpmList.get(i));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        List<String> bgmList = bgmWriter();
        int sizeBgm = bgmList.size();
        for (int i = 0; i < sizeBgm; i++) {
            bufferedWriter.write(bgmList.get(i));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        List<String> noteP1List = noteP1Writer();
        int sizeNoteP1 = noteP1List.size();
        for (int i = 0; i < sizeNoteP1; i++) {
            bufferedWriter.write(noteP1List.get(i));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        List<String> lnP1List = lnP1Writer();
        int sizeLnP1 = lnP1List.size();
        for (int i = 0; i < sizeLnP1; i++) {
            bufferedWriter.write(lnP1List.get(i));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        List<String> noteP2List = noteP2Writer();
        int sizeNoteP2 = noteP2List.size();
        for (int i = 0; i < sizeNoteP2; i++) {
            bufferedWriter.write(noteP2List.get(i));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        List<String> lnP2List = lnP2Writer();
        int sizeLnP2 = lnP2List.size();
        for (int i = 0; i < sizeLnP2; i++) {
            bufferedWriter.write(lnP2List.get(i));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        bufferedWriter.close();

    }

    private static List<String> headWriter() {

        List<String> headList = new ArrayList<>();
        headList.add("#GENERATOR: MUGChartConverter");
        headList.add("#PLAYER " + FormattedChart.getPlayer());
        headList.add("#TITLE " + FormattedChart.getTitle());
        headList.add("#ARTIST " + FormattedChart.getArtist());
        headList.add("#BPM " + FormattedChart.getBpm());
        headList.add("#RANK " + FormattedChart.getRank());
        headList.add("#LNTYPE 1");
        return headList;

    }

    private static List<String> wavWriter() {

        List<String> wavList = new ArrayList<>();
        int size = FormattedChart.wavList.size();
        for (int i = 0; i < size; i++) {
            String[] strings = FormattedChart.wavList.get(i);
            wavList.add("#WAV" + strings[0] + " " + strings[1]);
        }

        FormattedChart.wavList.clear();
        return wavList;

    }

    private static List<String> bpmWriter() {

        List<String> bpmList = new ArrayList<>();
        List<String> bpmInfoList = new ArrayList<>();//BPM
        List<String[]> group = new ArrayList<>();

        int size = FormattedChart.bpmList.size();
        int index = 0;
        for (int i = 0; i < size; i++) {
            int flag = 1;

            String[] strings1 = FormattedChart.bpmList.get(i);
            String bpm = strings1[1];
            for (int j = 0; j < bpmInfoList.size(); j++) {
                String strings2 = bpmInfoList.get(j);
                if (bpm.equals(strings2)) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 0) {
                continue;
            }
            String tag = Integer.toString(index + 1, 36).toUpperCase();
            if (tag.length() == 1) {
                tag = "0" + tag;
            }
            bpmInfoList.add(bpm);
            bpmList.add("#BPM" + tag + " " + bpm);
            index++;
        }

        if (size > 0) {

            String[] start = FormattedChart.bpmList.get(0);
            // String[] beatStart = {start[2], start[3], start[4]};
            // String[] start1 = Util.beatToGroup(beatStart);
            int indexOfStart = bpmInfoList.indexOf(start[1]);
            String bpmTagStart = Integer.toString(indexOfStart + 1, 36);
            if (bpmTagStart.length() == 1) {
                bpmTagStart = "0" + bpmTagStart;
            }
            group.add(new String[]{start[2], start[3], start[4], bpmTagStart});

            for (int i = 1; i < size; i++) {
                String[] strings = FormattedChart.bpmList.get(i);
                //String[] beat = {strings[2], strings[3], strings[4]};
                //String[] toGroup = Util.beatToGroup(beat);

                String[] strings1 = FormattedChart.bpmList.get(i - 1);
                //String[] beat1 = {strings1[2], strings1[3], strings1[4]};
                //String[] toGroup1 = Util.beatToGroup(beat1);

                int indexOf = bpmInfoList.indexOf(strings[1]);
                String bpmTag = Integer.toString(indexOf + 1, 36).toUpperCase();
                if (bpmTag.length() == 1) {
                    bpmTag = "0" + bpmTag;
                }

                if (!strings[2].equals(strings1[2]) || Integer.parseInt(strings[0]) < Integer.parseInt(strings1[0])) {
                    String arrangeNote = Util.arrangeNote(group);
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer append = buffer.append(arrangeNote, 0, 4).append("08:").append(arrangeNote.substring(4));
                    bpmList.add(String.valueOf(append));
                    group.clear();
                    group.add(new String[]{strings[2], strings[3], strings[4], bpmTag});
                    continue;
                }
                group.add(new String[]{strings[2], strings[3], strings[4], bpmTag});
            }
            if (!group.isEmpty()) {
                String arrangeNote = Util.arrangeNote(group);
                StringBuffer buffer = new StringBuffer();
                StringBuffer append = buffer.append(arrangeNote, 0, 4).append("08:").append(arrangeNote.substring(4));
                bpmList.add(String.valueOf(append));
                group.clear();
            }

        }

        FormattedChart.bpmList.clear();
        return bpmList;
    }

    private static List<String> bgmWriter() {

        List<String> bgmList = new ArrayList<>();
        List<String[]> group = new ArrayList<>();

        String[] start = FormattedChart.bgmList.get(0);
        //String[] beatStart = {start[0], start[1], start[2]};
        //String[] start1 = Util.beatToGroup(beatStart);

        group.add(new String[]{start[0], start[1], start[2], start[3]});

        int size = FormattedChart.bgmList.size();
        for (int i = 1; i < size; i++) {
            String[] strings = FormattedChart.bgmList.get(i);
            //String[] beat = {strings[0], strings[1], strings[2]};
            //String[] toGroup = Util.beatToGroup(beat);

            String[] strings1 = FormattedChart.bgmList.get(i - 1);
            //String[] beat1 = {strings1[0], strings1[1], strings1[2]};
            //String[] toGroup1 = Util.beatToGroup(beat1);

            if (!strings[0].equals(strings1[0]) || Integer.parseInt(strings[4]) < Integer.parseInt(strings1[4])) {
                String arrangeNote = Util.arrangeNote(group);
                StringBuffer buffer = new StringBuffer();
                StringBuffer append = buffer.append(arrangeNote, 0, 4).append("01:").append(arrangeNote.substring(4));
                bgmList.add(String.valueOf(append));
                group.clear();
                group.add(new String[]{strings[0], strings[1], strings[2], strings[3]});
                continue;
            }
            group.add(new String[]{strings[0], strings[1], strings[2], strings[3]});
        }
        if (!group.isEmpty()) {
            String arrangeNote = Util.arrangeNote(group);
            StringBuffer buffer = new StringBuffer();
            StringBuffer append = buffer.append(arrangeNote, 0, 4).append("01:").append(arrangeNote.substring(4));
            bgmList.add(String.valueOf(append));
            group.clear();
        }

        FormattedChart.bgmList.clear();
        return bgmList;
    }

    private static List<String> noteP1Writer() {

        List<String> noteP1List = new ArrayList<>();
        List<String[]> groupLine = new ArrayList<>();

        List<String[]> noteList = FormattedChart.noteP1List;
        String[] info = {"16:", "11:", "12:", "13:", "14:", "15:", "18:", "19:"};
        for (int j = 1; j <= 8; j++) {

            List<String[]> noteP1Line = new ArrayList<>();

            for (int i = 0; i < noteList.size(); i++) {
                String[] strings = noteList.get(i);
                if (Integer.valueOf(strings[3]) == j) {
                    noteP1Line.add(strings);
                }
            }

            if (noteP1Line.isEmpty()) {
                continue;
            }

            String[] start = noteP1Line.get(0);
            //String[] beatStart = {start[0], start[1], start[2]};
            //String[] toGroup = Util.beatToGroup(beatStart);
            groupLine.add(new String[]{start[0], start[1], start[2], start[4]});

            for (int i = 1; i < noteP1Line.size(); i++) {

                String[] strings1 = noteP1Line.get(i);
                //String[] beatStart1 = {strings1[0], strings1[1], strings1[2]};
                //String[] toGroup1 = Util.beatToGroup(beatStart1);

                String[] strings2 = noteP1Line.get(i - 1);
                //String[] beatStart2 = {strings2[0], strings2[1], strings2[2]};
                //String[] toGroup2 = Util.beatToGroup(beatStart2);

                if (!strings1[0].equals(strings2[0])) {
                    String s = Util.arrangeNote(groupLine);
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer append = buffer.append(s, 0, 4).append(info[j - 1]).append(s.substring(4));
                    noteP1List.add(String.valueOf(append));
                    groupLine.clear();
                    groupLine.add(new String[]{strings1[0], strings1[1], strings1[2], strings1[4]});
                    continue;
                }
                groupLine.add(new String[]{strings1[0], strings1[1], strings1[2], strings1[4]});

            }
            if (!groupLine.isEmpty()) {
                String s = Util.arrangeNote(groupLine);
                StringBuffer buffer = new StringBuffer();
                StringBuffer append = buffer.append(s, 0, 4).append(info[j - 1]).append(s.substring(4));
                noteP1List.add(String.valueOf(append));
                groupLine.clear();
            }
        }

        FormattedChart.noteP1List.clear();
        return noteP1List;

    }

    private static List<String> lnP1Writer() {

        List<String> lnP1List = new ArrayList<>();
        List<String[]> groupLine = new ArrayList<>();

        List<String[]> noteList = FormattedChart.lnP1List;
        String[] info = {"56:", "51:", "52:", "53:", "54:", "55:", "58:", "59:"};
        for (int j = 1; j <= 8; j++) {

            List<String[]> lnP1Line = new ArrayList<>();

            for (int i = 0; i < noteList.size(); i++) {
                String[] strings = noteList.get(i);
                if (Integer.valueOf(strings[3]) == j) {
                    lnP1Line.add(strings);
                }
            }

            if (lnP1Line.isEmpty()) {
                continue;
            }

            String[] start = lnP1Line.get(0);
            //String[] beatStart = {start[0], start[1], start[2]};
            //String[] toGroup = Util.beatToGroup(beatStart);
            groupLine.add(new String[]{start[0], start[1], start[2], start[4]});

            for (int i = 1; i < lnP1Line.size(); i++) {

                String[] strings1 = lnP1Line.get(i);
                //String[] beatStart1 = {strings1[0], strings1[1], strings1[2]};
                //String[] toGroup1 = Util.beatToGroup(beatStart1);

                String[] strings2 = lnP1Line.get(i - 1);
                //String[] beatStart2 = {strings2[0], strings2[1], strings2[2]};
                //String[] toGroup2 = Util.beatToGroup(beatStart2);

                if (!strings1[0].equals(strings2[0])) {
                    String s = Util.arrangeNote(groupLine);
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer append = buffer.append(s, 0, 4).append(info[j - 1]).append(s.substring(4));
                    lnP1List.add(String.valueOf(append));
                    groupLine.clear();
                    groupLine.add(new String[]{strings1[0], strings1[1], strings1[2], strings1[4]});
                    continue;
                }
                groupLine.add(new String[]{strings1[0], strings1[1], strings1[2], strings1[4]});

            }
            if (!groupLine.isEmpty()) {
                String s = Util.arrangeNote(groupLine);
                StringBuffer buffer = new StringBuffer();
                StringBuffer append = buffer.append(s, 0, 4).append(info[j - 1]).append(s.substring(4));
                lnP1List.add(String.valueOf(append));
                groupLine.clear();
            }
        }

        FormattedChart.lnP1List.clear();
        return lnP1List;

    }

    private static List<String> noteP2Writer() {

        List<String> noteP2List = new ArrayList<>();
        List<String[]> groupLine = new ArrayList<>();

        List<String[]> noteList = FormattedChart.noteP2List;
        String[] info = {"21:", "22:", "23:", "24:", "25:", "28:", "29:", "26:"};
        for (int j = 1; j <= 8; j++) {

            List<String[]> noteP2Line = new ArrayList<>();

            for (int i = 0; i < noteList.size(); i++) {
                String[] strings = noteList.get(i);
                if (Integer.valueOf(strings[3]) == j) {
                    noteP2Line.add(strings);
                }
            }

            if (noteP2Line.isEmpty()) {
                continue;
            }

            String[] start = noteP2Line.get(0);
            //String[] beatStart = {start[0], start[1], start[2]};
            //String[] toGroup = Util.beatToGroup(beatStart);
            groupLine.add(new String[]{start[0], start[1], start[2], start[4]});

            for (int i = 1; i < noteP2Line.size(); i++) {

                String[] strings1 = noteP2Line.get(i);
                //String[] beatStart1 = {strings1[0], strings1[1], strings1[2]};
                //String[] toGroup1 = Util.beatToGroup(beatStart1);

                String[] strings2 = noteP2Line.get(i - 1);
                //String[] beatStart2 = {strings2[0], strings2[1], strings2[2]};
                //String[] toGroup2 = Util.beatToGroup(beatStart2);

                if (!strings1[0].equals(strings2[0])) {
                    String s = Util.arrangeNote(groupLine);
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer append = buffer.append(s, 0, 4).append(info[j - 1]).append(s.substring(4));
                    noteP2List.add(String.valueOf(append));
                    groupLine.clear();
                    groupLine.add(new String[]{strings1[0], strings1[1], strings1[2], strings1[4]});
                    continue;
                }
                groupLine.add(new String[]{strings1[0], strings1[1], strings1[2], strings1[4]});

            }
            if (!groupLine.isEmpty()) {
                String s = Util.arrangeNote(groupLine);
                StringBuffer buffer = new StringBuffer();
                StringBuffer append = buffer.append(s, 0, 4).append(info[j - 1]).append(s.substring(4));
                noteP2List.add(String.valueOf(append));
                groupLine.clear();
            }
        }

        FormattedChart.noteP2List.clear();
        return noteP2List;

    }

    private static List<String> lnP2Writer() {

        List<String> lnP2List = new ArrayList<>();
        List<String[]> groupLine = new ArrayList<>();

        List<String[]> noteList = FormattedChart.lnP2List;
        String[] info = {"61:", "62:", "63:", "64:", "65:", "68:", "69:", "66:"};
        for (int j = 1; j <= 8; j++) {

            List<String[]> lnP2Line = new ArrayList<>();

            for (int i = 0; i < noteList.size(); i++) {
                String[] strings = noteList.get(i);
                if (Integer.valueOf(strings[3]) == j) {
                    lnP2Line.add(strings);
                }
            }

            if (lnP2Line.isEmpty()) {
                continue;
            }

            String[] start = lnP2Line.get(0);
            //String[] beatStart = {start[0], start[1], start[2]};
            //String[] toGroup = Util.beatToGroup(beatStart);
            groupLine.add(new String[]{start[0], start[1], start[2], start[4]});

            for (int i = 1; i < lnP2Line.size(); i++) {

                String[] strings1 = lnP2Line.get(i);
                //String[] beatStart1 = {strings1[0], strings1[1], strings1[2]};
                //String[] toGroup1 = Util.beatToGroup(beatStart1);

                String[] strings2 = lnP2Line.get(i - 1);
                //String[] beatStart2 = {strings2[0], strings2[1], strings2[2]};
                //String[] toGroup2 = Util.beatToGroup(beatStart2);

                if (!strings1[0].equals(strings2[0])) {
                    String s = Util.arrangeNote(groupLine);
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer append = buffer.append(s, 0, 4).append(info[j - 1]).append(s.substring(4));
                    lnP2List.add(String.valueOf(append));
                    groupLine.clear();
                    groupLine.add(new String[]{strings1[0], strings1[1], strings1[2], strings1[4]});
                    continue;
                }
                groupLine.add(new String[]{strings1[0], strings1[1], strings1[2], strings1[4]});

            }
            if (!groupLine.isEmpty()) {
                String s = Util.arrangeNote(groupLine);
                StringBuffer buffer = new StringBuffer();
                StringBuffer append = buffer.append(s, 0, 4).append(info[j - 1]).append(s.substring(4));
                lnP2List.add(String.valueOf(append));
                groupLine.clear();
            }
        }

        FormattedChart.lnP2List.clear();
        return lnP2List;

    }

}
