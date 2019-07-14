/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphCliqueProblem;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.util.Pair;

/**
 *
 * @author vern
 */
public class Grasp {

    private Graph g;
    private Map<Integer, int[]> validMapNodes = new HashMap<>();
    private ArrayList<int[]> nodesCombination = new ArrayList<>();
    private ArrayList<Node> newTempList = new ArrayList<>();

    boolean bestSolutionExist;
    int bestSlnExistCount;

    private int k = 0;
    private int iteration = 0;
    private List<Integer> bestSolution;
    private double greedyParam = 0;
    private int[] latestCombo;
    private int[] latestBestCombo;

    long startTime = 0;
    long endTime = 0;

    Grasp(Graph g) {
        this.g = g;
        this.k = g.getk_clique();
        this.greedyParam = 1.0;
        this.iteration = 100;
        this.bestSlnExistCount = 0;
        this.bestSolutionExist = false;
    }

    Grasp(Graph g, double gP) {
        this.g = g;
        this.k = g.getk_clique();
        this.greedyParam = gP;
        this.iteration = 100;
        this.bestSlnExistCount = 0;
        this.bestSolutionExist = false;
    }

    Grasp(Graph g, int i) {
        this.g = g;
        this.k = g.getk_clique();
        this.greedyParam = 1.0;
        this.iteration = i;
        this.bestSlnExistCount = 0;
        this.bestSolutionExist = false;
    }

    Grasp(Graph g, double gP, int i) {
        this.g = g;
        this.k = g.getk_clique();
        this.greedyParam = gP;
        this.iteration = i;
        this.bestSlnExistCount = 0;
        this.bestSolutionExist = false;
    }

    public Map<Integer, int[]> getValidMap() {
        return this.validMapNodes;
    }

    public void setValidMap(Map<Integer, int[]> m) {
        this.validMapNodes = m;
    }

    //combination of nodes
    public ArrayList<int[]> getCombination() {
        return nodesCombination;
    }

    public void addCombination(int[] combo) {
        this.nodesCombination.add(combo);
    }

    public List<Integer> getBestSolution() {
        return this.bestSolution;
    }

    public void setBestSolution(List<Integer> best) {
        this.bestSolution = best;
    }

//--------------------functions--------------------//
    public void begin() {
        startTime = System.nanoTime();
        GRASP();
        print(g.isCliqueFound());
    }

//------------------------------------------------------------------------------------------------------
// GRAPSP
//------------------------------------------------------------------------------------------------------    
    public void GRASP() {
        List<Integer> bestSln = new ArrayList<>();
        List<Integer> candidateSln = new ArrayList<>();
        List<Integer> foundSln = new ArrayList<>();
        bestSlnExistCount = 0;

        for (int i = 0; i < iteration; i++) {
            bestSolutionExist = false;
            candidateSln = constructCandidateSln();

            if (candidateSln.size() == 0 || candidateSln.isEmpty()) {
                continue;
            } else if (candidateSln.size() >= k - 2) {
                foundSln = greedyLocalSearch(candidateSln);
            } else {
                bestSln = candidateSln;
            }
            if (bestSln.isEmpty()) {
                bestSln = foundSln;
            }
            //if found exactly k-clique, bestSolutionExist = true
            if (bestSolutionExist) {
                bestSln = foundSln;
                bestSlnExistCount++;
            } else if (foundSln.size() >= bestSln.size()) {
                bestSln = foundSln;
            }
        }
        g.setCliqueFound(bestSolutionExist);
        setBestSolution(bestSln);

    }

    public List<Integer> constructCandidateSln() {
        List<Integer> candidateSln = new ArrayList<>();
        List<Integer> nodesList = g.getNodesList();
        List<Integer> tempNodesList = g.getNodesList();
        

        int n = g.getNoOfNodes();
        int tempK = k;
        List<Integer> RCL = new ArrayList<>();
        Random rand = new Random();

        double param = greedyParam;
        int numNodes = 0;
        numNodes = g.getNoOfNodes();

        candidateSln.clear();
        while (candidateSln.size() < tempK) {
            RCL = new ArrayList<>();
            RCL = buildRCL(tempNodesList, tempK, param, numNodes);
            if (!RCL.isEmpty()) {
                rand = new Random();
                int r = rand.nextInt(RCL.size());
                int N = RCL.get(r);
                candidateSln.add(N);
                tempNodesList.remove(new Integer(N));
            } else {
                if (tempK == n) {
                    tempK = tempK - 1;
                }
                else
                    break;
            }
            if (candidateSln.size() == tempK) {
                break;
            }
            param -= param;
        }
        return candidateSln;
    }

    public List<Integer> buildRCL(List<Integer> nodesList, int tempK, double param, int numNode) {
        List<Integer> RCL = new ArrayList<>();
//        Map<Integer, int[]> nodesMap = new HashMap<>();
//        nodesMap.clear();
//        nodesMap = g.getMapNodes();

        int min = (int) (tempK * param);
        int max = numNode - 1;

        for (Integer i : nodesList) {
            if (g.getEdgesCount(i) >= (min + param* (max - min))) {
                RCL.add(i);
//                nodesMap.remove(i);
            }
        }
        
//        int[]sortedArr = new int[RCL.size()];
//        sortedArr = g.sortArrByEdgesNodeList(nodesMap);
//        for(int i : sortedArr)
//        {
//            RCL.add(i);
//        }
        return RCL;
    }

    public List<Integer> greedyLocalSearch(List<Integer> candidateSln) {
        List<Integer> currentLocalBest = new ArrayList<>();
        boolean stopSearch = false;
        int oriK = k;
        int minK = k - 2;
        boolean cliqueFound = false;
        Map<Integer, int[]> candidateMap = new HashMap<>();
        candidateMap = g.getMapByNodes(candidateSln);
        setValidMap(candidateMap);
        
        if (candidateSln.size() < minK)
        {
            return candidateSln;
        }
        while (oriK >= minK || stopSearch == true) {
            cliqueFound = findClique(oriK, candidateMap, candidateSln);

            //exact k-clique found
            if (cliqueFound) {
                stopSearch = true;
                bestSolutionExist = true;
                if (oriK == k) {
                    bestSlnExistCount++;
                }
                break;
            } else {
                oriK--;
            }
        }
        if (latestBestCombo != null) {
            currentLocalBest = arrayToList(latestBestCombo);
            return currentLocalBest;
        } else {
            return arrayToList(latestCombo);
        }
    }

//------------------------------------------------------------------------------------------------------
// Evaluate and find k-clique in combination of valid nodes
//------------------------------------------------------------------------------------------------------    
//check for clique existence in combinations of valid nodes
    public boolean findClique(int oriK, Map<Integer, int[]> candidateMap, List<Integer> candidateSln) {
        System.out.println("finding clique......................");
//        Map<Integer, int[]> validMap = new HashMap<>();
//        validMap = getValidMap();
        int size = candidateMap.size();
        ArrayList<int[]> comboSets = new ArrayList<>();

        for (Integer i : candidateMap.keySet()) {
            System.out.print(i + " / ");
        }
        System.out.println();

        int[] candidateNodes = new int[candidateSln.size()];
        candidateNodes = listToArray(candidateSln);

        this.nodesCombination.clear();
//        validNodes = getValidNodes(candidateMap);
        if (size > oriK) {
            createCombination(candidateNodes, size, oriK);
        }

        comboSets.clear();
        comboSets = getCombination();
        Map<Integer, int[]> comboMap = new HashMap<>();

        boolean cliqueFound = false;

        if (!comboSets.isEmpty()) {
            for (int[] i : comboSets) {
                comboMap.clear();
                comboMap = getComboMaps(i, candidateMap);
                cliqueFound = checkNodesConnected(comboMap);

                if (cliqueFound == true) {
                    latestBestCombo = i;
                    break;
                } else {
                    latestCombo = i;
                }
            }
        } else {
            cliqueFound = checkNodesConnected(candidateMap);
            if (cliqueFound) {
                latestBestCombo = candidateNodes;
            } else {
                latestCombo = candidateNodes;
            }
        }

        return cliqueFound;
    }

    public int[] getValidNodes(Map<Integer, int[]> candidateMap) {
        int size = candidateMap.size();
        int[] validNodes = new int[size];
//        int count = 0;
//        for (Integer currentNode : validMap.keySet()) {
//            validNodes[count] = currentNode;
//            count++;
//        }
//
        return validNodes;
    }

    //get maps of nodes and edges based on the combination
    public Map<Integer, int[]> getComboMaps(int[] combo, Map<Integer, int[]> candidateMap) {
        Map<Integer, int[]> comboMap = new HashMap<>();

        for (int i : combo) {
            int[] e = candidateMap.get(i);
            comboMap.put(i, e);
        }

        return comboMap;
    }

    //check if nodes in ONE combinations are connected
    private boolean checkNodesConnected(Map<Integer, int[]> comboMap) {

        Map<Integer, int[]> compareMap = new HashMap<>();
        compareMap = comboMap;
        //check if all the nodes are connected to each other
//        if (comboMap.isEmpty())
//          System.out.println("null hello");
        for (Integer currentNode : comboMap.keySet()) {
            int[] e = compareMap.get(currentNode);
            for (Integer compareNode : compareMap.keySet()) {
                if (Objects.equals(compareNode, currentNode)) {
                    continue;
                } else {
                    //check if the compare node is the edge of the current node
                    //if not return false, else continue checking for other edges
                    if (!(IntStream.of(e).anyMatch(x -> x == compareNode))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

//------------------------------------------------------------------------------------------------------
// Combinations functions
//------------------------------------------------------------------------------------------------------
    public void createCombination(int arr[], int nodeListSize, int kSize) {
        // A temporary array to store all combination one by one 
        int data[] = new int[kSize];

        this.nodesCombination.clear();
        // Print all combination using temprary array 'data[]' 
        combinationUtil(arr, data, 0, nodeListSize - 1, 0, kSize);
    }

    public void combinationUtil(int arr[], int data[], int start, int end, int index, int kSize) {
        // Current combination is stored
        if (index == kSize) {
            int[] temp = new int[kSize];
            System.arraycopy(data, 0, temp, 0, kSize);
            addCombination(temp);
            return;
        }

        // replace index with all possible elements. The condition 
        // "end-i+1 >= r-index" makes sure that including one element 
        // at index will make a combination with remaining elements 
        // at remaining positions 
        for (int i = start; i <= end && end - i + 1 >= kSize - index; i++) {
            data[index] = arr[i];
            combinationUtil(arr, data, i + 1, end, index + 1, kSize);
        }
    }

    public List<Integer> arrayToList(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for (int i : arr) {
            list.add(i);
        }
        return list;
    }

    public int[] listToArray(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    private void print(boolean foundClique) {
        if (foundClique == true) {
            System.out.println(k + "-clique found! Please look into DATA folder for results.");
            bestSlnExistCount ++;
        } else {
            System.out.println(k + "-clique is not found!");
            System.out.println("Best Solution found:\n[");

        }

        for (int i : getBestSolution()) {
            System.out.print(i + "  ");
        }
        System.out.println("]");

        calculateTime();
    }

    //calculate computing time
    private void calculateTime() {
        this.endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        double t = (double) totalTime / 1000000.0;
        g.setRunningTime(t);
    }

}
