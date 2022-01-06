package com.converter.util;

import com.converter.formatted.FormattedChart;

import java.text.NumberFormat;

/**
 * @author asdwadsxc
 * @create 2021-12-29 16:36
 */
public class TimeToBeat {

    public static String[] timeToBeat(Integer time) {

        double bpm = Double.parseDouble(FormattedChart.getBpm());
        double result = 0.0;
        Integer baseTime = 0;


        for (int i = FormattedChart.bpmList.size(); i > 0; i--) {
            String[] strings = FormattedChart.bpmList.get(i - 1);
            if (time > Integer.parseInt(strings[0])) {
                baseTime = Integer.valueOf(strings[0]);
                bpm = Double.parseDouble(strings[1]);
                result = Double.parseDouble(strings[5]);
                break;
            }
        }

        Double now = ((time - baseTime) / ((60 * 1000) / bpm)) + result;

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);
        String format1 = nf.format(now);
        Double approximation = approximation(format1);
        String format = nf.format(approximation);

        String[] strings;
        if (!format.contains(".")) {
            strings = new String[]{format, "0"};
        } else {
            strings = format.split("\\.");
        }
        String s = decimalToFraction(strings[1]);
        String[] split = s.split("/");

        return new String[]{strings[0], split[0], split[1], String.valueOf(now)};//节拍[x,x,x] ， 计算结果
    }

    //取近似值
    private static Double approximation(String string) {

        double result = Double.parseDouble(string);

        if (string.contains(".")) {

            String[] split = string.split("\\.");

            if (split[1].length() < 1) {
                return result;
            }
            if (split[1].equals("003")) {
                return result - 0.003;
            }

            if (split[1].equals("998") || split[1].equals("248") || split[1].equals("748") || split[1].equals("498")) {
                return result + 0.002;
            }

            if (split[1].equals("376")) {
                return result - 0.001;
            }

            if (split[1].equals("624")) {
                return result + 0.001;
            }

            if (split[1].charAt(split[1].length() - 1) == '9') {
                return result + Math.pow(0.1, split[1].length());
            }

            if (split[1].charAt(split[1].length() - 1) == '1') {
                return result - Math.pow(0.1, split[1].length());
            }
        }
        
        //System.out.println(result);
        return result;
    }

    //小数化分数
    public static String decimalToFraction(String num) {

        int value = Integer.parseInt(num);
        int base;
        base = (int) Math.pow(10, num.length());
        int gcd = findGCD(value, base);
        return (value / gcd) + "/" + (base / gcd);
    }

    //求最大公约数
    private static int findGCD(int a, int b) {
        if (a == 0) {
            return b;
        }
        return findGCD(b % a, a);
    }

}
