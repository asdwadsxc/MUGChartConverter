package com.converter.bmx;

import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author asdwadsxc
 * @create 2022-01-01 12:06
 */
public class Util {

    public static String arrangeNote(List<String[]> noteList) {

        String string = "#";
        String[] strings = noteList.get(0);
        string += setFormat(Integer.parseInt(strings[0]), 3);

        int result = 1;//公倍数
        int flag = 0;
        for (int i = 0; i < noteList.size(); i++) {
            String[] strings1 = noteList.get(i);
            flag = Integer.parseInt(strings1[2]);
            if (result < flag) {
                flag = result;
                result = Integer.parseInt(strings1[2]);
            }
            if (isInteger(String.valueOf(result / (double) flag))) {
                continue;
            } else {
                result = get_lcm(result, flag);
            }
        }

        String[] value = new String[result];
        for (int i = 0; i < value.length; i++) {
            value[i] = "00";
        }
        for (int i = 0; i < noteList.size(); i++) {
            String[] strings1 = noteList.get(i);
            int i1 = result / Integer.parseInt(strings1[2]);
            value[(Integer.parseInt(strings1[1]) * i1)] = strings1[3];
        }
        for (int i = 0; i < value.length; i++) {
            string += value[i];
        }
        return string;
    }

    //求最大公约数
    private static int findGCD(int a, int b) {
        if (a == 0) {
            return b;
        }
        return findGCD(b % a, a);
    }

    //位数
    private static String setFormat(int i, int j) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMinimumIntegerDigits(j);
        String k = n.format(i);
        return k;
    }

    //最小公倍数
    private static int get_lcm(int n1, int n2) {
        return n1 * n2 / findGCD(n1, n2);
    }

    //判断整数
    private static boolean isInteger(String str) {

        Double aDouble = Double.valueOf(str);
        double d = Math.round(aDouble * 100) / 100.0;
        String s = String.valueOf(d);
        String[] split = s.split("\\.");
        return split[1].equals("0");

    }
}
