package com.converter.iidx;

import com.converter.bmx.WriteChart;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * @author asdwadsxc
 * @create 2021-12-29 11:41
 */
public class ReadChart {

    public static void readChart(String path) throws IOException {

        String audioFile = path.substring(0, path.lastIndexOf(".")) + ".s3p";
        int extractAudio = ExtractAudio.extractAudio(audioFile);

        if (extractAudio < 0) {
            System.out.println("Sound count out of index.");
            return;
        }

        FileInputStream fis = new FileInputStream(path);
        byte[] length = new byte[8];
        fis.read(length);
        int headLength = ByteBuffer.wrap(length, 0, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        int chartCount = headLength / 8;
        int[] chartLength = new int[chartCount];
        chartLength[0] = ByteBuffer.wrap(length, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        for (int i = 1; i < chartLength.length; i++) {
            fis.read(length);
            chartLength[i] = ByteBuffer.wrap(length, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        }

        for (int i = 0; i < chartCount; i++) {

            if (chartLength[i] == 0) {
                continue;
            }

            while (true) {

                byte[] info = new byte[8];
                fis.read(info);
                String toString1 = Arrays.toString(info);
                byte[] end = {-1, -1, -1, 127, 0, 0, 0, 0};
                String toString2 = Arrays.toString(end);
                if (toString1.equals(toString2)) {
                    System.out.println(Arrays.toString(info));
                    break;
                }

                switch (info[4]) {
                    case 0:
                    case 2:
                        ChartList.noteP1Info.add(info);
                        break;
                    case 1:
                    case 3:
                        ChartList.noteP2Info.add(info);
                        break;
                    case 4:
                        ChartList.bpmInfo.add(info);
                        break;
                    case 7:
                        ChartList.bgmInfo.add(info);
                        break;
                }
                //System.out.println(Arrays.toString(info));
            }
            //byte[] end = new byte[8];
            //fis.read(end);
            //System.out.println(Arrays.toString(end));

            Analyze.analyze();
            String dir = path.substring(0, path.lastIndexOf(".")) + "_" + i + ".bms";
            WriteChart.writeChart(dir);

            System.out.println("successful");
        }
        fis.close();
    }
}
