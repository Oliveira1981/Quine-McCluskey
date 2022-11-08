package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class Product extends Tools {

    private ArrayList<Integer> minTermsList;
    private String              literalView;
    private String               binaryView;
    private int                 decimalView;
    private int                   onesCount;
    private int             implicantBitPos;
    private int                        size;
    private boolean                hasPrime;
    private boolean             isEssential;
    private boolean                 isFinal;
    
    public Product() {
        minTermsList    = new ArrayList<>();
        literalView     =                "";
        binaryView      =            "0000";
        decimalView     =                 0;
        implicantBitPos =                -1;
        onesCount       =                 0;
        size            =                 0;
        hasPrime        =             false;
        isEssential     =              true;
    }
    
    public Product(String inputFormat, String inputExp, int size) {
        setProduct(inputFormat, inputExp, size);
    }
    
    public void setProduct(String inputFormat, String inputExp, int size) {
        this.size = size;
        
        switch(inputFormat) {
            case "Literal" -> {
                setProductFromLiteral(inputExp);
                binaryView     = literal2binary(literalView, size);
                decimalView    = binary2decimal(binaryView, size);
                minTermsList   = new ArrayList<>();
                int newDecimal = binary2decimal(binaryView, size);
                if (!minTermsList.contains(newDecimal)) {
                    minTermsList.add(newDecimal);
                }
            }
            case "Decimal" -> {
                setProductFromDecimal(Integer.parseInt(inputExp));
                decimalView = Integer.parseInt(inputExp);
                binaryView  = decimal2binary(Integer.parseInt(inputExp), size);
                literalView = binary2literal(binaryView, size);
            }
            case "Binário" -> {
                setProductFromBinary(inputExp);
                literalView    = binary2literal(binaryView, size);
                decimalView    = binary2decimal(binaryView, size);
                minTermsList   = new ArrayList<>();
                int newDecimal = binary2decimal(binaryView, size);
                if (!minTermsList.contains(newDecimal)) {
                    minTermsList.add(newDecimal);
                }
            }
            default -> {
            }
        }
        
        setOnesCount();
        implicantBitPos =    -1;
        hasPrime        = false;
        isEssential     =  true;
    }
    
    public String getLiteralView() {
        return literalView;
    }
    
    public ArrayList<Integer> getMinTermsList() {
        return minTermsList;
    }
    
    public String getBinaryView() {
        return binaryView;
    }
    
    public int getDecimalView() {
        return decimalView;
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
        
        for (int b=0; b < binaryView.length(); b++) {
            
            if (binaryView.charAt(b)=='1')
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
    
    public void addMinTerm(int newDecimal) {
        if (!minTermsList.contains(newDecimal)) {
            minTermsList.add(newDecimal);
        }
    }
    
    public void setProductFromLiteral(String litInput) {
        literalView = sortLiteralInput(litInput);
    }

    public void setProductFromBinary(String bitString) {
        binaryView = "";
        int dif    = size - bitString.length();
        
        while (dif > 0) {
            binaryView += "0";
            dif--;
        }
        
        for (int i=0; i < bitString.length(); i++) {
            binaryView += bitString.charAt(i);
        }
    }
    
    public void setProductFromDecimal(int decimal) {
        minTermsList = new ArrayList<>();
        
        if (!minTermsList.contains(decimal)) {
            minTermsList.add(decimal);
        }
    }

}
