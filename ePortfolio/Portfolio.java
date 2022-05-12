package ePortfolio;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Set;

/**
 * Portfolio class holds all of the methods for manipulating data as well the main for the program
 */
public class Portfolio {

    ArrayList<Investment> invests = new ArrayList<Investment>(); // creates a new arrayList to hold all of the investments
    HashMap<String, ArrayList<Integer>> wordIndex = new HashMap<String, ArrayList<Integer>>(); //creates a hashmap to associate keywords with indexes
    InputChecking inputObj = new InputChecking();
    static String[] fileName;

    /**
     * Main
     * @param args
     */
    public static void main(String[] args)
    {    
        fileName = args;
        PortfolioGUI gui = new PortfolioGUI();
        gui.setVisible(true);
    }

    /**
     * updates an existing investment by modifying the quantity and price of the investment as well as the bookvalue
     * @param input array of strings
     * @param invest investment which will be edited
     */
    public void updateInvestmentInfo(ArrayList<String> input, Investment invest) // updates a investments info
    {
        int quantity = Integer.parseInt(input.get(3));
        double price = Double.parseDouble(input.get(4));

        invest.setInvestBookValue(invest.calcBookValue(quantity, price)); // creates the new bookvalue
        invest.setInvestQuantity(invest.getInvestQuantity() + quantity); // adds the quantity purchased to the quantity already there
        invest.setInvestPrice(price); // updates the price 
    }

    /**
     * creates a new investment and adds it to the investment portfolio
     * @param input array of strings with new investment data
     * @param invests ArrayList holding all the previously bought investments
     * @param wordIndex Hashmap that keeps track of which words are associated with which investments
     */
    public void createNewInvestment(ArrayList<String> input, ArrayList<Investment> invests, HashMap<String, ArrayList<Integer>> wordIndex) throws Exception
    {
        String type = null;
        String symbol = null;
        String name = null;
        int quantity = 0;
        double price = 0;

        type = input.get(0);
        symbol = input.get(1);
        name = input.get(2); //takes in a string for the name of the investment
        quantity = Integer.parseInt(input.get(3));
        price = Double.parseDouble(input.get(4));

        if(type.equalsIgnoreCase("stock"))
        {
            Stock temp = new Stock();
            invests.add(new Stock(symbol, name, quantity, price, (temp.calcBookValue(quantity, price))));
        }
        else
        {
            Mutualfund temp = new Mutualfund();
            invests.add(new Mutualfund(symbol, name, quantity, price, (temp.calcBookValue(quantity, price)))); // populates a new investment object with values using a constructor in the array
        }
        
        addHashItem(wordIndex, invests.size() - 1, name); // adds the words in the title to the hashmap of words
    }

    /**
     * addHashItem adds or updates all the keyswords in a new invesments title
     * @param wordIndex Hashmap that keeps track of which words are associated with which investments 
     * @param index index of the investment that was just created
     * @param keywords name of the invesment
     */
    private void addHashItem(HashMap<String, ArrayList<Integer>> wordIndex, int index, String keywords)
    {
        String[] sepKeyWords = keywords.toLowerCase().split("[ ]+"); // splits the name into words separated by spaces

        for(int i = 0; i < sepKeyWords.length; i++) // loop runs through all of the keywords
        {
            ArrayList<Integer> temp = wordIndex.get(sepKeyWords[i]); // attempts to pull an arrayList of integers from the hashmap using the current keyword
            if(temp != null) // if it does pull an arraylist out
            {
                temp.add(index); // adds the index of the newly created investment to the arrayList associated with the current keyword
            }
            else // if it fails to pull an arrayList out
            {
                temp = new ArrayList<Integer>(); // creates a new arrayList for the current keyword
                temp.add(index); // adds the index of the newly created investment to the arrayList associated with the current keyword
            }
            wordIndex.put(sepKeyWords[i], temp); //places the arrayList into the hashMap at the current keyword, writing overtop of the previous arrayList
        }
    }

    /**
     * removeHashItem modifies the arrayLists associated with the keywords in the stock name so that hashMap no longer lists the index of those investments being associated with those keywords
     * @param wordIndex Hashmap that keeps track of which words are associated with which investments
     * @param element the index of the investment that is being removed on the investment arrayList
     * @param keywords name of the invesment
     */
    private void removeHashItem(HashMap<String, ArrayList<Integer>> wordIndex, int element, String keywords)
    {
        String[] sepKeyWords = keywords.toLowerCase().split("[ ]+"); // splits the name into words separated by spaces

        for(int i = 0; i < sepKeyWords.length; i++) // loop runs through all of the keywords 
        {
            ArrayList<Integer> temp = wordIndex.get(sepKeyWords[i]); // attempts to pull an arrayList of integers from the hashmap using the current keyword
            int index = temp.indexOf(element); // finds the index of the investment inside the arrayList of integers representing the indexes of the investments in the investment arrayList associated with the keyword
            if (index == -1)
            {
                System.out.println("Error: hashtable does not contain index of keyword");
            }
            temp.remove(index); // removes the integer representing the index of the investment
            if(temp.isEmpty())
            {
                wordIndex.remove(sepKeyWords[i]);
            }
            else
            {
                wordIndex.put(sepKeyWords[i], temp); // puts the arraylist of integers back into the hashmap
            }
        }
        Set<String> wrdsInHash = wordIndex.keySet();
        for(String currentKey:wrdsInHash) //loop goes through each word in the hashmap
        {
            ArrayList<Integer> temp = wordIndex.get(currentKey); // gets the arraylist associated with the word
            for(int i = 0; i < temp.size(); i++) // loops through each element in the array list
            {
                if(temp.get(i) == element) //if the element in the arraylist is the same as the element specified
                {
                    temp.remove(i); // remove the element (shouldn't ever need to happen as that is done above)
                }
                else if(temp.get(i) > element) // if the element in the arraylist is larger than the element specified, decrement by 1
                {
                    temp.set(i, temp.get(i) - 1);
                }
            }
            wordIndex.put(currentKey, temp); // puts the arraylist of integers back into the hashmap
        }
    }

    /**
     * sells a specified quantity and changed the price of an invesment entered by the user
     * @param input array of strings with investment info
     * @param invests Arraylist of investments
     * @param wordIndex Hashmap that keeps track of which words are associated with which investments 
     */
    public String sellInvestment(ArrayList<String> inputs, ArrayList<Investment> invests, HashMap<String, ArrayList<Integer>> wordIndex)
    {
        boolean isInvest = false;
        int sellQuantity = 0;
        double sellPrice = 0;

        sellQuantity = Integer.parseInt(inputs.get(1));
        sellPrice = Double.parseDouble(inputs.get(2));

        for(Investment currentInvest:invests) // increments through each the investment array
        {
            if(currentInvest.getInvestSymbol().equals(inputs.get(0))) // checks if the current investment is the same as the user entered investment
            {
                isInvest = true;
                if(currentInvest.getInvestQuantity() < sellQuantity) // if the user specified a higher quantity than they own (incorrect)
                {
                    return "The quantity entered exceeds the quantity owned! Please re-input symbol, quantity and price.";
                }
                else
                {
                    int newQuantity = (currentInvest.getInvestQuantity() - sellQuantity); // gets the new quantity after the sell
                    currentInvest.setInvestPrice(sellPrice); // gets the new price specified by the user
                    currentInvest.setInvestBookValue((newQuantity / currentInvest.getInvestQuantity()) * currentInvest.getInvestPrice()); // calculates the new bookValue based on the amount you own
                    currentInvest.setInvestQuantity(newQuantity); // sets the new quantity

                    if (currentInvest.getInvestQuantity() == 0) // if at the end of the sell the quantity you own is zero it erases the investment from the list
                    {
                        int index = invests.indexOf(currentInvest); // gets the index of the current intvestment out of the ArrayList of investments
                        removeHashItem(wordIndex, index, currentInvest.getInvestName()); // used to remove the indexes associated with the keywords of the investment that has been removed
                        invests.remove(currentInvest); // removes the investment from the arraylist
                    }
                    return "Payment Recieved: " + String.format("%.2f", currentInvest.calcPayment(sellQuantity)); // prints the payment recieved
                }
            }
        }
        if (isInvest == false) // if it is not a investment or a mutualfund
        {
            return "There is no investment with that name!";
        }
        return null;
    }
    
    /**
     * updateInvest prints all of the investments out and prompts the user for a new price for each investment
     * @param invests array of investments
     * @param newPrice user specified new price
     * @param desiredIndex index in the arrayList of investments that the user would like to edit
     */
    public void updateInvest(ArrayList<Investment> invests, double newPrice, int desiredIndex) // updates the price of every stock and mutualfund
    {
        invests.get(desiredIndex).setInvestPrice(newPrice); // sets the stocks price to newPrice
    }

    /**
     * Takes a string from the command line and attempts to open a file with it
     * @param args string from command line
     * @return null if it fails to open the file, returns a file if it opens correctly
     */
    public File openFile(String[] args)
    {
        try
        {
            File inputFile = new File(args[0]);
            return inputFile;
        }
        catch(Exception e)
        {
            System.out.println("Warning: Could not open file, may not have specified a file.");
            return null;
        }
    }

    /**
     * reads the data given by a formatted file
     * @param invests ArrayList of investments
     * @param wordIndex Hashmap that keeps track of which words are associated with which investments
     * @param inputFile File previously opened by openFile
     * @throws Exception if the creation of a new investment has invalid values
     */
    public void readFile(ArrayList<Investment> invests, HashMap<String, ArrayList<Integer>> wordIndex, File inputFile) throws Exception
    {

        Scanner fileScan = null;

        if(inputFile.length() == 0) // checks if the file is empty
        {
            System.out.println("Warning: Opened file is empty!");
            return;
        }
        else // if the file is not empty
        {
            try
            {                        
                fileScan = new Scanner(inputFile); //creates a scanner to scan the file
            }
            catch(Exception e)  // if the file could not be opened
            {
                System.out.println("Error: Could not create file scanner");
            }
            int i = 0;
            int arrInsertIndex = 0;
            String[] investmentData = new String[6]; //used to hold the data of an investment from the file
            while(fileScan.hasNextLine()) // while there is another line to read
            {
                boolean validInput = true;
                String lineText = fileScan.nextLine(); // takes a line of text from the file
                String[] usefulData = lineText.split("[\"]+"); // separates the line by " 
                investmentData[i] = usefulData[1]; // copies the data in the second argument into the investmentData array
                i++;
                if(i == 6) // once all of an investments data has been read
                {
                    //validInput = inputChecker.inputParser(investmentData, 3, 1, 2); // checks that each argument is of the correct data type
                    for(Investment current:invests)
                    {
                        if(current.getInvestSymbol().equals(investmentData[1]))
                        {
                            validInput = false;
                            break;
                        }
                    }
                    if(!validInput || (Integer.parseInt(investmentData[3]) < 1) || (Double.parseDouble(investmentData[4]) <= 0) || (Double.parseDouble(investmentData[5]) <= 0)) // if any arguments are of the wrong data type
                    {
                        System.out.println("Error: Investment block has invalid data, moving to next block.");
                        validInput = false;
                    }
                    else // To-Do: add hashtable functionality to readFile
                    { 
                        if(investmentData[0].equalsIgnoreCase("stock")) // if the investment is a stock
                        {
                            invests.add(new Stock(investmentData[1], investmentData[2], Integer.parseInt(investmentData[3]), Double.parseDouble(investmentData[4]), Double.parseDouble(investmentData[5])));
                        }
                        if(investmentData[0].equalsIgnoreCase("mutualfund")) //if the investment is a mutualfund
                        {
                            invests.add(new Mutualfund(investmentData[1], investmentData[2], Integer.parseInt(investmentData[3]), Double.parseDouble(investmentData[4]), Double.parseDouble(investmentData[5])));
                        }
                        addHashItem(wordIndex, arrInsertIndex, investmentData[2]); // adds the newly added investment to the hashmap
                        arrInsertIndex++; // used with the HashMap
                    }
                    if(fileScan.hasNextLine())
                    {
                        lineText = fileScan.nextLine(); // gets the \n separating each investment block
                    }
                    i = 0; // sets i to 0 to reset for next loop
                }

            }
        }
            fileScan.close(); // closes file scanner 
    }

    /**
     * populates a file with formatted data of every investment the investment array holds before the program finishes
     * @param invests arraylist of investments
     * @param file previously opened file that may or may not have been read from
     */
    public void populateFile(ArrayList<Investment> invests, File file)
    {
        try
        {
            PrintWriter fileWriter = new PrintWriter(file, "UTF-8"); //used to print into the file
            for(Investment temp:invests)
            {
                String type = null;
                if(temp instanceof Stock) // if the investment is a stock
                {
                    type = "stock";
                }
                if(temp instanceof Mutualfund) // if the investment is a mutualfund
                {
                    type = "mutualfund";
                }
                fileWriter.println("type = \"" + type + "\"");
                fileWriter.println("symbol = \"" + temp.getInvestSymbol() + "\"");
                fileWriter.println("name = \"" + temp.getInvestName() + "\"");
                fileWriter.println("quantity = \"" + temp.getInvestQuantity() + "\"");
                fileWriter.println("price = \"" + temp.getInvestPrice() + "\"");
                fileWriter.println("bookValue = \"" + temp.getInvestBookValue() + "\"\n");
            }
            fileWriter.close(); // closes the file reader
        }
        catch(Exception e)
        {
            System.out.println("Error: could not open file.");
        }

    }

    /**
     * search searches through the arraylist of investments for any investments that match the user specified values, uses a hashmap to make searches using keywords much more efficent
     * @param invests arraylist of investments
     * @param wordIndex Hashmap that keeps track of which words are associated with which investments 
     * @param symbol symbol of investment
     * @param keywordString string of keywords separated by spaces
     * @param low string specifying the lowest value the price can be 
     * @param high string specifying the highest value the price can be
     * @param matchingInvests arraylist of investments found to be matches
     * @return false if an error occurs, true if now errors occur
     */
    public boolean search(ArrayList<Investment> invests, HashMap<String, ArrayList<Integer>> wordIndex, String symbol, String keywordString, String low, String high, ArrayList<Investment> matchingInvests)
    {
        double priceLow = 0;
        double priceHigh = 0;
        ArrayList<Integer> elemsWKey = null; // used to hold a list of integers associated with the first keyword enteredt by the user

        boolean useSym = true; // tells the program to try searching through symbols    
        boolean useKey = true; // tells the program to try searching through keywords
        boolean usePrice = true; // tells the program to try searching through prices

        //BELOW IS SYMBOL CHECKING

        if(symbol.length() == 0)
        {
            useSym = false;
        }
        else
        {
            String[] tempArr = symbol.split("[ ]+");
            if(tempArr.length > 1)
            {
                return false;
            }
        } 

        //BELOW IS KEYWORD CHECKING
        boolean keywordDNE = false;
        
        if(keywordString.length() == 0)
        {
            useKey = false;
        }
        else
        {
            String[] keywords = keywordString.toLowerCase().split("[ ]+");
            useKey = true;

            elemsWKey = wordIndex.get(keywords[0]); // gets the ArrayList associated with the first keyword entered by the user
            if(elemsWKey != null)
            {
                for(int i = 1; i < keywords.length; i++) // loops the number of keyword after the first
                {
                    ArrayList<Integer> temp = wordIndex.get(keywords[i]); // assigns temp to the keyword i in the keyword array
                    if(temp == null) // if the keyword specified does not exist in the hashmap
                    {
                        keywordDNE = true;
                        break; //breaks out of for loop
                    }
                    elemsWKey.retainAll(temp); //finds the intersection of the elements between elemsWKey and temp
                }
            }
            if(elemsWKey == null)
            {
                keywordDNE = true;
            }
        }

        //BELOW IS PRICE CHECKING
        boolean noPriceCap = false; // used to keep track of if the user sets a price cap (ex. 10-)

        if((low.length() == 0) && (high.length() == 0)) // if the user does not specify a low or high
        {
            usePrice = false;
        }
        else if (low.length() != 0 && high.length() == 0) // if the user specifies a low but no high
        {
            try
            {
                priceLow = Double.parseDouble(low);
                noPriceCap = true;
                usePrice = true;
            }
            catch(Exception e)
            {
                return false;
            }
        }
        else if (low.length() == 0 && high.length() != 0) // if the user specifies a high but no low
        {
            try
            {
                priceHigh = Double.parseDouble(high);
                priceLow = 0;
                usePrice = true;
            }
            catch(Exception e)
            {
                return false;
            }
        }
        else // if the user specifies a low and a high
        {
            try
            {
                priceHigh = Double.parseDouble(high);
                priceLow = Double.parseDouble(low);
                usePrice = true;
            }
            catch(Exception e)
            {
                return false;
            }
        }

        boolean validPrice = true;
        boolean validSym = true;

        if(useKey) // if the elemsWKey array is not empty and the keywords have been checked
        {
            if(!keywordDNE)
            {
                for(int i = 0; i < elemsWKey.size(); i++)
                {
                    validPrice = true;
                    validSym = true;
                    Investment temp = invests.get(elemsWKey.get(i));
                    if (useSym == true)
                    {
                        if (temp.getInvestSymbol().equals(symbol) == false)
                        {
                            validSym = false;
                        }
                    }
                    if(usePrice == true) // if the search is using price
                    {
                        if(temp.getInvestPrice() > priceLow && noPriceCap == true) // if the stocks price is greater than the input and the user input a low with no cap
                        {
                            validPrice = true;
                        }
                        else if((noPriceCap == false) && (temp.getInvestPrice() > priceLow) && (temp.getInvestPrice() <= priceHigh)) // if the stocks price is between the low and high and there is no price cap
                        {
                            validPrice = true;
                        }
                        else // if it does not meet any of those criteria 
                        {
                            validPrice = false;
                        }
                    }
                    if(validSym == true && validPrice == true) // if the stock passes all user input checks it is printed
                    {
                    matchingInvests.add(temp);
                    }
                }
            }
            else
            {
                return true;
            }
        }
        else
        {
            for (Investment temp:invests)
            {
                validPrice = true;
                validSym = true;

                if(useSym == true) // if the search include symbols
                {
                    if(temp.getInvestSymbol().equalsIgnoreCase(symbol) == false) // if the current symbol is not the same as the inputted symbol
                    {
                        validSym = false;
                    }
                }
                if(usePrice == true) // if the search is using price
                {
                    if(temp.getInvestPrice() > priceLow && noPriceCap == true) // if the stocks price is greater than the input and the user input a low with no cap
                    {
                        validPrice = true;
                    }
                    else if((noPriceCap == false) && (temp.getInvestPrice() > priceLow) && (temp.getInvestPrice() <= priceHigh)) // if the stocks price is between the low and high and there is no price cap
                    {
                        validPrice = true;
                    }
                    else // if it does not meet any of those criteria 
                    {
                        validPrice = false;
                    }
                }
                if(validSym == true && validPrice == true) // if the stock passes all user input checks it is printed
                {
                    matchingInvests.add(temp);
                }
            }
        }
        return true;
    }
}
