package com.converter.o2jamNew;

import java.math.BigDecimal;

/**
 * @author asdwadsxc
 * @create 2022-01-04 11:54
 */
public class Util {

    //小数化分数
    public static String[] decimalToFraction(double num) {

        BigDecimal bigDecimal = BigDecimal.valueOf(num);

        String s = String.valueOf(bigDecimal);

        if (!s.contains(".")) {
            return new String[]{"0","1"};
        }

        String[] split = s.split("\\.");

        int value = Integer.parseInt(split[1]);

        int base;
        base = (int) Math.pow(10, split[1].length());
        int gcd = findGCD(value, base);
        return new String[]{String.valueOf((value / gcd)), String.valueOf((base / gcd))};
    }

    //求最大公约数
    private static int findGCD(int a, int b) {
        if (a == 0) {
            return b;
        }
        return findGCD(b % a, a);
    }

}
