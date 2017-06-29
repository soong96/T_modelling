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
public class TaskGetEuclidean implements Runnable{
    private CountDownLatch latch;
    private CountWord cw;
    private String doc1;
    private String doc2;
    private HashMap<String, Double> similarityMatrix;
    
    TaskGetEuclidean(CountWord cw, CountDownLatch latch, String doc1, String doc2, HashMap<String, Double> similarityMatrix){
        this.latch = latch;
        this.cw = cw;
        this.doc1 = doc1;
        this.doc2 = doc2;
        this.similarityMatrix = similarityMatrix;
    }
    
    
    public void run(){        
        try {      
            cw.calcEuclidean(doc1,doc2,similarityMatrix);//call method to calculate euclidean
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TaskGetEuclidean.class.getName()).log(Level.SEVERE, null, ex);
        }
        latch.countDown();
    }
}
