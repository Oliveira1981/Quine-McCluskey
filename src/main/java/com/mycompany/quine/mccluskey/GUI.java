package com.mycompany.quine.mccluskey;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
//import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
    private boolean hasResult;
    private String errorMsg;
    private ArrayList<SumOfProducts> sopsList;
    
    public GUI(){
        inputFormat = "";
        expression  = "";
        hasResult   = false;
        errorMsg    = "";
        sopsList    = null;
    }
    
    public String getInputFormat(){
        return inputFormat;
    }
    
    public String getExpression(){
        return expression;
    }
    
    public void showWindow() throws Exception {
        
        /*
        File file = new File(
            "D:\\Users\\Rodrigo\\OneDrive - rzpy\\Documents\\Mestrado\\FerCAD\\Expressões\\NPN_5_QuineMcCluskey_FULL.txt");
        Scanner sc = new Scanner(file);
        PrintWriter writer = new PrintWriter("Quine-McCluskey Results.txt", "UTF-8");
        
        while (sc.hasNextLine()) {
            optimizeExpressions(sc.nextLine(), writer);
        }
        writer.close();
        System.exit(0);
        */
        
        String[] templates = {
            "",
            "2+4+6+8+9+10+12+13+15",
            "4+5+6+7+9+11+12+13+14+15",
            "!A*!B*!C*!D + !A*!B*!C*D + !A*B*!C*D + !A*B*C*!D + !A*B*C*D",
            "ABCD+!A!BCD+A!B!C!D+!ABCD",
            "A!BCD+!ABC!D+!ABCD+A!B!CD",
            "ab!c+a!bc!d+!ab",
            "abc+bcd",
            "1111+0011+1000+0111",
            "1111+0011+1010+0111",
            "111+11+101+01",
            "15+3+10+7",
            "0xB754"
        };
        
        String[] wichReport = {
            "Relatório Completo",
            "Tabela Verdade",
            "Mintermos e seus Produtos",
            "Produtos e seus Mintermos",
            "Tabela de Cobertura"
        };
        
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame myFrame = new JFrame("ROSA Binary");
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myFrame.setMinimumSize(new Dimension(750,520));
        
        GridBagLayout grid = new GridBagLayout();
        JPanel vPanel = new JPanel(grid);
        JTabbedPane tabbedPane = new JTabbedPane(1);
        tabbedPane.setName("main");
        tabbedPane.setForeground(new Color(1, 90, 190));
        Font fontTab = new Font("Segoe UI", Font.BOLD, 14);
        tabbedPane.setFont(fontTab);
        tabbedPane.add("Quine-McCluskey", vPanel);
        tabbedPane.getComponent(0).setBackground(new Color(170, 170, 170));
        tabbedPane.add("Fatoração", new JPanel());
        tabbedPane.getComponent(1).setBackground(new Color(170, 170, 170));
        tabbedPane.add("Composição Funcional", new JPanel());
        tabbedPane.getComponent(2).setBackground(new Color(170, 170, 170));
        tabbedPane.addKeyListener(this);
        tabbedPane.setFocusable(true);
        GridBagConstraints c = new GridBagConstraints();
        vPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        
        Font font = new Font("Segoe UI", Font.BOLD, 13);
        
        Color borderColor = new Color(0, 0, 0, 0);
        //Color borderColor = new Color(0, 0, 0, 255);
        
        JLabel space1 = new JLabel("   ");
        c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 11;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space1.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(space1, c);
        
        JLabel space3 = new JLabel("   ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 9;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space3.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(space3, c);
        
        JLabel labelExpressions = new JLabel("Expressão:");
        labelExpressions.setFont(font);
        labelExpressions.setForeground(new Color(1, 90, 190));
	c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
	c.gridy = 1;
	c.gridwidth = 6;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        labelExpressions.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(labelExpressions, c);
        
        JComboBox<String> comboExpressions = new JComboBox<>(templates);
        comboExpressions.setPreferredSize(new Dimension(350, 30));
        comboExpressions.setMinimumSize(new Dimension(350, 30));
        comboExpressions.setEditable(true);
        JTextField editor = (JTextField) comboExpressions.getEditor().getEditorComponent();
        editor.addKeyListener(this);
        Font fontExp = new Font("Segoe UI", Font.PLAIN, 12);
        comboExpressions.setFont(fontExp);
        comboExpressions.setFocusable(true);
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 2;
	c.gridwidth = 5;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	comboExpressions.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(comboExpressions, c);
        
        JLabel space5 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 6;
	c.gridy = 2;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	space5.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(space5, c);
        
        JButton okButton = new JButton("Executar");
        okButton.setPreferredSize(new Dimension(90, 32));
        okButton.setMinimumSize(new Dimension(90, 32));
        okButton.addKeyListener(this);
        okButton.setFocusable(true);
        okButton.setBackground(new Color(11, 188, 255));
        okButton.setForeground(new Color(1, 90, 190));
        okButton.setFont(font);
	c.fill = GridBagConstraints.NONE;
	c.gridx = 7;
	c.gridy = 2;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	//okButton.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(okButton, c);
        myFrame.getRootPane().setDefaultButton(okButton);
        
        JLabel space5A = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 8;
	c.gridy = 2;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	space5A.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(space5A, c);
        
        JButton rndButton = new JButton("Aleatória");
        rndButton.setPreferredSize(new Dimension(90, 32));
        rndButton.setMinimumSize(new Dimension(90, 32));
        rndButton.addKeyListener(this);
        rndButton.setFocusable(true);
        rndButton.setBackground(new Color(11, 188, 255));
        rndButton.setForeground(new Color(1, 90, 190));
        rndButton.setFont(font);
	c.fill = GridBagConstraints.NONE;
	c.gridx = 9;
	c.gridy = 2;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	//rndButton.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(rndButton, c);
        
        JLabel space6 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 3;
	c.gridwidth = 9;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
	space6.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(space6, c);
        
        JLabel resultLabel = new JLabel("Resultado:");
        resultLabel.setFont(font);
        resultLabel.setForeground(new Color(1, 90, 190));
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 4;
	c.gridwidth = 9;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	resultLabel.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(resultLabel, c);
        
        JTextArea textAreaResult = new JTextArea();
        textAreaResult.addKeyListener(this);
        textAreaResult.setFocusable(true);
        textAreaResult.setLineWrap(true);
        textAreaResult.setEditable(false);
        textAreaResult.setBackground(new Color(44, 44, 44));
        textAreaResult.setForeground(new Color(1, 188, 255));
        Font fontResult = new Font("Consolas", Font.PLAIN, 16);
        textAreaResult.setFont(fontResult);
        Insets mResult = new Insets(6, 6, 0, 0);
        textAreaResult.setMargin(mResult);
        
        JScrollPane jScrollResult = new JScrollPane(textAreaResult);
	c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
	c.gridy = 5;
	c.gridwidth = 9;
	c.gridheight = 1;//4
	c.weightx = 100.0;
        c.weighty = 0.01;
	jScrollResult.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(jScrollResult, c);
        
        JLabel space9 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 6;
	c.gridwidth = 9;
	c.gridheight = 1;
        c.weightx = 0.0;
	space9.setBorder(BorderFactory.createLineBorder(borderColor));
        c.weighty = 0.0;
        vPanel.add(space9, c);
        
        JComboBox<String> comboWichReport = new JComboBox<>(wichReport);
        comboWichReport.setPreferredSize(new Dimension(250, 30));
        comboWichReport.setMinimumSize(new Dimension(250, 30));
        comboWichReport.addKeyListener(this);
        comboWichReport.setFocusable(true);
        Font fontRep = new Font("Segoe UI", Font.BOLD, 12);
	comboWichReport.setFont(fontRep);
        comboWichReport.setForeground(new Color(1, 90, 190));
        c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 7;
	c.gridwidth = 5;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	comboWichReport.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(comboWichReport, c);
        
        JTextArea textAreaReport = new JTextArea();
        textAreaReport.addKeyListener(this);
        textAreaReport.setFocusable(true);
        textAreaReport.setLineWrap(true);
        textAreaReport.setEditable(false);
        textAreaReport.setBackground(new Color(44, 44, 44));
        textAreaReport.setForeground(new Color(1, 188, 255));
        Font fontReport = new Font("Consolas", Font.PLAIN, 16);
        textAreaReport.setFont(fontReport);
        Insets mReport = new Insets(10, 10, 10, 10);
        textAreaReport.setMargin(mReport);
        
        JScrollPane jScrollReport = new JScrollPane(textAreaReport);
	c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
	c.gridy = 8;//14
	c.gridwidth = 9;
	c.gridheight = 1;
	c.weightx = 100.0;
        c.weighty = 0.6;
	jScrollReport.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(jScrollReport, c);
        
        JLabel space4 = new JLabel("   ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 10;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 9;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space4.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(space4, c);
        
        JLabel space2 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 9;
	c.gridwidth = 11;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        Font fontBottom = new Font("Segoe UI", Font.PLAIN, 6);
        space2.setFont(fontBottom);
	space2.setBorder(BorderFactory.createLineBorder(borderColor));
        vPanel.add(space2, c);
        
        //myFrame.add(vPanel);
        myFrame.add(tabbedPane);
        myFrame.pack();
        myFrame.setSize(750, 680);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        myFrame.setLocation(dim.width/2-myFrame.getSize().width/2, dim.height/2-myFrame.getSize().height/2);
        myFrame.setVisible(true);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_TAB);
        
        okButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    hasResult = true;
                    optimizeExpressions((String) comboExpressions.getSelectedItem());
                    if (errorMsg.isEmpty()) {
                        String results;
                        if (sopsList.size() > 1) {
                            results = "";
                        }
                        else {
                            results = sopsList.get(0).getResult();
                        }
                        textAreaResult.setText(results);
                        textAreaReport.setText(reportText(comboWichReport));
                    }
                    else {
                        textAreaResult.setText(errorMsg);
                        textAreaReport.setText("-");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        rndButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    hasResult = true;
                    String gen = generateRandomExpression(30, 10); //(numberOfProducts, numberOfVars)
                    editor.setText(gen);
                    
                    /*sopsList =*/ optimizeExpressions(gen);
                    if (errorMsg.isEmpty()) {
                        String results;
                        if (sopsList.size() > 1) {
                            results = "";
                        }
                        else {
                            results = sopsList.get(0).getResult();
                        }
                        textAreaResult.setText(results);
                        textAreaReport.setText(reportText(comboWichReport));
                    }
                    else {
                        textAreaResult.setText(errorMsg);
                        textAreaReport.setText("-");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        comboWichReport.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (errorMsg.isEmpty()) {
                        if (hasResult) {
                            try {
                                textAreaReport.setText(reportText(comboWichReport));
                            } catch (UnsupportedEncodingException ex) {
                                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    else {
                        textAreaReport.setText("-");
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        editor.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    hasResult = true;
                    errorMsg = "";
                    /*sopsList =*/ optimizeExpressions((String) comboExpressions.getSelectedItem());
                    if (errorMsg.isEmpty()) {
                        String results;
                        if (sopsList.size() > 1) {
                            results = "";
                        }
                        else {
                            results = sopsList.get(0).getResult();
                        }
                        textAreaResult.setText(results);
                        textAreaReport.setText(reportText(comboWichReport));
                    }
                    else {
                        textAreaResult.setText(errorMsg);
                        textAreaReport.setText("-");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        inputFormat = String.valueOf(comboExpressions.getSelectedItem());
    }
    
    public String reportText(JComboBox comboWichReport) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("Quine-McCluskey Results.txt", "UTF-8");
        String out = "";
        switch ((String)comboWichReport.getSelectedItem()) {
            case "Relatório Completo" -> {
                try {
                    for (int r=0; r < sopsList.size(); r++) {
                        out += sopsList.get(r).getFullReport();
                        //print(sopsList.get(r).expression2hexadecimal(sopsList.get(r).getOriginalInputExpression())+"\n", writer);
                        print(sopsList.get(r).getOriginalInputExpression()+"; ", writer);
                        print(sopsList.get(r).getResult()+"\n", writer);
                    }
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case "Tabela Verdade" -> {
                if (sopsList.size() == 1) {
                    out += sopsList.get(0).getTruthTable();
                }
                else {
                    for (int r=0; r < sopsList.size(); r++) {
                        out += "Expressão de Entrada: \n> ";
                        out += sopsList.get(r).getOriginalInputExpression() + "\n";
                        out += sopsList.get(r).getTruthTable();
                        out += "\n==================================================\n";
                    }
                }
            }
            case "Mintermos e seus Produtos" -> {
                if (sopsList.size() == 1) {
                    out += sopsList.get(0).getProductsFromMinTerms();
                }
                else{
                    for (int r=0; r < sopsList.size(); r++) {
                        out += "Expressão de Entrada: \n> ";
                        out += sopsList.get(r).getOriginalInputExpression() + "\n";
                        out += sopsList.get(r).getProductsFromMinTerms();
                        out += "\n==================================================\n";
                    }
                }
            }
            case "Produtos e seus Mintermos" -> {
                if (sopsList.size() == 1) {
                    out += sopsList.get(0).getMinTermsFromProducts();
                }
                else {
                    for (int r=0; r < sopsList.size(); r++) {
                        out += "Expressão de Entrada: \n> ";
                        out += sopsList.get(r).getOriginalInputExpression() + "\n";
                        out += sopsList.get(r).getMinTermsFromProducts();
                        out += "\n==================================================\n";
                    }
                }
            }
            case "Tabela de Cobertura" -> {
                if (sopsList.size() == 1) {
                    out += sopsList.get(0).getCoveringTable();
                }
                else {
                    for (int r=0; r < sopsList.size(); r++) {
                        out += "Expressão de Entrada: \n> ";
                        out += sopsList.get(r).getOriginalInputExpression() + "\n";
                        out += sopsList.get(r).getCoveringTable();
                        out += "\n==================================================\n";
                    }
                }
            }
            default -> {
                try {
                    for (int r=0; r < sopsList.size(); r++) {
                        out = sopsList.get(r).getFullReport();
                    }
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //print (out, writer);
        writer.close();
        return out;
    }
    
    public /*ArrayList<SumOfProducts>*/void optimizeExpressions(String allExpressions) throws Exception {
        sopsList = new ArrayList<>();
        
        //inputFormat = getInputFormat();
        allExpressions = removeSpacesFromExpression(allExpressions);
        
        int begin = 0;
        int end;
        do {
            end = allExpressions.indexOf(';', begin);
            //end = -1; //ACEITAR APENAS UMA EXPRESSÃO
            if (end < 0) {
                end = allExpressions.length();
            }
            expression = allExpressions.substring(begin, end);
            sopsList.add(new SumOfProducts());
            int lastSOPIndex = sopsList.size()-1;
            if (!sopsList.get(lastSOPIndex).setExpression(expression)) {
                begin = end + 1;
                continue;
            }
            sopsList.get(lastSOPIndex).sortByOnesCount();
            sopsList.get(lastSOPIndex).mergePrimeImplicants(10);
            sopsList.get(lastSOPIndex).fillMinTermsList();
            sopsList.get(lastSOPIndex).fillTruthTable();
            sopsList.get(lastSOPIndex).essentialProductsToFinalList();
            
            ArrayList<Integer> indexes = sopsList.get(lastSOPIndex).getCandidateProductsIndexes();
            sopsList.get(lastSOPIndex).permute(indexes);
            sopsList.get(lastSOPIndex).completeFinalList();
            sopsList.get(lastSOPIndex).buildOptimizedExpression();
            
            begin = end + 1;
            if (begin >= allExpressions.length()) {
                break;
            }
        }
        while (begin < allExpressions.length());
        
        //return sopsList;
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
