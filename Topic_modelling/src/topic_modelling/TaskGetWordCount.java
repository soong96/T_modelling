/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topic_modelling;

import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author soong96
 */
public class TaskGetWordCount implements Runnable{
    private CountDownLatch latch;
    private CountWord cw;
    private String doc;
    TaskGetWordCount(CountWord cw, CountDownLatch latch, String doc){
        this.latch = latch;
        this.cw = cw;
        this.doc = doc;
    }
    
    
    public void run(){
        try {
            cw.getWordCount(doc);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TaskGetWordCount.class.getName()).log(Level.SEVERE, null, ex);
        }
        latch.countDown();

    }
}