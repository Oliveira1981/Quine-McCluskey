package com.mycompany.quine.mccluskey;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Rodrigo da Rosa
 */
public class Tools {
    
    public static String literal2binary(String lit, int size) {
        String strBin = "";
        int pos = 0;
        
        for (int c = 0; c < lit.length(); c++) {
            boolean isNegate = false;
            
            while(lit.charAt(c) == '!') {
                isNegate = !isNegate;
                c++;
            }
            
            if (Character.isAlphabetic(lit.charAt(c))){
                
                while (lit.charAt(c) != getAlphabetChar(pos)) {
                    strBin += "_";
                    pos++;
                }
                
                if(isNegate)
                    strBin += "0";
                else
                    strBin += "1";
                
                pos++;
            }
        }
        
        while (strBin.length() < size) {
            strBin += "_";
        }
        
        return strBin;
    }
    
    public static int binary2decimal(String bits, int size) {
        int decimalValue = 0;
        
        while (bits.length() < size) {
            bits = "0" + bits;
        }
        
        for (int i = 0; i < bits.length(); i++) {
            int exp = bits.length() - 1 - i;
            
            if(bits.charAt(i) == '1') {
                decimalValue += (int) Math.pow(2, (double) exp);
            }
        }
        
        return decimalValue;
    }
    
    public static int hexa2decimal(String hexa) {
        //String hexa = hexaIn.substring(2);
        hexa = hexa.toUpperCase();
        int decimalValue = 0;
        
        for (int i = 0; i < hexa.length(); i++) {
            int expo = hexa.length() - 1 - i;
            
            char hexaChar = hexa.charAt(i);
            int hexaDigit;
            switch (hexaChar) {
                case 'A' -> {
                    hexaDigit = 10;
                }
                case 'B' -> {
                    hexaDigit = 11;
                }
                case 'C' -> {
                    hexaDigit = 12;
                }
                case 'D' -> {
                    hexaDigit = 13;
                }
                case 'E' -> {
                    hexaDigit = 14;
                }
                case 'F' -> {
                    hexaDigit = 15;
                }
                default -> {
                    hexaDigit = Integer.parseInt(Character.toString(hexaChar));
                }
            }
            decimalValue += hexaDigit * (int) Math.pow(16, (double) expo);
        }
        return decimalValue;
    }
    
    public static String decimal2binary(int deci, int size) {
        String str = "";
        int b = 0;
        int curr = deci;
        
        do {
            int bit = curr % 2;
            if (bit == 0) {
                str += "0";
            }
            if (bit == 1) {
                str += "1";
            }
            b++;
            curr = (int) (curr / 2);
        }
        while (curr > 0);
        
        while (str.length() < size)
            str += "0";
        
        StringBuilder reversedStr = new StringBuilder();
        reversedStr.append(str);
        reversedStr.reverse();
        return reversedStr.toString();
    }
    
    public static String binary2literal(String bits, int size) {
        while (bits.length() < size) {
            bits = "0" + bits;
        }
        
        String lit = "";
        int c = 0;
        
        for (int b=0; b < bits.length(); b++) {
            
            if (bits.charAt(b) != '_') {
                
                if (lit.length() > 0) {
                    if (lit.charAt(lit.length()-1) != '_')
                        lit += "*";
                }
                
                if (bits.charAt(b) == '0') {
                    lit += "!";
                }
                else {
                    lit += " ";
                }
                
                lit += getAlphabetChar(c);
            }
            
            c++;
        }
        
        return lit;
    }
    
    public static String sortLiteralInput(String input) {
        String upInput = input.toUpperCase();
        ArrayList<ArrayList<Character>> list = new ArrayList<>();
        
        int inputPos=0;
        
        while (inputPos < upInput.length()) {
            list.add(new ArrayList<>());
            //Reserva para o literal:
            list.get(list.size()-1).add('L');
            
            //Separa os "!"
            while (upInput.charAt(inputPos) == '!') {
                list.get(list.size()-1).add('!');
                inputPos++;
            }
            
            //Guarda o literal na posição reservada:
            list.get(list.size()-1).set(0,upInput.charAt(inputPos));
            inputPos++;
            
            if (list.size() > 1) {
                int lastPos = list.size()-1;
                
                if (list.get(lastPos).get(0) < list.get(lastPos-1).get(0)) {
                    int j = lastPos;
                    
                    do {
                        j--;
                        if (j < 1) break;
                    } while (list.get(lastPos).get(0) < list.get(j-1).get(0));
                    
                    list.add(j, list.remove(lastPos));
                }
            }
        }
        String str = "";
        
        for (int c=0; c < list.size(); c++) {
            
            if (list.get(c).size() > 1) {
                
                //Insere os "!"
                for (int n=1; n < list.get(c).size(); n++) {
                    str += list.get(c).get(n);
                }
                
            }
            
            str += list.get(c).get(0);
        }
        
        return str;
    }
    
    public static char getAlphabetChar(int c) {
        String alphabet =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.charAt(c);
    }
    
    //Retorna a posição do bit variante ou:
    //-1 se os produtos não são primos implicantes
    //-2 se os produtos são iguais
    public static int primeImplicantBitPosition(String product1, String product2, int size) {
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
    
    public static String subst(String str, int pos, char c) {
        return str.substring(0, pos) + c + str.substring(pos+1);
    }
    
    public static String cleanUpExpression(String in) {
        String out = "";
        
        for (int c=0; c < in.length(); c++) {
            
            if (Character.isAlphabetic(in.charAt(c)) ||
                in.charAt(c) == '!'                  ||
                in.charAt(c) == '+') {
                out += in.charAt(c);
            }
            
        }
        
        return out;
    }
    
    public static String removeSpacesFromExpression(String in) {
        String out = "";
        
        for (int c=0; c < in.length(); c++) {
            
            if (!(in.charAt(c) == ' ')) {
                out += in.charAt(c);
            }
        }
        
        return out;
    }
    
    public static int detectNumberOfVars(String inputFormat, String inputExp) {
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
                case "Binário" -> {
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
    
    public static boolean isNewVar(char c, String str) {
        char ch = Character.toUpperCase(c);
        String st = str.toUpperCase();
        
        for (int i=0; i < st.length(); i++) {
            
            if (ch == st.charAt(i))
                return false;
            
        }
        
        return true;
    }
    
    public static void print(Object obj) {
        System.out.print(obj);
    }
    
    public static Object print(Object obj, PrintWriter w) {
        //System.out.print(obj);
        w.print(obj);
        return obj;
    }
    
    public static void printarr(ArrayList array) {
        System.out.println();
        
        for (int i=0; i < array.size(); i++) {
            System.out.println (array.get(i));
        }
    }
    
    public static String detectInputFormat(String inputExp) {
        String in = inputExp.toUpperCase();
        String detectedFormat = "";
        
        for (int c=0; c < in.length(); c++) {
            
            if (in.charAt(c) != '!' &&
                in.charAt(c) != '+' &&
                in.charAt(c) != '*' &&
                in.charAt(c) != '_' &&
                in.charAt(c) != ' ') {
                
                if (Character.isAlphabetic(in.charAt(c))) {
                    
                    if (in.charAt(c) == 'X' &&
                       detectedFormat.equals("Binário")) {
                        return "Hexadecimal";
                    }
                    
                    if (detectedFormat.length() == 0 ||
                        detectedFormat.equals("Literal")) {
                        detectedFormat = "Literal";
                    }
                    else {
                        return "ERRO";
                    }
                }
                
                if (Character.isDigit(in.charAt(c))) {
                    
                    if ((Integer.parseInt(in.charAt(c)+"")) > 1) {
                        
                        if (detectedFormat.length() == 0 ||
                            detectedFormat.equals("Binário") ||
                            detectedFormat.equals("Decimal")) {
                            detectedFormat = "Decimal";
                        }
                        else {
                            return "ERRO";
                        }
                    }
                    else {
                        
                        if (detectedFormat.length() == 0 ||
                            detectedFormat.equals("Binário")) {
                            detectedFormat = "Binário";
                        }
                        else {
                            
                            if (detectedFormat.equals("Decimal")) {
                                detectedFormat = "Decimal";
                            }
                            else {
                                return "ERRO";
                            }
                        }
                    }
                }
            }
        }
        
        return detectedFormat;
    }
    
    public static String hexadecimal2expression(String hexa) {
        hexa = hexa.substring(2);
        String expression = "";
        for (int x=hexa.length()-1; x >= 0; x--) {
            int mult = hexa.length() - 1 - x;
            String hexaByte = Character.toString(hexa.charAt(x));
            int decimal = hexa2decimal(hexaByte);
            int size = 4;
            String binary = decimal2binary(decimal, 4);
            
            for (int i=0; i < size; i++) {
                int n = size - i - 1;
                if (binary.charAt(n) == '1') {
                    if (expression.length() > 0) {
                        expression += "+";
                    }
                    expression += Integer.toString(i + mult*4);
                }
            }
        }
        return expression;
    }
    
    public String expression2hexadecimal(String exp) {
        String hexa = "0x";
        
        
        return hexa;
    }
    
    public static boolean isDumb(ArrayList<MinTerm> mt, int numberOfVars) {
        int range = (int) Math.pow(2, numberOfVars);
        if (range != mt.size()) {
            return false;
        }
        for (int i=0; i < mt.size(); i++) {
            if (i != mt.get(i).getDecimalView()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean hasDuplicate(String str) {
        int begin = 0;
        int end;
        do {
            end = str.indexOf('+', begin);
            if (end < 0) {
                end = str.length();
            }
            String product = str.substring(begin, end);
            for (int i=0; i < product.length(); i++) {
                for (int j=i+1; j < product.length(); j++) {
                    if (Character.isAlphabetic(product.charAt(i))) {
                        if (product.charAt(i) == product.charAt(j)) {
                            return true;
                        }
                    }
                }
            }
            begin = end + 1;
            if (begin >= str.length()) {
                break;
            }
        }
        while (begin < str.length());
        
        return false;
    }
    
    public static boolean contains(String bitString, ArrayList<Product> productsList) {
        
        for (int i=0; i < productsList.size(); i++) {
            if (productsList.get(i).getBinaryView().equals(bitString)) {
                return true;
            }
        }
        return false;
    }
    
    public String generateRandomExpression(int numberOfProducts, int numberOfVars) {
        String exp = "";
        ArrayList<Integer> products = new ArrayList<>();
        Random random = new Random();
        int max = (int) Math.pow(2, numberOfVars);
        for (int i=0; i < numberOfProducts; i++) {
            if (i > 0) {
                exp += "+";
            }
            int p;
            do {
                p = random.nextInt(max);
            }
            while (products.contains(p) && (products.size() < max));
            products.add(p);
            exp += String.valueOf(p);
        }
        return exp;
    }
    
    public static int numberOfLiterals(String exp, int numberOfVars, int numberOfProducts) {
        if (exp.equals("1")) {
            return 0;
        }
        if (detectInputFormat(exp).equals("Literal")) {
            int count = 0;
            for (int i=0; i < exp.length(); i++) {
                if (Character.isAlphabetic(exp.charAt(i))) {
                    count++;
                }
            }
            return count;
        }
        
        return numberOfVars * numberOfProducts;
    }

}
