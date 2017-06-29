/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topic_modelling;

/**
 *
 * @author soong96
 */
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import com.apporiented.algorithm.clustering.*;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;

public class CSVReader {
    
    static String[] names = new String[]{"doc1","doc2","doc3","doc4","doc5","doc6","doc7","doc8","doc9","doc10","doc11","doc12","doc13","doc14","doc15","doc16","doc17","doc18","doc19","doc20"};
    static double[][] csvtables = new double[20][20]; //initialize with 20 rows and 20 columns for each row
    
    public CSVReader(String filename) throws FileNotFoundException{
        File file = new File(filename);
        Scanner scan = new Scanner(file);
        scan.nextLine(); //Skip the header line
        
        int c = 0; //set the row count
        int m = 20; //set the column count
        
        while(scan.hasNextLine()){
            String[] arr = scan.nextLine().split(",");
            
            for(int i = 1;i < 20;i++){
                double value = Double.parseDouble(arr[i]); //By having arr[i], the "doc1" string text is skip and insert only double;
                csvtables[c][i-1] = value;
            }
            c++;
        }
        
        scan.close();
        //Print the whole table
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                System.out.print(csvtables[i][j] + ", ");
            }
            System.out.println();
        }
        
        
    }
    
    public double[][] getCSVTables(){
        return csvtables;
    }
    
    public static Cluster createCluster() {        
        
        ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
        Cluster cluster = alg.performClustering(csvtables, names, new AverageLinkageStrategy());
        cluster.toConsole(0);
        return cluster;
    }
}