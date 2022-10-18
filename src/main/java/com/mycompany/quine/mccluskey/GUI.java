package com.mycompany.quine.mccluskey;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
*
 * @author Rodrigo da Rosa
 */
public final class GUI {
    
    private String inputFormat;
    private String expression;
    
    public GUI(){
        inputFormat = "";
        expression  = "";
    }
    
    public String getInputFormat(){
        return inputFormat;
    }
    
    public String getExpression(){
        return expression;
    }
    
    public void showDialog(String template) throws Exception {
        
        String[] optionsToChoose = {
            "Literal",
            "Decimal",
            "Binária",
        };
        
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Font font = new Font("Segoe UI", Font.BOLD, 13);
        JPanel myPanel = new JPanel(new GridLayout(0,1));
        
        JLabel textFieldLabel = new JLabel("Expressão");
        //JTextField jTextField = new JTextField("",20);
        JLabel comboBoxLabel = new JLabel("Tipo");
        JComboBox<String> jComboBox = new JComboBox<>(optionsToChoose);
        
        textFieldLabel.setFont(font);
        comboBoxLabel.setFont(font);
        
        myPanel.add(comboBoxLabel);
        myPanel.add(jComboBox);
        myPanel.add(Box.createHorizontalStrut(1));
        myPanel.add(textFieldLabel);
        
        //expression = JOptionPane.showInputDialog(null, myPanel, template);
        expression = (String) JOptionPane.showInputDialog(
                null,
                myPanel,
                "Entrada",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                template
        ); 
        inputFormat = String.valueOf(jComboBox.getSelectedItem());
    }

}
