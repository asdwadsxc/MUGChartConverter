package com.converter.malody;

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

}
