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
    private ArrayList<Product> productsList;
    private ArrayList<Product> auxProductsList;
    private ArrayList<ArrayList<Integer>> coveringTable;
    private int numberOfVars;
    
    public SumOfProducts(String inputFormat, String expression) {
        this.inputFormat = inputFormat;
        if (inputFormat.equals("Literal")) {
            this.inputExpression = cleanUpExpression(expression);
        }
        else {
            this.inputExpression = expression;
        }
        fillProductsList();
        //numberOfVars = setNumberOfVars(productsList);
    }
    
    public SumOfProducts() {
        this.inputFormat = "literal";
        this.inputExpression = "";
        fillProductsList();
        //numberOfVars = setNumberOfVars(productsList);
    }

    public void setExpression(String inputFormat, String expression) {
        this.inputFormat = inputFormat;
        if (inputFormat.equals("Literal")){
            this.inputExpression = cleanUpExpression(expression);
        }
        else {
            this.inputExpression = expression;
        }
        fillProductsList();
        //numberOfVars = setNumberOfVars(productsList);
    }

    public String getInputExpression() {
        return inputExpression;
    }
    
    public ArrayList<Product> getProductsList(){
        return productsList;
    }

    public ArrayList<Product> getAuxProductsList() {
        return auxProductsList;
    }
    
    public int getNumberOfVars() {
        return numberOfVars;
    }
    
    public void fillProductsList() {
        productsList = new ArrayList<>();
        coveringTable = new ArrayList<>();
        numberOfVars = detectNumberOfVars(inputFormat, inputExpression);
        int begin = 0;
        int end;
        do {
            end = inputExpression.indexOf('+', begin);
            if (end<0)
                end = inputExpression.length();
            String str = inputExpression.substring(begin, end);
            productsList.add(new Product(inputFormat, str, numberOfVars));
            
            coveringTable.add(new ArrayList<>());
            coveringTable.get(coveringTable.size()-1)
                .add(productsList.get(productsList.size()-1)
                    .getDecimal().get(0));
            
            begin = end+1;
            if (begin >= inputExpression.length())
                break;
        }
        while (begin < inputExpression.length());
    }
    
    public void sortByOnesCount() {
        for(int i=1; i<productsList.size(); i++) {
            int count_k = productsList.get(i).getOnesCount();
            if(count_k < productsList.get(i-1).getOnesCount()) {
                int j = i;
                do {
                    j--;
                    if (j<1) break;
                } while(count_k < productsList.get(j-1).getOnesCount());
                productsList.add(j, productsList.remove(i));
            }
        }
    }
    
    public int countOnes(String product) {
        return product.split("1", -1).length-1;
    }
    
    //Retorna a posição do bit variante ou:
    //-1 se os produtos não são primos implicantes
    //-2 se os produtos são iguais
    public int primeImplicantBitPosition(String product1, String product2, int size) {
        int count = 0;
        int pos = -1;
        for (int b=0; b < size; b++) {
            if (product1.charAt(b) != product2.charAt(b)) {
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
        for (int i=0; i < productsList.size(); i++)
            productsList.get(i).setHasPrime(false);
    }
    
    public void mergePrimeImplicants() {
        boolean primesWereFound = false;
        auxProductsList = new ArrayList<>();
        for (int i=0; i < (productsList.size()-1); i++) {
            int j = i + 1;
            while (j < productsList.size()) {
                int size = Math.min(productsList.get(i).getSize(),
                    productsList.get(j).getSize()
                );
                int bitPosition = primeImplicantBitPosition(productsList.get(i).getBinary(),
                    productsList.get(j).getBinary(),
                    size
                );
                if (bitPosition != -1) {
                    primesWereFound = true;
                    productsList.get(i).setHasPrime(true);
                    productsList.get(j).setHasPrime(true);
                    String bitString = "";
                    for (int c=0; c < productsList.get(i).getBinary().length(); c++)
                        if (c == bitPosition)
                            bitString += "_";
                        else
                            bitString += productsList.get(i).getBinary().charAt(c);
                    auxProductsList.add(new Product("Binária", bitString, numberOfVars));
                    auxProductsList.get(auxProductsList.size()-1).getDecimal().clear();
                    for(int d=0; d < productsList.get(i).getDecimal().size(); d++) {
                        auxProductsList.get(auxProductsList.size()-1)
                            .addDecimal(productsList.get(i).getDecimal().get(d));
                    }
                    for(int d=0; d < productsList.get(j).getDecimal().size(); d++) {
                        auxProductsList.get(auxProductsList.size()-1)
                            .addDecimal(productsList.get(j).getDecimal().get(d));
                    }
                }
                j++;
            }
            //Após testar com todos os mintermos, este mintermo não teve primo
            //e vai inalterado para a tabela auxiliar
            if (!productsList.get(i).hasPrime()) {
                auxProductsList.add(productsList.get(i));
            }
        }
        //Se o último mintermo da lista não teve primo,
        //vai inalterado para a tabela auxiliar
        if (!productsList.get(productsList.size()-1).hasPrime()) {
            auxProductsList.add(productsList.get(productsList.size()-1));
        }
        
        if (primesWereFound) {
            productsList = auxProductsList;
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
        for (int i=0; i < auxProductsList.size(); i++) {
            optimizedExpression += auxProductsList.get(i).getLiteral();
            if (i < (auxProductsList.size()-1))
                optimizedExpression += " + ";
        }
    }
    
    public int detectNumberOfVars(String inputFormat, String inputExp) {
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
            
            //Determina o tamanho de cada produto
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
            for (int m=0; m < productsList.size(); m++) {
                int numberOfDecimals = productsList.get(m).getDecimal().size();
                for (int d=0; d < numberOfDecimals; d++) {
                    int decimalFromCovering = coveringTable.get(c).get(0);
                    int decimalFromMinTerm = productsList.get(m).getDecimal().get(d);
                    if (decimalFromCovering == decimalFromMinTerm) {
                        coveringTable.get(c).add(1/*productsList.get(m).getLiteral()*/);
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
