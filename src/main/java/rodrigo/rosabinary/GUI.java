package rodrigo.rosabinary;

import java.awt.*;
import java.awt.event.*;
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
    
    public GUI(){
    }
    
    public void showMainWindow() throws Exception {
        
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        JFrame mainFrame = new JFrame("ROSA Binary");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        tabbedPane = new JTabbedPane(1);
        tabbedPane.setName("mainTabbedPane");
        tabbedPane.setForeground(new Color(30, 130, 230));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        quineMcCluskey = new QuineMcCluskey();
        tabbedPane.add("Quine-McCluskey", quineMcCluskey.quineMcPanel());
        
        mazeRouter = new MazeRouter();
        tabbedPane.add("Maze Router", mazeRouter.mazeRouterPanel());
        
        tabbedPane.add("Fatoração", new JPanel());
        
        tabbedPane.add("Composição Funcional", new JPanel());
        
        tabbedPane.addKeyListener(this);
        tabbedPane.setFocusable(true);
        
        mainFrame.add(tabbedPane);
        mainFrame.pack();
        mainFrame.setMinimumSize(new Dimension(1050,600));
        mainFrame.setSize(new Dimension(1050,700));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
        mainFrame.setVisible(true);
        mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        mainFrame.getRootPane().setDefaultButton(quineMcCluskey.okButton);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(quineMcCluskey.labelVariables);
        
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
