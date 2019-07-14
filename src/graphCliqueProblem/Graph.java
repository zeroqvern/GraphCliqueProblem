/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphCliqueProblem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author vern
 */
public class Graph {

    private int noOfNodes;
    private int noOfEdges;
    private double density;

    private int[][] pairs;
    private int[][] flippedPairs;
    private int[][] allPairs;

    private int k_clique;
    private boolean cliqueFound = false;

    private Map<Integer, int[]> mapNodes;

    private int sampleNum = 0;
    private int graphNum = 0;

    private double runTime = 0;

    Graph() {
        noOfNodes = 0;
        noOfEdges = 0;
        k_clique = 0;
        sampleNum = 0;
    }

    //diff edge
    Graph(int sample, int graph, int n, double d, double k) {
        sampleNum = sample;
        graphNum = graph;

        setup(n, d, k);
        begin();
    }

    //nodes
    public int getNoOfNodes() {
        return noOfNodes;
    }

    public void setNoOfNodes(int n) {
        if (n == 0) {
            int max = 30;
            int min = 10;
            int r = 0;

            Random rand = new Random();
            r = rand.nextInt((max - min) + 1) + min;

            noOfNodes = r;
        } else {
            noOfNodes = n;
        }
    }

    //density
    public double getDensity() {
        return density;
    }

    public void setDensity(double d) {
        if (d == 0) {
            int max = 100;
            int min = 60;
            int r = 0;

            Random rand = new Random();
            r = rand.nextInt((max - min) + 1) + min;

            double den = (double) r / 100.0;
            density = den;
        } else {
            density = d;
        }
    }

    //edges
    public int getNoOfEdges() {
        return noOfEdges;
    }

    public void setNoOfEdges() {

        int n = getNoOfNodes();
        double d = getDensity();
        int r = 0;
        int max = (n * (n - 1)) / 2;

        double e = 0;
        e = max * d;

        r = (int) e;

        this.noOfEdges = r;
    }

    //k-clique    
    public int getk_clique() {
        return k_clique;
    }

    public void setk_clique(double k) {
        int clique = 0;
        int max = getNoOfNodes();
        double minD = 0;

        if (k == 0) {

            minD = max * 0.5;

            int min = (int) minD;
            double cliqueD = 0.0;

            do {
                Random rand = new Random();
                clique = rand.nextInt((max - min) + 1) + min;

                cliqueD = (double) clique;

            } while (cliqueD < minD);

            k_clique = clique;
        } else {
            minD = max * k;
            k_clique = (int) minD;
        }
    }

    //pairs
    public int[][] getPairs() {
        return pairs;
    }

    public void setPairs(int[][] p) {
        this.pairs = p;
    }

    //flipped pairs
    public int[][] getFlippedPairs() {
        return flippedPairs;
    }

    public void setFlippedPairs(int[][] p) {
        this.flippedPairs = p;
    }

    //all pairs including flipped pairs
    public int[][] getAllPairs() {
        System.arraycopy(pairs, 0, allPairs, 0, pairs.length);
        System.arraycopy(flippedPairs, 0, allPairs, pairs.length, flippedPairs.length);

        Arrays.sort(allPairs, Comparator.comparingInt(a -> a[0]));

        return allPairs;
    }

    //map
    public Map<Integer, int[]> getMapNodes() {
        return this.mapNodes;
    }

    public void setMapNodes(Map<Integer, int[]> m) {
        this.mapNodes = m;
    }

    //found
    public boolean isCliqueFound() {
        return cliqueFound;
    }

    public void setCliqueFound(boolean f) {
        cliqueFound = f;
    }

    //time
    public double getRunningTime() {
        return runTime;
    }

    public void setRunningTime(double s) {
        runTime = s;
    }

    //--------------------functions--------------------//
    private void begin() {
        System.out.println("generating graph.........");

        int e = getNoOfEdges();

        pairs = new int[e][2];
        flippedPairs = new int[e][2];
        allPairs = new int[pairs.length + flippedPairs.length][2];

        generatePairs();
        Arrays.sort(flippedPairs, Comparator.comparingInt(a -> a[0]));
        getAllPairs();

        System.out.println("generating edges............");
        System.out.println(pairs.length);
//        for (int i = 0; i < pairs.length; i++) {
//
//            System.out.printf("%-2d" + "%s" + "%-2d" + "%n", allPairs[i][0], " <-->  ", allPairs[i][1]);
//        }

        System.out.println("mapping edges............");
        mapEdges();

        Map<Integer, int[]> nodeMap = getMapNodes();
        
         for(Integer key:nodeMap.keySet())
        {
            System.out.printf("%-3d"+ " %s ",key, "|");

            int[] z = nodeMap.get(key);
            for(int item : z)
            {
                System.out.printf("%d"+"%s",item, "--");
            }
             System.out.printf("%n");
         }
    }

    public void setup(int n, double d, double k) {
        setNoOfNodes(n);
        setDensity(d);
        setNoOfEdges();
        setk_clique(k);

//          System.out.println("Nodes: " + getNoOfNodes() + "\nEdges: " + getNoOfEdges() +
//                                                "\nK: " + getk_clique());
    }

    //generate edges between 2 nodes 
    // generatenode pairs
    private void generatePairs() {
        int max = getNoOfNodes();
        int min = 1;

        Random rand = new Random();
        int[][] pair = getPairs();
        int[][] flippedPair = getFlippedPairs();

        int edges = getNoOfEdges();

        boolean loopback = false;

        for (int j = 0; j < edges; j++) {
            do {
                loopback = false;

                int a = 0;
                int b = 0;

                a = rand.nextInt((max - min) + 1) + min;

                do {
                    b = rand.nextInt((max - min) + 1) + min;
                } while (b == a);

                if (a > b) {
                    a = a + b;
                    b = a - b;
                    a = a - b;
                }

                if (pair.length == 0) {
                    pair[j][0] = a;
                    pair[j][1] = b;

                    flippedPair[j][0] = b;
                    flippedPair[j][1] = a;
                } else {
                    //check for duplicates
                    if (checkDuplicate(pair, a, b)) {
                        loopback = true;
                    } else {
                        pair[j][0] = a;
                        pair[j][1] = b;

                        flippedPair[j][0] = b;
                        flippedPair[j][1] = a;

                        loopback = false;
                    }
                }
            } while (loopback == true);
        }

        setPairs(pair);
    }

    //check if generated pair is duplicated
    private boolean checkDuplicate(int[][] pair, int a, int b) {
        for (int i = 0; i < pair.length; i++) {
            if (pair[i][0] == a && pair[i][1] == b) {
                return true;
            }
        }

        return false;
    }

    //match edges to each nodes
    public void mapEdges() {
        int[][] pair = getAllPairs();
        Map<Integer, int[]> nodeMap = new HashMap<>();
        int[][] refNode = new int[1][1];
        boolean first = true;
        int count = 0;
        for (int i = 0; i < pair.length; i++) {
            if (i == 0) {
                refNode[0][0] = pair[i][0];
                count++;
            } else {
                if (refNode[0][0] == pair[i][0]) {
                    count++;
                } else {
                    int[] tempEdges = getEdgesArray(count, i, pair, refNode[0][0], first);
                    int x = i + 1;
                    nodeMap.put(refNode[0][0], tempEdges);

                    if (first == true) {
                        first = false;
                    }
                    count = 1;
                    refNode[0][0] = pair[i][0];
                }
                int x = i + 1;
                if (x == pair.length) {
                    int[] tempEdges = getEdgesArray(count, x, pair, refNode[0][0], first);
                    nodeMap.put(refNode[0][0], tempEdges);
                }
            }
        }
        setMapNodes(nodeMap);

        //unit checking
//        System.out.println("node num: " + mapNodes.size());
//        for(Integer key: mapNodes.keySet())
//        {
//                System.out.println("Nodes: " + key);
//                int[] z = mapNodes.get(key);
//                
//                for(int item : z)
//                {
//                    System.out.print(item + "--");
//                }
//                System.out.println("");
//         }
    }

    private int[] getEdgesArray(int count, int i, int[][] pair, int node, boolean first) {
        List<Integer> pairedEdges = new ArrayList<>();

        for (int j = count; j > 0; j--) {
            int x = i - j;
            pairedEdges.add(pair[x][1]);
        }

        int size = pairedEdges.size();
        int[] tempEdges = new int[size];
        for (int j = 0; j < size; j++) {
            tempEdges[j] = pairedEdges.get(j);
        }

        return tempEdges;

    }

    //getEdgesCount per node
    public int getEdgesCount(int n) {
        Map<Integer, int[]> nodesMap = getMapNodes();
        int edgesCount = 0;
        for (Integer currentNode : nodesMap.keySet()) {
            if (currentNode == n) {
                edgesCount = nodesMap.get(currentNode).length;
            }
        }
        return edgesCount;
    }

    //get nodes only
    public List<Integer> getNodesList() {
        List<Integer> nodesList = new ArrayList<Integer>();
        for (Integer i : getMapNodes().keySet()) {
            nodesList.add(i);
        }

        return nodesList;
    }
    
    public Map<Integer, int[]> getMapByNodes(List<Integer>nodesList)
    {
        Map<Integer, int[]> map = new HashMap<>();
        map = getMapNodes();
        Map<Integer, int[]> returnMap = new HashMap<>();
        
        for(int i : nodesList)
        {
            int[] value = map.get(i);
            returnMap.put(i, value);
        }
        
        return returnMap;
    }
    

    public int[][] sortArrByEdgesCount(Map<Integer, int[]> nodesMap) {
        int size = nodesMap.size();
        int[][] nodesEdgesCount = new int[size][2];
        int edgeCount = 0;
        int nodeCount = 1;

        for (int i = 0; i < size; i++) {
            edgeCount = getEdgesCount(nodeCount);
            nodesEdgesCount[i][0] = nodeCount;
            nodesEdgesCount[i][1] = edgeCount;
            nodeCount++;
        }

        Arrays.sort(nodesEdgesCount, Comparator.comparingInt(b -> b[1]));

        return nodesEdgesCount;
    }
    
    public int[] sortArrByEdgesNodeList (Map<Integer, int[]> nodesMap)
    {
        int[] nodeList = new int[nodesMap.size()];
        int[][] n = new int[nodesMap.size()][2];
        
        n = sortArrByEdgesCount(nodesMap);
        for(int i =0; i < n.length; i++)
        {
            nodeList[i] = n[i][0];
        }
        return nodeList;
    }

    public void writeFile(int csvNum) throws IOException {

        System.out.println("Writing to files.......");
        FileManager fm = new FileManager();
        fm.createDir(graphNum);

        if (sampleNum == 1 && graphNum == 1) {
            fm.writeOverallSampleHeader(graphNum);
            fm.writeCSVHeader(graphNum, csvNum);
        }

        fm.writeOverallSample(this, sampleNum);
        System.out.println("write overall sample complete....");

        fm.writeCSVSample(this, graphNum, csvNum);
        System.out.println("write overall in csv complete....");

//        fm.writeSample(graphNum, sampleNum, this);
        System.out.println("wrtie sample #" + sampleNum + " complete......");

    }

}
