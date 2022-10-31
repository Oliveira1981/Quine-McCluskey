package com.mycompany.quine.mccluskey;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public class QuineMcCluskey extends Tools{
        
    public static void main(String[] args) throws Exception  {
        
        PrintWriter writer = new PrintWriter("Quine-McCluskey Results.txt", "UTF-8");
        String template = "2+4+6+8+9+10+12+13+15";
        
        GUI gui = new GUI();
        gui.showDialog(template);
        
        String inputFormat;
        String allExpressions;
        
        //inputFormat = gui.getInputFormat();
        
        allExpressions  = gui.getExpression();
        if(allExpressions == null || allExpressions.length() == 0) {
            System.exit(0);
        }
        
        allExpressions = removeSpacesFromExpression(allExpressions);
        
        int begin = 0;
        int end;
        do {
            end = allExpressions.indexOf(';', begin);
            if (end < 0) {
                end = allExpressions.length();
            }
            String expression = allExpressions.substring(begin, end);
            
            inputFormat = detectInputFormat(expression);
            if (inputFormat.length() == 0 ||
                inputFormat.equals("ERRO")) {
                print("\nExpressão inconsistente.");
                begin = end + 1;
                continue;
            }
            
            print("\n===================================="
                  + "====================================");
            
            writer.println("===================================="
                         + "====================================");
            
            print("\nFormato de Entrada:"+
                "\n> "+inputFormat);
            writer.println("Formato de Entrada:"+
                "\n> "+inputFormat);
            
            if (inputFormat.equals("Hexadecimal")) {
                print("\n\nExpressão original:"+
                    "\n> "+expression);
                writer.println("\nExpressão original:"+
                    "\n> "+expression);
                expression = hexadecimal2expression(expression);
                inputFormat = "Decimal";
                print("\n\nFormato Convertido:"+
                    "\n> Decimal");
                writer.println("\nFormato Convertido:"+
                    "\n> Decimal");
            }
            
            print("\n\nExpressão:"+
                "\n> "+expression);
            writer.println("\nExpressão:"+
                "\n> "+expression);
            
/////////////////////////////////////////////////////
            SumOfProducts exp = new SumOfProducts(inputFormat, expression);
/////////////////////////////////////////////////////
    
            print("\n\nVariáveis:"+
                "\n> "+exp.getNumberOfVars()+"\n");
            writer.print("\nVariáveis:"+
                "\n> "+exp.getNumberOfVars()+"\n");
            
/////////////////////////////////////////////////////
            exp.sortByOnesCount();
/////////////////////////////////////////////////////
    
            writer.print("\nMintermos por número de 1s:");
            for(int i=0; i < exp.getProductsList().size(); i++) {
                writer.print("\n");
                writer.print(exp.getProductsList().get(i).getMinTermsList()+"\t");
                writer.print(exp.getProductsList().get(i).getBinaryView()+"\t");
                writer.print(exp.getProductsList().get(i).getLiteralView());
            }
            
            if (isDumb(exp.getMinTermsList())) {
                print ("\nDumb expression: use a WIRE!\n\n");
                writer.print ("\n\nDumb expression: use a WIRE!\n\n");
                writer.close();
                System.exit(0);
            }
            
/////////////////////////////////////////////////////
            exp.mergePrimeImplicants(10);
/////////////////////////////////////////////////////
    
            writer.print("\n\nImplicantes primos mesclados:\n");
            for(int i=0; i < exp.getProductsList().size(); i++) {
                int q = 0;
                for(; q < exp.getProductsList().get(i).getMinTermsList().size(); q++) {
                    writer.print("-"+exp.getProductsList().get(i).getMinTermsList().get(q));
                }
                if(q < 3)
                    writer.print("-\t");
                writer.print("\t");
                writer.print(exp.getProductsList().get(i).getBinaryView()+" \t");
                writer.print(exp.getProductsList().get(i).getLiteralView()+"\n");
            }
            
/////////////////////////////////////////////////////
            exp.fillMinTermsList();
/////////////////////////////////////////////////////
    
            writer.print ("\nMintermos e seus produtos (Tabela de Cobertura):");
            for (int i=0; i < exp.getMinTermsList().size(); i++) {
                writer.print("\n"+exp.getMinTermsList().get(i).getDecimalView()+" -");
                for (int p=0; p < exp.getMinTermsList().get(i).getProductsList().size(); p++) {
                    writer.print("\t\t"+exp.getMinTermsList().get(i).getProductsList().get(p));
                    if (exp.getMinTermsList().get(i).getProductsList().get(p).length() < 8)
                        writer.print("\t");
                }
            }
            
/////////////////////////////////////////////////////
            exp.essentialProductsToFinalList();
/////////////////////////////////////////////////////
    
            writer.print("\n\nProdutos Essenciais:\n> ");
            for (int i=0; i < exp.getFinalProductsList().size(); i++) {
                writer.print(exp.getFinalProductsList().get(i)+"\t");
            }
            
/////////////////////////////////////////////////////
            ArrayList<Integer> indexes = exp.getCandidateProductsIndexes();
/////////////////////////////////////////////////////
            writer.print("\n");
            for (int i=0; i < exp.getMinTermsList().size(); i++) {
                writer.print("\nMintermo "+exp.getMinTermsList().get(i).getDecimalView()+" ");
                if (exp.getMinTermsList().get(i).isIsCovered())
                    writer.print("está coberto.");
                else
                    writer.print(" - ");
            }
            
/////////////////////////////////////////////////////
            exp.permute(indexes);
/////////////////////////////////////////////////////
    
            /*for (int i=0; i<exp.getPermutations().size(); i++) {
                writer.print("\n"+exp.getPermutations().get(i));
            }*/
            
/////////////////////////////////////////////////////
            exp.completeFinalList();
/////////////////////////////////////////////////////
    
            writer.print("\n\nProdutos Finais:\n> ");
            for (int i=0; i < exp.getFinalProductsList().size(); i++) {
                writer.print(exp.getFinalProductsList().get(i)+"\t");
            }
            
/////////////////////////////////////////////////////
            exp.setOptimizedExpression();
/////////////////////////////////////////////////////
    
            print("\nExpressão otimizada:\n");
            print("> "+exp.getOptimizedExpression()+"\n\n");
            writer.print("\n\nExpressão otimizada:\n");
            writer.print("> "+exp.getOptimizedExpression()+"\n\n");
            
            begin = end + 1;
            if (begin >= allExpressions.length()) {
                break;
            }
        }
        while (begin < allExpressions.length());
        
        writer.close();
    }
}
