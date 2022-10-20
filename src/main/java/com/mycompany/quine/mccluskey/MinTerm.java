package com.mycompany.quine.mccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class MinTerm extends Tools {
    
    private ArrayList<String> productsList;
    private       int               decimal;
    private       String            literal;
    private       String             binary;
    private       int                  size;
    private       boolean         isCovered;

    public MinTerm() {
        productsList = new ArrayList<>();
        decimal            =           0;
        binary             =      "0000";
        literal            =          "";
        size               =           0;
        isCovered          =       false;
    }
    
    public MinTerm(int decimal, int size) {
        setMinTerm(decimal, size);
    }
    
    public void setMinTerm(int decimal, int size) {
        this.size = size;
        this.decimal = decimal;
        this.binary = decimal2binary(decimal, size);
        this.literal = binary2literal(this.binary, size);
        isCovered = false;
    }

    public ArrayList<String> getProductsList() {
        return productsList;
    }

    public int getDecimal() {
        return decimal;
    }

    public String getLiteral() {
        return literal;
    }

    public String getBinary() {
        return binary;
    }

    public int getSize() {
        return size;
    }

    public boolean isIsCovered() {
        return isCovered;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
        //E CONVERSORES
    }

    public void setLiteral(String literal) {
        this.literal = literal;
        //E CONVERSORES
    }

    public void setBinary(String binary) {
        this.binary = binary;
        //E CONVERSORES
    }

    public void setIsCovered(boolean isCovered) {
        this.isCovered = isCovered;
    }
    
    
    
    public void addProduct(String product) {
        productsList.add(product);
    }
    
    
    
    
}
