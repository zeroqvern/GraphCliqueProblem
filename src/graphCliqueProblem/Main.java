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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
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
    static int n =10;
    static int e = 0;
    static double d = 0.80;
    static double k = 0.5;
    static int methodType = 2;
    static int iteration = 10;
    static int chance = 1;

    static List<Graph> GRAPHDATA = new ArrayList<>();

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

        String searchTitle = "Which method?\n1. Exact - Exhaustive Search\n2. Non-exact - GRASP";
        String NodeTitle = "Please enter number of nodes: ";
//        String EdgesTitle = "Please enter number of edges: ";
        String DensityTitle = "Please enter density of graph (%): ";
        String kCliqueTitle = "Please enter the number of k-cliques: ";
        System.out.println(title + GraphGenerationTitle + graph);
        Scanner sc = new Scanner(System.in);

        if (graphNum != 1) {
            if (n != 0) {
                System.out.print(NodeTitle);
                n = sc.nextInt();
            }
            if (d != 0) {
                System.out.print(DensityTitle);
                d = sc.nextDouble();
            }
            if (k != 0) {
                System.out.print(kCliqueTitle);
                k = sc.nextDouble();
            }
        } else {
            System.out.print(NodeTitle);
            n = sc.nextInt();
            System.out.print(DensityTitle);
            d = sc.nextDouble();
            System.out.print(kCliqueTitle);
            k = sc.nextDouble();
        }

//        System.out.println(searchTitle);
//        methodType = sc.nextInt();
        if (chance == 0 && methodType == 1) {
            String chancesTitle = "Clique found chance:\n1. 50%\n2.100%";
            System.out.println(chancesTitle);
            chance = sc.nextInt();
        }

        TestCliqueSolution(methodType);

//        String accuracyTestTitle = "Run accuracy test for GRASP?\n1. Yes\n2. No";
//        System.out.println(accuracyTestTitle);
//        int accuracyTest = sc.nextInt();
//
//        if (accuracyTest == 1) {
//            runAccuracyTest();
//        }
    }

    static public void TestCliqueSolution(int runMethod) throws IOException {
        boolean reRun = false;
        boolean foundClique = false;
        int found = 0;
        int notFound = 0;
        int count = 0;

        int csvType = runMethod + 2;

        int total = 0;
        int half = 0;
        total = sampleSize;
        half = sampleSize / 2;

        GRAPHDATA.clear();

        for (int i = 1; i <= sampleSize; i++) {

            if (runMethod == 2) {
                do {
                    Graph g = new Graph(i, graphNum, n, d, k);
                    runMethods(runMethod, g);

                    foundClique = g.isCliqueFound();
                    System.out.println(foundClique);

                    if (foundClique == true) {
                        if (chance == 1) {
                            if (found == half && count < total) {
                                reRun = true;
                            } else {
                                g.writeFile(csvType);
                                reRun = false;
                                found++;
                                count++;
                                GRAPHDATA.add(g);
                                if (count == total) {
                                    break;
                                }
                            }
                        } else {
                            g.writeFile(csvType);
                            reRun = false;
                            found++;
                            GRAPHDATA.add(g);
                            if (found == total) {
                                break;
                            }
                        }

                    } else {
                        if (chance == 1) {
                            if (notFound == half && count < total) {
                                reRun = true;
                            } else {
                                g.writeFile(csvType);
                                reRun = false;
                                notFound++;
                                count++;
                                if (count == total) {
                                    break;
                                }
                            }
                        } else {
                            reRun = true;
                        }
                    }
                } while (reRun == true);
            } else {
                Graph g = new Graph(i, graphNum, n, d, k);
                runMethods(methodType, g);
                g.writeFile(csvType);
            }

        }

    }

    public static void runMethods(int methodType, Graph g) throws IOException {
        switch (methodType) {
            case 1:
                System.out.println("Entering exhaustive search....................");
                ExactMethod em = new ExactMethod(g);
                em.begin();
                break;

            case 2:
                System.out.println("Entering GRASP search....................");
                Grasp gr = new Grasp(g, iteration);
                gr.begin();
                break;
        }
    }

    public static void runAccuracyTest() throws IOException {
        methodType = 2;
        int predictions = 50;
        int found = 0;
        List<Double> accuracyList = new ArrayList<>();
        double accuracy = 0;
        
        System.out.println("Running accuracy test.............................");

        for (Graph g : GRAPHDATA) {
            found = 0;
            for (int i = 0; i < predictions; i++) {
                Grasp gr = new Grasp(g, iteration);
                gr.begin();
                if (gr.bestSlnExistCount != 0) {
                    found++;
                }
            }

            accuracy = 0;
            System.out.println("found: " + found);
            accuracy = calculateAccurate(found, predictions);
            accuracyList.add(accuracy);
        }

        FileManager fm = new FileManager();
        System.out.println("Writing accuracy data to file.............................");
        fm.createDir(graphNum);
        fm.writeCSVAccuracy(accuracyList, iteration);
    }

    public static double  calculateAccurate(int found, int totalPredictions) {
        double accuracy = 0;
        accuracy = (double)found / totalPredictions * 100.0;
        System.out.println("ac: " + accuracy);
        return accuracy;
    }

    public static void saveGraphData(Graph g) {
        GRAPHDATA.add(g);
    }

    private static int[] getUserInput() {

        String line;
        int graphSize, cliqueSize;

        Scanner sc = new Scanner(System.in);

        do {
            System.out.print("\nEnter Graph Size: ");
            line = sc.nextLine();

        } while (isNumberValid(line, 10, 25) == false);

        graphSize = Integer.parseInt(line);

        do {
            System.out.print("\nEnter Clique Size: ");
            line = sc.nextLine();

        } while (isNumberValid(line, graphSize / 2, graphSize) == false);

        cliqueSize = Integer.parseInt(line);

        int array[] = {graphSize, cliqueSize};

        return array;
    }

    public static boolean isNumberValid(String number, int min, int max) {
        try {
            int intValue = Integer.parseInt(number);

            if (intValue > max || intValue < min) {
                System.out.print("Input must be a digit between " + min + " to " + max + ". Please try again!\n");
                return false;
            } else {
                return true;
            }

        } catch (NumberFormatException e) {
            System.out.print("Input must an integer. Please try again!\n");
            return false;
        }

    }

    public static boolean loopCheck(int csvType, boolean foundClique, Graph g, int found, int notFound, int count) throws IOException {

        int total = 0;
        int half = 0;
        total = sampleSize;
        half = sampleSize / 2;

        if (foundClique == true) {
            if (chance == 1) {
                if (found == half && count < total) {
                    return true;
                } else {
                    g.writeFile(csvType);
                    found++;
                    count++;
                    if (count == total) {
                        return false;
                    }
                }
            } else {
                g.writeFile(csvType);
                found++;
                if (found == total) {
                    return false;
                }
            }

        } else {
            if (chance == 1) {
                if (notFound == half && count < total) {
                    return true;
                } else {
                    g.writeFile(csvType);
                    notFound++;
                    count++;
                    if (count == total) {
                        return false;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

}
