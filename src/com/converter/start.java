package com.converter;

import org.jdom2.JDOMException;

import java.io.*;

/**
 * @author asdwadsxc
 * @create 2021-12-29 11:39
 */

public class start {

    public static void main(String[] args) throws IOException, JDOMException {

        //String path = "C:\\Users\\31062\\Desktop\\test\\mi_1_more_time.txt";
        String path = args[0];

        String[] split = path.split("\\\\");
        String fileName = split[split.length - 1];

        String[] file = fileName.split("\\.");
        String index = file[file.length - 1];

        switch (index) {
            case "1":
                com.converter.iidx.ReadChart.readChart(path);
                break;
            case "txt":
                identityTxt(path);
                break;
            case "mc":
                com.converter.malody.ReadChart.readChart(path);
                break;
            default:
                com.converter.musynx.ReadChart.readChart(path);
                break;
        }
    }

    private static void identityTxt(String path) throws IOException, JDOMException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String identity;

        while (true) {

            identity = bufferedReader.readLine();
            if (identity == null) {
                System.out.println("Unsupported");
                break;
            }
            if (identity.contains("NoteFactoryPrepare")) {
                bufferedReader.close();
                com.converter.musynx.ReadChart.readChart(path);
                break;
            }
            if (identity.contains("JamXML")) {
                bufferedReader.close();
                com.converter.o2jamNew.ReadChart.readChart(path);
                break;
            }
        }
    }
}
