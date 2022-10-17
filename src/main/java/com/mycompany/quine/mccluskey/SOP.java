package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class SOP {

    private String inputFormat;
    private String inputExpression;
    private String optimizedExpression;
    private ArrayList<MinTerm> minTermsTable;
    private ArrayList<MinTerm> auxMinTermsTable;
    
    public SOP(String inputFormat, String expression) {
        this.inputFormat = inputFormat;
        if (inputFormat.equals("Literal")){
            this.inputExpression = cleanUpExpression(expression);
        }
        else {
            this.inputExpression = expression;
        }
        fillMinTermsTable();
    }
    
    public SOP() {
        this.inputFormat = "literal";
        this.inputExpression = "";
        fillMinTermsTable();
    }

    public void setExpression(String inputFormat, String expression) {
        this.inputFormat = inputFormat;
        if (inputFormat.equals("Literal")){
            this.inputExpression = cleanUpExpression(expression);
        }
        else {
            this.inputExpression = expression;
        }
        fillMinTermsTable();
    }

    public String getInputExpression() {
        return inputExpression;
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
            end = inputExpression.indexOf('+', begin);
            if (end<0)
                end = inputExpression.length();
            String str = inputExpression.substring(begin, end);
            minTermsTable.add(new MinTerm(inputFormat, str));
            begin = end+1;
            if (begin>=inputExpression.length())
                break;
        }
        while (begin<inputExpression.length());
    }
    
    public void sortByOnesCount() {
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
    
    //Retorna a posição do bit variante ou -1 se não é primo implicante
    public int primeImplicantBitPosition(String minTerm1, String minTerm2, int size) {
        int count = 0;
        int pos = -1;
        for (int b=0; b<size; b++) {
            if (minTerm1.charAt(b) != minTerm2.charAt(b)) {
                count++;
                pos = b;
            }
            if (count > 1)
                return -1;
        }
        if (count == 0)
            return -2;
        return pos;
    }
    
    public String subst(String str, int pos, char c) {
        return str.substring(0, pos) + c + str.substring(pos+1);
    }
    
    public void resetHasPrime() {
        for (int i=0; i < minTermsTable.size(); i++)
            minTermsTable.get(i).setHasPrime(false);
    }
    
    //FALTA RECURSIVIDADE (ALTERNAR TABELAS AUX E NORMAL)
    //VAI TER QUE RESETAR TODOS OS hasPrime antes de começar
    public void groupPrimeImplicants() {
        boolean primesWereFound = false;
        //reset hasPrime de toda a tabela OU AQUI
        auxMinTermsTable = new ArrayList<>();
        for (int i=0; i < (minTermsTable.size()-1); i++) {
            int j = i + 1;
            while (j < minTermsTable.size()) {
                int size = Math.min(
                    minTermsTable.get(i).getSize(),
                    minTermsTable.get(j).getSize()
                );
                int bitPosition = primeImplicantBitPosition(
                    minTermsTable.get(i).getBinary(),
                    minTermsTable.get(j).getBinary(),
                    size
                );
                if (bitPosition != -1) {
                    primesWereFound = true;
                    minTermsTable.get(i).setHasPrime(true);
                    minTermsTable.get(j).setHasPrime(true);
                    String bitString = "";
                    for (int c=0; c < minTermsTable.get(i).getSize(); c++)
                        if (c == bitPosition) bitString += "_";
                        else bitString += minTermsTable.get(i).getBinary().charAt(c);
                    auxMinTermsTable.add(new MinTerm("Binária", bitString));
                    auxMinTermsTable.get(auxMinTermsTable.size()-1).getDecimal().clear();
                    for(int d=0; d < minTermsTable.get(i).getDecimal().size(); d++) {
                        auxMinTermsTable.get(auxMinTermsTable.size()-1).addDecimal(
                            minTermsTable.get(i).getDecimal().get(d)
                        );
                    }
                    for(int d=0; d < minTermsTable.get(j).getDecimal().size(); d++) {
                        auxMinTermsTable.get(auxMinTermsTable.size()-1).addDecimal(
                            minTermsTable.get(j).getDecimal().get(d)
                        );
                    }
                }
                j++;
            }
            //Após testar com todos os mintermos, este mintermo não teve primo
            //e vai inalterado para a tabela auxiliar
            if (!minTermsTable.get(i).hasPrime()) {
                auxMinTermsTable.add(minTermsTable.get(i));
            }
        }
        //Se o último mintermo da lista não teve primo,
        //vai inalterado para a tabela auxiliar
        if (!minTermsTable.get(minTermsTable.size()-1).hasPrime()) {
            auxMinTermsTable.add(minTermsTable.get(minTermsTable.size()-1));
        }
        
        if (primesWereFound) {
        //if (r>0) {
            minTermsTable = auxMinTermsTable;
            resetHasPrime();
            groupPrimeImplicants();
        }
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
    
    public String getOptimizedExpression() {
        return optimizedExpression;
    }
    
    public void print(Object obj) {
        System.out.print(obj);
    }
    
}
