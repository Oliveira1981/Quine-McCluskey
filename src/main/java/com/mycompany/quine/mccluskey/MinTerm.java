package com.mycompany.quine.mccluskey;

import java.util.ArrayList;
import java.util.BitSet;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class MinTerm {

    private       String              literal;
    private       int             decimal_OLD;
    private ArrayList<Integer>    decimal_NEW;
    private       BitSet           binary_OLD;
    private       String               binary;
    private       int               onesCount;
    private       int         implicantBitPos;
    private       int                    size;
    private ArrayList<Integer> primesList_OLD;
    private       boolean            hasPrime;
    private       boolean         isEssential;
    
    public MinTerm() {
        binary_OLD  = new BitSet(1);
        binary_OLD.         clear();
        binary          = "0000";
        decimal_OLD     =      0;
        decimal_NEW = new ArrayList<>();
        literal         =     "";
        implicantBitPos =     -1;
        onesCount       =      0;
        size            =      0;
        hasPrime        =  false;
        isEssential     =   true;
    }
    
    public MinTerm(String inputFormat, String inputExp) {
        setSize(inputFormat, inputExp);
        switch(inputFormat) {
            case "Literal" -> {
                setMinTermFromLiteral(inputExp);
                //binary_OLD = literal2binary_OLD(literal);
                binary      = literal2binary(literal);
                decimal_OLD = binary2decimal(binary);
                decimal_NEW = new ArrayList<>();
                decimal_NEW.add(binary2decimal(binary));
                
            }
            case "Decimal" -> {
                setMinTermFromDecimal(Integer.parseInt(inputExp));
                //binary_OLD = decimal2binary_OLD(Integer.parseInt(inputExp));
                binary = decimal2binary(Integer.parseInt(inputExp));
                //literal = binary2literal_OLD(binary_OLD);
                literal = binary2literal(binary);
            }
            case "Binária" -> {
                setMinTermFromBinary(inputExp);
                //decimal_OLD = binary2decimal_OLD(binary_OLD);
                decimal_NEW = new ArrayList<>();
                //decimal_NEW.add(binary2decimal_OLD(binary_OLD));
                decimal_NEW.add(binary2decimal(binary));
                //literal = binary2literal_OLD(binary_OLD);
                literal = binary2literal(binary);
            }
            default -> {
            }
        }
        setOnesCount();
        primesList_OLD = new ArrayList<>();
        primesList_OLD.add(decimal_OLD);
        implicantBitPos = -1;
        hasPrime = false;
        isEssential = true;
    }
    
    public String getLiteral() {
        return literal;
    }
    
    public int getDecimal_OLD() {
        return decimal_OLD;
    }
    
    public ArrayList<Integer> getDecimal() {
        return decimal_NEW;
    }
    
    public BitSet getBinary_OLD() {
        return binary_OLD;
    }
    
    public String getBinary() {
        return binary;
        /*
        String str = "";
        for (int b=size-1; b>=0; b--) {
            if (!binary_OLD.get(b))
                str += "0";
            if (binary_OLD.get(b))
                str += "1";
        }
        return str;
        */
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

    public ArrayList<Integer> getPrimesList_OLD() {
        return primesList_OLD;
    }

    public boolean hasPrime() {
        return hasPrime;
    }

    public boolean isEssential() {
        return isEssential;
    }
    
    public void setSize(String inputFormat, String inputExp) {
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
                size = 4;
            }
            default -> {
                size = 4;
            }
        }
    }
    
    public void setImplicantBitPos(int pos) {
        this.implicantBitPos = pos;
    }
    
    public void setOnesCount() {
        int count = 0;
        for (int b=0; b<size; b++){
            if (binary.charAt(b)=='1');
                count ++;
        }
        onesCount = count;
    }

    public void addToPrimesList(int newPrime) {
        this.primesList_OLD.add(newPrime);
    }
    
    public void setHasPrime(boolean hasPrime) {
        this.hasPrime = hasPrime;
    }

    public void setIsEssential(boolean isEssential) {
        this.isEssential = isEssential;
    }
    
    public void addDecimal (int newDecimal) {
        decimal_NEW.add(newDecimal); 
    }
    
    public void setMinTermFromLiteral(String litInput) {
        literal = sortLiteralInput(litInput);
        //literal = litInput;
    }

    public void setMinTermFromBinary(String bitString) {
        binary = bitString;
        binary_OLD = new BitSet(bitString.length());
        for (int i=0; i<size; i++) {
            int b = size-1-i;
            if (bitString.charAt(i) == '0')
                binary_OLD.clear(b);
            if (bitString.charAt(i) == '1')
                binary_OLD.set(b);
        }
    }
    
    public void setMinTermFromDecimal(int decimal) {
        this.decimal_OLD = decimal;
        decimal_NEW = new ArrayList<>();
        decimal_NEW.add(decimal);
    }
    
    public void setMinTermFromBitSet(BitSet bits) {
        binary_OLD = bits;
        setSize("Literal", "----");
        //Configura o bitString
        binary = "";
        for (int b = (size-1); b >= 0; b--) {
            //int b = size-1-i;
            if (!bits.get(b))
                binary += "0";
            if (bits.get(b))
                binary += "1";
        }
        literal = binary2literal_OLD(binary_OLD);
        decimal_OLD = binary2decimal_OLD(binary_OLD);
        decimal_NEW = new ArrayList<>();
        decimal_NEW.add(binary2decimal_OLD(binary_OLD));
        setOnesCount();
        implicantBitPos = -1;
        hasPrime = false;
        isEssential = true;
    }
    
    public String literal2binary(String lit) {
        String strBin = "";
        
        for (int c = 0; c < lit.length(); c++) {
            boolean isNegate = false;
            while(lit.charAt(c) == '!') {
                isNegate = !isNegate;
                c++;
            }
            if (Character.isAlphabetic(lit.charAt(c))){
                if(isNegate)
                    strBin += "0";
                else
                    strBin += "1";
            }
        }
        //Preenche invertido o BitSet com a strBin
        binary_OLD = new BitSet(strBin.length());
        for (int i=0; i<strBin.length(); i++) {
            int b = strBin.length()-1-i;
            if (strBin.charAt(i) == '0')
                binary_OLD.clear(b);
            if (strBin.charAt(i) == '1')
                binary_OLD.set(b);
        }
        return strBin;
    }
    
    public BitSet literal2binary_OLD(String lit) {
        String strBin = "";
        
        for (int c = 0; c < lit.length(); c++) {
            boolean isNegate = false;
            while(lit.charAt(c) == '!') {
                isNegate = !isNegate;
                c++;
            }
            if (Character.isAlphabetic(lit.charAt(c))){
                if(isNegate)
                    strBin += "0";
                else
                    strBin += "1";
            }
        }
        // Preencher invertido o BitSet com a String
        BitSet bits = new BitSet(strBin.length());
        for (int i=0; i<strBin.length(); i++) {
            int b = strBin.length()-1-i;
            if (strBin.charAt(i) == '0')
                bits.clear(b);
            if (strBin.charAt(i) == '1')
                bits.set(b);
        }
        return bits;
    }
    
    public int binary2decimal(String bits) {
        int decimalValue = 0;
        for (int i = 0; i<bits.length(); i++) {
            int exp = bits.length()-1-i;
            if(bits.charAt(i) == '1') {
                decimalValue += (int) Math.pow(2, (double) exp);
            }
        }
        return decimalValue;
    }
    
    public int binary2decimal_OLD(BitSet bits) {
        int decimalValue = 0;
        for (int i=0; i<bits.size(); i++) {
            if(bits.get(i)) {
                decimalValue += (int) Math.pow(2, (double) i);
            }
        }
        return decimalValue;
    }
    
    public String decimal2binary(int deci) {
        String str = "";
        binary_OLD = new BitSet(size);
        int b = 0;
        int curr = deci;
        do {
            int bit = curr % 2;
            if (bit == 0) {
                str += "0";
                binary_OLD.clear(b);
            }
            if (bit == 1) {
                str += "1";
                binary_OLD.set(b);
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
    
    public BitSet decimal2binary_OLD(int deci) {
        BitSet bits = new BitSet(size);
        int b = 0;
        int curr = deci;
        do {
            int bit = curr % 2;
            if (bit == 0)
                bits.clear(b);
            if (bit == 1)
                bits.set(b);
            b++;
            curr = (int) (curr / 2);
        }
        while (curr > 0);
        return bits;
    }
    
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
    
    public String binary2literal_OLD(BitSet bits) {
        String lit = "";
        int c = 0;
        for (int b=size-1; b>=0; b--) {
            if (!bits.get(b))
                lit += "!";
            lit += getAlphabetChar(c);
            c++;
            if (b > 0)
                lit += "*";
        }
        return lit;
    }
    
    public String sortLiteralInput(String input) {
        String upInput = input.toUpperCase();
        ArrayList<ArrayList<Character>> list = new ArrayList<>();
        
        int inputPos=0;
        while (inputPos < upInput.length()) {
            list.add(new ArrayList<>());
            list.get(list.size()-1).add('L'); //Reserva para o literal
            
            //Separa os "!"
            while (upInput.charAt(inputPos) == '!') {
                list.get(list.size()-1).add('!');
                inputPos++;
            }
            
            list.get(list.size()-1).set(0,upInput.charAt(inputPos)); //Guarda literal na posição reservada
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
        System.out.println(obj);
    }

}
