package com.converter.o2jamNew;

import com.converter.bmx.WriteChart;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * @author asdwadsxc
 * @create 2022-01-04 8:57
 */
public class ReadChart {

    public static void readChart(String path) throws JDOMException, IOException {

        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(new File(path));

        System.out.print("Please enter the BPM of the song: ");
        Scanner scanner = new Scanner(System.in);
        ChartList.setBpm(scanner.nextDouble());

        String dir = path.substring(0, path.lastIndexOf("\\")) + "\\output";
        File dir1 = new File(dir);

        String wavName = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf(".")) + ".mp3";

        Element rootElement = document.getRootElement();
        List O2ModeList = rootElement.getChildren("O2Mode");

        for (int i = 0; i < O2ModeList.size(); i++) {
            Element o2Mode = (Element) O2ModeList.get(i);
            String nDifficuty = o2Mode.getAttributeValue("nDifficuty");
            String nKeyCount = o2Mode.getAttributeValue("nKeyCount");

            String[] difficultyName = {"easy", "normal", "hard", "other_1", "other_2", "other_3", "other_4"};

            List o2ChartList = o2Mode.getChildren("O2Track");
            int flag = 1;
            if(o2ChartList.isEmpty()){
                continue;
            }
            for (int j = 0; j < o2ChartList.size(); j++) {
                Element o2Track = (Element) o2ChartList.get(j);
                String nType = o2Track.getAttributeValue("nType");
                switch (nType) {
                    case "1":
                        List o2BpmInfoList = o2Track.getChildren("O2NoteInfo");
                        for (int k = 0; k < o2BpmInfoList.size(); k++) {
                            Element o2BpmInfo = (Element) o2BpmInfoList.get(k);
                            String nTune = o2BpmInfo.getAttributeValue("nTune");
                            String fGrid = o2BpmInfo.getAttributeValue("fGrid");
                            String fData = o2BpmInfo.getAttributeValue("fData");
                            ChartList.bpmInfo.add(new String[]{nTune, fGrid, fData});
                        }
                        break;
                    case "2":
                        List o2NoteInfoList = o2Track.getChildren("O2NoteInfo");
                        for (int k = 0; k < o2NoteInfoList.size(); k++) {
                            Element o2NoteInfoNote = (Element) o2NoteInfoList.get(k);
                            String nTune = o2NoteInfoNote.getAttributeValue("nTune");
                            String fGrid = o2NoteInfoNote.getAttributeValue("fGrid");
                            String nTypeNote = o2NoteInfoNote.getAttributeValue("nType");
                            ChartList.noteInfo.add(new String[]{nTune, fGrid, nTypeNote, String.valueOf(flag)});
                        }
                        flag++;
                        break;
                    case "3":
                        List o2WavInfoList = o2Track.getChildren("O2NoteInfo");
                        Element o2WavInfoNote = (Element) o2WavInfoList.get(0);
                        String nTune = o2WavInfoNote.getAttributeValue("nTune");
                        String fGrid = o2WavInfoNote.getAttributeValue("fGrid");
                        ChartList.bgmInfo.add(new String[]{nTune, fGrid, wavName});
                        break;
                }
            }
            Analyze.analyze();

            if (!dir1.exists()) {
                dir1.mkdir();
            }

            String chartFile = dir + "\\" + nKeyCount + "k_" + difficultyName[Integer.parseInt(nDifficuty)] + ".bms";
            WriteChart.writeChart(chartFile);

            System.out.println("successful");

        }
    }
}
