package rodrigo.rosabinary;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    private MazeRouter         mazeRouter;
    private boolean             darkTheme;
    
    public GUI(){
        darkTheme = true;
    }
    
    public void showMainWindow() throws Exception {
        
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        if (darkTheme) {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        }
        else {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        }
        JFrame mainFrame = new JFrame("ROSA Binary");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        tabbedPane = new JTabbedPane(1);
        tabbedPane.setName("mainTabbedPane");
        tabbedPane.setForeground(new Color(30, 130, 230));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        quineMcCluskey = new QuineMcCluskey();
        tabbedPane.add("Quine-McCluskey", quineMcCluskey.quineMcPanel(darkTheme));
        
        mazeRouter = new MazeRouter();
        tabbedPane.add("Maze Router", mazeRouter.mazeRouterPanel(darkTheme));
        
        tabbedPane.add("Fatoração", new JPanel());
        
        tabbedPane.add("Composição Funcional", new JPanel());
        
        tabbedPane.addKeyListener(this);
        tabbedPane.setFocusable(true);
        
        tabbedPane.getComponent(0).addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("foreground".equals(evt.getPropertyName())){
                    try {
                        if (quineMcCluskey.darkTheme) {
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
        });
        
        mainFrame.add(tabbedPane);
        mainFrame.pack();
        mainFrame.setMinimumSize(new Dimension(840, 400));
        mainFrame.setSize(new Dimension(900, 600));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
        mainFrame.setVisible(true);
        mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        mainFrame.getRootPane().setDefaultButton(quineMcCluskey.okButton);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(quineMcCluskey.labelThemeDark);
        
        /*tabbedPane.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex()==1) {
                    //tabbedPane.setComponentAt(1, mazeRouterPanel());
                }
            }
        });
        */
        /*
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) { 
                printt("\n");
            }
        });
        */
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
