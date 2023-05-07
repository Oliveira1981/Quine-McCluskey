package rodrigo.rosabinary.quinemccluskey;

import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Rodrigo Rosa
 */
public class SumOfProducts {

    private boolean
            isError,
            inspect;
    
    private int
            numberOfVars,
            numberOfProducts,
            totalNumberOfLiterals,
            progress,
            oldProgress;
    
    private String
            result,
            report,
            originalInputFormat,
            inputFormat,
            originalInputExpression,
            convertedExpression,
            variablesList;
    
    private ArrayList<Integer>
            numberOfLiteralsList;
    
    private ArrayList<String>
            essentialProductsList,
            notEssentialProductsList,
            finalProductsList,
            truthTable;
    
    private ArrayList<Product>
            productsList,     // Linhas da Tabela de Cobertura
            auxProductsList;
    
    private ArrayList<MinTerm>
            minTermsList;     // Colunas da Tabela de Cobertura
    
    private ArrayList<ArrayList<Integer>>
            combinationsList;
    
    public JProgressBar
            progressBar;
    
    public SumOfProducts(String expression, int numVars, JProgressBar progressBar) {
        setExpression(expression, numVars);
    }
    
    public SumOfProducts(JProgressBar progressBar) {
        this.isError                 = false;
        this.inspect                 = false;
        this.totalNumberOfLiterals   = 0;
        this.numberOfVars            = 0; //Auto
        this.progress                = 0;
        this.oldProgress             = 0;
        this.originalInputExpression = "";
        this.convertedExpression     = "";
        this.variablesList           = "";
        this.report                  = "";
        this.originalInputFormat     = "Literal";
        this.inputFormat             = "Literal";
        this.progressBar             = progressBar;
    }
    
    public final boolean setExpression(String expression, int selectedNumberOfVars) {
        this.isError         = false;
        this.report          = "";
        originalInputFormat  = Tools.detectInputFormat(expression);
        inputFormat          = originalInputFormat;
        
        if(!isValidInput(expression, selectedNumberOfVars)) {
            return false;
        }
        
        originalInputExpression = expression;
        if (inputFormat.equals("Hexadecimal")) {
            convertedExpression = Tools.hexadecimal2expression(expression);
            originalInputFormat = "Hexadecimal";
            inputFormat = "Decimal";
            expression = convertedExpression;
        }
        else {
            convertedExpression = expression;
        }
        
        numberOfVars = Tools.detectNumberOfVars(inputFormat, expression);
        if(!isValidNumberOfVars(selectedNumberOfVars)) {
            return false;
        }
        
        if (selectedNumberOfVars > 0) {
            numberOfVars = selectedNumberOfVars;
        }
        
        if (inputFormat.equals("Literal")){
            this.convertedExpression = Tools.cleanUpExpression(expression);
            fillVariablesList(convertedExpression);
        }
        else {
            fillVariablesList("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        fillProductsList(selectedNumberOfVars);
        //fillTruthTable();
        
        return true;
    }
    
    public boolean isValidInput(String expression, int selectedNumberOfVars) {//vai precisar testar o número de variáveis também
        if (inputFormat.length() == 0  ||
            inputFormat.equals("ERRO") ||
            expression.isBlank()       ||
           (inputFormat.equals("Literal") && Tools.hasDuplicate(expression))) {
            
            isError = true;
            result = "Expressão inconsistente.";
            return false;
        }
        
        if (inputFormat.equals("Hexadecimal")) {
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
        }
        
        return isValidNumberOfVars(selectedNumberOfVars);
    }
    
    public boolean isValidNumberOfVars(int selectedNumberOfVars) {
        if (selectedNumberOfVars > 0
         && selectedNumberOfVars < numberOfVars) {
            isError = true;
            result = "A expressão tem mais variáveis do que o número selecionado.";
            return false;
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
    
    public int getNumberOfProducts() {
        return numberOfProducts;
    }
    
    public int getNEPLSize() {
        return notEssentialProductsList.size();
    }
    
    public String getTruthTable() {
        if (isError) {
            return "-";
        }
        String str = "";
        for (int i=0; i < truthTable.size(); i++) {
            str += "\n" + truthTable.get(i);
        }
        str += "\n\nRepresentação Hexadecimal:\n> ";
        str += expression2hexadecimal(convertedExpression);
        str += "\n";
        
        return str;
    }
    
    public boolean isError() {
        return isError;
    }
    
    public boolean isInspect() {
        return inspect;
    }
    
    public String getReport() {
        return report;
    }
    
    public void fillProductsList(int selectedNumberofVars) {
        productsList = new ArrayList<>();
        minTermsList = new ArrayList<>();
        
        int begin    = 0;
        int end;
        do {
            end = convertedExpression.indexOf('+', begin);
            
            if (end < 0) {
                end = convertedExpression.length();
            }
            
            String str = convertedExpression.substring(begin, end);
            
            //Trabalha os Don't Care (gera todas as variações)
            ArrayList<String> allStr;
            if (inputFormat.equals("Literal")) {
                variablesList = Tools.completeVarsList(variablesList, numberOfVars);
                allStr = (ArrayList<String>) Tools.getAllVariations(str, variablesList, numberOfVars).clone();
            }
            else {
                allStr = new ArrayList<>();
                allStr.add(str);
            }
            
            for (int a=0; a < allStr.size(); a++) {
                Product newProduct = new Product(
                    inputFormat, allStr.get(a), variablesList, numberOfVars
                );
                if (!Tools.productsListContains(newProduct.getLiteralView(), productsList)) {
                    productsList.add(newProduct);
                }
                
                MinTerm newMinTerm = new MinTerm(
                    productsList.get(
                        productsList.size()-1
                    ).getMinTermsList().get(0), variablesList, numberOfVars
                );
                
                if (!Tools.minTermsListContains(newMinTerm.getDecimalView(), minTermsList)) {
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
            if (i < variablesList.length()) {
                str += variablesList.charAt(i) + " ";
            }
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
            String binary = Tools.decimal2binary(i-1, numberOfVars);
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
            int count = productsList.get(i).getOnesCount();
            
            if(count < productsList.get(i-1).getOnesCount()) {
                int j = i;
                do {
                    j--;
                    if (j < 1) break;
                }
                while(count < productsList.get(j-1).getOnesCount());
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
                
                //Se a diferença da contagem de 1's for diferente de 1,
                //não precisa testar, pois é impossível serem implicantes primos
                //FALTA TESTAR SE OS RESULTADOS SE MANTÊM CORRETOS!
                if ((productsList.get(j).getOnesCount()
                   - productsList.get(i).getOnesCount()) == 1) {
                    int size =
                        Math.min(productsList.get(i).getSize(),
                                 productsList.get(j).getSize());
                    int bitPosition =
                        Tools.primeImplicantBitPosition(productsList.get(i).getBinaryView(),
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
                        if (!Tools.contains(bitString, auxProductsList)) {
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
                    //j++;
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
        if (Tools.isDumb(minTermsList, numberOfVars)) {
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
    
    public void fillFinalProductsLists() {
        //Coloca na essentialProductsList todos os
        //produtos que aparecem apenas uma vez em algum mintermo
        //finalProductsList = new ArrayList<>();
        essentialProductsList = new ArrayList<>();
        notEssentialProductsList = new ArrayList<>();
        finalProductsList = new ArrayList<>();
        String productString;
        
        for (int m=0; m < minTermsList.size(); m++) {
            
            if (minTermsList.get(m).getProductsList().size() == 1) {
                productString = minTermsList.get(m).getProductsList().get(0);
                
                if (!essentialProductsList.contains(productString)) {
                    essentialProductsList.add(productString);
                }
                
                if (!finalProductsList.contains(productString)) {
                    finalProductsList.add(productString);
                }
            }
        }
        
        for (int p = 0; p < productsList.size(); p++) {
            productString = productsList.get(p).getLiteralView();
            
            if (!essentialProductsList.contains(productString)) {
                notEssentialProductsList.add(productString);
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
    
    //stack overflow user935714
    public void combinations(int len, int startPosition, String[] candidateCombination) {
        ArrayList<String> finalListBackup = (ArrayList) finalProductsList.clone();
        if (len == 0) {
            finalProductsList.addAll(Arrays.asList(candidateCombination));
            setIsCovered();
            if (isAllCovered()) {
                return;
            }
            else {
                finalProductsList = (ArrayList) finalListBackup.clone();
            }
            return;
        }
        for (int i = startPosition; i <= notEssentialProductsList.size()-len; i++) {
            candidateCombination[candidateCombination.length - len] = notEssentialProductsList.get(i);
            if (isAllCovered()) {
                return;
            }
            combinations(len-1, i+1, candidateCombination);
        }
    }
    
    // Usar quando número de produtos não essenciais > 23:
    // Mais rápido, mas, por não abordar todas as combinações,
    // pode não retornar o resultado ótimo.
    public void completeFinalList_ALT(boolean updateScreen, boolean updatePB, JTextArea report) {
        ArrayList<String> finalListOriginal = (ArrayList) finalProductsList.clone();
        int NEPLSize = notEssentialProductsList.size();
        int pbUpdateFactor = updatePB ? Math.max(1, NEPLSize/24) : -1;
        
        //NO SORTING
        if (updateScreen) {
            report.append("\n\n1. Combinações de produtos não ordenados:");
            report.append("\n  » Buscando a melhor combinação...");
            report.update(report.getGraphics());
        }
        ArrayList<String> finalList_NO_SORTING = (ArrayList) finalProductsList.clone();
        int numberOfLiterals_NO_SORTING = 0;
        int i = 1;
        while (i <= NEPLSize) {
            String[] candidateCombination = new String[i];
            combinations(i, 0, candidateCombination);
            if (isAllCovered()) {
                //return;
                finalList_NO_SORTING = (ArrayList) finalProductsList.clone();
                for (int t=0; t < finalList_NO_SORTING.size(); t++) {
                    numberOfLiterals_NO_SORTING += Tools.numberOfLiterals2(finalList_NO_SORTING.get(t));
                }
                if(updatePB) {
                    progress = 49*i/NEPLSize;
                    progressBar.setValue(progress);
                    progressBar.setString(progress+"%");
                    progressBar.update(progressBar.getGraphics());
                }
                break;
            }
            if(Math.floorMod(i, pbUpdateFactor) == 0) {
                if(updatePB) {
                    progress = 49*i/NEPLSize;
                    progressBar.setValue(progress);
                    progressBar.setString(progress+"%");
                    progressBar.update(progressBar.getGraphics());
                }
            }
            i++;
        }
        if(updatePB) {
            progress = 49;
            progressBar.setValue(progress);
            progressBar.setString(progress+"%");
            progressBar.update(progressBar.getGraphics());
        }
        
        //SORTING
        if(updateScreen) {
            report.append(" Pronto.\n\n2. Combinações de produtos pré-ordenados:");
            report.update(report.getGraphics());
        }
        finalProductsList = (ArrayList) finalListOriginal.clone();
        setIsCovered();
        if(updateScreen) {
            report.append("\n  » Ordenando produtos por número de literais...");
            report.update(report.getGraphics());
        }
        Tools.sortProductsSet(notEssentialProductsList);
        if(updateScreen) {
            report.append(" Pronto.\n  » Buscando a melhor combinação...");
            report.update(report.getGraphics());
        }
        int numberOfLiterals_SORTING = 0;
        i = 1;
        while (i <= NEPLSize) {
            String[] candidateCombination = new String[i];
            combinations(i, 0, candidateCombination);
            if (isAllCovered()) {
                //return;
                for (int t=0; t < finalProductsList.size(); t++) {
                    numberOfLiterals_SORTING += Tools.numberOfLiterals2(finalProductsList.get(t));
                }
                if(updatePB) {
                    progress = 49+ 49*i/NEPLSize;
                    progressBar.setValue(progress);
                    progressBar.setString(progress+"%");
                    progressBar.update(progressBar.getGraphics());
                }
                break;
            }
            if(updatePB) {
                if(Math.floorMod(i, pbUpdateFactor) == 0) {
                    progress = 49+ 49*i/NEPLSize;
                    progressBar.setValue(progress);
                    progressBar.setString(progress+"%");
                    progressBar.update(progressBar.getGraphics());
                }
            }
            i++;
        }
        if(updatePB) {
            progress = 98;
            progressBar.setValue(progress);
            progressBar.setString(progress+"%");
            progressBar.update(progressBar.getGraphics());
        }
        if(updateScreen) {
            report.append(" Pronto.");
            report.update(report.getGraphics());
        }
        
        if (numberOfLiterals_NO_SORTING < numberOfLiterals_SORTING) {
            finalProductsList = (ArrayList) finalList_NO_SORTING.clone();
        }
        if(updatePB) {
            progress = 100;
            progressBar.setValue(progress);
            progressBar.setString(progress+"%");
            progressBar.update(progressBar.getGraphics());
        }
    }
    
    // completeFinalList STEP 1 /////
    public void setNumberOfLiteralsList() {
        //long startTime = System.nanoTime();
        numberOfLiteralsList = new ArrayList<>();
        int thisNumberOfLiterals;
        //int lastNOL = 0;
        for(int p = 0; p < notEssentialProductsList.size(); p++) {
            thisNumberOfLiterals = Tools.numberOfLiterals2(notEssentialProductsList.get(p));
            //if((lastNOL > 0) && (thisNumberOfLiterals != lastNOL)) {
            //    print("\nLAST:"+lastNOL+"  THIS:"+thisNumberOfLiterals);
            //}
            //lastNOL = thisNumberOfLiterals;
            numberOfLiteralsList.add(thisNumberOfLiterals);
            // Valor necessário para o Counting Sort
            totalNumberOfLiterals += thisNumberOfLiterals;
        }
        //print(String.format("\nsetNumberOfLiteralsList: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
    }
    
    // completeFinalList STEP 2 /////
    public void generateAllCombinations(int n, boolean updatePB) {
        //long startTime = System.nanoTime();
        combinationsList = new ArrayList<>();
        for (int r = 1; r <= n; r++) {
            int[] combination = new int[r];
            
            // initialize with lowest lexicographic combination
            for (int i = 0; i < r; i++) {
                combination[i] = i;
            }
            
            while (combination[r - 1] < n) {
                combinationsList.add(new ArrayList<>());
                for (int i = 0; i < combination.length; i++) {
                    combinationsList.get(combinationsList.size()-1).add(combination[i]);
                }
                
                // generate next combination in lexicographic order
                int t = r - 1;
                while (t != 0 && combination[t] == n - r + t) {
                    t--;
                }
                combination[t]++;
                for (int i = t + 1; i < r; i++) {
                    combination[i] = combination[i - 1] + 1;
                }
                /*
                int last=combinationsList.size()-1;
                for(int x=0; x < combinationsList.get(last).size(); x++) {
                    print(combinationsList.get(last).get(x)+"\t");
                }
                print("\n");
                */
            }
            if(updatePB) {
                progress = 33*r/n;
                if(progress-oldProgress > 4) {
                    progressBar.setIndeterminate(false);
                    oldProgress = progress;
                    //print("\r"+progress+"\tSTEP 2");
                    progressBar.setValue(progress);
                    progressBar.setString(progress+"%");
                    progressBar.setStringPainted(true);
                    progressBar.update(progressBar.getGraphics());
                }
            }
        }
        //print(String.format("\ngenerateAllCombinations: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
        //print("\n----------------------");
    }
    
    // completeFinalList STEP 3 /////
    public void addIndexToCombinationsList(boolean updatePB) {
        //long startTime = System.nanoTime();
        int pbUpdateFactor = updatePB ? Math.max(1, combinationsList.size()/80) : -1;
        for(int c = 0; c < combinationsList.size(); c++) {
            int combinationNumberOfLiterals = 0;
            for (int i = 0; i < combinationsList.get(c).size(); i++) {
                combinationNumberOfLiterals +=
                    numberOfLiteralsList.get(combinationsList.get(c).get(i));
            }
            combinationsList.get(c).add(0, combinationNumberOfLiterals);
            if(updatePB){
                if(Math.floorMod(c, pbUpdateFactor)==0) {
                    progress = 33 + 12*c/combinationsList.size();
                    if(progress-oldProgress > 4) {
                        oldProgress = progress;
                        //print("\r"+progress+"\tSTEP 3");
                        progressBar.setValue(progress);
                        progressBar.setString(progress+"%");
                        progressBar.setStringPainted(true);
                        progressBar.update(progressBar.getGraphics());
                    }
                }
            }
        }
        //print(String.format("\naddIndexToCombinationsList: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
        //print("\n----------------------");
    }
    
    // completeFinalList STEP 4 /////
    // Counting Sort, by Rajat Mishra, geeksforgeeks.org
    public void sortCombinationsList(boolean updatePB) {
        //long startTime;// = System.nanoTime();
        
        int clSize = combinationsList.size();
        int pbUpdateFactor = updatePB ? Math.max(1, combinationsList.size()/80) : -1;
        
        // The output objects array that will have sorted
        ArrayList<ArrayList<Integer>> output = new ArrayList<>();
        
        // Create a count array to store count of individual
        // objects and initialize count array as 0
        int count[] = new int[totalNumberOfLiterals+1];
        //startTime = System.nanoTime();
        for (int i = 0; i < totalNumberOfLiterals+1; ++i) {
            count[i] = 0;
            if(updatePB){
                progress = 45 + 5*i/(totalNumberOfLiterals+1);
                if(progress-oldProgress > 4) {
                    oldProgress = progress;
                    //print("\r"+progress+"\tSTEP 4a");
                    progressBar.setValue(progress);
                    progressBar.setString(progress+"%");
                    progressBar.setStringPainted(true);
                    progressBar.update(progressBar.getGraphics());
                }
            }
        }
        //print(String.format("\nSTEP 4a: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
        //print("\n----------------------");
        // store count of each object
        //startTime = System.nanoTime();
        for (int i = 0; i < clSize; ++i) {
            ++count[combinationsList.get(i).get(0)];
            if(updatePB){
                if(Math.floorMod(i, pbUpdateFactor)==0) {
                    progress = 50 + 8*i/clSize;
                    if(progress-oldProgress > 4) {
                        oldProgress = progress;
                        //print("\r"+progress+"\tSTEP 4b");
                        progressBar.setValue(progress);
                        progressBar.setString(progress+"%");
                        progressBar.setStringPainted(true);
                        progressBar.update(progressBar.getGraphics());
                    }
                }
            }
        }
        //print(String.format("\nSTEP 4b: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
        //print("\n----------------------");
        
        // Change count[i] so that count[i] now contains
        // actual position of this object in output array
        //startTime = System.nanoTime();
        for (int i = 1; i <= totalNumberOfLiterals; ++i) {
            count[i] += count[i - 1];
            if(updatePB){
                progress = 58 + 6*i/totalNumberOfLiterals;
                if(progress-oldProgress > 4) {
                    oldProgress = progress;
                    //print("\r"+progress+"\tSTEP 4c");
                    progressBar.setValue(progress);
                    progressBar.setString(progress+"%");
                    progressBar.setStringPainted(true);
                    progressBar.update(progressBar.getGraphics());
                }
            }
        }
        //print(String.format("\nSTEP 4c: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
        //print("\n----------------------");
        
        //startTime = System.nanoTime();
        int outputSize = (int) Math.pow(2, notEssentialProductsList.size())-1;
        for (int i = 0; i < outputSize; ++i) {
            output.add(new ArrayList<>());
            if(updatePB){
                if(Math.floorMod(i, pbUpdateFactor)==0) {
                    progress = 64 + 6*i/outputSize;
                    if(progress-oldProgress > 4) {
                        oldProgress = progress;
                        //print("\r"+progress+"\tSTEP 4d");
                        progressBar.setValue(progress);
                        progressBar.setString(progress+"%");
                        progressBar.setStringPainted(true);
                        progressBar.update(progressBar.getGraphics());
                    }
                }
            }
        }
        //print(String.format("\nSTEP 4d: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
        //print("\n----------------------");
        
        // Build the output object array
        // To make it stable we are operating in
        // reverse order.
        //startTime = System.nanoTime();
        for (int i = clSize - 1; i >= 0; i--) {
            int countIndex = combinationsList.get(i).get(0);
            int outputIndex = count[countIndex] - 1;
            output.set(outputIndex, combinationsList.get(i));
            --count[combinationsList.get(i).get(0)];
            if(updatePB){
                if(Math.floorMod(i, pbUpdateFactor)==0) {
                    progress = 70 + 8*(clSize-i)/clSize;
                    if(progress-oldProgress > 4) {
                        oldProgress = progress;
                        //print("\r"+progress+"\tSTEP 4e");
                        progressBar.setValue(progress);
                        progressBar.setString(progress+"%");
                        progressBar.setStringPainted(true);
                        progressBar.update(progressBar.getGraphics());
                    }
                }
            }
        }
        //print(String.format("\nSTEP 4e: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
        //print("\n----------------------");
        
        // Copy the output array to arr, so that arr now
        // contains sorted objects
        combinationsList = (ArrayList) output.clone();
        //print(String.format("\nsortCombinationsList: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
    }
    
    // completeFinalList STEP 5 /////
    public void testCombinations(boolean updatePB) {
        //long startTime = System.nanoTime();
        int pbUpdateFactor = updatePB ? Math.max(1, combinationsList.size()/80) : -1;
        ArrayList<String> finalListInitial = (ArrayList) finalProductsList.clone();
        for (int c = 0; c < combinationsList.size(); c++) {
            for (int i = 1; i < combinationsList.get(c).size(); i++) {
                finalProductsList.add(
                    notEssentialProductsList.get(combinationsList.get(c).get(i))
                );
            }
            //print("\nComb "+c+"\nFinal Products List: "+finalProductsList+"\n");
            setIsCovered();
            if (isAllCovered()) {
                if(updatePB) {
                    progress = 100;
                    //print("\r"+progress+"\tSTEP 5");
                    progressBar.setValue(progress);
                    progressBar.setString(progress+"%");
                    progressBar.setStringPainted(true);
                    progressBar.update(progressBar.getGraphics());
                }
                //print(String.format("\ntestCombinationsList: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
                return;
            }
            finalProductsList = (ArrayList) finalListInitial.clone();
            setIsCovered();
            if(updatePB) {
                if(Math.floorMod(c, pbUpdateFactor)==0) {
                    progress = 78 + 22*c/combinationsList.size();
                    if(progress-oldProgress > 4) {
                        oldProgress = progress;
                        //print("\r"+progress+"\tSTEP 5");
                        progressBar.setValue(progress);
                        progressBar.setString(progress+"%");
                        progressBar.setStringPainted(true);
                        progressBar.update(progressBar.getGraphics());
                    }
                }
            }
        }
        //print(String.format("\nSTEP 5: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
        //print("\n----------------------");
    }
    
    public void completeFinalList(boolean updateScreen, boolean updatePB, JTextArea report) {
        progress = 0;
        if (updatePB) {
            progressBar.setValue(progress);
        }
        if (isAllCovered()) {
            if (updatePB) {
                progressBar.setValue(100);
                progressBar.setString(100 + "%");
                progressBar.setStringPainted(true);
            }
            return;
        }
        if (notEssentialProductsList.size() > 23) {
            // Geraria um número muito grande de combinações
            inspect = true;
            if (updateScreen) {
                report.append("\n\nNúmero muito grande de combinações!"
                            + "\n\nMÉTODO ALTERNATIVO:");
                report.update(report.getGraphics());
            }
            completeFinalList_ALT(updateScreen, updatePB, report);
            return;
        }

        if (updateScreen) {
            report.append("\n\n » Construindo tabela de cobertura...");
            report.update(report.getGraphics());
        }
        // STEP 1 /////
        if (updateScreen) {
            //187, »
            report.append(" Pronto.\n\n » Listando números de literais...");
            report.update(report.getGraphics());
        }
        setNumberOfLiteralsList();

        // STEP 2 /////
        if (updateScreen) {
            report.append(" Pronto.\n\n » Gernado combinações de produtos...");
            report.update(report.getGraphics());
        }
        generateAllCombinations(notEssentialProductsList.size(), updatePB);

        // STEP 3 /////
        if (updateScreen) {
            report.append(" Pronto.\n\n » Adicionando índice às combinações...");
            report.update(report.getGraphics());
        }
        addIndexToCombinationsList(updatePB);

        // STEP 4 /////
        if (updateScreen) {
            report.append(" Pronto.\n\n » Ordenando combinações...");
            report.update(report.getGraphics());
        }
        sortCombinationsList(updatePB);

        // STEP 5 /////
        if (updateScreen) {
            report.append(" Pronto.\n\n » Buscando a melhor combinação...");
            report.update(report.getGraphics());
        }
        testCombinations(updatePB);
    }
    
    public void sortMinTermsList() {
        for(int i=1; i < minTermsList.size(); i++) {
            int count = minTermsList.get(i).getDecimalView();
            
            if(count < minTermsList.get(i-1).getDecimalView()) {
                int j = i;
                do {
                    j--;
                    if (j < 1) break;
                } while(count < minTermsList.get(j-1).getDecimalView());
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
    
    public String getCoverageTable() {
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
                    if (minTermsList.get(j).getProductsList().size() == 1){
                        //Implicante primo essencial
                        fmtRow.format("%-4s", " " + (char) 1028 + " |");
                    }
                    else {
                        //Implicante primo comum
                        fmtRow.format("%-4s", " " + (char) 1161 + " |");
                    }
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
            int decimalDigit = Tools.binary2decimal(fourBits, 4);
            hexa += Tools.decimalDigit2hexaDigit(decimalDigit);
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
    
    public String getBasicReport() throws FileNotFoundException, UnsupportedEncodingException {
        report = "";
        report += ("\nEXPRESSÃO DE ENTRADA: \n> " + originalInputExpression + "\n");
        
        if (isError) {
            report += ("\nEXPRESSÃO INCONSISTENTE.\n");
            return report;
        }
        
        report += ("> " + originalInputFormat + "\n");
        
        if (!inputFormat.equals(originalInputFormat)) {
            report += ("\nEXPRESSÃO CONVERTIDA: \n> " + convertedExpression + "\n");
            report += ("> " + inputFormat + "\n");
        }
        
        report += ("> " + numberOfVars + " variáveis\n");
        
        report += ("> " + Tools.numberOfLiterals(
                convertedExpression,
                numberOfVars,
                numberOfProducts) + " literais\n");
        
        //report += ("\nPRODUTOS ESSENCIAIS:\n> ");
        //for (int i=0; i < essentialProductsList.size(); i++) {
        //    report += (essentialProductsList.get(i)+"\t");
        //}
        //report += ("\n");
        
        report += ("\nEXPRESSÃO OTIMIZADA:\n");
        report += ("> " + result + "\n");
            
        report += ("> " + Tools.numberOfLiterals(
                result,
                numberOfVars,
                numberOfProducts) + " literais\n");
        
        report += ("\n==================================================\n");
        
        return report;
    }
    
    public void print(Object obj) {
        System.out.print(obj);
    }
    
    public Object printr(Object obj) {
        System.out.print(obj);
        return obj;
    }
    
    public Object print(Object obj, PrintWriter w) {
        w.print(obj);
        return obj;
    }

    public static void printArray(ArrayList array) {
        System.out.println();
        for (int i=0; i < array.size(); i++) {
            System.out.println(array.get(i));
        }
    }
    
    public static void printDoubleArray(ArrayList<ArrayList<Integer>> array) {
        for(int i = 0; i < array.size(); i++) {
            System.out.print("\n");
            for(int j = 0; j < array.get(i).size(); j++) {
                if (!array.get(i).isEmpty()) {
                    System.out.print(array.get(i).get(j) + "\t");
                }
            }
        }
    }
    
}
