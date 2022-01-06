package com.converter.o2jamNew;

import com.converter.formatted.FormattedChart;

/**
 * @author asdwadsxc
 * @create 2022-01-04 11:33
 */
public class Analyze {

    public static void analyze() {

        analyzeHead();

        analyzeBpm();
        ChartList.bpmInfo.clear();

        analyzeBgmNote();
        ChartList.bgmInfo.clear();

        analyzeNote();
        ChartList.noteInfo.clear();

    }

    private static void analyzeHead() {

        FormattedChart.setArtist("");
        FormattedChart.setTitle("");
        FormattedChart.setPlayer(1);
        FormattedChart.setRank(3);
        FormattedChart.setBpm(String.valueOf(ChartList.getBpm()));

    }

    private static void analyzeBpm() {

        for (int i = 0; i < ChartList.bpmInfo.size(); i++) {
            String[] strings = ChartList.bpmInfo.get(i);
            int result1 = Integer.parseInt(strings[0]);
            double result2 = Double.parseDouble(strings[1]);
            double bpmOri = Double.parseDouble(strings[2]);
            double bpm1 = bpmOri * ChartList.getBpm();
            double bpm = Math.round(bpm1 * 10000) / 10000.0;

            if (result2 == 1) {
                result1 = result1 + 1;
                result2 = 0;
            }

            double l = Math.round(result2 * 10000) / 10000.0;

            String[] toFraction = Util.decimalToFraction(l);

            FormattedChart.bpmList.add(new String[]{String.valueOf(i), String.valueOf(bpm), String.valueOf(result1), toFraction[0], toFraction[1], "1"});//时间，BPM，节拍[x,x,x]，节拍计算结果

        }
    }

    private static void analyzeBgmNote() {

        String[] strings = ChartList.bgmInfo.get(0);

        FormattedChart.wavList.add(new String[]{"01", strings[2]});

        int result1 = Integer.parseInt(strings[0]);
        double result2 = Double.parseDouble(strings[1]);

        if (result2 == 1) {
            result1 = result1 + 1;
            result2 = 0;
        }

        double l = Math.round(result2 * 10000) / 10000.0;
        String[] toFraction = Util.decimalToFraction(l);

        FormattedChart.bgmList.add(new String[]{String.valueOf(result1), toFraction[0], toFraction[1], "01", "11"});
    }

    private static void analyzeNote() {

        for (int i = 0; i < ChartList.noteInfo.size(); i++) {

            String[] strings = ChartList.noteInfo.get(i);

            int result1 = Integer.parseInt(strings[0]);
            double result2 = Double.parseDouble(strings[1]);

            if (result2 == 1) {
                result1 = result1 + 1;
                result2 = 0;
            }

            double l = Math.round(result2 * 10000) / 10000.0;
            String[] toFraction = Util.decimalToFraction(l);

            switch (strings[2]) {
                case "0":
                    FormattedChart.noteP1List.add(new String[]{String.valueOf(result1), toFraction[0], toFraction[1], strings[3], "02"});
                    break;
                case "2":
                case "3":
                    FormattedChart.lnP1List.add(new String[]{String.valueOf(result1), toFraction[0], toFraction[1], strings[3], "02"});
                    break;
            }
        }
    }
}
