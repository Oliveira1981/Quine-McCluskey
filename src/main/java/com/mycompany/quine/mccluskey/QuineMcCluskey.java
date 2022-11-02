package com.mycompany.quine.mccluskey;

/**
 *
 * @author Rodrigo da Rosa
 */
public class QuineMcCluskey extends Tools{
        
    public static void main(String[] args) throws Exception  {
        
        String template = "2+4+6+8+9+10+12+13+15";
        
        GUI gui = new GUI();
        gui.showDialog(template);
    }
}
