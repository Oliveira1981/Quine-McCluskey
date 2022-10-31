package com.mycompany.quine.mccluskey;

import java.awt.Font;
import java.awt.GridLayout;
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
        
        String[] templates = {
            "2+4+6+8+9+10+12+13+15",
            "ABCD+!A!BCD+A!B!C!D+!ABCD",
            "A!BCD+!ABC!D+!ABCD+A!B!CD",
            "!A*!B*!C*!D + !A*!B*!C*D + !A*B*!C*D + !A*B*C*!D + !A*B*C*D",
            "ab!c+a!bc!d+!ab",
            "abc+bcd",
            "1111+0011+1000+0111",
            "1111+0011+1010+0111",
            "111+11+101+01",
            "15+3+10+7",
            "0xB754"
        };
        
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JPanel myPanel = new JPanel(new GridLayout(0,1));
        
        JLabel comboBoxLabel = new JLabel("Expressão");
        Font font = new Font("Segoe UI", Font.BOLD, 13);
        comboBoxLabel.setFont(font);
        JComboBox<String> jComboBox = new JComboBox<>(templates);
        jComboBox.setEditable(true);
        
        myPanel.add(comboBoxLabel);
        myPanel.add(jComboBox);
        
        JOptionPane.showMessageDialog(null, myPanel, "Entrada", -1, null);
        expression = (String) jComboBox.getSelectedItem();
        inputFormat = String.valueOf(jComboBox.getSelectedItem());
    }

}
