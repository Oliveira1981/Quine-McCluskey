package rodrigo.rosabinary.quinemccluskey;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author Rodrigo Rosa
 */
public final class QuineMcCluskey implements KeyListener {
    
    public boolean
            hasResult,
            darkTheme,
            updateScreen,
            showProgressBar,
            writeToFile,
            writeInputExp,
            writeResultExp,
            writeHexa,
            writeNumLit;
    
    private int
            numVars,
            progress;
    
    private String
            inputFormat,
            expressions,
            errorMsg,
            previousEditor;
    
    private Font
            fontReport;
    
    private JButton
            okButton;
    
    public JCheckBox
            checkReadEntireFile,
            checkWriteToFile,
            checkShowProgressBar,
            checkUpdateScreen,
            checkInputExp,
            checkResultExp,
            checkHexa,
            checkNumLit;
            
    private JDialog
            dialog;
    
    private JComboBox<String>
            comboExpressions,
            comboWichReport,
            comboWichInput;
    
    public JLabel
            labelVariables,
            labelThemeDark,
            labelStartLine,
            labelEndLine,
            labelResult,
            labelResultsFromFile;
    
    private JPanel
            quineMcPanel;
    
    private JProgressBar
            progressBar;
    
    private JScrollPane
            scrollReport;
    
    private JSlider
            sliderVars;
    
    private JTextArea
            textAreaReport,
            textAreaResult;
    
    private JTextField
            labelTime,
            textStartLine,
            textEndLine;
    
    private PrintWriter
            outputFile;
    
    private SumOfProducts
            sumOfProducts;
    
    private ArrayList<String>
            fullReport;
    
    private String[] wichInput = {
        "Carregar expressões de um arquivo...",
        "Digitar expressão",
        "Gerar expressão aleatória"
    };
    
    private String[] wichReport = {
        "Relatório",
        "Tabela Verdade",
        "Mintermos e seus Produtos",
        "Produtos e seus Mintermos",
        "Tabela de Cobertura"
    };
    
    public QuineMcCluskey(boolean darkTheme) throws FileNotFoundException {
        hasResult          =    false;
        updateScreen       =     true;
        showProgressBar    =     true;
        writeToFile        =    false;
        writeInputExp      =     true;
        writeResultExp     =     true;
        writeHexa          =     true;
        writeNumLit        =    false;
        numVars            =        0;
        progress           =        0;
        inputFormat        =       "";
        errorMsg           =       "";
        previousEditor     =       "";
        sumOfProducts      =     null;
        createQuineMcPanel(darkTheme);
    }
    
    public String getInputFormat(){
        return inputFormat;
    }
    
    public String getExpression(){
        return expressions;
    }
    
    public JPanel getPanel(){
        return quineMcPanel;
    }
    
    public void createQuineMcPanel (boolean theme) throws FileNotFoundException {
        
        this.darkTheme = theme;
        
        Color textBGColor         , textColor,
              buttonBGColor       , buttonTextColor,
              comboBGColor        , comboTextColor,
              labelColor          , disabledLabelColor,
              highlightLabelColor ,
              readEntireFileColor , caretColor;
        
        if(darkTheme) {
            textBGColor         = new Color( 44,  44,  44);
            textColor           = new Color( 50, 150, 250);
            buttonBGColor       = new Color( 30,  50, 100);
            buttonTextColor     = new Color( 50, 150, 250);
            comboBGColor        = new Color( 70,  73,  75);//70, 73, 75 (flatlaf defaut)
            comboTextColor      = new Color( 30, 130, 230);
            labelColor          = new Color( 30, 130, 230);
            disabledLabelColor  = new Color(110, 110, 110);
            highlightLabelColor = new Color( 60, 160, 255);
            readEntireFileColor = new Color( 30, 130, 230);
            caretColor          = new Color( 30, 120, 255);
        }
        else {
            textBGColor         = new Color(222, 222, 222);
            textColor           = new Color( 10,  80, 170);
            buttonBGColor       = new Color(199, 222, 244);
            buttonTextColor     = new Color( 50, 150, 250);
            comboBGColor        = new Color(248, 248, 248);
            comboTextColor      = new Color( 30, 130, 230);
            labelColor          = new Color( 30, 130, 230);
            disabledLabelColor  = new Color(188, 188, 188);
            highlightLabelColor = new Color( 60, 160, 255);
            readEntireFileColor = new Color( 30, 130, 230);
            caretColor          = new Color( 30, 120, 255);
        }
        
        quineMcPanel = new JPanel(new GridBagLayout());
        
        Font fontDefaultBold = new Font("Segoe UI", Font.BOLD, 13);
        GridBagConstraints gbcSpaces = new GridBagConstraints();
        quineMcPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        Color borderColor = new Color(0, 0, 0, 0);
        
        JLabel space1 = new JLabel("   ");
        gbcSpaces.fill = GridBagConstraints.HORIZONTAL;
        gbcSpaces.gridx = 0;
        gbcSpaces.gridy = 0;
        gbcSpaces.gridwidth = 16;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        space1.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space1, gbcSpaces);
        
        JLabel space3 = new JLabel("   ");
        gbcSpaces.fill = GridBagConstraints.VERTICAL;
        gbcSpaces.gridx = 0;
        gbcSpaces.gridy = 1;
        gbcSpaces.gridwidth = 1;
        gbcSpaces.gridheight = 9;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        space3.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space3, gbcSpaces);
        
        comboWichInput = new JComboBox<>(wichInput);
        comboWichInput.setSelectedIndex(1); // Digitar
        comboWichInput.setPreferredSize(new Dimension(220, 30));
        comboWichInput.setMinimumSize(new Dimension(220, 30));
        comboWichInput.setFocusable(true);
        comboWichInput.setFont(new Font("Segoe UI", Font.BOLD, 12));
        comboWichInput.setBackground(comboBGColor);
        comboWichInput.setForeground(comboTextColor);//30, 130, 230
        GridBagConstraints gbcWichInput = new GridBagConstraints();
        gbcWichInput.fill = GridBagConstraints.NONE;
        gbcWichInput.gridx = 1;
        gbcWichInput.gridy = 1;
        gbcWichInput.gridwidth = 1;
        gbcWichInput.gridheight = 1;
        gbcWichInput.weightx = 0.0;
        gbcWichInput.weighty = 0.0;
        gbcWichInput.anchor = GridBagConstraints.WEST;
        //comboWichInput.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(comboWichInput, gbcWichInput);
        
        JLabel space4a = new JLabel(" ");
        gbcSpaces.fill = GridBagConstraints.HORIZONTAL;
        gbcSpaces.gridx = 2;
        gbcSpaces.gridy = 1;
        gbcSpaces.gridwidth = 1;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        gbcSpaces.anchor = GridBagConstraints.WEST;
        space4a.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space4a, gbcSpaces);
        
        checkReadEntireFile = new JCheckBox("Inteiro");
        checkReadEntireFile.setFont(fontDefaultBold);
        checkReadEntireFile.setForeground(labelColor);//30, 130, 230
        checkReadEntireFile.setFocusable(false);
        checkReadEntireFile.setSelected(true);
        GridBagConstraints gbcCheckReadEntireFile = new GridBagConstraints();
        gbcCheckReadEntireFile.fill = GridBagConstraints.HORIZONTAL;
        gbcCheckReadEntireFile.gridx = 3;
        gbcCheckReadEntireFile.gridy = 1;
        gbcCheckReadEntireFile.gridwidth = 1;
        gbcCheckReadEntireFile.gridheight = 1;
        gbcCheckReadEntireFile.weightx = 0.0;
        gbcCheckReadEntireFile.weighty = 0.0;
        gbcCheckReadEntireFile.anchor = GridBagConstraints.WEST;
        checkReadEntireFile.setBorder(BorderFactory.createLineBorder(borderColor));
        checkReadEntireFile.setVisible(false);
        quineMcPanel.add(checkReadEntireFile, gbcCheckReadEntireFile);
        
        labelStartLine = new JLabel("     Linha inicial: ");
        labelStartLine.setFont(fontDefaultBold);
        labelStartLine.setForeground(disabledLabelColor);//110, 110, 110
        GridBagConstraints gbcLabelStartLine = new GridBagConstraints();
        gbcLabelStartLine.fill = GridBagConstraints.HORIZONTAL;
        gbcLabelStartLine.gridx = 4;
        gbcLabelStartLine.gridy = 1;
        gbcLabelStartLine.gridwidth = 1;
        gbcLabelStartLine.gridheight = 1;
        gbcLabelStartLine.weightx = 0.0;
        gbcLabelStartLine.weighty = 0.0;
        gbcLabelStartLine.anchor = GridBagConstraints.WEST;
        labelStartLine.setBorder(BorderFactory.createLineBorder(borderColor));
        labelStartLine.setVisible(false);
        quineMcPanel.add(labelStartLine, gbcLabelStartLine);
        
        textStartLine = new JTextField();
        textStartLine.setPreferredSize(new Dimension(55, 20));
        textStartLine.setMinimumSize(new Dimension(55, 20));
        textStartLine.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textStartLine.setFocusable(false);
        GridBagConstraints gbcTextStartLine = new GridBagConstraints();
        gbcTextStartLine.fill = GridBagConstraints.HORIZONTAL;
        gbcTextStartLine.gridx = 5;
        gbcTextStartLine.gridy = 1;
        gbcTextStartLine.gridwidth = 1;
        gbcTextStartLine.gridheight = 1;
        gbcTextStartLine.weightx = 0.0;
        gbcTextStartLine.weighty = 0.0;
        gbcTextStartLine.anchor = GridBagConstraints.WEST;
        //textStartLine.setBorder(BorderFactory.createLineBorder(borderColor));
        textStartLine.setVisible(false);
        quineMcPanel.add(textStartLine, gbcTextStartLine);
        
        labelEndLine = new JLabel("     Linha final: ");
        labelEndLine.setFont(fontDefaultBold);
        labelEndLine.setForeground(disabledLabelColor);//110, 110, 110
        GridBagConstraints gbcLabelEndLine = new GridBagConstraints();
        gbcLabelEndLine.fill = GridBagConstraints.HORIZONTAL;
        gbcLabelEndLine.gridx = 6;
        gbcLabelEndLine.gridy = 1;
        gbcLabelEndLine.gridwidth = 1;
        gbcLabelEndLine.gridheight = 1;
        gbcLabelEndLine.weightx = 0.0;
        gbcLabelEndLine.weighty = 0.0;
        gbcLabelEndLine.anchor = GridBagConstraints.WEST;
        labelEndLine.setBorder(BorderFactory.createLineBorder(borderColor));
        labelEndLine.setVisible(false);
        quineMcPanel.add(labelEndLine, gbcLabelEndLine);
        
        textEndLine = new JTextField();
        textEndLine.setForeground(labelColor);
        textEndLine.setPreferredSize(new Dimension(55, 20));
        textEndLine.setMinimumSize(new Dimension(55, 20));
        textEndLine.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textEndLine.setFocusable(false);
        GridBagConstraints gbcTextEndLine = new GridBagConstraints();
        gbcTextEndLine.fill = GridBagConstraints.HORIZONTAL;
        gbcTextEndLine.gridx = 7;
        gbcTextEndLine.gridy = 1;
        gbcTextEndLine.gridwidth = 1;
        gbcTextEndLine.gridheight = 1;
        gbcTextEndLine.weightx = 0.0;
        gbcTextEndLine.weighty = 0.0;
        gbcTextEndLine.anchor = GridBagConstraints.WEST;
        //textEndLine.setBorder(BorderFactory.createLineBorder(borderColor));
        textEndLine.setVisible(false);
        quineMcPanel.add(textEndLine, gbcTextEndLine);
        
        labelVariables = new JLabel("   Número de variáveis: Auto");
        labelVariables.setFont(fontDefaultBold);
        labelVariables.setForeground(labelColor);
        GridBagConstraints gbcLabelVariables = new GridBagConstraints();
        gbcLabelVariables.fill = GridBagConstraints.HORIZONTAL;
        gbcLabelVariables.gridx = 11;
        gbcLabelVariables.gridy = 1;
        gbcLabelVariables.gridwidth = 1;
        gbcLabelVariables.gridheight = 1;
        gbcLabelVariables.weightx = 0.0;
        gbcLabelVariables.weighty = 0.0;
        gbcLabelVariables.anchor = GridBagConstraints.WEST;
        labelVariables.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(labelVariables, gbcLabelVariables);
        
        checkUpdateScreen = new JCheckBox("Atualizações na tela");
        checkUpdateScreen.setForeground(highlightLabelColor);
        checkUpdateScreen.setFont(new Font("Segoe.UI", Font.BOLD, 12));
        checkUpdateScreen.setFocusable(true);
        checkUpdateScreen.setSelected(true);
        GridBagConstraints gbcCheckUpdateScreen = new GridBagConstraints();
        gbcCheckUpdateScreen.fill = GridBagConstraints.HORIZONTAL;
        gbcCheckUpdateScreen.gridx = 12;//14
        gbcCheckUpdateScreen.gridy = 1;
        gbcCheckUpdateScreen.gridwidth = 1;
        gbcCheckUpdateScreen.gridheight = 1;
        gbcCheckUpdateScreen.weightx = 1.0;
        gbcCheckUpdateScreen.weighty = 0.0;
        gbcCheckUpdateScreen.anchor = GridBagConstraints.EAST;
        checkUpdateScreen.setHorizontalAlignment(SwingConstants.RIGHT);
        checkUpdateScreen.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(checkUpdateScreen, gbcCheckUpdateScreen);
        
        checkShowProgressBar = new JCheckBox("Barra de progresso");
        checkShowProgressBar.setForeground(highlightLabelColor);
        checkShowProgressBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        checkShowProgressBar.setFocusable(true);
        checkShowProgressBar.setSelected(true);
        GridBagConstraints gbcCheckShowProgressBar = new GridBagConstraints();
        gbcCheckShowProgressBar.fill = GridBagConstraints.HORIZONTAL;
        gbcCheckShowProgressBar.gridx = 13;
        gbcCheckShowProgressBar.gridy = 1;
        gbcCheckShowProgressBar.gridwidth = 1;
        gbcCheckShowProgressBar.gridheight = 1;
        gbcCheckShowProgressBar.weightx = 1.0;
        gbcCheckShowProgressBar.weighty = 0.0;
        //gbcCheckShowProgressBar.anchor = GridBagConstraints.EAST;
        checkShowProgressBar.setHorizontalAlignment(SwingConstants.RIGHT);
        checkShowProgressBar.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(checkShowProgressBar, gbcCheckShowProgressBar);
        
        checkWriteToFile = new JCheckBox("Resultado em arquivo");
        checkWriteToFile.setForeground(labelColor);
        checkWriteToFile.setFont(new Font("Segoe UI", Font.BOLD, 12));
        checkWriteToFile.setFocusable(true);
        checkWriteToFile.setSelected(false);
        GridBagConstraints gbcCheckWriteToFile = new GridBagConstraints();
        gbcCheckWriteToFile.fill = GridBagConstraints.HORIZONTAL;
        gbcCheckWriteToFile.gridx = 14;//12
        gbcCheckWriteToFile.gridy = 1;
        gbcCheckWriteToFile.gridwidth = 1;
        gbcCheckWriteToFile.gridheight = 1;
        gbcCheckWriteToFile.weightx = 1.0;
        gbcCheckWriteToFile.weighty = 0.0;
        gbcCheckWriteToFile.anchor = GridBagConstraints.EAST;
        checkWriteToFile.setHorizontalAlignment(SwingConstants.RIGHT);
        checkWriteToFile.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(checkWriteToFile, gbcCheckWriteToFile);
        
        /*JLabel space10 = new JLabel(" ");
        gbcSpaces.fill = GridBagConstraints.HORIZONTAL;
        gbcSpaces.gridx = 13;
        gbcSpaces.gridy = 1;
        gbcSpaces.gridwidth = 2;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        gbcSpaces.anchor = GridBagConstraints.WEST;
        space4a.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space10, gbcSpaces);*/
        
        /*JLabel space11 = new JLabel(" ");
        gbcSpaces.fill = GridBagConstraints.HORIZONTAL;
        gbcSpaces.gridx = 13;
        gbcSpaces.gridy = 1;
        gbcSpaces.gridwidth = 2;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 1.0;
        gbcSpaces.weighty = 0.0;
        gbcSpaces.anchor = GridBagConstraints.WEST;
        space4a.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space11, gbcSpaces);*/
        
        /*JLabel labelThemeLight = new JLabel("Claro");
        labelThemeLight.setFont(fontDefault);
        labelThemeLight.setForeground(disabledLabelColor);
        labelThemeLight.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbcLabelThemeLight = new GridBagConstraints();
        gbcLabelThemeLight.fill = GridBagConstraints.HORIZONTAL;
        gbcLabelThemeLight.gridx = 12;
        gbcLabelThemeLight.gridy = 1;
        gbcLabelThemeLight.gridwidth = 1;
        gbcLabelThemeLight.gridheight = 1;
        gbcLabelThemeLight.weightx = 100.0;
        gbcLabelThemeLight.weighty = 0.0;
        gbcLabelThemeLight.anchor = GridBagConstraints.EAST;
        labelThemeLight.setBorder(BorderFactory.createLineBorder(borderColor));
        labelThemeLight.setVisible(false);
        quineMcPanel.add(labelThemeLight, gbcLabelThemeLight);
        
        JSlider sliderTheme = new JSlider(JSlider.HORIZONTAL, 0, 1, 1); // min, max, inicial
        //sliderTheme.addKeyListener(this);
        sliderTheme.setFocusable(false);
        sliderTheme.setMinorTickSpacing(1);
        sliderTheme.setSnapToTicks(true);
        sliderTheme.setMinimumSize(new Dimension(60, 20));
        sliderTheme.setMaximumSize(new Dimension(60, 20));
        sliderTheme.setPreferredSize(new Dimension(60, 20));
        sliderTheme.setSize(new Dimension(60, 20));
        GridBagConstraints gbcSliderTheme = new GridBagConstraints();
        gbcSliderTheme.fill = GridBagConstraints.NONE;
        gbcSliderTheme.gridx = 13;
        gbcSliderTheme.gridy = 1;
        gbcSliderTheme.gridwidth = 1;
        gbcSliderTheme.gridheight = 1;
        gbcSliderTheme.weightx = 1.0;
        gbcSliderTheme.weighty = 0.0;
        gbcSliderTheme.anchor = GridBagConstraints.EAST;
        sliderTheme.setBorder(BorderFactory.createLineBorder(borderColor));
        sliderTheme.setVisible(false);
        quineMcPanel.add(sliderTheme, gbcSliderTheme);
        
        labelThemeDark = new JLabel("   Escuro");
        labelThemeDark.setFont(fontDefault);
        labelThemeDark.setForeground(labelColor);
        labelThemeDark.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbcLabelThemeDark = new GridBagConstraints();
        gbcLabelThemeDark.fill = GridBagConstraints.NONE;
        gbcLabelThemeDark.gridx = 14;
        gbcLabelThemeDark.gridy = 1;
        gbcLabelThemeDark.gridwidth = 1;
        gbcLabelThemeDark.gridheight = 1;
        gbcLabelThemeDark.weightx = 0.0;
        gbcLabelThemeDark.weighty = 0.0;
        gbcLabelThemeDark.anchor = GridBagConstraints.EAST;
        labelThemeDark.setBorder(BorderFactory.createLineBorder(borderColor));
        labelThemeDark.setVisible(false);
        quineMcPanel.add(labelThemeDark, gbcLabelThemeDark);*/
        
        JLabel space3a = new JLabel(" ");
        space3a.setFont(new Font("SEGOE UI", Font.PLAIN, 1));
        gbcSpaces.fill = GridBagConstraints.HORIZONTAL;
        gbcSpaces.gridx = 1;
        gbcSpaces.gridy = 2;
        gbcSpaces.gridwidth = 14;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        space3a.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space3a, gbcSpaces);
        
        comboExpressions = new JComboBox<>(getHistory());
        comboExpressions.setPreferredSize(new Dimension(500, 30));
        comboExpressions.setMinimumSize(new Dimension(500, 30));
        comboExpressions.setEditable(true);
        JTextField editor = (JTextField) comboExpressions.getEditor().getEditorComponent();
        editor.addKeyListener(this);
        editor.setName("Editor");
        comboExpressions.setSelectedIndex(-1);
        comboExpressions.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboExpressions.setFocusable(true);
        GridBagConstraints gbcComboExpressions = new GridBagConstraints();
        gbcComboExpressions.fill = GridBagConstraints.HORIZONTAL;
        gbcComboExpressions.gridx = 1;
        gbcComboExpressions.gridy = 3;
        gbcComboExpressions.gridwidth = 7;
        gbcComboExpressions.gridheight = 1;
        gbcComboExpressions.weightx = 0.0;
        gbcComboExpressions.weighty = 0.0;
        gbcComboExpressions.anchor = GridBagConstraints.WEST;
        //comboExpressions.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(comboExpressions, gbcComboExpressions);
        
        JLabel space5 = new JLabel(" ");
        gbcSpaces.fill = GridBagConstraints.HORIZONTAL;
        gbcSpaces.gridx = 8;
        gbcSpaces.gridy = 3;
        gbcSpaces.gridwidth = 1;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        gbcSpaces.anchor = GridBagConstraints.WEST;
        space5.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space5, gbcSpaces);
        
        okButton = new JButton("Executar");
        okButton.setPreferredSize(new Dimension(90, 30));
        okButton.setMinimumSize(new Dimension(90, 30));
        okButton.setFocusable(true);
        okButton.setBackground(buttonBGColor);//30, 50, 100
        okButton.setForeground(buttonTextColor);//50, 150, 250
        okButton.setFont(fontDefaultBold);
        GridBagConstraints gbcOkButton = new GridBagConstraints();
        gbcOkButton.fill = GridBagConstraints.HORIZONTAL;
        gbcOkButton.gridx = 9;
        gbcOkButton.gridy = 3;
        gbcOkButton.gridwidth = 1;
        gbcOkButton.gridheight = 1;
        gbcOkButton.weightx = 0.0;
        gbcOkButton.weighty = 0.0;
        gbcOkButton.anchor = GridBagConstraints.WEST;
        //okButton.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(okButton, gbcOkButton);
        
        JLabel space5B = new JLabel(" ");
        gbcSpaces.fill = GridBagConstraints.HORIZONTAL;
        gbcSpaces.gridx = 10;
        gbcSpaces.gridy = 3;
        gbcSpaces.gridwidth = 1;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        gbcSpaces.anchor = GridBagConstraints.WEST;
        space5B.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space5B, gbcSpaces);
        
        Dictionary<Integer, Component> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("Auto"));
        labelTable.put(4, new JLabel("4"));
        labelTable.put(8, new JLabel("8"));
        labelTable.put(12, new JLabel("12"));
        labelTable.put(16, new JLabel("16"));
        
        sliderVars = new JSlider(JSlider.HORIZONTAL, 0, 16, 0); // min, max, inicial
        sliderVars.setMinorTickSpacing(1);
        sliderVars.setSnapToTicks(true);
        sliderVars.setMinimumSize(new Dimension(200, 30));
        sliderVars.setLabelTable(labelTable);
        GridBagConstraints gbcSliderVars = new GridBagConstraints();
        gbcSliderVars.fill = GridBagConstraints.VERTICAL;
        gbcSliderVars.gridx = 11;
        gbcSliderVars.gridy = 3;
        gbcSliderVars.gridwidth = 1;
        gbcSliderVars.gridheight = 1;
        gbcSliderVars.weightx = 0.0;
        gbcSliderVars.weighty = 0.0;
        gbcSliderVars.anchor = GridBagConstraints.WEST;
        sliderVars.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(sliderVars, gbcSliderVars);
        
        JLabel space6 = new JLabel(" ");
        gbcSpaces.fill = GridBagConstraints.HORIZONTAL;
        gbcSpaces.gridx = 1;
        gbcSpaces.gridy = 4;
        gbcSpaces.gridwidth = 14;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        space6.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space6, gbcSpaces);
        
        labelResult = new JLabel("Expressão Minimizada:");
        labelResult.setFont(fontDefaultBold);
        labelResult.setForeground(labelColor);
        GridBagConstraints gbcLabelResult = new GridBagConstraints();
        gbcLabelResult.fill = GridBagConstraints.NONE;
        gbcLabelResult.gridx = 1;
        gbcLabelResult.gridy = 5;
        gbcLabelResult.gridwidth = 1;
        gbcLabelResult.gridheight = 1;
        gbcLabelResult.weightx = 0.0;
        gbcLabelResult.weighty = 0.0;
        gbcLabelResult.anchor = GridBagConstraints.WEST;
        labelResult.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(labelResult, gbcLabelResult);
        
        textAreaResult = new JTextArea();
        textAreaResult.setFocusable(true);
        textAreaResult.setLineWrap(true);
        textAreaResult.setEditable(false);
        textAreaResult.setBackground(textBGColor);
        textAreaResult.setForeground(textColor);//50, 150, 250
        textAreaResult.setFont(new Font("Consolas", Font.PLAIN, 16));
        Insets mResult = new Insets(12, 8, 4, 6);
        textAreaResult.setMargin(mResult);
        GridBagConstraints gbcTextAreaResult = new GridBagConstraints();
        gbcTextAreaResult.fill = GridBagConstraints.HORIZONTAL;
        gbcTextAreaResult.gridx = 1;
        gbcTextAreaResult.gridy = 6;
        gbcTextAreaResult.gridwidth = 14;
        gbcTextAreaResult.gridheight = 1;
        gbcTextAreaResult.weightx = 0.0;
        gbcTextAreaResult.weighty = 0.0;
        gbcTextAreaResult.anchor = GridBagConstraints.WEST;
        //textAreaResult.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(textAreaResult, gbcTextAreaResult);
        
        JLabel space9 = new JLabel(" ");
        gbcSpaces.fill = GridBagConstraints.HORIZONTAL;
        gbcSpaces.gridx = 1;
        gbcSpaces.gridy = 7;
        gbcSpaces.gridwidth = 14;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        space9.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space9, gbcSpaces);
        
        comboWichReport = new JComboBox<>(wichReport);
        comboWichReport.setPreferredSize(new Dimension(250, 30));
        comboWichReport.setMinimumSize(new Dimension(250, 30));
        comboWichReport.setFocusable(true);
        comboWichReport.setFont(new Font("Segoe UI", Font.BOLD, 12));
        comboWichReport.setBackground(comboBGColor);
        comboWichReport.setForeground(comboTextColor);//30, 130, 230
        GridBagConstraints gbcComboWichReport = new GridBagConstraints();
        gbcComboWichReport.fill = GridBagConstraints.NONE;
        gbcComboWichReport.gridx = 1;
        gbcComboWichReport.gridy = 8;
        gbcComboWichReport.gridwidth = 1;
        gbcComboWichReport.gridheight = 1;
        gbcComboWichReport.weightx = 0.0;
        gbcComboWichReport.weighty = 0.0;
        gbcComboWichReport.anchor = GridBagConstraints.WEST;
        //comboWichReport.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(comboWichReport, gbcComboWichReport);
        
        labelResultsFromFile = new JLabel(" Resultados:");
        labelResultsFromFile.setPreferredSize(new Dimension(250, 30));
        labelResultsFromFile.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelResultsFromFile.setForeground(labelColor);
        GridBagConstraints gbcLabelResultsFromFile = new GridBagConstraints();
        gbcLabelResultsFromFile.fill = GridBagConstraints.NONE;
        gbcLabelResultsFromFile.gridx = 1;
        gbcLabelResultsFromFile.gridy = 8;
        gbcLabelResultsFromFile.gridwidth = 5;
        gbcLabelResultsFromFile.gridheight = 1;
        gbcLabelResultsFromFile.weightx = 0.0;
        gbcLabelResultsFromFile.weighty = 0.0;
        gbcLabelResultsFromFile.anchor = GridBagConstraints.WEST;
        //labelResultsFromFile.setBorder(BorderFactory.createLineBorder(borderColor));
        
        labelTime = new JTextField(" ");
        labelTime.setEditable(false);
        labelTime.setMinimumSize(new Dimension(250, 30));
        labelTime.setFont(new Font("Consolas", Font.PLAIN, 14));
        labelTime.setForeground(labelColor);
        labelTime.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbcLabelTime = new GridBagConstraints();
        gbcLabelTime.fill = GridBagConstraints.BOTH;
        gbcLabelTime.gridx = 3;
        gbcLabelTime.gridy = 8;
        gbcLabelTime.gridwidth = 12;
        gbcLabelTime.gridheight = 1;
        gbcLabelTime.weightx = 0.0;
        gbcLabelTime.weighty = 0.0;
        gbcLabelTime.anchor = GridBagConstraints.WEST;
        labelTime.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(labelTime, gbcLabelTime);
        
        textAreaReport = new JTextArea();
        textAreaReport.setFocusable(true);
        textAreaReport.setLineWrap(true);
        textAreaReport.setEditable(false);
        textAreaReport.setBackground(textBGColor);
        textAreaReport.setForeground(textColor);
        fontReport = new Font("Consolas", Font.PLAIN, 16);
        textAreaReport.setFont(fontReport);
        Insets mReport = new Insets(10, 10, 10, 10);
        textAreaReport.setMargin(mReport);
        
        scrollReport = new JScrollPane(textAreaReport);
        GridBagConstraints gbcScrollReport = new GridBagConstraints();
        gbcScrollReport.fill = GridBagConstraints.BOTH;
        gbcScrollReport.gridx = 1;
        gbcScrollReport.gridy = 9;
        gbcScrollReport.gridwidth = 14;
        gbcScrollReport.gridheight = 1;
        gbcScrollReport.weightx = 100.0;
        gbcScrollReport.weighty = 0.6;
        scrollReport.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(scrollReport, gbcScrollReport);
        
        JLabel space4 = new JLabel("   ");
        gbcSpaces.fill = GridBagConstraints.VERTICAL;
        gbcSpaces.gridx = 15;
        gbcSpaces.gridy = 1;
        gbcSpaces.gridwidth = 1;
        gbcSpaces.gridheight = 9;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        space4.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space4, gbcSpaces);
        
        JLabel space2 = new JLabel(" ");
        gbcSpaces.fill = GridBagConstraints.HORIZONTAL;
        gbcSpaces.gridx = 0;
        gbcSpaces.gridy = 10;
        gbcSpaces.gridwidth = 16;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        space2.setFont(new Font("Segoe UI", Font.PLAIN, 6));
        space2.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(space2, gbcSpaces);
        
        progressBar = new JProgressBar();
        gbcSpaces.fill = GridBagConstraints.BOTH;
        gbcSpaces.gridx = 11;
        gbcSpaces.gridy = 10;
        gbcSpaces.gridwidth = 4;
        gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        progressBar.setString(" ");
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
        progressBar.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(progressBar, gbcSpaces);
        
        for(int c=0; c<quineMcPanel.getComponentCount(); c++) {
            quineMcPanel.getComponent(c).addKeyListener(this);
        }
        
        wichResultsToFile();
        
        /*sliderTheme.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                int selectedExp = comboExpressions.getSelectedIndex();
                
                // DARK THEME ///////////////
                if (source.getValue() == 1) {
                    darkTheme = true;
                    labelThemeDark.setForeground(darkLabelColor);
                    labelThemeLight.setForeground(darkDisabledLabelColor);
                    
                    textAreaReport.setBackground(darkTextBGColor);
                    textAreaReport.setForeground(darkTextColor);
                    
                    comboWichReport.setBackground(darkComboBGColor);
                    
                    textAreaResult.setBackground(darkTextBGColor);
                    textAreaResult.setForeground(darkTextColor);//50, 150, 250
                    
                    comboWichInput.setBackground(darkComboBGColor);
                    
                    okButton.setBackground(darkButtonBGColor);//30, 50, 100
                    okButton.setForeground(darkButtonTextColor);//50, 150, 250
                    
                    labelEndLine.setForeground(darkDisabledLabelColor);//110, 110, 110
                    labelStartLine.setForeground(darkDisabledLabelColor);//110, 110, 110
                    
                    //ENJAMBRE pra GUI ouvir a mudança
                    createQuineMcPanel.setForeground(Color.green);
                    
                    // e isto é só pra atualizar o comboWichReport, 
                    // que não foi atualizado pois estava off
                    try {
                        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                    }
                    catch (UnsupportedLookAndFeelException ex) {
                    }
                    SwingUtilities.updateComponentTreeUI(comboWichReport);
                }
                // LIGHT THEME //////////////
                else {
                    darkTheme = false;
                    labelThemeDark.setForeground(lightDisabledLabelColor);
                    labelThemeLight.setForeground(lightLabelColor);
                    
                    textAreaReport.setBackground(lightTextBGColor);
                    textAreaReport.setForeground(lightTextColor);
                    
                    comboWichReport.setBackground(lightComboBGColor);
                    
                    textAreaResult.setBackground(lightTextBGColor);
                    textAreaResult.setForeground(lightTextColor);//10, 110, 210
                    
                    comboWichInput.setBackground(lightComboBGColor);
                    
                    okButton.setBackground(lightButtonBGColor);
                    okButton.setForeground(lightButtonTextColor);//50, 150, 250
                    
                    labelEndLine.setForeground(lightDisabledLabelColor);
                    labelStartLine.setForeground(lightDisabledLabelColor);
                    
                    //ENJAMBRE pra GUI ouvir a mudança
                    createQuineMcPanel.setForeground(Color.red);
                    
                    // e isto é só pra atualizar o comboWichReport, 
                    // que não foi atualizado pois estava off
                    try {
                        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                    }
                    catch (UnsupportedLookAndFeelException ex) {
                    }
                    SwingUtilities.updateComponentTreeUI(comboWichReport);
                }
                comboExpressions.setSelectedIndex(selectedExp);
                sliderTheme.update(sliderTheme.getGraphics());
            }
        });*/
        
        comboWichInput.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                quineMcPanel.getRootPane().setDefaultButton(null);
                comboExpressions.setSelectedIndex(-1);
                errorMsg = "";
                textAreaReport.setText("");
                progressBar.setValue(0);
                progressBar.setString(" ");
                switch (comboWichInput.getSelectedIndex()) {
                    case 1 -> { // input: digitar
                        textAreaResult.setText("");
                        checkReadEntireFile.setVisible(false);
                        labelStartLine.setVisible(false);
                        textStartLine.setVisible(false);
                        labelEndLine.setVisible(false);
                        textEndLine.setVisible(false);
                        
                        checkReadEntireFile.setFocusable(false);
                        labelStartLine.setFocusable(false);
                        textStartLine.setFocusable(false);
                        labelEndLine.setFocusable(false);
                        textEndLine.setFocusable(false);
                        
                        quineMcPanel.remove(labelResultsFromFile);
                        quineMcPanel.add(comboWichReport, gbcComboWichReport);
                        
                        labelResult.setVisible(true);
                        textAreaResult.setVisible(true);
                        quineMcPanel.repaint();
                    }
                    case 2 -> { // input: aleatória
                        textAreaResult.setText("");
                        checkReadEntireFile.setVisible(false);
                        labelStartLine.setVisible(false);
                        textStartLine.setVisible(false);
                        labelEndLine.setVisible(false);
                        textEndLine.setVisible(false);
                        
                        checkReadEntireFile.setFocusable(false);
                        labelStartLine.setFocusable(false);
                        textStartLine.setFocusable(false);
                        labelEndLine.setFocusable(false);
                        textEndLine.setFocusable(false);
                        
                        quineMcPanel.remove(labelResultsFromFile);
                        quineMcPanel.add(comboWichReport, gbcComboWichReport);
                        
                        labelResult.setVisible(true);
                        textAreaResult.setVisible(true);
                        quineMcPanel.repaint();
                        hasResult = true;
                        String gen;
                        int vars = sliderVars.getValue();
                        if (vars == 0) {
                            gen = Tools.generateRandomExpression(12, 4);
                        }
                        else {
                            gen = Tools.generateRandomExpression(
                                vars*3, //numberOfProducts 
                                vars    //numberOfVars
                            );
                        }
                        //editor.setText(gen);
                        comboExpressions.setSelectedItem(gen);
                    }
                    case 0 -> { // input: arquivo
                        
                        checkReadEntireFile.setVisible(true);
                        labelStartLine.setVisible(true);
                        textStartLine.setVisible(true);
                        labelEndLine.setVisible(true);
                        textEndLine.setVisible(true);
                        
                        checkReadEntireFile.setFocusable(true);
                        textStartLine.setFocusable(true);
                        textEndLine.setFocusable(true);
                        
                        checkReadEntireFile.setForeground(readEntireFileColor);
                        checkReadEntireFile.setSelected(true);
                        
                        quineMcPanel.remove(comboWichReport);
                        quineMcPanel.add(labelResultsFromFile, gbcLabelResultsFromFile);
                        quineMcPanel.getRootPane().setDefaultButton(okButton);
                            
                        labelResult.setVisible(false);
                        textAreaResult.setVisible(false);
                        quineMcPanel.repaint();
                        
                        try {
                            editor.setText(selectFile());
                            comboExpressions.setSelectedItem(editor.getText());
                        }
                        catch (UnsupportedEncodingException ex) {
                        }
                        catch (Exception ex) {
                        }
                    }
                }
                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(checkWriteToFile);
            }
        });
        
        comboWichInput.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                comboWichInput.setForeground(highlightLabelColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                comboWichInput.setForeground(labelColor);
            }
        
        });
        
        comboWichReport.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                comboWichReport.setForeground(highlightLabelColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                comboWichReport.setForeground(labelColor);
            }
        
        });
        
        checkReadEntireFile.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkReadEntireFile.isSelected()) {
                    checkReadEntireFile.setForeground(disabledLabelColor);
                    labelStartLine.setForeground(labelColor);
                    labelEndLine.setForeground(labelColor);
                    textStartLine.setEnabled(true);
                    textStartLine.setEditable(true);
                    textEndLine.setEnabled(true);
                    textEndLine.setEditable(true);
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(checkReadEntireFile);
                }
                else {
                    checkReadEntireFile.setForeground(labelColor);
                    labelStartLine.setForeground(disabledLabelColor);
                    labelEndLine.setForeground(disabledLabelColor);
                    textStartLine.setEnabled(false);
                    textStartLine.setEditable(false);
                    textStartLine.setText("");
                    textEndLine.setEnabled(false);
                    textEndLine.setEditable(false);
                    textEndLine.setText("");
                }
                
            }
        });
        
        checkWriteToFile.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkWriteToFile.isSelected()) {
                    checkWriteToFile.setForeground(highlightLabelColor);
                    writeToFile = true;
                    //wichResultsToFile();
                    showDialog();
                }
                else {
                    checkWriteToFile.setForeground(labelColor);
                    writeToFile = false;
                }
                
            }
            
        });
        
        checkWriteToFile.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                checkWriteToFile.setForeground(highlightLabelColor);
                ToolTipManager.sharedInstance().setInitialDelay(50);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (checkWriteToFile.isSelected()) {
                    checkWriteToFile.setForeground(highlightLabelColor);
                }
                else {
                    checkWriteToFile.setForeground(labelColor);
                }
            }
            
        });
        
        checkShowProgressBar.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkShowProgressBar.isSelected()) {
                    checkShowProgressBar.setForeground(highlightLabelColor);
                    showProgressBar = true;
                }
                else {
                    checkShowProgressBar.setForeground(labelColor);
                    showProgressBar = false;
                    progressBar.setValue(0);
                    progressBar.setString(" ");
                    progressBar.update(progressBar.getGraphics());
                }
                
            }
            
        });
        
        checkShowProgressBar.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                checkShowProgressBar.setForeground(highlightLabelColor);
                //ToolTipManager.sharedInstance().setInitialDelay(50);
                //checkShowProgressBar.setToolTipText("marcado: mais lento\ndesmarcado: mais rápido");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (checkShowProgressBar.isSelected()) {
                    checkShowProgressBar.setForeground(highlightLabelColor);
                }
                else {
                    checkShowProgressBar.setForeground(labelColor);
                }
            }
            
        });
        
        checkUpdateScreen.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkUpdateScreen.isSelected()) {
                    checkUpdateScreen.setForeground(highlightLabelColor);
                    updateScreen = true;
                }
                else {
                    checkUpdateScreen.setForeground(labelColor);
                    updateScreen = false;
                }
                
            }
            
        });
        
        checkUpdateScreen.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                checkUpdateScreen.setForeground(highlightLabelColor);
                //ToolTipManager.sharedInstance().setInitialDelay(50);
                //checkUpdateScreen.setToolTipText("marcado: mais lento\ndesmarcado: mais rápido");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (checkUpdateScreen.isSelected()) {
                    checkUpdateScreen.setForeground(highlightLabelColor);
                }
                else {
                    checkUpdateScreen.setForeground(labelColor);
                }
            }
            
        });
        
        /*MouseListener mouseListenerLabelLight = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sliderTheme.setValue(0);
            }
        };
        labelThemeLight.addMouseListener(mouseListenerLabelLight);
        
        MouseListener mouseListenerLabelDark = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sliderTheme.setValue(1);
            }
        };
        labelThemeDark.addMouseListener(mouseListenerLabelDark);*/
        
        MouseListener mouseListenerStartLine = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                checkReadEntireFile.setSelected(false);
                checkReadEntireFile.setForeground(disabledLabelColor);
                labelStartLine.setForeground(labelColor);
                labelEndLine.setForeground(labelColor);
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
                checkReadEntireFile.setForeground(disabledLabelColor);
                labelStartLine.setForeground(labelColor);
                labelEndLine.setForeground(labelColor);
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
        
        sliderVars.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                numVars = source.getValue();
                if (numVars == 0) {
                    labelVariables.setText("   Número de variáveis: Auto");
                }
                else {
                    labelVariables.setText("   Número de variáveis: " + numVars);
                }
                quineMcPanel.repaint();
            }
        });
        
        okButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                previousEditor = editor.getText();
                if (writeToFile) {
                    try {
                        setFileToWrite("Quine-McCluskey Results.csv");
                    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                        Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                hasResult = true;
                switch (comboWichInput.getSelectedIndex()) {
                    case 0 -> { // input: arquivo
                        textAreaReport.setText("Processando...");
                        if(showProgressBar) {
                            progressBar.setValue(1);
                            progressBar.setString("0%");
                            progressBar.setStringPainted(true);
                        }
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if(showProgressBar) {
                                    progressBar.update(progressBar.getGraphics());
                                }
                                long startTime = System.nanoTime();
                                labelTime.setText("Tempo:        ");
                                
                                int startLine = 1;
                                int endLine = -1;
                                if (!checkReadEntireFile.isSelected()) {
                                    if (textStartLine.getText().isEmpty()) {
                                        startLine = 1;
                                    }
                                    else {
                                        startLine = Integer.parseInt(textStartLine.getText());
                                    }
                                    if (textEndLine.getText().isEmpty()) {
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
                                File selectedFile = new File(editor.getText());
                                if(!selectedFile.exists()){
                                    print("\nNo file selected.\n");
                                    errorMsg = "Nenhum arquivo selecionado.";
                                    textAreaReport.setText(errorMsg);
                                    return;
                                }
                                Scanner sc = null;
                                try {
                                    sc = new Scanner(selectedFile);
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                int line = 1;
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
                                
                                textAreaReport.setFont(new Font("Consolas", Font.PLAIN, 14));
                                fullReport = new ArrayList<>();
                                
                                int lineIndex = line;
                                if (endLine == -1) {
                                    endLine = Tools.countTotalLines(selectedFile.getPath());
                                }
                                int linesToRead = 1 + endLine - line;
                                // updateFactor calculado de modo a aumentar suavemente com o aumento do número de linhas
                                int updateFactor = showProgressBar ? 100_000 * (int) Math.log10(Math.max(10, linesToRead-5000)) : -1;
                                
                                while (line <= endLine) {
                                    lineIndex = executeSingleFromFile(sc.nextLine(), lineIndex, startTime, line==endLine);
                                    if(showProgressBar) {
                                        progress = 95*line/linesToRead;
                                        if(Math.floorMod(System.nanoTime()-startTime, updateFactor) == 0) { // aleatório, não tempo
                                            progressBar.setValue(progress);
                                            progressBar.setString(progress+"%");
                                            progressBar.setStringPainted(true);
                                            progressBar.update(progressBar.getGraphics());
                                        }
                                    }
                                    line++;
                                }
                                textAreaReport.append("\n\nFinalizando...");
                                textAreaReport.update(textAreaReport.getGraphics());
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
                                textAreaReport.setCaretColor(caretColor);
                                textAreaReport.getCaret().setVisible(true);
                                int fullReportSize = fullReport.size();
                                // updateFactor calculado de modo a aumentar suavemente com o aumento do número de linhas
                                updateFactor = showProgressBar ? 100_000 * (int) Math.log10(Math.max(10, linesToRead-5000)) : -1;
                                for (int a = 0; a < fullReport.size(); a++) {
                                    textAreaReport.append(fullReport.get(a));
                                    if(showProgressBar) {
                                        if(Math.floorMod(System.nanoTime()-startTime, updateFactor) == 0) { // aleatório, não tempo
                                            progress = 95 + 5*a/fullReportSize;
                                            progressBar.setValue(progress);
                                            progressBar.setString(progress+"%");
                                            progressBar.setStringPainted(true);
                                            progressBar.update(progressBar.getGraphics());
                                        }
                                    }
                                }
                                textAreaReport.setCaretPosition(0);
                                if(showProgressBar) {
                                    progressBar.setValue(100);
                                    progressBar.setString(100+"%");
                                    progressBar.setStringPainted(true);
                                    progressBar.update(progressBar.getGraphics());
                                }
                                labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
                            }
                        });
                    }
                    case 1 -> { // input: digitar
                        executeSingle();
                    }
                    case 2 -> { // input: aleatória
                        executeSingle();
                    }
                }
                if (comboWichInput.getSelectedIndex() == 0) {
                    textAreaResult.setText("");
                }
                textAreaReport.setCaretPosition(0);
                //if(writeToFile) {
                //    outputFile.close();
                //}
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
                    textAreaReport.setCaretPosition(0);
                }
                catch (FileNotFoundException | UnsupportedEncodingException ex) {
                }
            }
        });
        
        editor.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                previousEditor = editor.getText();
                if (writeToFile) {
                    try {
                        setFileToWrite("Quine-McCluskey Results.txt");
                    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                        Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(editor.getText().isBlank()) {
                    editor.setText((String) comboExpressions.getSelectedItem());
                }
                //errorMsg = "";
                hasResult = true;
                executeSingle();
                //if(writeToFile) {
                //    outputFile.close();
                //}
            }
        });
        
        comboExpressions.addActionListener(new ActionListener() {
            // Isso é apenas para limpar os resultados
            // quando se seleciona com o mouse
            // pelo histórico uma nova expressão
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!editor.getText().equals(previousEditor)){
                    textAreaResult.setText("");
                    textAreaReport.setText("");
                }
            }
        });
    }
    
    public void wichResultsToFile() {
        dialog = new JDialog(SwingUtilities.windowForComponent(quineMcPanel));
        dialog.setName("dialog");
        dialog.setModal(true);
        JPanel panel = new JPanel();
        GridLayout gridLayout = new GridLayout(5,1);
        panel.setLayout(gridLayout);

        checkInputExp  = new JCheckBox("Expressão de entrada");
        checkResultExp = new JCheckBox("Expressão minimizada");
        checkHexa      = new JCheckBox("Representação hexadecimal");
        checkNumLit    = new JCheckBox("Número de literais");
        JButton ok     = new JButton("OK");
        checkInputExp .setSelected(true);
        checkResultExp.setSelected(true);
        checkHexa     .setSelected(true);
        checkHexa     .setSelected(true);
        checkNumLit   .setSelected(true);
        panel.add(checkInputExp);
        panel.add(checkResultExp);
        panel.add(checkHexa);
        panel.add(checkNumLit);
        panel.add(ok);
        
        for(int c=0; c<panel.getComponentCount(); c++) {
            panel.getComponent(c).addKeyListener(this);
        }
        
        dialog.add(panel);
        dialog.pack();
        dialog.setSize(new Dimension(200, 180));
        dialog.setResizable(false);
        
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeInputExp = checkInputExp.isSelected();
                writeResultExp = checkResultExp.isSelected();
                writeHexa = checkHexa.isSelected();
                writeNumLit = checkNumLit.isSelected();
                dialog.dispose();
            }
        });
        dialog.setVisible(false);
    }
    
    public void showDialog() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Point pointer = new Point(checkWriteToFile.getLocation());
        SwingUtilities.convertPointToScreen(pointer, checkWriteToFile);
        pointer.x -= 1180;
        pointer.x = Math.min(Math.max(1, pointer.x - dialog.getWidth()/2), (int) dim.getWidth());
        pointer.y = Math.min(pointer.y, dim.height - dialog.getHeight() - 100);
        dialog.setLocation(pointer);
        dialog.setVisible(true);
    }
    
    public void optimizeExpressions(
                String inputExpression,
                int numVars,
                boolean updateScreen,
                boolean showProgressBar,
                boolean lastLine
                ) throws Exception {

        expressions = Tools.removeSpacesFromExpression(inputExpression);
        sumOfProducts = new SumOfProducts(progressBar);
        if (!sumOfProducts.setExpression(expressions, numVars))
            return;
        
        if(updateScreen) {
            textAreaReport.append("\n\n » Ordenando e mesclando "
                    + sumOfProducts.getProductsList().size() + " produtos...");
            textAreaReport.update(textAreaReport.getGraphics());
        }
        
        sumOfProducts.sortByOnesCount();
        sumOfProducts.mergePrimeImplicants(sumOfProducts.getNumberOfVars());
        
        if(updateScreen) {
            textAreaReport.append(" Pronto.");
            textAreaReport.update(textAreaReport.getGraphics());
        }        
        sumOfProducts.fillMinTermsList();
        sumOfProducts.fillTruthTable();
        sumOfProducts.fillFinalProductsLists();
        sumOfProducts.completeFinalList(updateScreen, showProgressBar, textAreaReport);
        sumOfProducts.buildOptimizedExpression();
        
        if (writeToFile) {
            writeResultsToFile(writeInputExp,
                writeResultExp,
                writeHexa,
                writeNumLit,
                lastLine);
        }
    }
    
    public void executeSingle() {
        labelTime.setText("Tempo:        ");
        labelTime.update(labelTime.getGraphics());
        textAreaResult.setText("Processando...");
        textAreaReport.setText("...");
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    long startTime = System.nanoTime();
                    //labelTime.setText("Tempo:        ");
                    optimizeExpressions(
                            (String) comboExpressions.getSelectedItem(),
                            sliderVars.getValue(),
                            updateScreen,
                            showProgressBar,
                            true
                    );
                    textAreaReport.setFont(fontReport);
                    if (errorMsg.isEmpty()) {
                        String results;
                        results = sumOfProducts.getResult();
                        textAreaResult.setText(results);
                        textAreaReport.setText(reportText(comboWichReport));
                    } else {
                        textAreaResult.setText(errorMsg);
                        textAreaReport.setText("-");
                    }
                    //textAreaResult.update(textAreaResult.getGraphics());
                    //textAreaReport.update(textAreaReport.getGraphics());
                    String currentSelectedExp = (String) comboExpressions.getSelectedItem();
                    if (!(Arrays.asList(getHistory())
                            .contains(currentSelectedExp))
                            && currentSelectedExp != null) {
                        comboExpressions.insertItemAt(currentSelectedExp, 0);
                        comboExpressions.setSelectedIndex(0);
                        comboExpressions.update(comboExpressions.getGraphics());
                        addToHistory(currentSelectedExp);
                    }
                    labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime) / 1000000000));
                } catch (Exception ex) {
                    Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public int executeSingleFromFile(String expression, int lineIndex, long startTime, boolean lastLine) {
        try {
            optimizeExpressions(expression, numVars, false, false, lastLine);
        } catch (Exception ex) {
            Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
        }
        fullReport.add("\n" + lineIndex + "\t");
        lineIndex++;

        String result = sumOfProducts.getResult();
        String formattedResult = result + ' ';
        for (int c = result.length(); c < 80; c++) {
            formattedResult += '.';
        }
        fullReport.add(formattedResult + " ");

        String hexa = sumOfProducts.expression2hexadecimal(sumOfProducts.getResult());
        String formattedHexa = hexa + ' ';
        for (int c = hexa.length(); c < 20; c++) {
            formattedHexa += '.';
        }
        fullReport.add(formattedHexa + " ");

        String numLit = String.valueOf(Tools.numberOfLiterals(sumOfProducts.getResult(), sumOfProducts.getNumberOfVars(), sumOfProducts.getNumberOfProducts()));
        String formattedNumLit = "";
        for (int c = 0; c < (3 - numLit.length()); c++) {
            formattedNumLit += ' ';
        }
        formattedNumLit += numLit;
        fullReport.add(formattedNumLit + "\n");

        //Exibe atualização a cada X iterações
        if(updateScreen) {
            if (Math.floorMod(lineIndex, 500) == 0) {
                textAreaReport.setText(
                    "ÍNDICE\t"
                    + "EXPRESSÃO MINIMIZADA\n"
            );
                textAreaReport.append("\n" + lineIndex + "\t" + result + " ...");
                textAreaReport.update(textAreaReport.getGraphics());
                labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime) / 1000000000));
                labelTime.update(labelTime.getGraphics());
            }
        }
        return lineIndex;
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
    
    public void setFileToWrite(String outputFileName) throws
        FileNotFoundException,
        UnsupportedEncodingException {
        outputFile = new PrintWriter(outputFileName);
    }
    
    public void writeResultsToFile(
            boolean printIn,
            boolean printResult,
            boolean printHexa,
            boolean printNumLit,
            boolean lastLine) {
        boolean hasPreviousData = false;
        if(printIn) {
            print(sumOfProducts.getOriginalInputExpression(), outputFile);
            hasPreviousData = true;
        }
        if(printResult) {
            if(hasPreviousData)
                print(",", outputFile);
            print(sumOfProducts.getResult(), outputFile);
            hasPreviousData = true;
        }
        if(printHexa) {
            if(hasPreviousData)
                print(",", outputFile);
            print(sumOfProducts.expression2hexadecimal(sumOfProducts.getResult()), outputFile);
            hasPreviousData = true;
        }
        if(printNumLit) {
            if(hasPreviousData)
                print(",", outputFile);
            print(Tools.numberOfLiterals(sumOfProducts.getResult(), sumOfProducts.getNumberOfVars(), sumOfProducts.getNumberOfProducts()), outputFile);
            hasPreviousData = true;
        }
        print("\n", outputFile);
        if(lastLine) {
            outputFile.close();
        }
    }
    
    public void openOutputFile(String outputFileName)  throws
        FileNotFoundException,
        UnsupportedEncodingException {
        
        File fileOut = new File(
            outputFileName
        );
        try {
            Desktop.getDesktop().open(fileOut);
        }
        catch (IOException ex) {
        }
    }
    
    public String reportText(JComboBox comboWichReport) throws FileNotFoundException, UnsupportedEncodingException {
        String out = "";
        switch ((String)comboWichReport.getSelectedItem()) {
            case "Relatório" -> {
                out += sumOfProducts.getBasicReport();
            }
            case "Tabela Verdade" -> {
                out += sumOfProducts.getTruthTable();
            }
            case "Mintermos e seus Produtos" -> {
                out += sumOfProducts.getProductsFromMinTerms();
            }
            case "Produtos e seus Mintermos" -> {
                out += sumOfProducts.getMinTermsFromProducts();
            }
            case "Tabela de Cobertura" -> {
                out += sumOfProducts.getCoverageTable();
            }
            default -> {
                out = sumOfProducts.getBasicReport();
            }
        }
        return out;
    }
    
    public String[] getHistory() throws FileNotFoundException {
        File selectedFile = new File("history");
        if(!selectedFile.exists()){
            return new String[0];
        }
        Scanner sc = new Scanner(selectedFile);
        ArrayList<String> arr = new ArrayList<>();
        int i = 0;
        while(sc.hasNext()) {
            arr.add(sc.nextLine());
            i++;
        }
        String[] history = new String[arr.size()];
        history = arr.toArray(history);
        return history;
    }
    
    public void addToHistory(String newLine) throws FileNotFoundException {
        String[] previousHistory = getHistory();
        PrintWriter newHistoryFile = new PrintWriter("history");
        newHistoryFile.print(newLine + "\n");
        for (String previousHistoryLine : previousHistory) {
            //if (!newLine.equals(previousHistoryLine)) {
                newHistoryFile.print(previousHistoryLine + "\n");
            //}
        }
        newHistoryFile.close();
    }
    
    /*
    tabbedPane.getComponent(0).addPropertyChangeListener(new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("foreground".equals(evt.getPropertyName())){
    */
    
    public void print(Object obj) {
        System.out.print(obj);
    }
    
    public Object printr(Object obj) {
        System.out.print(obj);
        return obj;
    }
    
    public Object print(Object obj, PrintWriter w) {
        w.print(obj);
        return obj;
    }

    public static void printArray(ArrayList array) {
        System.out.println();
        for (int i=0; i < array.size(); i++) {
            System.out.println(array.get(i));
        }
    }
    
    public static void printDoubleArray(ArrayList<ArrayList<Object>> array) {
        for(int i = 0; i < array.size(); i++) {
            System.out.print("\n");
            for(int j = 0; j < array.get(i).size(); j++) {
                if (!array.get(i).isEmpty()) {
                    System.out.print(array.get(i).get(j) + "\t");
                }
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ("Editor".equals(e.getComponent().getName())) {
            if(e.getKeyCode() != KeyEvent.VK_CONTROL
               &&
               e.getKeyCode() != KeyEvent.VK_ALT
               &&
               e.getKeyCode() != KeyEvent.VK_SHIFT
               &&
               e.getKeyCode() != KeyEvent.VK_LEFT
               &&
               e.getKeyCode() != KeyEvent.VK_RIGHT
               &&
               e.getKeyCode() != KeyEvent.VK_UP
               &&
               e.getKeyCode() != KeyEvent.VK_DOWN
               &&
               e.getKeyCode() != KeyEvent.VK_TAB) {
                textAreaResult.setText("");
                textAreaReport.setText("");
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if(dialog.isVisible()) {
                writeInputExp = checkInputExp.isSelected();
                writeResultExp = checkResultExp.isSelected();
                writeHexa = checkHexa.isSelected();
                writeNumLit = checkNumLit.isSelected();
                dialog.dispose();
            }
            else {
                print("\n");
                System.exit(0);
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(dialog != null) {
                if(dialog.isVisible()) {
                    dialog.dispose();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
