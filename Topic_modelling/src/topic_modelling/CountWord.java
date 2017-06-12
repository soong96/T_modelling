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

/**
 *
 * @author 15027493
 */
public class CountWord {
    private HashMap<String, Object> mapAll = new HashMap<String, Object>();
    private HashMap<String, Double> map = new HashMap<String, Double>();
    private HashMap<String, Double> mapTFIDF = new HashMap<String, Double>();
    private HashMap<String, Object> mapAllTFIDF = new HashMap<String, Object>();
    private HashMap<String, Double> similarityMatrix = new HashMap<String, Double>();
    private LinkedList<String> stopwords = new LinkedList<String>();
    private static final char DEFAULT_SEPARATOR = ',';
    
    public void calcWordCount() throws FileNotFoundException{
        // TODO code application logic here
        Scanner sc2 = null;
        stopWords();
        Stemmer stem= new Stemmer();
        for(int y = 1; y < 21; y++){
            String b = "doc"+y;
            try {                
                sc2 = new Scanner(new File("src\\topic_modelling\\Document\\" + b + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();  
            }
            while (sc2.hasNextLine()) {
                    Scanner s2 = new Scanner(sc2.nextLine());
                while (s2.hasNext()) {
                    String s = s2.next();
                    s = stem.stem(s);
                    String[] words = s.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s");
                    
                    if(!stopwords.contains(words[0])){
                        if(!map.containsKey(words[0])){
                            map.put(words[0],0.0);
                        }
                    }

                    map.remove("");

                }
            }
        }

        for(int y = 1; y < 21; y++){     
            
            String b = "doc"+y;
            try {                
                sc2 = new Scanner(new File("src\\topic_modelling\\Document\\" + b + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();  
            }
            while (sc2.hasNextLine()) {
                    Scanner s2 = new Scanner(sc2.nextLine());
                while (s2.hasNext()) {
                    String s = s2.next();
                    s = stem.stem(s);
                    String[] words = s.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s");
                   // words[0] = stem.stem(words[0]);
                    if(!stopwords.contains(words[0])){
                        if(map.containsKey(words[0])){
                            double i = map.get(words[0])+1;
                            map.put(words[0],i);
                        }
                        else{
                            map.put(words[0],1.0);
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
            
            //normalizeMap(first);
            System.out.println(map);
            System.out.println("\n\n");
            mapAll.put(b,map.clone());
            map.replaceAll((k,v) -> 0.0);
        }
       
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
           
        }
        for(int j = 1; j < 21; j++){
            String doc1 = "doc" + j;
            for(int i = 1; i < 21; i++){
                
                String doc2 = "doc" + i;
                String compare = doc1 +" : " + doc2;
                double result = EuclideanDistance((HashMap)mapAllTFIDF.get(doc1),(HashMap)mapAllTFIDF.get(doc2));
                similarityMatrix.put(compare, result);
            }
        }
        writeCSVTFIDF();
        writeCSVSimilarMatrix();
    }
    
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
            mapTFIDF.clear();
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
            
    public void writeCSVSimilarMatrix() throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(new File("matrix1.csv"));
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= 20 ; i++){
            for(int j = 1; j <= 20; j++){
                String b = "doc" + i + " : " + "doc" +j;
                writeLine(sb,b);
                sb.append(similarityMatrix.get(b));
                sb.append("\n");
                
            }
        } 
        pw.write(sb.toString());
        pw.close();  
    }   
    
    
    public void writeLine(StringBuilder sb, String word) throws FileNotFoundException{
        sb.append(word + DEFAULT_SEPARATOR);
    }
    
    public void normalizeMap(double largest){
         for(Map.Entry m:map.entrySet()){               
             double calculation = (double)m.getValue()/largest;
             String key = (String)m.getKey();
             map.put(key, calculation);
             
         }
    }
    
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
    
    public double calcTF(String term){
        double termFreq = map.get(term);
        double totalTerm = 0;

        for(Map.Entry m:map.entrySet()){            
            totalTerm = totalTerm + (double)m.getValue();       
     
        }
        return termFreq/totalTerm;
    }
    
    public double calcTFIDF(String term){
        
        return calcTF(term)*calcIDF(term);
        
    }
    
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
        return finalResult;
    }     
    
    
}
