package ePortfolio;

/**
 * Represents a single investment
 */
public abstract class Investment {

    InputChecking inputChecker = new InputChecking();

    public static double stdCommission = 9.99;
    public static double mutualSellCom = 45;

    private String investSymbol;
    private String investName;
    private int investQuantity;
    private double investPrice;
    private double investBookValue;

    public Investment()
    {

    }

    /**
     * Constructor that houses the values of an investment, parent to mutualfund and stock classes
     * @param symbol
     * @param name
     * @param quantity
     * @param price
     * @param bookValue
     */
    public Investment(String symbol, String name, int quantity, double price, double bookValue) throws Exception
    {   
        if(symbol == null || symbol.isEmpty())
        {
            System.out.println("Error: invalid symbol input");
            throw new Exception();
        }
        if(name == null || name.isEmpty())
        {
            System.out.println("Error: invalid name input");
            throw new Exception();
        }
        if( quantity < 1)
        {
            System.out.println("Error: invalid quantity input");
            throw new Exception();
        }
        if(price  <= 0)
        {
            System.out.println("Error: invalid price input");
            throw new Exception();
        }
        if(bookValue <= 0)
        {
            System.out.println("Error: invalid bookvalue input");
            throw new Exception();
        }

        investSymbol = symbol;
        investName = name;
        investQuantity = quantity;
        investPrice = price;
        investBookValue = bookValue;
    }
    
     
    @Override
    public boolean equals(Object otherObject)
    {
        if(otherObject == null)
        {
            return false;
        }
        else if(getClass() != otherObject.getClass())
        {
            return false;
        }
        else
        {
            Investment otherInvestment = (Investment)otherObject;
            return(investSymbol.equals(otherInvestment.investSymbol) && investName.equals(otherInvestment.investName));
        }
    }
    
    @Override
    public String toString()
    {
        return "Symbol: " + investSymbol + " Name: " + investName + " Quantity: " + investQuantity + " Price: " + investPrice + " Bookvalue: " + String.format("%.2f", investBookValue) + "\n";
    }

    /**
     * getInvestSymbol returns the symbol of the investment
     * @return object's symbol
     */
    public String getInvestSymbol() {
        return investSymbol;
    }

    /**
     * sets the investment's symbol to the string given by investSymbol
     * @param investSymbol
     */
    public void setInvestSymbol(String investSymbol) {
        this.investSymbol = investSymbol;
    }

    /**
     * getInvestName returns the name of the investment
     * @return object's name
     */
    public String getInvestName() {
        return investName;
    }

    /**
     * sets the investment's name to the string given by investName
     * @param investName
     */
    public void setInvestName(String investName) {
        this.investName = investName;
    }

    /**
     * getInvestQuantity returns the quantity of the investment
     * @return object's quantity
     */
    public int getInvestQuantity() {
        return investQuantity;
    }

    /**
     * sets the investment's quantity to the integer given by investQuantity
     * @param investQuantity
     */
    public void setInvestQuantity(int investQuantity) {
        this.investQuantity = investQuantity;
    }

    /**
     * returns the investment's price
     * @return object's price
     */
    public double getInvestPrice() {
        return investPrice;
    }

    /**
     * sets the price of the investment using the double investPrice
     * @param investPrice
     */
    public void setInvestPrice(double investPrice) {
        this.investPrice = investPrice;
    }

    /**
     * returns the bookvalue of the investment
     * @return object's bookvalue
     */
    public double getInvestBookValue() {
        return investBookValue;
    }

    /**
     * sets the bookvalue of the investment using the double investBookValue
     * @param investBookValue
     */
    public void setInvestBookValue(double investBookValue) {
        this.investBookValue = investBookValue;
    }

    public abstract double calcBookValue(int addedQuantity, double newPrice);
    public abstract double calcGain();
    public abstract double calcPayment(int soldQuantity);
}  