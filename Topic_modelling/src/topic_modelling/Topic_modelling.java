/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topic_modelling;

import java.io.FileNotFoundException;

/**
 *
 * @author soong96
 */
public class Topic_modelling {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        CountWord cw = new CountWord();
        cw.calcWordCount();
    }
    
}
