package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public class QuineMcCluskey extends Tools{
    
    public static void main(String[] args) throws Exception  {
        
        //String template = "ABCD+!A!BCD+A!B!C!D+!ABCD";
        //String template = "A!BCD+!ABC!D+!ABCD+A!B!CD";
        //String template = "!A*!B*!C*!D + !A*!B*!C*D + !A*B*!C*D + !A*B*C*!D + !A*B*C*D";
        //String template = "ab!c+a!bc!d+!ab"; //diferentes tamanhos (definir pelo mintermo maior)
        //String template = "abc+bcd"; //diferentes tamanhos (definir pelo mintermo maior)
        
        //String template = "1111+0011+1000+0111";
        //String template = "1111+0011+1010+0111";
        //String template = "111+11+101+01"; //diferentes tamanhos (definir pelo mintermo maior)
        
        //String template = "15+3+10+7";
        String template = "2+4+6+8+9+10+12+13+15";
        
        GUI gui = new GUI();

/////////////////////////////////////////////////////
        gui.showDialog(template);
/////////////////////////////////////////////////////
        
        String inputFormat;
        String expression;
        
/////////////////////////////////////////////////////
        //inputFormat = gui.getInputFormat();
        expression  = gui.getExpression();
        if(expression == null)
            System.exit(0);
        inputFormat = inputFormatDetection(expression);
/////////////////////////////////////////////////////
        
        System.out.println("Formato de entrada:\n - "+inputFormat);
        System.out.println("\nExpressão:\n - "+expression);
        
/////////////////////////////////////////////////////
        SumOfProducts exp = new SumOfProducts(inputFormat, expression);
/////////////////////////////////////////////////////

        print("\nVariáveis: "+exp.getNumberOfVars()+"\n");

/////////////////////////////////////////////////////
        exp.sortByOnesCount();
/////////////////////////////////////////////////////
        
        print ("\nMintermos por número de 1s:");
        for(int i=0; i < exp.getProductsList().size(); i++) {
            print("\n");
            print(exp.getProductsList().get(i).getMinTermsList()+"\t");
            print(exp.getProductsList().get(i).getBinaryView()+"\t");
            print(exp.getProductsList().get(i).getLiteralView());
        }
        
/////////////////////////////////////////////////////
        exp.mergePrimeImplicants(10);
/////////////////////////////////////////////////////

        print("\n\nImplicantes primos mesclados:\n");
        for(int i=0; i < exp.getProductsList().size(); i++) {
            int q = 0;
            for(; q < exp.getProductsList().get(i).getMinTermsList().size(); q++) {
                print("-"+exp.getProductsList().get(i).getMinTermsList().get(q));
            }
            if(q < 3)
                print("-\t");
            print("\t");
            print(exp.getProductsList().get(i).getBinaryView()+" \t");
            print(exp.getProductsList().get(i).getLiteralView()+"\n");
        }
        
/////////////////////////////////////////////////////
        exp.fillMinTermsList();
/////////////////////////////////////////////////////
        
        print ("\nMintermos e seus produtos (Tabela de Cobertura):");
        for (int i=0; i < exp.getMinTermsList().size(); i++) {
            print("\n"+exp.getMinTermsList().get(i).getDecimalView()+" -");
            for (int p=0; p < exp.getMinTermsList().get(i).getProductsList().size(); p++) {
                print("\t\t"+exp.getMinTermsList().get(i).getProductsList().get(p).getLiteralView());//.get(0));
                if (exp.getMinTermsList().get(i).getProductsList().get(p).getLiteralView().length() < 8)
                    print("\t");
            }
        }
        
/////////////////////////////////////////////////////
        exp.essentialProductsToFinalList();
/////////////////////////////////////////////////////
        
        print("\n\nProdutos Essenciais:\n");
        for (int i=0; i < exp.getFinalProductsList().size(); i++) {
            print(exp.getFinalProductsList().get(i).getLiteralView()+"\t");
        }
        
/////////////////////////////////////////////////////
        ArrayList<Integer> indexes = exp.getCandidateProductsIndexes();
/////////////////////////////////////////////////////
        print("\n");
        for (int i=0; i < exp.getMinTermsList().size(); i++) {
            print("\nMinTerm "+exp.getMinTermsList().get(i).getDecimalView()+" ");
            if (exp.getMinTermsList().get(i).isIsCovered())
                print("está coberto.");
            else
                print(" - ");
        }
        
/////////////////////////////////////////////////////
        exp.permute(indexes);
/////////////////////////////////////////////////////

        /*for (int i=0; i<exp.getPermutations().size(); i++) {
            print("\n"+exp.getPermutations().get(i));
        }*/
        
/////////////////////////////////////////////////////
        exp.completeFinalList();
/////////////////////////////////////////////////////

        print("\n\nProdutos Finais:\n");
        for (int i=0; i < exp.getFinalProductsList().size(); i++) {
            print(exp.getFinalProductsList().get(i).getLiteralView()+"\t");
        }
        
/////////////////////////////////////////////////////
        exp.setOptimizedExpression();
/////////////////////////////////////////////////////

        print("\n\nExpressão otimizada:\n");
        print(exp.getOptimizedExpression()+"\n\n");
        
    }
}
