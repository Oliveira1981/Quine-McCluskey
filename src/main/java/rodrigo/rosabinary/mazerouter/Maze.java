package rodrigo.rosabinary.mazerouter;

import java.util.ArrayList;

/**
 *
 * @author Rodrigo Rosa
 */
public class Maze {
    
    private final int sizeX;
    private final int sizeY;
    private ArrayList<ArrayList<Cell>> maze;
    private Position sourcePosition;
    private Position targetPosition;
    private ArrayList<Position> expansionList;
    
    public Maze(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        maze = new ArrayList<>();
        for (int y=0; y < sizeY; y++) {
            maze.add(new ArrayList<>());
            for (int x=0; x < sizeX; x++) {
                maze.get(y).add(new Cell('.'));
            }
        }
        sourcePosition = new Position(0, 0);
        targetPosition = new Position(sizeX-1, sizeY-1);
        expansionList = new ArrayList<>();
    }
    
    public void printMazeChars() {
        print("    ");
        for (int x=0; x < sizeX; x++) {
            print(x + " ");
            if (x < 10) {
                print(" ");
            }
        }
        for (int y=0; y < sizeY; y++) {
            println();
            if (y < 10) {
                print(" ");
            }
            print(y + "  ");
            for (int x=0; x < sizeX; x++) {
                print(maze.get(y).get(x).getChar()+"  ");
            }
        }
        println();
    }
    
    public void printMazeWeights() {
        print("     ");
        for (int x=0; x < sizeX; x++) {
            print(x + " ");
            if (x < 10) {
                print(" ");
            }
        }
        for (int y=0; y < sizeY; y++) {
            println();
            if (y < 10) {
                print(" ");
            }
            print(y + "  ");
            for (int x=0; x < sizeX; x++) {
                int weight = maze.get(y).get(x).getWeight();
                if (weight == 0) {
                    print (" . ");
                }
                else {
                    if (weight == -1) {
                        print (" X ");
                    }
                    else {
                        if (weight < 10) {
                            print(" ");
                        }
                        print(weight + " ");
                    }
                }
            }
        }
        println();
    }
    
    public void setChar(Position pos, char c) {
        int x = pos.getX();
        int y = pos.getY();
        maze.get(y).get(x).setChar(c);
    }
    
    public void setPathWeight(Position pos, int pw) {
        int x = pos.getX();
        int y = pos.getY();
        maze.get(y).get(x).setWeight(pw);
    }
    
    public void setFree(Position pos, boolean isFree) {
        int x = pos.getX();
        int y = pos.getY();
        maze.get(y).get(x).setFree();
    }
    
    public char getChar(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        return maze.get(y).get(x).getChar();
    }
    
    public int getPathWeight(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        return maze.get(y).get(x).getWeight();
    }
    
    public boolean isFree(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        return maze.get(y).get(x).isFree();
    }
    
    public void setHorizontalBlock(int startX, int startY, int blockSize) {
        if (startX < 0) {
            startX = 0;
        }
        if (startY < 0) {
            startY = 0;
        }
        if (startX >= sizeX) {
            startX = sizeX-1;
            blockSize = 1;
        }
        if (startY >= sizeY) {
            startY = sizeY-1;
        }
        if ((startX + blockSize) >= sizeX) {
            blockSize = sizeX - startX;
        }
        for(int x=startX; x < (startX+blockSize); x++) {
            maze.get(startY).get(x).setChar('X');
            maze.get(startY).get(x).setWeight(-1);
            maze.get(startY).get(x).setBlocked();
        }
    }
    
    public void setVerticalBlock(int startX, int startY, int blockSize) {
        if (startX < 0) {
            startX = 0;
        }
        if (startY < 0) {
            startY = 0;
        }
        if (startX >= sizeX) {
            startX = sizeX-1;
            blockSize = 1;
        }
        if (startY >= sizeY) {
            startY = sizeY-1;
        }
        if ((startY + blockSize) >= sizeY) {
            blockSize = sizeY - startY;
        }
        for(int y=startY; y < (startY+blockSize); y++) {
            maze.get(y).get(startX).setChar('X');
            maze.get(y).get(startX).setWeight(-1);
            maze.get(y).get(startX).setBlocked();
        }
    }
    
    public void setSource(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        maze.get(y).get(x).setChar('S');
        maze.get(y).get(x).setSource(true);
        sourcePosition.set(x, y);
    }
    
    public void setTarget(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        maze.get(y).get(x).setChar('T');
        maze.get(y).get(x).setTarget(true);
        targetPosition.set(x, y);
    }
    
    public void setSource() {
        for (int y=0; y < sizeY; y++) {
            for (int x=0; x < sizeX; x++) {
                if (maze.get(y).get(x).isSource()) {
                    maze.get(y).get(x).setChar('S');
                    sourcePosition.set(x, y);
                    return;
                }
            }
        }
    }
    
    public void setTarget() {
        for (int y=0; y < sizeY; y++) {
            for (int x=0; x < sizeX; x++) {
                if (maze.get(y).get(x).isTarget()) {
                    maze.get(y).get(x).setChar('T');
                    targetPosition.set(x, y);
                    return;
                }
            }
        }
    }
    
    public void setPathCell(Position pos) {
        maze.get(pos.getY()).get(pos.getX()).setPath(true);
        maze.get(pos.getY()).get(pos.getX()).setChar('O');
    }
    
    public void setFree(Position pos) {
        maze.get(pos.getY()).get(pos.getX()).setFree();
        maze.get(pos.getY()).get(pos.getX()).setChar('.');
    }
    
    public void setBlocked(Position pos) {
        maze.get(pos.getY()).get(pos.getX()).setBlocked();
        maze.get(pos.getY()).get(pos.getX()).setChar('X');
    }
    
    public int getSizeX() {
        return sizeX;
    }
    
    public int getSizeY() {
        return sizeY;
    }
    
    public Cell getCell(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        return maze.get(y).get(x);
    }
    
    public Position getSourcePosition() {
        return sourcePosition;
    }
    
    public Position getTargetPosition() {
        return targetPosition;
    }
    
    public boolean isPathCell(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        return maze.get(y).get(x).isPath();
    }
    
    public void setCell(Position pos, char c) {
        int x = pos.getX();
        int y = pos.getY();
        maze.get(y).get(x).setChar(c);
    }
    
    public boolean expand(Position pos) {
        //boolean expanded = false;
        if (getCell(pos).isTarget()) {
            return true;
        }
        int currentWeight = getCell(pos).getWeight();
        int expandedWeight = 1 + currentWeight;
        
        // UP ///////////////
        Position up = pos.getUp();
        if (isValid(up)) {
            if (getCell(up).isFree() &&
               !getCell(up).isExpanded() &&
               !getCell(up).isExpanding() &&
               !getCell(up).isSource()) {
                    getCell(up).setWeight(expandedWeight);
                    getCell(up).setIsExpanding(true);
                    expansionList.add(up);
                    //expanded = true;
                    if (getCell(up).isTarget()) {
                        return true;
                    }
            }
        }
        
        // DOWN /////////////
        Position down = pos.getDown(); //GET DOWN, MAKE LOVE!
        if (isValid(down)) {
            if (getCell(down).isFree() &&
               !getCell(down).isExpanded() &&
               !getCell(down).isExpanding() &&
               !getCell(down).isSource()) {
                    getCell(down).setWeight(expandedWeight);
                    getCell(down).setIsExpanding(true);
                    expansionList.add(down);
                    //expanded = true;
                    if (getCell(down).isTarget()) {
                        return true;
                    }
            }
        }
        
        // LEFT /////////////
        Position left = pos.getLeft();
        if (isValid(left)) {
            if (getCell(left).isFree() &&
               !getCell(left).isExpanded() &&
               !getCell(left).isExpanding() &&
               !getCell(left).isSource()) {
                    getCell(left).setWeight(expandedWeight);
                    getCell(left).setIsExpanding(true);
                    expansionList.add(left);
                    //expanded = true;
                    if (getCell(left).isTarget()) {
                        return true;
                    }
            }
        }
        
        // RIGHT ////////////
        Position right = pos.getRight();
        if (isValid(right)) {
            if (getCell(right).isFree() &&
               !getCell(right).isExpanded() &&
               !getCell(right).isExpanding() &&
               !getCell(right).isSource()) {
                    getCell(right).setWeight(expandedWeight);
                    getCell(right).setIsExpanding(true);
                    expansionList.add(right);
                    //expanded = true;
                    if (getCell(right).isTarget()) {
                        return true;
                    }
            }
        }
        return false;
    }
    
    public ArrayList<Position> getExpansionList() {
        return expansionList;
    }
    
    public void expansionListAdd(Position pos) {
        expansionList.add(pos);
    }
    
    public Position expansionListRemove(int i) {
        return expansionList.remove(i);
    }
    
    public boolean isValid(Position pos){
        int x = pos.getX();
        int y = pos.getY();
        if (x >= 0)
            if (y >= 0)
                if (x < sizeX)
                    if (y < sizeY)
                        return true;
        return false;
    }
    
    public void cleanUpMaze() {
        maze.clear();
        maze = new ArrayList<>();
        for (int y=0; y < sizeY; y++) {
            maze.add(new ArrayList<>());
            for (int x=0; x < sizeX; x++) {
                maze.get(y).add(new Cell('.'));
            }
        }
        sourcePosition = new Position();
        targetPosition = new Position();
        expansionList = new ArrayList<>();
    }
    
    public void print(Object obj) {
        System.out.print(obj);
    }
    
    public void println(Object obj) {
        System.out.println(obj);
    }
    
    public void println() {
        System.out.println();
    }
}
