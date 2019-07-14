/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphCliqueProblem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 *
 * @author vern
 */
public class ExactMethod {

    private Graph g = new Graph();

    private Map<Integer, int[]> validMapNodes = new HashMap<>();
    private ArrayList<int[]> nodesCombination = new ArrayList<>();

    private int k = 0;

    long startTime = 0;
    long endTime = 0;

    //initialization
    ExactMethod(Graph g) {
        this.g = g;
        k = g.getk_clique();
    }

//------------------------------------------------------------------------------------------------------
// getter setters
//------------------------------------------------------------------------------------------------------
// map of valid nodes with its edges
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

//------------------------------------------------------------------------------------------------------
// BEGIN exhaustive search 
//------------------------------------------------------------------------------------------------------
    public void begin() throws IOException {
        this.startTime = System.nanoTime();

        boolean pass = firstRoundElimination();
        if (pass == true) {
            System.out.println("begin second round elimination............");
            secondRoundElimination();
        } else {
            print(false);
        }

    }

//------------------------------------------------------------------------------------------------------
// Elimination Rounds
//------------------------------------------------------------------------------------------------------    
    //check if there are enough edges to form k-cllique
    public boolean firstRoundElimination() {
        System.out.println("begin first round elimination............");
        int numEdges = g.getNoOfEdges();
        int requiredEdges = (k * (k - 1)) / 2;

        if (numEdges < requiredEdges) {
            return false;
        } else {
            return true;
        }
    }

    //eliminate nodes with not enough edges
    public void secondRoundElimination() throws IOException {
        Map<Integer, int[]> nodesMap = g.getMapNodes();
        Map<Integer, int[]> validMaps = new HashMap<>();
        boolean found = false;
        for (Integer key : nodesMap.keySet()) {
            int[] z = nodesMap.get(key);

            if (z.length >= (k - 1)) {
                validMaps.put(key, z);
            }
        }
        setValidMap(validMaps);
        if (validMaps.size() < k) {
            print(false);
        } else {
            findClique();
        }
    }

//------------------------------------------------------------------------------------------------------
// Evaluate and find k-clique in combination of valid nodes
//------------------------------------------------------------------------------------------------------    
    //check for clique existence in combinations of valid nodes
    public void findClique() throws IOException {
        System.out.println("finding clique......................");
        Map<Integer, int[]> validMap = new HashMap<>();
        validMap = getValidMap();
        int size = validMap.size();
        int[] validNodes = new int[size];
        
        validNodes = getValidNodes(validMap);

        this.nodesCombination.clear();
        createCombination(validNodes, size, k);
        
        
        ArrayList<int[]> comboSets = getCombination();
        Map<Integer, int[]> comboMap = new HashMap<>();

        boolean cliqueFound = false;

        for (int[] i : comboSets) {
            comboMap = getComboMaps(i, validMap);
            cliqueFound = checkNodesConnected(comboMap);

            if (cliqueFound == true) {
                break;
            }
        }
        g.setCliqueFound(cliqueFound);
        print(cliqueFound);
    }

    public int[] getValidNodes( Map<Integer, int[]> validMap )
    {
        int size = validMap.size();
        int[] validNodes = new int[size];
        int count = 0;
         for (Integer currentNode : validMap.keySet()) {
             validNodes[count] = currentNode;
             count++;
         }
         
         return validNodes;
    }
    //get maps of nodes and edges based on the combination
    public Map<Integer, int[]> getComboMaps(int[] combo, Map<Integer, int[]> validMap) {
        Map<Integer, int[]> comboMap = new HashMap<>();

        for (int i : combo) {
            int[] e = validMap.get(i);
            comboMap.put(i, e);
        }

        return comboMap;
    }

    //check if nodes in ONE combinations are connected
    private boolean checkNodesConnected(Map<Integer, int[]> comboMap) {

        //check if all the nodes are connected to each other
        for (Integer currentNode : comboMap.keySet()) {
            int[] e = comboMap.get(currentNode);

            for (Integer compareNode : comboMap.keySet()) {
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
    public void createCombination(int arr[], int nodeSize, int kSize) {
        // A temporary array to store all combination one by one 
        int data[] = new int[kSize];
        
        this.nodesCombination.clear();
        // Print all combination using temprary array 'data[]' 
        combinationUtil(arr, data, 0, nodeSize - 1, 0, kSize);
    }

    public void combinationUtil(int arr[], int data[], int start, int end, int index, int kSize) {
        // Current combination is ready to be printed, print it 
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

//------------------------------------------------------------------------------------------------------
// Supporting functions
//------------------------------------------------------------------------------------------------------
    //print
    private void print(boolean foundClique) {
        if (foundClique == true) {
            System.out.println(k + "-clique found! Please look into DATA folder for results.");
        } else {
            System.out.println(k + "-clique is not found!");
        }
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
