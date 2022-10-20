package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class Product extends Tools {

    private ArrayList<Integer> decimalsList;
    private ArrayList<MinTerm> minTermsList;
    private       String            literal;
    private       String             binary;
    private       int             onesCount;
    private       int       implicantBitPos;
    private       int                  size;
    private       boolean          hasPrime;
    private       boolean       isEssential;
    
    public Product() {
        decimalsList = new ArrayList<>();
        binary             = "0000";
        literal            =     "";
        implicantBitPos    =     -1;
        onesCount          =      0;
        size               =      0;
        hasPrime           =  false;
        isEssential        =   true;
    }
    
    public Product(String inputFormat, String inputExp, int size) {
        setProduct(inputFormat, inputExp, size);
    }
    
    public void setProduct(String inputFormat, String inputExp, int size) {
        this.size = size;
        switch(inputFormat) {
            case "Literal" -> {
                setProductFromLiteral(inputExp);
                binary = literal2binary(literal, size);
                decimalsList = new ArrayList<>();
                decimalsList.add(binary2decimal(binary, size));
            }
            case "Decimal" -> {
                setProductFromDecimal(Integer.parseInt(inputExp));
                binary = decimal2binary(Integer.parseInt(inputExp), size);
                literal = binary2literal(binary, size);
            }
            case "Binária" -> {
                setProductFromBinary(inputExp);
                decimalsList = new ArrayList<>();
                decimalsList.add(binary2decimal(binary, size));
                literal = binary2literal(binary, size);
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
    
    public ArrayList<Integer> getDecimalsList() {
        return decimalsList;
    }
    
    public ArrayList<MinTerm> getMinTermsList() {
        return minTermsList;
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
    
    public void addDecimal(int newDecimal) {
        decimalsList.add(newDecimal);
    }
    
    public void addMinTerm(MinTerm mt) {
        minTermsList.add(mt);
    }
    
    public void setProductFromLiteral(String litInput) {
        literal = sortLiteralInput(litInput);
    }

    public void setProductFromBinary(String bitString) {
        binary = "";
        int dif = size - bitString.length();
        while (dif > 0) {
            binary += "0";
            dif--;
        }
        for (int i=0; i < bitString.length(); i++) {
            binary += bitString.charAt(i);
        }
    }
    
    public void setProductFromDecimal(int decimal) {
        this.decimalsList = new ArrayList<>();
        this.decimalsList.add(decimal);
    }

}
