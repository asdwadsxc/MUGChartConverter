package com.converter.malody;

import com.converter.formatted.FormattedChart;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author asdwadsxc
 * @create 2022-01-09 13:26
 */
public class WriteChart {

    public static void writeChart(String path) throws IOException {

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));

        JSONObject chart = new JSONObject();

        JSONObject meta = writeMeta();
        chart.put("meta", meta);

        JSONArray time = writeTime();
        chart.put("time", time);

        JSONArray effect = writeEffect();
        chart.put("effect", effect);

        JSONArray note = writeNote();
        chart.put("note", note);

        JSONObject extra = writeExtra();
        chart.put("extra", extra);

        bufferedWriter.write(chart.toString());
        bufferedWriter.flush();
        bufferedWriter.close();

    }

    private static JSONObject writeMeta() {

        JSONObject meta = new JSONObject();
        meta.put("$ver", 0);
        meta.put("creator", FormattedChart.getCreator());
        meta.put("background", "");
        meta.put("version", FormattedChart.getVersion());
        meta.put("mode", 0);

        JSONObject song = new JSONObject();
        song.put("title", FormattedChart.getTitle());
        song.put("artist", FormattedChart.getArtist());
        meta.put("song", song);

        JSONObject mode_ext = new JSONObject();
        mode_ext.put("column", FormattedChart.getColumn());
        meta.put("mode_ext", mode_ext);

        return meta;
    }

    private static JSONArray writeTime() {

        JSONArray time = new JSONArray();

        JSONObject bpm1 = new JSONObject();
        String theBpm = FormattedChart.getBpm();
        bpm1.put("beat", new int[]{0, 0, 1});
        bpm1.put("bpm", Double.parseDouble(theBpm));
        time.put(bpm1);

        for (int i = 0; i < FormattedChart.bpmList.size(); i++) {
            JSONObject bpmInfo = new JSONObject();
            String[] strings = FormattedChart.bpmList.get(i);
            double bpm = Double.parseDouble(strings[1]);
            int[] ints = Util.groupToBeat(new String[]{strings[2], strings[3], strings[4]});
            bpmInfo.put("beat", ints);
            bpmInfo.put("bpm", bpm);
            time.put(bpmInfo);
        }

        FormattedChart.bpmList.clear();
        return time;
    }

    private static JSONArray writeEffect() {

        JSONArray effect = new JSONArray();

        return effect;
    }

    private static JSONArray writeNote() {

        JSONArray Note = new JSONArray();

        //note
        for (int i = 0; i < FormattedChart.noteP1List.size(); i++) {
            JSONObject noteInfo = new JSONObject();
            String[] strings = FormattedChart.noteP1List.get(i);
            int[] ints = Util.groupToBeat(new String[]{strings[0], strings[1], strings[2]});
            int column = Integer.parseInt(strings[3]);
            noteInfo.put("beat", ints);
            noteInfo.put("column", column - 1);
            String sound = Util.findSound(strings[4]);
            if (sound != null) {
                noteInfo.put("sound", sound);
                noteInfo.put("vol", 100);
            }
            Note.put(noteInfo);
        }
        FormattedChart.noteP1List.clear();

        //lnNote
        for (int i = 0; i < FormattedChart.lnP1List.size(); i++) {
            JSONObject lnInfo = new JSONObject();
            String[] strings = FormattedChart.lnP1List.get(i++);
            String[] endStrings = FormattedChart.lnP1List.get(i);
            int[] ints = Util.groupToBeat(new String[]{strings[0], strings[1], strings[2]});
            int[] endInts = Util.groupToBeat(new String[]{endStrings[0], endStrings[1], endStrings[2]});
            int column = Integer.parseInt(strings[3]);
            lnInfo.put("beat", ints);
            lnInfo.put("endbeat", endInts);
            lnInfo.put("column", column - 1);
            String sound = Util.findSound(strings[4]);
            if (sound != null) {
                lnInfo.put("sound", sound);
                lnInfo.put("vol", 100);
            }
            Note.put(lnInfo);
        }
        FormattedChart.lnP1List.clear();

        //soundNote
        for (int i = 0; i < FormattedChart.bgmList.size(); i++) {
            JSONObject bgmInfo = new JSONObject();
            String[] strings = FormattedChart.bgmList.get(i++);
            int[] ints = Util.groupToBeat(new String[]{strings[0], strings[1], strings[2]});
            bgmInfo.put("beat", ints);
            bgmInfo.put("type", 1);
            String sound = Util.findSound(strings[3]);
            if (sound != null) {
                bgmInfo.put("sound", sound);
                bgmInfo.put("vol", 100);
            }
            Note.put(bgmInfo);
        }
        FormattedChart.bgmList.clear();

        return Note;
    }

    private static JSONObject writeExtra() {

        JSONObject extra = new JSONObject();

        JSONObject test = new JSONObject();
        test.put("divide", 4);
        test.put("speed", 100);
        test.put("save", 0);
        test.put("lock", 0);
        test.put("edit_mode", 0);

        extra.put("test", test);

        return extra;
    }

}
