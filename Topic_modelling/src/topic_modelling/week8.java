/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package week8q3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
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
        HashMap<String, Object> mapAll = new HashMap<String, Object>();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        LinkedList list = new LinkedList();

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
                    
                    if(!list.contains(words[0])){
                        list.add(words[0]);
                    }


                }
            }
            int first = 0;
            String firstString = null;
            int second = 0;
            String secondString = null;
            for(Map.Entry m:map.entrySet()){  
                if(first < (int)m.getValue()){
                    second = first;
                    secondString = firstString;
                    first = (int) m.getValue();
                    firstString = (String) m.getKey();
                }                            
            }
            System.out.println("Highest :" + first +" = " + firstString);
            System.out.println("Second Highest :" + second +" = " + secondString);
            System.out.println(map);
            System.out.println("\n\n");
            mapAll.put(b,map.clone());
            map.clear();
        }
        System.out.println(mapAll);
        System.out.println(list);
    }
}
