package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public class Tools {
    
    public String literal2binary(String lit, int size) {
        String strBin = "";
        int pos = 0;
        
        for (int c = 0; c < lit.length(); c++) {
            boolean isNegate = false;
            
            while(lit.charAt(c) == '!') {
                isNegate = !isNegate;
                c++;
            }
            
            if (Character.isAlphabetic(lit.charAt(c))){
                
                if (lit.charAt(c) != getAlphabetChar(pos)) {
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
    
    public int binary2decimal(String bits, int size) {
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
    
    public String decimal2binary(int deci, int size) {
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
    
    public String binary2literal(String bits, int size) {
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
    
    public String sortLiteralInput(String input) {
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
    
    public char getAlphabetChar(int c) {
        String alphabet =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.charAt(c);
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
    
    public String cleanUpExpression(String in) {
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
    
    public static void print (Object obj) {
        System.out.print(obj);
    }

}
