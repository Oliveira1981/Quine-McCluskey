package rodrigo.rosabinary.quinemccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo da Rosa
 */
public final class MinTerm {//extends Tools {
    
    private ArrayList<String> productsList;
    private String             literalView;
    private String              binaryView;
    private int                decimalView;
    private int                       size;
    private boolean              isCovered;

    public MinTerm() {
        productsList = new ArrayList<>();
        literalView  =                "";
        binaryView   =            "0000";
        decimalView  =                 0;
        size         =                 0;
        isCovered    =             false;
    }
    
    public MinTerm(int decimal, String vars, int size) {
        setMinTerm(decimal, vars, size);
    }
    
    public void setMinTerm(int decimal, String vars, int size) {
        this.size        = size;
        this.decimalView = decimal;
        this.binaryView  = Tools.decimal2binary(decimal, size);
        this.literalView = Tools.binary2literal(this.binaryView, vars, size);
        isCovered        = false;
        productsList     = new ArrayList<>();
    }
    
    public ArrayList<String> getProductsList() {
        return productsList;
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
    
    public void setMinTermFromDecimal(int decimalView, String vars) {
        this.decimalView = decimalView;
        this.binaryView  = Tools.decimal2binary(decimalView, size);
        this.literalView = Tools.binary2literal(binaryView, vars, size);
    }
    
    public void setMinTermFromLiteral(String literalView, String vars) {
        this.literalView = literalView;
        this.binaryView  = Tools.literal2binary(literalView, vars, size);
        this.decimalView = Tools.binary2decimal(binaryView, size);
    }
    
    public void setMinTermFromBinary(String binaryView, String vars) {
        this.binaryView  = binaryView;
        this.literalView = Tools.binary2literal(binaryView, vars, size);
        this.decimalView = Tools.binary2decimal(binaryView, size);
    }
    
    public void setIsCovered(boolean isCovered) {
        this.isCovered = isCovered;
    }
    
    public void addProduct(String product) {
        productsList.add(product);
    }

}
