/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphCliqueProblem;

/**
 *
 * @author vern
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vern
 */
public class Main {

    final static int sampleSize = 50;
    static int graphNum = 1;
    static int n = 15;
    static int e = 0;
    static double  d = 0;
    static double k = 0;
    static int chance =0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        int graphCount = 0;
        int totalLoop = 0;

        Scanner sc = new Scanner(System.in);
        System.out.println("How many loops you want to test?");
        totalLoop = sc.nextInt();

        do {
            titleFunction();
            graphNum++;
            graphCount++;
        } while (graphCount < totalLoop);

    }

    static public void titleFunction() throws IOException {
        String title = "#-----------------------------------------------------------------------------------------------------------------#\n"
                + "Graph Clique Problem\n"
                + "#-----------------------------------------------------------------------------------------------------------------#\n";
        String GraphGenerationTitle = "# ----Graph Generation Phase---- #\n\nEnter zero(0) to randomize"
                + " variable.\n\n";
        String graph = "Graph #" + graphNum + "\n\n";

        String NodeTitle = "Please enter number of nodes: ";
//        String EdgesTitle = "Please enter number of edges: ";
        String DensityTitle = "Please enter density of graph (%): ";
        String kCliqueTitle = "Please enter the number of k-cliques: ";
        System.out.println(title + GraphGenerationTitle + graph);
        Scanner sc = new Scanner(System.in);

        if (graphNum != 1) {
//            if (n != 0) {
//                System.out.print(NodeTitle);
//                n = sc.nextInt();
//            }
            if (d != 0) {
                
                System.out.print(DensityTitle);
                d = sc.nextDouble();

            }
            if (k != 0) {
                System.out.print(kCliqueTitle);
                k = sc.nextDouble();
            }
        } 
        else {
//            System.out.print(NodeTitle);
//            n = sc.nextInt();
//            System.out.print(EdgesTitle);
//            e = sc.nextInt();
            System.out.print(DensityTitle);
            d = sc.nextDouble();
            System.out.print(kCliqueTitle);
            k = sc.nextDouble();
        }
        
        if (chance == 0)
        {
            String chancesTitle = "Clique found chance:\n1. 50%\n2.100%";
            System.out.println(chancesTitle);
            chance = sc.nextInt();
        }
       

        TestCliqueSolution(n, d, k);
    }


    static public void TestCliqueSolution(int n, double d, double k) throws IOException {
        boolean reRun = false;
        boolean foundClique = false;
        int found = 0;
        int notFound = 0;
        int count = 0;
        
        int total = 0;
        int half = 0;
        
        total = sampleSize;
        half = sampleSize/2;
        for (int i = 1; i <= sampleSize; i++) 
        {
            
            do
            {
                Graph g = new Graph(i, graphNum, n, d, k);
                ExhaustiveSearch es = new ExhaustiveSearch(g);
                es.begin();
                foundClique = g.isCliqueFound();
                System.out.println(foundClique);

                if(foundClique == true)
                {
                    if(chance == 1)
                    {
                        if (found == half && count < total) {
                            reRun = true;
                        } else {
//                            g.writeFile();
                            reRun = false;
                            found++;
                            count++;
                            if (count == total)
                            {
                                break;
                            }
                        }
                    }
                    else
                    {
//                       g.writeFile();  
                        reRun = false;
                        found ++;
                        if(found == total)
                            break;
                    }
                    
                }
                else
                {
                    if(chance == 1)
                    {
                        if (notFound == half && count < total)
                            reRun = true;
                        else
                        {
//                            g.writeFile();
                            reRun = false;
                            notFound ++;
                            count ++;
                            if (count == total)
                                break;
                        }    
                    }
                    else
                        reRun = true;
                }
            }while (reRun == true);   
        }
    }
}
