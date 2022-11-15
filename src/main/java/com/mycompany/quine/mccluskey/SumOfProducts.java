package com.mycompany.quine.mccluskey;

import java.io.FileNotFoundException;
//import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class SumOfProducts extends Tools {

    private String                 originalInputFormat;
    private String                         inputFormat;
    private String             originalInputExpression;
    private String                 convertedExpression;
    private String                       variablesList;
    private ArrayList<String>     originalProductsList;
    private ArrayList<Product>            productsList; // Linhas da coveringTable
    private ArrayList<Product>         auxProductsList;
    private ArrayList<MinTerm>            minTermsList; // Colunas da coveringTable
    private ArrayList<String>    essentialProductsList;
    private ArrayList<String>        finalProductsList;
    private ArrayList<ArrayList<Integer>> permutations;
    private int                           numberOfVars;
    private int                       numberOfProducts;
    private ArrayList<String>               truthTable;
    private boolean                            isError;
    private String                              result;
    private String                              report;
    
    public SumOfProducts(String expression) {
        setExpression(expression);
    }
    
    public SumOfProducts() {
        this.originalInputFormat     = "literal";
        this.inputFormat             = "literal";
        this.originalInputExpression = "";
        this.convertedExpression     = "";
        this.variablesList           = "";
        this.isError                 = false;
        this.report                  = "";
    }
    
    public boolean setExpression(String expression) {
        this.isError         = false;
        this.report          = "";
        inputFormat = detectInputFormat(expression);
        originalInputExpression = expression;
        
        if(!isValidInput(expression)) {
            return false;
        }
        
        if (inputFormat.equals("Literal")){
            this.convertedExpression = cleanUpExpression(expression);
            fillVariablesList(convertedExpression);
        }
        else {
            fillVariablesList("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        fillProductsList();
        fillTruthTable();
        
        return true;
    }
    
    public boolean isValidInput(String expression) {
        
        if (inputFormat.length() == 0  ||
            inputFormat.equals("ERRO") ||
           (inputFormat.equals("Literal") && hasDuplicate(expression))) {
            
            isError = true;
            result = "Expressão inconsistente.";
            return false;
        }
        
        if (inputFormat.equals("Hexadecimal")) {
            //report += ("\nExpressão original:\n> " + expression + "\n");
            if (expression.length() > 2) {
                for (int i=2; i < expression.length(); i++) {
                    if (Character.isAlphabetic(expression.charAt(i))) {
                        String upExp = expression.toUpperCase();
                        if (upExp.charAt(i) != 'A' &&
                            upExp.charAt(i) != 'B' &&
                            upExp.charAt(i) != 'C' &&
                            upExp.charAt(i) != 'D' &&
                            upExp.charAt(i) != 'E' &&
                            upExp.charAt(i) != 'F') {
                            isError = true;
                            result = "Expressão inconsistente.";
                            return false;
                        }
                    }
                    else {
                        if (!Character.isDigit(expression.charAt(i))) {
                            isError = true;
                            result = "Expressão inconsistente.";
                            return false;
                        }
                    }
                }
            }
            else {
                isError = true;
                result = "Expressão inconsistente.";
                return false;
            }
            convertedExpression = hexadecimal2expression(expression);
            originalInputFormat = "Hexadecimal";
            inputFormat = "Decimal";
        }
        else {
            originalInputFormat = inputFormat;
            convertedExpression = expression;
        }
        
        return true;
    }
    
    public String getOriginalInputExpression() {
        return originalInputExpression;
    }
    
    public String getConvertedExpression() {
        return convertedExpression;
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
    
    public ArrayList<String> getEssentialProductsList() {
        return essentialProductsList;
    }
    
    public ArrayList<String> getFinalProductsList() {
        return finalProductsList;
    }
    
    public int getNumberOfVars() {
        return numberOfVars;
    }
    
    public String getTruthTable() {
        if (isError) {
            return "-";
        }
        String str = "";
        for (int i=0; i < truthTable.size(); i++) {
            str += "\n" + truthTable.get(i);
        }
        str += "\n\nHexadecimal: " + expression2hexadecimal(convertedExpression) + "\n";
        
        return str;
    }
    
    public ArrayList<ArrayList<Integer>> getPermutations() {
        return permutations;
    }

    public boolean isError() {
        return isError;
    }
    
    public String getReport() {
        return report;
    }
    
    public void fillProductsList() {
        productsList = new ArrayList<>();
        minTermsList = new ArrayList<>();
        permutations = new ArrayList<>();
        numberOfVars = detectNumberOfVars(inputFormat, convertedExpression);
        //numberOfVars = 3;
        int begin    = 0;
        int end;
        do {
            end = convertedExpression.indexOf('+', begin);
            
            if (end < 0) {
                end = convertedExpression.length();
            }
            
            String str = convertedExpression.substring(begin, end);
            ArrayList<String> originalProductsList = new ArrayList<>();
            originalProductsList.add(str);
            
            //Trabalha os Don't Care (gera todas as variações)
            ArrayList<String> allStr;
            if (inputFormat.equals("Literal")) {
                allStr = (ArrayList<String>) getAllVariations(str, variablesList, numberOfVars).clone();
            }
            else {
                allStr = new ArrayList<>();
                allStr.add(str);
            }
            
            for (int a=0; a < allStr.size(); a++) {
                Product newProduct = new Product(
                    inputFormat, allStr.get(a), variablesList, numberOfVars
                );
                
                if (!productsListContains(newProduct.getLiteralView(), productsList)) {
                    productsList.add(newProduct);
                }
                
                MinTerm newMinTerm = new MinTerm(
                    productsList.get(
                        productsList.size()-1
                    ).getMinTermsList().get(0), variablesList, numberOfVars
                );
                
                if (!minTermsListContains(newMinTerm.getDecimalView(), minTermsList)) {
                    minTermsList.add(newMinTerm);
                }
            }
            
            begin = end + 1;
            if (begin >= convertedExpression.length())
                break;
        }
        while (begin < convertedExpression.length());
        
        numberOfProducts = productsList.size();
    }
    
    public void fillTruthTable() {
        truthTable = new ArrayList<>();
        String str = "";
        for (int i=0; i < numberOfVars; i++) {
            str += variablesList.charAt(i) + " ";
        }
        str += "| SAÍDA\n";
        int strSize = str.length();
        for (int i=0; i < strSize-1; i++) {
            str += "-";
        }
        truthTable.add(str);//HEADER
        int size = (int) Math.pow(2, numberOfVars);
        int m = 0;
        for (int i=1; i <= size; i++) {
            String binary = decimal2binary(i-1, numberOfVars);
            String allBits = "";
            for (int j=0; j < binary.length(); j++) {
                allBits += binary.charAt(j) + " ";
            }
            
            truthTable.add(allBits);
            str = truthTable.get(truthTable.size()-1);
            if (m < minTermsList.size()) {
                if (minTermsList.get(m).getDecimalView() == (i-1)) {
                    str += "|   1";
                    m++;
                }
                else {
                    str += "|   .";
                }
            }
            else {
                str += "|   .";
            }
            truthTable.remove(truthTable.size()-1);
            truthTable.add(str);
        }
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
                    if (!contains(bitString, auxProductsList)) {
                        auxProductsList.add(new Product("Binário", bitString, variablesList, numberOfVars));
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
    
    public void buildOptimizedExpression() {
        if (isDumb(minTermsList, numberOfVars)) {
            result = "1";
            return;
        }
        result = "";
        
        for (int i=0; i < finalProductsList.size(); i++) {
            result += finalProductsList.get(i);
            if (i < (finalProductsList.size()-1))
                result += " + ";
        }
    }
    
    public void setResult(String str) {
        this.result = str;
    }
    
    public void fillMinTermsList() {
        for (int m=0; m < minTermsList.size(); m++) {
            
            for (int p=0; p < productsList.size(); p++) {
                
                for (int d=0; d < productsList.get(p).getMinTermsList().size(); d++) {
                    int mtDecimal = minTermsList.get(m).getDecimalView();
                    int pdDecimal = productsList.get(p).getMinTermsList().get(d);
                    
                    if (mtDecimal == pdDecimal) {
                        if (!minTermsList.get(m)
                            .getProductsList()
                            .contains(productsList.get(p).getLiteralView())) {
                            minTermsList.get(m).addProduct(productsList.get(p).getLiteralView());
                        }
                    }
                }
            }
        }
        sortMinTermsList();
    }
    
    public void essentialProductsToFinalList() {
        //Colocar na essentialProductsList todos os
        //produtos que aparecem apenas uma vez em algum mintermo
        //finalProductsList = new ArrayList<>();
        essentialProductsList = new ArrayList<>();
        finalProductsList = new ArrayList<>();
        
        for (int m=0; m < minTermsList.size(); m++) {
            
            if (minTermsList.get(m).getProductsList().size() == 1) {
                String productString = minTermsList.get(m).getProductsList().get(0);
                
                if (!essentialProductsList.contains(productString)) {
                    essentialProductsList.add(productString);
                }
                
                if (!finalProductsList.contains(productString)) {
                    finalProductsList.add(productString);
                }
            }
        }
        
        setIsCovered();
    }
    
    public void setIsCovered() {
        clearAllCovered();
        
        for (int e=0; e < finalProductsList.size(); e++) {
            String product = finalProductsList.get(e);
            
            for (int m=0; m < minTermsList.size(); m++) {
                if (minTermsList.get(m).getProductsList().contains(product)) {
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
        
        ArrayList<String> finalListBackup = (ArrayList) finalProductsList.clone();
        ArrayList<String> finalListCandidate = (ArrayList) finalProductsList.clone();
        int smaller = getCandidateProductsIndexes().size();
        int addedProducts;
        
        for (int p=0; p < permutations.size(); p++) {
            setIsCovered();
            addedProducts =
                completeFinalListCandidate(p, smaller);
            
            if (addedProducts < smaller) {
                smaller = addedProducts;
                finalListCandidate = (ArrayList) finalProductsList.clone();
            }
            
            if (addedProducts == 1) {
                return;
            }
            
            if (p == permutations.size()-1 &&
                smaller == getCandidateProductsIndexes().size()) {
                return;
            }
            else {
                finalProductsList = (ArrayList) finalListBackup.clone();
            }
            
        }
        
        finalProductsList = (ArrayList) finalListCandidate.clone();
    }
    
    public int completeFinalListCandidate(int candidate, int smaller) {
        int addedProductsCount = 0;
        
        for (int r=0; r < permutations.get(candidate).size(); r++) {
            int p = permutations.get(candidate).get(r);
            String productString = productsList.get(p).getLiteralView();
            
            if (!finalProductsList.contains(productString)) {
                finalProductsList.add(productString);
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
            
            if (!finalProductsList.contains(productString)) {
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
    
    public void sortMinTermsList() {
        for(int i=1; i < minTermsList.size(); i++) {
            int count_k = minTermsList.get(i).getDecimalView();
            
            if(count_k < minTermsList.get(i-1).getDecimalView()) {
                int j = i;
                do {
                    j--;
                    if (j < 1) break;
                } while(count_k < minTermsList.get(j-1).getDecimalView());
                minTermsList.add(j, minTermsList.remove(i));
            }
        }
    }
    
    public String getResult() {
        return result;
    }
    
    public String getMinTermsFromProducts() {
        if (isError) {
            return "-";
        }
        String str = "";
        Formatter fmt = new Formatter();
        int size = 2 + numberOfVars*3;
        for (int j=0; j < size-7; j++) { //"Produto" tem 7 letras
            str += " ";
        }
        fmt.format("%-1s %-10s %-20s\n", "Produto" + str, "Binário", "Mintermos");
        for (int j=0; j < 21+size; j++) {
            fmt.format("%s", "-");
        }
        fmt.format("%s", "\n");
        for(int i=0; i < productsList.size(); i++) {
            int literalSize = productsList.get(i).getLiteralView().length();
            str = "";
            for (int j=0; j < size-literalSize; j++) {
                str += " ";
            }
            fmt.format("%-1s %-10s %-20s\n",
                    productsList.get(i).getLiteralView() + str,
                    productsList.get(i).getBinaryView(),
                    productsList.get(i).getMinTermsList()
            );
        }
        str = "\n" + fmt;
        return str;
    }
    
    public String getProductsFromMinTerms() {
        if (isError) {
            return "-";
        }
        String str;
        Formatter fmt = new Formatter();
        fmt.format("%-20s %-20s\n", "Mintermo", "Produtos");
        //Falta definir número de "-" de acordo com o tamanho dos produtos
        fmt.format("%s", "-----------------------------------------\n");
        for(int i=0; i < minTermsList.size(); i++) {
            fmt.format("%-20s %-20s\n",
                    minTermsList.get(i).getDecimalView(),
                    minTermsList.get(i).getProductsList()
            );
        }
        str = "\n" + fmt;
        return str;
    }
    
    public String getCoveringTable() {
        if (isError) {
            return "-";
        }
        String str = "";
        Formatter fmtHeader = new Formatter();
        for (int i=0; i < numberOfVars; i++) {
            str += " ";
        }
        str += "  |";
        for (int i=0; i < minTermsList.size(); i++) {
            fmtHeader.format("%4s", minTermsList.get(i).getDecimalView() + "|");
        }
        str = "\n" + str + fmtHeader;
        
        for (int i=0; i < productsList.size(); i++) {
            str += "\n";
            for (int r=0; r < 3+(numberOfVars+(minTermsList.size())*4); r++) {
                str += ".";
            }
            Formatter fmtRow = new Formatter();
            fmtRow.format("%-4s", productsList.get(i).getBinaryView() + "  |");
            for (int j=0; j < minTermsList.size(); j++) {
                if (productsList.get(i)
                    .getMinTermsList()
                        .contains(minTermsList.get(j)
                            .getDecimalView()
                    )){
                    fmtRow.format("%-4s", " X |");
                }
                else {
                    fmtRow.format("%-4s", "   |");
                }
            }
            str += "\n" + fmtRow;
        }
        str += "\n";
        for (int r=0; r < 3+(numberOfVars+(minTermsList.size())*4); r++) {
            str += ".";
        }
        return str + "\n";
    }
    
    public String expression2hexadecimal(String exp) {
        if (isError) {
            return "-";
        }
        String hexa = "0x";
        
        int i = truthTable.size()-1;
        while (i > 0) {
            String fourBits = "";
            for (int b=0; b < 4; b++) {
                if (i < 1) {
                    break;
                }
                int last = truthTable.get(i).length() - 1;
                fourBits += truthTable.get(i).charAt(last);
                i--;
            }
            int decimalDigit = binary2decimal(fourBits, 4);
            hexa += decimalDigit2hexaDigit(decimalDigit);
        }
        
        return hexa;
    }
    
    public void fillVariablesList(String expression) {
        expression = expression.toUpperCase();
        for (int i=0; i < expression.length(); i++) {
            char lit = expression.charAt(i);
            if (Character.isAlphabetic(lit)) {
                if (!variablesList.contains(lit+"")) {
                    variablesList += lit;
                }
            }
        }
        char[] charList = variablesList.toCharArray();
        Arrays.sort(charList);
        variablesList = new String(charList);
    }
    
    public String getFullReport() throws FileNotFoundException, UnsupportedEncodingException {
        //PrintWriter writer = new PrintWriter("Quine-McCluskey Results.txt", "UTF-8");
        report = "";
        report += print("\nExpressão de Entrada: \n> " + originalInputExpression + "\n"/*, writer*/);
        
        if (isError) {
            report += print("\nExpressão Inconsistente.\n"/*, writer*/);
            return report;
        }
        
        report += print("\nFormato de Entrada:\n> " + originalInputFormat + "\n"/*, writer*/);
        
        if (!inputFormat.equals(originalInputFormat)) {
            report += print("\nExpressão Convertida: \n> " + convertedExpression + "\n"/*, writer*/);
            report += print("\nFormato de Entrada Convertido:\n> " + inputFormat + "\n"/*, writer*/);
        }
        
        report += print("\nVariáveis:\n> " + numberOfVars + "\n"/*, writer*/);
        
        report += print("\nQuantidade de Literais na Entrada:\n"/*, writer*/);
        report += print("> " + numberOfLiterals(
                convertedExpression,
                numberOfVars,
                numberOfProducts) + "\n"/*, writer*/);
        
        report += print("\nSaída Hexadecimal:\n> " + expression2hexadecimal(originalInputExpression) + "\n"/*, writer*/);
        //print(expression2hexadecimal(originalInputExpression)+"\n", writer);
        
        report += print ("\nMintermos e seus Produtos:\n"/*, writer*/);
        report += print (getProductsFromMinTerms()/*, writer*/);
        
        report += print("\nProdutos e seus Mintermos:\n"/*, writer*/);
        report += print(getMinTermsFromProducts()/*, writer*/);
        
        report += print ("\nTabela de Cobertura:\n"/*, writer*/);
        report += print (getCoveringTable()/*, writer*/);
        
        report += print("\nProdutos Essenciais:\n> "/*, writer*/);
        for (int i=0; i < essentialProductsList.size(); i++) {
            report += print(essentialProductsList.get(i)+"\t"/*, writer*/);
        }
        report += print("\n"/*, writer*/);
        
        report += print("\nExpressão Otimizada:\n"/*, writer*/);
        report += print("> " + result + "\n"/*, writer*/);
        
        report += print("\nQuantidade de Literais na Saída:\n"/*, writer*/);
        report += print("> " + numberOfLiterals(result, numberOfVars, numberOfProducts) + "\n"/*, writer*/);
        
        //report += print ("\nFim dos Resultados.\n"/*, writer*/);
        report += print("\n==================================================\n"/*, writer*/);
        
        //writer.close();
        return report;
    }

}
