package com.mycompany.quine.mccluskey;

import static com.mycompany.quine.mccluskey.Tools.generateRandomExpression;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.util.ArrayList;
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
    private String report;
    private boolean hasResult;
    private String errorMsg;
    private SumOfProducts exp;
    
    public GUI(){
        inputFormat = "";
        expression  = "";
        report      = "";
        hasResult   = false;
        errorMsg    = "";
        exp         = null;
    }
    
    public String getInputFormat(){
        return inputFormat;
    }
    
    public String getExpression(){
        return expression;
    }
    
    public void showWindow() throws Exception {
        String[] templates = {
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
            "Completo",
            "Tabela Verdade",
            "Implicantes Primos",
            "Tabela de Cobertura"
        };
        
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame myFrame = new JFrame("Quine-McCluskey");
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myFrame.setMinimumSize(new Dimension(700,580));
        
        GridBagLayout grid = new GridBagLayout();
        JPanel vPanel = new JPanel(grid);
        GridBagConstraints c = new GridBagConstraints();
        vPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        
        Font font = new Font("Segoe UI", Font.BOLD, 13);
        
        JLabel space1 = new JLabel("   ");
        c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 11;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space1, c);
        
        JLabel space3 = new JLabel("   ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 18;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space3, c);
        
        JLabel labelExpressions = new JLabel("Expressão:");
        labelExpressions.setFont(font);
        labelExpressions.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
	c.gridy = 1;
	c.gridwidth = 6;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        labelExpressions.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
	comboExpressions.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
	space5.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space5, c);
        
        JButton okButton = new JButton("Executar");
        okButton.setPreferredSize(new Dimension(90, 32));
        okButton.setMinimumSize(new Dimension(90, 32));
        okButton.addKeyListener(this);
        okButton.setFocusable(true);
        okButton.setBackground(new Color(11, 188, 255));
        okButton.setForeground(new Color(11, 111, 222));
        okButton.setFont(font);
	c.fill = GridBagConstraints.NONE;
	c.gridx = 7;
	c.gridy = 2;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	okButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
	space5A.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space5A, c);
        
        JButton rndButton = new JButton("Aleatória");
        rndButton.setPreferredSize(new Dimension(90, 32));
        rndButton.setMinimumSize(new Dimension(90, 32));
        rndButton.addKeyListener(this);
        rndButton.setFocusable(true);
        rndButton.setBackground(new Color(11, 188, 255));
        rndButton.setForeground(new Color(11, 111, 222));
        rndButton.setFont(font);
	c.fill = GridBagConstraints.NONE;
	c.gridx = 9;
	c.gridy = 2;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	rndButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(rndButton, c);
        
        /*JLabel space5B = new JLabel("5B");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 10;
	c.gridy = 2;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	space5B.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space5B, c);*/
        
        JLabel space6 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 3;
	c.gridwidth = 9;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
	space6.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space6, c);
        
        JLabel labelWichReport = new JLabel("Relatório a exibir:");
        labelWichReport.setFont(font);
        labelWichReport.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
	c.gridy = 4;
	c.gridwidth = 6;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	labelWichReport.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(labelWichReport, c);
        
        JComboBox<String> comboWichReport = new JComboBox<>(wichReport);
        comboWichReport.setPreferredSize(new Dimension(250, 30));
        comboWichReport.setMinimumSize(new Dimension(250, 30));
        comboWichReport.addKeyListener(this);
        comboWichReport.setFocusable(true);
        Font fontRep = new Font("Segoe UI", Font.PLAIN, 12);
	comboWichReport.setFont(fontRep);
        c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 5;
	c.gridwidth = 5;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	comboWichReport.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(comboWichReport, c);
        
        /*JLabel space7 = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 6;
	c.gridy = 5;
	c.gridwidth = 4;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	space7.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space7, c);
        
        JLabel space8 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 7;
	c.gridy = 5;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	space8.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space8, c);
        
        JLabel space8B = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 8;
	c.gridy = 5;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	space8B.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space8B, c);
        
        JLabel space8C = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 9;
	c.gridy = 5;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 100.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.EAST;
	space8C.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space8C, c);
        */
        JLabel space9 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 6;
	c.gridwidth = 9;
	c.gridheight = 1;
        c.weightx = 0.0;
	space9.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        c.weighty = 0.0;
        vPanel.add(space9, c);
        
        JLabel resultLabel = new JLabel("Resultado:");
        resultLabel.setFont(font);
        resultLabel.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 7;
	c.gridwidth = 9;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	resultLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
	c.gridy = 8;
	c.gridwidth = 9;
	c.gridheight = 6;//4
	c.weightx = 100.0;
        c.weighty = 0.01;//PARA VÁRIOS RESULTADOS: 0.1
	jScrollResult.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(jScrollResult, c);
        
        JLabel space10 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 14;//12
	c.gridwidth = 9;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space10.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space10, c);
        
        JLabel labelReport = new JLabel("Relatório:");
        labelReport.setFont(font);
        labelReport.setForeground(new Color(1, 111, 222));
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 15;//13
	c.gridwidth = 9;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	labelReport.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(labelReport, c);
        
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
	c.gridy = 16;//14
	c.gridwidth = 9;
	c.gridheight = 3;
	c.weightx = 100.0;
        c.weighty = 0.6;
	jScrollReport.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(jScrollReport, c);
        
        JLabel space4 = new JLabel("   ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 10;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 18;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space4.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space4, c);
        
        JLabel space2 = new JLabel(" ");
	c.fill = GridBagConstraints.NONE;
	c.gridx = 0;
	c.gridy = 19;
	c.gridwidth = 11;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        Font fontBottom = new Font("Segoe UI", Font.PLAIN, 6);
        space2.setFont(fontBottom);
	space2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        vPanel.add(space2, c);
        
        myFrame.add(vPanel);
        myFrame.pack();
        myFrame.setSize(750, 600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        myFrame.setLocation(dim.width/2-myFrame.getSize().width/2, dim.height/2-myFrame.getSize().height/2);
        myFrame.setVisible(true);
        
        okButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    hasResult = true;
                    exp = optimizeExpressions((String) comboExpressions.getSelectedItem());
                    if (errorMsg.isEmpty()) {
                        String results = exp.getOptimizedExpression();
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
                    String gen = generateRandomExpression(10, 4);
                    editor.setText(gen);
                    
                    exp = optimizeExpressions(gen);
                    if (errorMsg.isEmpty()) {
                        String results = exp.getOptimizedExpression();
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
                            textAreaReport.setText(reportText(comboWichReport));
                        }
                    }
                    else {
                        textAreaReport.setText("-");
                    }
                } catch (Exception ex) {
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
                    exp = optimizeExpressions((String) comboExpressions.getSelectedItem());
                    if (errorMsg.isEmpty()) {
                        String results = exp.getOptimizedExpression();
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
    
    public String reportText(JComboBox comboWichReport) {
        
        String out = "";
        switch ((String)comboWichReport.getSelectedItem()) {
            case "Completo"            -> out = report;
            case "Tabela Verdade"      -> {
                out += "Tabela Verdade";
                int tableSize = exp.getTruthTable().size();
                for (int i=0; i < tableSize; i++) {
                    out += "\n" + exp.getTruthTable().get(i);
                }
            }
            case "Implicantes Primos"  -> {
                //Colocar como método de sumOfProducts
                out += "Implicantes primos mesclados:\n";
                for(int i=0; i < exp.getProductsList().size(); i++) {
                    int q = 0;
                    for(; q < exp.getProductsList().get(i).getMinTermsList().size(); q++) {
                        out += "-"+exp.getProductsList().get(i).getMinTermsList().get(q);
                    }
                    if(q < 3) {
                        out += "-\t";
                    }
                    out += "\t";
                    out += exp.getProductsList().get(i).getBinaryView()+" \t";
                    out += exp.getProductsList().get(i).getLiteralView()+"\n";
                }
            }
            case "Tabela de Cobertura" -> {
                //Colocar como método de sumOfProducts
                out += "Tabela de Cobertura:";
                for (int i=0; i < exp.getMinTermsList().size(); i++) {
                    out += "\n"+exp.getMinTermsList().get(i).getDecimalView()+" -";
                    for (int p=0; p < exp.getMinTermsList().get(i).getProductsList().size(); p++) {
                        out += "\t\t"+exp.getMinTermsList().get(i).getProductsList().get(p);
                        if (exp.getMinTermsList().get(i).getProductsList().get(p).length() < 8) {
                            out += "\t";
                        }
                    }
                }
            }
            default -> out = report;
        }
        return out;
    }
    
    public SumOfProducts optimizeExpressions(String allExpressions) throws Exception {
        PrintWriter writer = new PrintWriter("Quine-McCluskey Results.txt", "UTF-8");
        String results = "";
        report  = "";
        SumOfProducts exp = null;
        
        //inputFormat = getInputFormat();
        allExpressions = removeSpacesFromExpression(allExpressions);
        
        int begin = 0;
        int end;
        do {
            //end = allExpressions.indexOf(';', begin);
            end = -1; //ACEITAR APENAS UMA EXPRESSÃO
            if (end < 0) {
                end = allExpressions.length();
            }
            expression = allExpressions.substring(begin, end);
            
            inputFormat = detectInputFormat(expression);
            if (inputFormat.length() == 0  ||
                inputFormat.equals("ERRO") ||
               (inputFormat.equals("Literal") && hasDuplicate(expression))) {
                
                errorMsg = "Expressão inconsistente.";
                //exp = new SumOfProducts(errorMsg);
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
            exp = new SumOfProducts(inputFormat, expression);
/////////////////////////////////////////////////////

            if (inputFormat.equals("Literal")) {
                report += print("\nQuantidade de Literais na entrada:\n", writer);
                report += print("> " + numberOfLiterals(expression) + "\n", writer);
            }
            
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
                errorMsg = "[VDD]";
                //exp = new SumOfProducts(errorMsg);
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
            //exp.sortMinTermsList();
            exp.fillTruthTable();
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
            exp.buildOptimizedExpression();
/////////////////////////////////////////////////////
            
            report += print("\n\nQuantidade de Literais na saída:\n", writer);
            report += print("> " + numberOfLiterals(exp.getOptimizedExpression()) + "\n", writer);
            
            report += print("\nExpressão otimizada:\n", writer);
            report += print("> "+exp.getOptimizedExpression()+"\n", writer);
            
            report += print ("\nFim do resultado parcial.\n", writer);
            report += print("==================================================\n\n", writer);
            
            if (results.length() > 0 ) {
                results += ";\n";
            }
            results += exp.getOptimizedExpression();
            
            begin = end + 1;
            if (begin >= allExpressions.length()) {
                break;
            }
        }
        while (begin < allExpressions.length());
        //exp.fillTruthTable();
        report += print ("Fim dos resultados.\n", writer);
        report += print("==================================================\n", writer);
        
        writer.close();
        //return results;
        return exp;
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
