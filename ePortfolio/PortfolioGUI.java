package ePortfolio;

import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class PortfolioGUI extends JFrame 
{
    InputChecking inputObj = new InputChecking(); // object used for interacting with inputChecking methods
    Portfolio portObj = new Portfolio(); // object used used for interacting with Portfolio methods and values

    private int desiredIndex = 0;
    private File inputFile = null;
    
    private SwapPanel swapper; // object used to swap between panels
    private JPanel cPanel; // holds the current panel being displayed
    private JPanel nPanel; // holds the value of the panel is being switched too

    private JMenuBar menuBar;
    private JMenu commandMenu;
    private JMenuItem createInvest, sellInvest, updateInvest, gain, searching, exit;

    private JButton buyReset, buy, sellReset, sell, prev, next, save, searchReset, search;
    private JTextField symbolInput, nameInput, quanInput, priceInput, totalGainOutput, highInput, lowInput;
    private JTextArea outputDisplay;
    private JComboBox<String> combobox; 

    private JLabel headerLabel;
    private GridBagConstraints gbc; // holds the grid bag constraints
    
    public PortfolioGUI()
    {
        super();
        createInitGui();
    }

    /**
     * createInitGUI creates the initial window, menu, and loads data from the file specified in the command line
     */
    private void createInitGui()
    {
        setTitle("Student GUI");
        setSize(550, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new CheckOnExit());

        menuBar = new JMenuBar();
        swapper = new SwapPanel();

        //creates command menu
        commandMenu = new JMenu("Commands");
        //buy invest button
        createInvest = new JMenuItem("Create an Investment");
        createInvest.addActionListener(swapper);
        commandMenu.add(createInvest);
        //sell invest button
        sellInvest = new JMenuItem("Sell an Investment");
        sellInvest.addActionListener(swapper);
        commandMenu.add(sellInvest);
        //updateInvest button
        updateInvest = new JMenuItem("Update existing Investments");
        updateInvest.addActionListener(swapper);
        commandMenu.add(updateInvest);
        //getGain button
        gain = new JMenuItem("Display gains");
        gain.addActionListener(swapper);
        commandMenu.add(gain);
        //search button
        searching = new JMenuItem("Search Investments");
        searching.addActionListener(swapper);
        commandMenu.add(searching);
        //exit button
        exit = new JMenuItem("Exit the program");
        exit.addActionListener(new Exit());
        commandMenu.add(exit);

        menuBar.add(commandMenu);

        setJMenuBar(menuBar);

        loadFile();

        cPanel = createInitPanel();

        add(cPanel);
        cPanel.setVisible(true); 
    }

    /**
     * loadFile loads the data from the file specified in the command line and stores it in the array list
     */
    private void loadFile()
    {
        inputFile = portObj.openFile(Portfolio.fileName); // tries to open the file from the command line
        if(inputFile != null) // if openFile does not return null
        {
            try
            {
                portObj.readFile(portObj.invests, portObj.wordIndex, inputFile); // reads investment data from command line file
            }
            catch(Exception e)
            {
                outputDisplay.setText("Failed to read file: Exception thrown!");
            }
        }
    }

    /**
     * createInitPanel creates the welcome panel for the user
     * @return returns the panel created in createInitPanel
     */
    private JPanel createInitPanel()
    {
        JPanel initPanel = new JPanel();
        initPanel.setLayout(new BorderLayout());

        headerLabel = new JLabel("Welcome to ePortfolio", JLabel.CENTER);
        JTextArea infoLabel = new JTextArea("Choose a command from the \"Commands\" menu to buy and sell investments, update the prices of all investments, get the total gain across the portfolio, search for specific investments, or exit the program.");
        infoLabel.setLineWrap(true);
        infoLabel.setEditable(false);
        infoLabel.setMargin(new Insets(40, 30, 40, 30));

        initPanel.add(headerLabel, BorderLayout.NORTH);
        initPanel.add(infoLabel, BorderLayout.CENTER);

        return initPanel;
    }

    /**
     * createInvestPanel creates the panel that is used for purchasing investments
     * @return the panel created in createInvestPanel
     */
    private JPanel createInvestPanel()
    {
        setSize(600, 450);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,20,5,20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel investPanel = new JPanel(new GridBagLayout());
        investPanel.setPreferredSize(new Dimension(600, 450));
        JLabel titleLabel = new JLabel("Buying An Investment");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        investPanel.add(titleLabel, gbc);

        JPanel infoJPanel = new JPanel(new GridBagLayout());

        gbc.weightx = 0.2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel type = new JLabel("Type");
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoJPanel.add(type, gbc);
        JLabel symbolLabel = new JLabel("Symbol");
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoJPanel.add(symbolLabel, gbc);
        JLabel nameLabel = new JLabel("Name");
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoJPanel.add(nameLabel, gbc);
        JLabel quantityLabel = new JLabel("Quantity");
        gbc.gridx = 0;
        gbc.gridy = 3;
        infoJPanel.add(quantityLabel, gbc);
        JLabel priceLabel = new JLabel("Price");
        gbc.gridx = 0;
        gbc.gridy = 4;
        infoJPanel.add(priceLabel, gbc);
        
        String[] investTypes = {"Stock", "Mutualfund"};
        combobox = new JComboBox<String>(investTypes);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        infoJPanel.add(combobox, gbc);
        symbolInput = new JTextField();
        symbolInput.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        infoJPanel.add(symbolInput, gbc);
        nameInput = new JTextField();
        nameInput.setColumns(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        infoJPanel.add(nameInput, gbc);
        quanInput = new JTextField();
        quanInput.setColumns(5);
        gbc.gridx = 1;
        gbc.gridy = 3;
        infoJPanel.add(quanInput,gbc);
        priceInput = new JTextField();
        priceInput.setColumns(5);
        gbc.gridx = 1;
        gbc.gridy = 4;
        infoJPanel.add(priceInput,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;

        investPanel.add(infoJPanel, gbc);

        JPanel buttonJPanel = new JPanel(new GridLayout(2, 1, 50, 20));
        //buy and reset buttons
        buy = new JButton("Buy");
        buy.setPreferredSize(new Dimension(40, 20));
        buy.addActionListener(new Buy());
        buttonJPanel.add(buy, gbc);
        buyReset = new JButton("Reset");
        buyReset.setPreferredSize(new Dimension(40, 20));
        buyReset.addActionListener(new BuyRstLis());
        buttonJPanel.add(buyReset, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        investPanel.add(buttonJPanel, gbc);

        JPanel outputJPanel = new JPanel(new GridLayout(2,1));

        JLabel outputLabel = new JLabel("Output");
        outputJPanel.add(outputLabel);

        outputDisplay = new JTextArea(5, 40);
        outputDisplay.setEditable(false);
        JScrollPane scrollingText = new JScrollPane(outputDisplay);
        outputJPanel.add(scrollingText);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        gbc.gridwidth = 3;
        investPanel.add(outputJPanel, gbc);

        return investPanel;
    }

    /**
     * createSellPanel creates the panel that is used to sell investments
     * @return the panel created in createSellPanel
     */
    private JPanel createSellPanel()
    {
        setSize(600, 450);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,20,5,20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel sellPanel = new JPanel(new GridBagLayout());
        sellPanel.setPreferredSize(new Dimension(600, 450));
        JLabel titleLabel = new JLabel("Selling An Investment");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        sellPanel.add(titleLabel, gbc);
        //infoPanel starts
        JPanel infoJPanel = new JPanel(new GridBagLayout());

        gbc.weightx = 0.2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel type = new JLabel("Symbol");
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoJPanel.add(type, gbc);
        JLabel symbolLabel = new JLabel("Quantity");
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoJPanel.add(symbolLabel, gbc);
        JLabel nameLabel = new JLabel("Price");
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoJPanel.add(nameLabel, gbc);

        symbolInput = new JTextField();
        symbolInput.setColumns(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        infoJPanel.add(symbolInput, gbc);
        quanInput = new JTextField();
        quanInput.setColumns(5);
        gbc.gridx = 1;
        gbc.gridy = 1;
        infoJPanel.add(quanInput,gbc);
        priceInput = new JTextField();
        priceInput.setColumns(5);
        gbc.gridx = 1;
        gbc.gridy = 2;
        infoJPanel.add(priceInput,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        sellPanel.add(infoJPanel, gbc);
        //buttonPanel starts
        JPanel buttonJPanel = new JPanel(new GridLayout(2, 1, 50, 20));

        sell = new JButton("Sell");
        sell.setPreferredSize(new Dimension(40, 20));
        sell.addActionListener(new Sell());
        buttonJPanel.add(sell, gbc);
        sellReset = new JButton("Reset");
        sellReset.setPreferredSize(new Dimension(40, 20));
        sellReset.addActionListener(new SellRstLis());
        buttonJPanel.add(sellReset, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        sellPanel.add(buttonJPanel, gbc);

        JPanel outputJPanel = new JPanel(new GridLayout(2,1));

        JLabel outputLabel = new JLabel("Output");
        outputJPanel.add(outputLabel);

        outputDisplay = new JTextArea(5, 40);
        outputDisplay.setEditable(false);
        JScrollPane scrollingText = new JScrollPane(outputDisplay);
        outputJPanel.add(scrollingText);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        gbc.gridwidth = 3;
        sellPanel.add(outputJPanel, gbc);

        return sellPanel;
    }

    /**
     * createUpdatePanel creates the panel that is used to update the prices of the investments
     * @return the panel created in createUpdatePanel
     */
    private JPanel createUpdatePanel()
    {
        setSize(600, 450);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,20,5,20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel updatePanel = new JPanel(new GridBagLayout());
        updatePanel.setPreferredSize(new Dimension(600, 450));
        JLabel titleLabel = new JLabel("Update Existing Investments");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        updatePanel.add(titleLabel, gbc);
        //infoPanel starts
        JPanel infoJPanel = new JPanel(new GridBagLayout());

        gbc.weightx = 0.2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel symbolLabel = new JLabel("Symbol");
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoJPanel.add(symbolLabel, gbc);
        JLabel nameLabel = new JLabel("Name");
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoJPanel.add(nameLabel, gbc);
        JLabel priceLabel = new JLabel("Price");
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoJPanel.add(priceLabel, gbc);

        symbolInput = new JTextField();
        symbolInput.setColumns(20);
        symbolInput.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        infoJPanel.add(symbolInput, gbc);
        nameInput = new JTextField();
        nameInput.setColumns(5);
        nameInput.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        infoJPanel.add(nameInput,gbc);
        priceInput = new JTextField();
        priceInput.setColumns(5);
        gbc.gridx = 1;
        gbc.gridy = 2;
        infoJPanel.add(priceInput,gbc);

        if(!portObj.invests.isEmpty())
        {
            symbolInput.setText(portObj.invests.get(desiredIndex).getInvestSymbol());
            nameInput.setText(portObj.invests.get(desiredIndex).getInvestName());
            priceInput.setText(Double.toString(portObj.invests.get(desiredIndex).getInvestPrice()));
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        updatePanel.add(infoJPanel, gbc);
        //buttonPanel starts
        JPanel buttonJPanel = new JPanel(new GridLayout(3, 1, 50, 20));

        prev = new JButton("Prev");
        prev.setPreferredSize(new Dimension(40, 20));
        prev.addActionListener(new UpdatePrev());
        buttonJPanel.add(prev, gbc);
        next = new JButton("Next");
        next.setPreferredSize(new Dimension(40, 20));
        next.addActionListener(new UpdateNext());
        buttonJPanel.add(next, gbc);
        save = new JButton("Save");
        save.setPreferredSize(new Dimension(40, 20));
        save.addActionListener(new UpdateSave());
        buttonJPanel.add(save, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        updatePanel.add(buttonJPanel, gbc);

        //output Panel starts
        JPanel outputJPanel = new JPanel(new GridLayout(2,1));

        JLabel outputLabel = new JLabel("Output");
        outputJPanel.add(outputLabel);

        outputDisplay = new JTextArea(5, 40);
        outputDisplay.setEditable(false);
        JScrollPane scrollingText = new JScrollPane(outputDisplay);
        outputJPanel.add(scrollingText);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        gbc.gridwidth = 3;
        updatePanel.add(outputJPanel, gbc);

        return updatePanel;
    }

    /**
     * createGainPanel creates the panel that is used to find the total gain across all investments as well as individual gains
     * @return the panel created in createGainPanel
     */
    private JPanel createGainJPanel()
    {
        setSize(600, 470);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,20,5,20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel gainPanel = new JPanel(new GridBagLayout());
        gainPanel.setPreferredSize(new Dimension(600, 470));
        JLabel titleLabel = new JLabel("Getting Total Gain");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gainPanel.add(titleLabel, gbc);

        //infoPanel starts
        JPanel infoJPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel gainLabel = new JLabel("Total Gain:");
        infoJPanel.add(gainLabel, gbc);

        totalGainOutput = new JTextField();
        totalGainOutput.setColumns(20);
        totalGainOutput.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        infoJPanel.add(totalGainOutput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gainPanel.add(infoJPanel, gbc);

        JPanel outputJPanel = new JPanel(new GridLayout(2, 1));

        JLabel outputLabel = new JLabel("Output");
        outputJPanel.add(outputLabel);

        outputDisplay = new JTextArea(10, 40);
        outputDisplay.setEditable(false);
        JScrollPane scrollingText = new JScrollPane(outputDisplay);
        outputJPanel.add(scrollingText);

        getGain();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 3;
        gainPanel.add(outputJPanel, gbc);

        return gainPanel;
    }

    /**
     * createsSearchPanel creates the panel that is used for searching for specific investments
     * @return the panel created in createSearchPanel
     */
    private JPanel createSearchPanel()
    {
        setSize(600, 450);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,20,5,20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel searchPanel = new JPanel(new GridBagLayout());

        searchPanel.setPreferredSize(new Dimension(600, 450));
        JLabel titleLabel = new JLabel("Search Existing Investments");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        searchPanel.add(titleLabel, gbc);
        //infoPanel starts here
        JPanel infoJPanel = new JPanel(new GridBagLayout());

        gbc.weightx = 0.2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel symbolLabel = new JLabel("Symbol");
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoJPanel.add(symbolLabel, gbc);
        JLabel keywordsLabel = new JLabel("Keywords");
        gbc.gridx = 0;
        gbc.gridy = 2;
        infoJPanel.add(keywordsLabel, gbc);
        JLabel lowLabel = new JLabel("Low Price");
        gbc.gridx = 0;
        gbc.gridy = 3;
        infoJPanel.add(lowLabel, gbc);
        JLabel highLabel = new JLabel("High Price");
        gbc.gridx = 0;
        gbc.gridy = 4;
        infoJPanel.add(highLabel, gbc);

        symbolInput = new JTextField();
        symbolInput.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        infoJPanel.add(symbolInput, gbc);
        nameInput = new JTextField();
        nameInput.setColumns(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        infoJPanel.add(nameInput, gbc);
        lowInput = new JTextField();
        lowInput.setColumns(5);
        gbc.gridx = 1;
        gbc.gridy = 3;
        infoJPanel.add(lowInput,gbc);
        highInput = new JTextField();
        highInput.setColumns(5);
        gbc.gridx = 1;
        gbc.gridy = 4;
        infoJPanel.add(highInput,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        searchPanel.add(infoJPanel, gbc);
        //button panel starts here
        JPanel buttonJPanel = new JPanel(new GridLayout(2, 1, 50, 20));

        search = new JButton("Search");
        search .setPreferredSize(new Dimension(40, 20));
        search.addActionListener(new Search());
        buttonJPanel.add(search, gbc);
        searchReset = new JButton("Reset");
        searchReset.setPreferredSize(new Dimension(40, 20));
        searchReset.addActionListener(new SearchRstLis());
        buttonJPanel.add(searchReset, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        searchPanel.add(buttonJPanel, gbc);
        //output panel starts here
        JPanel outputJPanel = new JPanel(new GridLayout(2,1));

        JLabel outputLabel = new JLabel("Output");
        outputJPanel.add(outputLabel);

        outputDisplay = new JTextArea(5, 40);
        outputDisplay.setEditable(false);
        JScrollPane scrollingText = new JScrollPane(outputDisplay);
        outputJPanel.add(scrollingText);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        gbc.gridwidth = 3;
        searchPanel.add(outputJPanel, gbc);

        return searchPanel;
    }

    /**
     * SwapPanel is used to swap between two panels and is intitiated by the menu 
     */
    private class SwapPanel implements ActionListener //used to active panels
    {
        public void actionPerformed(ActionEvent event)
        {
            if(event.getSource() == createInvest) //if buy panel menu button is pressed
            {
                nPanel = createInvestPanel();
            }
            if(event.getSource() == sellInvest) //if sell panel menu button is pressed
            {
                nPanel = createSellPanel();
            }
            if(event.getSource() == updateInvest) //if update panel menu button is pressed
            {
                nPanel = createUpdatePanel();
            }
            if(event.getSource() == gain) //if gain panel menu button is pressed
            {
                nPanel = createGainJPanel();
            }
            if(event.getSource() == searching) //if search panel menu button is pressed
            {
                nPanel = createSearchPanel();
            }

            cPanel.setVisible(false); // makes the current panel invisible
            add(nPanel); // add the new panel to the frame
            nPanel.setVisible(true);// makes the new panel visible

            cPanel = nPanel; // makes the currently active panel equal to the new panel
        }
    }

    /**
     * BuyRstLis is used to clear the text fields in the buy panel
     */
    private class BuyRstLis implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            combobox.setSelectedIndex(0);
            symbolInput.setText("");
            nameInput.setText("");
            quanInput.setText("");
            priceInput.setText("");
        }
    }

    /**
     * Buy is used to buy an investment on the buy panel
     */
    private class Buy implements ActionListener // used if the user presses the buy button on the buy panel
    {
        public void actionPerformed(ActionEvent e)
        {
            boolean newInvest = true;
            ArrayList<String> input = new ArrayList<String>(); // creates an arraylist to hold entered values

            String type = (String)combobox.getSelectedItem();
            input.add(type);
            input.add(symbolInput.getText());
            input.add(nameInput.getText());
            input.add(quanInput.getText());
            input.add(priceInput.getText());

            if(inputObj.inputParser(input, 3, 1, 1)) //checks that all the values are usable
            {
                for(Investment currentInvestment:portObj.invests) // loops through arraylist of existing investments
                {
                    if(currentInvestment.getInvestSymbol().equals(input.get(1))) // if an investment matching the one entered exists already
                    {
                        portObj.updateInvestmentInfo(input, currentInvestment);
                        outputDisplay.setText("Existing Investment: You have bought " + input.get(3) + " more shares of " + currentInvestment.getInvestSymbol() + ". You now have: " + currentInvestment.getInvestQuantity() + " of " +currentInvestment.getInvestSymbol());
                        newInvest = false;
                    }
                }
                if(newInvest) // if the entered investment is new
                {
                    try
                    {
                        portObj.createNewInvestment(input, portObj.invests, portObj.wordIndex);
                    }
                    catch(Exception f)
                    {
                        outputDisplay.setText("Failed to create investment: Exception thrown!");
                    }
                    outputDisplay.setText("New Investment: You have bought " + input.get(3) + " shares of " + input.get(1) + " at " + input.get(4));
                }
            }
            else
            {
                outputDisplay.setText("Invalid input! Please reinput values.");
            }
        }
    }

    /**
     * SellRstLis clears the text fields in the sell panel
     */
    private class SellRstLis implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            symbolInput.setText("");
            quanInput.setText("");
            priceInput.setText("");
        }
    }

    /**
     * Sell sells an investment on the sell panel
     */
    private class Sell implements ActionListener //used if the user presses sell on the sell panel
    {
        public void actionPerformed(ActionEvent e) 
        {
            ArrayList<String> input = new ArrayList<String>(); // arraylist to hold user inputs

            input.add(symbolInput.getText());
            input.add(quanInput.getText());
            input.add(priceInput.getText());

            if(inputObj.inputParser(input, 1, 1, 1)) // checks to see if the user entered values are usable
            {
                outputDisplay.setText(portObj.sellInvestment(input, portObj.invests, portObj.wordIndex));
            }
            else
            {
                outputDisplay.setText("Invalid input! Please reinput values.");  
            }

        }
    }

    /**
     * UpdatePrev decrements the index of the investment being currently displayed and displays the new investment
     */
    private class UpdatePrev implements ActionListener //used if the user presses prev on the update panel
    {
        public void actionPerformed(ActionEvent e)
        {
            if(desiredIndex > 0) // if desired index is greater than 0
            {
                desiredIndex--;

                symbolInput.setText(portObj.invests.get(desiredIndex).getInvestSymbol());
                nameInput.setText(portObj.invests.get(desiredIndex).getInvestName());
                priceInput.setText(Double.toString(portObj.invests.get(desiredIndex).getInvestPrice()));
            }
            else
            {
                outputDisplay.setText("No previous investment!");
            }
        }
    }

    /**
     * UpdateNext increments the index of the investment being currently displayed and displays the new investment
     */
    private class UpdateNext implements ActionListener //used if the user presses next on the update panel
    {
        public void actionPerformed(ActionEvent e)
        {
            int arraySize = portObj.invests.size();

            if(desiredIndex < (arraySize - 1)) // if desired index is less than the size of the array of investments - 1
            {
                desiredIndex++;

                symbolInput.setText(portObj.invests.get(desiredIndex).getInvestSymbol());
                nameInput.setText(portObj.invests.get(desiredIndex).getInvestName());
                priceInput.setText(Double.toString(portObj.invests.get(desiredIndex).getInvestPrice()));
            }
            else
            {
                outputDisplay.setText("No next investment!");
            }
        }
    }

    /**
     * UpdateSave updates the price of the investment to the new value the in priceInput text field
     */
    private class UpdateSave implements ActionListener //used if the user presses save on the update panel
    {
        public void actionPerformed(ActionEvent e)
        {
            if(!portObj.invests.isEmpty())
            {
                double newPrice = 0;
                ArrayList<String> input = new ArrayList<String>(); //holds user inputed price as a string
    
                input.add(priceInput.getText()); // gets the user inputed price
    
                if(inputObj.inputParser(input, 0, 0, 1)) //checks if the price is usable
                {
                    newPrice = Double.parseDouble(input.get(0)); 
    
                    portObj.updateInvest(portObj.invests, newPrice, desiredIndex); //updates investment price
        
                    outputDisplay.setText(portObj.invests.get(desiredIndex).toString()); //prints investment info
                }
                else
                {
                    outputDisplay.setText("Invalid input! Please re-input values.");
                }
            }
            else
            {
                outputDisplay.setText("No investments to edit!");
            }
        }
    }

    /**
     * getGain prints the total gain across all invesments as well as individual gains
     */
    private void getGain() // used to display gains for investments on gain panel
    {
        double totalGain = 0;

        for(Investment currentInvestment:portObj.invests)
        {
            double currentGain = currentInvestment.calcGain();
            outputDisplay.append("Gain from " + currentInvestment.getInvestSymbol() + ": " + String.format("%.2f",currentGain) + "\n");
            totalGain += currentGain;
        }

        totalGainOutput.setText(String.format("%.2f", totalGain)); 
    }

    /**
     * SearchRstLis clears the text fields on the search panel
     */
    private class SearchRstLis implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            symbolInput.setText("");
            nameInput.setText("");
            lowInput.setText("");
            highInput.setText("");
        }
    }

    /**
     * Search searches for an investment that matches the values that were inputted by the user
     */
    private class Search implements ActionListener //used if the user presses search on the search panel
    {
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<Investment> matchingInvests  = new ArrayList<Investment>(); //holds the user entered values

            String symbol = symbolInput.getText();
            String keywords = nameInput.getText();
            String low = lowInput.getText();
            String high = highInput.getText();

            boolean result = portObj.search(portObj.invests, portObj.wordIndex, symbol, keywords, low, high, matchingInvests); //searches the investment array using the user entered value, error checking handled in search method

            if(matchingInvests.size() == 0 && result) // search finished and returned nothing in the array
            {
                outputDisplay.setText("No matching investments!");
            }
            else if(result) //search finished and did return something in the array
            {
                outputDisplay.setText("");
                for(Investment currentInvestment:matchingInvests)
                {
                    outputDisplay.append(currentInvestment.toString());
                }
            }
            else // search did not finish (error)
            {
                outputDisplay.setText("Invalid input! Please re-input values.");
            }
        }
    }

    /**
     * Exit exits the program when the user presses the correct menu option after it save the contents if the user specified a file
     */
    private class Exit implements ActionListener //used when the exit program menu option is clicked
    {
        public void actionPerformed(ActionEvent e)
        {
            dispose();
            setVisible(false);
            portObj.populateFile(portObj.invests, inputFile); //tries to populate file before exit
            System.exit(0);
        }
    }

    private class CheckOnExit implements WindowListener 
    {

        @Override
        public void windowOpened(WindowEvent e) {     
        }

        @Override
        public void windowClosing(WindowEvent e) { //used to populate file before exit
            dispose();
            setVisible(false);
            portObj.populateFile(portObj.invests, inputFile);
            System.exit(0);   
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) { 
        }

        @Override
        public void windowDeiconified(WindowEvent e) {  
        }

        @Override
        public void windowActivated(WindowEvent e) { 
        }

        @Override
        public void windowDeactivated(WindowEvent e) { 
        }
        
    }
}   
