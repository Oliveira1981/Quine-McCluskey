package rodrigo.rosabinary.quinemccluskey;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author Rodrigo Rosa
 */
public class QuineMcCluskey implements KeyListener {
    
    public String                inputFormat;
    public ArrayList<String>     expressions;
    public int                       numVars;
    public boolean                 hasResult;
    public String                   errorMsg;
    public ArrayList<SumOfProducts> sopsList;
    public PrintWriter            outputFile;
    public boolean        writeResultsToFile;
    public JButton                  okButton;
    public JLabel             labelVariables;
    public JLabel             labelThemeDark;
    public JProgressBar          progressBar;
    public boolean                 darkTheme;
    
    public String[] wichInput = {
        "Carregar expressões de um arquivo...",
        "Digitar expressão",
        "Gerar expressão aleatória"
    };
    
    public String[] wichReport = {
        "Relatório",
        "Tabela Verdade",
        "Mintermos e seus Produtos",
        "Produtos e seus Mintermos",
        "Tabela de Cobertura"
    };
    
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
        String[] oldHistory = getHistory();
        PrintWriter newHistoryFile = new PrintWriter("history");
        newHistoryFile.print(newLine + "\n");
        for (String oldHistoryLine : oldHistory) {
            //if (!newLine.equals(oldHistoryLine)) {
                newHistoryFile.print(oldHistoryLine + "\n");
            //}
        }
        newHistoryFile.close();
    }
    
    public QuineMcCluskey() {
        inputFormat        = "";
        numVars            = 0;
        hasResult          = false;
        errorMsg           = "";
        sopsList           = null;
        writeResultsToFile = !true;
        //expressions        = new ArrayList<>();
    }
    
    public String getInputFormat(){
        return inputFormat;
    }
    
    public ArrayList<String> getExpression(){
        return expressions;
    }
    
    public JPanel quineMcPanel (boolean theme) throws FileNotFoundException {
        
        this.darkTheme = theme;
        
        Color textBGColor         , textColor,
              buttonBGColor       , buttonTextColor,
              comboBGColor        , comboTextColor,
              labelColor          , disabledLabelColor,
              readEntireFileColor , caretColor
        ;
        
        if(darkTheme) {
            textBGColor         = new Color( 44,  44,  44);
            textColor           = new Color( 50, 150, 250);
            buttonBGColor       = new Color( 30,  50, 100);
            buttonTextColor     = new Color( 50, 150, 250);
            comboBGColor        = new Color( 70,  73,  75);//70, 73, 75 (flatlaf defaut)
            comboTextColor      = new Color( 30, 130, 230);
            labelColor          = new Color( 30, 130, 230);
            disabledLabelColor  = new Color(110, 110, 110);
            readEntireFileColor = new Color( 30, 130, 230);
            caretColor          = new Color( 30, 120, 255);
        }
        else {
            textBGColor         = new Color(222, 222, 222);
            textColor           = new Color( 10, 80, 170);
            buttonBGColor       = new Color(199, 222, 244);
            buttonTextColor     = new Color( 50, 150, 250);
            comboBGColor        = new Color(248, 248, 248);
            comboTextColor      = new Color( 30, 130, 230);
            labelColor          = new Color( 30, 130, 230);
            disabledLabelColor  = new Color(188, 188, 188);
            readEntireFileColor = new Color( 30, 130, 230);
            caretColor          = new Color( 30, 120, 255);
        }
        
        JPanel quineMcPanel = new JPanel(new GridBagLayout());
        
        Font fontDefault = new Font("Segoe UI", Font.BOLD, 13);
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
        
        JComboBox<String> comboWichInput = new JComboBox<>(wichInput);
        comboWichInput.setSelectedIndex(1); // Digitar
        comboWichInput.setPreferredSize(new Dimension(220, 30));
        comboWichInput.setMinimumSize(new Dimension(220, 30));
        comboWichInput.addKeyListener(this);
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
        
        JCheckBox checkReadEntireFile = new JCheckBox("Inteiro");
        checkReadEntireFile.setFont(fontDefault);
        checkReadEntireFile.setForeground(labelColor);//30, 130, 230
        checkReadEntireFile.addKeyListener(this);
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
        
        JLabel labelStartLine = new JLabel("     Linha inicial: ");
        labelStartLine.setFont(fontDefault);
        labelStartLine.setForeground(disabledLabelColor);//110, 110, 110
        labelStartLine.addKeyListener(this);
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
        
        JTextField textStartLine = new JTextField();
        textStartLine.setPreferredSize(new Dimension(55, 20));
        textStartLine.setMinimumSize(new Dimension(55, 20));
        textStartLine.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textStartLine.addKeyListener(this);
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
        
        JLabel labelEndLine = new JLabel("     Linha final: ");
        labelEndLine.setFont(fontDefault);
        labelEndLine.setForeground(disabledLabelColor);//110, 110, 110
        labelEndLine.addKeyListener(this);
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
        
        JTextField textEndLine = new JTextField();
        textEndLine.setForeground(labelColor);
        textEndLine.setPreferredSize(new Dimension(55, 20));
        textEndLine.setMinimumSize(new Dimension(55, 20));
        textEndLine.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textEndLine.addKeyListener(this);
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
        labelVariables.setFont(fontDefault);
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
        
        JLabel labelThemeLight = new JLabel("Claro");
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
        sliderTheme.addKeyListener(this);
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
        quineMcPanel.add(labelThemeDark, gbcLabelThemeDark);
        
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
        
        JComboBox<String> comboExpressions = new JComboBox<>(getHistory());
        comboExpressions.setPreferredSize(new Dimension(500, 30));
        comboExpressions.setMinimumSize(new Dimension(500, 30));
        comboExpressions.setEditable(true);
        JTextField editor = (JTextField) comboExpressions.getEditor().getEditorComponent();
        editor.addKeyListener(this);
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
        okButton.addKeyListener(this);
        okButton.setFocusable(true);
        okButton.setBackground(buttonBGColor);//30, 50, 100
        okButton.setForeground(buttonTextColor);//50, 150, 250
        okButton.setFont(fontDefault);
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
        
        JSlider sliderVars = new JSlider(JSlider.HORIZONTAL, 0, 16, 0); // min, max, inicial
        sliderVars.addKeyListener(this);
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
        
        JLabel labelResult = new JLabel("Expressão Minimizada:");
        labelResult.setFont(fontDefault);
        labelResult.setForeground(labelColor);
        GridBagConstraints gbcLabelResult = new GridBagConstraints();
	gbcLabelResult.fill = GridBagConstraints.NONE;
	gbcLabelResult.gridx = 1;
	gbcLabelResult.gridy = 5;
	gbcLabelResult.gridwidth = 14;
	gbcLabelResult.gridheight = 1;
        gbcLabelResult.weightx = 0.0;
        gbcLabelResult.weighty = 0.0;
        gbcLabelResult.anchor = GridBagConstraints.WEST;
	labelResult.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(labelResult, gbcLabelResult);
        
        JTextArea textAreaResult = new JTextArea();
        textAreaResult.addKeyListener(this);
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
        
        JComboBox<String> comboWichReport = new JComboBox<>(wichReport);
        comboWichReport.setPreferredSize(new Dimension(250, 30));
        comboWichReport.setMinimumSize(new Dimension(250, 30));
        comboWichReport.addKeyListener(this);
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
        
        JLabel labelResultsFromFile = new JLabel(" Resultados:");
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
        
        JTextField labelTime = new JTextField(" ");
        labelTime.setEditable(false);
        labelTime.setMinimumSize(new Dimension(250, 30));
        labelTime.setFont(new Font("Consolas", Font.PLAIN, 14));
        labelTime.setForeground(labelColor);
        labelTime.setHorizontalAlignment(SwingConstants.RIGHT);
        GridBagConstraints gbcLabelTime = new GridBagConstraints();
        gbcLabelTime.fill = GridBagConstraints.BOTH;
	gbcLabelTime.gridx = 12;
	gbcLabelTime.gridy = 8;
	gbcLabelTime.gridwidth = 3;
	gbcLabelTime.gridheight = 1;
        gbcLabelTime.weightx = 0.0;
        gbcLabelTime.weighty = 0.0;
        gbcLabelTime.anchor = GridBagConstraints.WEST;
	labelTime.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(labelTime, gbcLabelTime);
        
        JTextArea textAreaReport = new JTextArea();
        textAreaReport.addKeyListener(this);
        textAreaReport.setFocusable(true);
        textAreaReport.setLineWrap(true);
        textAreaReport.setEditable(false);
        textAreaReport.setBackground(textBGColor);
        textAreaReport.setForeground(textColor);
        Font fontReport = new Font("Consolas", Font.PLAIN, 16);
        textAreaReport.setFont(fontReport);
        Insets mReport = new Insets(10, 10, 10, 10);
        textAreaReport.setMargin(mReport);
        
        JScrollPane scrollReport = new JScrollPane(textAreaReport);
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
	gbcSpaces.gridx = 12;
	gbcSpaces.gridy = 10;
	gbcSpaces.gridwidth = 3;
	gbcSpaces.gridheight = 1;
        gbcSpaces.weightx = 0.0;
        gbcSpaces.weighty = 0.0;
        progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 6));
        progressBar.setIndeterminate(!true);
        progressBar.setValue(0);
	progressBar.setBorder(BorderFactory.createLineBorder(borderColor));
        quineMcPanel.add(progressBar, gbcSpaces);
        
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
                    quineMcPanel.setForeground(Color.green);
                    
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
                    quineMcPanel.setForeground(Color.red);
                    
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
                comboExpressions.setSelectedIndex(-1);
                errorMsg = "";
                switch (comboWichInput.getSelectedIndex()) {
                    case 1 -> { // input: digitar
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
                                vars+vars*2, //numberOfProducts 
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
                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(comboWichInput);
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
                labelTime.setText("Tempo:        ");
                labelTime.update(labelTime.getGraphics());
                //long startTime = System.nanoTime();
                if (writeResultsToFile) {
                    try {
                        setFileToWrite("Quine-McCluskey Results.txt");
                    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                        Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                hasResult = true;
                switch (comboWichInput.getSelectedIndex()) {
                    case 1 -> { // input: digitar
                        textAreaResult.setText("Processando...");
                        textAreaReport.setText("...");
                        //textAreaResult.update(textAreaResult.getGraphics());
                        //textAreaReport.update(textAreaReport.getGraphics());
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            
                            @Override
                            public void run() {
                                try {
                                    long startTime = System.nanoTime();
                                    labelTime.setText("Tempo:        ");
                                    //labelTime.update(labelTime.getGraphics());
                                    //SwingUtilities.updateComponentTreeUI(textAreaReport);
                                    optimizeExpressions(
                                            (String) comboExpressions.getSelectedItem(),
                                            sliderVars.getValue()
                                    );
                                    textAreaReport.setFont(fontReport);
                                    String currentSelectedExp = (String) comboExpressions.getSelectedItem();
                                    if (!(Arrays.asList(getHistory()))
                                        .contains(
                                            currentSelectedExp)) {
                                        comboExpressions.insertItemAt(currentSelectedExp, 0);
                                        comboExpressions.setSelectedIndex(0);
                                        comboExpressions.update(comboExpressions.getGraphics());
                                        addToHistory(currentSelectedExp);
                                    }
                                    if (errorMsg.isEmpty()) {
                                        String results;
                                        results = sopsList.get(0).getResult();
                                        for (int r = 1; r < sopsList.size(); r++) {
                                            results += ";\n" + sopsList.get(r).getResult();
                                        }
                                        textAreaResult.setText(results);
                                        textAreaReport.setText(reportText(comboWichReport));
                                    }
                                    else {
                                        textAreaResult.setText(errorMsg);
                                        textAreaReport.setText("-");
                                    }
                                    //textAreaReport.update(textAreaReport.getGraphics());
                                    labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
                                    //labelTime.update(labelTime.getGraphics());
                                    //quineMcPanel.repaint();
                                } catch (Exception ex) {
                                    Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }
                    case 2 -> { // input: aleatória
                        textAreaResult.setText("Processando...");
                        textAreaReport.setText("...");
                        //textAreaResult.update(textAreaResult.getGraphics());
                        //textAreaReport.update(textAreaReport.getGraphics());
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            
                            @Override
                            public void run() {
                                try {
                                    long startTime = System.nanoTime();
                                    labelTime.setText("Tempo:        ");
                                    //labelTime.update(labelTime.getGraphics());
                                    //SwingUtilities.updateComponentTreeUI(textAreaReport);
                                    optimizeExpressions(
                                            (String) comboExpressions.getSelectedItem(),
                                            sliderVars.getValue()
                                    );
                                    textAreaReport.setFont(fontReport);
                                    String currentSelectedExp = (String) comboExpressions.getSelectedItem();
                                    if (!(Arrays.asList(getHistory()))
                                        .contains(
                                            currentSelectedExp)) {
                                        comboExpressions.insertItemAt(currentSelectedExp, 0);
                                        comboExpressions.setSelectedIndex(0);
                                        comboExpressions.update(comboExpressions.getGraphics());
                                        addToHistory(currentSelectedExp);
                                    }
                                    if (errorMsg.isEmpty()) {
                                        String results;
                                        results = sopsList.get(0).getResult();
                                        for (int r = 1; r < sopsList.size(); r++) {
                                            results += ";\n" + sopsList.get(r).getResult();
                                        }
                                        textAreaResult.setText(results);
                                        textAreaReport.setText(reportText(comboWichReport));
                                    }
                                    else {
                                        textAreaResult.setText(errorMsg);
                                        textAreaReport.setText("-");
                                    }
                                    //textAreaReport.update(textAreaReport.getGraphics());
                                    labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
                                    //labelTime.update(labelTime.getGraphics());
                                    //quineMcPanel.repaint();
                                } catch (Exception ex) {
                                    Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }
                    case 0 -> { // input: arquivo
                        textAreaReport.setText("Processando...");
                        //textAreaReport.update(textAreaReport.getGraphics());
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            
                            @Override
                            public void run() {
                                long startTime = System.nanoTime();
                                labelTime.setText("Tempo:        ");
                                //labelTime.update(labelTime.getGraphics());
                                //SwingUtilities.updateComponentTreeUI(textAreaReport);
                                
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
                                //if (line < startLine) {
                                //    print("\nSkipping line(s)...\n");
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
                                //print("\nReading...");
                                    
                                textAreaReport.setFont(new Font("Consolas", Font.PLAIN, 14));
                                //textAreaReport.setText("");
                                int count = line;
                                ArrayList<String> fullReport = new ArrayList<>();
                                if (endLine == -1) { // LER ATÉ O FINAL DO ARQUIVO
                                    while (sc.hasNext()) {
                                        try {
                                            //print("\nline:"+line++);
                                            optimizeExpressions(sc.nextLine(), numVars);
                                        } catch (Exception ex) {
                                            Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                            
                                        //Apenas uma expressão por linha
                                        int x = 0;
                                            
                                        //Aceita mais de uma expressão por linha
                                        //for (int x = 0; x < expressions.size(); x++) {
                                            
                                            //textAreaReport.append("\n" + count + "\t");
                                            fullReport.add("\n" + count + "\t");
                                            count++;
                                                
                                            String result = sopsList.get(x).getResult();
                                            String formattedResult = result + ' ';
                                            for (int c = result.length(); c < 80; c++) {
                                                formattedResult += '.';
                                            }
                                            //textAreaReport.append(formattedResult + " ");
                                            fullReport.add(formattedResult + " ");
                                                
                                            String hexa = sopsList.get(x).expression2hexadecimal(sopsList.get(0).getResult());
                                            String formattedHexa = hexa + ' ';
                                            for (int c = hexa.length(); c < 20; c++) {
                                                formattedHexa += '.';
                                            }
                                            //textAreaReport.append(formattedHexa + " ");
                                            fullReport.add(formattedHexa + " ");
                                                
                                            //String numLit = String.valueOf(SumOfProducts.numberOfLiterals(sopsList.get(0).getResult(), sopsList.get(0).getNumberOfVars(), sopsList.get(0).getNumberOfProducts()));
                                            String numLit = String.valueOf(Tools.numberOfLiterals(sopsList.get(x).getResult(), sopsList.get(x).getNumberOfVars(), sopsList.get(x).getNumberOfProducts()));String formattedNumLit = "";
                                            for (int c = 0; c < (3 - numLit.length()); c++) {
                                                formattedNumLit += ' ';
                                            }
                                            formattedNumLit += numLit;
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
                                        //}
                                    }
                                }
                                else {
                                    while (line <= endLine) {
                                        try {
                                            //print("\nline:"+line);
                                            optimizeExpressions(sc.nextLine(), numVars);
                                        } catch (Exception ex) {
                                            Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                            
                                        //Apenas uma expressão por linha
                                        int x = 0;
                                            
                                        //Aceita mais de uma expressão por linha
                                        //for (int x = 0; x < expressions.size(); x++) {
                                            
                                            //textAreaReport.append("\n" + count + "\t");
                                            fullReport.add("\n" + count + "\t");
                                            count++;
                                            
                                            String result = sopsList.get(x).getResult();
                                            String formattedResult = result + ' ';
                                            for (int c = result.length(); c < 80; c++) {
                                                formattedResult += '.';
                                            }
                                            //textAreaReport.append(formattedResult + " ");
                                            fullReport.add(formattedResult + " ");
                                                
                                            String hexa = sopsList.get(x).expression2hexadecimal(sopsList.get(x).getResult());
                                            String formattedHexa = hexa + ' ';
                                            for (int c = hexa.length(); c < 20; c++) {
                                                formattedHexa += '.';
                                            }
                                            //textAreaReport.append(formattedHexa + " ");
                                            fullReport.add(formattedHexa + " ");
                                                
                                            //String numLit = String.valueOf(SumOfProducts.numberOfLiterals(sopsList.get(0).getResult(), sopsList.get(0).getNumberOfVars(), sopsList.get(0).getNumberOfProducts()));
                                            String numLit = String.valueOf(Tools.numberOfLiterals(sopsList.get(x).getResult(), sopsList.get(x).getNumberOfVars(), sopsList.get(x).getNumberOfProducts()));
                                            String formattedNumLit = "";
                                            for (int c = 0; c < (3 - numLit.length()); c++) {
                                                formattedNumLit += ' ';
                                            }
                                            formattedNumLit += numLit;
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
                                        //}
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
                                textAreaReport.setCaretColor(caretColor);
                                textAreaReport.getCaret().setVisible(true);
                                for (int a = 0; a < fullReport.size(); a++) {
                                    textAreaReport.append(fullReport.get(a));
                                }
                                textAreaReport.setCaretPosition(0);
                                labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
                                //quineMcPanel.repaint();
                            }
                        });
                    }
                }
                if (comboWichInput.getSelectedIndex() == 0) {
                    textAreaResult.setText("");
                }
                textAreaReport.setCaretPosition(0);
                if(writeResultsToFile) {
                    outputFile.close();
                }
                //labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
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
                            }
                            catch (UnsupportedEncodingException ex) {
                            }
                        }
                    }
                    else {
                        textAreaReport.setText("-");
                    }
                    textAreaReport.setCaretPosition(0);
                }
                catch (FileNotFoundException ex) {
                }
            }
        });
        
        editor.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                //long startTime = System.nanoTime();
                textAreaResult.setText("Processando...");
                textAreaReport.setText("...");
                labelTime.setText("Tempo:        ");
                labelTime.update(labelTime.getGraphics());
                //textAreaResult.update(textAreaResult.getGraphics());
                //textAreaReport.update(textAreaReport.getGraphics());
                if(editor.getText().isBlank()) {
                    editor.setText((String) comboExpressions.getSelectedItem());
                }
                if (writeResultsToFile) {
                    try {
                        setFileToWrite("Quine-McCluskey Results.txt");
                    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                        Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                hasResult = true;
                errorMsg = "";
                SwingUtilities.invokeLater(new Runnable() {
                    
                    @Override
                    public void run() {
                        long startTime = System.nanoTime();
                        //SwingUtilities.updateComponentTreeUI(textAreaReport);
                        try {
                            optimizeExpressions(
                                (String) comboExpressions.getSelectedItem(),
                                sliderVars.getValue()
                            );
                        } catch (Exception ex) {
                            Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (errorMsg.isEmpty()) {
                            String results;
                            if (sopsList.size() > 1) {
                                results = "";
                            }
                            else {
                                results = sopsList.get(0).getResult();
                            }
                            textAreaResult.setText(results);
                            try {
                                textAreaReport.setText(reportText(comboWichReport));
                            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                                Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else {
                            textAreaResult.setText(errorMsg);
                            textAreaReport.setText("-");
                        }
                        try {
                            String currentSelectedExp = (String) comboExpressions.getSelectedItem();
                            if (!(Arrays.asList(getHistory()))
                                .contains(
                                    currentSelectedExp)) {
                                comboExpressions.insertItemAt(currentSelectedExp, 0);
                                comboExpressions.setSelectedIndex(0);
                                comboExpressions.update(comboExpressions.getGraphics());
                                addToHistory(currentSelectedExp);
                            }
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(QuineMcCluskey.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        textAreaReport.setCaretPosition(0);
                        //textAreaReport.update(textAreaReport.getGraphics());
                        labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
                        //labelTime.update(labelTime.getGraphics());
                        //quineMcPanel.repaint();
                    }
                });
                if(writeResultsToFile) {
                    outputFile.close();
                }
                //labelTime.setText(String.format("Tempo: %.3f s", (float) (System.nanoTime() - startTime)/1000000000));
                //labelTime.update(labelTime.getGraphics());
                //System.out.printf("\nTime elapsed: %.3f s", (float) elapsedTime/1000000000);
            }
        });
        
        return quineMcPanel;
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
        
        File selectedFile = new File(filePath);
        if(!selectedFile.exists()){
            print("\nNo file selected.\n");
            return 1;
        }
        Scanner sc = new Scanner(selectedFile);
        
        int line = 1;
        if (line < startLine) {
            print("\nSkipping line(s)...\n");
        }
        while (line < startLine) {
            sc.nextLine();
            line++;
        }
        print("\nReading...");
        
        if (endLine == -1) { // LER ATÉ O FINAL DO ARQUIVO
            while (sc.hasNext()) {
                optimizeExpressions(sc.nextLine(), numVars);
            }
        }
        else {
            while (line <= endLine) {
                //print("\nLine " + line + "\t"); //LEVA MUITO MAIS TEMPO SE FICAR MOSTRANDO A LINHA
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
        outputFile = new PrintWriter(outputFileName);
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
                try {
                    for (int r=0; r < sopsList.size(); r++) {
                        out += sopsList.get(r).getBasicReport();
                    }
                }
                catch (UnsupportedEncodingException ex) {
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
                }
                catch (UnsupportedEncodingException ex) {
                }
            }
        }
        return out;
    }
    
    public void optimizeExpressions(
        String allExpressions,
        int numVars
        ) throws Exception {
        
        //SumOfProducts sopsList = new SumOfProducts();
        sopsList = new ArrayList<>();
        expressions = new ArrayList<>();
        
        allExpressions = Tools.removeSpacesFromExpression(allExpressions);
        int begin = 0;
        int end;
        do {
            end = allExpressions.indexOf(';', begin);
            //end = -1; //ACEITAR APENAS UMA EXPRESSÃO POR LINHA
            if (end < 0) {
                end = allExpressions.length();
            }
            expressions.add(allExpressions.substring(begin, end));
            //expression = allExpressions;
            sopsList.add(new SumOfProducts(progressBar));
            int lastExpressionIndex = expressions.size()-1;
            int lastSOPIndex = sopsList.size()-1;
            //sopsList.get(lastSOPIndex).setExpression(expression, numVars);
            if (!sopsList.get(lastSOPIndex).setExpression(expressions.get(lastExpressionIndex), numVars)) {
                begin = end + 1;
                continue;
            }
            sopsList.get(lastSOPIndex).sortByOnesCount();
            sopsList.get(lastSOPIndex).mergePrimeImplicants(10);
            sopsList.get(lastSOPIndex).fillMinTermsList();
            sopsList.get(lastSOPIndex).fillTruthTable();
            sopsList.get(lastSOPIndex).fillFinalProductsLists();
            
            sopsList.get(lastSOPIndex).completeFinalList();
            //sopsList.get(lastSOPIndex).setNumberOfLiteralsList();
            //generateAllCombinations(sopsList.get(lastSOPIndex).getNEPLSize());
            //System.exit(0);
            
            sopsList.get(lastSOPIndex).buildOptimizedExpression();
            
            if (writeResultsToFile) {
                print(sopsList.get(lastSOPIndex).getResult()+"\t", outputFile);
                print(sopsList.get(lastSOPIndex).expression2hexadecimal(sopsList.get(lastSOPIndex).getResult())+"\t", outputFile);
                print(Tools.numberOfLiterals(sopsList.get(lastSOPIndex).getResult(), sopsList.get(lastSOPIndex).getNumberOfVars(), sopsList.get(lastSOPIndex).getNumberOfProducts()), outputFile);
                //print("\t"+sopsList.get(lastSOPIndex).getNEPLSize(), outputFile);
                //if (sopsList.get(lastSOPIndex).isInspect())
                //    print(" < INSPECT! >", outputFile);
                print("\n", outputFile);
            }
            
            begin = end + 1;
            if (begin >= allExpressions.length()) {
                break;
            }
        }
        while (begin < allExpressions.length());
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
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            print("\n");
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    /*
    private ArrayList<ArrayList<Integer>> combinationsList;
    private int progress;
    
    // completeFinalList STEP 2 /////
    public void generateAllCombinations(int n) {
        long startTime = System.nanoTime();
        combinationsList = new ArrayList<>();
        for (int r = 1; r <= n; r++) {
            int[] combination = new int[r];
            
            // initialize with lowest lexicographic combination
            for (int i = 0; i < r; i++) {
                combination[i] = i;
            }
            
            while (combination[r - 1] < n) {
                combinationsList.add(new ArrayList<>());
                for (int i = 0; i < combination.length; i++) {
                    combinationsList.get(combinationsList.size()-1).add(combination[i]);
                }
                
                // generate next combination in lexicographic order
                int t = r - 1;
                while (t != 0 && combination[t] == n - r + t) {
                    t--;
                }
                combination[t]++;
                for (int i = t + 1; i < r; i++) {
                    combination[i] = combination[i - 1] + 1;
                }
            }
            progress = 100*r/n; //33
            //print("\r"+progress+"\tSTEP 2");
            progressBar.setValue(progress);
            progressBar.update(progressBar.getGraphics());
        }
        print(String.format("\ngenerateAllCombinations: %.5f s", (float) (System.nanoTime() - startTime)/1000000000));
        print("\n----------------------");
    }
    */
}
