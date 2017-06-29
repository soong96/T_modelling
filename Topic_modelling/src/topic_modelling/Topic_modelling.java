/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topic_modelling;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author soong96
 */
public class Topic_modelling {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        CountWord cw = new CountWord();
        cw.stopWords();//read the stopword into a list
        CountDownLatch latch = new CountDownLatch(20);
        //use latch to make sure we get all the words into hashmap before getting the word count of each document
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 1; i < 21 ;i++){
            String doc = "doc"+(i);
            pool.submit(new TaskGetWord(cw, latch, doc));
        }
        System.out.println("waiting for GET ALL WORDS processes to complete....");
        try {
            //current thread will get notified if all chidren's are done 
            // and thread will resume from wait() mode.
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("GET ALL WORDS Process Completed....");
        System.out.println("Parent Thread Resuming work to GET WORD COUNT....");
        
        CountDownLatch latch1 = new CountDownLatch(20);
        for (int i = 1; i < 21 ;i++){
            String doc = "doc"+(i);
            pool.submit(new TaskGetWordCount(cw, latch1, doc)); //Let threads to get all the word counts for each documents
        }
          System.out.println("waiting for GET COUNT WORDS processes to complete....");
        try {
            //current thread will get notified if all chidren's are done 
            // and thread will resume from wait() mode.
            latch1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("GET COUNT WORDS Process Completed....");
        System.out.println("Parent Thread Resuming work to CALCULATE TFIDF....");
        
        CountDownLatch latchTFIDF = new CountDownLatch(20);
        for (int i = 1; i < 21 ;i++){
            String doc = "doc"+i;
            pool.submit(new TaskGetTFIDF(cw, latchTFIDF, doc)); //Let threads to get all the word counts for each documents
        }
          System.out.println("waiting for CALCULATION OF TFIDF processes to complete....");
        try {
            //current thread will get notified if all chidren's are done 
            // and thread will resume from wait() mode.
            latchTFIDF.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("CALCULATION OF TFIDF Process Completed....");
        System.out.println("Parent Thread Resuming work to CALCULATE EUCLIDEAN MATRIX, COSINE MATRIX and MANHANTTAN MATRIX....");

        cw.writeCSVTFIDF(); //Write tfidf into CSV file
        
        CountDownLatch latch2 = new CountDownLatch(1200);//latch to ensure all 20*20 documents is compared
        HashMap<String, Double> similarityMatrixEuclidean = new HashMap<String, Double>();
        HashMap<String, Double> similarityMatrixCosine = new HashMap<String, Double>();
        HashMap<String, Double> similarityMatrixManhanttan = new HashMap<String, Double>();
        for (int i = 1; i < 21; i++){
            String doc1 = "doc" + i;
            for(int j = 1; j < 21; j++){                
               String doc2 = "doc" + j;
               pool.submit(new TaskGetEuclidean(cw,latch2, doc1, doc2, similarityMatrixEuclidean));//submit task to threads to compare the documents
               pool.submit(new TaskGetCosine(cw,latch2, doc1, doc2, similarityMatrixCosine));
               pool.submit(new TaskGetManhanttan(cw,latch2, doc1, doc2, similarityMatrixManhanttan));
            }
        }
        System.out.println("waiting for CALCULATION OF EUCLIDEAN MATRIX, COSINE MATRIX and MANHANTTAN MATRIX processes to complete....");
        try {
            //current thread will get notified if all chidren's are done 
            // and thread will resume from wait() mode.
            latch2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("CALCULATION OF EUCLIDEAN MATRIX, COSINE MATRIX and MANHANTTAN MATRIX Process Completed....");
        System.out.println("Parent Thread Resuming work to Print Similarity Matrix into CSV file....");
        
        //submit tasks to print CSV of similarity matrix computed previously
        pool.submit(new TaskWriteCSVMatrix(cw,"Euclidean", similarityMatrixEuclidean));
        pool.submit(new TaskWriteCSVMatrix(cw,"Cosine", similarityMatrixCosine));
        pool.submit(new TaskWriteCSVMatrix(cw,"Manhanttan", similarityMatrixManhanttan));

        pool.shutdown();
        
        if(!pool.awaitTermination(5, TimeUnit.MINUTES)){
            pool.shutdownNow();
        }

        System.out.println(cw.wordcount);
        System.out.println(cw.totalword);

    }
    
}
