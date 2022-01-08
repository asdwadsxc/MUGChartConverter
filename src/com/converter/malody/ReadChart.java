package com.converter.malody;

import com.converter.bmx.WriteChart;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author asdwadsxc
 * @create 2022-01-07 9:14
 */
public class ReadChart {

    public static void readChart(String path) throws IOException {

        JSONTokener jsonTokener = new JSONTokener(new FileReader(new File(path)));
        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();

        JSONObject metaInfo = jsonObject.getJSONObject("meta");
        int mode = metaInfo.getInt("mode");
        if (mode != 0) {
            System.out.println("Unsupported mode");
            return;
        }
        ChartList.setMetaInfo(metaInfo.toString());

        JSONArray timeList = jsonObject.getJSONArray("time");
        for (int i = 0; i < timeList.length(); i++) {
            JSONObject time = (JSONObject) timeList.get(i);
            ChartList.bpmInfo.add(time.toString());
        }

        JSONArray noteList = jsonObject.getJSONArray("note");
        for (int i = 0; i < noteList.length(); i++) {
            JSONObject note = (JSONObject) noteList.get(i);
            //音频编号
            if (note.has("sound")) {
                String sound = note.getString("sound");
                if (!ChartList.wavInfo.contains(sound)) {
                    ChartList.wavInfo.add(sound);
                }
            }
            //提取音符类型
            if (note.has("endbeat")) {
                ChartList.lnNoteInfo.add(note.toString());
            } else if (!note.has("column")) {
                ChartList.bgmInfo.add(note.toString());
            } else {
                ChartList.noteInfo.add(note.toString());
            }
        }

        if (ChartList.wavInfo.size() > 1295) {
            System.out.println("Sound count out of index.");
            return;
        }

        Analyze.analyze();

        for (int i = 0; i < ChartList.wavInfo.size(); i++) {
            System.out.println(ChartList.wavInfo.get(i));
        }

        int i = path.indexOf(".mc");
        path = path.substring(0, i) + ".bms";
        WriteChart.writeChart(path);

        System.out.println("Successful");
    }
}
