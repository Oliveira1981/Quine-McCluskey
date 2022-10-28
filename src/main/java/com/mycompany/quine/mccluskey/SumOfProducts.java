package com.mycompany.quine.mccluskey;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class SumOfProducts extends Tools {

    private String                         inputFormat;
    private String                     inputExpression;
    private String                 optimizedExpression;
    private ArrayList<Product>            productsList; // Linhas da coveringTable
    private ArrayList<Product>         auxProductsList;
    private ArrayList<MinTerm>            minTermsList; // Colunas da coveringTable
    private ArrayList<String>     finalProductsListStr;
    private ArrayList<ArrayList<Integer>> permutations;
    private int                           numberOfVars;
    
    public SumOfProducts(String inputFormat, String expression) {
        this.inputFormat = inputFormat;
        
        if (inputFormat.equals("Literal")) {
            this.inputExpression = cleanUpExpression(expression);
        }
        else {
            this.inputExpression = expression;
        }
        
        fillProductsList();
    }
    
    public SumOfProducts() {
        this.inputFormat     = "literal";
        this.inputExpression =        "";
        fillProductsList();
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
    
    public ArrayList<String> getFinalProductsListStr() {
        return finalProductsListStr;
    }
    
    public int getNumberOfVars() {
        return numberOfVars;
    }

    public ArrayList<ArrayList<Integer>> getPermutations() {
        return permutations;
    }
    
    public void fillProductsList() {
        productsList = new ArrayList<>();
        minTermsList = new ArrayList<>();
        permutations = new ArrayList<>();
        numberOfVars = detectNumberOfVars(inputFormat, inputExpression);
        int begin    = 0;
        int end;
        
        do {
            end = inputExpression.indexOf('+', begin);
            if (end < 0) {
                end = inputExpression.length();
            }
            String str = inputExpression.substring(begin, end);
            productsList.add(new Product(inputFormat, str, numberOfVars));
            
            minTermsList
                .add(new MinTerm(productsList
                    .get(productsList.size()-1)
                        .getMinTermsList().get(0), numberOfVars));
            
            begin = end+1;
            if (begin >= inputExpression.length())
                break;
        }
        while (begin < inputExpression.length());
    }
    
    public void sortByOnesCount() {
        for(int i=1; i < productsList.size(); i++) {
            int count_k = productsList.get(i).getOnesCount();
            
            if(count_k < productsList.get(i-1).getOnesCount()) {
                int j = i;
                do {
                    j--;
                    if (j < 1) break;
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
    
    public void mergePrimeImplicants(int limit) {
        boolean primesWereFound = false;
        auxProductsList = new ArrayList<>();
        
        for (int i=0; i < (productsList.size()-1); i++) {
            int j = i + 1;
            
            while (j < productsList.size()) {
                int size =
                    Math.min(productsList.get(i).getSize(),
                             productsList.get(j).getSize());
                int bitPosition =
                    primeImplicantBitPosition(productsList.get(i).getBinaryView(),
                                              productsList.get(j).getBinaryView(),size);
                if (bitPosition != -1) {
                    primesWereFound = true;
                    productsList.get(i).setHasPrime(true);
                    productsList.get(j).setHasPrime(true);
                    String bitString = "";
                    
                    for (int c=0; c < productsList.get(i).getBinaryView().length(); c++) {
                        if (c == bitPosition)
                            bitString += "_";
                        else
                            bitString += productsList.get(i).getBinaryView().charAt(c);
                    }
                    auxProductsList.add(new Product("Binário", bitString, numberOfVars));
                    auxProductsList.get(auxProductsList.size()-1).getMinTermsList().clear();
                    
                    for(int d=0; d < productsList.get(i).getMinTermsList().size(); d++) {
                        auxProductsList.get(auxProductsList.size()-1)
                            .addMinTerm(productsList.get(i).getMinTermsList().get(d));
                    }
                    
                    for(int d=0; d < productsList.get(j).getMinTermsList().size(); d++) {
                        auxProductsList.get(auxProductsList.size()-1)
                            .addMinTerm(productsList.get(j).getMinTermsList().get(d));
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
        
        if (primesWereFound && numberOfVars > 1 && limit > 0) {
            productsList = auxProductsList;
            resetHasPrime();
            mergePrimeImplicants(--limit);
        }
    }
    
    public void setOptimizedExpression() {
        optimizedExpression = "";
        
        for (int i=0; i < finalProductsListStr.size(); i++) {
            optimizedExpression += finalProductsListStr.get(i);
            if (i < (finalProductsListStr.size()-1))
                optimizedExpression += " + ";
        }
    }
    
    public void fillMinTermsList() {
        for (int m=0; m < minTermsList.size(); m++) {
            
            for (int p=0; p < productsList.size(); p++) {
                
                for (int d=0; d < productsList.get(p).getMinTermsList().size(); d++) {
                    int mtDecimal = minTermsList.get(m).getDecimalView();
                    int pdDecimal = productsList.get(p).getMinTermsList().get(d);
                    
                    if (mtDecimal == pdDecimal) {
                        if (!minTermsList.get(m)
                            .getProductsListString()
                            .contains(productsList.get(p).getLiteralView())) {
                            minTermsList.get(m).addProduct(productsList.get(p).getLiteralView());
                        }
                    }
                }
            }
        }
    }
    
    public void essentialProductsToFinalList() {
        //Colocar na essentialProductsList todos os
        //produtos que aparecem apenas uma vez em algum mintermo
        //finalProductsList = new ArrayList<>();
        finalProductsListStr = new ArrayList<>();
        
        for (int m=0; m < minTermsList.size(); m++) {
            
            if (minTermsList.get(m).getProductsListString().size() == 1) {
                String productString = minTermsList.get(m).getProductsListString().get(0);
                
                if (!finalProductsListStr.contains(productString)) {
                    finalProductsListStr.add(productString);
                }
            }
        }
        
        setIsCovered();
    }
    
    public void setIsCovered() {
        clearAllCovered();
        
        for (int e=0; e < finalProductsListStr.size(); e++) {
            String product = finalProductsListStr.get(e);
            
            for (int m=0; m < minTermsList.size(); m++) {
                if (minTermsList.get(m).getProductsListString().contains(product)) {
                    minTermsList.get(m).setIsCovered(true);
                }
            }
        }
    }
    
    public boolean isAllCovered() {
        for (int i=0; i < minTermsList.size(); i++) {
            
            if (!minTermsList.get(i).isIsCovered()) {
                return false;
            }
        }
        
        return true;
    }
    
    public void clearAllCovered() {
        for (int i=0; i < minTermsList.size(); i++) {
            minTermsList.get(i).setIsCovered(false);
        }
    }
    
    public void completeFinalList() {
        if (isAllCovered()) {
            return;
        }
        
        ArrayList<String> finalListBackup = (ArrayList) finalProductsListStr.clone();
        ArrayList<String> finalListCandidate = (ArrayList) finalProductsListStr.clone();
        int smaller = getCandidateProductsIndexes().size();
        int addedProducts;
        
        for (int p=0; p < permutations.size(); p++) {
            setIsCovered();
            addedProducts =
                completeFinalListCandidate(p, smaller);
            
            if (addedProducts < smaller) {
                smaller = addedProducts;
                finalListCandidate = (ArrayList) finalProductsListStr.clone();
            }
            
            if (addedProducts == 1) {
                return;
            }
            
            if (p == permutations.size()-1 &&
                smaller == getCandidateProductsIndexes().size()) {
                return;
            }
            else {
                finalProductsListStr = (ArrayList) finalListBackup.clone();
            }
            
        }
        
        finalProductsListStr = (ArrayList) finalListCandidate.clone();
    }
    
    public int completeFinalListCandidate(int candidate, int smaller) {
        int addedProductsCount = 0;
        
        for (int r=0; r < permutations.get(candidate).size(); r++) {
            int p = permutations.get(candidate).get(r);
            String productString = productsList.get(p).getLiteralView();
            
            if (!finalProductsListStr.contains(productString)) {
                finalProductsListStr.add(productString);
                addedProductsCount++;
                
                if (addedProductsCount >= smaller) {
                    return addedProductsCount;
                }
                
                for (int d=0; d < productsList.get(p).getMinTermsList().size(); d++) {
                    int decimal = productsList.get(p).getMinTermsList().get(d);
                    
                    for (int m=0; m < minTermsList.size(); m++) {
                        
                        if (decimal == minTermsList.get(m).getDecimalView()){
                            minTermsList.get(m).setIsCovered(true);
                        }
                        
                    }
                    
                }
                
                if (isAllCovered()) {
                    return addedProductsCount;
                }
            }
        }
        
        return addedProductsCount;
    }
    
    public ArrayList getCandidateProductsIndexes() {
        ArrayList<Integer> indexes = new ArrayList<>();
        
        for (int p=0; p < productsList.size(); p++) {
            String productString = productsList.get(p).getLiteralView();
            
            if (!finalProductsListStr.contains(productString)) {
                indexes.add(p);
            }
        }
        
        return indexes;
    }
    
    public void permute(ArrayList elements) {
        int n = elements.size();
        int[] indexes = new int[n];
        
        for (int i = 0; i < n; i++) {
            indexes[i] = 0;
        }
        
        permutations.add((ArrayList) elements.clone());
        int i = 0;
        
        while (i < n) {
            
            if (indexes[i] < i) {
                Collections.swap(elements, i % 2 == 0 ?  0: indexes[i], i);
                permutations.add((ArrayList) elements.clone());
                indexes[i]++;
                i = 0;
            }
            else {
                indexes[i] = 0;
                i++;
            }
        }
    }
    
    public String getOptimizedExpression() {
        return optimizedExpression;
    }

}
