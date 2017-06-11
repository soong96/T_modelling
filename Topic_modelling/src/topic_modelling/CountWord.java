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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author 15027493
 */
public class CountWord {
    private HashMap<String, Object> mapAll = new HashMap<String, Object>();
    private HashMap<String, Integer> map = new HashMap<String, Integer>();
    private HashMap<String, Double> mapTFIDF = new HashMap<String, Double>();
    private HashMap<String, Object> mapAllTFIDF = new HashMap<String, Object>();
    private static final char DEFAULT_SEPARATOR = ',';
    
    public void calcWordCount() throws FileNotFoundException{
        // TODO code application logic here
        Scanner sc2 = null;
        for(int y = 1; y < 11; y++){
            String b = "doc"+y;
            try {                
                sc2 = new Scanner(new File("src\\topic_modelling\\" + b + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();  
            }
            while (sc2.hasNextLine()) {
                    Scanner s2 = new Scanner(sc2.nextLine());
                while (s2.hasNext()) {
                    String s = s2.next();
                    String[] words = s.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s");
                    if(!map.containsKey(words[0])){
                        map.put(words[0],0);
                    }
                }
            }
        }

        for(int y = 1; y < 11; y++){     
            
            String b = "doc"+y;
            try {                
                sc2 = new Scanner(new File("src\\topic_modelling\\" + b + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();  
            }
            while (sc2.hasNextLine()) {
                    Scanner s2 = new Scanner(sc2.nextLine());
                while (s2.hasNext()) {
                    String s = s2.next();
                    String[] words = s.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s");

                    if(map.containsKey(words[0])){
                        int i = map.get(words[0])+1;
                        map.put(words[0],i);
                    }
                    else{
                        map.put(words[0],1);
                    }                               

                }
            }
            int first = 0;
            for(Map.Entry m:map.entrySet()){  
                if(first < (int)m.getValue()){
                    first = (int) m.getValue();
                }                            
            }
            //normalizeMap(first);
            System.out.println(map);
            System.out.println("\n\n");
            mapAll.put(b,map.clone());
            map.replaceAll((k,v) -> 0);
        }
       
        for(Map.Entry m:mapAll.entrySet()){  
            map.putAll((HashMap)m.getValue());
            for(Map.Entry m2:map.entrySet()){  
                if((int)m2.getValue() == 0){
                    mapTFIDF.put(m2.getKey().toString(), 0.0);
                }
                else
                    mapTFIDF.put(m2.getKey().toString(), calcTFIDF(m2.getKey().toString()));                
            }
            mapAllTFIDF.put(m.getKey().toString(),mapTFIDF.clone());
            mapTFIDF.clear();
           
        }
        writeCSV();
    }
    
    public void writeCSV() throws FileNotFoundException{
        PrintWriter pw = new PrintWriter(new File("matrix.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append(" "+DEFAULT_SEPARATOR);
        for(Map.Entry m:map.entrySet()){          
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
        pw.write(sb.toString());
        pw.close();        
        
    }
            
        
    
    
    public void writeLine(StringBuilder sb, String word) throws FileNotFoundException{
        sb.append(word + DEFAULT_SEPARATOR);
    }
    
    public void normalizeMap(int largest){
         for(Map.Entry m:map.entrySet()){               
             int calculation = (int)m.getValue()/largest;
             String key = (String)m.getKey();
             map.put(key, calculation);
             
         }
    }
    
    public double calcIDF(String term){
        int n =0;
        HashMap<String, Integer> hash = new HashMap<String, Integer>();
        for(Map.Entry m:mapAll.entrySet()){               
            String key = (String)m.getKey();
            hash.putAll((HashMap)mapAll.get(key));
            if(hash.get(term)>0){
                n++;
            }
            hash.clear();
        }
        return Math.log(mapAll.size()/n);
    }
    
    public double calcTF(String term){
        int termFreq = map.get(term);
        int totalTerm = 0;

        for(Map.Entry m:map.entrySet()){            
            totalTerm = totalTerm + (int)m.getValue();       
     
        }
        return termFreq/totalTerm;
    }
    
    public double calcTFIDF(String term){
        
        return calcTF(term)*calcIDF(term);
        
    }
}
