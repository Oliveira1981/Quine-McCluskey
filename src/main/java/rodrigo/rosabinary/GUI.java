package rodrigo.rosabinary;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import rodrigo.rosabinary.mazerouter.*;
import rodrigo.rosabinary.quinemccluskey.*;

/**
 *
 * @author Rodrigo Rosa
 */
public class GUI implements KeyListener {
    
    private JTabbedPane        tabbedPane;
    private QuineMcCluskey quineMcCluskey;
    private MazeRouter               maze;
    private final boolean       darkTheme;
    
    public GUI(boolean darkTheme){
        this.darkTheme = darkTheme;
    }
    
    public void showMainWindow() throws Exception {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                try {
                    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    if (darkTheme) {
                        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                    }
                    else {
                        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                    }
                }
                catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                JFrame mainFrame = new JFrame("ROSA Binary");
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                
                tabbedPane = new JTabbedPane(1);
                tabbedPane.setName("mainTabbedPane");
                tabbedPane.setForeground(new Color(30, 130, 230));
                tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
                
                try {
                    quineMcCluskey = new QuineMcCluskey(darkTheme);
                    tabbedPane.add("Quine-McCluskey", quineMcCluskey.getPanel());
                    
                    maze = new MazeRouter(darkTheme);
                    tabbedPane.add("Maze", maze.getMazePanel());
                    
                    tabbedPane.add("Fatoração", new JPanel());
                    
                    tabbedPane.add("Composição Funcional", new JPanel());
                }
                catch (FileNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                //tabbedPane.addKeyListener(this);
                tabbedPane.setFocusable(true);
                
                /*tabbedPane.getComponent(0).addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("foreground".equals(evt.getPropertyName())){
                            try {
                                if (darkTheme) {
                                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                                }
                                else {
                                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                                }
                            }
                            catch (UnsupportedLookAndFeelException ex) {
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    SwingUtilities.updateComponentTreeUI(mainFrame);
                                }
                            });
                        }
                    }
                });*/
                
                mainFrame.add(tabbedPane);
                mainFrame.pack();
                mainFrame.setMinimumSize(new Dimension(1200, 500));
                mainFrame.setSize(new Dimension(1200, 600));
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
                mainFrame.setVisible(true);
                mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                //mainFrame.getRootPane().setDefaultButton(quineMcCluskey.okButton);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(quineMcCluskey.labelVariables);
                
                /*
                mainFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) { 
                        printt("\n");
                    }
                });
                */
            }
        });
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
    }

}
