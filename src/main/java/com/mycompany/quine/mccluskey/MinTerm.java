package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class MinTerm extends Tools {
    
    private ArrayList<String> productsString;
    private String               literalView;
    private String                binaryView;
    private int                  decimalView;
    private int                         size;
    private boolean                isCovered;

    public MinTerm() {
        productsString = new ArrayList<>();
        literalView  =                "";
        binaryView   =            "0000";
        decimalView  =                 0;
        size         =                 0;
        isCovered    =             false;
    }
    
    public MinTerm(int decimal, int size) {
        setMinTerm(decimal, size);
    }
    
    public void setMinTerm(int decimal, int size) {
        this.size        = size;
        this.decimalView = decimal;
        this.binaryView  = decimal2binary(decimal, size);
        this.literalView = binary2literal(this.binaryView, size);
        isCovered        = false;
        productsString     = new ArrayList<>();
    }
    
    public ArrayList<String> getProductsListString() {
        return productsString;
    }
    
    public int getDecimalView() {
        return decimalView;
    }
    
    public String getLiteralView() {
        return literalView;
    }
    
    public String getBinaryView() {
        return binaryView;
    }
    
    public int getSize() {
        return size;
    }
    
    public boolean isIsCovered() {
        return isCovered;
    }
    
    public void setMinTermFromDecimal(int decimalView) {
        this.decimalView = decimalView;
        this.binaryView  = decimal2binary(decimalView, size);
        this.literalView = binary2literal(binaryView, size);
    }
    
    public void setMinTermFromLiteral(String literalView) {
        this.literalView = literalView;
        this.binaryView  = literal2binary(literalView, size);
        this.decimalView = binary2decimal(binaryView, size);
    }
    
    public void setMinTermFromBinary(String binaryView) {
        this.binaryView  = binaryView;
        this.literalView = binary2literal(binaryView, size);
        this.decimalView = binary2decimal(binaryView, size);
    }
    
    public void setIsCovered(boolean isCovered) {
        this.isCovered = isCovered;
    }
    
    public void addProduct(String product) {
        productsString.add(product);
    }

}
