/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphCliqueProblem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vern
 */
public class Elimination {

    Graph g;
    int k;

    private Map<Integer, int[]> validMapNodes = new HashMap<>();

    public Elimination(Graph g) {
        this.g = g;
        this.k = g.getk_clique();

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

    public boolean begin() {
        boolean first = false;
        boolean second = false;

        first = firstRoundElimination();
        if (first == true) {
            second = secondRoundElimination();
            if (second == true)
                return true;
            else
                return false;
        } 
        else
            return false;
    }

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
    public boolean secondRoundElimination() {
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
            return false;
        } else {
            return true;
        }
    }
}
