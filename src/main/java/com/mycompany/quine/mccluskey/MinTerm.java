package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class MinTerm {

    private ArrayList<Integer> decimal;
    private       String       literal;
    private       String        binary;
    private       int        onesCount;
    private       int  implicantBitPos;
    private       int             size;
    private       boolean     hasPrime;
    private       boolean  isEssential;
    
    public MinTerm() {
        decimal = new ArrayList<>();
        binary             = "0000";
        literal            =     "";
        implicantBitPos    =     -1;
        onesCount          =      0;
        size               =      0;
        hasPrime           =  false;
        isEssential        =   true;
    }
    
    public MinTerm(String inputFormat, String inputExp, int size) {
        setMinTerm(inputFormat, inputExp, size);
    }
    
    public void setMinTerm(String inputFormat, String inputExp, int size) {
        //setIndividualSize(inputFormat, inputExp);
        this.size = size;
        switch(inputFormat) {
            case "Literal" -> {
                setMinTermFromLiteral(inputExp);
                binary = literal2binary(literal);
                decimal = new ArrayList<>();
                decimal.add(binary2decimal(binary));
            }
            case "Decimal" -> {
                setMinTermFromDecimal(Integer.parseInt(inputExp));
                binary = decimal2binary(Integer.parseInt(inputExp));
                literal = binary2literal(binary);
            }
            case "Binária" -> {
                setMinTermFromBinary(inputExp);
                decimal = new ArrayList<>();
                decimal.add(binary2decimal(binary));
                literal = binary2literal(binary);
            }
            default -> {
            }
        }
        setOnesCount();
        implicantBitPos = -1;
        hasPrime = false;
        isEssential = true;
    }
    
    public String getLiteral() {
        return literal;
    }
    
    public ArrayList<Integer> getDecimal() {
        return decimal;
    }
    
    public String getBinary() {
        return binary;
    }
    
    public int getImplicantBitPos() {
        return implicantBitPos;
    }
    
    public int getOnesCount() {
        return onesCount;
    }
    
    public int getSize() {
        return size;
    }

    public boolean hasPrime() {
        return hasPrime;
    }

    public boolean isEssential() {
        return isEssential;
    }
    
    //Determina o número de variáveis a ser utilizado,
    //mesmo por mintermos menores
    //Ver "SOP.java, setNumberOfVars
    //public void setGlobalSize(int globalSize) {
    //    size = globalSize;
    //}
    
    //Determina o número de variáveis deste mintermo
    /*
    public void setIndividualSize(String inputFormat, String inputExp) {
        switch(inputFormat) {
            case "Literal" -> {
                int count = 0;
                for (int c=0; c<inputExp.length(); c++) {
                    if (Character.isAlphabetic(inputExp.charAt(c))){
                        count++;
                    }
                }
                size = count;
            }
            case "Binária" -> {
                size = inputExp.length();
            }
            case "Decimal" -> {
                size = 0;
                int curr = Integer.parseInt(inputExp);
                do {
                    curr = (int) (curr / 2);
                    size++;
                }
                while (curr > 0);
            }
            default -> {
                size = 4;
            }
        }
    }
    */
    public void setImplicantBitPos(int pos) {
        this.implicantBitPos = pos;
    }
    
    public void setOnesCount() {
        int count = 0;
        for (int b=0; b < binary.length(); b++){
            if (binary.charAt(b)=='1')
                count ++;
        }
        onesCount = count;
    }

    public void setHasPrime(boolean hasPrime) {
        this.hasPrime = hasPrime;
    }

    public void setIsEssential(boolean isEssential) {
        this.isEssential = isEssential;
    }
    
    public void addDecimal (int newDecimal) {
        decimal.add(newDecimal); 
    }
    
    //FALTA FECHAR COM O size, que é tmb SOP.numberOfVars
    public void setMinTermFromLiteral(String litInput) {
        literal = sortLiteralInput(litInput);
    }

    //FALTA FECHAR COM O size, que é tmb SOP.numberOfVars
    public void setMinTermFromBinary(String bitString) {
        binary = bitString;
    }
    
    public void setMinTermFromDecimal(int decimal) {
        this.decimal = new ArrayList<>();
        this.decimal.add(decimal);
    }
    
    //FALTA FECHAR COM O size, que é tmb SOP.numberOfVars
    public String literal2binary(String lit) {
        String strBin = "";
        int pos = 0;
        for (int c = 0; c < lit.length(); c++) {
            boolean isNegate = false;
            while(lit.charAt(c) == '!') {
                isNegate = !isNegate;
                c++;
            }
            if (Character.isAlphabetic(lit.charAt(c))){
                if (lit.charAt(c) != getAlphabetChar(pos))
                    strBin += "_";
                if(isNegate)
                    strBin += "0";
                else
                    strBin += "1";
                pos++;
            }
        }
        return strBin;
    }
    
    public int binary2decimal(String bits) {
        int decimalValue = 0;
        for (int i = 0; i < bits.length(); i++) {
            int exp = bits.length()-1-i;
            if(bits.charAt(i) == '1') {
                decimalValue += (int) Math.pow(2, (double) exp);
            }
        }
        return decimalValue;
    }
    
    //testar se FECHA COM O size, que é tmb SOP.numberOfVars
    public String decimal2binary(int deci) {
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
    
    //FALTA FECHAR COM O size, que é tmb SOP.numberOfVars
    public String binary2literal(String bits) {
        String lit = "";
        int c = 0;
        for (int b=0; b < bits.length(); b++) {
            if (bits.charAt(b) != '_') {
                if (lit.length() > 0)
                    if (lit.charAt(lit.length()-1) != '_')
                        lit += "*";
                if (bits.charAt(b) == '0') {
                    lit += "!";
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
    
    public void print (Object obj) {
        System.out.print(obj);
    }

}
