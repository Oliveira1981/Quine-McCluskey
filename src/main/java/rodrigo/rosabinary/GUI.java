package rodrigo.rosabinary;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import rodrigo.rosabinary.mazerouter.*;
import rodrigo.rosabinary.quinemccluskey.*;

/**
*
 * @author Rodrigo da Rosa
 */
public final class GUI /*extends Tools*/ implements KeyListener {
    
    private String inputFormat;
    private String expression;
    private int numVars;
    private boolean hasResult;
    private String errorMsg;
    private ArrayList<SumOfProducts> sopsList;
    //private PrintWriter outputFile;
    private JTabbedPane tabbedPane;
    private MazeRouter mazeRouter;
    
    public GUI(){
        inputFormat = "";
        expression  = "";
        numVars     = 0;
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
    
    public String selectFile() throws
        FileNotFoundException,
        UnsupportedEncodingException,
        Exception {
        
        FileDialog dialog = new FileDialog((Frame)null, "Selecione um arquivo");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String inputFilePath = dialog.getDirectory();
        String inputFileName = dialog.getFile();
        dialog.dispose();
        if (inputFilePath == null || inputFileName == null) {
            return "";
        }
        return inputFilePath + inputFileName;
    }
    /*
    public int readFromFile(String filePath, int startLine, int endLine) throws Exception {
        
        setFileToWrite("Quine-McCluskey Results.txt");
        File selectedFile = new File(filePath);
        if(!selectedFile.exists()){
            printt("\nNo file selected.\n");
            return 1;
        }
        Scanner sc = new Scanner(selectedFile);
        
        int line = 1;
        if (line < startLine) {
            printt("\nSkipping line(s)...\n");
        }
        while (line < startLine) {
            sc.nextLine();
            line++;
        }
        printt("\nReading...");
        
        if (endLine == -1) { // LER ATÉ O FINAL DO ARQUIVO
            while (sc.hasNext()) {
                optimizeExpressions(sc.nextLine(), numVars);
            }
        }
        else {
            while (line <= endLine) {
                //printt("\nLine " + line + "\t"); //LEVA MUITO MAIS TEMPO SE FICAR MOSTRANDO A LINHA
                optimizeExpressions(sc.nextLine(), numVars);
                line++;
            }
        }
        outputFile.close();
        return 0;
    }
    */
    public void setFileToWrite(String outputFileName) throws
        FileNotFoundException,
        UnsupportedEncodingException {
        //outputFile = new PrintWriter(outputFileName);
    }
    
    public void openOutputFile(String outputFileName)  throws
        FileNotFoundException,
        UnsupportedEncodingException {
        
        File fileOut = new File(
            outputFileName
        );
        try {
            Desktop.getDesktop().open(fileOut);
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showMainWindow() throws Exception {
        
        String[] wichInput = {
            "Carregar expressões de um arquivo...",
            "Digitar expressão",
            "Gerar expressão aleatória"
        };
        
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
            "Relatório Básico",
            "Tabela Verdade",
            "Mintermos e seus Produtos",
            "Produtos e seus Mintermos",
            "Tabela de Cobertura"
        };
        
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        JFrame mainFrame = new JFrame("ROSA Binary");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        GridBagLayout grid = new GridBagLayout();
        
        tabbedPane = new JTabbedPane(1);
        tabbedPane.setName("mainTabbedPane");
        tabbedPane.setForeground(new Color(30, 130, 230));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JPanel quineMcPanel = new JPanel(grid);
        tabbedPane.add("Quine-McCluskey", quineMcPanel);
        
        mazeRouter = new MazeRouter();
        tabbedPane.add("Maze Router", mazeRouter.mazeRouterPanel());
        
        tabbedPane.add("Fatoração", new JPanel());
        
        tabbedPane.add("Composição Funcional", new JPanel());
        
        tabbedPane.addKeyListener(this);
        tabbedPane.setFocusable(true);
        
        GridBagConstraints c = new GridBagConstraints();
        
        quineMcPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        Font fontDefault = new Font("Segoe UI", Font.BOLD, 13);
        Color borderColor = new Color(0, 0, 0, 0);
        
        JLabel space1 = new JLabel("   ");
        c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 15;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space1.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space1, c);
        
        JLabel space3 = new JLabel("   ");
	c.fill = GridBagConstraints.VERTICAL;
	c.gridx = 0;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 9;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space3.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space3, c);
        
        JComboBox<String> comboWichInput = new JComboBox<>(wichInput);
        comboWichInput.setSelectedIndex(1); // Digitar
        comboWichInput.setPreferredSize(new Dimension(220, 30));
        comboWichInput.setMinimumSize(new Dimension(220, 30));
        comboWichInput.addKeyListener(this);
        comboWichInput.setFocusable(true);
	comboWichInput.setFont(new Font("Segoe UI", Font.BOLD, 12));
        //comboWichInput.setForeground(new Color(1, 90, 190));
        comboWichInput.setForeground(new Color(30, 130, 230));
        c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	//comboWichInput.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(comboWichInput, c);
        
        JLabel space4a = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 2;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	space4a.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space4a, c);
        
        JCheckBox checkReadEntireFile = new JCheckBox("Inteiro");
        checkReadEntireFile.setFont(fontDefault);
        //labelInteiro.setForeground(new Color(1, 90, 190));
        checkReadEntireFile.setForeground(new Color(30, 130, 230));
        checkReadEntireFile.addKeyListener(this);
        checkReadEntireFile.setFocusable(true);
        checkReadEntireFile.setSelected(true);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        checkReadEntireFile.setBorder(BorderFactory.createLineBorder(borderColor));
        //vPanel.add(checkReadEntireFile, c);
        
        JLabel labelStartLine = new JLabel("     Linha inicial: ");
        labelStartLine.setFont(fontDefault);
        //labelStartLine.setForeground(new Color(30, 130, 230));
        labelStartLine.setForeground(new Color(110, 110, 110));
        labelStartLine.addKeyListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        labelStartLine.setBorder(BorderFactory.createLineBorder(borderColor));
        //vPanel.add(labelStartLine, c);
        
        JTextField textStartLine = new JTextField();
        //textStartLine.setForeground(new Color(30, 130, 230));
        textStartLine.setPreferredSize(new Dimension(55, 20));
        textStartLine.setMinimumSize(new Dimension(55, 20));
        textStartLine.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textStartLine.addKeyListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        //textStartLine.setBorder(BorderFactory.createLineBorder(borderColor));
        //vPanel.add(textStartLine, c);
        
        JLabel labelEndLine = new JLabel("     Linha final: ");
        labelEndLine.setFont(fontDefault);
        //labelEndLine.setForeground(new Color(30, 130, 230));
        labelEndLine.setForeground(new Color(110, 110, 110));
        labelEndLine.addKeyListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 6;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        labelEndLine.setBorder(BorderFactory.createLineBorder(borderColor));
        //vPanel.add(labelEndLine, c);
        
        JTextField textEndLine = new JTextField();
        //textEndLine.setForeground(new Color(1, 90, 190));
        //textEndLine.setForeground(new Color(30, 130, 230));
        textEndLine.setPreferredSize(new Dimension(55, 20));
        textEndLine.setMinimumSize(new Dimension(55, 20));
        textEndLine.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textEndLine.addKeyListener(this);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 7;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        //textEndLine.setBorder(BorderFactory.createLineBorder(borderColor));
        //vPanel.add(textEndLine, c);
        
        JLabel labelVariables = new JLabel("   Número de variáveis: Auto");
        labelVariables.setFont(fontDefault);
        //labelExpressions.setForeground(new Color(1, 90, 190));
        labelVariables.setForeground(new Color(30, 130, 230));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 11;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        labelVariables.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(labelVariables, c);
        
        JLabel space3a = new JLabel(" ");
        //space3a.setVisible(false);
        space3a.setFont(new Font("SEGOE UI", Font.PLAIN, 1));
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 2;
	c.gridwidth = 13;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space3a.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space3a, c);
        
        JComboBox<String> comboExpressions = new JComboBox<>(templates);
        comboExpressions.setPreferredSize(new Dimension(500, 30));
        comboExpressions.setMinimumSize(new Dimension(500, 30));
        comboExpressions.setEditable(true);
        JTextField editor = (JTextField) comboExpressions.getEditor().getEditorComponent();
        editor.addKeyListener(this);
        comboExpressions.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboExpressions.setFocusable(true);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 3;
	c.gridwidth = 7;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	//comboExpressions.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(comboExpressions, c);
        
        JLabel space5 = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 8;
	c.gridy = 3;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	space5.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space5, c);
        
        JButton okButton = new JButton("Executar");
        okButton.setPreferredSize(new Dimension(90, 30));
        okButton.setMinimumSize(new Dimension(90, 30));
        okButton.addKeyListener(this);
        okButton.setFocusable(true);
        okButton.setBackground(new Color(30, 50, 100));
        okButton.setForeground(new Color(50, 150, 250));
        //okButton.setForeground(new Color(30, 130, 230));
        okButton.setFont(fontDefault);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 9;
	c.gridy = 3;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	//okButton.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(okButton, c);
        mainFrame.getRootPane().setDefaultButton(okButton);
        
        JLabel space5B = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 10;
	c.gridy = 3;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	space5B.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space5B, c);
        
        Dictionary<Integer, Component> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("Auto"));
        labelTable.put(4, new JLabel("4"));
        labelTable.put(8, new JLabel("8"));
        labelTable.put(12, new JLabel("12"));
        labelTable.put(16, new JLabel("16"));
        
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 16, 0); // min, max, inicial
        slider.addKeyListener(this);
        //slider.addChangeListener(this);
        slider.setMajorTickSpacing(4);
        slider.setMinorTickSpacing(1);
        //slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        //slider.setPaintLabels(true);  
        slider.setMinimumSize(new Dimension(200, 30));
        slider.setLabelTable(labelTable);
	c.fill = GridBagConstraints.VERTICAL;
	c.gridx = 11;
	c.gridy = 3;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
        slider.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(slider, c);
        
        JLabel space6 = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 4;
	c.gridwidth = 13;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space6.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space6, c);
        
        JLabel resultLabel = new JLabel("Expressão Minimizada:");
        resultLabel.setFont(fontDefault);
        //resultLabel.setForeground(new Color(1, 90, 190));
        resultLabel.setForeground(new Color(30, 130, 230));
	c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 5;
	c.gridwidth = 13;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	resultLabel.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(resultLabel, c);
        
        JTextArea textAreaResult = new JTextArea();
        textAreaResult.addKeyListener(this);
        textAreaResult.setFocusable(true);
        textAreaResult.setLineWrap(true);
        textAreaResult.setEditable(false);
        textAreaResult.setBackground(new Color(44, 44, 44));
        //textAreaResult.setForeground(new Color(1, 188, 255));
        textAreaResult.setForeground(new Color(50, 150, 250));
        textAreaResult.setFont(new Font("Consolas", Font.PLAIN, 16));
        Insets mResult = new Insets(12, 8, 4, 6);
        textAreaResult.setMargin(mResult);
	c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
	c.gridy = 6;
	c.gridwidth = 13;
	c.gridheight = 1;
	c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	//textAreaResult.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(textAreaResult, c);
        
        JLabel space9 = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 1;
	c.gridy = 7;
	c.gridwidth = 13;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space9.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space9, c);
        
        JComboBox<String> comboWichReport = new JComboBox<>(wichReport);
        comboWichReport.setPreferredSize(new Dimension(250, 30));
        comboWichReport.setMinimumSize(new Dimension(250, 30));
        comboWichReport.addKeyListener(this);
        comboWichReport.setFocusable(true);
	comboWichReport.setFont(new Font("Segoe UI", Font.BOLD, 12));
        //comboWichReport.setForeground(new Color(1, 90, 190));
        comboWichReport.setForeground(new Color(30, 130, 230));
        c.fill = GridBagConstraints.NONE;
	c.gridx = 1;
	c.gridy = 8;
	c.gridwidth = 1;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	//comboWichReport.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(comboWichReport, c);
        
        JLabel labelResultsFromFile = new JLabel(" Resultados:");
        labelResultsFromFile.setPreferredSize(new Dimension(250, 30));
     	labelResultsFromFile.setFont(new Font("Segoe UI", Font.BOLD, 14));
        //labelResultsFromFile.setForeground(new Color(1, 90, 190));
        labelResultsFromFile.setForeground(new Color(30, 130, 230));
        
        JTextField labelTime = new JTextField(" ");
        labelTime.setEditable(false);
        labelTime.setMinimumSize(new Dimension(250, 30));
        labelTime.setFont(new Font("Consolas", Font.PLAIN, 14));
        labelTime.setForeground(new Color(30, 130, 230));
        labelTime.setHorizontalAlignment(SwingConstants.RIGHT);
        c.fill = GridBagConstraints.BOTH;
	c.gridx = 11;
	c.gridy = 8;
	c.gridwidth = 3;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.WEST;
	labelTime.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(labelTime, c);
        
        JTextArea textAreaReport = new JTextArea();
        textAreaReport.addKeyListener(this);
        textAreaReport.setFocusable(true);
        textAreaReport.setLineWrap(true);
        textAreaReport.setEditable(false);
        textAreaReport.setBackground(new Color(44, 44, 44));
        //textAreaReport.setForeground(new Color(1, 188, 255));
        textAreaReport.setForeground(new Color(50, 150, 250));
        Font fontReport = new Font("Consolas", Font.PLAIN, 16);
        textAreaReport.setFont(fontReport);
        Insets mReport = new Insets(10, 10, 10, 10);
        textAreaReport.setMargin(mReport);
        
        JScrollPane jScrollReport = new JScrollPane(textAreaReport);
	c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
	c.gridy = 9;
	c.gridwidth = 13;
	c.gridheight = 1;
	c.weightx = 100.0;
        c.weighty = 0.6;
	jScrollReport.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(jScrollReport, c);
        
        JLabel space4 = new JLabel("   ");
	c.fill = GridBagConstraints.VERTICAL;
	c.gridx = 14;
	c.gridy = 1;
	c.gridwidth = 1;
	c.gridheight = 9;
        c.weightx = 0.0;
        c.weighty = 0.0;
	space4.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space4, c);
        
        JLabel space2 = new JLabel(" ");
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 10;
	c.gridwidth = 15;
	c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        space2.setFont(new Font("Segoe UI", Font.PLAIN, 6));
	space2.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space2, c);
        
        mainFrame.add(tabbedPane);
        mainFrame.pack();
        mainFrame.setMinimumSize(new Dimension(1050,600));
        mainFrame.setSize(new Dimension(1050,700));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
        mainFrame.setVisible(true);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(labelVariables);
        
        /*tabbedPane.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex()==1) {
                    //tabbedPane.setComponentAt(1, mazeRouterPanel());
                }
            }
        });
        */
        comboWichInput.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                errorMsg = "";
                switch (comboWichInput.getSelectedIndex()) {
                    case 1 -> { // input: digitar
                        quineMcPanel.remove(checkReadEntireFile);
                        quineMcPanel.remove(labelStartLine);
                        quineMcPanel.remove(textStartLine);
                        quineMcPanel.remove(labelEndLine);
                        quineMcPanel.remove(textEndLine);
                        quineMcPanel.remove(labelResultsFromFile);
                        
                        c.fill = GridBagConstraints.NONE;
                        c.gridx = 1;
                        c.gridy = 8;
                        c.gridwidth = 5;
                        c.gridheight = 1;
                        c.weightx = 0.0;
                        c.weighty = 0.0;
                        c.anchor = GridBagConstraints.WEST;
                        quineMcPanel.add(comboWichReport, c);
                        
                        resultLabel.setVisible(true);
                        textAreaResult.setVisible(true);
                        mainFrame.repaint();
                    }
                    case 2 -> { // input: aleatória
                        quineMcPanel.remove(checkReadEntireFile);
                        quineMcPanel.remove(labelStartLine);
                        quineMcPanel.remove(textStartLine);
                        quineMcPanel.remove(labelEndLine);
                        quineMcPanel.remove(textEndLine);
                        quineMcPanel.remove(labelResultsFromFile);
                        
                        c.fill = GridBagConstraints.NONE;
                        c.gridx = 1;
                        c.gridy = 8;
                        c.gridwidth = 5;
                        c.gridheight = 1;
                        c.weightx = 0.0;
                        c.weighty = 0.0;
                        c.anchor = GridBagConstraints.WEST;
                        quineMcPanel.add(comboWichReport, c);
                        
                        resultLabel.setVisible(true);
                        textAreaResult.setVisible(true);
                        mainFrame.repaint();
                        hasResult = true;
                        String gen;
                        int vars = slider.getValue();
                        if (vars == 0) {
                            gen = Tools.generateRandomExpression(8, 4);
                        }
                        else {
                            gen = Tools.generateRandomExpression(
                                vars*2, //numberOfProducts 
                                vars    //numberOfVars
                            );
                        }
                        editor.setText(gen);
                    }
                    case 0 -> { // input: arquivo
                        
                        checkReadEntireFile.setForeground(new Color(30, 130, 230));
                        checkReadEntireFile.setFocusable(true);
                        checkReadEntireFile.setSelected(true);
                        c.fill = GridBagConstraints.HORIZONTAL;
                        c.gridx = 3;
                	c.gridy = 1;
                        c.gridwidth = 1;
                        c.gridheight = 1;
                        c.weightx = 0.0;
                        c.weighty = 0.0;
                        c.anchor = GridBagConstraints.WEST;
                        checkReadEntireFile.setSelected(true);
                        quineMcPanel.add(checkReadEntireFile, c);
                        
                        labelStartLine.setForeground(new Color(110, 110, 110));
                        c.fill = GridBagConstraints.HORIZONTAL;
                        c.gridx = 4;
                        c.gridy = 1;
                        c.gridwidth = 1;
                        c.gridheight = 1;
                        c.weightx = 0.0;
                        c.weighty = 0.0;
                        c.anchor = GridBagConstraints.WEST;
                        quineMcPanel.add(labelStartLine, c);
                        
                        textStartLine.setText("");
                        c.fill = GridBagConstraints.HORIZONTAL;
                        c.gridx = 5;
                        c.gridy = 1;
                        c.gridwidth = 1;
                        c.gridheight = 1;
                        c.weightx = 0.0;
                        c.weighty = 0.0;
                        c.anchor = GridBagConstraints.WEST;
                        textStartLine.setEnabled(false);
                        textStartLine.setEditable(false);
                        quineMcPanel.add(textStartLine, c);
                        
                        labelEndLine.setForeground(new Color(110, 110, 110));
                        c.fill = GridBagConstraints.HORIZONTAL;
                        c.gridx = 6;
                        c.gridy = 1;
                        c.gridwidth = 1;
                        c.gridheight = 1;
                        c.weightx = 0.0;
                        c.weighty = 0.0;
                        c.anchor = GridBagConstraints.WEST;
                        quineMcPanel.add(labelEndLine, c);
                        
                        textEndLine.setText("");
                        c.fill = GridBagConstraints.HORIZONTAL;
                        c.gridx = 7;
                        c.gridy = 1;
                        c.gridwidth = 1;
                        c.gridheight = 1;
                        c.weightx = 0.0;
                        c.weighty = 0.0;
                        c.anchor = GridBagConstraints.WEST;
                        textEndLine.setEnabled(false);
                        textEndLine.setEditable(false);
                        quineMcPanel.add(textEndLine, c);
                        
                        quineMcPanel.remove(comboWichReport);
                        c.fill = GridBagConstraints.NONE;
                	c.gridx = 1;
                	c.gridy = 8;
                	c.gridwidth = 5;
                        c.gridheight = 1;
                        c.weightx = 0.0;
                        c.weighty = 0.0;
                        c.anchor = GridBagConstraints.WEST;
                        //labelResultsFromFile.setBorder(BorderFactory.createLineBorder(borderColor));
                        quineMcPanel.add(labelResultsFromFile, c);
                            
                        resultLabel.setVisible(false);
                        textAreaResult.setVisible(false);
                        mainFrame.repaint();
                        
                        try {
                            editor.setText(selectFile());
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    default -> {
                        return;
                    }
                }
            }
        });
        
        checkReadEntireFile.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkReadEntireFile.isSelected()) {
                    checkReadEntireFile.setForeground(new Color(110, 110, 110));
                    labelStartLine.setForeground(new Color(30, 130, 230));
                    labelEndLine.setForeground(new Color(30, 130, 230));
                    textStartLine.setEnabled(true);
                    textStartLine.setEditable(true);
                    textEndLine.setEnabled(true);
                    textEndLine.setEditable(true);
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(checkReadEntireFile);
                }
                else {
                    checkReadEntireFile.setForeground(new Color(30, 130, 230));
                    labelStartLine.setForeground(new Color(110, 110, 110));
                    labelEndLine.setForeground(new Color(110, 110, 110));
                    textStartLine.setEnabled(!true);
                    textStartLine.setEditable(!true);
                    textStartLine.setText("");
                    textEndLine.setEnabled(!true);
                    textEndLine.setEditable(!true);
                    textEndLine.setText("");
                }
                
            }
        });
        
        MouseListener mouseListenerStartLine = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkReadEntireFile.setSelected(false);
                checkReadEntireFile.setForeground(new Color(110, 110, 110));
                labelStartLine.setForeground(new Color(30, 130, 230));
                labelEndLine.setForeground(new Color(30, 130, 230));
                textStartLine.setEnabled(true);
                textStartLine.setEditable(true);
                textEndLine.setEnabled(true);
                textEndLine.setEditable(true);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(checkReadEntireFile);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        labelStartLine.addMouseListener(mouseListenerStartLine);
        textStartLine.addMouseListener(mouseListenerStartLine);
        
        MouseListener mouseListenerEndLine = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (checkReadEntireFile.isSelected()) {
                    textStartLine.setText("1");
                }
                checkReadEntireFile.setSelected(false);
                checkReadEntireFile.setForeground(new Color(110, 110, 110));
                labelStartLine.setForeground(new Color(30, 130, 230));
                labelEndLine.setForeground(new Color(30, 130, 230));
                textStartLine.setEnabled(true);
                textStartLine.setEditable(true);
                textEndLine.setEnabled(true);
                textEndLine.setEditable(true);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(labelEndLine);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        labelEndLine.addMouseListener(mouseListenerEndLine);
        textEndLine.addMouseListener(mouseListenerEndLine);
        
        slider.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                numVars = (int)source.getValue();
                quineMcPanel.remove(labelVariables);
                if (numVars == 0) {
                    labelVariables.setText("   Número de variáveis: Auto");
                }
                else {
                    labelVariables.setText("   Número de variáveis: " + numVars);
                }
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 11;
                c.gridy = 1;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.weightx = 0.0;
                c.weighty = 0.0;
                c.anchor = GridBagConstraints.WEST;
                quineMcPanel.add(labelVariables, c);
                quineMcPanel.repaint();
            }
        });
        
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                labelTime.setText("Tempo:        ");
                labelTime.update(labelTime.getGraphics());
                long startTime = System.nanoTime();
                try {
                    //openOutputFile("Quine-McCluskey Results.txt");
                    //setFileToWrite("Quine-McCluskey Results.txt");
                    hasResult = true;
                    switch (comboWichInput.getSelectedIndex()) {
                        case 1 -> { // input: digitar
                            optimizeExpressions(
                                (String) comboExpressions.getSelectedItem(),
                                (int) slider.getValue()/*,
                                outputFile*/
                            );
                            textAreaReport.setFont(fontReport);
                        }
                        case 2 -> { // input: aleatória
                            optimizeExpressions(
                                editor.getText(),
                                (int) slider.getValue()/*,
                                outputFile*/
                            );
                            textAreaReport.setFont(fontReport);
                        }
                        case 0 -> { // input: arquivo
                            int startLine = 1;
                            int endLine = -1;
                            if (!checkReadEntireFile.isSelected()) {
                                if (textStartLine.getText().equals("")) {
                                    startLine = 1;
                                }
                                else {
                                    startLine = Integer.parseInt(textStartLine.getText());
                                }
                                if (textEndLine.getText().equals("")) {
                                    endLine = -1;
                                }
                                else {
                                    endLine = Integer.parseInt(textEndLine.getText());
                                }
                            }
                            if ((endLine != -1) && (endLine < startLine)) {
                                errorMsg = "Linha final menor que linha inicial.";
                                textAreaReport.setText(errorMsg);
                                return;
                            }
                            //setFileToWrite("Quine-McCluskey Results.txt");
                            File selectedFile = new File(editor.getText());
                            if(!selectedFile.exists()){
                                Tools.printt("\nNo file selected.\n");
                                errorMsg = "Nenhum arquivo selecionado.";
                                textAreaReport.setText(errorMsg);
                                return;
                            }
                            Scanner sc = new Scanner(selectedFile);
                            
                            int line = 1;
                            //if (line < startLine) {
                            //    printt("\nSkipping line(s)...\n");
                            //}
                            while (line < startLine) {
                                if (sc.hasNext()) {
                                    sc.nextLine();
                                    line++;
                                }
                                else {
                                    break;
                                }
                            }
                            if (!sc.hasNext()) {
                                errorMsg = "Linha inicial além do fim do arquivo.";
                                textAreaReport.setText(errorMsg);
                                return;
                            }
                            //printt("\nReading...");
                            
                            textAreaReport.setFont(new Font("Consolas", Font.PLAIN, 14));
                            textAreaReport.setText("");
                            int count = line;
                            ArrayList<String> fullReport = new ArrayList<>();
                            if (endLine == -1) { // LER ATÉ O FINAL DO ARQUIVO
                                while (sc.hasNext()) {
                                    optimizeExpressions(sc.nextLine(), numVars/*, outputFile*/);
                                    //textAreaReport.append("\n" + count + "\t");
                                    fullReport.add("\n" + count + "\t");
                                    count++;
                                    
                                    String result = sopsList.get(0).getResult();
                                    String formattedResult = result + ' ';
                                    for (int c = result.length(); c < 80; c++) {
                                        formattedResult = formattedResult + '.';
                                    }
                                    //textAreaReport.append(formattedResult + " ");
                                    fullReport.add(formattedResult + " ");
                                    
                                    String hexa = sopsList.get(0).expression2hexadecimal(sopsList.get(0).getResult());
                                    String formattedHexa = hexa + ' ';
                                    for (int c = hexa.length(); c < 20; c++) {
                                        formattedHexa = formattedHexa + '.';
                                    }
                                    //textAreaReport.append(formattedHexa + " ");
                                    fullReport.add(formattedHexa + " ");
                                    
                                    //String numLit = String.valueOf(SumOfProducts.numberOfLiterals(sopsList.get(0).getResult(), sopsList.get(0).getNumberOfVars(), sopsList.get(0).getNumberOfProducts()));
                                    String numLit = String.valueOf(Tools.numberOfLiterals(sopsList.get(0).getResult(), sopsList.get(0).getNumberOfVars(), sopsList.get(0).getNumberOfProducts()));String formattedNumLit = "";
                                    for (int c = 0; c < (3 - numLit.length()); c++) {
                                        formattedNumLit = formattedNumLit + ' ';
                                    }
                                    formattedNumLit = formattedNumLit + numLit;
                                    //textAreaReport.append(formattedNumLit + "\n");
                                    fullReport.add(formattedNumLit + "\n");
                                    
                                    //Exibe atualização a cada X iterações
                                    if (Math.floorMod(count, 500) == 0) {
                                        textAreaReport.setText(
                                            "ÍNDICE\t" +
                                            "EXPRESSÃO MINIMIZADA\n"
                                        );
                                        textAreaReport.append("\n" + count + "\t" + result + " ...");
                                        textAreaReport.update(textAreaReport.getGraphics());
                                        labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
                                        labelTime.update(labelTime.getGraphics());
                                    }
                                }
                            }
                            else {
                                while (line <= endLine) {
                                    optimizeExpressions(sc.nextLine(), numVars/*, outputFile*/);
                                    //textAreaReport.append("\n" + count + "\t");
                                    fullReport.add("\n" + count + "\t");
                                    count++;
                                    
                                    String result = sopsList.get(0).getResult();
                                    String formattedResult = result + ' ';
                                    for (int c = result.length(); c < 80; c++) {
                                        formattedResult = formattedResult + '.';
                                    }
                                    //textAreaReport.append(formattedResult + " ");
                                    fullReport.add(formattedResult + " ");
                                    
                                    String hexa = sopsList.get(0).expression2hexadecimal(sopsList.get(0).getResult());
                                    String formattedHexa = hexa + ' ';
                                    for (int c = hexa.length(); c < 20; c++) {
                                        formattedHexa = formattedHexa + '.';
                                    }
                                    //textAreaReport.append(formattedHexa + " ");
                                    fullReport.add(formattedHexa + " ");
                                    
                                    //String numLit = String.valueOf(SumOfProducts.numberOfLiterals(sopsList.get(0).getResult(), sopsList.get(0).getNumberOfVars(), sopsList.get(0).getNumberOfProducts()));
                                    String numLit = String.valueOf(Tools.numberOfLiterals(sopsList.get(0).getResult(), sopsList.get(0).getNumberOfVars(), sopsList.get(0).getNumberOfProducts()));
                                    String formattedNumLit = "";
                                    for (int c = 0; c < (3 - numLit.length()); c++) {
                                        formattedNumLit = formattedNumLit + ' ';
                                    }
                                    formattedNumLit = formattedNumLit + numLit;
                                    //textAreaReport.append(formattedNumLit + "\n");
                                    fullReport.add(formattedNumLit + "\n");
                                    
                                    //Exibe atualização a cada X iterações
                                    if (Math.floorMod(count, 500) == 0) {
                                        textAreaReport.setText(
                                            "ÍNDICE\t" +
                                            "EXPRESSÃO MINIMIZADA\n"
                                        );
                                        textAreaReport.append("\n" + count + "\t" + result + " ...");
                                        textAreaReport.update(textAreaReport.getGraphics());
                                        labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
                                        labelTime.update(labelTime.getGraphics());
                                    }
                                    line++;
                                }
                            }
                            textAreaReport.append("\n\nFinalizando...");
                            textAreaReport.update(textAreaReport.getGraphics());
                            //mainFrame.setMinimumSize(new Dimension(1020,520));
                            textAreaReport.setText(
                                "ÍNDICE\t" +
                                "EXPRESSÃO MINIMIZADA" +
                                "                " +
                                "                " +
                                "                " +
                                "              " +
                                "CÓDIGO HEXADECIMAL" +
                                "    " +
                                "LIT.\n"
                            );
                            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(labelTime);
                            textAreaReport.setCaretColor(new Color(30, 120, 255));
                            textAreaReport.getCaret().setVisible(true);
                            for (int a = 0; a < fullReport.size(); a++) {
                                textAreaReport.append(fullReport.get(a));
                            }
                            textAreaReport.setCaretPosition(0);
                            quineMcPanel.repaint();
                            //outputFile.close();
                            /*if (readFromFile(editor.getText(),
                                    startLine,
                                    endLine
                                ) != 0) {
                                errorMsg = "Nenhum arquivo selecionado";
                            }*/
                        }
                    }
                    if (comboWichInput.getSelectedIndex() == 0) {
                        textAreaResult.setText("");
                    }
                    else {
                        if (errorMsg.isEmpty()) {
                            String results;
                            results = sopsList.get(0).getResult();
                            for (int r = 1; r < sopsList.size(); r++) {
                                results += ";\n" + sopsList.get(r).getResult();
                            }
                            textAreaResult.setText(results);
                            textAreaReport.setText(reportText(comboWichReport/*, writer*/));
                        }
                        else {
                            textAreaResult.setText(errorMsg);
                            textAreaReport.setText("-");
                        }
                    }
                    //outputFile.close();
                }
                catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
                //labelTime.update(labelTime.getGraphics());
                //System.out.printf("\nTime elapsed: %.3f s", (float) elapsedTime/1000000000);
            }
        });
        
        comboWichReport.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (errorMsg.isEmpty()) {
                        if (hasResult) {
                            try {
                                textAreaReport.setText(reportText(comboWichReport/*, writer*/));
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
                long startTime = System.nanoTime();
                try {
                    //setFileToWrite("Quine-McCluskey Results.txt");
                    //openOutputFile("Quine-McCluskey Results.txt");
                    hasResult = true;
                    errorMsg = "";
                    optimizeExpressions(
                        (String) comboExpressions.getSelectedItem(),
                        (int) slider.getValue()/*,
                        outputFile*/
                    );
                    if (errorMsg.isEmpty()) {
                        String results;
                        if (sopsList.size() > 1) {
                            results = "";
                        }
                        else {
                            results = sopsList.get(0).getResult();
                        }
                        textAreaResult.setText(results);
                        textAreaReport.setText(reportText(comboWichReport/*, writer*/));
                    }
                    else {
                        textAreaResult.setText(errorMsg);
                        textAreaReport.setText("-");
                    }
                    //outputFile.close();
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                //long elapsedTime = System.nanoTime() - startTime;
                labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
                labelTime.update(labelTime.getGraphics());
                //System.out.printf("\nTime elapsed: %.3f s", (float) elapsedTime/1000000000);
            }
        });
        /*
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) { 
                printt("\n");
            }
        });
        */
        inputFormat = String.valueOf(comboExpressions.getSelectedItem());
    }
    
    public String reportText(JComboBox comboWichReport) throws FileNotFoundException, UnsupportedEncodingException {
        //PrintWriter writer = new PrintWriter("Quine-McCluskey Results 1.txt", "UTF-8");
        String out = "";
        switch ((String)comboWichReport.getSelectedItem()) {
            case "Relatório Básico" -> {
                try {
                    for (int r=0; r < sopsList.size(); r++) {
                        out += sopsList.get(r).getBasicReport();
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
                        out = sopsList.get(r).getBasicReport();
                    }
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return out;
    }
    
    public void optimizeExpressions(
        String allExpressions,
        int numVars/*,
        PrintWriter writer*/) throws Exception {
        
        sopsList = new ArrayList<>();
        //SumOfProducts sopsList = new SumOfProducts();
        
        allExpressions = Tools.removeSpacesFromExpression(allExpressions);
        int begin = 0;
        int end;
        do {
            end = allExpressions.indexOf(';', begin);
            //end = -1; //ACEITAR APENAS UMA EXPRESSÃO POR LINHA
            if (end < 0) {
                end = allExpressions.length();
            }
            expression = allExpressions.substring(begin, end);
            //expression = allExpressions;
            sopsList.add(new SumOfProducts());
            int lastSOPIndex = sopsList.size()-1;
            //sopsList.get(lastSOPIndex).setExpression(expression, numVars);
            if (!sopsList.get(lastSOPIndex).setExpression(expression, numVars)) {
                begin = end + 1;
                continue;
            }
            sopsList.get(lastSOPIndex).sortByOnesCount();
            sopsList.get(lastSOPIndex).mergePrimeImplicants(10);
            sopsList.get(lastSOPIndex).fillMinTermsList();
            sopsList.get(lastSOPIndex).fillTruthTable();
            sopsList.get(lastSOPIndex).fillFinalProductsLists();
            sopsList.get(lastSOPIndex).completeFinalList();
            sopsList.get(lastSOPIndex).buildOptimizedExpression();
            
////////////// LER E ESCREVER EM ARQUIVO [BLOCK START] /////////////////////////
            //print(sopsList.get(lastSOPIndex).getResult()+"\t", /*writer*/outputFile);
            //print(sopsList.get(lastSOPIndex).expression2hexadecimal(sopsList.get(lastSOPIndex).getResult())+"\t", /*writer*/outputFile);
            //print(SumOfProducts.numberOfLiterals(sopsList.get(lastSOPIndex).getResult(), sopsList.get(lastSOPIndex).getNumberOfVars(), sopsList.get(lastSOPIndex).getNumberOfProducts())+"\n", /*writer*/outputFile);
////////////// LER E ESCREVER EM ARQUIVO [BLOCK END] ///////////////////////////
            
            begin = end + 1;
            if (begin >= allExpressions.length()) {
                break;
            }
        }
        while (begin < allExpressions.length());
        //outputFile.close();
        //openOutputFile("Quine-McCluskey Results.txt");
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (tabbedPane.getSelectedIndex() != 1) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Tools.printt("\n");
                System.exit(0);
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
    }

}
