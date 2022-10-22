package com.mycompany.quine.mccluskey;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rodrigo da Rosa
 */
public class QuineMcCluskey extends Tools{

    public static void main(String[] args) throws Exception  {
        
        //String litTemplate = "ABCD+!A!BCD+A!B!C!D+!ABCD";
        //String litTemplate = "A!BCD+!ABC!D+!ABCD+A!B!CD";
        String litTemplate = "!A*!B*!C*!D + !A*!B*!C*D + !A*B*!C*D + !A*B*C*!D + !A*B*C*D";
        //String litTemplate = "ab!c+a!bc!d+!ab"; //diferentes tamanhos (definir pelo mintermo maior)
        //String litTemplate = "abc+bcd"; //diferentes tamanhos (definir pelo mintermo maior)
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
        print("\nVARS: "+exp.getNumberOfVars()+"\n");
        exp.sortByOnesCount();
        for(int i=0; i<exp.getProductsList().size(); i++) {
            print("\n");
            print(exp.getProductsList().get(i).getDecimalsList()+"\t");
            print(exp.getProductsList().get(i).getBinary()+"\t");
            print(exp.getProductsList().get(i).getLiteral());
        }
        
        exp.mergePrimeImplicants();
        print("\n+++++++++++++++++++++++++++++++\n\n");
        for(int i=0; i<exp.getProductsList().size(); i++) {
            int q = 0;
            for(; q<exp.getProductsList().get(i).getDecimalsList().size(); q++) {
                print("-"+exp.getProductsList().get(i).getDecimalsList().get(q));
            }
            if(q < 3)
                print("-\t");
            print("\t");
            print(exp.getProductsList().get(i).getBinary()+" \t");
            print(exp.getProductsList().get(i).getLiteral()+"\n");
        }
        
        exp.fillMinTermsList();
        for (int i=0; i<exp.getMinTermsList().size(); i++) {
            print("\n"+exp.getMinTermsList().get(i).getDecimal()+" -");
            for (int p=0; p<exp.getMinTermsList().get(i).getProductsList_NEW().size(); p++) {
                print("\t\t"+exp.getMinTermsList().get(i).getProductsList_NEW().get(p).getLiteral());//.get(0));
            }
        }
        
        exp.essentialProductsToFinalList();
        print("\n\nFinal Products List:\n");
        for (int i=0; i<exp.getFinalProductsList().size(); i++) {
            print(exp.getFinalProductsList().get(i).getLiteral()+"\t");
        }
        
        ArrayList<Integer> indexes = exp.getCandidateProductsIndexes();
        for (int i=0; i < indexes.size(); i++) {
            print("\n"+indexes.get(i));
        }
        
        for (int i=0; i<exp.getMinTermsList().size(); i++) {
            print("\nMinTerm "+exp.getMinTermsList().get(i).getDecimal()+" ");
            if (exp.getMinTermsList().get(i).isIsCovered())
                print("is covered.");
            else
                print(" - ");
        }
        
        exp.permute(indexes, indexes.size());
        for (int i=0; i<exp.getPermutations().size(); i++) {
            print("\n"+exp.getPermutations().get(i));
        }
        
        print("\n\nAdded products: "+exp.completeFinalListCandidate()+"\n");
        print("\nFinal Products List:\n");
        for (int i=0; i<exp.getFinalProductsList().size(); i++) {
            print(exp.getFinalProductsList().get(i).getLiteral()+"\t");
        }
        
        exp.setOptimizedExpression();
        print("\n\n"+exp.getOptimizedExpression()+"\n");
        
    }
}
