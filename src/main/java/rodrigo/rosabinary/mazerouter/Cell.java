package rodrigo.rosabinary.mazerouter;

/**
 *
 * @author Rodrigo Rosa
 */
public class Cell {
    
    private boolean
            isFree,
            isExpanded,
            isExpanding,
            isSource,
            isTarget,
            isPath;
    
    private int
            weight;
    
    private char
            c;
    
    public Cell(char c) {
        this.isFree      =  true;
        this.isExpanded  = false;
        this.isExpanding = false;
        this.isSource    = false;
        this.isTarget    = false;
        this.isPath      = false;
        this.weight      =     0;
        this.c           =     c;
    }
    
    public void setChar(char c) {
        this.c = c;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public void setFree() {
        this.isFree = true;
    }
    
    public void setBlocked() {
        this.isFree = false;
    }
    
    public void setIsExpanding(boolean isExpanding) {
        this.isExpanding = isExpanding;
    }
    
    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
        //if (!this.isTarget) {
        //    this.c = 'o';
        //}
    }
    
    public void setSource(boolean isSource) {
        this.isSource = isSource;
    }
    
    public void setTarget(boolean isTarget) {
        this.isTarget = isTarget;
    }
    
    public void setPath(boolean isPath) {
        this.isPath = isPath;
    }
    
    public char getChar() {
        return c;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public boolean isFree() {
        return isFree;
    }
    
    public boolean isBlocked() {
        return !isFree;
    }
    
    public boolean isExpanding() {
        return isExpanding;
    }
    
    public boolean isExpanded() {
        return isExpanded;
    }
    
    public boolean isSource() {
        return isSource;
    }
    
    public boolean isTarget() {
        return isTarget;
    }
    
    public boolean isPath() {
        return isPath;
    }

}
