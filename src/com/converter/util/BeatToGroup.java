package com.converter.util;

/**
 * @author asdwadsxc
 * @create 2022-01-04 14:04
 */
public class BeatToGroup {

    public static String[] beatToGroup(String[] beat) {

        String[] result = new String[3];

        int base0 = Integer.parseInt(beat[0]);
        int base1 = Integer.parseInt(beat[1]);
        int base2 = Integer.parseInt(beat[2]);

        int base = base0 % 4;

        int result0 = base0 / 4;
        int result1 = base * base2 + base1;
        int result2 = base2 * 4;

        result[0] = String.valueOf(result0);
        result[1] = String.valueOf(result1);
        result[2] = String.valueOf(result2);

        return result;
    }

}
