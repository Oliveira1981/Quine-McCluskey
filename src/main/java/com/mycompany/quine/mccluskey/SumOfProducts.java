package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class SumOfProducts extends Tools {

    private String inputFormat;
    private String inputExpression;
    private String optimizedExpression;
    private ArrayList<Product> productsList;          // Linhas da coveringTable
    private ArrayList<Product> auxProductsList;
    private ArrayList<MinTerm> minTermsList;         // Colunas da coveringTable
    private ArrayList<ArrayList<Integer>> coveringTable;//Talvez vire uma Classe
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
    
    public ArrayList<MinTerm> getMinTermsList() {
        return minTermsList;
    }
    
    public int getNumberOfVars() {
        return numberOfVars;
    }
    
    public void fillProductsList() {
        productsList = new ArrayList<>();
        //coveringTable = new ArrayList<>();
        minTermsList = new ArrayList<>();
        numberOfVars = detectNumberOfVars(inputFormat, inputExpression);
        int begin = 0;
        int end;
        do {
            end = inputExpression.indexOf('+', begin);
            if (end<0)
                end = inputExpression.length();
            String str = inputExpression.substring(begin, end);
            productsList.add(new Product(inputFormat, str, numberOfVars));
            
            minTermsList
                .add(new MinTerm(productsList
                    .get(productsList.size()-1)
                        .getDecimalsList().get(0), numberOfVars));
            
            /*
            coveringTable.add(new ArrayList<>());
            coveringTable.get(coveringTable.size()-1)
                .add(productsList.get(productsList.size()-1)
                    .getDecimalsList().get(0));
            */
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
                    auxProductsList.get(auxProductsList.size()-1).getDecimalsList().clear();
                    for(int d=0; d < productsList.get(i).getDecimalsList().size(); d++) {
                        auxProductsList.get(auxProductsList.size()-1)
                            .addDecimal(productsList.get(i).getDecimalsList().get(d));
                    }
                    for(int d=0; d < productsList.get(j).getDecimalsList().size(); d++) {
                        auxProductsList.get(auxProductsList.size()-1)
                            .addDecimal(productsList.get(j).getDecimalsList().get(d));
                    }
                    /*
                    int firstDecimal_1 = productsList.get(i).getDecimalsList().get(0);
                    int firstDecimal_2 = productsList.get(j).getDecimalsList().get(0);
                    print("\nfd: "+firstDecimal_1);
                    String product = auxProductsList.get(auxProductsList.size()-1).getLiteral();
                    for (int d=0; d < minTermsList.size(); d++) {
                        print("\nmtdecimal: "+minTermsList.get(d).getDecimal());
                        if (firstDecimal_1 == minTermsList.get(d).getDecimal() ||
                            firstDecimal_2 == minTermsList.get(d).getDecimal()) {
                            minTermsList.get(d).addProduct(product);
                        }
                    }*/
                }
                j++;
            }
            //Após testar com todos os mintermos, este mintermo não teve primo
            //e vai inalterado para a tabela auxiliar
            if (!productsList.get(i).hasPrime()) {
                auxProductsList.add(productsList.get(i));
                /*
                int firstDecimal = productsList.get(i).getDecimalsList().get(0);
                String product = productsList.get(i).getLiteral();
                for (int d=0; d < minTermsList.size(); d++) {
                    if (firstDecimal == minTermsList.get(d).getDecimal()) {
                        minTermsList.get(d).addProduct(product);
                    }
                }*/
            }
        }
        //Se o último mintermo da lista não teve primo,
        //vai inalterado para a tabela auxiliar
        if (!productsList.get(productsList.size()-1).hasPrime()) {
            auxProductsList.add(productsList.get(productsList.size()-1));
            /*
            int firstDecimal = productsList.get(productsList.size()-1).getDecimalsList().get(0);
            String product = productsList.get(productsList.size()-1).getLiteral();
            for (int d=0; d < minTermsList.size(); d++) {
                if (firstDecimal == minTermsList.get(d).getDecimal()) {
                    minTermsList.get(d).addProduct(product);
                }
            }*/
        }
        
        if (primesWereFound) {
            productsList = auxProductsList;
            resetHasPrime();
            /*for (int d=0; d < minTermsList.size(); d++) {
                minTermsList.get(d).getProductsList().clear();
            }*/
            mergePrimeImplicants();
        }
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
    
    public void fillMinTermsList() {
        for (int m=0; m < minTermsList.size(); m++) {
            for (int p=0; p<productsList.size(); p++) {
                for (int d=0; d<productsList.get(p).getDecimalsList().size(); d++) {
                    int mtDecimal = minTermsList.get(m).getDecimal();
                    int pdDecimal = productsList.get(p).getDecimalsList().get(d);
                    if (mtDecimal == pdDecimal) {
                        String product = productsList.get(p).getLiteral();
                        minTermsList.get(m).addProduct(product);
                    }
                }
            }
        }
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
                int numberOfDecimals = productsList.get(m).getDecimalsList().size();
                for (int d=0; d < numberOfDecimals; d++) {
                    int decimalFromCovering = coveringTable.get(c).get(0);
                    int decimalFromProduct = productsList.get(m).getDecimalsList().get(d);
                    if (decimalFromCovering == decimalFromProduct) {
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

}
