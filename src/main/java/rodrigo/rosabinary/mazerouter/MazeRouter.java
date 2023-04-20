package rodrigo.rosabinary.mazerouter;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 *
 * @author Rodrigo Rosa
 */
public class MazeRouter implements KeyListener {
    
    public Maze maze             = new Maze(60, 30);
    public JPanel panel;
    public ArrayList<JTextField> textFields = new ArrayList<>();
    public Font textFieldFont    = new Font("Segoe UI", Font.BOLD, 10);
    public boolean pressed       = false;
    public boolean played        = false;
    //public boolean released      = false;
    public boolean firstStepDone = false;
    public boolean targetFound   = false;
    public boolean showBorder    = true;
    public Position currentPos   = new Position();
    public Color colorFree;//       = new Color(10, 10, 10);
    public Color colorBlocked    = new Color(150, 30, 50);
    public Color colorSource     = new Color(80, 180, 255);
    public Color colorTarget     = new Color(0, 220, 50);
    public Color colorExpanding  = Color.MAGENTA;
    public Color colorExpanded   = new Color(20, 50, 250);
    public Color colorPath       = new Color(255, 200, 0);
    public Color colorBorder;//     = new Color(100, 100, 100, 40);
    public Sound sound           = new Sound();
    public Clip clip;

    public MazeRouter(){
        
    }
    
    public JPanel mazeRouterPanel (boolean darkTheme) {
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panel = new JPanel(grid);
        //JScrollPane scrollPane = new JScrollPane(panel);
        if (darkTheme) {
            colorFree = new Color(10, 10, 10);
            colorBorder = new Color(100, 100, 100, 40);
        }
        else {
            colorFree = new Color(199, 222, 244);
            colorBorder = new Color(155, 177, 199, 40);
        }
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        for (int y=0; y < maze.getSizeY(); y++) {
            for (int x=0; x < maze.getSizeX(); x++) {
                textFields.add(new JTextField());
                int last = textFields.size()-1;
                int a = x;
                int b = y;
                //textFields.get(last).setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.1f)));
                textFields.get(last).setBorder(BorderFactory.createLineBorder(colorBorder));
                textFields.get(last).setForeground(new Color(50, 50, 50));
                int cellHeight = dim.height/maze.getSizeX()-3;
                textFields.get(last).setPreferredSize(new Dimension(cellHeight, cellHeight));
                textFields.get(last).setMinimumSize(new Dimension(10, 10));
                textFields.get(last).setBackground(colorFree);
                textFields.get(last).setHorizontalAlignment(0);
                textFields.get(last).setEditable(false);
                textFields.get(last).setToolTipText(x+","+y);
        	c.fill = GridBagConstraints.BOTH;
        	c.gridx = x;
        	c.gridy = y;
        	c.gridwidth = 1;
        	c.gridheight = 1;
                c.weightx = 0.1;
                c.weighty = 0.1;
                panel.add(textFields.get(last), c);
                textFields.get(last).addKeyListener(this);
                textFields.get(last).addMouseListener(new MouseListener() {
                    
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (maze.getCell(new Position(a, b)).isTarget()) {
                            textFields.get(last).setBackground(colorFree);
                            textFields.get(last).setText("");
                            maze.getCell(new Position(a, b)).setSource(false);
                            maze.getCell(new Position(a, b)).setTarget(false);
                            maze.setFree(new Position(a, b));
                            return;
                        }
                        if (maze.getCell(new Position(a, b)).isSource()) {
                            try {
                                sound.play("setTarget.wav");
                            } catch (LineUnavailableException |
                                     UnsupportedAudioFileException |
                                     IOException ex) {
                            }
                            textFields.get(last).setBackground(colorTarget);
                            textFields.get(last).setFont(textFieldFont);
                            textFields.get(last).setText("T");
                            maze.getCell(new Position(a, b)).setTarget(true);
                            maze.getCell(new Position(a, b)).setSource(false);
                            maze.setFree(new Position(a, b));
                            return;
                        }
                        if (maze.getCell(new Position(a, b)).isBlocked()){
                            try {
                                sound.play("setSource.wav");
                            } catch (LineUnavailableException |
                                     UnsupportedAudioFileException |
                                     IOException ex) {
                            }
                            textFields.get(last).setBackground(colorSource);
                            textFields.get(last).setFont(textFieldFont);
                            textFields.get(last).setText("S");
                            maze.getCell(new Position(a, b)).setSource(true);
                            maze.getCell(new Position(a, b)).setTarget(false);
                            maze.getCell(new Position(a, b)).setFree();
                            return;
                        }
                        if (maze.getCell(new Position(a, b)).isFree()) {
                            textFields.get(last).setBackground(colorBlocked);
                            maze.setBlocked(new Position(a, b));
                            maze.getCell(new Position(a, b)).setSource(false);
                            maze.getCell(new Position(a, b)).setTarget(false);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        pressed = true;
                        //released = false;
                        try {
                            sound.play("click.wav");
                        } catch (LineUnavailableException |
                                 UnsupportedAudioFileException |
                                 IOException ex) {
                        }
                    }
                    
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        //released = true;
                        pressed = false;
                        if (played) {
                            played = false;
                            sound.stop(clip);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (pressed) {
                            if (!played) {
                                try {
                                    //clip = sound.play("blockLine.wav");
                                    clip = sound.loop("blockLine.wav");
                                    played = true;
                                } catch (LineUnavailableException |
                                         UnsupportedAudioFileException |
                                         IOException ex) {
                                }
                            }
                            Position pos = new Position(a, b);
                            int textFieldIndex = a+b*maze.getSizeX();
                            if (maze.getCell(pos).isBlocked()) {
                                maze.getCell(pos).setFree();
                                textFields.get(textFieldIndex).setBackground(colorFree);
                            }
                            else {
                                maze.getCell(pos).setBlocked();
                                textFields.get(textFieldIndex).setBackground(colorBlocked);
                            }
                            textFields.get(textFieldIndex).setText("");
                            maze.getCell(pos).setSource(false);
                            maze.getCell(pos).setTarget(false);
                        }
                    }
                });
            }
        }
        return panel;
    }
    
    public void findPath(Maze maze){
        Position sourcePos = maze.getSourcePosition();
        maze.expand(sourcePos);
        currentPos = sourcePos;
        
        while(!maze.getExpansionList().isEmpty() &&
            !targetFound) {
            
            targetFound = findPathStep();
        }
    }
    
    public boolean findPathStep() {
        int steps = maze.getExpansionList().size();
        if (steps == 0) {
            showFinalResult();
            return false;
        }
        ArrayList<Position> pos = maze.getExpansionList();
        for(int c=0; c<pos.size(); c++) {
            maze.getCell(pos.get(c)).setIsExpanding(false);
            maze.getCell(pos.get(c)).setIsExpanded(true);
        }
        while(steps > 0) {
            ArrayList<Position> neighbors = currentPos.getNeighbors();
            for (int n=0; n < neighbors.size(); n++) {
                if (maze.isValid(neighbors.get(n))) {
                    if (maze.getCell(neighbors.get(n)).isTarget()) {
                        targetFound = true;
                        try {
                            sound.play("found.wav");
                        } catch (LineUnavailableException |
                                 UnsupportedAudioFileException |
                                 IOException ex) {
                        }
                        return true;
                    }
                }
            }
            currentPos = maze.expansionListRemove(0);
            if (maze.getCell(currentPos).isTarget()) {
                targetFound = true;
                return true;
            }
            if (maze.expand(currentPos)) {//targetFound
                targetFound = true;
                try {
                    sound.play("found.wav");
                } catch (LineUnavailableException |
                         UnsupportedAudioFileException |
                         IOException ex) {
                }
                return true;
            }
            steps--;
        }
        return false;
    }
    
    public static ArrayList<Position> getPath(Maze maze) {
        ArrayList<Position> path = new ArrayList<>();
        Position targetPos = maze.getTargetPosition();
        path.add(targetPos);
        
        while (maze.getCell(path.get(0)).getWeight() > 1) {
            Position currentPos = path.get(0);
            Position nextPos;
            ArrayList<Position> neighbors = currentPos.getNeighbors();
            for(int n=0; n < neighbors.size(); n++) {
                if (maze.isValid(neighbors.get(n))) {
                    nextPos = neighbors.get(n);
                    if (maze.getCell(nextPos).isExpanded()) {
                        if ((maze.getCell(currentPos).getWeight() -
                             maze.getCell(nextPos).getWeight()) == 1) {
                            path.add(0, nextPos);
                            maze.setPathCell(nextPos);
                            break;
                        }
                    }
                }
            }
        }
        path.add(0, maze.getSourcePosition());
        return path;
    }
    
    public void showResult () {
        int bPos=0;
        for (int y=0; y < maze.getSizeY(); y++) {
            for (int x=0; x < maze.getSizeX(); x++) {
                if (maze.getCell(new Position(x, y)).isExpanding()) {
                    textFields.get(bPos).setBackground(colorExpanding);
                    textFields.get(bPos).setFont(textFieldFont);
                    int w = maze.getCell(new Position(x, y)).getWeight();
                    textFields.get(bPos).setText(String.valueOf(w));
                }
                if (maze.getCell(new Position(x, y)).isExpanded()) {
                    textFields.get(bPos).setBackground(colorExpanded);
                    textFields.get(bPos).setFont(textFieldFont);
                    int w = maze.getCell(new Position(x, y)).getWeight();
                    textFields.get(bPos).setText(String.valueOf(w));
                }
                if (maze.getCell(new Position(x, y)).isPath()) {
                    textFields.get(bPos).setBackground(colorPath);
                }
                if (maze.getCell(new Position(x, y)).isTarget()) {
                    textFields.get(bPos).setFont(textFieldFont);
                    textFields.get(bPos).setText("T");
                    textFields.get(bPos).setBackground(colorTarget);
                }
                if (showBorder) {
                    //textFields.get(bPos).setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.1f)));
                    textFields.get(bPos).setBorder(BorderFactory.createLineBorder(colorBorder));
                }
                else {
                    textFields.get(bPos).setBorder(BorderFactory.createStrokeBorder(new BasicStroke(0.0f)));
                }
                bPos++;
            }
        }
    }
    
    public void cleanUpMaze() {
        int bPos=0;
        for (int y=0; y < maze.getSizeY(); y++) {
            for (int x=0; x < maze.getSizeX(); x++) {
                textFields.get(bPos).setText("");
                textFields.get(bPos).setBackground(colorFree);
                maze.cleanUpMaze();
                bPos++;
            }
        }
        //panel.repaint();
    }
    
    public void showFinalResult() {
        //ArrayList<Position> path = getPath(maze);
        getPath(maze);
        
        if (!targetFound) {
            maze.println("Caminho não encontrado.");
            showResult();
            try {
                sound.play("notFound.wav");
            } catch (LineUnavailableException |
                     UnsupportedAudioFileException |
                     IOException ex) {
            }
            return;
        }
        
        /*maze.println("Caminho encontrado:\n" +
                     "X\tY"
        );
        for (int p=0; p < path.size(); p++) {
            maze.print(path.get(p).getX()+"\t"+
                       path.get(p).getY()+"\n"
            );
        }
        
        maze.println("Comprimento: " +
            maze.getCell(maze.getTargetPosition()).getWeight()
        );*/
        showResult();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            cleanUpMaze();
            firstStepDone = false;
            targetFound = false;
            try {
                sound.play("reset.wav");
            } catch (LineUnavailableException |
                     UnsupportedAudioFileException |
                     IOException ex) {
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            try {
                sound.play("expanding.wav");
            } catch (LineUnavailableException |
                     UnsupportedAudioFileException |
                     IOException ex) {
            }
            if (targetFound) {
                return;
            }
            if (!firstStepDone) {
                firstStepDone = true;
                maze.setSource();
                maze.setTarget();
                Position sourcePos = maze.getSourcePosition();
                maze.expand(sourcePos);
                currentPos = sourcePos;
                showResult();
                return;
            }
            if(!findPathStep()) {
                showResult();
            }
            else {
                targetFound = true;
                showFinalResult();
            }
        }
        
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            maze.setSource();
            maze.setTarget();
            findPath(maze);
            showFinalResult();
        }
        if (e.getKeyCode() == KeyEvent.VK_B) {
            showBorder = !showBorder;
            showResult();
            //panel.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
