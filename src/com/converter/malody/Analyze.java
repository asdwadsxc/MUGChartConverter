package com.converter.malody;

import com.converter.formatted.FormattedChart;
import com.converter.util.BeatToGroup;
import com.converter.util.TimeToBeat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author asdwadsxc
 * @create 2022-01-07 10:44
 */
public class Analyze {

    public static void analyze() {

        ChartList.wavInfo.sort((Comparator<String>) (o1, o2) -> {
            String s1 = (String) o1;
            String s2 = (String) o2;
            return s1.compareTo(s2);
        });

        analyzeHead();
        analyzeWav();
        analyzeBgmNote(ChartList.bgmInfo);
        analyzeBpm();
        analyzeNote();
        analyzeLnNote();

        ChartList.wavInfo.clear();
        ChartList.bgmInfo.clear();
        ChartList.bpmInfo.clear();
        ChartList.noteInfo.clear();
        ChartList.lnNoteInfo.clear();

    }

    private static void analyzeHead() {

        String metaInfo = ChartList.getMetaInfo();
        JSONTokener jsonTokener = new JSONTokener(metaInfo);
        JSONObject meta = (JSONObject) jsonTokener.nextValue();

        JSONObject mode_ext = (JSONObject) meta.get("mode_ext");
        int column = mode_ext.getInt("column");
        if (column > 8) {
            FormattedChart.setPlayer(2);
        } else {
            FormattedChart.setPlayer(1);
        }

        JSONObject song = (JSONObject) meta.get("song");
        String title = song.getString("title");
        String artist = song.getString("artist");
        FormattedChart.setTitle(title);
        FormattedChart.setArtist(artist);

        String bpmInfo = ChartList.bpmInfo.get(0);
        JSONTokener jsonTokener1 = new JSONTokener(bpmInfo);
        JSONObject bpm1 = (JSONObject) jsonTokener1.nextValue();
        String bpm = bpm1.get("bpm").toString();
        FormattedChart.setBpm(bpm);
        ChartList.bpmInfo.remove(0);

        FormattedChart.setRank(3);

    }

    private static void analyzeWav() {

        for (int i = 0; i < ChartList.wavInfo.size(); i++) {

            String name = ChartList.wavInfo.get(i);
            String index = Integer.toString(i + 1, 36).toUpperCase();
            if (index.length() == 1) {
                index = "0" + index;
            }

            FormattedChart.wavList.add(new String[]{index, name});
        }

    }

    private static void analyzeBpm() {

        for (int i = 0; i < ChartList.bpmInfo.size(); i++) {
            String timeInfo = ChartList.bpmInfo.get(i);
            JSONTokener jsonTokener = new JSONTokener(timeInfo);
            JSONObject time = (JSONObject) jsonTokener.nextValue();
            JSONArray beat = (JSONArray) time.get("beat");
            String bpm = time.get("bpm").toString();
            int beat0 = beat.getInt(0);
            int beat1 = beat.getInt(1);
            int beat2 = beat.getInt(2);
            int[] ints = Util.simplifyBeat(beat1, beat2);
            String[] beats = {String.valueOf(beat0), String.valueOf(ints[0]), String.valueOf(ints[1])};
            String[] toGroup = BeatToGroup.beatToGroup(beats);
            FormattedChart.bpmList.add(new String[]{String.valueOf(i), bpm, toGroup[0], toGroup[1], toGroup[2], String.valueOf(i)});
        }

    }

    private static void analyzeBgmNote(List<String> bgmInfo) {

        List<String> bgmInfoRe = new ArrayList<>();
        int[] flag = {0, 0, 0};

        for (int i = 0; i < bgmInfo.size(); i++) {

            String s = bgmInfo.get(i);
            JSONTokener jsonTokener = new JSONTokener(s);
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();

            JSONArray beat = (JSONArray) jsonObject.get("beat");
            int beat0 = beat.getInt(0);
            int beat1 = beat.getInt(1);
            int beat2 = beat.getInt(2);
            int[] ints = Util.simplifyBeat(beat1, beat2);

            if (jsonObject.has("offset")) {
                int offset = jsonObject.getInt("offset");
                if (offset >= 0) {
                    double beatCount = beat0 + ((double) beat1 / beat2);
                    double beatTime = (1000 * 60) / Double.parseDouble(FormattedChart.getBpm());
                    double offsetTimeD = beatCount * beatTime + offset;
                    int offsetTime = (int) Math.round(offsetTimeD);
                    String[] toBeat = TimeToBeat.timeToBeat(offsetTime);

                    beat0 = Integer.parseInt(toBeat[0]);
                    ints[0] = Integer.parseInt(toBeat[1]);
                    ints[1] = Integer.parseInt(toBeat[2]);
                }
            }

            if (beat0 != flag[0] || ints[0] != flag[1] || ints[1] != flag[2]) {
                String sound = jsonObject.getString("sound");
                int indexOf = ChartList.wavInfo.indexOf(sound);
                String index = Integer.toString(indexOf + 1, 36).toUpperCase();
                if (index.length() == 1) {
                    index = "0" + index;
                }

                String[] beats = {String.valueOf(beat0), String.valueOf(ints[0]), String.valueOf(ints[1])};
                String[] toGroup = BeatToGroup.beatToGroup(beats);

                flag[0] = beat0;
                flag[1] = ints[0];
                flag[2] = ints[1];

                FormattedChart.bgmList.add(new String[]{toGroup[0], toGroup[1], toGroup[2], index, String.valueOf(i)});
            } else {
                bgmInfoRe.add(s);
            }
        }

        if (!bgmInfoRe.isEmpty()) {
            analyzeBgmNote(bgmInfoRe);
        }

    }

    private static void analyzeNote() {

        for (int i = 0; i < ChartList.noteInfo.size(); i++) {
            String noteInfo = ChartList.noteInfo.get(i);
            JSONTokener jsonTokener = new JSONTokener(noteInfo);
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            JSONArray beat = (JSONArray) jsonObject.get("beat");
            int beat0 = beat.getInt(0);
            int beat1 = beat.getInt(1);
            int beat2 = beat.getInt(2);
            int[] ints = Util.simplifyBeat(beat1, beat2);
            String[] beats = {String.valueOf(beat0), String.valueOf(ints[0]), String.valueOf(ints[1])};
            String[] toGroup = BeatToGroup.beatToGroup(beats);
            int column = jsonObject.getInt("column");
            if (jsonObject.has("sound")) {
                String sound = jsonObject.getString("sound");
                int indexOf = ChartList.wavInfo.indexOf(sound);
                String index = Integer.toString(indexOf + 1, 36).toUpperCase();
                if (index.length() == 1) {
                    index = "0" + index;
                }
                FormattedChart.noteP1List.add(new String[]{toGroup[0], toGroup[1], toGroup[2], String.valueOf(column + 1), index});
            } else {
                int size = ChartList.wavInfo.size();
                String index = Integer.toString(size + 1, 36).toUpperCase();
                if (index.length() == 1) {
                    index = "0" + index;
                }
                FormattedChart.noteP1List.add(new String[]{toGroup[0], toGroup[1], toGroup[2], String.valueOf(column + 1), index});
            }
        }
    }

    private static void analyzeLnNote() {

        for (int i = 0; i < ChartList.lnNoteInfo.size(); i++) {
            String noteInfo = ChartList.lnNoteInfo.get(i);
            JSONTokener jsonTokener = new JSONTokener(noteInfo);
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            JSONArray beat = (JSONArray) jsonObject.get("beat");
            int beat0 = beat.getInt(0);
            int beat1 = beat.getInt(1);
            int beat2 = beat.getInt(2);
            int[] ints = Util.simplifyBeat(beat1, beat2);
            String[] beats = {String.valueOf(beat0), String.valueOf(ints[0]), String.valueOf(ints[1])};
            String[] toGroup = BeatToGroup.beatToGroup(beats);

            JSONArray endbeat = (JSONArray) jsonObject.get("endbeat");
            int endbeat0 = endbeat.getInt(0);
            int endbeat1 = endbeat.getInt(1);
            int endbeat2 = endbeat.getInt(2);
            int[] endints = Util.simplifyBeat(endbeat1, endbeat2);
            String[] endbeats = {String.valueOf(endbeat0), String.valueOf(endints[0]), String.valueOf(endints[1])};
            String[] endToGroup = BeatToGroup.beatToGroup(endbeats);

            int column = jsonObject.getInt("column");

            if (jsonObject.has("sound")) {
                String sound = jsonObject.getString("sound");
                int indexOf = ChartList.wavInfo.indexOf(sound);
                String index = Integer.toString(indexOf + 1, 36).toUpperCase();
                if (index.length() == 1) {
                    index = "0" + index;
                }
                FormattedChart.lnP1List.add(new String[]{toGroup[0], toGroup[1], toGroup[2], String.valueOf(column + 1), index});
                FormattedChart.lnP1List.add(new String[]{endToGroup[0], endToGroup[1], endToGroup[2], String.valueOf(column + 1), index});
            } else {
                int size = ChartList.wavInfo.size();
                String index = Integer.toString(size + 1, 36).toUpperCase();
                if (index.length() == 1) {
                    index = "0" + index;
                }
                FormattedChart.lnP1List.add(new String[]{toGroup[0], toGroup[1], toGroup[2], String.valueOf(column + 1), index});
                FormattedChart.lnP1List.add(new String[]{endToGroup[0], endToGroup[1], endToGroup[2], String.valueOf(column + 1), index});
            }
        }
    }
}
