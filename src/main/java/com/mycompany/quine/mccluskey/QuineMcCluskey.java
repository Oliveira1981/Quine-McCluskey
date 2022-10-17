package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public class QuineMcCluskey {

    public static void main(String[] args) throws Exception  {
        
        String litTemplate = "ABCD+!A!BCD+A!B!C!D+!ABCD";
        //String litTemplate = "A!BCD+!ABC!D+!ABCD+A!B!CD";
        String binTemplate = "1111+0011+1000+0111";
        //String binTemplate = "1111+0011+1010+0111";
        //String deciTemplate = "15+3+10+7";
        String deciTemplate = "2+4+6+8+9+10+12+13+15";
        
        GUI gui = new GUI();
        gui.showDialog(deciTemplate);
        
        String inputFormat;
        String expression;
        inputFormat = gui.getInputFormat();
        expression = gui.getExpression();
        if(expression == null) System.exit(0);
        
        System.out.println("inputFormat:"+inputFormat);
        System.out.println("expression:"+expression);
        
        SOP exp = new SOP(inputFormat, expression);
        
        for(int i=0; i<exp.getMinTermsTable().size(); i++) {
            exp.print("\n");
            exp.print(exp.getMinTermsTable().get(i).getDecimal()+"   ");
            exp.print(exp.getMinTermsTable().get(i).getBinary()+"   ");
            exp.print(exp.getMinTermsTable().get(i).getLiteral()+"\n");
        }
        
        exp.groupPrimeImplicants();
        exp.print("\n+++++++++++++++++++++++++++++++\n");
        
        for(int i=0; i<exp.getMinTermsTable().size(); i++) {
            for(int q=0; q<exp.getMinTermsTable().get(i).getDecimal().size(); q++) {
                exp.print("-"+exp.getMinTermsTable().get(i).getDecimal().get(q));
            }
            exp.print("-   ");
            exp.print(exp.getMinTermsTable().get(i).getBinary()+"   ");
            exp.print(exp.getMinTermsTable().get(i).getLiteral()+"\n");
        }
        exp.setOptimizedExpression();
        exp.print("\n"+exp.getOptimizedExpression()+"\n");
        
    }
}
