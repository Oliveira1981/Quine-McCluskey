package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public class QuineMcCluskey {

    public static void main(String[] args) throws Exception  {
        
        //String litTemplate = "ABCD+!A!BCD+A!B!C!D+!ABCD";
        //String litTemplate = "A!BCD+!ABC!D+!ABCD+A!B!CD";
        //String litTemplate = "!A*!B*!C*!D + !A*!B*!C*D + !A*B*!C*D + !A*B*C*!D + !A*B*C*D";
        //String litTemplate = "ab!c+a!bc!d+!ab"; //diferentes tamanhos (definir pelo mintermo maior)
        String litTemplate = "abc+bcd"; //diferentes tamanhos (definir pelo mintermo maior)
        //String binTemplate = "1111+0011+1000+0111";
        String binTemplate = "1111+0011+1010+0111";
        //String binTemplate = "111+11+101+01"; //diferentes tamanhos (definir pelo mintermo maior)
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
        
        SumOfProducts exp = new SumOfProducts(inputFormat, expression);
        exp.print("\nVARS: "+exp.getNumberOfVars()+"\n");
        exp.sortByOnesCount();
        for(int i=0; i<exp.getProductsList().size(); i++) {
            exp.print("\n");
            exp.print(exp.getProductsList().get(i).getDecimalsList()+"   ");
            exp.print(exp.getProductsList().get(i).getBinary()+"   ");
            exp.print(exp.getProductsList().get(i).getLiteral());
        }
        
        exp.mergePrimeImplicants();
        exp.print("\n+++++++++++++++++++++++++++++++\n");
        for(int i=0; i<exp.getProductsList().size(); i++) {
            for(int q=0; q<exp.getProductsList().get(i).getDecimalsList().size(); q++) {
                exp.print("-"+exp.getProductsList().get(i).getDecimalsList().get(q));
            }
            exp.print("-   ");
            exp.print(exp.getProductsList().get(i).getBinary()+"   ");
            exp.print(exp.getProductsList().get(i).getLiteral()+"\n");
        }
        
        for (int i=0; i<exp.getMinTermsList().size(); i++) {
            exp.print("\n"+exp.getMinTermsList().get(i).getDecimal());
        }
        exp.setOptimizedExpression();
        exp.print("\n"+exp.getOptimizedExpression()+"\n");
        
    }
}
