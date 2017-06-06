/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topic_modelling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author 15027493
 */
public class week8 {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        Scanner sc2 = null;
        HashMap<String, HashMap> a = new HashMap<String, HashMap>();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        HashMap<String, Integer> allWords = new HashMap<String, Integer>();
       

        for(int counter = 1; counter < 11; counter++){     
            LinkedList<String>[] doc=new LinkedList[10];  

            doc[counter-1] = new LinkedList<String>();
            String b = "doc"+counter;
            try {                
                sc2 = new Scanner(new File("C:\\Users\\soong96\\Documents\\GitHub\\T_modelling\\Topic_modelling\\src\\topic_modelling\\" + b + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();  
            }
            int i = 0;
            while (sc2.hasNextLine()) {
                    Scanner s2 = new Scanner(sc2.nextLine());                    
                while (s2.hasNext()) {
                    String s = s2.next();
                    String[] words = s.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s");
                    
                    if(!allWords.containsKey(words[0])){
                        allWords.put(words[0],0);
                    }
                    doc[i].add(words[0]);

                    /*if(map.containsKey(words[0])){
                        int i = map.get(words[0])+1;
                        map.put(words[0],i);
                    }
                    else{
                        map.put(words[0],1);
                    }*/


                }
                i++;
            }
        }
            for (int m = 0; m < doc.length; m++){
                Iterator<String> itr=doc[m].iterator();  
                while(itr.hasNext()){    
                    String word = itr.next();
                    if(map.containsKey(word)){
                        int count = map.get(word)+1;
                        map.put(word,count);
                    }
                    else{
                        map.put(word,1);
                    }
                }
                int first = 0;
                String firstString = null;
                int second = 0;
                String secondString = null;
                for(Map.Entry ma:map.entrySet()){  
                    if(first < (int)ma.getValue()){
                        second = first;
                        secondString = firstString;
                        first = (int) ma.getValue();
                        firstString = (String) ma.getKey();
                    }                            
                }
                System.out.println("Highest :" + first +" = " + firstString);
                System.out.println("Second Highest :" + second +" = " + secondString);
                System.out.println(map);
                System.out.println("\n\n");
                a.put(b,map);
                map.replaceAll((k,v) -> 0);
            }

            
           
        
        System.out.println(a);
    }
}
