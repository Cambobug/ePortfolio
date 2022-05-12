package ePortfolio;

public class Stock extends Investment{
    
    public Stock()
    {

    }

    /**
     * Constructor used to create a stock object, uses the superclasses investment constructor and methods for manipulation
     * @param symbol
     * @param name
     * @param quantity
     * @param price
     * @param bookValue
     */
    public Stock(String symbol, String name, int quantity, double price, double bookValue) throws Exception
    {
        super(symbol, name, quantity, price, bookValue);
    }

    @Override
    public double calcBookValue(int addedQuantity, double newPrice) 
    {
        return getInvestBookValue() + (newPrice * addedQuantity) + stdCommission;
    }
    @Override
    public double calcGain() 
    {
        return (getInvestPrice() * getInvestQuantity()) - getInvestBookValue();
    }
    @Override
    public double calcPayment(int soldQuantity) 
    {
        return (getInvestPrice() * soldQuantity) - stdCommission;
    }
}
