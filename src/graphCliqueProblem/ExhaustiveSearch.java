/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphCliqueProblem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 *
 * @author vern
 */
public class ExhaustiveSearch {
    
    private Graph g = new Graph ();
    
    private Map <Integer, int[]> remainMapNodes;
    private Map <Integer, int[]> tempMapNodes = new HashMap<>();

    private  int k = 0;
   
   long startTime = 0;
   long endTime = 0;
   
   private boolean largerClique = false;
    
    ExhaustiveSearch (Graph g) 
    {
        this.g = g;
        k = g.getk_clique();
        setRemainMap(g.getMapNodes());
    }
    
    
    // remainMapNodes
     public Map<Integer, int[]>  getRemainMap () { return this.remainMapNodes; }
     public void setRemainMap (Map<Integer, int[]> m){ this.remainMapNodes = m; }
    
    
    
    
    //--------------------functions--------------------//

//begin search    
    public void begin() throws IOException
    {
        System.out.println("begin exact method searching.....");
        
        //calcute running time
        long startTime = System.nanoTime();
        this.startTime = startTime;
        
       boolean pass = firstRoundElimination();
       if(pass == true)
       {
           System.out.println("begin second round elimination............");
           secondRoundElimination();
       }
       else
           print(false);      
       
    }
    

    public boolean firstRoundElimination()
    {
        System.out.println("begin first round elimination............");
        int numEdges = g.getNoOfEdges();
        int requiredEdges = (k*(k-1))/2;
        
        if(numEdges < requiredEdges)
        {
            return false;
        }
        else
            return true;
    }
    
    //eliminate nodes with not enough edges
    public void secondRoundElimination() throws IOException
    {
        Map<Integer, int[]> nodesMap = g.getMapNodes();
        Map <Integer, int[]> remainM = new HashMap<>();
         boolean found = false;
          for(Integer key: nodesMap.keySet())
        {
            int[] z = nodesMap.get(key);
               
            if (z.length >=  (k-1))
                remainM.put(key, z);
         }
          setRemainMap(remainM);
          if (remainM.size() < k)
              print(false);
          else
          {
              found = findClique (remainM);
              print(found);
          }
    }
    
    
    //find clique begin
    private boolean findClique (Map <Integer, int[]> remainM)
    {
        tempMapNodes.clear();
        int nodesSize = remainM.size();
        boolean[]  masterCheck = new boolean[nodesSize];
        
        if (nodesSize < k)
            return false;
        
        int masterCount = 0;
        for (Integer currentNode: remainM.keySet())
        {
            boolean[] check = new boolean[nodesSize];
            int[] e = remainM.get(currentNode);

            int count = 0;
            for(Integer compareNode: remainM.keySet())
            {
                if(compareNode == currentNode)
                    check[count] = true;  
                else
                {
                    //retrieve edges from current node
                    if (IntStream.of(e).anyMatch(x -> x == compareNode))
                        check[count] = true;
                    else
                    {
                        if(nodesSize == k)
                            return false;
                        else
                            check[count] = false;
                    }
                }
                count ++;
            }
            int countTrue = 0;
            for (int c = 0; c < check.length; c++)
            {
                if (check[c] == true)
                    countTrue ++;
            }
            if (countTrue >= (k))
            {
                masterCheck[masterCount] = true;
                tempMapNodes.put(currentNode, e);
            }
            else
                masterCheck[masterCount] = false;
            masterCount ++;
        }
        //check if all nodes are valid for a clique
        if (tempMapNodes.size() < k)    
            return false;
        else
        {
            boolean containFalse  = false;
            for (boolean mc : masterCheck)
            {
                if(mc == false)
                {
                    containFalse = true;
                    break;
                }
            }
                setRemainMap(tempMapNodes);
            //if contain false, we need to loop back,
            //this because there is a possibility that 
            //there are other nodes connected to the invalid node
            if (containFalse == true)
            {
                findClique (getRemainMap());
            }
            else
            {
                int sizeN = getRemainMap().size();
                
                 if(sizeN < k)
                    return false;
                else if (sizeN == k)
                    return true;
                else
                {
                    largerClique = true;
                    return true;
                    
                }
            }
        }
        return false;
    }
    
    private void print (boolean foundClique) throws IOException
    {
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        double t = (double) totalTime / 1000000.0;
        g.setRunningTime(t);
        
        g.setCliqueFound(foundClique);
        
        if(foundClique == true)
        {
            System.out.println(k + "-clique found! Please look into DATA folder for results.");
            
        }
        else
        {
            System.out.println(k + "-clique is not found!");
        }
        if(largerClique== true)
                System.out.println("Largerclique found!");
    }
   
}
