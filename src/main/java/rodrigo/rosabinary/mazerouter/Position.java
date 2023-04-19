package rodrigo.rosabinary.mazerouter;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo Rosa
 */
public class Position {
    private int x;
    private int y;
    
    public Position() {
        this.x = 0;
        this.y = 0;
    }
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int[] get() {
        int[] pos = new int[2];
        pos[0] = x;
        pos[1] = y;
        return pos;
    }
    
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    
    public Position getUp() {
        return new Position(x, y-1);
    }

    public Position getDown() {
        return new Position(x, y+1);
    }

    public Position getLeft() {
        return new Position(x-1, y);
    }

    public Position getRight() {
        return new Position(x+1, y);
    }
    
    public ArrayList<Position> getNeighbors() {
        ArrayList<Position> neighbors = new ArrayList<>();
        neighbors.add(getUp());
        neighbors.add(getDown());
        neighbors.add(getLeft());
        neighbors.add(getRight());
        return neighbors;
    }
    
    public boolean isValid(int sizeX, int sizeY){
        if (x >= 0)
            if (y >= 0)
                if (x < sizeX)
                    if (y < sizeY)
                        return true;
        return false;
    }

}
