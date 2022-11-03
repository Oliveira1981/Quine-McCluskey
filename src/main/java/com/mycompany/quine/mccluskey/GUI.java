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
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myFrame.setMinimumSize(new Dimension(650,400));
        
        GridBagLayout grid = new GridBagLayout();
        JPanel vPanel = new JPanel(grid);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        
        Font font = new Font("Segoe UI", Font.BOLD, 13);
        
        JLabel space1 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 10;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        vPanel.add(space1, c);
        
        JLabel space3 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 18;
        c.weightx = 0.0;
        c.weighty = 100.0;
        vPanel.add(space3, c);
        
        JLabel labelExpressions = new JLabel("Expressões:");
        labelExpressions.setFont(font);
        labelExpressions.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
	c.gridy = 1;
	c.gridwidth = 5;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        vPanel.add(labelExpressions, c);
        
        JComboBox<String> comboExpressions = new JComboBox<>(templates);
        comboExpressions.setEditable(true);
        JTextField editor = (JTextField) comboExpressions.getEditor().getEditorComponent();
        editor.addKeyListener(this);
        comboExpressions.setFocusable(true);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 2;
	c.gridwidth = 5;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        //c.anchor = GridBagConstraints.LINE_END;
        vPanel.add(comboExpressions, c);
        
        /*JLabel space5 = new JLabel("5");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 6;
	c.gridy = 2;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.EAST;
        vPanel.add(space5, c);
        */
        JButton okButton = new JButton("Executar");
        okButton.addKeyListener(this);
        okButton.setFocusable(true);
        okButton.setBackground(new Color(11, 188, 255));
        okButton.setForeground(new Color(11, 111, 222));
	c.fill = GridBagConstraints.NONE;
	c.gridx = 7;
	c.gridy = 2;
	c.gridwidth = 2;
	c.gridheight = 1;
        c.weightx = 0.0;//0.1
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.EAST;
        vPanel.add(okButton, c);
        myFrame.getRootPane().setDefaultButton(okButton);
        
        JLabel space6 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 3;
	c.gridwidth = 8;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        vPanel.add(space6, c);
        
        JLabel labelViewFormat = new JLabel("Apresentação:");
        labelViewFormat.setFont(font);
        labelViewFormat.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
	c.gridy = 4;
	c.gridwidth = 3;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        vPanel.add(labelViewFormat, c);
        
        JLabel space7 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 4;
	c.gridy = 4;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        vPanel.add(space7, c);
        
        JLabel labelWichReport = new JLabel("Exibir:");
        labelWichReport.setFont(font);
        labelWichReport.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
	c.gridy = 4;
	c.gridwidth = 4;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        vPanel.add(labelWichReport, c);
        
        JComboBox<String> comboViewFormat = new JComboBox<>();
        //comboViewFormat.setEditable(false);
        JTextField editor2 = (JTextField) comboViewFormat.getEditor().getEditorComponent();
        editor2.addKeyListener(this);
        comboViewFormat.setFocusable(true);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 5;
	c.gridwidth = 3;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        vPanel.add(comboViewFormat, c);
        
        JLabel space8 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 4;
	c.gridy = 5;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        vPanel.add(space8, c);
        
        JComboBox<String> comboWichReport = new JComboBox<>();
        //comboWichReport.setEditable(false);
        JTextField editor3 = (JTextField) comboWichReport.getEditor().getEditorComponent();
        editor3.addKeyListener(this);
        comboWichReport.setFocusable(true);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 5;
	c.gridy = 5;
	c.gridwidth = 4;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        vPanel.add(comboWichReport, c);
        
        JLabel space9 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 6;
	c.gridwidth = 8;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        vPanel.add(space9, c);
        
        JLabel resultLabel = new JLabel("Resultados:");
        resultLabel.setFont(font);
        resultLabel.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 7;
	c.gridwidth = 8;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        vPanel.add(resultLabel, c);
        
        JTextArea textAreaResult = new JTextArea();
        textAreaResult.addKeyListener(this);
        textAreaResult.setFocusable(true);
        textAreaResult.setLineWrap(true);
        textAreaResult.setEditable(false);
        textAreaResult.setBackground(new Color(44, 44, 44));
        textAreaResult.setForeground(new Color(1, 188, 255));
        Font fontResult = new Font("Consolas", Font.PLAIN, 15);
        textAreaResult.setFont(fontResult);
        
        JScrollPane jScrollResult = new JScrollPane(textAreaResult);
	c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
	c.gridy = 8;
	c.gridwidth = 8;
	c.gridheight = 1;
	c.weightx = 100.0;
        c.weighty = 0.0;
        vPanel.add(jScrollResult, c);
        
        JLabel space10 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 9;
	c.gridwidth = 8;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        vPanel.add(space10, c);
        
        JLabel labelReport = new JLabel("Relatório:");
        labelReport.setFont(font);
        labelReport.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 10;
	c.gridwidth = 8;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        vPanel.add(labelReport, c);
        
        JTextArea textAreaReport = new JTextArea();
        textAreaReport.addKeyListener(this);
        textAreaReport.setFocusable(true);
        textAreaReport.setLineWrap(true);
        textAreaReport.setEditable(false);
        textAreaReport.setBackground(new Color(44, 44, 44));
        textAreaReport.setForeground(new Color(1, 188, 255));
        Font fontReport = new Font("Consolas", Font.PLAIN, 15);
        textAreaReport.setFont(fontReport);
        
        JScrollPane jScrollReport = new JScrollPane(textAreaReport);
	c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
	c.gridy = 11;
	c.gridwidth = 8;
	c.gridheight = 8;
	c.weightx = 100.0;
        c.weighty = 0.6;
        vPanel.add(jScrollReport, c);
        
        JLabel space4 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 9;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 18;
        c.weightx = 0.0;
        c.weighty = 100.0;
        vPanel.add(space4, c);
        
        JLabel space2 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 19;
	c.gridwidth = 10;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        vPanel.add(space2, c);
        
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
                    textAreaReport.setText(
                        optimizeExpressions((String) comboExpressions.getSelectedItem())
                    );
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        inputFormat = String.valueOf(comboExpressions.getSelectedItem());
    }
    
    public String optimizeExpressions(String allExpressions) throws Exception {
        PrintWriter writer = new PrintWriter("Quine-McCluskey Results.txt", "UTF-8");
        String report  = "";
        
        //inputFormat = getInputFormat();
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
            if (inputFormat.length() == 0  ||
                inputFormat.equals("ERRO") ||
               (inputFormat.equals("Literal") && hasDuplicate(expression))) {
                
                report += print("Expressão:\n> " + expression + "\n", writer);
                report += print("\nExpressão inconsistente.\n", writer);
                report += print("\nFim do resultado parcial.\n", writer);
                report += print("==================================================\n\n", writer);
                begin = end + 1;
                continue;
            }
            
            report += print("Formato de Entrada:\n> " + inputFormat + "\n", writer);
            
            if (inputFormat.equals("Hexadecimal")) {
                report += print("\nExpressão original:\n> " + expression + "\n", writer);
                expression = hexadecimal2expression(expression);
                inputFormat = "Decimal";
                report += print("\nFormato Convertido:\n> Decimal\n", writer);
            }
            
            report += print("\nExpressão:\n> " + expression + "\n", writer);
            
/////////////////////////////////////////////////////
            SumOfProducts exp = new SumOfProducts(inputFormat, expression);
/////////////////////////////////////////////////////
    
            report += print("\nVariáveis:\n> " + exp.getNumberOfVars() + "\n", writer);
            
/////////////////////////////////////////////////////
            exp.sortByOnesCount();
/////////////////////////////////////////////////////
            
            report += print("\nMintermos por número de 1s:", writer);
            for(int i=0; i < exp.getProductsList().size(); i++) {
                report += print("\n", writer);
                report += print(exp.getProductsList().get(i).getMinTermsList()+"\t", writer);
                report += print(exp.getProductsList().get(i).getBinaryView()+"\t", writer);
                report += print(exp.getProductsList().get(i).getLiteralView(), writer);
            }
            
            if (isDumb(exp.getMinTermsList(), exp.getNumberOfVars())) {
                report += print ("\n\nExpressão redundante:\n> não use portas lógicas, ligue em VDD.\n", writer);
                report += print("\nFim do resultado parcial.\n", writer);
                report += print("==================================================\n\n", writer);
                begin = end + 1;
                continue;
            }
            
/////////////////////////////////////////////////////
            exp.mergePrimeImplicants(10);
/////////////////////////////////////////////////////
    
            report += print("\n\nImplicantes primos mesclados:\n", writer);
            for(int i=0; i < exp.getProductsList().size(); i++) {
                int q = 0;
                for(; q < exp.getProductsList().get(i).getMinTermsList().size(); q++) {
                    report += print("-"+exp.getProductsList().get(i).getMinTermsList().get(q), writer);
                }
                if(q < 3) {
                    report += print("-\t", writer);
                }
                report += print("\t", writer);
                report += print(exp.getProductsList().get(i).getBinaryView()+" \t", writer);
                report += print(exp.getProductsList().get(i).getLiteralView()+"\n", writer);
            }
            
/////////////////////////////////////////////////////
            exp.fillMinTermsList();
/////////////////////////////////////////////////////
    
            report += print ("\nMintermos e seus produtos (Tabela de Cobertura):", writer);
            for (int i=0; i < exp.getMinTermsList().size(); i++) {
                report += print("\n"+exp.getMinTermsList().get(i).getDecimalView()+" -", writer);
                for (int p=0; p < exp.getMinTermsList().get(i).getProductsList().size(); p++) {
                    report += print("\t\t"+exp.getMinTermsList().get(i).getProductsList().get(p), writer);
                    if (exp.getMinTermsList().get(i).getProductsList().get(p).length() < 8) {
                        report += print("\t", writer);
                    }
                }
            }
            
/////////////////////////////////////////////////////
            exp.essentialProductsToFinalList();
/////////////////////////////////////////////////////
    
            report += print("\n\nProdutos Essenciais:\n> ", writer);
            for (int i=0; i < exp.getFinalProductsList().size(); i++) {
                report += print(exp.getFinalProductsList().get(i)+"\t", writer);
            }
            
/////////////////////////////////////////////////////
            ArrayList<Integer> indexes = exp.getCandidateProductsIndexes();
/////////////////////////////////////////////////////
            report += print("\n", writer);
            for (int i=0; i < exp.getMinTermsList().size(); i++) {
                report += print("\nMintermo "+exp.getMinTermsList().get(i).getDecimalView()+" ", writer);
                if (exp.getMinTermsList().get(i).isIsCovered()) {
                    report += print("está coberto.", writer);
                }
                else {
                    report += print(" - ", writer);
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
    
            report += print("\n\nProdutos Finais:\n> ", writer);
            for (int i=0; i < exp.getFinalProductsList().size(); i++) {
                report += print(exp.getFinalProductsList().get(i)+"\t", writer);
            }
            
/////////////////////////////////////////////////////
            exp.setOptimizedExpression();
/////////////////////////////////////////////////////
            
            report += print("\n\nExpressão otimizada:\n", writer);
            report += print("> "+exp.getOptimizedExpression()+"\n", writer);
            
            report += print ("\nFim do resultado parcial.\n", writer);
            report += print("==================================================\n\n", writer);
            
            begin = end + 1;
            if (begin >= allExpressions.length()) {
                break;
            }
        }
        while (begin < allExpressions.length());
        
        report += print ("Fim dos resultados.\n", writer);
        report += print("==================================================\n", writer);
            
        writer.close();
        return report;
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
