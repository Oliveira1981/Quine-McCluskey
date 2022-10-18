package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class SumOfProducts {

    private String inputFormat;
    private String inputExpression;
    private String optimizedExpression;
    private ArrayList<MinTerm> minTermsTable;
    private ArrayList<MinTerm> auxMinTermsTable;
    private ArrayList<ArrayList<Integer>> coveringTable;
    private int numberOfVars;
    
    public SumOfProducts(String inputFormat, String expression) {
        this.inputFormat = inputFormat;
        if (inputFormat.equals("Literal")){
            this.inputExpression = cleanUpExpression(expression);
        }
        else {
            this.inputExpression = expression;
        }
        fillMinTermsTable();
        //numberOfVars = setNumberOfVars(minTermsTable);
    }
    
    public SumOfProducts() {
        this.inputFormat = "literal";
        this.inputExpression = "";
        fillMinTermsTable();
        //numberOfVars = setNumberOfVars(minTermsTable);
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
        //numberOfVars = setNumberOfVars(minTermsTable);
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
    
    public int getNumberOfVars() {
        return numberOfVars;
    }
    
    public void fillMinTermsTable() {
        minTermsTable = new ArrayList<>();
        coveringTable = new ArrayList<>();
        numberOfVars = getNumberOfVars(inputFormat, inputExpression);
        int begin = 0;
        int end;
        do {
            end = inputExpression.indexOf('+', begin);
            if (end<0)
                end = inputExpression.length();
            String str = inputExpression.substring(begin, end);
            minTermsTable.add(new MinTerm(inputFormat, str, numberOfVars));
            
            coveringTable.add(new ArrayList<>());
            coveringTable.get(coveringTable.size()-1)
                .add(minTermsTable.get(minTermsTable.size()-1)
                    .getDecimal().get(0));
            
            begin = end+1;
            if (begin >= inputExpression.length())
                break;
        }
        while (begin < inputExpression.length());
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
        for (int b=0; b < size; b++) {
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
    
    public void mergePrimeImplicants() {
        boolean primesWereFound = false;
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
                    for (int c=0; c < minTermsTable.get(i).getBinary().length(); c++)
                        if (c == bitPosition) bitString += "_";
                        else bitString += minTermsTable.get(i).getBinary().charAt(c);
                    auxMinTermsTable.add(new MinTerm("Binária", bitString, numberOfVars));
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
            minTermsTable = auxMinTermsTable;
            resetHasPrime();
            mergePrimeImplicants();
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
    
    public int getNumberOfVars(String inputFormat, String inputExp) {
        int begin = 0;
        int end;
        int biggestSize = 0;
        int currSize;
        String vars = "";
        do {
            end = inputExp.indexOf('+', begin);
            if (end < 0)
                end = inputExp.length();
            String str = inputExp.substring(begin, end);
            
            //Determina o tamanho de cada mintermo
            switch(inputFormat) {
                case "Literal" -> {
                    //currSize = 0;
                    for (int c=0; c < str.length(); c++) {
                        if (Character.isAlphabetic(str.charAt(c))) {
                            if (isNewVar(str.charAt(c), vars)) {
                                //currSize++;
                                vars += str.charAt(c);
                            }
                        }
                    }
                    if (vars.length() > biggestSize)
                        biggestSize = vars.length();
                }
                case "Binária" -> {
                    if (str.length() > biggestSize){
                        biggestSize = str.length();
                    }
                }
                case "Decimal" -> {
                    currSize = 0;
                    int integerInput = Integer.parseInt(str);
                    do {
                        integerInput = (int) (integerInput / 2);
                        currSize++;
                    }
                    while (integerInput > 0);
                    if (currSize > biggestSize)
                        biggestSize = currSize;
                    }
                default -> {
                    biggestSize = 4;
                }
            }
            //////////////////////////////////////
            
            begin = end+1;
            if (begin >= inputExp.length())
                break;
        }
        while (begin < inputExp.length());
        return biggestSize;
    }
    
    public boolean isNewVar(char c, String str) {
        for (int i=0; i < str.length(); i++) {
            if (c == str.charAt(i))
                return false;
        }
        return true;
    }
    
    public void fillCoveringTable() {
        /*
        VAI TER QUE SER COM STRINGS
        pra cada decimal (cada entrada da coveringTable),
        varre, em cada mintermo, seus decimais
        se algum é igual, adiciona o mintermo à coveringTable
        (o decimal está no i, e o mintermo estará no j)
        */
        for (int c=0; c < coveringTable.size(); c++) {
            for (int m=0; m < minTermsTable.size(); m++) {
                int numberOfDecimals = minTermsTable.get(m).getDecimal().size();
                for (int d=0; d < numberOfDecimals; d++) {
                    int decimalFromCovering = coveringTable.get(c).get(0);
                    int decimalFromMinTerm = minTermsTable.get(m).getDecimal().get(d);
                    if (decimalFromCovering == decimalFromMinTerm) {
                        coveringTable.get(c).add(1/*minTermsTable.get(m).getLiteral()*/);
                    }
                }
            }
        }
    }
    /*
    PRA RESOLVER A TABELA DE COBERTURA
    em cada mintermo, pra cada decimal dele,
    procura decimal igual em todos os demais mintermos (ver *)
    se achou, marca o mintermo original como não essencial
    se não achou, marca como essencial
    e marca todos os decimais dele como cobertos (não sei como)
    
    * ir colocando numa lista os decimais já testados e comparar com ela
    pra não repetir a busca com um mesmo decimal
    
    */
        
    public String getOptimizedExpression() {
        return optimizedExpression;
    }
    
    public void print(Object obj) {
        System.out.print(obj);
    }
    
}
