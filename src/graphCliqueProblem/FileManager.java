/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphCliqueProblem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author vern
 */
public class FileManager {

    private String path;
    private boolean append_to_file = true;

    private String dirDataPath;
    private String dirGraphPath;
    private String dirSamplesPath;

    private Map<Integer, int[]> mapNodes;

    int fileType = 0;

    public FileManager(int ft) {
        fileType = ft;
    }

    public FileManager() {
        append_to_file = true;
    }

    public FileManager(String file_path, boolean append_value) {
        path = file_path;
        append_to_file = append_value;
    }

    //map
    public Map<Integer, int[]> getMapNodes() {
        return this.mapNodes;
    }

    public void setMapNodes(Map<Integer, int[]> m) {
        this.mapNodes = m;
    }

    public void createDir(int graphNum) {
        String dirDATA = "DATA";
        String dirGraph = "Graph #";
        String dirAllSample = "AllSample";

        StringBuilder sb = new StringBuilder();
        sb.append(dirGraph);

        Path currentRelativePath = Paths.get("");
        File baseDir = new File(currentRelativePath.toAbsolutePath().toString());

        File subDirDATA = new File(baseDir, dirDATA);
        File subDirGraph = new File("");

        sb.append(graphNum);
        dirGraph = sb.toString();
        subDirGraph = new File(subDirDATA, dirGraph);

        File subDirSample = new File(subDirGraph, dirAllSample);

        checkDirExist(subDirDATA);
        checkDirExist(subDirGraph);
        checkDirExist(subDirSample);

        this.dirDataPath = subDirDATA.toString();
        this.dirGraphPath = subDirGraph.toString();
        this.dirSamplesPath = subDirSample.toString();
    }

    private void checkDirExist(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
    }

    public String getPathtoText(int fileType, int sampleNum) {
        String allSampleText = "Overall_Sample_Results.txt";
        String csvText = "E_S.csv";
        String sampleText = "sample #";
        String txtExt = ".txt";
        String csvText2 = "GRASP.csv";
        String csvText3 = "GRASP-Accuracy.csv";

        String graphTempText = "graphTemp.txt";
        String timeText = "time.txt";

        File fileInDir = new File("");

        switch (fileType) {
            //Overall sample
            case 1:

                fileInDir = new File(this.dirGraphPath, allSampleText);
                return fileInDir.getPath();

            //individual sample
            case 2:
                String textFile = sampleText + sampleNum + txtExt;
                fileInDir = new File(this.dirSamplesPath, textFile);
                return fileInDir.getPath();

            //csv file
            case 3:
//                String csvFile = csvText+ sampleNum + csvExt;
                fileInDir = new File(this.dirDataPath, csvText);
                return fileInDir.getPath();

            case 4:
                fileInDir = new File(this.dirDataPath, csvText2);
                return fileInDir.getPath();

            case 5:
                fileInDir = new File(this.dirDataPath, csvText3);
                return fileInDir.getPath();

        }

        return fileInDir.getPath();
    }

    public void writeToFile(String textLine) throws IOException {
        FileWriter write = new FileWriter(path, append_to_file);
        PrintWriter print_line = new PrintWriter(write);

        print_line.printf("%s" + "%n", textLine);

        print_line.close();
    }

    public void writeEdgesToFile(int numNodes, int node, List<Integer> edges) throws IOException {
//         FileWriter write = new FileWriter (path, append_to_file);
//         PrintWriter print_line = new PrintWriter (write);
//        
// 
//        print_line.printf("%d" + "%s",node, ":");
//        
//        for (int i = 0; i < edges.size(); i++)
//        {
//            print_line.printf("%d-",edges.get(i));
//        }
//        
//        print_line.printf( "%n");
//        
//        print_line.close();
    }

    public void writeOverallSampleHeader(int graphNum) throws IOException {
        String s = getPathtoText(1, graphNum);
        FileWriter write = new FileWriter(s, append_to_file);
        PrintWriter print_line = new PrintWriter(write);

        print_line.printf("%s" + "%d" + "%n%n", "Graph #", graphNum);

        print_line.printf("%s%n", "--------------------------------------------------------------------------------");
        print_line.printf(" %s\t" + "%s" + " %s\t" + " %s" + " %s\t" + " %s" + "%s\t" + " %s" + "%s\t" + " %s" + "%s\t" + "%n",
                "sample #", "|", "nodes #", "|", "edges #", "|", "k-clique  ", "|", "found", "|", "time (ms)");
        print_line.printf("%s%n", "--------------------------------------------------------------------------------");

        print_line.close();

    }

    public void writeOverallSample(Graph g, int sampleNum) throws IOException {
        String s = getPathtoText(1, 0);
        FileWriter write = new FileWriter(s, append_to_file);
        PrintWriter print_line = new PrintWriter(write);

        String num = sampleNum + "";
        String n = g.getNoOfNodes() + "";
        String e = g.getNoOfEdges() + "";
        String k = g.getk_clique() + "";
        String found = g.isCliqueFound() + "";
        String time = g.getRunningTime() + "";

        print_line.printf("  %s" + "\t", sampleNum);

        print_line.printf(" %s", "|");
        print_line.printf(" %s" + "\t", n);

        print_line.printf("%s", "|");
        print_line.printf("  %s" + "\t", e);

        print_line.printf(" %s", "|");
        print_line.printf(" %s" + "\t", k);

        print_line.printf("%s", "|");
        print_line.printf("  %s" + "\t", found);

        print_line.printf("%s", "|");
        print_line.printf("  %s" + "\t", time);

        print_line.printf("%n");
        print_line.close();
    }

    public void writeCSVHeader(int graphNum, int csvNum) throws IOException {
        String s = getPathtoText(csvNum, graphNum);
        FileWriter write = new FileWriter(s, append_to_file);
        PrintWriter print_line = new PrintWriter(write);

        String text = "sample #,nodes #,density (%),edges #,k,found,time(ms)";
        print_line.println("Graph #" + graphNum);
        print_line.println(text);
        print_line.close();
    }

    public void writeCSVSample(Graph g, int graphNum, int csvNum) throws IOException {
        String s = getPathtoText(csvNum, graphNum);
        FileWriter write = new FileWriter(s, append_to_file);
        PrintWriter print_line = new PrintWriter(write);

        String num = graphNum + "";
        String n = g.getNoOfNodes() + "";
        String d = g.getDensity() + "";
        String e = g.getNoOfEdges() + "";
        String k = g.getk_clique() + "";
        String found = g.isCliqueFound() + "";
        String time = g.getRunningTime() + "";

        String text = graphNum + ", " + n + ", " + d + "," + e + ", " + k + ", " + found + ", " + time;
        print_line.println(text);
        print_line.close();
    }

    public void writeCSVAccuracy(List<Double> accuracyList, int iteration) throws IOException {
         String s = getPathtoText(5, 0);
        FileWriter write = new FileWriter(s, append_to_file);
        PrintWriter print_line = new PrintWriter(write);
        
         String headerText = "#,iterations.Accuracy";
         print_line.println(headerText);
        
        for (int i = 0; i < accuracyList.size(); i++)
        {
            print_line.println(i + "," + iteration + ","+ accuracyList.get(i));
        }
        print_line.close();
    }

    public void writeSample(int graphNum, int sampleNum, Graph g) throws IOException {
        String s = getPathtoText(2, sampleNum);
        FileWriter write = new FileWriter(s, append_to_file);
        PrintWriter print_line = new PrintWriter(write);

        int numNode = g.getNoOfNodes();
        int e = g.getNoOfEdges();
        int k = g.getk_clique();
        String kclique = k + "-clique";
        String found = g.isCliqueFound() + "";
        String time = g.getRunningTime() + "";

        print_line.printf("%s" + "%d" + "%n", "Graph #", graphNum);
        print_line.printf("%s" + "%d" + "%n", "Number of nodes: ", numNode);
        print_line.printf("%s%n", "========================================================");
        print_line.printf("%s" + "%d" + "%n", "Sample #: ", sampleNum);
        print_line.printf("%s" + "%d" + "%n", "Number of edges: ", e);
        print_line.printf("%s" + "%n", kclique);
        print_line.printf("%s" + "%s" + "%s", "Is ", kclique, " found?:  ");
        print_line.printf("%s" + "%n", found);
        print_line.printf("%s" + "%s" + "%n", "Time: ", time);
        print_line.printf("%s%n%n", "========================================================");

        Map<Integer, int[]> nodeMap = g.getMapNodes();

        int[][] pairs = g.getPairs();

        for (int i = 0; i < pairs.length; i++) {

            print_line.printf("%-2d" + "%s" + "%-2d" + "%n", pairs[i][0], " <-->  ", pairs[i][1]);
        }

        print_line.printf("%s%n", "========================================================");

        for (Integer key : nodeMap.keySet()) {
            print_line.printf("%-3d" + " %s ", key, "|");

            int[] z = nodeMap.get(key);
            for (int item : z) {
                print_line.printf("%d" + "%s", item, "--");
            }
            print_line.printf("%n");
        }
        print_line.close();
    }

    public void ReadFile() throws FileNotFoundException, IOException {

//        Scanner read = new Scanner (new File(path)); 
//        
//        while(read.hasNext())
//        {
//             String node = read.next();
//             String[] data1 = node.split(":");
//
//             int n = Integer.parseInt(data1[0].trim());
//                    
//            String[] data2 = data1[1].split("-");
//
//            int[] y = new int [data2.length];
//            for (int i = 0; i < data2.length; i++)
//            {
//                y[i] = Integer.parseInt(data2[i].trim());
//            }
//            
//            mapNodes.put(n, y);
//        }
//        read.close();
    }

}
