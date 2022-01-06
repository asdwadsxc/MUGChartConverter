package com.converter.musynx;

import com.converter.bmx.WriteChart;
import com.converter.formatted.FormattedChart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author asdwadsxc
 * @create 2022-01-03 9:04
 */
public class ReadChart {

    public static void readChart(String path) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String text;

        while (true) {
            text = bufferedReader.readLine();
            if (text == null)
                break;
            String[] split = text.split("\t");
            if ("BPM".equals(split[0])) {
                FormattedChart.setBpm(split[1].trim());
            } else if ("Rank".equals(split[0])) {
                FormattedChart.setRank(Integer.parseInt(split[1].trim()));
            } else if ("WAV".equals(split[0])) {
                FormattedChart.wavList.add(new String[]{split[1], split[2] + ".wav"});
            } else if ("BPMChanger".equals(split[0])) {
                ChartList.bpmInfo.add(split);
            } else if ("MusicNote".equals(split[0])) {
                ChartList.bgmInfo.add(split);
            } else if ("Note".equals(split[0])) {
                ChartList.noteInfo.add(split);
            } else if ("LongNote".equals(split[0])) {
                ChartList.lnNoteInfo.add(split);
            }
        }

        Analyze.analyze();

        int i = path.indexOf(".txt");
        if (i < 0) {
            path = path + "_bms.bms";
        } else {
            path = path.substring(0, i) + ".bms";
        }

        WriteChart.writeChart(path);

        System.out.println("successful");

    }
}
