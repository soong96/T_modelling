/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topic_modelling;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author soong96
 */
public class TaskWriteCSVMatrix  implements Runnable{
    private CountWord cw;
    private String fileName;
    private HashMap<String, Double> similarityMatrix;

    
    TaskWriteCSVMatrix(CountWord cw, String fileName, HashMap<String, Double> similarityMatrix){
        this.cw = cw;
        this.fileName = fileName;
        this.similarityMatrix = similarityMatrix;
    }
    
    
    public void run(){        
        try {
            cw.writeCSVSimilarMatrix(fileName,similarityMatrix);//write csv of euclidean matrix
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TaskWriteCSVMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
}
