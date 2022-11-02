package com.mycompany.quine.mccluskey;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
*
 * @author Rodrigo da Rosa
 */
public final class GUI extends Tools implements KeyListener {
    
    private String inputFormat;
    private String expression;
    //JFrame myFrame = new JFrame("Quine-McCluskey");
    
    public GUI(){
        inputFormat = "";
        expression  = "";
        //myFrame.addKeyListener(this);
    }
    
    public String getInputFormat(){
        return inputFormat;
    }
    
    public String getExpression(){
        return expression;
    }
    
    public void showWindow() throws Exception {
        String[] templates = {
            "abc+cbd;a2;1101+0111+0101;0+1+2+3+4+5+6+7;0xA2B2;;2+4+6+8+9+10+12+13+15",
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
        JFrame myFrame = new JFrame("Quine-McCluskey");
        //myFrame.addKeyListener(this);
        //myFrame.setFocusable(true);
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myFrame.setMinimumSize(new Dimension(650,350));
        
        GridBagLayout grid = new GridBagLayout();
        JPanel vPanel = new JPanel(grid);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        
        Font font = new Font("Segoe UI", Font.BOLD, 13);
        
        JLabel vSpaceUp = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 4;
	c.gridheight = 1;
        c.weightx = 1.0;
        c.weighty = 0.01;
        vPanel.add(vSpaceUp, c);
        
        JLabel hSpaceLeft = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 5;
        c.weightx = 0.1;
        c.weighty = 0.98;
        vPanel.add(hSpaceLeft, c);
        
        JLabel comboBoxLabel = new JLabel("Expressões:");
        comboBoxLabel.setFont(font);
        comboBoxLabel.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
	c.gridy = 1;
	c.gridwidth = 2;
	c.gridheight = 1;
        c.weightx = 0.8;
        c.weighty = 0.01;
        vPanel.add(comboBoxLabel, c);
        
        JComboBox<String> jComboBox = new JComboBox<>(templates);
        jComboBox.setEditable(true);
        JTextField editor = (JTextField) jComboBox.getEditor().getEditorComponent();
        editor.addKeyListener(this);
        jComboBox.setFocusable(true);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 2;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.7;
        c.weighty = 0.01;
        vPanel.add(jComboBox, c);
        
        JButton okButton = new JButton("Executar");
        okButton.addKeyListener(this);
        okButton.setFocusable(true);
        okButton.setBackground(new Color(11, 188, 255));
        okButton.setForeground(new Color(11, 111, 222));
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 2;
	c.gridy = 2;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.1;
        c.weighty = 0.01;
        vPanel.add(okButton, c);
        myFrame.getRootPane().setDefaultButton(okButton);
        
        JLabel hSpaceCenter = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 3;
	c.gridwidth = 2;
	c.gridheight = 1;
        c.weightx = 0.8;
        c.weighty = 0.01;
        vPanel.add(hSpaceCenter, c);
        
        JLabel resultLabel = new JLabel("Resultados:");
        resultLabel.setFont(font);
        resultLabel.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 4;
	c.gridwidth = 2;
	c.gridheight = 1;
        c.weightx = 0.8;
        c.weighty = 0.01;
        vPanel.add(resultLabel, c);
        
        JTextArea textArea = new JTextArea();
        textArea.addKeyListener(this);
        textArea.setFocusable(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBackground(new Color(44, 44, 44));
        textArea.setForeground(new Color(1, 188, 255));
        Font fontResult = new Font("Consolas", Font.PLAIN, 15);
        textArea.setFont(fontResult);
        
        JScrollPane jScroll = new JScrollPane(textArea);
	c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
	c.gridy = 5;
	c.gridwidth = 2;
	c.gridheight = 1;
	c.weightx = 0.8;
        c.weighty = 0.95;
        vPanel.add(jScroll, c);
        
        JLabel hSpaceRight = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 3;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 5;
        c.weightx = 0.1;
        c.weighty = 0.98;
        vPanel.add(hSpaceRight, c);
        
        JLabel hSpaceBottom = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 6;
	c.gridwidth = 4;
	c.gridheight = 1;
        c.weightx = 1.0;
        c.weighty = 0.01;
        vPanel.add(hSpaceBottom, c);
        
        myFrame.add(vPanel);
        myFrame.pack();
        myFrame.setSize(650, 600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        myFrame.setLocation(dim.width/2-myFrame.getSize().width/2, dim.height/2-myFrame.getSize().height/2);
        myFrame.setVisible(true);
        
        okButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    textArea.setText(
                        optimizeExpressions((String) jComboBox.getSelectedItem())
                    );
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        inputFormat = String.valueOf(jComboBox.getSelectedItem());
    }
    
    public String optimizeExpressions(String allExpressions) throws Exception {
        PrintWriter writer = new PrintWriter("Quine-McCluskey Results.txt", "UTF-8");
        String result  = "";
        
        inputFormat = getInputFormat();
        
        if(allExpressions == null || allExpressions.length() == 0) {
            writer.close();
            System.exit(0);
        }
        
        allExpressions = removeSpacesFromExpression(allExpressions);
        
        int begin = 0;
        int end;
        do {
            end = allExpressions.indexOf(';', begin);
            if (end < 0) {
                end = allExpressions.length();
            }
            expression = allExpressions.substring(begin, end);
            
            inputFormat = detectInputFormat(expression);
            if (inputFormat.length() == 0 ||
                inputFormat.equals("ERRO")) {
                result += print("Expressão:\n> " + expression + "\n", writer);
                result += print("\nExpressão inconsistente.\n", writer);
                result += print("\nFim do resultado parcial.\n", writer);
                result += print("==================================================\n\n", writer);
                begin = end + 1;
                continue;
            }
            
            result += print("Formato de Entrada:\n> " + inputFormat + "\n", writer);
            
            if (inputFormat.equals("Hexadecimal")) {
                result += print("\nExpressão original:\n> " + expression + "\n", writer);
                expression = hexadecimal2expression(expression);
                inputFormat = "Decimal";
                result += print("\nFormato Convertido:\n> Decimal\n", writer);
            }
            
            result += print("\nExpressão:\n> " + expression + "\n", writer);
            
/////////////////////////////////////////////////////
            SumOfProducts exp = new SumOfProducts(inputFormat, expression);
/////////////////////////////////////////////////////
    
            result += print("\nVariáveis:\n> " + exp.getNumberOfVars() + "\n", writer);
            
/////////////////////////////////////////////////////
            exp.sortByOnesCount();
/////////////////////////////////////////////////////
            
            result += print("\nMintermos por número de 1s:", writer);
            for(int i=0; i < exp.getProductsList().size(); i++) {
                result += print("\n", writer);
                result += print(exp.getProductsList().get(i).getMinTermsList()+"\t", writer);
                result += print(exp.getProductsList().get(i).getBinaryView()+"\t", writer);
                result += print(exp.getProductsList().get(i).getLiteralView(), writer);
            }
            
            if (isDumb(exp.getMinTermsList())) {
                result += print ("\n\nExpressão redundante:\n> não use portas lógicas, ligue em VDD.\n", writer);
                result += print("\nFim do resultado parcial.\n", writer);
                result += print("==================================================\n\n", writer);
                begin = end + 1;
                continue;
            }
            
/////////////////////////////////////////////////////
            exp.mergePrimeImplicants(10);
/////////////////////////////////////////////////////
    
            result += print("\n\nImplicantes primos mesclados:\n", writer);
            for(int i=0; i < exp.getProductsList().size(); i++) {
                int q = 0;
                for(; q < exp.getProductsList().get(i).getMinTermsList().size(); q++) {
                    result += print("-"+exp.getProductsList().get(i).getMinTermsList().get(q), writer);
                }
                if(q < 3) {
                    result += print("-\t", writer);
                }
                result += print("\t", writer);
                result += print(exp.getProductsList().get(i).getBinaryView()+" \t", writer);
                result += print(exp.getProductsList().get(i).getLiteralView()+"\n", writer);
            }
            
/////////////////////////////////////////////////////
            exp.fillMinTermsList();
/////////////////////////////////////////////////////
    
            result += print ("\nMintermos e seus produtos (Tabela de Cobertura):", writer);
            for (int i=0; i < exp.getMinTermsList().size(); i++) {
                result += print("\n"+exp.getMinTermsList().get(i).getDecimalView()+" -", writer);
                for (int p=0; p < exp.getMinTermsList().get(i).getProductsList().size(); p++) {
                    result += print("\t\t"+exp.getMinTermsList().get(i).getProductsList().get(p), writer);
                    if (exp.getMinTermsList().get(i).getProductsList().get(p).length() < 8) {
                        result += print("\t", writer);
                    }
                }
            }
            
/////////////////////////////////////////////////////
            exp.essentialProductsToFinalList();
/////////////////////////////////////////////////////
    
            result += print("\n\nProdutos Essenciais:\n> ", writer);
            for (int i=0; i < exp.getFinalProductsList().size(); i++) {
                result += print(exp.getFinalProductsList().get(i)+"\t", writer);
            }
            
/////////////////////////////////////////////////////
            ArrayList<Integer> indexes = exp.getCandidateProductsIndexes();
/////////////////////////////////////////////////////
            result += print("\n", writer);
            for (int i=0; i < exp.getMinTermsList().size(); i++) {
                result += print("\nMintermo "+exp.getMinTermsList().get(i).getDecimalView()+" ", writer);
                if (exp.getMinTermsList().get(i).isIsCovered()) {
                    result += print("está coberto.", writer);
                }
                else {
                    result += print(" - ", writer);
                }
            }
            
/////////////////////////////////////////////////////
            exp.permute(indexes);
/////////////////////////////////////////////////////
    
            //for (int i=0; i<exp.getPermutations().size(); i++) {
            //    r += print("\n"+exp.getPermutations().get(i), writer);
            //}
            
/////////////////////////////////////////////////////
            exp.completeFinalList();
/////////////////////////////////////////////////////
    
            result += print("\n\nProdutos Finais:\n> ", writer);
            for (int i=0; i < exp.getFinalProductsList().size(); i++) {
                result += print(exp.getFinalProductsList().get(i)+"\t", writer);
            }
            
/////////////////////////////////////////////////////
            exp.setOptimizedExpression();
/////////////////////////////////////////////////////
            
            result += print("\n\nExpressão otimizada:\n", writer);
            result += print("> "+exp.getOptimizedExpression()+"\n", writer);
            
            result += print ("\nFim do resultado parcial.\n", writer);
            result += print("==================================================\n\n", writer);
            
            begin = end + 1;
            if (begin >= allExpressions.length()) {
                break;
            }
        }
        while (begin < allExpressions.length());
        
        result += print ("Fim dos resultados.\n", writer);
        result += print("==================================================\n", writer);
            
        writer.close();
        return result;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
