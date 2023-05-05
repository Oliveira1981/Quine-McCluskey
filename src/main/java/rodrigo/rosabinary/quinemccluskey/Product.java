package rodrigo.rosabinary.quinemccluskey;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo Rosa
 */
public class Product {

    private boolean
            hasPrime,
            isEssential;
    
    private int
            decimalView,
            onesCount,
            implicantBitPos,
            size;
    
    private String
            literalView,
            binaryView;
    
    private ArrayList<Integer>
            minTermsList;
    
    public Product() {
        hasPrime        =             false;
        isEssential     =              true;
        decimalView     =                 0;
        onesCount       =                 0;
        size            =                 0;
        implicantBitPos =                -1;
        literalView     =                "";
        binaryView      =            "0000";
        minTermsList    = new ArrayList<>();
    }
    
    public Product(String inputFormat, String inputExp, String vars, int size) {
        setProduct(inputFormat, inputExp, vars, size);
    }
    
    public final void setProduct(String inputFormat, String inputExp, String vars, int size) {
        this.size = size;
        
        switch(inputFormat) {
            case "Literal" -> {
                setProductFromLiteral(inputExp);
                binaryView     = Tools.literal2binary(literalView, vars, size);
                decimalView    = Tools.binary2decimal(binaryView, size);
                minTermsList   = new ArrayList<>();
                int newDecimal = Tools.binary2decimal(binaryView, size);
                if (!minTermsList.contains(newDecimal)) {
                    minTermsList.add(newDecimal);
                }
                //numberOfLiterals = numberOfLiterals2(inputExp);
            }
            case "Decimal" -> {
                setProductFromDecimal(Integer.parseInt(inputExp));
                decimalView = Integer.parseInt(inputExp);
                binaryView  = Tools.decimal2binary(Integer.parseInt(inputExp), size);
                literalView = Tools.binary2literal(binaryView, vars, size);
                //numberOfLiterals = size;
                //RESOLVER: SE VIER DE DECIMAL TEM QUE PREENCHER VARS COM ALPHABETCHAR
            }
            case "Binário" -> {
                setProductFromBinary(inputExp);
                literalView    = Tools.binary2literal(binaryView, vars, size);
                decimalView    = Tools.binary2decimal(binaryView, size);
                minTermsList   = new ArrayList<>();
                int newDecimal = Tools.binary2decimal(binaryView, size);
                if (!minTermsList.contains(newDecimal)) {
                    minTermsList.add(newDecimal);
                }
                //numberOfLiterals = size;
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
        //literalView = sortLiteralInput(litInput);
        literalView = litInput;
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
