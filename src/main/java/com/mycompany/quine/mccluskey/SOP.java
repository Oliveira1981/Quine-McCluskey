package com.mycompany.quine.mccluskey;

import java.util.ArrayList;
import java.util.BitSet;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class SOP {

    private String inputFormat;
    private String expression;
    private String optimizedExpression;
    private ArrayList<String> minTerms_OLD;
    private ArrayList<MinTerm> minTermsTable;
    private ArrayList<MinTerm> auxMinTermsTable;
    private ArrayList<ArrayList<String>> primeImplicTable;
    
    public SOP(String inputFormat, String expression) {
        this.inputFormat = inputFormat;
        if (inputFormat.equals("Literal")){
            this.expression = cleanUpExpression(expression);
        }
        else {
            this.expression = expression;
        }
        //setMinTerms_OLD();
        fillMinTermsTable();
    }
    
    public SOP() {
        this.inputFormat = "literal";
        this.expression = "";
        //setMinTerms_OLD();
        fillMinTermsTable();
    }

    public void setExpression(String inputFormat, String expression) {
        this.inputFormat = inputFormat;
        if (inputFormat.equals("Literal")){
            this.expression = cleanUpExpression(expression);
        }
        else {
            this.expression = expression;
        }
        //setMinTerms_OLD();
        fillMinTermsTable();
    }

    public String getExpression() {
        return expression;
    }
    
    public ArrayList<String> getMinTerms_OLD(){
        return minTerms_OLD;
    }
    
    public ArrayList<MinTerm> getMinTermsTable(){
        return minTermsTable;
    }

    public ArrayList<MinTerm> getAuxMinTermsTable() {
        return auxMinTermsTable;
    }
    
    public void fillMinTermsTable() {
        minTermsTable = new ArrayList<>();
        int begin = 0;
        int end;
        do {
            end = expression.indexOf('+', begin);
            if (end<0)
                end = expression.length();
            String str = expression.substring(begin, end);
            minTermsTable.add(new MinTerm(inputFormat, str));
            begin = end+1;
            if (begin>=expression.length())
                break;
        }
        while (begin<expression.length());
    }
    
    public void setMinTerms_OLD() {
        
        int size = 1 + this.expression.split("\\+", -1).length-1;
        
        minTerms_OLD = new ArrayList<>(size);
        
        int i = 0;
        minTerms_OLD.add("");
        boolean isNegate = false;
        for (int c = 0; c < expression.length(); c++) {
            if (expression.charAt(c) == '!'){
                isNegate = !isNegate;
                c++;
            }
            else
                isNegate = false;
            if (Character.isAlphabetic(expression.charAt(c))){
                if(isNegate)
                    minTerms_OLD.set(i, minTerms_OLD.get(i) + "0");
                else
                    minTerms_OLD.set(i, minTerms_OLD.get(i) + "1");
                isNegate = false;
            }
            if (expression.charAt(c) == '+'){
                i++;
                minTerms_OLD.add("");
            }
        }
    }
    
    public void sortByOnes_NEW() {
        for(int i=1; i<minTermsTable.size(); i++) {
            int count_k = minTermsTable.get(i).getOnesCount();
            if(count_k < minTermsTable.get(i-1).getOnesCount()) {
                int j = i;
                do {
                    j--;
                    if (j<1) break;
                } while(count_k < minTermsTable.get(j-1).getOnesCount());
                minTermsTable.add(j, minTermsTable.remove(i));
            }
        }
    }
    
    public int countOnes(String minTerm) {
        return minTerm.split("1", -1).length-1;
    }
    
    public void sortByOnes_OLD() {
        for(int i=1; i<minTerms_OLD.size(); i++) {
            int count_k = countOnes(minTerms_OLD.get(i));
            if(count_k < countOnes(minTerms_OLD.get(i-1))) {
                int j = i;
                do {
                    j--;
                    if (j<1) break;
                } while(count_k < countOnes(minTerms_OLD.get(j-1)));
                minTerms_OLD.add(j, minTerms_OLD.remove(i));
            }
        }
    }
    
    //Retorna a posição do bit variante ou -1 se não é primo implicante
    //FALTA CONSIDERAR TAMBÉM OS BITS IMPLICANTES: "_"
    public int isPrimeImplicant_NEW(String minTerm1, String minTerm2, int size) {
        int count = 0;
        int pos = -1;
        //int size = minTerm1.get
        //for (int b=0; b<minTerm1.length(); b++) {
        for (int b=0; b<size; b++) {
            if (minTerm1.charAt(b) != minTerm2.charAt(b)) {
                count++;
                pos = b;
            }
            if (count >= 2)
                return -1;
        }
        return pos;
    }
    
    //Retorna a posição do bit variante ou -1 se não é primo implicante
    public int isPrimeImplicant_OLD(String min1, String min2) {
        int count = 0;
        int pos = -1;
        for (int c=0; c<min1.length(); c++) {
            if (min1.charAt(c) != min2.charAt(c)) {
                count++;
                pos = c;
            }
            if (count >= 2)
                return -1;
        }
        return pos;
    }
    
    public String subst(String str, int pos, char c) {
        return str.substring(0, pos) + c + str.substring(pos+1);
    }
    
    //FALTA RECURSIVIDADE (ALTERNAR TABELAS AUX E NORMAL)
    public void groupPrimeImplicants() {
        boolean hasPrime = false;
        auxMinTermsTable = new ArrayList<>();
        for (int i=0; i < (minTermsTable.size()-1); i++) {
            int j = i + 1;
            while (j < minTermsTable.size()) {
                int size = Math.min(
                    minTermsTable.get(i).getSize(),
                    minTermsTable.get(j).getSize()
                );
                int implicantBitPosition = isPrimeImplicant_NEW(
                    minTermsTable.get(i).getBinary(),
                    minTermsTable.get(j).getBinary(),
                    size
                );
                //minTermsTable.get(i).setImplicantBitPos(pos);
                //minTermsTable.get(j).setImplicantBitPos(pos);
                if (implicantBitPosition != -1) {
                    hasPrime = true;
                    minTermsTable.get(i).setHasPrime(true);
                    minTermsTable.get(j).setHasPrime(true);
                    int newDecimal = minTermsTable.get(j).getDecimal().get(0);
                    //minTermsTable.get(i).addDecimal(newDecimal);
                    //newDecimal = minTermsTable.get(i).getDecimal().get(0);
                    //minTermsTable.get(j).addDecimal(newDecimal);
                    String bitString = "";
                    //print("BS: "+minTermsTable.get(i).getBinary()+" ORIGINAL ########");
                    for (int c=0; c<minTermsTable.get(i).getSize(); c++)
                        if (c == implicantBitPosition) bitString += "_";
                        else bitString += minTermsTable.get(i).getBinary().charAt(c);
                    //print("BS: "+bitString+" ###");
                    //minTermsTable.get(i).setMinTermFromBinary(bitString);
                    auxMinTermsTable.add(new MinTerm("Binária", bitString));
                    auxMinTermsTable.get(auxMinTermsTable.size()-1).addDecimal(newDecimal);
                    auxMinTermsTable.get(auxMinTermsTable.size()-1).setHasPrime(true);
                    //print("RRR "+getAuxMinTermsTable().get(auxMinTermsTable.size()-1).getDecimal());
                    //int primeIndex = 
                    //    minTermsTable.get(j).getDecimal_OLD();
                    //minTermsTable.get(i).addToPrimesList(primeIndex);
                    //minTermsTable.remove(j);
                }
                else {
                    
                }
                j++;
            }
            //Após testar com todos os mintermos, este mintermo não teve primo
            //e vai sozinho para a tabela auxiliar
            if (!minTermsTable.get(i).hasPrime()) {
                auxMinTermsTable.add(minTermsTable.get(i));
            }
        }
        //Se o último mintermo da lista não teve primo,
        //vai sozinho para a tabela auxiliar
        if (!minTermsTable.get(minTermsTable.size()-1).hasPrime()) {
            auxMinTermsTable.add(minTermsTable.get(minTermsTable.size()-1));
        }
        //if (hasPrime) {
          //  groupPrimeImplicants();
        //}
    }
    
    public void setPrimeImplicTable_OLD() {
        int t = -1;
        primeImplicTable = new ArrayList<>();
        for (int i=0; i<(minTerms_OLD.size()-1); i++) {
            for (int j=(i+1); j<minTerms_OLD.size(); j++) {
                int pos = isPrimeImplicant_OLD(minTerms_OLD.get(i), minTerms_OLD.get(j));
                if (pos != -1) {
                    primeImplicTable.add(new ArrayList<>());
                    t++;
                    
                    // ÍNDICE //////
                    primeImplicTable.get(t).add(String.valueOf(i));
                    primeImplicTable.get(t).add(String.valueOf(j));
                    ////////////////
                    
                    primeImplicTable.get(t).add(
                        subst(minTerms_OLD.get(i), pos, '_')
                    );
                }
            }
        }
    }
    
    public ArrayList<ArrayList<String>> getPrimeImplicTable() {
        return primeImplicTable;
    }
    
    public char getAlphabetChar(int c) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.charAt(c);
    }
    
    public String cleanUpExpression(String in) {
        String out = "";
        for (int c=0; c < in.length(); c++) {
            if (
              Character.isAlphabetic(in.charAt(c)) ||
              in.charAt(c) == '!' ||
              in.charAt(c) == '+') {
                out += in.charAt(c);
            }
        }
        
        return out;
        //return in;
    }
    
    public void setOptimizedExpression() {
        optimizedExpression = "";
        //Temporariamente usando a aux
        for (int i=0; i < auxMinTermsTable.size(); i++) {
            optimizedExpression += auxMinTermsTable.get(i).getLiteral();
            if (i < (auxMinTermsTable.size()-1))
                optimizedExpression += " + ";
        }
    }
    
    public void setSOPfact_OLD() {
        optimizedExpression = "";
        for (int i=0; i<primeImplicTable.size(); i++) {
            String minTerm = primeImplicTable.get(i).get(2); //COM índice
            //String minTerm = primeImplicTable.get(i).get(0); //SEM índice
            if (i>0)
                optimizedExpression += " + ";
            for(int c=0; c<minTerm.length(); c++) {
                if (minTerm.charAt(c) == '0') {
                    optimizedExpression += "!" + getAlphabetChar(c);
                }
                if (minTerm.charAt(c) == '1') {
                    optimizedExpression += getAlphabetChar(c);
                }
            }
        }
    }
    
    public String getOptimizedExpression() {
        return optimizedExpression;
    }
    
    public void print (Object obj) {
        System.out.println(obj);
    }
    
}
