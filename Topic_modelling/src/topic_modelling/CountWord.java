/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topic_modelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author 15027493
 */
public class CountWord {
    private HashMap<String, Object> mapAll = new HashMap<String, Object>();
    private HashMap<String, Double> map = new HashMap<String, Double>();
    private HashMap<String, Double> mapTFIDF = new HashMap<String, Double>();
    private HashMap<String, Object> mapAllTFIDF = new HashMap<String, Object>();
    //private HashMap<String, Double> similarityMatrix = new HashMap<String, Double>();
    private LinkedList<String> stopwords = new LinkedList<String>();
    private static final char DEFAULT_SEPARATOR = ',';
    public int wordcount = 0;
    public int totalword = 0;
    
    //method to get all the words in all documents.
    public void getAllWord(String doc) throws FileNotFoundException{
        Scanner sc2 = null;
        Stemmer stem= new Stemmer();
   
        try {                
            sc2 = new Scanner(new File("src\\topic_modelling\\Document\\" + doc + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
        while (sc2.hasNextLine()) {
                Scanner s2 = new Scanner(sc2.nextLine());
            while (s2.hasNext()) {
                String s = s2.next();
                StringTokenizer st = new StringTokenizer(s);  
                synchronized(this){
                    while (st.hasMoreTokens()) {  
                        s = stem.stem(st.nextToken(","));                       
                        String[] words = s.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s");    
                        if(!stopwords.contains(words[0])){

                                if(!map.containsKey(words[0])){
                                    map.put(words[0],0.0);
                                    wordcount++;
                                }
                        }                  
                    }  
                }
                map.remove("");

            }
        }
        System.out.println("The word count currently are: " + wordcount);
    }
    //method to get the word counts for each documents
    public synchronized void getWordCount(String doc) throws FileNotFoundException{
        Scanner sc2 = null;
        Stemmer stem= new Stemmer();

        try {                
            sc2 = new Scanner(new File("src\\topic_modelling\\Document\\" + doc + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
        while (sc2.hasNextLine()) {
                Scanner s2 = new Scanner(sc2.nextLine());
            while (s2.hasNext()) {
                String s = s2.next();
                StringTokenizer st = new StringTokenizer(s);                  
                while (st.hasMoreTokens()) {  
                    s = stem.stem(st.nextToken(","));  
                    String[] words = s.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s");
                   // words[0] = stem.stem(words[0]);
                    if(!stopwords.contains(words[0])){
                        if(map.containsKey(words[0])){
                            double i = map.get(words[0])+1;
                            map.put(words[0],i);
                            totalword = totalword + 1;
                        }
                        else{
                            map.put(words[0],1.0);
                            totalword = totalword + 1;
                        }      
                    }
                }
                
            }
            map.remove("");

        }
        double first = 0;
        for(Map.Entry m:map.entrySet()){  
            if(first < (double)m.getValue()){
                first = (double) m.getValue();
            }                            
        }

        //normalizeMap(first);//normalization is replaced by TFIDF 
        //System.out.println(map);
        //System.out.println("\n\n");
        mapAll.put(doc,map.clone());
        map.replaceAll((k,v) -> 0.0);

    }
    //method to calculate and get all the TFIDF of all documents
    public void calcAndGetTFIDF(String doc){
        // TODO code application logic here              
      /*
        for(Map.Entry m:mapAll.entrySet()){  
            map.putAll((HashMap)m.getValue());
            for(Map.Entry m2:map.entrySet()){  
                if((double)m2.getValue() == 0){
                    mapTFIDF.put(m2.getKey().toString(), 0.0);
                }
                else
                    mapTFIDF.put(m2.getKey().toString(), calcTFIDF(m2.getKey().toString()));                
            }
            mapAllTFIDF.put(m.getKey().toString(),mapTFIDF.clone());
            mapTFIDF.replaceAll((k,v) -> 0.0);
           
        }    */  
        HashMap<String, Double> hashmap = new HashMap<String, Double>();
        HashMap<String, Double> hashmapTFIDF = new HashMap<String, Double>();
        
        hashmap.putAll((HashMap)mapAll.get(doc));
        //System.out.println(hashmap);
        for(Map.Entry m2:hashmap.entrySet()){
            if((double)m2.getValue() == 0){               
                hashmapTFIDF.put(m2.getKey().toString(), 0.0);
            }
            else
                hashmapTFIDF.put(m2.getKey().toString(), calcTFIDF(m2.getKey().toString(), hashmap));                
        }
        System.out.println(hashmapTFIDF);
        mapAllTFIDF.put(doc,hashmapTFIDF.clone());
;
        
    }
    
    
    
    //method to get the euclidean distance between two document and put the value into matrix
    public void calcEuclidean(String doc1, String doc2, HashMap<String, Double> similarityMatrix) throws FileNotFoundException{
        String compare = doc1 +" : " + doc2;
        double result = EuclideanDistance((HashMap)mapAllTFIDF.get(doc1),(HashMap)mapAllTFIDF.get(doc2));
        similarityMatrix.put(compare, result);     
       // System.out.println(similarityMatrix);
        //writeCSVTFIDF();
        //writeCSVSimilarMatrix("Euclidean");
        //similarityMatrix.clear();
    }
    //method to get the cosine similarity between two document and put the value into matrix
    public void calcCosine(String doc1, String doc2, HashMap<String, Double> similarityMatrix) throws FileNotFoundException{
        String compare = doc1 +" : " + doc2;           
        double result = cosineSimilarity((HashMap)mapAllTFIDF.get(doc1),(HashMap)mapAllTFIDF.get(doc2));
        similarityMatrix.put(compare, result);               
       // writeCSVSimilarMatrix("Cosine");
       // similarityMatrix.clear();
    }
    //method to get the manhanttan distance between two document and put the value into matrix
    public void calcManhanttan(String doc1, String doc2, HashMap<String, Double> similarityMatrix) throws FileNotFoundException{
        String compare = doc1 +" : " + doc2;
        double result = manhanttanDistance((HashMap)mapAllTFIDF.get(doc1),(HashMap)mapAllTFIDF.get(doc2));
        similarityMatrix.put(compare, result);  
        //writeCSVSimilarMatrix("Manhanttan");
        //similarityMatrix.clear();
    }
    
    //method to write TFIDF of each documents into matrix
    public void writeCSVTFIDF() throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(new File("matrix.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append(" "+DEFAULT_SEPARATOR);
        for(Map.Entry m:mapTFIDF.entrySet()){          
            writeLine(sb,m.getKey().toString());           
        }
        sb.append("\n");

        for(int i = 1; i <= mapAllTFIDF.size(); i++){
            String b = "doc"+i;
            writeLine(sb,b);
            mapTFIDF.putAll((HashMap)mapAllTFIDF.get(b));
            for(Map.Entry m:mapTFIDF.entrySet()){          
                writeLine(sb,m.getValue().toString());           
            }
            sb.append("\n");
        }
        /*for(int i = 1; i <= 20 ; i++){
            for(int j = 1; j <= 20; j++){
                String b = "doc" + i + " : " + "doc" +j;
                writeLine(sb,b);
                sb.append(similarityMatrix.get(b));
                sb.append("\n");
                
            }
        } */
        pw.write(sb.toString());
        pw.close();        
        
    }
            
    //method to write the computed similarity matrix into CSV file
    public void writeCSVSimilarMatrix(String matrix, HashMap similarityMatrix) throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(new File("matrix" + matrix + ".csv"));
        StringBuilder sb = new StringBuilder();
        sb.append(" "+DEFAULT_SEPARATOR);
        for(int i = 1; i <= 20 ; i++){
            String b = "doc"+i;
            writeLine(sb,b);
        }
        sb.append("\n");
        for(int i = 1; i <= 20 ; i++){
            writeLine(sb,"doc"+i);
            for(int j = 1; j <= 20; j++){
                String b = "doc" + i + " : " + "doc" +j;               
                writeLine(sb,similarityMatrix.get(b).toString());                
            }
            sb.append("\n");
        } 
        pw.write(sb.toString());
        pw.close();  
    }   
    
    
    public void writeLine(StringBuilder sb, String word) throws FileNotFoundException{
        sb.append(word + DEFAULT_SEPARATOR);
    }
    //method for normalization
    public void normalizeMap(double largest){
         for(Map.Entry m:map.entrySet()){               
             double calculation = (double)m.getValue()/largest;
             String key = (String)m.getKey();
             map.put(key, calculation);
             
         }
    }
    
    //method to calculate IDF
    public double calcIDF(String term){
        double n =0;
        HashMap<String, Double> hash = new HashMap<String, Double>();
        for(Map.Entry m:mapAll.entrySet()){               
            String key = (String)m.getKey();
            hash.putAll((HashMap)mapAll.get(key));
            if(hash.get(term)>0){
                n++;
            }
            hash.clear();
        }
        
        double numDocs = mapAll.size();
        double result = numDocs/n;
        return Math.log(result);
    }
    //method to calculate TF
    public double calcTF(String term, HashMap<String, Double> map){
        double termFreq = map.get(term);
        double totalTerm = 0;

        for(Map.Entry m:map.entrySet()){            
            totalTerm = totalTerm + (double)m.getValue();       
     
        }
        return termFreq/totalTerm;
    }
    //method to calculate TFIDF
    public double calcTFIDF(String term, HashMap<String, Double> map){
        
        return calcTF(term, map)*calcIDF(term);
        
    }
    
    //method to get all the stopwords into list
    public void stopWords(){
        Scanner sc2 = null;
        try {                
            sc2 = new Scanner(new File("src\\topic_modelling\\stopwords.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
        while (sc2.hasNextLine()) {
                Scanner s2 = new Scanner(sc2.nextLine());
            while (s2.hasNext()) {
                String s = s2.next();             
                String[] words = s.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s");
                stopwords.add(words[0]);   
                
            }
        }
    }
    
    //method to calculate euclidean distance between two document
    public double EuclideanDistance(HashMap doc1, HashMap doc2){
        HashMap<String, Double> hash1 = new HashMap<String,Double>();
        HashMap<String, Double> hash2 = new HashMap<String,Double>();
        hash1.putAll(doc1);
        hash2.putAll(doc2);
        Double finalResult = 0.0;
        for(Map.Entry m:mapTFIDF.entrySet()){               
            String key = (String)m.getKey();
            Double x1 = hash1.get(key);
            Double x2 = hash2.get(key);
            Double diff = x1-x2;
            Double result = Math.pow(diff, 2);  
            finalResult = finalResult + result;
        }
        return Math.sqrt(finalResult);
    }     
    //method to calculate cosine similarity between two document
    public double cosineSimilarity(HashMap doc1, HashMap doc2){
        HashMap<String, Double> hash1 = new HashMap<String,Double>();
        HashMap<String, Double> hash2 = new HashMap<String,Double>();
        hash1.putAll(doc1);
        hash2.putAll(doc2);
        double dotProduct = 0.0;
        double normX = 0.0;
        double normY = 0.0;
        for(Map.Entry m:mapTFIDF.entrySet()){               
            String key = (String)m.getKey();
            dotProduct = dotProduct + hash1.get(key) * hash2.get(key);
            normX = normX + Math.pow(hash1.get(key), 2);
            normY = normY + Math.pow(hash2.get(key), 2);           
        }
        double finalResult = dotProduct/ (Math.sqrt(normX)* Math.sqrt(normY));
        //return 1 - finalResult;
        return finalResult;
    }
    //method to calculate manhanttan distance between two document
    public double manhanttanDistance(HashMap doc1, HashMap doc2){
        HashMap<String, Double> hash1 = new HashMap<String,Double>();
        HashMap<String, Double> hash2 = new HashMap<String,Double>();
        hash1.putAll(doc1);
        hash2.putAll(doc2);
        double distance = 0.0;
        // the sum of the absolute values of the horizontal and the vertical distance
        for(Map.Entry m:mapTFIDF.entrySet()){               
            String key = (String)m.getKey();
            double A = hash1.get(key);
            double B = hash2.get(key);
            distance = distance + Math.abs(A - B);
        }
        return distance;
    }
    
}
