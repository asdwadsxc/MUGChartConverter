package com.converter.iidx;

import com.converter.formatted.FormattedChart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author asdwadsxc
 * @create 2021-12-31 18:33
 */
public class ExtractAudio {

    public static int extractAudio(String audioFile) throws IOException {

        FormattedChart.wavList.clear();

        String dir = audioFile.substring(0, audioFile.lastIndexOf("\\")) + "\\sounds\\";
        File audio = new File(audioFile);
        FileInputStream fis = new FileInputStream(audio);

        byte[] head = new byte[4];
        fis.read(head);

        byte[] count = new byte[4];
        fis.read(count);
        int anInt = ByteBuffer.wrap(count).order(ByteOrder.LITTLE_ENDIAN).getInt();

        if (anInt > 1295) {
            return -1;
        }

        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        byte[] index = new byte[4];
        fis.read(index);
        int anInt2 = ByteBuffer.wrap(index).order(ByteOrder.LITTLE_ENDIAN).getInt();

        byte[] other = new byte[anInt2 - 12];
        fis.read(other);

        for (int i = 0; i < anInt; i++) {

            byte[] soundHead = new byte[8];
            fis.read(soundHead);

            byte[] soundLength = new byte[4];
            fis.read(soundLength);
            int anInt1 = ByteBuffer.wrap(soundLength).order(ByteOrder.LITTLE_ENDIAN).getInt();

            byte[] offset = new byte[20];
            fis.read(offset);

            byte[] mainSound = new byte[anInt1];
            fis.read(mainSound);

            String s = Integer.toString(i + 1, 36).toUpperCase();
            if (s.length() == 1) {
                s = "0" + s;
            }

            ChartList.wavInfo.add(new String[]{s, "sounds\\" + s + ".wav"});

            FileOutputStream fos = new FileOutputStream(dir + s + ".wma");
            fos.write(mainSound);

        }

        fis.close();
        return anInt;

    }
}
