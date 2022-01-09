package com.converter.malody;

import com.converter.formatted.FormattedChart;

import java.util.List;

/**
 * @author asdwadsxc
 * @create 2022-01-07 10:21
 */
public class Util {

    public static int[] simplifyBeat(int beat1, int beat2) {

        int gcd = findGCD(beat1, beat2);

        return new int[]{beat1 / gcd, beat2 / gcd};
    }

    //求最大公约数
    private static int findGCD(int a, int b) {
        if (a == 0) {
            return b;
        }
        return findGCD(b % a, a);
    }

    public static int[] groupToBeat(String[] strings) {

        int beat0 = Integer.parseInt(strings[0]);
        int beat1 = Integer.parseInt(strings[1]);
        int beat2 = Integer.parseInt(strings[2]);

        int result0 = beat0 * 4 + (beat1 * 4) / beat2;
        int result1 = (beat1 * 4) % beat2;

        int[] ints = simplifyBeat(result1, beat2);

        return new int[]{result0, ints[0], ints[1]};
    }

    public static String findSound(String index) {

        for (int i = 0; i < FormattedChart.wavList.size(); i++) {
            String[] strings = FormattedChart.wavList.get(i);
            if (strings[0].equals(index)) {
                return strings[1];
            }
        }
        return null;
    }
}
