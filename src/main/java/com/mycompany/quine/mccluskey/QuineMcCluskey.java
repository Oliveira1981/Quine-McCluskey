package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public class QuineMcCluskey extends Tools{

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
            exp.print(exp.getProductsList().get(i).getDecimalsList()+"\t");
            exp.print(exp.getProductsList().get(i).getBinary()+"\t");
            exp.print(exp.getProductsList().get(i).getLiteral());
        }
        
        exp.mergePrimeImplicants();
        exp.print("\n+++++++++++++++++++++++++++++++\n\n");
        for(int i=0; i<exp.getProductsList().size(); i++) {
            int q = 0;
            for(; q<exp.getProductsList().get(i).getDecimalsList().size(); q++) {
                exp.print("-"+exp.getProductsList().get(i).getDecimalsList().get(q));
            }
            if(q < 3)
                exp.print("-\t");
            exp.print("\t");
            exp.print(exp.getProductsList().get(i).getBinary()+" \t");
            exp.print(exp.getProductsList().get(i).getLiteral()+"\n");
        }
        
        exp.fillMinTermsList();
        for (int i=0; i<exp.getMinTermsList().size(); i++) {
            exp.print("\n"+exp.getMinTermsList().get(i).getDecimal()+" -");
            for (int p=0; p<exp.getMinTermsList().get(i).getProductsList_NEW().size(); p++) {
                exp.print("\t\t"+exp.getMinTermsList().get(i).getProductsList_NEW().get(p).getLiteral());//.get(0));
            }
        }
        
        exp.essentialProductsToFinalList();
        exp.print("\n\nFinal Products List:\n");
        for (int i=0; i<exp.getFinalProductsList().size(); i++) {
            exp.print(exp.getFinalProductsList().get(i).getLiteral()+"\n");
        }
        
        for (int i=0; i<exp.getMinTermsList().size(); i++) {
            exp.print("\nMinTerm "+exp.getMinTermsList().get(i).getDecimal()+" ");
            if (exp.getMinTermsList().get(i).isIsCovered())
                exp.print("is covered.");
            else
                exp.print(" - ");
        }
        
        exp.print("\n\nAdded products: "+exp.completeFinalListCandidate()+"\n");
        exp.print("\nFinal Products List:\n");
        for (int i=0; i<exp.getFinalProductsList().size(); i++) {
            exp.print(exp.getFinalProductsList().get(i).getLiteral()+"\t");
        }
        
        exp.setOptimizedExpression();
        exp.print("\n\n"+exp.getOptimizedExpression()+"\n");
        
    }
}
